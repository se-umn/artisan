package de.unipassau.abc.generation;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import de.unipassau.abc.ABCUtils;

// Execute the give test classes, parse the results and returns a data structor for pass/fail tests
// XXXX THIS WORKS UNDER THE ASSUMPTION THAT THERE"S ONLY ONE TEST METHOD FOR EACH TEST CLASS !!
public class TestSuiteExecutor {

	private static final Logger logger = LoggerFactory.getLogger(TestSuiteExecutor.class);

	private List<File> projectJars;

	public TestSuiteExecutor(List<File> projectJars) {
		this.projectJars = new ArrayList<>(projectJars);
	}

	public String buildClassPath(File srcFolder) {
		///
		StringBuilder cpBuilder = new StringBuilder();
		// Include the class we are delta debugging
		cpBuilder.append(srcFolder.getAbsolutePath()).append(File.pathSeparator);
		// Include the project deps
		for (File jarFile : this.projectJars) {
			cpBuilder.append(jarFile.getAbsolutePath()).append(File.pathSeparator);
		}
		// Include JUnit
		cpBuilder.append(ABCUtils.buildJUnit4Classpath()).append(File.pathSeparator);
		// Include System rules for
		cpBuilder.append(ABCUtils.getSystemRulesJar()).append(File.pathSeparator);
		// Include XMLDumper
		cpBuilder.append(ABCUtils.getTraceJar()).append(File.pathSeparator);
		// Include XMLDumper
		for (File jarFile : ABCUtils.getXStreamJars()) {
			cpBuilder.append(jarFile.getAbsolutePath()).append(File.pathSeparator);
		}

		return cpBuilder.toString();
	}

	// XXXX THIS WORKS UNDER THE ASSUMPTION THAT THERE"S ONLY ONE TEST METHOD
	// FOR EACH TEST CLASS !!
	public Set<MethodDeclaration> executeAndReportFailedTests(Set<CompilationUnit> testClasses) {

		Set<MethodDeclaration> failedTests = new HashSet<>();
		Iterator<CompilationUnit> iterator = testClasses.iterator();
		while (iterator.hasNext()) {
			CompilationUnit testClass = iterator.next();
			if (!compileJUnitTest(testClass)) {
				// Accumulate the test methods in this call
				failedTests.addAll(getTestMethods(testClass));
				//
				iterator.remove();
			}
		}
		// At this point the testClasses set should contain only compilable
		// tests
		failedTests.addAll(compileAndRunJUnitTests(testClasses));
		//
		return failedTests;

	}

	private Set<MethodDeclaration> getTestMethods(CompilationUnit testClass) {
		final Set<MethodDeclaration> testMethods = new HashSet<>();
		testClass.accept(new VoidVisitorAdapter<Void>() {
			@Override
			public void visit(MethodDeclaration n, Void arg) {
				if (n.isAnnotationPresent(org.junit.Test.class)) {
					testMethods.add(n);
				}
				super.visit(n, arg);
			}
		}, null);

		return null;
	}

	private boolean compileJUnitTest(CompilationUnit junitClass) {
		try {
			File tempOutputDir = Files.createTempDirectory("DeltaDebug").toFile();
			tempOutputDir.deleteOnExit();
			boolean result = compileJUnitTest(junitClass, tempOutputDir);
			// Remove the folder
			// tempOutputDir.delete();

			return result;

		} catch (Throwable e) {
			logger.warn("Cannot compileJUnitTest " + junitClass.getTypes().get(0).getNameAsString(), e);
			return false;
		}

	}

	private boolean compileJUnitTest(CompilationUnit junitClass, File tempOutputDir) {
		String testClassName = junitClass.getType(0).getNameAsString();
		String packageDeclaration = junitClass.getPackageDeclaration().get().getNameAsString();

		try {
			// Create the structure as javac expects
			File packageFile = new File(tempOutputDir, packageDeclaration.replaceAll("\\.", File.separator));
			packageFile.mkdirs();

			// Store the testClassName into a File inside the tempOutputDir
			File classFile = new File(packageFile, testClassName + ".java");

			// Not nice to pass by String ...
			Files.write(classFile.toPath(), junitClass.toString().getBytes());

			// TODO Compilatoin is still one by one ...

			String classpath = buildClassPath(tempOutputDir);

			//
			final List<String> args = new ArrayList<>();
			args.add("-classpath");
			args.add(classpath);

			java.nio.file.Files.walkFileTree(tempOutputDir.toPath(), new SimpleFileVisitor<Path>() {
				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
					if (file.toString().endsWith(".java")) {
						args.add(file.toFile().getAbsolutePath());
					}
					return super.visitFile(file, attrs);
				}
			});

			JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
			int compilationResult = compiler.run(null, null, null, //
					args.toArray(new String[] {}));

			if (compilationResult != 0) {
				logger.debug("Compilation failed");
				return false;
			} else {
				return true;
			}
		} catch (Throwable e) {
			logger.warn("Cannot compile " + testClassName, e);
			return false;
		}
	}

	public Set<String> compileAndRunJUnitTests(Collection<CompilationUnit> testClasses)
			throws IOException, URISyntaxException, InterruptedException {
		final Set<String> failedTests = new HashSet<>();
		try {
			// https://javabeat.net/the-java-6-0-compiler-api/
			File tempOutputDir = Files.createTempDirectory("DeltaDebug").toFile();
			tempOutputDir.deleteOnExit();

			if (!compileJUnitTests(testClasses, tempOutputDir)) {
				throw new RuntimeException("Compilation should not fail at this point !!!");
			}

			// Now execute all of classes under tempOutputDir
			// Refactor

			String javaPath = SystemUtils.JAVA_HOME + File.separator + "bin" + File.separator + "java";

			// TODO Here it would be nice to execute a single test, but this
			// requires a custom runner that must be used instead of plain
			// JUnitCore
			// https://stackoverflow.com/questions/9288107/run-single-test-from-a-junit-class-using-command-line
			String classpath = buildClassPath(tempOutputDir);

			StringBuilder testClassNames = new StringBuilder();
			for (CompilationUnit testClass : testClasses) {
				if (testClassNames.length() != 0) {
					testClassNames.append(" ");
				}
				testClassNames.append(testClass.getPackageDeclaration().get().getNameAsString()).append(".")
						.append(testClass.getTypes().get(0).getNameAsString());
			}

			ProcessBuilder processBuilder = new ProcessBuilder(javaPath, "-cp", classpath, "org.junit.runner.JUnitCore",
					testClassNames.toString());

			logger.trace("DeltaDebugger.runJUnitTest() " + processBuilder.command());

			// Parse output ...

			Process process = processBuilder.start();
			try (
					BufferedReader reader = new BufferedReader (new InputStreamReader(process.getOutputStream()));){

				// Wait for the execution to end.
				if (!process.waitFor(4000 * testClasses.size(), TimeUnit.MILLISECONDS)) {
					logger.info("Timeout for test execution");
					// timeout - kill the process.
					process.destroyForcibly(); // consider using destroyForcibly
												// instead
					// Force Wait 1 sec to clean up
					Thread.sleep(1000);
				}

				// I hope that at this point we still have the stream ?
				while (read.hasNext()) {
					if (scanner.nextLine().matches("")) {
						// FQN OF THE FILE
						failedTests.add("");
					}
				}

			} catch (Throwable e) {
				e.printStackTrace();
				// Avoid resource leakeage. This should be IDEMPOTENT
				process.destroyForcibly();
				// TODO: handle exception
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	private boolean compileJUnitTests(Collection<CompilationUnit> testClasses, File tempOutputDir) {
		try {

			// TODO Factor this out
			// Create a file to compile for each test
			for (CompilationUnit junitClass : testClasses) {
				String testClassName = junitClass.getType(0).getNameAsString();
				String packageDeclaration = junitClass.getPackageDeclaration().get().getNameAsString();

				// Create the structure as javac expects
				File packageFile = new File(tempOutputDir, packageDeclaration.replaceAll("\\.", File.separator));
				packageFile.mkdirs();

				// Store the testClassName into a File inside the tempOutputDir
				File classFile = new File(packageFile, testClassName + ".java");

				// Not nice to pass by String ...
				Files.write(classFile.toPath(), junitClass.toString().getBytes());
			}

			String classpath = buildClassPath(tempOutputDir);

			// Now compile all of them together
			final List<String> args = new ArrayList<>();
			args.add("-classpath");
			args.add(classpath);

			java.nio.file.Files.walkFileTree(tempOutputDir.toPath(), new SimpleFileVisitor<Path>() {
				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
					if (file.toString().endsWith(".java")) {
						args.add(file.toFile().getAbsolutePath());
					}
					return super.visitFile(file, attrs);
				}
			});

			JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
			int compilationResult = compiler.run(null, null, null, //
					args.toArray(new String[] {}));

			if (compilationResult != 0) {
				logger.debug("Compilation failed");
				return false;
			} else {
				return true;
			}
		} catch (Throwable e) {
			logger.warn("Compilation failed ", e);
			return false;
		}
	}

	public boolean compileAndRunJUnitTest(//
			CompilationUnit junitClass, MethodDeclaration testMethod, //
			List<File> projectJars) throws IOException, URISyntaxException, InterruptedException {

		long time = System.currentTimeMillis();
		try {
			// https://javabeat.net/the-java-6-0-compiler-api/
			File tempOutputDir = Files.createTempDirectory("DeltaDebug").toFile();
			tempOutputDir.deleteOnExit();

			String classpath = buildClassPath(tempOutputDir);
			String testClassName = junitClass.getType(0).getNameAsString();
			String packageDeclaration = junitClass.getPackageDeclaration().get().getNameAsString();

			if (!compileJUnitTest(junitClass, tempOutputDir)) {
				logger.debug("Compilation failed");
				return false;
			}

			String javaPath = SystemUtils.JAVA_HOME + File.separator + "bin" + File.separator + "java";

			// TODO Here it would be nice to execute a single test, but this
			// requires a custom runner that must be used instead of plain
			// JUnitCore
			// https://stackoverflow.com/questions/9288107/run-single-test-from-a-junit-class-using-command-line
			ProcessBuilder processBuilder = new ProcessBuilder(javaPath, "-cp", classpath, "org.junit.runner.JUnitCore",
					packageDeclaration + "." + testClassName);

			// if (logger.isTraceEnabled()) {
			logger.trace("DeltaDebugger.runJUnitTest() " + processBuilder.command());
			// This causes problems wher run on command line
			processBuilder.inheritIO();
			// }

			Process process = processBuilder.start();

			// Wait for the execution to end.
			if (!process.waitFor(4000, TimeUnit.MILLISECONDS)) {
				logger.info("Timeout for test execution");
				// timeout - kill the process.
				process.destroyForcibly(); // consider using destroyForcibly
											// instead
				// Force Wait 1 sec to clean up
				Thread.sleep(1000);
			}

			try {
				int result = process.exitValue(); // ;process.waitFor();

				if (result != 0) {
					logger.debug(" Test FAILED " + result);
				}
				return (result == 0);
			} catch (Throwable e) {
				e.printStackTrace();
				// Avoid resource leakeage. This should be IDEMPOTENT
				process.destroyForcibly();
				// TODO: handle exception
			}
			return false;
		} finally {
			time = System.currentTimeMillis() - time;
			logger.info("Verification done in " + time + " ms");
		}

	}

}

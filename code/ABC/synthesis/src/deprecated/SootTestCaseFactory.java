package de.unipassau.abc.generation.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JarTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;
import com.strobel.decompiler.DecompilerSettings;

import de.unipassau.abc.data.DataDependencyGraph;
import de.unipassau.abc.data.ExecutionFlowGraph;
import de.unipassau.abc.data.JimpleUtils;
import de.unipassau.abc.data.MethodInvocation;
import de.unipassau.abc.data.Triplette;
import de.unipassau.abc.generation.utils.Utils;
import soot.SootClass;
import soot.SootMethod;
import soot.SourceLocator;
import soot.Unit;
import soot.Value;
import soot.jimple.AssignStmt;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.JasminClass;
import soot.options.Options;
import soot.util.JasminOutputStream;

public class SootTestCaseFactory {

	private final static Logger logger = LoggerFactory.getLogger(SootTestCaseFactory.class);

	private static Value extractBase(Unit u) {
		if (u instanceof AssignStmt) {
			System.out.println("caseAssignStmt NOT IMPLEMENTED" + u);
			return null;
		} else if (u instanceof InvokeStmt) {
			System.out.println("caseInvokeStmt " + u);
			InvokeExpr invokeExpr = ((InvokeStmt) u).getInvokeExpr();
			if (invokeExpr instanceof InstanceInvokeExpr) {
				return ((InstanceInvokeExpr) invokeExpr).getBase();
			} else {
				// if (invokeExpr instanceof StaticInvokeExpr) {
				return null;
			}
		} else {
			System.out.println("SootTestCaseFactory.extractBase() Cannot handle " + u.getClass() + " " + u);
			return null;
		}
	}

	/**
	 * This generates directly the augmented java classes.
	 */

	public static Set<CompilationUnit> generateTestFiles(List<File> projectJars, Collection<SootClass> testClasses)
			throws IOException {

		// Use a temp folder
		File outputDir = Files.createTempDirectory("SootDecompile").toFile();
		outputDir.deleteOnExit();

		Options.v().set_output_dir(outputDir.getAbsolutePath());

		logger.debug("Output directory is " + outputDir.getAbsolutePath());

		if (!outputDir.exists() && !outputDir.mkdirs()) {
			throw new RuntimeException("Output dir " + outputDir.getAbsolutePath() + " cannot be created ");
		}

		// Externalize this to a dependency !
		Set<CompilationUnit> generatedClasses = new HashSet<>();
		// Resolve the missing generics
		CombinedTypeSolver combinedTypeSolver = new CombinedTypeSolver();
		combinedTypeSolver.add(new ReflectionTypeSolver());
		for (File jar : projectJars) {
			combinedTypeSolver.add(new JarTypeSolver(jar.getAbsolutePath()));
		}

		for (SootClass testClass : testClasses) {
			logger.info("TestCaseFactory.generateTestFiles() " + testClass.getName());
			String classFileName = SourceLocator.v().getFileNameFor(testClass, Options.output_format_class);
			try {

				// Since we set the Soot set_output_dir, the following returns
				// exactly the class file we need
				// Generate a the .class File from jimple
				File testClassFile = generateClassBytecode(classFileName, testClass);
				try {
					Class javaTestClass = loadClass(testClass.getName(), Files.readAllBytes(testClassFile.toPath()));
					// TODO Verify using ASM !
				} catch (Throwable e) {
					logger.error("Cannot generate class file for " + classFileName);
					e.printStackTrace();
					// cannot generate class - skip
				}

				// Write directly into a String
				// File sourceFile = new File(classFileName.replaceAll(".class",
				// ".java"));

				// logger.debug("The output files are:\n" + //
				// testClassFile.getAbsolutePath() + "\n" + //
				// sourceFile.getAbsolutePath());

				// You need to call flush/close the have the class actually
				// generated, that's why this is in a try(Resource){} block
				DecompilerSettings decompilerSettings = DecompilerSettings.javaDefaults();
				//
				decompilerSettings.setForceExplicitImports(true);
				decompilerSettings.setForceExplicitTypeArguments(true);
				decompilerSettings.setRetainRedundantCasts(true);
				// Not sure ?
				// decompilerSettings.setExcludeNestedTypes( true );
				decompilerSettings.setMergeVariables(false);
				decompilerSettings.setSimplifyMemberReferences(true);
				//

				// Maybe it will be wise working with Files instead of big
				// strings ?!
				try (final Writer writer = new StringWriter();) {
					com.strobel.decompiler.Decompiler.decompile(testClassFile.getAbsolutePath(),
							new com.strobel.decompiler.PlainTextOutput(writer), //
							decompilerSettings);

					CompilationUnit javaCode = JavaParser.parse(writer.toString());
					// Forcefully initialize all the Objects to null if they are
					// not
					// initialized !
					TestCaseFactory.forceObjectInitialization(javaCode, combinedTypeSolver);

					// During delta debugging there's no need to resolve types
					// if (resolveTypes) {
					// This updates the code
					TestCaseFactory.resolveMissingGenerics(javaCode, combinedTypeSolver);
					// }

					// extract the type of retunValue if any
					// Type returnValueType = null;
					// for (SootMethod m : testClass.getMethods()) {
					// if (m.getName().startsWith("test")) {
					// for (Local l : m.getActiveBody().getLocals()) {
					// if (l.getName().equals("returnValue")) {
					// returnValueType = l.getType();
					// //
					// System.out.println("SootTestCaseFactory.generateTestFiles()
					// // Found return value "
					// // + returnValueType);
					// break;
					// }
					//
					// }
					// }
					// }
					//
					// if (returnValueType != null) {
					// TestCaseFactory.checkAndSetRetunValue(javaCode,
					// returnValueType.toString());
					// }

					generatedClasses.add(javaCode);
				}

			} catch (Throwable e) {
				logger.error("Cannot generate class file for " + classFileName);
				e.printStackTrace();
				// cannot generate class - skip
				continue;
			}
		}

		return generatedClasses;

	}

	// TODO We really need this? I bet soot can directly generate Java code ...
	private static File generateClassBytecode(String fileName, SootClass sClass) throws IOException {

		// Since the fileName might be structures, i.e., the class has package,
		// we need to create the directory structure

		File outputFile = new File(fileName);
		if (!outputFile.getParentFile().exists()) {
			outputFile.getParentFile().mkdirs();
		}

		if (System.getProperty("debug") != null && System.getProperty("debug").equals("true")) {
			JimpleUtils.prettyPrint(sClass);
		}

		// Autoclose those ?
		try (OutputStream streamOut = new JasminOutputStream(new FileOutputStream(fileName));
				PrintWriter writerOut = new PrintWriter(new OutputStreamWriter(streamOut));) {
			JasminClass jasminClass = new soot.jimple.JasminClass(sClass);
			jasminClass.print(writerOut);
			writerOut.flush();
		}

		return new File(fileName);
	}

	private static Class loadClass(String className, byte[] b) {
		System.out.println("TestCaseFactory.loadClass() ! ");
		// override classDefine (as it is protected) and define the class.
		Class clazz = null;
		try {
			ClassLoader loader = new URLClassLoader(new URL[] {}, ClassLoader.getSystemClassLoader());
			Class cls = Class.forName("java.lang.ClassLoader");
			java.lang.reflect.Method method = cls.getDeclaredMethod("defineClass",
					new Class[] { String.class, byte[].class, int.class, int.class });

			// protected method invocaton
			method.setAccessible(true);
			try {
				Object[] args = new Object[] { className, b, new Integer(0), new Integer(b.length) };
				clazz = (Class) method.invoke(loader, args);
			} finally {
				method.setAccessible(false);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		return clazz;
	}

	/**
	 * Generate the class and store it to outputDir folder
	 * 
	 * @param projectJars
	 * @param testClasses
	 * @param carvedTestCases
	 * @param resetEnvironmentBy
	 * @return
	 * @throws IOException
	 */

	// Use a thread pool to parallelize this
	public static Set<File> generateAugmenetedTestFiles(List<File> projectJars, Set<SootClass> testClasses,
			List<Triplette<ExecutionFlowGraph, DataDependencyGraph, SootMethod>> carvedTestCases,
			String resetEnvironmentBy, File outputDir) throws IOException {

		// Use a thread pool to compile and generate the files !
		final ExecutorService executor = Executors.newFixedThreadPool(10);

		// Use a temp folder
		// File outputDir = Files.createTempDirectory("SootDecompile").toFile();
		// outputDir.deleteOnExit();

		Options.v().set_output_dir(outputDir.getAbsolutePath());

		// logger.debug("Output directory is " + outputDir.getAbsolutePath());

		if (!outputDir.exists() && !outputDir.mkdirs()) {
			throw new RuntimeException("Output dir " + outputDir.getAbsolutePath() + " cannot be created ");
		}

		// Externalize this to a dependency !
		// Set<CompilationUnit> generatedClasses = new HashSet<>();
		Set<File> generatedFiles = new HashSet<>();

		// Is this thread safe ?!

		// Resolve the missing generics
		final CombinedTypeSolver combinedTypeSolver = new CombinedTypeSolver();
		combinedTypeSolver.add(new ReflectionTypeSolver());
		for (File jar : projectJars) {
			combinedTypeSolver.add(new JarTypeSolver(jar.getAbsolutePath()));
		}

		List<Future<File>> files = new ArrayList<>();
		for (final SootClass testClass : testClasses) {
			files.add(executor.submit(new Callable<File>() {

				@Override
				public File call() throws Exception {
					logger.info("\n TestCaseFactory.generateTestFiles() [" + Thread.currentThread().getName()
							+ "] For Class " + testClass.getName());

					String classFileName = SourceLocator.v().getFileNameFor(testClass, Options.output_format_class);
					try {
						// Since we set the Soot set_output_dir, the following
						// returns
						// exactly the class file we need
						// Generate a the .class File from jimple
						File testClassFile = generateClassBytecode(classFileName, testClass);

						// You need to call flush/close the have the class
						// actually
						// generated, that's why this is in a try(Resource){}
						// block
						DecompilerSettings decompilerSettings = DecompilerSettings.javaDefaults();
						//
						decompilerSettings.setForceExplicitImports(true);
						decompilerSettings.setForceExplicitTypeArguments(true);
						//
						try (final Writer writer = new StringWriter();) {
							com.strobel.decompiler.Decompiler.decompile(testClassFile.getAbsolutePath(),
									new com.strobel.decompiler.PlainTextOutput(writer), //
									decompilerSettings);
							CompilationUnit javaCode = JavaParser.parse(writer.toString());
							TestCaseFactory.resolveMissingGenerics(javaCode, combinedTypeSolver);
							Utils.createAtBeforeResetMethod(resetEnvironmentBy, javaCode);
							// Return the file
							return (File) Utils.storeToFile(Collections.singleton(javaCode), outputDir).iterator()
									.next();
						}
					} catch (Throwable e) {
						logger.error("Cannot generate class file for " + classFileName);
						e.printStackTrace();
						// cannot generate class - skip
						// continue;
					}
					return null;
				}
			}));
		}

		// Do not accept new work
		executor.shutdown();
		//
		logger.info("Collecting the results from Test Generation");

		for (Future<File> fFile : files) {
			try {
				File f = fFile.get();
				if (f != null) {

					// This is blocking
					System.out
							.println("SootTestCaseFactory.generateAugmenetedTestFiles() Adding " + f.getAbsolutePath());
					generatedFiles.add(f);
				}
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}

		return generatedFiles;
	}

	private static void includeXMLValidationCalls(SootClass testClass, //
			List<Triplette<ExecutionFlowGraph, DataDependencyGraph, SootMethod>> carvedTestCases) {
		// Include in the tests the XML Assertions
		for (Triplette<ExecutionFlowGraph, DataDependencyGraph, SootMethod> carvedTestCase : carvedTestCases) {

			MethodInvocation mut = carvedTestCase.getFirst().getLastMethodInvocation();
			SootMethod testMethod = carvedTestCase.getThird();

			// Match class with testMethod
			for (SootMethod m : testClass.getMethods()) {
				// We might need to compare the signature
				if (m.equals(testMethod)) {
					final List<Unit> validation = Utils.generateValidationUnit(testMethod, mut.getXmlDumpForOwner(),
							mut.getXmlDumpForReturn(), carvedTestCase.getSecond().getReturnObjectLocalFor(mut));

					if (!validation.isEmpty()) {
						Unit returnStmt = m.getActiveBody().getUnits().getLast();
						m.getActiveBody().getUnits().insertBefore(validation, returnStmt);
					}
				}
			}

		}

	}

}

package de.unipassau.abc.generation;

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
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JarTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;
import com.strobel.decompiler.DecompilerSettings;

import de.unipassau.abc.utils.JimpleUtils;
import soot.SootClass;
import soot.SourceLocator;
import soot.jimple.JasminClass;
import soot.options.Options;
import soot.util.JasminOutputStream;

public class SootTestCaseFactory {

	private final static Logger logger = LoggerFactory.getLogger(SootTestCaseFactory.class);

	public static Set<CompilationUnit> generateTestFiles(List<File> projectJars, Collection<SootClass> testClasses) throws IOException {

		// Use a temp folder
		File outputDir = Files.createTempDirectory("SootDecompile").toFile();
		outputDir.deleteOnExit();
		
		Options.v().set_output_dir(outputDir.getAbsolutePath());

		logger.debug("Output directory is " + outputDir.getAbsolutePath());

		if (!outputDir.exists() && !outputDir.mkdirs()) {
			throw new RuntimeException("Output dir " + outputDir.getAbsolutePath() + " cannot be created ");
		}

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
				final Writer writer = new StringWriter();
				com.strobel.decompiler.Decompiler.decompile(testClassFile.getAbsolutePath(),
						new com.strobel.decompiler.PlainTextOutput(writer), //
						decompilerSettings);

				CompilationUnit javaCode = JavaParser.parse(writer.toString());

				// Forcefully initialize all the Objects to null if they are not
				// initialized !
				TestCaseFactory.forceObjectInitialization(javaCode, combinedTypeSolver);

				// During delta debugging there's no need to resolve types
				// if (resolveTypes) {
				// This updates the code
				TestCaseFactory.resolveMissingGenerics(javaCode, combinedTypeSolver);
				// }

				generatedClasses.add(javaCode);

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

		OutputStream streamOut = new JasminOutputStream(new FileOutputStream(fileName));
		PrintWriter writerOut = new PrintWriter(new OutputStreamWriter(streamOut));
		JasminClass jasminClass = new soot.jimple.JasminClass(sClass);
		jasminClass.print(writerOut);
		writerOut.flush();
		streamOut.close();

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

}

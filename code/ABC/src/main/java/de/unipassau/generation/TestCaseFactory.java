package de.unipassau.generation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.unipassau.utils.JimpleUtils;
import soot.ArrayType;
import soot.Local;
import soot.Modifier;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.SourceLocator;
import soot.Type;
import soot.VoidType;
import soot.jimple.JasminClass;
import soot.jimple.Jimple;
import soot.jimple.JimpleBody;
import soot.jimple.StringConstant;
import soot.options.Options;
import soot.util.Chain;
import soot.util.JasminOutputStream;

public class TestCaseFactory {

	private final static Logger logger = LoggerFactory.getLogger(TestCaseFactory.class);

	public static void generateTestFiles(File outputDir, Collection<SootClass> testClasses) {

		Options.v().set_output_dir(outputDir.getAbsolutePath());

		logger.debug("Output directory is " + outputDir.getAbsolutePath());

		if (!outputDir.exists() && !outputDir.mkdirs()) {
			throw new RuntimeException("Output dir " + outputDir.getAbsolutePath() + " cannot be created ");
		}

		for (SootClass testClass : testClasses) {

			logger.info("Generating Test Class :::: " + testClass.getName());

			// Since we set the Soot set_output_dir, the following returns exactly the class file we
			// need
			String classFileName = SourceLocator.v().getFileNameFor(testClass, Options.output_format_class);
			// Generate a the .class File from jimple
			try {
				File testClassFile = generateClassContent(classFileName, testClass);

				File sourceFile = new File(classFileName.replaceAll(".class", ".java"));
				logger.debug("The output files are:\n" + //
						testClassFile.getAbsolutePath() + "\n" + //
						sourceFile.getAbsolutePath());

				// You need to call flush/close the have the class actually
				// generated, that's why this is in a try(Resource){} block
				try (final BufferedWriter writer = new BufferedWriter(new FileWriter(sourceFile))) {
					com.strobel.decompiler.Decompiler.decompile(testClassFile.getAbsolutePath(),
							new com.strobel.decompiler.PlainTextOutput(writer));
				}

			} catch (Exception e) {
				logger.error("Cannot generate class file for " + classFileName);
				e.printStackTrace();
				// cannot generate class - skip
				continue;
			}

		}

	}

	private static void helloWorld() throws IOException {

		System.out.println("TestCaseFactory.helloWorld()\n\n\n");

		SootClass sClass;
		SootMethod method;

		// Resolve dependencies
		Scene.v().loadClassAndSupport("java.lang.Object");
		Scene.v().loadClassAndSupport("java.lang.System");

		// Declare 'public class HelloWorld'
		sClass = new SootClass("HelloWorld", Modifier.PUBLIC);

		// 'extends Object'
		sClass.setSuperclass(Scene.v().getSootClass("java.lang.Object"));
		Scene.v().addClass(sClass);

		// Create the method, public static void main(String[])
		method = new SootMethod("main", Arrays.asList(new Type[] { ArrayType.v(RefType.v("java.lang.String"), 1) }),
				VoidType.v(), Modifier.PUBLIC | Modifier.STATIC);

		sClass.addMethod(method);

		// Create the method body
		{
			// create empty body
			JimpleBody body = Jimple.v().newBody(method);

			method.setActiveBody(body);
			Chain units = body.getUnits();
			Local arg, tmpRef;

			// Add some locals, java.lang.String l0
			arg = Jimple.v().newLocal("l0", ArrayType.v(RefType.v("java.lang.String"), 1));
			body.getLocals().add(arg);

			// Add locals, java.io.printStream tmpRef
			tmpRef = Jimple.v().newLocal("tmpRef", RefType.v("java.io.PrintStream"));
			body.getLocals().add(tmpRef);

			// add "l0 = @parameter0"
			units.add(Jimple.v().newIdentityStmt(arg,
					Jimple.v().newParameterRef(ArrayType.v(RefType.v("java.lang.String"), 1), 0)));

			// add "tmpRef = java.lang.System.out"
			units.add(Jimple.v().newAssignStmt(tmpRef, Jimple.v()
					.newStaticFieldRef(Scene.v().getField("<java.lang.System: java.io.PrintStream out>").makeRef())));

			// insert "tmpRef.println("Hello world!")"
			{
				SootMethod toCall = Scene.v().getMethod("<java.io.PrintStream: void println(java.lang.String)>");
				units.add(Jimple.v().newInvokeStmt(
						Jimple.v().newVirtualInvokeExpr(tmpRef, toCall.makeRef(), StringConstant.v("Hello world!"))));
			}

			// insert "return"
			units.add(Jimple.v().newReturnVoidStmt());

		}

		String fileName = SourceLocator.v().getFileNameFor(sClass, Options.output_format_class);
		generateClassContent(fileName, sClass);
		System.out.println("HelloWorld ==> " + fileName);

	}

	private static File generateClassContent(String fileName, SootClass sClass) throws IOException {

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

}
package de.unipassau.abc.generation;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PushbackReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.event.Level;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.visitor.ModifierVisitor;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.resolution.types.ResolvedReferenceType;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JarTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;

import de.unipassau.abc.carving.Carver;
import de.unipassau.abc.utils.Slf4jSimpleLoggerRule;
import soot.G;
import soot.SootClass;
import soot.SootResolver;
import soot.jimple.parser.Walker;
import soot.jimple.parser.lexer.Lexer;
import soot.jimple.parser.lexer.LexerException;
import soot.jimple.parser.node.Start;
import soot.jimple.parser.parser.Parser;
import soot.jimple.parser.parser.ParserException;
import soot.options.Options;
import soot.util.EscapedReader;

public class TestGeneration {

	@Rule
	public Slf4jSimpleLoggerRule loggerLevelRule = new Slf4jSimpleLoggerRule(Level.TRACE);

	@Before
	public void setupSoot() {
		G.reset();
	}

	@Test
	public void testEmployee() throws IOException, ParserException, LexerException {
		File projectJar = new File("./src/test/resources/Employee.jar");
		Options.v().set_print_tags_in_output(true);
		Carver.setupSoot(Collections.singletonList(projectJar));
		//
		SootClass sootClass = readFromJimple("./src/test/resources/jimples/org.employee.TestFileRead_1.jimple");
		// JimpleUtils.prettyPrint( sootClass );
		CompilationUnit testCode = TestCaseFactory.converSootClassToJavaClass(sootClass);
		System.out.println(testCode);
		// Check for generics !
	}

	@Test
	public void testHotelGenerics() throws IOException, ParserException, LexerException {
		try {
			List<File> projectJars = new ArrayList<>();
			projectJars.add(new File("./src/test/resources/HotelReservationSystem.jar"));
			projectJars.add(new File("./src/test/resources/HotelReservationSystem-tests.jar"));
			projectJars.add(new File("./src/test/resources/system-rules-1.17.0.jar"));

			String javaCode = new String(
					Files.readAllBytes(Paths.get("./src/test/resources/javas/org.hotelme.TestRoom_10.javaz")));

			CompilationUnit cu = JavaParser.parse(javaCode);
			//
			CombinedTypeSolver combinedTypeSolver = new CombinedTypeSolver();
			combinedTypeSolver.add(new ReflectionTypeSolver());
			for (File jar : projectJars) {
				combinedTypeSolver.add(new JarTypeSolver(jar.getAbsolutePath()));
			}

			// Collect the Collections that are missing a generic type
			final Map<String, ResolvedType> missingTypes = new HashMap<>();

			cu.accept(new VoidVisitorAdapter<JavaParserFacade>() {
				public void visit(final AssignExpr n, final JavaParserFacade javaParserFacade) {
					super.visit(n, javaParserFacade);
					try {
						if (missingTypes.containsKey(n.getTarget().toString())) {
							return;
						}
						// if (n.getTarget().equals(new
						// NameExpr("javautilarrayList00"))) {
						ResolvedType targetType = javaParserFacade.getType(n.getTarget());
						ResolvedType valueType = javaParserFacade.getType(n.getValue());

						if (targetType.isReferenceType() && valueType.isReferenceType()) {
							//
							if (((ResolvedReferenceType) targetType).typeParametersMap().isEmpty()
									&& !((ResolvedReferenceType) valueType).typeParametersMap().isEmpty()) {
								System.out.println(n.getTarget() + " misses type. Update to: " + valueType.describe());
								missingTypes.put(n.getTarget().toString(), valueType);

							}
						}
					} catch (Exception e) {
						// TODO: handle exception
					}

				}

			}, JavaParserFacade.get(combinedTypeSolver));

			cu.accept(new ModifierVisitor<Void>() {
				public VariableDeclarator visit(final VariableDeclarator n, Void arg) {
					super.visit(n, arg);
					if (missingTypes.containsKey(n.getName().asString())) {
						System.out.println("Updating Type Def for " + n.getName());
						n.setType(missingTypes.get(n.getName().asString()).describe());
					}
					return n;
				}
			}, null);

			System.out.println("TestGeneration.testHotelGenerics()\n" + cu);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}

	}

	@Test
	public void testForceInitialization() throws IOException, ParserException, LexerException {
		try {
			List<File> projectJars = new ArrayList<>();
			projectJars.add(new File("./src/test/resources/HotelReservationSystem.jar"));
			projectJars.add(new File("./src/test/resources/HotelReservationSystem-tests.jar"));
			projectJars.add(new File("./src/test/resources/system-rules-1.17.0.jar"));

			String javaCode = new String(
					Files.readAllBytes(Paths.get("./src/test/resources/javas/org.hotelme.TestRoom_10.javaz")));

			CompilationUnit cu = JavaParser.parse(javaCode);
			//
			CombinedTypeSolver combinedTypeSolver = new CombinedTypeSolver();
			combinedTypeSolver.add(new ReflectionTypeSolver());
			for (File jar : projectJars) {
				combinedTypeSolver.add(new JarTypeSolver(jar.getAbsolutePath()));
			}

			TestCaseFactory.forceObjectInitialization(cu, combinedTypeSolver);

			System.out.println(cu);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	
	@Test
	public void testRenameToMinimized() throws IOException, ParserException, LexerException {
		try {
			List<File> projectJars = new ArrayList<>();
			projectJars.add(new File("./src/test/resources/HotelReservationSystem.jar"));
			projectJars.add(new File("./src/test/resources/HotelReservationSystem-tests.jar"));
			projectJars.add(new File("./src/test/resources/system-rules-1.17.0.jar"));

			String javaCode = new String(
					Files.readAllBytes(Paths.get("./src/test/resources/javas/org.hotelme.TestRoom_10.javaz")));

			CompilationUnit cu = JavaParser.parse(javaCode);

			Carver.renameClass( cu, "_minimized");
			
			// Assert class name is new, assert constructor returns the new name
			//
			System.out.println(cu);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testAddBefore() throws IOException, ParserException, LexerException {
		try {
			List<File> projectJars = new ArrayList<>();
			projectJars.add(new File("./src/test/resources/HotelReservationSystem.jar"));
			projectJars.add(new File("./src/test/resources/HotelReservationSystem-tests.jar"));
			projectJars.add(new File("./src/test/resources/system-rules-1.17.0.jar"));

			String javaCode = new String(
					Files.readAllBytes(Paths.get("./src/test/resources/javas/org.hotelme.TestRoom_10.javaz")));

			CompilationUnit cu = JavaParser.parse(javaCode);

			String resetEnvironmentBy = "org.hotelme.utils.SystemTestUtils.dropAndRecreateTheDb()";
			
			Carver.createAtBeforeResetMethod(resetEnvironmentBy, cu);

			System.out.println(cu);
			
			// Now clean it up
			Carver.removeAtBeforeResetMethod(cu);
			
			System.out.println(cu);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	private CompilationUnit setupCompilationUnit(String testClassName, List<File> projectJars) throws IOException {

		String javaCode = new String(
				Files.readAllBytes(Paths.get("./src/test/resources/javas/org.hotelme." + testClassName + ".javaz")));

		CompilationUnit cu = JavaParser.parse(javaCode);
		//
		CombinedTypeSolver combinedTypeSolver = new CombinedTypeSolver();
		combinedTypeSolver.add(new ReflectionTypeSolver());
		for (File jar : projectJars) {
			combinedTypeSolver.add(new JarTypeSolver(jar.getAbsolutePath()));
		}

		TestCaseFactory.resolveMissingGenerics(cu, combinedTypeSolver);

		return cu;
	}


	private File compileFile(String testClassName, List<File> projectJars) throws IOException {

		String javaCode = new String(
				Files.readAllBytes(Paths.get("./src/test/resources/javas/org.hotelme." + testClassName + ".javaz")));

		File tempDir = Files.createTempDirectory("Test").toFile();
		//
//		tempDir.deleteOnExit();
		//
		File dirFile = new File(tempDir, "org/hotelme");
		dirFile.mkdirs();

		File classFile = new File(dirFile, testClassName + ".java");
		classFile.createNewFile();

		CompilationUnit cu = JavaParser.parse(javaCode);
		//
		CombinedTypeSolver combinedTypeSolver = new CombinedTypeSolver();
		combinedTypeSolver.add(new ReflectionTypeSolver());
		for (File jar : projectJars) {
			combinedTypeSolver.add(new JarTypeSolver(jar.getAbsolutePath()));
		}

		TestCaseFactory.resolveMissingGenerics(cu, combinedTypeSolver);

		System.out.println("TestGeneration.testHotelGenerics()\n" + cu);

		Files.write(classFile.toPath(), cu.toString().getBytes());

		return tempDir;
	}

	private List<File> getHotelMeProjectsJars() {
		List<File> projectJars = new ArrayList<>();
		projectJars.add(new File("./src/test/resources/HotelReservationSystem.jar"));
		projectJars.add(new File("./src/test/resources/HotelReservationSystem-tests.jar"));
		projectJars.add(new File("./src/test/resources/system-rules-1.17.0.jar"));
		//
		projectJars.add(new File("/Users/gambi/.m2/repository/joda-time/joda-time/2.9.4/joda-time-2.9.4.jar"));
		projectJars.add(new File(
				"/Users/gambi/.m2/repository/mysql/mysql-connector-java/5.1.39/mysql-connector-java-5.1.39.jar"));
		//
		return projectJars;
	}

	@Test
	public void testHotelGenericsUsage() throws IOException, ParserException, LexerException {
		try {
			String testClassName = "TestRoom_16";

			List<File> projectJars = getHotelMeProjectsJars();

			File tempDir = compileFile(testClassName, projectJars);

			// Assert by compiling the code !
			boolean compiled = DeltaDebugger.compileAndRunJUnitTest("org.hotelme." + testClassName, tempDir,
					projectJars);

			assertTrue(compiled);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}

	}

	@Test
	public void testHotelGenericsUsage_1() throws IOException, ParserException, LexerException {
		try {
			String testClassName = "TestRoom_17";

			List<File> projectJars = getHotelMeProjectsJars();

			File tempDir = compileFile(testClassName, projectJars);

			// Assert by compiling the code !
			boolean compiled = DeltaDebugger.compileAndRunJUnitTest("org.hotelme." + testClassName, tempDir,
					projectJars);

			assertTrue(compiled);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}

	}
	
	@Test
	public void testHotelGenericsUsage_TestHotelView_1() throws IOException, ParserException, LexerException {
		try {
			String testClassName = "TestHotelView_1";

			List<File> projectJars = getHotelMeProjectsJars();

			CompilationUnit testClass  = setupCompilationUnit(testClassName, projectJars);

			TestSuiteExecutor testSuiteExecutor = new TestSuiteExecutor(projectJars);
			// This executes the test into a temp folder anyway, no need to
			// rename them !
			testSuiteExecutor.compileRunAndGetCoverageJUnitTests(Collections.singletonList(testClass));
			
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}

	}

	@Test
	public void testHotelGenericsUsage2() throws IOException, ParserException, LexerException {
		try {
			String testClassName = "TestRoom_18";

			List<File> projectJars = getHotelMeProjectsJars();

			File tempDir = compileFile(testClassName, projectJars);

			// Assert by compiling the code !
			boolean compiled = DeltaDebugger.compileAndRunJUnitTest("org.hotelme." + testClassName, tempDir,
					projectJars);

			assertTrue(compiled);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}

	}

	@Test
	public void testHotelGenericsUsage3() throws IOException, ParserException, LexerException {
		try {
			String testClassName = "TestRoom_19";

			List<File> projectJars = getHotelMeProjectsJars();

			File tempDir = compileFile(testClassName, projectJars);

			// Assert by compiling the code !
			boolean compiled = DeltaDebugger.compileAndRunJUnitTest("org.hotelme." + testClassName, tempDir,
					projectJars);

			assertTrue(compiled);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}

	}

	private SootClass readFromJimple(String jimpleFile) throws ParserException, LexerException, IOException {
		InputStream stream = new FileInputStream(jimpleFile);
		Parser p = new Parser(new Lexer(
				new PushbackReader(new EscapedReader(new BufferedReader(new InputStreamReader(stream))), 1024)));
		Start sableAST = p.parse();
		Walker w = new Walker(SootResolver.v());
		sableAST.apply(w);
		return w.getSootClass();

		// Iterator<String> lines = Files.readAllLines(
		// Paths.get(jimpleFile)).iterator();
		//
		// // Class org.employee.TestValidation_1
		// String testCaseName = lines.next().split(" ")[1];
		//
		// SootClass sClass = new SootClass(testCaseName, Modifier.PUBLIC);
		// sClass.setSuperclass(Scene.v().getSootClass("java.lang.Object"));
		// Scene.v().addClass(sClass);
		//
		//// Method public void test_1() throws java.lang.Exception
		// String testMethodName = lines.next().split(" ")[3].replace("(",
		// "").replace(")","");
		// //
		// SootMethod testMethod = new SootMethod(testMethodName,
		// Collections.<Type>emptyList(), VoidType.v(), Modifier.PUBLIC);
		//
		// SootClass e = Scene.v().loadClassAndSupport("java.lang.Exception");
		// testMethod.addExceptionIfAbsent(e);
		//
		// // Annotate the method with org.junit.Test
		// AnnotationGenerator.v().annotate(testMethod, org.junit.Test.class);
		//
		// // Build the body from the input list of statements
		// JimpleBody body = Jimple.v().newBody(testMethod);
		// testMethod.setActiveBody(body);
		//
		// lines.next(); // SKIP THE EMPTY LINE AFTER METHOD DECLARATION
		//
		// List<Local> locas = new ArrayList<>();
		// for( String line = lines.next(); line != "";){
		// String[] tokens = line.split(" ");
		// body.getLocals().add( Jimple.v().newLocal( tokens[0], RefType.v(
		// tokens[1])) );
		// }
		//// soot.jimple.parser.Parse.parse(istream, sc)
		//// Unit unit = Unit.from
		//// sClass.addMethod(testMethod);
		//// body.insertIdentityStmts();
		// return sClass;
	}
}

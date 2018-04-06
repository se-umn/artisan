package de.unipassau.abc.generation;

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
import org.junit.Test;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.CastExpr;
import com.github.javaparser.ast.visitor.ModifierVisitor;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.resolution.types.ResolvedReferenceType;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JarTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;

import de.unipassau.abc.carving.Carver;
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
	public void testHotelGenericsUsage() throws IOException, ParserException, LexerException {
		try {
			List<File> projectJars = new ArrayList<>();
			projectJars.add(new File("./src/test/resources/HotelReservationSystem.jar"));
			projectJars.add(new File("./src/test/resources/HotelReservationSystem-tests.jar"));
			projectJars.add(new File("./src/test/resources/system-rules-1.17.0.jar"));

			String javaCode = new String(
					Files.readAllBytes(Paths.get("./src/test/resources/javas/org.hotelme.TestRoom_24.javaz")));

			// javautilarrayList00 -> ArrayList<>
			// javautilarrayList00.add( ROOM )
			//
			// THIS IS THE PROBLEM: No casting. Ideally, we should define
			// javautilarrayList00 as javautilarrayList00<Room>
			// But casting might be easy to implement !
			// orghotelmeroom00 = javautilarrayList00.get(0);

			CompilationUnit cu = JavaParser.parse(javaCode);
			//
			CombinedTypeSolver combinedTypeSolver = new CombinedTypeSolver();
			combinedTypeSolver.add(new ReflectionTypeSolver());
			for (File jar : projectJars) {
				combinedTypeSolver.add(new JarTypeSolver(jar.getAbsolutePath()));
			}

			// Collect the Collections that are missing a generic type
			final Map<String, ResolvedType> missingTypes = new HashMap<>();

			cu.accept(new ModifierVisitor<JavaParserFacade>() {
				public AssignExpr visit(final AssignExpr n, JavaParserFacade javaParserFacade) {
					super.visit(n, javaParserFacade);

					try {
						ResolvedType targetType = javaParserFacade.getType(n.getTarget());
						ResolvedType valueType = javaParserFacade.getType(n.getValue());

//						if (targetType.isReferenceType() && valueType.isReferenceType()) {

							if (!targetType.isAssignableBy(valueType)) {
							System.out.println(
									n);
								System.out.println(
										"Cast needed " + targetType.describe() + " --> " + valueType.describe());
								
								CastExpr c = new CastExpr();
								c.setType( targetType.describe());
								c.setExpression( n.getValue() );
								n.setValue( c );
							}
							//
							// if (((ResolvedReferenceType)
							// targetType).typeParametersMap().isEmpty()
							// && !((ResolvedReferenceType)
							// valueType).typeParametersMap().isEmpty()) {
							// System.out.println(n.getTarget() + " misses type.
							// Update to: " + valueType.describe());
							// missingTypes.put(n.getTarget().toString(),
							// valueType);
//						}
						// }
					} catch (Exception e) {
						// TODO: handle exception
					}
					// if( missingTypes.containsKey( n.getName().asString() ) ){
					// System.out.println("Updating Type Def for " +
					// n.getName());
					// n.setType( missingTypes.get( n.getName().asString( )
					// ).describe() );
					// }
					return n;
				}
			}, JavaParserFacade.get(combinedTypeSolver));

			System.out.println("TestGeneration.testHotelGenerics()\n" + cu);
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

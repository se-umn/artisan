package de.unipassau.abc.generation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PushbackReader;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

import de.unipassau.abc.carving.Carver;
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
	public void setupSoot(){
		File projectJar = new File("./src/test/resources/Employee.jar");
		Options.v().set_print_tags_in_output( true );
		Carver.setupSoot(Collections.singletonList(projectJar));
	}
	@Test
	public void test() throws IOException, ParserException, LexerException {
		SootClass sootClass = readFromJimple("./src/test/resources/jimples/org.employee.TestFileRead_1.jimple");
//		JimpleUtils.prettyPrint( sootClass );
		TestCaseFactory.converSootClassToJavaClass( sootClass );
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

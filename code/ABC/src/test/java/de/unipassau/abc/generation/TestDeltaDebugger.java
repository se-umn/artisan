package de.unipassau.abc.generation;

import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PushbackReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import soot.G;
import soot.SootClass;
import soot.SootResolver;
import soot.jimple.parser.Walker;
import soot.jimple.parser.lexer.Lexer;
import soot.jimple.parser.lexer.LexerException;
import soot.jimple.parser.node.Start;
import soot.jimple.parser.parser.Parser;
import soot.jimple.parser.parser.ParserException;
import soot.util.EscapedReader;

public class TestDeltaDebugger {

	private SootClass readFromJimple(String jimpleFile) throws ParserException, LexerException, IOException {
		InputStream stream = new FileInputStream(jimpleFile);
		Parser p = new Parser(new Lexer(
				new PushbackReader(new EscapedReader(new BufferedReader(new InputStreamReader(stream))), 1024)));
		Start sableAST = p.parse();
		Walker w = new Walker(SootResolver.v());
		sableAST.apply(w);
		return w.getSootClass();
	}

	@Before
	public void setupSoot() {
		G.reset();
	}

	@Test
	public void parseOutput() {
		final Pattern failedTestPattern = Pattern.compile("\\d\\d*\\) (.*)"
					+ "\\(([a-zA_Z_][\\.\\w]*(\\[\\])?)?(,[a-zA_Z_][\\.\\w]*(\\[\\])?)*\\)");

		Set<String> failedTests = new HashSet<>();
		
		for (String line : new String[] { "There was 1 failure:", "1) test_1(org.employee.TestFileHandle_1_minimized)",
				"java.lang.NullPointerException", "1) test_2(org.employee.TestFileHandle_2_minimized)",
				"at org.employee.TestFileHandle_1_minimized.test_1(TestFileHandle_1_minimized.java:106)" }) {
			Matcher matcher = failedTestPattern.matcher(line);
			if( matcher.matches() ){
				String testMethod = matcher.group(1);
				String testClass = matcher.group(2);
						
				
				System.out.println("TestDeltaDebugger.parseOutput() " + testClass + "." + testMethod);
				failedTests.add(  testClass + "." + testMethod );
			}
		}

		Assert.assertEquals( 2 , failedTests.size());
	}

	@Test
	public void testDeltaDebuggerWithSystemExit() {
		try {
			List<File> projectJars = new ArrayList<>();
			projectJars.add(new File("./src/test/resources/HotelReservationSystem.jar"));
			projectJars.add(new File("./src/test/resources/HotelReservationSystem-tests.jar"));
			projectJars.add(new File("./src/test/resources/system-rules-1.17.0.jar"));

			SootClass sootClass = readFromJimple("./src/test/resources/jimples/org.employee.TestFileRead_1.jimple");

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
}

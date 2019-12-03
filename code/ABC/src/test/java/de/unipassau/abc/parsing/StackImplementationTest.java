package de.unipassau.abc.parsing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.rules.TemporaryFolder;
import org.slf4j.event.Level;

import de.unipassau.abc.carving.Carver;
import de.unipassau.abc.carving.StackImplementation;
import de.unipassau.abc.carving.exceptions.CarvingException;
import de.unipassau.abc.data.ABCUtils;
import de.unipassau.abc.data.CallGraph;
import de.unipassau.abc.data.DataDependencyGraph;
import de.unipassau.abc.data.ExecutionFlowGraph;
import de.unipassau.abc.data.MethodInvocationMatcher;
import de.unipassau.abc.data.Triplette;
import de.unipassau.abc.tracing.Trace;
import de.unipassau.abc.utils.ABCTestUtils;
import de.unipassau.abc.utils.ManualTest;
import de.unipassau.abc.utils.Slf4jSimpleLoggerRule;
import soot.FastHierarchy;
import soot.G;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;

//@RunWith(PowerMockRunner.class)
//@PrepareForTest(Graph_Details.class)
public class StackImplementationTest {

	@Rule
	public TemporaryFolder temporaryFolder = new TemporaryFolder();

	@Rule
	public Slf4jSimpleLoggerRule loggerLevel = new Slf4jSimpleLoggerRule(Level.TRACE);

	private final List<MethodInvocationMatcher> emptyMethodInvocationMatcherList = new ArrayList<MethodInvocationMatcher>();

	@BeforeClass
	public static void setupSoot() {
		Carver.setupSoot(Collections.singletonList(new File(ABCTestUtils.getTestSubjectJar())));
	}

	@AfterClass
	public static void resetSoot() {
		G.reset();
	}

	@Test
	public void resolveMissingMethodFromInterfaces() {
		Carver.setupSoot(Collections
				.singletonList(new File("/Users/gambi/.m2/repository/org/hsqldb/hsqldb/2.4.0/hsqldb-2.4.0.jar")));

		try {
			ABCUtils.lookUpMethod("<java.sql.PreparedStatement: java.sql.ResultSet getResultSet()>");
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void testParserWithStoreArrays() throws FileNotFoundException, IOException {

		try {
			Carver.setupSoot(Collections.singletonList(new File(ABCTestUtils.getTestSubjectJar())));

			File traceFile = new File("./src/test/resources/ArrayHandlingClass-trace.txt");

			// Init of STRING[]
			StackImplementation stackImplementation = new StackImplementation(emptyMethodInvocationMatcherList);
			Map<String, Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>> parsedTrace = stackImplementation
					.parseTrace(traceFile.getAbsolutePath(), emptyMethodInvocationMatcherList);
			// parsedTrace.values().iterator().next().getFirst().visualize()
			// parsedTrace.values().iterator().next().getSecond().visualize()
			// parsedTrace.values().iterator().next().getThird().visualize()
			assertEquals(1, parsedTrace.size());

			// Reset

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void testTraceParseWithTraceFromTestSubject() throws Exception {

		File traceFile = new File("./src/test/resources/DummySystemTestGetSimple-trace.txt");

		StackImplementation stackImplementation = new StackImplementation(emptyMethodInvocationMatcherList);
		Map<String, Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>> parsedTrace = stackImplementation
				.parseTrace(traceFile.getAbsolutePath(), emptyMethodInvocationMatcherList);

		assertEquals(1, parsedTrace.size());

		// TODO Better assertions here !
		// parsedTrace.getFirst().visualize();
		// parsedTrace.getSecond().visualize();
		// parsedTrace.getThird().visualize();

		// System.out.println("StackImplementationTest.testTraceParseWithTraceFromTestSubject()");
	}

	@Test
	@Category(ManualTest.class)
	public void testTraceParseWithSystemIn() throws Exception {
		File traceFile = new File("/Users/gambi/action-based-test-carving/code/ABC/scripts/tracingOut/trace.txt");

		// MethodInvocationMatcher byClass =
		// MethodInvocationMatcher.byClass("org.employee.EmployeeMetaData");

		StackImplementation stackImplementation = new StackImplementation(emptyMethodInvocationMatcherList);
		Map<String, Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>> parsedTrace = stackImplementation
				.parseTrace(traceFile.getAbsolutePath(), emptyMethodInvocationMatcherList);

		// TODO Add assertions here !
		// parsedTrace.getFirst().visualize();
		// parsedTrace.getSecond().visualize();
		// parsedTrace.getThird().visualize();

		// Assert that file exists or similar ?

		System.out.println("StackImplementationTest.testTraceParseWithTraceFromTestSubject()");
	}

	/// The following tests are broken since the trace format changed !
	@Ignore
	@Test
	public void testTraceParserWithFormalParameters() throws Exception {
		// Trace file
		String trace = Trace.METHOD_START_TOKEN
				+ "SpecialInvokeExpr;<jpass.util.Configuration: java.lang.Object getValue(java.lang.String,java.lang.Object,java.lang.Class)>;(system.look.and.feel.enabled,true,class java.lang.Boolean)"
				+ "\n" + Trace.METHOD_OBJECT_TOKEN
				+ "<jpass.util.Configuration: java.lang.Object getValue(java.lang.String,java.lang.Object,java.lang.Class)>;jpass.util.Configuration@16c0663d"
				+ "\n" + Trace.METHOD_END_TOKEN
				+ "<jpass.util.Configuration: java.lang.Object getValue(java.lang.String,java.lang.Object,java.lang.Class)>;true";

		File traceFile = temporaryFolder.newFile("trace");
		try (PrintStream out = new PrintStream(new FileOutputStream(traceFile))) {
			out.print(trace);
		}

		// StackImplementation stackImplementation = new
		// StackImplementation(emptyMethodInvocationMatcherList);
		StackImplementation stackImplementation = new StackImplementation(emptyMethodInvocationMatcherList);
		// Parsing
		stackImplementation.parseTrace(traceFile.getAbsolutePath(), emptyMethodInvocationMatcherList);

		// Regression?
		Hashtable<String, String> hashParams = new Hashtable<String, String>();
		hashParams.put(
				"<jpass.util.Configuration: java.lang.Object getValue(java.lang.String,java.lang.Object,java.lang.Class)>",
				"system.look.and.feel.enabled,true,class java.lang.Boolean");

		fail("Not implemented! ");
	}

	@Ignore
	@Test
	public void testTraceParser() throws Exception {
		// Trace file
		String trace = Trace.METHOD_START_TOKEN
				+ "StaticInvokeExpr;<jpass.util.Configuration: jpass.util.Configuration getInstance()>" + "\n"
				+ Trace.METHOD_START_TOKEN + "SpecialInvokeExpr;<jpass.util.Configuration: void <init>()>" + "\n"
				+ Trace.METHOD_OBJECT_TOKEN
				+ "<jpass.util.Configuration: void <init>()>;jpass.util.Configuration@16c0663d" + "\n"
				+ Trace.METHOD_END_TOKEN + "<jpass.util.Configuration: void <init>()>;null" + "\n"
				+ Trace.METHOD_END_TOKEN
				+ "<jpass.util.Configuration: jpass.util.Configuration getInstance()>;jpass.util.Configuration@16c0663d";

		File traceFile = temporaryFolder.newFile("trace");
		try (PrintStream out = new PrintStream(new FileOutputStream(traceFile))) {
			out.print(trace);

		}

		StackImplementation stackImplementation = new StackImplementation(emptyMethodInvocationMatcherList);

		// Parsing
		stackImplementation.parseTrace(traceFile.getAbsolutePath(), emptyMethodInvocationMatcherList);

		fail("Not implemented! ");
	}

	@Ignore
	@Test
	public void testTraceParserWithMultilineStringsInObjectToken() throws Exception {
		// Trace file
		String trace = Trace.METHOD_START_TOKEN
				+ "VirtualInvokeExpr;<jpass.ui.action.TextComponentActionType: java.lang.String getName()>" + "\n"
				+ Trace.METHOD_OBJECT_TOKEN
				+ "<jpass.ui.action.TextComponentActionType: java.lang.String getName()>;SELECT_ALL\n\nB\n" + "\n"
				+ Trace.METHOD_END_TOKEN
				+ " <jpass.ui.action.TextComponentActionType: java.lang.String getName()>;jpass.text.select_all_action"
				+ "\n";

		File traceFile = temporaryFolder.newFile("trace");
		try (PrintStream out = new PrintStream(new FileOutputStream(traceFile))) {
			out.print(trace);

		}

		// StackImplementation stackImplementation = new
		// StackImplementation(emptyMethodInvocationMatcherList);
		StackImplementation stackImplementation = new StackImplementation(emptyMethodInvocationMatcherList);

		// Parsing
		stackImplementation.parseTrace(traceFile.getAbsolutePath(), emptyMethodInvocationMatcherList);

		Hashtable<String, String> hashId = new Hashtable<String, String>();
		hashId.put("<jpass.ui.action.TextComponentActionType: java.lang.String getName()>", "SELECT_ALL\n\nB\n");
		fail("Not implemented! ");
	}

	@Ignore
	@Test
	public void testTraceParserWithMultilineStringsInParameter() throws Exception {
		// Trace file
		String trace = Trace.METHOD_START_TOKEN
				+ "SpecialInvokeExpr;<jpass.util.Configuration: java.lang.Object getValue(java.lang.String,java.lang.Object,java.lang.Class)>;(system.look.and.feel.enabled\n\nA\nB\n,true,class java.lang.Boolean)"
				+ "\n" + Trace.METHOD_OBJECT_TOKEN
				+ "<jpass.util.Configuration: java.lang.Object getValue(java.lang.String,java.lang.Object,java.lang.Class)>;jpass.util.Configuration@16c0663d"
				+ "\n" + Trace.METHOD_END_TOKEN
				+ "<jpass.util.Configuration: java.lang.Object getValue(java.lang.String,java.lang.Object,java.lang.Class)>;true";

		File traceFile = temporaryFolder.newFile("trace");
		try (PrintStream out = new PrintStream(new FileOutputStream(traceFile))) {
			out.print(trace);

		}

		StackImplementation stackImplementation = new StackImplementation(emptyMethodInvocationMatcherList);

		// Parsing
		stackImplementation.parseTrace(traceFile.getAbsolutePath(), emptyMethodInvocationMatcherList);

		Hashtable<String, String> hashParams = new Hashtable<String, String>();
		hashParams.put(
				"<jpass.util.Configuration: java.lang.Object getValue(java.lang.String,java.lang.Object,java.lang.Class)>",
				"system.look.and.feel.enabled\n\nA\nB\n,true,class java.lang.Boolean");
		fail("Not implemented! ");
	}
}

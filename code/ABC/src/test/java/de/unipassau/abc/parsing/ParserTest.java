package de.unipassau.abc.parsing;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.Rule;
import org.junit.Test;
import org.slf4j.event.Level;

import de.unipassau.abc.carving.CallGraph;
import de.unipassau.abc.carving.DataDependencyGraph;
import de.unipassau.abc.carving.ExecutionFlowGraph;
import de.unipassau.abc.carving.MethodInvocationMatcher;
import de.unipassau.abc.carving.ObjectInstance;
import de.unipassau.abc.carving.StackImplementation;
import de.unipassau.abc.data.Triplette;
import de.unipassau.abc.utils.ABCTestUtils;
import de.unipassau.abc.utils.Slf4jSimpleLoggerRule;

public class ParserTest {
	@Rule
	public Slf4jSimpleLoggerRule loggerLevel = new Slf4jSimpleLoggerRule(Level.TRACE);

	@Test
	public void testParseStaticMethodStartWithArrayParameters() throws FileNotFoundException, IOException {
		List<String> lines = new ArrayList<>();
		lines.add(
				"[>];StaticInvokeExpr;<java.nio.file.Files: java.nio.file.Path createTempDirectory(java.lang.String,java.nio.file.attribute.FileAttribute[])>;(TEMP,java.nio.file.attribute.FileAttribute[]@722676897)");
		lines.add(
				"[<];<java.nio.file.Files: java.nio.file.Path createTempDirectory(java.lang.String,java.nio.file.attribute.FileAttribute[])>;/Users/gambi/Documents/Passau/Research/action-based-test-carving/code/ABC/scripts/./tracingOut/8058f1ab-1299-41b0-a03c-9a80d1333207.xml;java.nio.file.Path@71853550;");

		// Write to File
		File tmpTraceFile = ABCTestUtils.writeTraceToTempFile(lines);

		List<MethodInvocationMatcher> noMatchList = Collections.singletonList(MethodInvocationMatcher.noMatch());
		StackImplementation stackImplementation = new StackImplementation(noMatchList);
		Map<String, Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>> parsedTraces = stackImplementation
				.parseTrace(tmpTraceFile.getAbsolutePath(), noMatchList);

		assertEquals(1, parsedTraces.size());
	}

	@Test
	public void testParsingWithAlias() throws FileNotFoundException, IOException {
		List<String> lines = new ArrayList<>();
		lines.add(
				"[>];StaticInvokeExpr;<java.nio.file.Files: java.nio.file.Path createTempDirectory(java.lang.String,java.nio.file.attribute.FileAttribute[])>;(TEMP,java.nio.file.attribute.FileAttribute[]@1819937367)");
		lines.add(
				"[<];<java.nio.file.Files: java.nio.file.Path createTempDirectory(java.lang.String,java.nio.file.attribute.FileAttribute[])>;/Users/gambi/Documents/Passau/Research/action-based-test-carving/code/ABC/scripts/./tracingOut/949d221c-55ed-40a1-82ca-56cf7cda8eff.xml;java.nio.file.Path@1360263927;");
		lines.add("[>];InterfaceInvokeExpr;<java.nio.file.Path: java.io.File toFile()>");
		lines.add(
				"[||];<java.nio.file.Path: java.io.File toFile()>;/Users/gambi/Documents/Passau/Research/action-based-test-carving/code/ABC/scripts/./tracingOut/7e68ce4b-5124-40e5-8904-7ae28a984973.xml;sun.nio.fs.UnixPath@1360263927");
		lines.add(
				"[<];<java.nio.file.Path: java.io.File toFile()>;/Users/gambi/Documents/Passau/Research/action-based-test-carving/code/ABC/scripts/./tracingOut/20d5759b-8c4c-41c7-8e79-26c436fa2dcb.xml;java.io.File@1784808357;");

		// Write to File
		File tmpTraceFile = ABCTestUtils.writeTraceToTempFile(lines);

		List<MethodInvocationMatcher> noMatchListAsPurity = Collections.singletonList(MethodInvocationMatcher.noMatch());
		List<MethodInvocationMatcher> externalInterfaceMatcher = Collections.singletonList(MethodInvocationMatcher.byClass("java.nio.file.Files"));
		StackImplementation stackImplementation = new StackImplementation( noMatchListAsPurity);
		
		Map<String, Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>> parsedTraces = stackImplementation
				.parseTrace(tmpTraceFile.getAbsolutePath(), externalInterfaceMatcher);

		assertEquals(1, parsedTraces.size());
		//
		DataDependencyGraph dataDependencyGraph = parsedTraces.values().iterator().next().getSecond();
		ObjectInstance alias = new ObjectInstance("sun.nio.fs.UnixPath@1360263927");
		
		assertFalse("Data Dep Graph contains alias!", dataDependencyGraph.getObjectInstances().contains( alias ));
	}
}

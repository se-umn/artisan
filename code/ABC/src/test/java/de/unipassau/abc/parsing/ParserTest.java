package de.unipassau.abc.parsing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.event.Level;

import de.unipassau.abc.carving.CallGraph;
import de.unipassau.abc.carving.Carver;
import de.unipassau.abc.carving.DataDependencyGraph;
import de.unipassau.abc.carving.ExecutionFlowGraph;
import de.unipassau.abc.carving.MethodInvocationMatcher;
import de.unipassau.abc.carving.ObjectInstance;
import de.unipassau.abc.carving.StackImplementation;
import de.unipassau.abc.data.Triplette;
import de.unipassau.abc.utils.ABCTestUtils;
import de.unipassau.abc.utils.Slf4jSimpleLoggerRule;
import edu.emory.mathcs.backport.java.util.Arrays;
import soot.G;

public class ParserTest {

	@Rule
	public Slf4jSimpleLoggerRule loggerLevel = new Slf4jSimpleLoggerRule(Level.TRACE);

//	@BeforeClass
//	public static void setupSoot() {
//		Carver.setupSoot(Collections.EMPTY_LIST);
//	}

//	@AfterClass
//	public static void resetSoot() {
//		G.reset();
//	}

	@Test
	public void testParseMethodEndWithoutXML(){
//		Pattern pattern = Pattern.compile("^http://.*?/(.*?)/.*?$");
//		Matcher matcher = pattern.matcher(urlString);
//		matcher.group(0);
		
		String methodEnd = "[<];<java.io.File: boolean createNewFile()>;;;true";
		String[] tokens = methodEnd.split(";");
		System.out.println( Arrays.toString( tokens ));
		
		
	}
	
	@Test
	public void testParseStaticMethodStartWithArrayParameters() throws FileNotFoundException, IOException {
		List<String> lines = new ArrayList<>();
		lines.add("[>];ArrayOperation;<java.nio.file.attribute.FileAttribute[]: void <init>(int)>;(0)");
		lines.add(
				"[||];<java.nio.file.attribute.FileAttribute[]: void <init>(int)>;/var/folders/jf/9l21bwgd7czdw8jm38z2pr3m0000gq/T/tmp-dump8070163515588955062/f5860eaa-f79e-4a0e-b7c9-a92a206bc837.xml;java.nio.file.attribute.FileAttribute[]@1148232870");
		lines.add("[<];<java.nio.file.attribute.FileAttribute[]: void <init>(int)>;;");
		lines.add("[>];StringOperation;<java.lang.String: void <init>()>");
		lines.add(
				"[||];<java.lang.String: void <init>()>;/var/folders/jf/9l21bwgd7czdw8jm38z2pr3m0000gq/T/tmp-dump8070163515588955062/bdf9d744-f081-4e94-b862-f9566851d393.xml;java.lang.String@2127000850");
		lines.add("[<];<java.lang.String: void <init>()>;;");
		lines.add(
				"[>];StaticInvokeExpr;<java.nio.file.Files: java.nio.file.Path createTempDirectory(java.lang.String,java.nio.file.attribute.FileAttribute[])>;(java.lang.String@2127000850,java.nio.file.attribute.FileAttribute[]@1148232870)");
		lines.add(
				"[<];<java.nio.file.Files: java.nio.file.Path createTempDirectory(java.lang.String,java.nio.file.attribute.FileAttribute[])>;/var/folders/jf/9l21bwgd7czdw8jm38z2pr3m0000gq/T/tmp-dump8070163515588955062/023240bb-ae6e-44c1-94d5-5d5f07c44e66.xml;java.nio.file.Path@402942061;");

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
		lines.add("[>];ArrayOperation;<java.nio.file.attribute.FileAttribute[]: void <init>(int)>;(0)");
		lines.add(
				"[||];<java.nio.file.attribute.FileAttribute[]: void <init>(int)>;/var/folders/jf/9l21bwgd7czdw8jm38z2pr3m0000gq/T/tmp-dump8070163515588955062/f5860eaa-f79e-4a0e-b7c9-a92a206bc837.xml;java.nio.file.attribute.FileAttribute[]@1148232870");
		lines.add("[<];<java.nio.file.attribute.FileAttribute[]: void <init>(int)>;;");
		lines.add("[>];StringOperation;<java.lang.String: void <init>()>");
		lines.add(
				"[||];<java.lang.String: void <init>()>;/var/folders/jf/9l21bwgd7czdw8jm38z2pr3m0000gq/T/tmp-dump8070163515588955062/bdf9d744-f081-4e94-b862-f9566851d393.xml;java.lang.String@2127000850");
		lines.add("[<];<java.lang.String: void <init>()>;;");
		lines.add(
				"[>];StaticInvokeExpr;<java.nio.file.Files: java.nio.file.Path createTempDirectory(java.lang.String,java.nio.file.attribute.FileAttribute[])>;(java.lang.String@2127000850,java.nio.file.attribute.FileAttribute[]@1148232870)");
		lines.add(
				"[<];<java.nio.file.Files: java.nio.file.Path createTempDirectory(java.lang.String,java.nio.file.attribute.FileAttribute[])>;/var/folders/jf/9l21bwgd7czdw8jm38z2pr3m0000gq/T/tmp-dump8070163515588955062/023240bb-ae6e-44c1-94d5-5d5f07c44e66.xml;java.nio.file.Path@402942061;");
		lines.add("[>];InterfaceInvokeExpr;<java.nio.file.Path: java.io.File toFile()>");
		lines.add(
				"[||];<java.nio.file.Path: java.io.File toFile()>;/var/folders/jf/9l21bwgd7czdw8jm38z2pr3m0000gq/T/tmp-dump8070163515588955062/e67d3b19-6e38-44ed-ae31-38d7aee61910.xml;sun.nio.fs.UnixPath@402942061");
		lines.add(
				"[<];<java.nio.file.Path: java.io.File toFile()>;/var/folders/jf/9l21bwgd7czdw8jm38z2pr3m0000gq/T/tmp-dump8070163515588955062/000990d3-b542-40f2-a685-09de1a94634f.xml;java.io.File@1777763698;");

		// Write to File
		File tmpTraceFile = ABCTestUtils.writeTraceToTempFile(lines);

		List<MethodInvocationMatcher> noMatchListAsPurity = Collections
				.singletonList(MethodInvocationMatcher.noMatch());
		List<MethodInvocationMatcher> externalInterfaceMatcher = Collections
				.singletonList(MethodInvocationMatcher.byClass("java.nio.file.Files"));
		StackImplementation stackImplementation = new StackImplementation(noMatchListAsPurity);

		Map<String, Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>> parsedTraces = stackImplementation
				.parseTrace(tmpTraceFile.getAbsolutePath(), externalInterfaceMatcher);

		assertEquals(1, parsedTraces.size());
		//
		DataDependencyGraph dataDependencyGraph = parsedTraces.values().iterator().next().getSecond();
		ObjectInstance alias = new ObjectInstance("sun.nio.fs.UnixPath@1360263927");

		assertFalse("Data Dep Graph contains alias!", dataDependencyGraph.getObjectInstances().contains(alias));
	}
}

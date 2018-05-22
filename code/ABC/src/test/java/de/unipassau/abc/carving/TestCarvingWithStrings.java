package de.unipassau.abc.carving;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.slf4j.event.Level;

import com.google.common.io.Files;

import de.unipassau.abc.data.Triplette;
import de.unipassau.abc.utils.Slf4jSimpleLoggerRule;

public class TestCarvingWithStrings {

	@Rule
	public TemporaryFolder temporaryFolder = new TemporaryFolder();
	
	@Rule
	public Slf4jSimpleLoggerRule loggerLevelRule = new Slf4jSimpleLoggerRule(Level.TRACE);
	
	@BeforeClass
	public static void setupSoot(){
		Carver.setupSoot( Collections.EMPTY_LIST );
	}
	
	@Test
	public void testWithStringDeclaration()throws Exception{
		String trace = "[>];ArrayOperation;<java.nio.file.attribute.FileAttribute[]: void <init>(int)>;(0)" + "\n" +
					"[||];<java.nio.file.attribute.FileAttribute[]: void <init>(int)>;/Users/gambi/Documents/Passau/Research/action-based-test-carving/code/ABC/scripts/./tracingOut/2f5ae63f-e866-454a-941d-c5ade0081876.xml;java.nio.file.attribute.FileAttribute[]@1025922771" + "\n" +
					"[<];<java.nio.file.attribute.FileAttribute[]: void <init>(int)>;;" + "\n" +
					// The string passed as parameter here is not initialized
					// TODO Check if tracing actually trace that
					"[>];StaticInvokeExpr;<java.nio.file.Files: java.nio.file.Path createTempDirectory(java.lang.String,java.nio.file.attribute.FileAttribute[])>;(java.lang.String@537810434,java.nio.file.attribute.FileAttribute[]@1025922771)" + "\n" +
					"[<];<java.nio.file.Files: java.nio.file.Path createTempDirectory(java.lang.String,java.nio.file.attribute.FileAttribute[])>;/Users/gambi/Documents/Passau/Research/action-based-test-carving/code/ABC/scripts/./tracingOut/91be6409-2c65-4604-858d-c0c5686b4915.xml;java.nio.file.Path@1319106496;" + "\n" +
					"[>];InterfaceInvokeExpr;<java.nio.file.Path: java.io.File toFile()>" + "\n" +
					"[||];<java.nio.file.Path: java.io.File toFile()>;/Users/gambi/Documents/Passau/Research/action-based-test-carving/code/ABC/scripts/./tracingOut/1b0408ed-40fb-486f-86ca-d81192d6ceaa.xml;sun.nio.fs.UnixPath@1319106496" + "\n" +
					"[<];<java.nio.file.Path: java.io.File toFile()>;/Users/gambi/Documents/Passau/Research/action-based-test-carving/code/ABC/scripts/./tracingOut/2a050612-2784-451e-895c-5b2d46af9ab5.xml;java.io.File@1974914672;" + "\n";
		
		File traceFile = temporaryFolder.newFile();
		Files.write( trace.getBytes(), traceFile);
		
		StackImplementation parser = new StackImplementation( Collections.EMPTY_LIST);
		Map<String, Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>> parsedTrace = parser.parseTrace(traceFile.getAbsolutePath(), Collections.EMPTY_LIST );
		
		
	}
}


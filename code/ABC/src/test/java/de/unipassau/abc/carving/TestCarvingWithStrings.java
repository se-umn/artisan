package de.unipassau.abc.carving;

import java.io.File;
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
		String trace = "[>];MainArgsOperation;<abc.MainArgs: java.lang.String[] getMainArgs()>"+//
				"[<];<abc.MainArgs: java.lang.String[] getMainArgs()>;;/Users/gambi/Documents/Passau/Research/action-based-test-carving/test-subjects/OriginalEvaluation/ApplicationCarvingDemo/data/abc-trace/916a8e7c-779c-4cbb-8b36-22cd129d0b38.xml;java.lang.String[]@532854629;"+//
				"[>];SpecialInvokeExpr;<directory.CmdLineParser: void <init>()>"+//
				"[||];<directory.CmdLineParser: void <init>()>;directory.CmdLineParser@315138752"+//
				"[<];<directory.CmdLineParser: void <init>()>;/Users/gambi/Documents/Passau/Research/action-based-test-carving/test-subjects/OriginalEvaluation/ApplicationCarvingDemo/data/abc-trace/866f46a2-2e0f-4261-9dca-b811ed950e20.xml;;"+//
				"[>];StaticInvokeExpr;<directory.AbstractDirectoryDAO: directory.DirectoryDAO getDefault()>"+//
				"[>];ClassOperation;<abc.Class: java.lang.Class get()>"+//
				"[<];<abc.Class: java.lang.Class get()>;;/Users/gambi/Documents/Passau/Research/action-based-test-carving/test-subjects/OriginalEvaluation/ApplicationCarvingDemo/data/abc-trace/08cb94b0-263a-4718-b8bb-06345f03d250.xml;java.lang.Class@897074030;"+//
				"[>];StaticInvokeExpr;<org.apache.log4j.Logger: org.apache.log4j.Logger getLogger(java.lang.Class)>;(java.lang.Class@897074030)"+//
				"[<];<org.apache.log4j.Logger: org.apache.log4j.Logger getLogger(java.lang.Class)>;;;org.apache.log4j.Logger@999609945;"+//
				"[>];FieldOperation;<abc.Field: void set(org.apache.log4j.Logger)>;(org.apache.log4j.Logger@999609945)"+//
				"[<];<abc.Field: void set(org.apache.log4j.Logger)>;;;"+//
				"[>];StaticFieldOperation;<abc.StaticField: org.apache.log4j.Logger get(java.lang.String)>;(directory.HSQLDBDirectoryDAO.logger)"+//
				"[<];<abc.StaticField: org.apache.log4j.Logger get(java.lang.String)>;;/Users/gambi/Documents/Passau/Research/action-based-test-carving/test-subjects/OriginalEvaluation/ApplicationCarvingDemo/data/abc-trace/155f996e-7fea-462e-a24d-937589f4f206.xml;org.apache.log4j.Logger@999609945;"+//
				"[>];SpecialInvokeExpr;<org.apache.log4j.PatternLayout: void <init>()>"+//
				"[||];<org.apache.log4j.PatternLayout: void <init>()>;org.apache.log4j.PatternLayout@2007331442"+//
				"[<];<org.apache.log4j.PatternLayout: void <init>()>;;;"+//
				"[>];SpecialInvokeExpr;<org.apache.log4j.ConsoleAppender: void <init>(org.apache.log4j.Layout)>;(org.apache.log4j.PatternLayout@2007331442)"+//
				"[||];<org.apache.log4j.ConsoleAppender: void <init>(org.apache.log4j.Layout)>;org.apache.log4j.ConsoleAppender@1848415041"+//
				"[<];<org.apache.log4j.ConsoleAppender: void <init>(org.apache.log4j.Layout)>;;;"+//
				"[>];VirtualInvokeExpr;<org.apache.log4j.Logger: void addAppender(org.apache.log4j.Appender)>;(org.apache.log4j.ConsoleAppender@1848415041)"+//
				"[||];<org.apache.log4j.Logger: void addAppender(org.apache.log4j.Appender)>;org.apache.log4j.Logger@999609945"+//
				"[<];<org.apache.log4j.Logger: void addAppender(org.apache.log4j.Appender)>;;;"+//
				"[>];StaticFieldOperation;<abc.StaticField: org.apache.log4j.Logger get(java.lang.String)>;(directory.HSQLDBDirectoryDAO.logger)"+//
				"[<];<abc.StaticField: org.apache.log4j.Logger get(java.lang.String)>;;/Users/gambi/Documents/Passau/Research/action-based-test-carving/test-subjects/OriginalEvaluation/ApplicationCarvingDemo/data/abc-trace/5af70674-ce8d-42c5-a4cc-21c1f8e2b9fc.xml;org.apache.log4j.Logger@999609945;"+//
				"[>];StaticFieldOperation;<abc.StaticField: org.apache.log4j.Level get(java.lang.String)>;(org.apache.log4j.Level.OFF)"+//
				"[<];<abc.StaticField: org.apache.log4j.Level get(java.lang.String)>;;/Users/gambi/Documents/Passau/Research/action-based-test-carving/test-subjects/OriginalEvaluation/ApplicationCarvingDemo/data/abc-trace/8b471830-f646-4916-b527-f535b7380ea7.xml;org.apache.log4j.Level@667346055;"+//
				"[>];VirtualInvokeExpr;<org.apache.log4j.Logger: void setLevel(org.apache.log4j.Level)>;(org.apache.log4j.Level@667346055)"+//
				"[||];<org.apache.log4j.Logger: void setLevel(org.apache.log4j.Level)>;org.apache.log4j.Logger@999609945"+//
				"[<];<org.apache.log4j.Logger: void setLevel(org.apache.log4j.Level)>;;;"+//
				"[>];SpecialInvokeExpr;<directory.HSQLDBDirectoryDAO: void <init>()>"+//
				"[>];SpecialInvokeExpr;<org.hsqldb.jdbcDriver: void <init>()>"+//
				"[||];<org.hsqldb.jdbcDriver: void <init>()>;org.hsqldb.jdbcDriver@103536485"+//
				"[<];<org.hsqldb.jdbcDriver: void <init>()>;;;"+//
				"[>];SpecialInvokeExpr;<java.util.Properties: void <init>()>"+//
				"[||];<java.util.Properties: void <init>()>;java.util.Properties@37380050"+//
				"[<];<java.util.Properties: void <init>()>;;;"+//
				"[>];StringOperation;<abc.String: java.lang.String set()>"+//
				"[<];<abc.String: java.lang.String set()>;;/Users/gambi/Documents/Passau/Research/action-based-test-carving/test-subjects/OriginalEvaluation/ApplicationCarvingDemo/data/abc-trace/a2da1287-2dfe-4043-beac-a4f63ed6db12.xml;java.lang.String@231977479;"+//
				"[>];StringOperation;<abc.String: java.lang.String set()>"+//
				"[<];<abc.String: java.lang.String set()>;;/Users/gambi/Documents/Passau/Research/action-based-test-carving/test-subjects/OriginalEvaluation/ApplicationCarvingDemo/data/abc-trace/f11a5cd0-d70f-4969-8f7a-f2f37ad56d67.xml;java.lang.String@93314457;"+//
				"[>];VirtualInvokeExpr;<java.util.Properties: java.lang.Object setProperty(java.lang.String,java.lang.String)>;(java.lang.String@231977479,java.lang.String@93314457)"+//
				"[||];<java.util.Properties: java.lang.Object setProperty(java.lang.String,java.lang.String)>;java.util.Properties@37380050"+//
				"[<];<java.util.Properties: java.lang.Object setProperty(java.lang.String,java.lang.String)>;;;java.lang.Object@0;"+//
				"[>];StringOperation;<abc.String: java.lang.String set()>"+//
				"[<];<abc.String: java.lang.String set()>;;/Users/gambi/Documents/Passau/Research/action-based-test-carving/test-subjects/OriginalEvaluation/ApplicationCarvingDemo/data/abc-trace/fd936ec1-5316-4f8a-8ecb-0fdba5f03bc1.xml;java.lang.String@2076287037;"+//
				"[>];StringOperation;<abc.String: java.lang.String set()>"+//
				"[<];<abc.String: java.lang.String set()>;;/Users/gambi/Documents/Passau/Research/action-based-test-carving/test-subjects/OriginalEvaluation/ApplicationCarvingDemo/data/abc-trace/a8076593-9acb-440e-a3d0-a6a818e819fa.xml;java.lang.String@195615004;"+//
				"[>];VirtualInvokeExpr;<java.util.Properties: java.lang.Object setProperty(java.lang.String,java.lang.String)>;(java.lang.String@2076287037,java.lang.String@195615004)"+//
				"[||];<java.util.Properties: java.lang.Object setProperty(java.lang.String,java.lang.String)>;java.util.Properties@37380050"+//
				"[<];<java.util.Properties: java.lang.Object setProperty(java.lang.String,java.lang.String)>;;;java.lang.Object@0;"+//
				"[>];StringOperation;<abc.String: java.lang.String set()>"+//
				"[<];<abc.String: java.lang.String set()>;;/Users/gambi/Documents/Passau/Research/action-based-test-carving/test-subjects/OriginalEvaluation/ApplicationCarvingDemo/data/abc-trace/e583dbed-08ff-4ea4-878e-88427a193b01.xml;java.lang.String@97652294;"+//
				"[>];StaticInvokeExpr;<java.lang.System: void exit(int)>;(0)";

		
		File traceFile = temporaryFolder.newFile();
		Files.write( trace.getBytes(), traceFile);
		
		StackImplementation parser = new StackImplementation( Collections.EMPTY_LIST);
		Map<String, Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>> parsedTrace = parser.parseTrace(traceFile.getAbsolutePath(), Collections.EMPTY_LIST );
		
		
		System.out.println("TestCarvingWithStrings.testWithStringDeclaration() DONE");
	}
}


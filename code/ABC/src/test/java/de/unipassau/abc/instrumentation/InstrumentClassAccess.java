package de.unipassau.abc.instrumentation;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.rules.TemporaryFolder;
import org.slf4j.event.Level;

import de.unipassau.abc.ABCUtils;
import de.unipassau.abc.utils.Slf4jSimpleLoggerRule;
import de.unipassau.abc.utils.SystemTest;

public class InstrumentClassAccess {

		@Rule
		public Slf4jSimpleLoggerRule loggerLevelRule = new Slf4jSimpleLoggerRule(Level.TRACE);

		@Rule
		public TemporaryFolder temporaryFolder = new TemporaryFolder();

		private static File traceJar = new File(ABCUtils.getTraceJar());

//		@BeforeClass
//		public static void setupTraceJar() {
//			// Use this if you want to inspect the traces afterwards
//			// File outputDir = com.google.common.io.Files.createTempDir();
//			// File traceOutput = File.createTempFile("trace", ".txt");
//
//			traceJar = new File("./libs/trace.jar"); // Eclipse testing
//			if (!traceJar.exists()) {
//				traceJar = new File("../libs/trace.jar"); // Actual usage ...
//				if (!traceJar.exists()) {
//					throw new RuntimeException("trace.jar file is missing");
//				}
//			}
//		}

		@Test
		@Category(SystemTest.class)
		public void instrumentAndTraceTestSubjects() throws URISyntaxException, IOException, InterruptedException {
			File outputDir = temporaryFolder.newFolder();

			// Note that this includes both SUT and its test. If we do not trace
			// test cases, then we might miss information for carving
			File testsubjectJar = new File("./libs/testsubject-tests.jar");

			InstrumentTracer tracer = new InstrumentTracer();
			tracer.main(new String[] { "--project-jar", testsubjectJar.getAbsolutePath(), //
					"--output-to", outputDir.getAbsolutePath(), //
					"--output-type", "jimple", 
					"--exclude", "de.unipassau.abc.testsubject2.*",
					"--include", "de.unipassau.abc.testsubject2.Main" });

			// TODO Separate methos call; Visualize the JIMPLE Files
			for( File jimpleFile : outputDir.listFiles(new FilenameFilter() {

				@Override
				public boolean accept(File dir, String name) {
					return name.endsWith(".jimple");
				}
			})){
				System.out.println("==================================");
				System.out.println(" " + jimpleFile.getAbsolutePath());
				System.out.println("==================================");
				for( String line : Files.readAllLines( jimpleFile.toPath(), Charset.defaultCharset() ) ){
					System.out.println( line );
				}
				System.out.println("==================================");
			}

			


		}

}

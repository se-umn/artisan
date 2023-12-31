package de.unipassau.abc.instrumentation;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.rules.TemporaryFolder;
import org.slf4j.event.Level;

import de.unipassau.abc.data.ABCUtils;
import de.unipassau.abc.utils.Slf4jSimpleLoggerRule;
import de.unipassau.abc.utils.SystemTest;

public class InstrumentEmployeeTest {

	@Rule
	public Slf4jSimpleLoggerRule loggerLevelRule = new Slf4jSimpleLoggerRule(Level.TRACE);

	@Rule
	public TemporaryFolder temporaryFolder = new TemporaryFolder();

	private static File traceJar = new File(ABCUtils.getTraceJar());

	@Test
	@Category(SystemTest.class)
	public void instrumentAndTrace() throws URISyntaxException, IOException {
		File outputDir = temporaryFolder.newFolder();
		InstrumentTracer tracer = new InstrumentTracer();

		tracer.main(new String[] { "--project-jar", "./src/test/resources/Employee.jar", //
				"--output-to", outputDir.getAbsolutePath(), //
				"--output-type", "jimple" });

		// TODO Maybe a Hamcrest matcher here?
		assertEquals(11, outputDir.listFiles(new FilenameFilter() {

			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".jimple");
			}
		}).length);
		
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

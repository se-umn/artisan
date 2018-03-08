package de.unipassau.carving;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.rules.TemporaryFolder;
import org.slf4j.event.Level;

import de.unipassau.utils.Slf4jSimpleLoggerRule;
import de.unipassau.utils.SystemTest;

public class CarverTest {

	@Rule
	public TemporaryFolder temporaryFolderRule = new TemporaryFolder();
	
	@Rule
	public Slf4jSimpleLoggerRule loggerLevelRule = new Slf4jSimpleLoggerRule(Level.DEBUG);
	
	@Test
	@Category(SystemTest.class)
	public void testCarver() throws IOException, InterruptedException {
		Carver carver = new Carver();
		File outputDirectory = temporaryFolderRule.newFolder();
		String[] args = new String[] {
				// String methodToBeCarved =
				"<org.employee.Validation: int numberValidation(java.lang.String)>",
				// String traceFile =
				"./src/test/resources/Employee-trace.txt",
				// String projectJar =
				"./src/test/resources/Employee.jar",
				// String outputDir =
				outputDirectory.getAbsolutePath() };
		//
		//
		carver.main(args);
		// 
		assertEquals(2, outputDirectory.listFiles().length );

	}
}

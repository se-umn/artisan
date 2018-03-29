package de.unipassau.abc.tracing;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class TraceTest {

	@Rule
	public TemporaryFolder temporaryFolder = new TemporaryFolder();

	private File traceFile;

	@Before
	public void setOutputDir() throws IOException {
		File outputFolder = temporaryFolder.newFolder();
		System.setProperty("trace.output", outputFolder.getAbsolutePath());
		traceFile  = new File( outputFolder, "trace.txt");
	}

	@Test
	public void processMethodStartWithBoxedTypes() throws IOException {
		Trace.methodStart("InterfaceInvokeExpr", "<java.sql.Connection: void setAutoCommit(boolean)>",
				new Boolean(true));
		for(String s : Files.readAllLines( traceFile.toPath(), Charset.defaultCharset())){
			System.out.println("TraceTest.processMethodStartWithBoxedTypes() " + s);
		}
		
	}
}

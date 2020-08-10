package de.unipassau.abc.parsing;

import java.io.File;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.slf4j.event.Level;

import de.unipassau.abc.carving.exceptions.CarvingException;
import de.unipassau.abc.utils.Slf4jSimpleLoggerRule;

@Ignore
public class DuafDroidParserTest {

	@Rule
	public TemporaryFolder tempFolder = new TemporaryFolder();

	@Rule
	public Slf4jSimpleLoggerRule loggerLevelRule = new Slf4jSimpleLoggerRule(Level.INFO);

	@Test
	public void mainTestSingleThread() throws IOException, CarvingException {

		File outputTo = new File("src/test/resources/android-28-traces/Notepad-Alessio/single"); // tempFolder.newFile("parsed.xml");
		outputTo.mkdirs();
		File traceFile = new File("src/test/resources/apps/Notepad-Alessio/traces/trace.log");
		String apk = "/Users/gambi/MyDroidFax/apks/Notepad-Alessio.apk";
		String androidJar = "/Users/gambi/Library/Android/sdk/platforms/android-28/android.jar";

		String[] args = new String[] { "--trace-files", traceFile.getAbsolutePath(), "--store-artifacts-to",
				outputTo.getAbsolutePath(), "--apk", apk, "--android-jar", androidJar };

		Main.main(args);

//        // Check that this is actually happening
//        XStream xStream = new XStream();
//
//        // Read all the files from the folder
//        Map<String, Map<String, Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>>> parsedTraceFiles = new HashMap<>();
//        for (File file : outputTo.listFiles(new FilenameFilter() {
//
//            @Override
//            public boolean accept(File dir, String name) {
//                return name.startsWith(traceFile.getName()) && name.endsWith(".parsed.xml");
//            }
//        })) {
//            System.out.println("DuafDroidParserTest.mainTest() Reading from " + file);
//            parsedTraceFiles.put(file.getAbsolutePath(),
//                    (Map<String, Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>>) xStream.fromXML(file));
//        }
		ParsedTrace parsedTrace = ParsedTraceImpl.loadFromDirectory(outputTo);
		Assert.assertEquals(1, parsedTrace.getThreadCount());
	}

	@Test // Note this takes minutes to complete since tracking AsynchTasks
			// explodes (they use plenty of StringBuilder invocations)
	// TODO This fails with OOM CG Overhead. I suspect that the code is not
	// really optimized
	public void mainTestMultiThread() throws IOException, CarvingException {

		// File outputTo = tempFolder.newFolder("ABC-DuafDroidParserTest");
		File outputTo = new File("src/test/resources/apps/org.ametro/parsed/multi");
		outputTo.mkdirs();

		File traceFile = new File("src/test/resources/apps/org.ametro/traces/trace.log");
		String apk = "/Users/gambi/MyDroidFax/apks/filtered-apps/org.ametro/apk/org.ametro_40.apk";
		String androidJar = "/Users/gambi/Library/Android/sdk/platforms/android-28/android.jar";

		String[] args = new String[] { "--trace-files", traceFile.getAbsolutePath(), "--store-artifacts-to",
				outputTo.getAbsolutePath(), "--apk", apk, "--android-jar", androidJar };

		Main.main(args);

		// Check that this is actually happening
		ParsedTrace parsedTrace = ParsedTraceImpl.loadFromDirectory(outputTo);

		Assert.assertEquals(8, parsedTrace.getThreadCount());
	}

}

package de.unipassau.abc.evaluation;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.runner.JunitTestRunner;

public class JUnitTestExecutor {

	public final static String JAVA = "pah.java";

	private static String OS = System.getProperty("os.name").toLowerCase();

	/**
	 * Return the specified java environment or simply java, assuming that your java
	 * is configured to be the java command from the JDK 11
	 * 
	 * @return
	 */
	private String getJava() {
		return System.getProperty(JAVA, "java");
	}

	// https://mkyong.com/java/how-to-detect-os-in-java-systemgetpropertyosname/
	private boolean isWindows() {
		return (OS.indexOf("win") >= 0);
	}

	private String getEncoding() {
		if (isWindows()) {
			// Wrap the option in `"` for Windows. TODO Is this really necessary?
			return '"' + "-Dfile.encoding=UTF-8" + '"';
		} else {
			return "-Dfile.encoding=UTF-8";
		}
	}

	List<String> cpArgs = new ArrayList<String>();

	public JUnitTestExecutor(List<String> cpEntries) {
		// Set the class path for test execution. All the tests will use the same
		// configuration since they are application specific
		cpArgs.add("-cp");
		cpArgs.add(String.join(File.pathSeparator, cpEntries));
	}

	/**
	 * Execute the test using a "remote" JVM. Returns an object containing the
	 * output of the execution.
	 * 
	 * @param args
	 * @throws IOException
	 */
	public Map<String, Object> executeTest(String test, String... jvmFlags) throws Exception {
		List<String> _args = new ArrayList<String>();
		_args.add(getJava());
		_args.add(getEncoding());
		// Classpath
		_args.addAll(cpArgs);

		// Include any given jvmFlag
		for (String jvmFlag : jvmFlags) {
			_args.add("-D" + jvmFlag);
		}

		// This is the main java class that wraps JUnit Core
		_args.add(JunitTestRunner.class.getName());

		// This is the test to run
		_args.add(test);

		System.out.println("RUNNING " + _args);
		ProcessBuilder pb = new ProcessBuilder(_args);

		Process process = pb.start();

		BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), "UTF-8"));
		StringBuilder builder = new StringBuilder();
		String line = null;
		while ((line = reader.readLine()) != null) {
			builder.append(line);
			builder.append(System.getProperty("line.separator"));
		}

		BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
		StringBuilder errorBuilder = new StringBuilder();
		String errorLine = null;
		while ((errorLine = errorReader.readLine()) != null) {
			errorBuilder.append(errorLine);
			errorBuilder.append(System.getProperty("line.separator"));
		}

		String stdOut = builder.toString();
		String stdError = errorBuilder.toString();
		int exitCode = process.waitFor();

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("exitCode", exitCode);
		result.put("stdOut", stdOut);
		result.put("stdError", stdError);

		return result;

	}
}
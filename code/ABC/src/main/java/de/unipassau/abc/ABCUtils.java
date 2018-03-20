package de.unipassau.abc;

import java.io.File;

public class ABCUtils {

	public static String getTraceJar() {
		String traceJar = "./libs/trace.jar"; // Eclipse testing

		if (!new File(traceJar).exists()) {
			traceJar = "../libs/trace.jar"; // Actual usage ...
			if (!new File(traceJar).exists()) {
				throw new RuntimeException(new File(traceJar).getAbsolutePath() + " file is missing");
			}
		}

		return traceJar;
	}

	// I do not like this...
	public static String getSystemRulesJar() {
		String jar = "./src/main/resources/system-rules-1.17.0.jar";; // Eclipse testing

		if (!new File(jar).exists()) {
			jar = "../src/main/resources/system-rules-1.17.0.jar"; // Actual usage ...
			if (!new File(jar).exists()) {
				throw new RuntimeException(new File(jar).getAbsolutePath() + " file is missing");
			}
		}

		return jar;
	}
}

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
}

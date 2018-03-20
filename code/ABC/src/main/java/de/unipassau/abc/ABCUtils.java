package de.unipassau.abc;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
	
	// I do not like this...
	public static List<String> getXStreamJars() {
		List<String> jars = new ArrayList<>();
		for( String jarName : new String[]{"xmlpull-1.1.3.1.jar", "xpp3_min-1.1.4c.jar", "xstream-1.4.10.jar"}){
			String jar = "./src/test/resources/"+jarName; // Eclipse testing
			
			if (!new File(jar).exists()) {
				jar = "../src/test/resources/"+jarName; // Actual usage ...
				if (!new File(jar).exists()) {
					throw new RuntimeException(new File(jar).getAbsolutePath() + " file is missing");
				}
			}
			
		}

		return jars;
	}
}

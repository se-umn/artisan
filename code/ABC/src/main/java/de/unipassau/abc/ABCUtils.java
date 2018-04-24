package de.unipassau.abc;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.SystemUtils;

public class ABCUtils {

	public static String getTraceJar() {
		String traceJar = new File("./libs/trace.jar").getAbsolutePath(); // Eclipse testing

		if (!new File(traceJar).exists()) {
			traceJar = new File("../libs/trace.jar").getAbsolutePath(); // Actual usage ...
			if (!new File(traceJar).exists()) {
				throw new RuntimeException(new File(traceJar).getAbsolutePath() + " file is missing");
			}
		}

		return traceJar;
	}

	// I do not like this...
	public static String getSystemRulesJar() {
		String jar = new File("./src/main/resources/system-rules-1.17.0.jar").getAbsolutePath();

		if (!new File(jar).exists()) {
			jar = new File("../src/main/resources/system-rules-1.17.0.jar").getAbsolutePath(); // Actual
																	// usage ...
			if (!new File(jar).exists()) {
				throw new RuntimeException(new File(jar).getAbsolutePath() + " file is missing");
			}
		}

		return jar;
	}

	// I do not like this...
	// public static List<String> getXStreamJars() {
	// List<String> jars = new ArrayList<>();
	// for( String jarName : new String[]{"xmlpull-1.1.3.1.jar",
	// "xpp3_min-1.1.4c.jar", "xstream-1.4.10.jar"}){
	// String jar = "./src/test/resources/"+jarName; // Eclipse testing
	//
	// if (!new File(jar).exists()) {
	// jar = "../src/test/resources/"+jarName; // Actual usage ...
	// if (!new File(jar).exists()) {
	// throw new RuntimeException(new File(jar).getAbsolutePath() + " file is
	// missing");
	// }
	// }
	//
	// }
	//
	// return jars;
	// }

	// I do not like this...
	public static List<File> getXStreamJars() {
		List<File> jars = new ArrayList<>();
		for (String jarName : new String[] { "xmlpull-1.1.3.1.jar", "xpp3_min-1.1.4c.jar", "xstream-1.4.10.jar" }) {
			String jar = new File("./src/test/resources/" + jarName).getAbsolutePath(); // Eclipse testing

			if (!new File(jar).exists()) {
				jar = new File("../src/test/resources/" + jarName).getAbsolutePath(); // Actual usage ...
				if (!new File(jar).exists()) {
					throw new RuntimeException(new File(jar).getAbsolutePath() + " file is missing");
				}
			}

			jars.add(new File(jar));

		}

		return jars;
	}

	public static String buildJUnit4Classpath() {
		StringBuilder junitCPBuilder = new StringBuilder();
		for (String cpEntry : SystemUtils.JAVA_CLASS_PATH.split(File.pathSeparator)) {
			if (cpEntry.contains("junit-4")) {
				junitCPBuilder.append(cpEntry).append(File.pathSeparator);
			} else if (cpEntry.contains("hamcrest-core")) {
				junitCPBuilder.append(cpEntry).append(File.pathSeparator);
			}
		}
		junitCPBuilder.reverse().deleteCharAt(0).reverse();
		return junitCPBuilder.toString();

	}

	public static String getJacocoAget() {
//		
		String agentJar = new File("./libs/jacocoagent.jar").getAbsolutePath(); // Eclipse testing

		if (!new File(agentJar).exists()) {
			agentJar = new File("../libs/jacocoagent.jar").getAbsolutePath(); // Actual usage ...
			if (!new File(agentJar).exists()) {
				throw new RuntimeException(new File(agentJar).getAbsolutePath() + " file is missing");
			}
		}
		return agentJar;
		
//		for (String cpEntry : SystemUtils.JAVA_CLASS_PATH.split(File.pathSeparator)) {
//			if (cpEntry.contains("org.jacoco.agent")) {
//				return cpEntry;
//			}
//		}
//		throw new RuntimeException("JaCoCo Agent is missing");
	}
}

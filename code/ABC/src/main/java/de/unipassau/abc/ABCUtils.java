package de.unipassau.abc;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ABCUtils {

	private final static Logger logger = LoggerFactory.getLogger(ABCUtils.class);

	public final static String ABC_HOME = System.getenv("ABC_HOME");

	public static String getTraceJar() {

		String prefix = (ABC_HOME != null) ? ABC_HOME + "/" : "";
		
		String traceJar = new File(prefix + "./libs/trace.jar").getAbsolutePath();

		if (new File(traceJar).exists()) {
			return new File(traceJar).getAbsolutePath();
		}

		if (new File("../libs/trace.jar").exists()) {
			new File("../libs/trace.jar").getAbsolutePath();
		}

		if (System.getProperty("java.class.path").contains("trace.jar")) {
			String[] currentPath = SystemUtils.JAVA_CLASS_PATH.split(File.pathSeparator);
			for (String cpEntry : currentPath) {
				// Heuristically return what we need...
				if (cpEntry.endsWith("trace.jar")) {
					logger.info("Found trace.jar on CP");
					return cpEntry;
				}
			}
		}
		throw new RuntimeException(new File(traceJar).getAbsolutePath() + " file is missing");
	}

	// I do not like this...
	public static String getSystemRulesJar() {

		String prefix = (ABC_HOME != null) ? ABC_HOME + "/" : "";

		String jar = new File(prefix + "./libs/system-rules-1.17.0.jar").getAbsolutePath();

		if (!new File(jar).exists()) {
			jar = new File("../libs/system-rules-1.17.0.jar").getAbsolutePath(); // Actual
			// usage ...
			if (!new File(jar).exists()) {
				throw new RuntimeException(new File(jar).getAbsolutePath() + " file is missing");
			}
		}

		return jar;
	}

	public static List<File> getXStreamJars() {
		List<File> jars = new ArrayList<>();

		String prefix = (ABC_HOME != null) ? ABC_HOME + "/" : "";

		for (String jarName : new String[] { "xmlpull-1.1.3.1.jar", "xpp3_min-1.1.4c.jar", "xstream-1.4.10.jar",
				"slf4j-api-1.7.20.jar" }) {

			String jar = new File(prefix + "./libs/" + jarName).getAbsolutePath(); // Eclipse
			// testing

			if (!new File(jar).exists()) {
				jar = new File("../libs/" + jarName).getAbsolutePath(); // Actual
				// usage
				// ...
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
		String prefix = (ABC_HOME != null) ? ABC_HOME + "/" : "";

		//
		String agentJar = new File(prefix + "./libs/jacocoagent.jar").getAbsolutePath(); // Eclipse
		// testing

		if (!new File(agentJar).exists()) {
			agentJar = new File("../libs/jacocoagent.jar").getAbsolutePath(); // Actual
																				// usage
																				// ...
			if (!new File(agentJar).exists()) {
				throw new RuntimeException(new File(agentJar).getAbsolutePath() + " file is missing");
			}
		}
		return agentJar;
	}
}

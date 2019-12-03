package de.unipassau.abc;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.unipassau.abc.data.JimpleUtils;
import de.unipassau.abc.exceptions.ABCException;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;

public class ABCUtils {

	private final static Logger logger = LoggerFactory.getLogger(ABCUtils.class);

	public final static String ABC_HOME = System.getenv("ABC_HOME");

	public static final List<String> ARTIFICIAL_METHODS = Arrays.asList(new String[] { "StringOperation",
			"ArrayOperation", "ClassOperation", "MainArgsOperation", "StaticFieldOperation", "FieldOperation" });

	private final static HashMap<String, String> cachedMethods = new HashMap<>();

	// private static SootClass findInterfaceImplementing(String subSignature ){
	//
	// }
	//
	// private static SootClass findClassImplemeting(String subSignature ){
	//
	// }

	/**
	 * Recursively loop up the method
	 * 
	 * @param classOrInterface
	 * @param methodSubSignature
	 * @return
	 */
	public static SootMethod lookUp(SootClass classOrInterface, String jimpleMethod) {
		SootMethod theMethod = null;

		String methodSubSignature = JimpleUtils.getSubSignature(jimpleMethod);
		try {
			logger.trace("Check if " + methodSubSignature + " is declared  by " + classOrInterface);
			theMethod = classOrInterface.getMethod(methodSubSignature);
		} catch (RuntimeException e) {
			if (classOrInterface.hasSuperclass()) {
				// Rebuild the jimpleMethod for this class !
				String signature = "<" + classOrInterface.getSuperclass().getType().getClassName() + ": "
						+ methodSubSignature + ">";

				logger.trace("Check if " + methodSubSignature + " is declared  by superclass of " + classOrInterface
						+ " which is " + classOrInterface.getSuperclass() + " as " + signature);

				theMethod = lookUp(classOrInterface.getSuperclass(), signature);
			}

			if (theMethod == null) {
				for (SootClass sInterface : classOrInterface.getInterfaces()) {

					String signature = "<" + sInterface + ": " + methodSubSignature + ">";

					logger.trace("Check if " + methodSubSignature + " is declared by interface " + sInterface
							+ " of class " + classOrInterface + "as " + signature);

					theMethod = lookUp(sInterface, signature);
					if (theMethod != null) {
						break;
					}
				}
			}
			// TODO: handle exception
		}

		if (theMethod != null) {
			logger.trace("Found " + methodSubSignature + " as declared by "
					+ (classOrInterface.isInterface() ? "interface " : "") + classOrInterface);
			//
//			String signature = "<" + classOrInterface.getSuperclass().getType().getClassName() + ": " + methodSubSignature+">";
			String signature = jimpleMethod;

			logger.trace("ABCUtils.lookUp() Store " + signature + " --> " + theMethod.getSignature());
			cachedMethods.put(signature, theMethod.getSignature());

		}
		return theMethod;
	}

	/**
	 * We need to look up which class or interface declare a method such that we can
	 * reconstruct it later
	 * 
	 * @param jimpleMethod
	 * @return
	 * @throws CarvingException
	 */
	public static String lookUpMethod(String jimpleMethod) throws ABCException {
		SootClass sClass = Scene.v().getSootClass(JimpleUtils.getClassNameForMethod(jimpleMethod));

		// This method has the cache, so we can also cache partial results
		SootMethod sMethod = lookUp(sClass, jimpleMethod);

		if (sMethod != null) {
			return sMethod.getSignature();
		} else {
			throw new ABCException("Cannot find Soot Method for " + jimpleMethod);
		}

	}

	public static SootMethod lookUpSootMethod(String jimpleMethod) throws ABCException {
		SootClass sClass = Scene.v().getSootClass(JimpleUtils.getClassNameForMethod(jimpleMethod));

		// This method has the cache, so we can also cache partial results
		SootMethod sMethod = lookUp(sClass, jimpleMethod);

		if (sMethod != null) {
			return sMethod;
		} else {
			throw new ABCException("Cannot find Soot Method for " + jimpleMethod);
		}

	}

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

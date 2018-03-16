package de.unipassau.abc.tracing;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Trace {

	public final static String METHOD_START_TOKEN = "[>];";
	public final static String METHOD_END_TOKEN = "[<];";
	public final static String METHOD_OBJECT_TOKEN = "[||];";

	private static File traceFile;
	static {
		try {

			// USE A CONSTANT FOR THIS !
			if (System.getProperty("trace.output") != null) {
				traceFile = new File(System.getProperty("trace.output"));
			} else if (System.getenv("trace.output") != null) {
				traceFile = new File(System.getenv("trace.output"));
			} else {
				traceFile = File.createTempFile("temp-trace", ".txt");
			}
			System.out.println("**** Trace() Output to " + traceFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Displays what method is currently executing when it has parameters
	 * 
	 * @param typeOfMethod
	 *            - Is it a virtual invoke,New invoke,static invoke etc
	 * @param method
	 *            -Name of the method
	 * @param objects-
	 *            Parameters of the method
	 */
	public static void methodStart(String typeOfMethod, String method, Object... objects) {

		// System.out.println("Trace.methodStart() " + method);

		String content = METHOD_START_TOKEN + typeOfMethod + ";" + method;

		if (objects.length > 0) {
			String[] parametersType = extractParameterTypes(method);

			content = content + ";(";

			// Note that String objects are not passed by Value so far !
			for (int i = 0; i < objects.length; i++) {

				if (isPrimitive(parametersType[i])) {
					// By Value
					content += String.valueOf(objects[i]);
				} else if (isString(parametersType[i]) && objects[i] != null) {
					// Not null string By Value
					content += String.valueOf(objects[i]);
				} else {
					// By Reference - String can be NULL !
					content += (objects[i]).getClass().getName() + "@" + System.identityHashCode(objects[i]);
				}

				if (i != objects.length - 1) {
					content += ",";
				}
			}

			content += ")";
		}
		appendToTraceFile(content + "\n");
	}

	// This is the same above but without parameters
	// public static void methodStart(String typeOfMethod, String method) {
	// System.out.println("Trace.methodStart() " + method);
	// appendToTraceFile(METHOD_START_TOKEN + typeOfMethod + ";" + method +
	// "\n");
	// }

	private static String[] extractParameterTypes(String method) {
		Pattern argsPattern = Pattern.compile("\\((.*?)\\)");
		Matcher matchPattern = argsPattern.matcher(method);
		if (matchPattern.find()) {
			return matchPattern.group(1).split(",");
		}
		// System.out.println("Trace.extractParameterTypes() WARNING NO
		// PARAMETERS ?!!");
		return new String[] {};
	}

	/**
	 * @param method
	 *            - The method name
	 * @param o
	 *            - the Id of the object
	 */
	public static void methodObject(String method, Object o) {
		String xmlFile = null;
		try {
			// Primitives are not tracked, right ?
			if ("java.lang.String".equals(method.split(" ")[1])) {
				System.out.println("Trace.methodObject() Skip dump of Strings");
			} else {
				xmlFile = XMLDumper.dumpObject(method, o);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		String content = METHOD_OBJECT_TOKEN + method + ";"//
				+ ((xmlFile != null) ? xmlFile : "") + ";" //
				+ "" + o.getClass().getName() + "@" + System.identityHashCode(o);

		appendToTraceFile(content + "\n");
	}

	// This is for primitives values
	private static void methodStopForPrimitive(String methodName, String returnValue) {
		String content = METHOD_END_TOKEN + //
				methodName + ";" + //
				"" + ";" + //
				returnValue;
		appendToTraceFile(content + "\n");
	}

	private static boolean isPrimitive(String type) {
		return type.equals("boolean") || type.equals("byte") || type.equals("char") || type.equals("short")
				|| type.equals("int") || type.equals("long") || type.equals("float") || type.equals("double");
	}

	private static boolean isString(String type) {
		return type.equals("java.lang.String");
	}

	public static boolean isVoid(String type) {
		return type.equals("void");
	}

	public static void methodStop(String methodName, Object returnValue) {

		// We distinguish primitives and boxed using methodName which specifies
		// the return type !
		if (isPrimitive(extractReturnType(methodName))) {
			methodStopForPrimitive(methodName, returnValue.toString());
		} else if (isVoid(extractReturnType(methodName))) {
			methodStopForVoid(methodName);
		} else {
			methodStopForObject(methodName, returnValue);
		}
	}

	private static void methodStopForObject(String methodName, Object returnValue) {
		String xmlFile = null;
		try {
			// Returns null for null objects
			xmlFile = XMLDumper.dumpObject(methodName, returnValue);
		} catch (IOException e) {
			e.printStackTrace();
		}

		String content = METHOD_END_TOKEN + //
				methodName + ";" + //
				((xmlFile != null) ? xmlFile : "") + ";" + //
				returnValue.getClass().getName() + "@" + System.identityHashCode(returnValue) + ";";

		appendToTraceFile(content + "\n");

	}

	private static void methodStopForVoid(String methodName) {
		// System.out.println("Trace.methodStop() " + methodName);
		String content = METHOD_END_TOKEN + //
				methodName + ";" + //
				"" + ";"; // Empty XML File
		appendToTraceFile(content + "\n");
	}

	// public static void methodStopForNullObject(String method) {
	// System.out.println("Trace.methodStopForNullObject() " + method + "
	// returnValue is NULL");
	// appendToTraceFile(METHOD_END_TOKEN + method + ";" + "null" + "\n");
	// }

	private static String extractReturnType(String methodName) {
		return methodName.split(" ")[1];
	}

	public static void appendToTraceFile(String content) {
		BufferedWriter bw = null;
		FileWriter fw = null;
		try {
			// System.out.println(content);
			fw = new FileWriter(traceFile, true);
			bw = new BufferedWriter(fw);
			bw.write(content);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {

				if (bw != null)
					bw.close();

				if (fw != null)
					fw.close();

			} catch (IOException ex) {

				ex.printStackTrace();

			}
		}
	}

}

package de.unipassau.abc.tracing;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Trace {

	public final static char DELIMITER = ';';

	public final static String METHOD_START_TOKEN = "[>]" + DELIMITER;
	public final static String METHOD_END_TOKEN = "[<]" + DELIMITER;
	public final static String METHOD_OBJECT_TOKEN = "[||]" + DELIMITER;

	private static File traceFile;
	static {
		try {

			File traceDir;
			// USE A CONSTANT FOR THIS !
			if (System.getProperty("trace.output") != null) {
				traceDir = new File(System.getProperty("trace.output"));
			} else if (System.getenv("trace.output") != null) {
				traceDir = new File(System.getenv("trace.output"));
			} else {
				traceDir = Files.createTempDirectory("temp-trace").toFile();
			}

			if (!traceDir.exists()) {
				if (!traceDir.mkdirs()) {
					throw new IOException("Cannot create trace dir " + traceDir.getAbsolutePath());
				}
			}
			traceFile = new File(traceDir, "trace.txt");
//			System.out.println("**** Trace() Output to " + traceFile);
		} catch (IOException e) {
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
		// TODO Maybe a StringBuffer here?
		String content = METHOD_START_TOKEN + typeOfMethod + ";" + method;

		if ("StaticFieldOperation".equals(typeOfMethod)) {
			// Use the objects literally... class.field
			String staticFieldReference = (String) objects[0];
			content += ";(";
			content += staticFieldReference;
			content += ")";

		} else if (objects.length > 0) {
			// The formal parameters might be different than the actual ones!
			// Object -> String
			// So use the actual type for the trace !
			String[] parametersType = extractParameterTypes(method);

			content = content + ";(";

			// NOTE: ALWAYS USE THE ACTUAL PARAMETER TO CHECK THE TYPE, unless
			// for null and primitives. Primitives at this point are boxed,
			// while we need their VALUE.
			//
			// FormalParameter might be Object but actual parameter Array,
			// unless those are NULL !
			for (int i = 0; i < objects.length; i++) {

				String pType = null;
				if (isPrimitive(parametersType[i])) {
					pType = parametersType[i];
				} else if (objects[i] != null) {
					pType = (objects[i]).getClass().getName();
				} else {
					// In case of null we use the type of formal parameter
					pType = parametersType[i];
				}

				if (isPrimitive(pType)) {
					content += String.valueOf(objects[i]);
				}
				// Arrays
				else if (pType.startsWith("[")) {
					// An array might be null
					if (objects[i] == null) {
						content += pType + "[]" + "@" + System.identityHashCode(objects[i]);
					}
					// NON-Null object, which might be an array
					else if ((objects[i]).getClass().getName().contains("[L")) {
						String transformedClassName = (objects[i]).getClass().getName().replace("[L", "").replace(";",
								"") + "[]";
						content += transformedClassName + "@" + System.identityHashCode(objects[i]);
					}
					// Here we get array of primitive types. By default we keep
					// the formal type
					else {
						// Arrays of primitive types are weird, they are not
						// [L
						// but [B for byte [I for integer
						// To avoid problems we use the formal parameters
						content += pType + "@" + System.identityHashCode(objects[i]);
					}
				} else {
					// To handle Objects
					if (objects[i] == null) {
						// Use the formal parameter
						content += pType + "@" + System.identityHashCode(objects[i]);
					} else {
						String actualParameterType = (objects[i]).getClass().getName();
						// TODO Track String like regular objects
						// if (isString(actualParameterType)) {
						// // Not null string By Value
						// content += String.valueOf(objects[i]);
						// } else {
						// All the remaining By Reference
						content += actualParameterType + "@" + System.identityHashCode(objects[i]);
						// }
					}
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

	private static boolean isArray(String formalParameterType) {
		return formalParameterType.endsWith("[]");
	}

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
	 * @param owner
	 *            - the Id of the object
	 */
	public static void methodObject(String method, Object owner) {
		// There should be only one
		String parameterType = extractClassType(method);

		// Dumping the XML of the object BEFORE the calls is useless, since that
		// status is the result of some other call.
		// The dump shall be taken at Method Stop !
		String content = METHOD_OBJECT_TOKEN + method + ";";

		if (isArray(parameterType)) {
			// System.out.println("Trace.methodObject() " + parameterType );
			// TODO Not sure this is the best approach, tho
			// We transform:
			// [Ljava.nio.file.attribute.FileAttribute;@448354164
			// To java.nio.file.attribute.FileAttribute[]@448354164
			String transformedClassName = owner.getClass().getName().replace("[L", "").replace(";", "") + "[]";
			content += transformedClassName + "@" + System.identityHashCode(owner);
		}
		// else if (isString(parameterType) && o != null) {
		// // Not null string By Value
		// content += String.valueOf(o);
		// }
		else {
			content += "" + owner.getClass().getName() + "@" + System.identityHashCode(owner);
		}

		appendToTraceFile(content + "\n");
	}

	// This is for primitives values
	private static void methodStopForPrimitive(String methodName, String returnValue) {
		String content = METHOD_END_TOKEN + //
				methodName + ";" + //
				"" + ";" + // XML Owner
				"" + ";" + // Return value Owner
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

	public static void methodStop(String method, Object owner, Object returnValue) {

		// We distinguish primitives and boxed using methodName which specifies
		// the return type !
		if (isPrimitive(
				extractReturnType(method)) /*
											 * || (isString(extractReturnType(
											 * method)) && returnValue != null)
											 */) {
			methodStopForPrimitive(method, returnValue.toString());
		} else if (isVoid(extractReturnType(method))) {
			methodStopForVoid(method, owner);
		} else {
			methodStopForObject(method, owner, returnValue);
		}
	}

	private static void methodStopForObject(String methodName, Object owner, Object returnValue) {
		String ownerXmlFile = null;
		String returnXmlFile = null;
		try {
			if (owner != null) {
				ownerXmlFile = XMLDumper.dumpObject(methodName, owner);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			// Returns null for null objects
			if (returnValue != null) {
				returnXmlFile = XMLDumper.dumpObject(methodName, returnValue);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		
		
		String content = METHOD_END_TOKEN + //
				methodName + ";" + //
				((ownerXmlFile != null) ? ownerXmlFile : "") + ";" + // Owner
				((returnXmlFile != null) ? returnXmlFile : "") + ";" + // Return
				extractReturnType(methodName) + "@" + System.identityHashCode(returnValue) + ";";

		appendToTraceFile(content + "\n");

	}

	private static void methodStopForVoid(String methodName, Object owner) {
		// System.out.println("Trace.methodStop() " + methodName);
		String xmlFile = null;
		try {
			if (owner != null) {
				xmlFile = XMLDumper.dumpObject(methodName, owner);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		String content = METHOD_END_TOKEN + //
				methodName + ";" + //
				((xmlFile != null) ? xmlFile : "") + ";" + // Owner XML File
															// null for Static
				"" + ";"; // Return Object XML File
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

	// NOT SURE THIS IS CORRECT !
	private static String extractClassType(String methodName) {
		return methodName.replace("<", "").split(" ")[0].replaceAll(":", "");
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

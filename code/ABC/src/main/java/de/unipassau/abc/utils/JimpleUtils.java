package de.unipassau.abc.utils;

import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import soot.Body;
import soot.Local;
import soot.SootClass;
import soot.SootMethod;
import soot.Type;
import soot.Unit;

public class JimpleUtils {

	/**
	 * Return parameter type names
	 * 
	 * @param jimpleMethod
	 * @return
	 */
	private final static Pattern params = Pattern.compile("\\((.*?)\\)");

	public static String[] getParameterList(String jimpleMethod) {

		Matcher matchPattern = params.matcher(jimpleMethod);
		if (matchPattern.find()) {
			String t = matchPattern.group(1);
			if (t.length() == 0) {
				return new String[] {};
			}
			return t.split(",");
		}
		return new String[] {};
	}

	public static boolean isPrimitive(String type) {
		return type.equals("boolean") || type.equals("byte") || type.equals("char") || type.equals("short")
				|| type.equals("int") || type.equals("long") || type.equals("float") || type.equals("double");
	}

	public static boolean isString(String type) {
		return type.equals("java.lang.String");
	}

	public static boolean isVoid(String type) {
		return type.equals("void");
	}

	public static String getClassNameForMethod(String jimpleMethod) {
		return jimpleMethod.replaceFirst("<", "").split(" ")[0].replaceAll(":", "");
	}

	public static String getReturnType(String jimpleMethod) {
		return jimpleMethod.split(" ")[1];
	}

	public static boolean hasVoidReturnType(String jimpleMethod) {
		return isVoid(getReturnType(jimpleMethod));
	}

	public static void prettyPrint(SootClass sClass) {
		Iterator<SootMethod> i = sClass.methodIterator();
		System.out.println("Class " + sClass.getName());
		while (i.hasNext()) {
			SootMethod m = i.next();
			System.out.println("\t" + m.getDeclaration());
			System.out.println(prettyPrint(m.getActiveBody()));
		}

	}

	public static String prettyPrint(Body body) {
		StringBuffer bodyBuffer = new StringBuffer();

		Iterator<Local> il = body.getLocals().iterator();
		while (il.hasNext()) {
			Local l = il.next();
			bodyBuffer.append(l).append(" ").append(l.getType()).append("\n");
		}
		Iterator<Unit> iu = body.getUnits().iterator();
		while (iu.hasNext()) {
			bodyBuffer.append(iu.next()).append("\n");
		}
		return bodyBuffer.toString();
	}

	public static String getMethodName(String jimpleMethod) {
		return jimpleMethod.replaceFirst("<", "").split(" ")[2].split("\\(")[0];
	}

	/**
	 * We adopt a naive approach and LITERALLY compare methods, this might not
	 * work in general but should be good enough. Note that for the test Method
	 * we forcefully insert the body.insertIdentityStmtsz
	 * 
	 * @param testClass
	 * @param testMethod
	 * @return
	 */
	public static boolean classContainsEquivalentMethod(SootClass testClass, SootMethod testMethod) {
		String fakeIdentityStmts = String.format("this := @this: %s", testClass.getName());
		// Patch: add the "this := @this: <ClassName>" entry
		final String testMethodBody = fakeIdentityStmts + "\n" + prettyPrint(testMethod.getActiveBody());

		for (SootMethod method : testClass.getMethods()) {
			String methoBody = prettyPrint(method.getActiveBody());
			if (testMethodBody.equals(methoBody)) {
				return true;
			}
		}
		return false;
	}

	public static boolean isPrimitive(Type type) {
		return isPrimitive(type.toString());
	}
}

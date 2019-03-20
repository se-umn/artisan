package de.unipassau.abc.utils;

import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.unipassau.abc.carving.MethodInvocation;
import soot.Body;
import soot.Local;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.Type;
import soot.Unit;

public class JimpleUtils {

	private final static Logger logger = LoggerFactory.getLogger( JimpleUtils.class );
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
	
	public static boolean isPrimitiveDeclaration(String type) {
		return type.startsWith("boolean") || type.startsWith("byte") || type.startsWith("char") || type.startsWith("short")
				|| type.startsWith("int") || type.startsWith("long") || type.startsWith("float") || type.startsWith("double");
	}

	public static boolean isString(Type type) {
		return isString(type.toString());
	}

	public static boolean isString(String type) {
		return type.equals("java.lang.String");
	}
	
	public static boolean isStringContent(String stringContent) {
        return stringContent != null && (stringContent.startsWith("[") && stringContent.endsWith("]"));
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
		// String fileName = SourceLocator.v().getFileNameFor(sClass,
		// Options.output_format_jimple);
//		try (PrintWriter writerOut = new PrintWriter(new OutputStreamWriter(System.out))) {
//			// Options.v().set_xml_attributes(true);
//			Options.v().set_print_tags_in_output(true);
//			Printer.v().printTo(sClass, writerOut);
//			writerOut.flush();
//		}

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
		return getEquivalentMethod(testClass, testMethod) != null;
	}

	public static SootMethod getEquivalentMethod(SootClass testClass, SootMethod testMethod) {
		String fakeIdentityStmts = String.format("this := @this: %s", testClass.getName());
		// Patch: add the "this := @this: <ClassName>" entry
		final String testMethodBody = fakeIdentityStmts + "\n" + prettyPrint(testMethod.getActiveBody());

		for (SootMethod method : testClass.getMethods()) {
			String methoBody = prettyPrint(method.getActiveBody());
			if (testMethodBody.equals(methoBody)) {
				return method;
			}
		}
		return null;
	}

	public static boolean isPrimitive(Type type) {
		return isPrimitive(type.toString());
	}

	public static boolean isNull(String string) {
		try {
			// System.out.println("JimpleUtils.isNull() " + string);
			// we use system hash, this is 0 for null objects
			return string == null || (string.split("@")[1].equals("0"));
		} catch (Throwable e) {
			System.out.println("JimpleUtils.isNull() ERROR FOR " + string);
			throw e;
		}
	}

	public static String getSubSignature(String jimpleMethod) {
		// 0    1    2
//		<Type: void addCaretListener(javax.swing.event.CaretListener)>
		StringBuffer sb = new StringBuffer();
		return sb.append(jimpleMethod.split(" ")[1]+" "+jimpleMethod.split(" ")[2]).deleteCharAt(sb.length()-1).toString();
	}

	// Some caching maybe ?
	public static SootMethod toSootMethod(MethodInvocation mi) {
		try{
		return Scene.v().getMethod( mi.getMethodSignature() );
		}catch(Throwable t ){
			logger.error("Cannot find method {} ", mi.getMethodSignature() );
			return null;
		}
	}
	
    public static boolean isConstructor(String methodSignature) {
        return methodSignature.contains("<init>");
    }
}

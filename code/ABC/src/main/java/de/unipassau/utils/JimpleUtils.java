package de.unipassau.utils;

import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import soot.Body;
import soot.SootClass;
import soot.SootMethod;
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
			if( t.length() == 0 ){
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
		return jimpleMethod.replaceFirst("<","").split(" ")[0].replaceAll(":", "");
	}

	public static String getReturnType(String jimpleMethod) {
		return jimpleMethod.split(" ")[1];
	}

	public static boolean hasVoidReturnType(String jimpleMethod) {
		return isVoid( getReturnType(jimpleMethod));
	}

	public static void prettyPrint(SootClass sClass){
		 Iterator<SootMethod> i = sClass.methodIterator();
		 System.out.println("Class " + sClass.getName() );
		 while(i.hasNext() ){
			 SootMethod m = i.next();
			 System.out.println( "\t"+  m.getDeclaration());
			 Body b = m.getActiveBody();
			 Iterator<Unit> iu = b.getUnits().iterator();
			 while( iu.hasNext()){
				 System.out.println(  iu.next() );
			 }
		 }
		
	}

	public static String getMethodName(String jimpleMethod) {
		return jimpleMethod.replaceFirst("<","").split(" ")[2].split("\\(")[0];
	}
}

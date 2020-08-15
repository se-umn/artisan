package de.unipassau.abc.generation.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import de.unipassau.abc.data.JimpleUtils;
import de.unipassau.abc.data.MethodInvocation;
import soot.Modifier;
import soot.Scene;
import soot.SootClass;

@Deprecated

public class AllTestTogether implements SootTestClassGenerator {

	private Map<String, SootClass> testClasses = new HashMap<>();

	private SootClass getTestClass(String classUnderTest) {
		String simpleClassName = classUnderTest.replaceAll("\\.", " ")
				.split(" ")[classUnderTest.replaceAll("\\.", " ").split(" ").length - 1];
		String classPackage = classUnderTest.substring(0, classUnderTest.lastIndexOf("."));

		String testCaseName = classPackage + "." + "Test" + simpleClassName;

		SootClass sClass = new SootClass(testCaseName, Modifier.PUBLIC);
		sClass.setSuperclass(Scene.v().getSootClass("java.lang.Object"));

		Scene.v().addClass(sClass);

		return sClass;
	}

	@Override
	public SootClass getTestClassFor(MethodInvocation mut) {

		String classUnderTest = JimpleUtils.getClassNameForMethod(mut.getMethodSignature());

		if (!testClasses.containsKey(classUnderTest)) {
			testClasses.put(classUnderTest, getTestClass(classUnderTest));
		}

		return testClasses.get(classUnderTest);
	}

	@Override
	public Collection<SootClass> getTestClasses() {
		return testClasses.values();
	}

}

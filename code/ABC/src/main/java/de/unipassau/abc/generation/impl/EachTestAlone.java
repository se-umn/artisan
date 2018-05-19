package de.unipassau.abc.generation.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.omg.Messaging.SYNC_WITH_TRANSPORT;

import de.unipassau.abc.carving.MethodInvocation;
import de.unipassau.abc.generation.TestClassGenerator;
import de.unipassau.abc.utils.JimpleUtils;
import soot.Modifier;
import soot.Scene;
import soot.SootClass;

/**
 * Each carved test is inside a different test class. This helps with the
 * evaluation on RTS
 * 
 * @author gambi
 *
 */
public class EachTestAlone implements TestClassGenerator {

	private Map<String, AtomicInteger> testClasseCounters = new HashMap<>();
	private List<SootClass> testClasses = new ArrayList<>();

	private SootClass getTestClass(String classUnderTest, int i) {
		System.out.println("classUnderTest " + classUnderTest );
		String simpleClassName = classUnderTest.replaceAll("\\.", " ")
				.split(" ")[classUnderTest.replaceAll("\\.", " ").split(" ").length - 1] + //
				"_" + i;

		System.out.println("simpleClassName " + simpleClassName );

		String classPackage = classUnderTest.substring(0, classUnderTest.lastIndexOf("."));

		System.out.println("classPackage " + classPackage );
		
		String testCaseName = classPackage + "." + "Test" + simpleClassName;

		if( testCaseName.contains("[")){
			throw new RuntimeException("Wrong class name " + testCaseName + " for " + classUnderTest );
		}
		
		SootClass sClass = new SootClass(testCaseName, Modifier.PUBLIC);
		sClass.setSuperclass(Scene.v().getSootClass("java.lang.Object"));

		Scene.v().addClass(sClass);

		return sClass;
	}

	@Override
	public SootClass getTestClassFor(MethodInvocation mut) {

		System.out.println("\n EachTestAlone.getTestClassFor() MUT " + mut.getJimpleMethod() );
		String classUnderTest = JimpleUtils.getClassNameForMethod(mut.getJimpleMethod());
		System.out.println("class Under test is " + classUnderTest + "\n");

		if (!testClasseCounters.containsKey(classUnderTest)) {
			testClasseCounters.put(classUnderTest, new AtomicInteger(0));
		}
		SootClass testClass = getTestClass(classUnderTest, testClasseCounters.get(classUnderTest).incrementAndGet());
		
		testClasses.add(testClass);
		return testClass;
	}

	@Override
	public Collection<SootClass> getTestClasses() {
		return testClasses;
	}

}

package de.unipassau.abc.generation.utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import de.unipassau.abc.data.JimpleUtils;
import de.unipassau.abc.data.MethodInvocation;
import de.unipassau.abc.generation.data.CarvedTest;

/**
 * Place carved tests that test the same method together
 *
 * 
 * TODO Far from being finished
 * 
 * @author gambitemp
 *
 */
public class ByMethodUnderTest implements TestCaseOrganizer {

	@Override
	public Set<TestClass> organize(CarvedTest... carvedTests) {

		Map<MethodInvocation, Set<CarvedTest>> muts = new HashMap<MethodInvocation, Set<CarvedTest>>();
		for (CarvedTest carvedTest : carvedTests) {
			muts.putIfAbsent(carvedTest.getMethodUnderTest(), new HashSet<CarvedTest>());
			muts.get(carvedTest.getMethodUnderTest()).add(carvedTest);
		}

		// Now make sure to define proper naming and packaging
		Set<TestClass> testSuite = new HashSet<TestClass>();

		for (Entry<MethodInvocation, Set<CarvedTest>> testGroup : muts.entrySet()) {
			MethodInvocation methodInvocation = testGroup.getKey();
			TestClass testCase = new TestClass(JimpleUtils.getPackage(methodInvocation.getMethodSignature()),
					JimpleUtils.getMethodName(methodInvocation.getMethodSignature()), testGroup.getValue());

			testSuite.add(testCase);
		}

		return testSuite;
	}

}

package de.unipassau.abc.generation.utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import de.unipassau.abc.data.JimpleUtils;
import de.unipassau.abc.generation.data.CarvedTest;

/**
 * Place carved tests that test the same method together TODO Far from being
 * finished
 * 
 * @author gambitemp
 *
 */
public class ByClassUnderTest implements TestCaseOrganizer {

	@Override
	public Set<TestCase> organize(CarvedTest... carvedTests) {

		Map<String, Set<CarvedTest>> muts = new HashMap<String, Set<CarvedTest>>();
		for (CarvedTest carvedTest : carvedTests) {
			muts.putIfAbsent(JimpleUtils.getClassNameForMethod(carvedTest.getMethodUnderTest().getMethodSignature()),
					new HashSet<CarvedTest>());
			muts.get(carvedTest.getMethodUnderTest()).add(carvedTest);
		}

		// Now make sure to define proper naming and packaging
		Set<TestCase> testSuite = new HashSet<TestCase>();

		for (Entry<String, Set<CarvedTest>> testGroup : muts.entrySet()) {
			TestCase testCase = new TestCase(testGroup.getKey(), testGroup.getKey(), testGroup.getValue());
			testSuite.add(testCase);
		}

		return testSuite;
	}

}

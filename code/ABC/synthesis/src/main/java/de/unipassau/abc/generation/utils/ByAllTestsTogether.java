package de.unipassau.abc.generation.utils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import de.unipassau.abc.generation.data.CarvedTest;

/**
 * TODO Far from being finished
 * 
 * @author gambitemp
 *
 */
public class ByAllTestsTogether implements TestCaseOrganizer {

	@Override
	public Set<TestClass> organize(CarvedTest... carvedTests) {

		Set<TestClass> testSuite = new HashSet<>();
		TestClass testCase = new TestClass("all", "AllCarvedTests",
				new HashSet<>(Arrays.asList(carvedTests)));
		testSuite.add(testCase);

		return testSuite;
	}
}

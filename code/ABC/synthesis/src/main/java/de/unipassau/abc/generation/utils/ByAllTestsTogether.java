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
	public Set<TestCase> organize(CarvedTest... carvedTests) {

		Set<TestCase> testSuite = new HashSet<TestCase>();
		TestCase testCase = new TestCase("all", "AllCarvedTests", new HashSet<CarvedTest>(Arrays.asList(carvedTests)));
		testSuite.add(testCase);

		return testSuite;
	}
}

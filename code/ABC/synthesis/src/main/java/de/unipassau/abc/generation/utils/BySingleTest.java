package de.unipassau.abc.generation.utils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import de.unipassau.abc.data.JimpleUtils;
import de.unipassau.abc.generation.data.CarvedTest;

/**
 * Each carved test is inside a different test class. This helps with the
 * evaluation on RTS
 * 
 * @author gambi
 *
 */
public class BySingleTest implements TestCaseOrganizer {

	@Override
	public Set<TestCase> organize(CarvedTest... carvedTests) {

		Set<TestCase> testSuite = new HashSet<TestCase>();
		for (CarvedTest carvedTest : carvedTests) {
			TestCase testCase = new TestCase(
					JimpleUtils.getClassNameForMethod(carvedTest.getMethodUnderTest().getMethodSignature()),
					"Test" + JimpleUtils.getMethodName(carvedTest.getMethodUnderTest().getMethodSignature()),
					new HashSet<CarvedTest>(Arrays.asList(carvedTest)));

			testSuite.add(testCase);

		}
		return testSuite;
	}
}
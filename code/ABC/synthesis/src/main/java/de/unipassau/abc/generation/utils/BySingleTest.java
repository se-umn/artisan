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

	private TestCaseNamer testCaseNamer;

	public BySingleTest(TestCaseNamer testCaseNamer) {
		this.testCaseNamer = testCaseNamer;
	}

	@Override
	public Set<TestClass> organize(CarvedTest... carvedTests) {

		Set<TestClass> testSuite = new HashSet<TestClass>();
		for (CarvedTest carvedTest : carvedTests) {

			String testClassName = testCaseNamer.generateTestClassName(carvedTest);

			String testPackageName = JimpleUtils
					.getPackageNameForMethod(carvedTest.getMethodUnderTest().getMethodSignature());

			TestClass testCase = new TestClass(testPackageName, testClassName,
					new HashSet<CarvedTest>(Arrays.asList(carvedTest)));

			testSuite.add(testCase);

		}
		return testSuite;
	}
}
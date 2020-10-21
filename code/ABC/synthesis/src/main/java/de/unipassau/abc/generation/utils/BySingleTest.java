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

			String testClassName = "Test"
					+ JimpleUtils.getSimpleClassNameForMethod(carvedTest.getMethodUnderTest().getMethodSignature());
			String testPackageName = JimpleUtils
					.getPackageNameForMethod(carvedTest.getMethodUnderTest().getMethodSignature());
			// TODO This will not work with Multipoe
			if (JimpleUtils.isConstructor(carvedTest.getMethodUnderTest().getMethodSignature())) {
				testClassName += "Constructor";
			} else if (JimpleUtils
					.isConstructorOrClassConstructor(carvedTest.getMethodUnderTest().getMethodSignature())) {
				testClassName += "StaticConstructor";
			} else {
				String methodName = JimpleUtils.getMethodName(carvedTest.getMethodUnderTest().getMethodSignature());
				// Make sure we capitalize it correctly
				testClassName += methodName.substring(0, 1).toUpperCase() + methodName.substring(1);
			}

			TestCase testCase = new TestCase(testPackageName, testClassName,
					new HashSet<CarvedTest>(Arrays.asList(carvedTest)));

			testSuite.add(testCase);

		}
		return testSuite;
	}
}
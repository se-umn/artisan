package de.unipassau.abc.generation.utils;

import de.unipassau.abc.data.JimpleUtils;
import de.unipassau.abc.generation.data.CarvedTest;

public class NameTestCaseAfterClassUnderTest implements TestCaseNamer {

	@Override
	public String generateTestClassName(CarvedTest carvedTest) {
		String testClassName = "Test"
				+ JimpleUtils.getSimpleClassNameForMethod(carvedTest.getMethodUnderTest().getMethodSignature());
		// TODO This will not work with Multipoe
		if (JimpleUtils.isConstructor(carvedTest.getMethodUnderTest().getMethodSignature())) {
			testClassName += "Constructor";
		} else if (JimpleUtils.isConstructorOrClassConstructor(carvedTest.getMethodUnderTest().getMethodSignature())) {
			testClassName += "StaticConstructor";
		} else {
			String methodName = JimpleUtils.getMethodName(carvedTest.getMethodUnderTest().getMethodSignature());
			// Make sure we capitalize it correctly
			testClassName += methodName.substring(0, 1).toUpperCase() + methodName.substring(1);
		}
		return testClassName;
	}

}

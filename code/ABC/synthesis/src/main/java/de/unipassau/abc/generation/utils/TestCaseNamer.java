package de.unipassau.abc.generation.utils;

import de.unipassau.abc.generation.data.CarvedTest;

public interface TestCaseNamer {

	/**
	 * Generate a name for the test class
	 * @param carvedTest
	 */
	public String generateTestClassName(CarvedTest carvedTest);

	
}

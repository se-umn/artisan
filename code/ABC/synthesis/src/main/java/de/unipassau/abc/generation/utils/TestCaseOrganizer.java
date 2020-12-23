package de.unipassau.abc.generation.utils;

import java.util.Set;

import de.unipassau.abc.generation.data.CarvedTest;

public interface TestCaseOrganizer {

	/**
	 * Return a set of test cases that partition the given carvedTests.
	 * 
	 * @param carvedTests
	 * @return
	 */
	public Set<TestClass> organize(CarvedTest... carvedTests);

}

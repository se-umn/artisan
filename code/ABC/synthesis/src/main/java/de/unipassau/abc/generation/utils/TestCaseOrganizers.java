package de.unipassau.abc.generation.utils;

/**
 * Utility class to access the various implementations of TestCaseOrganizer
 * 
 * @author gambitemp
 *
 */
public class TestCaseOrganizers {

	public static TestCaseOrganizer byMethodUnderTest() {
		return new ByMethodUnderTest();
	}

	public static TestCaseOrganizer byClassUnderTest() {
		return new ByClassUnderTest();
	}

	public static TestCaseOrganizer byAllTestsTogether() {
		return new ByAllTestsTogether();
	}

	public static TestCaseOrganizer byEachTestAlone() {
		return new BySingleTest();
	}
}

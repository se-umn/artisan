package de.unipassau.abc.generation;

import java.util.Collection;

import de.unipassau.abc.carving.CarvedExecution;
import de.unipassau.abc.generation.data.CarvedTest;

public interface TestGenerator {

	/**
	 * Generate a number of carved test cases from the given carved executions. This
	 * includes generating the necessary mocking, assertions and setup methods.
	 * 
	 * CarvedTests are not organized into classes or suites.
	 * 
	 * @param carvedExecutions
	 * @return
	 */
	public Collection<CarvedTest> generateTests(Collection<CarvedExecution> carvedExecutions);
}

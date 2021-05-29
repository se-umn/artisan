package de.unipassau.abc.generation;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import de.unipassau.abc.carving.exceptions.CarvingException;
import de.unipassau.abc.data.MethodInvocation;
import de.unipassau.abc.exceptions.ABCException;
import de.unipassau.abc.generation.data.CarvedTest;
import de.unipassau.abc.parsing.ParsedTrace;

public interface TestGenerator {

	/**
	 * Generate a number of carved test cases from the given carved executions. This
	 * includes generating the necessary mocking, assertions and setup methods.
	 * 
	 * Since Test Generation may require different rounds of carving TestGenerator
	 * works directly on the ParsedTraces.
	 * 
	 * CarvedTests are not organized into classes or suites and they are not
	 * optimized either;
	 * 
	 * @param carvedExecutions
	 * @return
	 * @throws ABCException
	 * @throws CarvingException
	 */
	public Collection<CarvedTest> generateTests(List<MethodInvocation> targetMethodsInvocationsUnderTest,
			ParsedTrace trace) throws CarvingException, ABCException;
}

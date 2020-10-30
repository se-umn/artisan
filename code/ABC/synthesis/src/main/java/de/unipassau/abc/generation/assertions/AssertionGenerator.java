package de.unipassau.abc.generation.assertions;

import de.unipassau.abc.carving.CarvedExecution;
import de.unipassau.abc.generation.data.CarvedTest;

public interface AssertionGenerator {

	public CarvingAssertion generateAssertionsFor(CarvedTest carvedTest, CarvedExecution carvedExecution);
}

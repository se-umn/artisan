package de.unipassau.abc.generation.utils;

import java.util.concurrent.atomic.AtomicInteger;

import de.unipassau.abc.generation.data.CarvedTest;

/**
 * This uses a global static counter !
 * 
 * @author gambitemp
 *
 */
public class NameTestCaseGlobally implements TestCaseNamer {

	private static AtomicInteger globalTestClassId = new AtomicInteger(0);

	@Override
	public String generateTestClassName(CarvedTest carvedTest) {
		return "Test" + String.format("%03d", globalTestClassId.incrementAndGet());
	}

}

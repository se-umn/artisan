package de.unipassau.abc.generation.utils;

import de.unipassau.abc.generation.data.CarvedTest;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * This uses a global static counter !
 * 
 * @author gambitemp
 *
 */
public class NameTestCaseBasedOnCarvedTest implements TestCaseNamer {

	private static AtomicInteger globalTestClassId = new AtomicInteger(0);

	@Override
	public String generateTestClassName(CarvedTest carvedTest) {
		return "Test" + String.format("%03d", Integer.parseInt(carvedTest.getUniqueIdentifier()));
	}

}

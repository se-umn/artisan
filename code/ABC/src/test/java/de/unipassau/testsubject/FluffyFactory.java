package de.unipassau.testsubject;

import java.util.concurrent.atomic.AtomicInteger;

public class FluffyFactory {

	private static AtomicInteger idGenerator = new AtomicInteger();

	public static Fluffy makeAFluffy() {
		Fluffy fluffy = new Fluffy(idGenerator.incrementAndGet());
		return fluffy;
	}

	public static Fluffy makeAFluffyWitGoofy(Goofy goofy) {
		// Goofy not really used, it just create a chain of dependencies
		Fluffy fluffy = new Fluffy(idGenerator.incrementAndGet());
		return fluffy;
	}
}

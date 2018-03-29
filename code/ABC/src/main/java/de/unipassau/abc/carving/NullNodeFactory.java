package de.unipassau.abc.carving;

import java.util.concurrent.atomic.AtomicInteger;

public class NullNodeFactory {

	private final static AtomicInteger uniqueId = new AtomicInteger(0);
	
	public static DataNode get(String type) {
//		System.out.println("NullNodeFactory.get() for " + type);
		return new NullInstance(uniqueId.incrementAndGet(), type);
	}

	
}

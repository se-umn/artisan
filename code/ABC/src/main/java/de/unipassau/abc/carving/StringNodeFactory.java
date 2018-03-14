package de.unipassau.abc.carving;

import java.util.concurrent.atomic.AtomicInteger;

public class StringNodeFactory {

	private final static AtomicInteger uniqueId = new AtomicInteger(0);

	public static StringValue get(String string) {
		return new StringValue( uniqueId.incrementAndGet(), string); 
	}
	
	
}

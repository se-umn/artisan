package de.unipassau.abc.carving;

import java.util.concurrent.atomic.AtomicInteger;

public class PrimitiveNodeFactory {

	private static AtomicInteger uniqueId= new AtomicInteger(0);
	
	public static PrimitiveValue get(String type, String value){
		return new PrimitiveValue(uniqueId.incrementAndGet(),  type, value);
	}
	
}

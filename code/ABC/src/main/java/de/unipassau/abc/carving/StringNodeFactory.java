package de.unipassau.abc.carving;

import java.util.concurrent.atomic.AtomicInteger;

public class StringNodeFactory {

	private final static AtomicInteger uniqueId = new AtomicInteger(0);

	public static StringNode get(String objectId, String value) {
		StringNode node = new StringNode( Integer.parseInt(objectId.split("@")[1]), value);
		System.out.println("StringNodeFactory.get() Generate new string node " + node + " with value " + value);
		return node;
	}

}

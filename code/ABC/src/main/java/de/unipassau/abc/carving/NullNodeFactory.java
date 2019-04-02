package de.unipassau.abc.carving;

public class NullNodeFactory {

	public static DataNode get(String type) {
		return new NullInstance(type);
	}

	
}

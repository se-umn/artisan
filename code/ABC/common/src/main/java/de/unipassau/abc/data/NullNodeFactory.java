package de.unipassau.abc.data;

public class NullNodeFactory {

	public static NullInstance get(String type) {
		return new NullInstance(type);
	}

}

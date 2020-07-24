package de.unipassau.abc.instrumentation;

import soot.tagkit.AttributeValueException;
import soot.tagkit.Tag;

/**
 * This is a Marker tag
 */
public class SyntheticCallTag implements Tag {

	final public static String TAG_NAME = "SyntheticCall";

	@Override
	public String getName() {
		return TAG_NAME;
	}

	@Override
	public byte[] getValue() throws AttributeValueException {
		return null;
	}

	private SyntheticCallTag() {
	}

	final static public SyntheticCallTag TAG = new SyntheticCallTag();

}

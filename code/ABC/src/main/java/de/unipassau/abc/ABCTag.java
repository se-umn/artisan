package de.unipassau.abc;

import soot.tagkit.AttributeValueException;
import soot.tagkit.Tag;

/**
 * This is a Marker tag
 */
public class ABCTag implements Tag {

	final public static String TAG_NAME = "ABC";

	@Override
	public String getName() {
		return TAG_NAME;
	}

	@Override
	public byte[] getValue() throws AttributeValueException {
		return null;
	}

	private ABCTag(){ }
	
	final static public ABCTag TAG = new ABCTag();

}

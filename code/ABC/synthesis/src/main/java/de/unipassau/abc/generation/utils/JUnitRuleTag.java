package de.unipassau.abc.generation.utils;

import soot.tagkit.AttributeValueException;
import soot.tagkit.Tag;

public class JUnitRuleTag implements Tag{

	public static JUnitRuleTag TAG = new JUnitRuleTag();
	
	@Override
	public String getName() {
		return "JUnitRule"; 
	}

	@Override
	public byte[] getValue() throws AttributeValueException {
		// TODO Auto-generated method stub
		return null;
	}

}

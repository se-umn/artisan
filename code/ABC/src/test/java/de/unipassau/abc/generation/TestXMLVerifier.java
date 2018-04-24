package de.unipassau.abc.generation;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import de.unipassau.abc.tracing.XMLVerifier;
import soot.Value;
import soot.jimple.FloatConstant;
import soot.jimple.IntConstant;

public class TestXMLVerifier {

	@Test
	public void testTrueBoolean() throws IOException {
		Value booleanValue = IntConstant.v(1);
		Boolean boolObject = Boolean.TRUE;
		XMLVerifier.verifyPrimitive(boolObject, booleanValue.toString());
	}

	@Test
	public void testFalseBoolean() throws IOException {
		Value booleanValue = IntConstant.v(0);
		Boolean boolObject = Boolean.FALSE;
		XMLVerifier.verifyPrimitive(boolObject, booleanValue.toString());
	}

	@Test
	public void testWrongBoolean() throws IOException {
		try {
			Value booleanValue = IntConstant.v(2);
			Boolean boolObject = Boolean.FALSE;
			XMLVerifier.verifyPrimitive(boolObject, booleanValue.toString());
		} catch (AssertionError e) {
			Assert.assertTrue(e.getMessage().contains("Unexpected value for boolean!"));
		}
	}

	@Test
	public void testFloat() throws IOException {
		Value floatValue = FloatConstant.v(69.0F);
		Float floatObject = new Float(69.0);
		XMLVerifier.verifyPrimitive(floatObject, floatValue.toString());

	}
}

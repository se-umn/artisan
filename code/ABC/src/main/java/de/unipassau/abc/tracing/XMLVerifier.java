package de.unipassau.abc.tracing;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.thoughtworks.xstream.XStream;

/**
 * @author gambi
 *
 */
public class XMLVerifier {

	public static boolean skip = Boolean.parseBoolean(System.getProperty("skip.xmlverifier", "false"));
	
	/**
	 * 
	 * @param object
	 * @param xmlExpected
	 * @throws IOException
	 */
	public static void verify(Object object, String xmlExpected) throws IOException {
		if (skip ){
			return;
		}
		
		// clear out existing permissions and set own ones
		XStream xstream = new XStream();

		String expected = new String(Files.readAllBytes(Paths.get(xmlExpected)));
		String actual = xstream.toXML(object);
		//
		
		org.junit.Assert.assertEquals("Object " + object + " does not match its serialized form "
				+ XMLDumper.loadObject(xmlExpected) + " inside file " + xmlExpected, expected, actual);
	}

	// org.junit.ComparisonFailure: Object true does not match its expected
	// value 1 expected:<[true]> but was:<[1]>

	// Primitive Values Are Different !
	// Floating point numbers are store somehow with F and sometimes without it
	// org.junit.ComparisonFailure: Object 69.0 does not match its expected
	// value 69.0F expected:<69.0[F]> but was:<69.0[]>
	// Called by Carver
	public static void verifyPrimitive(Object boxedPrimitive, String expecteValueToString) throws IOException {
		if (skip ){
			return;
		}
		
		Object boxedExpectedValue = null;
		
		if (boxedPrimitive instanceof Boolean) {
			if( "1".equals(expecteValueToString) ){
				boxedExpectedValue  = Boolean.TRUE; 
			} else if( "0".equals(expecteValueToString) ){
				boxedExpectedValue  = Boolean.TRUE; 
			}  else {
				org.junit.Assert.fail("Unexpected value for boolean!");
			}
		} else if (boxedPrimitive instanceof Byte) {
			// Not sure this is ok !
			boxedExpectedValue = Byte.parseByte(expecteValueToString);
		} else if (boxedPrimitive instanceof Character) {
			boxedExpectedValue = Character.valueOf(expecteValueToString.charAt(0));
		} else if (boxedPrimitive instanceof Double) {
			boxedExpectedValue = Double.parseDouble( expecteValueToString );
		} else if (boxedPrimitive instanceof Float) {
			boxedExpectedValue  = Float.parseFloat( expecteValueToString );
		} else if (boxedPrimitive instanceof Integer) {
			boxedExpectedValue = Integer.parseInt( expecteValueToString );
		} else if (boxedPrimitive instanceof Long) {
			boxedExpectedValue = Long.parseLong( expecteValueToString );
		} else if (boxedPrimitive instanceof Short) {
			boxedExpectedValue = Short.parseShort( expecteValueToString );
		} else {
			org.junit.Assert.fail("Unexpected primitive type " + boxedPrimitive );
		}
		
		org.junit.Assert.assertEquals(
				"Object " + boxedPrimitive + " does not match its expected string value " + expecteValueToString,
				boxedExpectedValue, boxedPrimitive);
	}

}

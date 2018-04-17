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

	/**
	 * 
	 * @param object
	 * @param xmlExpected
	 * @throws IOException
	 */
	public static void verify(Object object, String xmlExpected) throws IOException {
		// clear out existing permissions and set own ones
		XStream xstream = new XStream();
		
		String expected = new String(Files.readAllBytes(Paths.get(xmlExpected)));
		String actual = xstream.toXML(object);
		//
		org.junit.Assert.assertEquals("Object " + object + " does not match its serialized form " + XMLDumper.loadObject( xmlExpected ) + " inside file " + xmlExpected,
				expected, actual);
	}

	// org.junit.ComparisonFailure: Object true does not match its expected value 1 expected:<[true]> but was:<[1]>

	// Primitive Values Are Different !
	public static void verifyPrimitive(Object boxedPrimitive, String expecteValueToString) throws IOException {
		
		String actualValueToString = boxedPrimitive.toString();
		
		// org.junit.ComparisonFailure: Object true does not match its expected value 1 expected:<[true]> but was:<[1]>
		if( "true".equalsIgnoreCase( actualValueToString) ){
			actualValueToString = "1";
		} else if ("false".equalsIgnoreCase( actualValueToString ) ){
			actualValueToString = "0";
		}
		
		if( "true".equalsIgnoreCase( expecteValueToString) ){
			expecteValueToString = "1";
		} else if ("false".equalsIgnoreCase( expecteValueToString ) ){
			expecteValueToString = "0";
		}
		
		
		
		// clear out existing permissions and set own ones
		org.junit.Assert.assertEquals("Object " + actualValueToString + " does not match its expected value " + expecteValueToString,
				expecteValueToString, actualValueToString);
	}

}

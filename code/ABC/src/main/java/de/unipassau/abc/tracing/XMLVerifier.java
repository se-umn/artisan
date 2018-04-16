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
		System.out.println("XMLVerifier.verify() Actual : " + object );
		System.out.println("XMLVerifier.verify() Stored : " + XMLDumper.loadObject( xmlExpected ) );
		//
		org.junit.Assert.assertEquals("Object " + object + " does not match its serialized form " + XMLDumper.loadObject( xmlExpected ) + " inside file " + xmlExpected,
				expected, actual);
	}

	// Primitive Values Are Different !
	public static void verifyPrimitive(Object boxedPrimitive, String valueToString) throws IOException {
		// clear out existing permissions and set own ones
		org.junit.Assert.assertEquals("Object " + boxedPrimitive + " does not match its expected value" + valueToString,
				boxedPrimitive.toString(), valueToString);
	}

}

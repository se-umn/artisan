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
		org.junit.Assert.assertEquals("Object " + object + " does not match its serialized form inside " + xmlExpected,
				expected, actual);
	}

}

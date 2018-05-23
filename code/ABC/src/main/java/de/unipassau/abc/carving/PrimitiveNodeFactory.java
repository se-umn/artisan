package de.unipassau.abc.carving;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicInteger;

import com.thoughtworks.xstream.XStream;

public class PrimitiveNodeFactory {

	private static AtomicInteger uniqueId = new AtomicInteger(0);

	public static PrimitiveValue get(String type, String value) {
		return new PrimitiveValue(uniqueId.incrementAndGet(), type, value);
	}

	public static PrimitiveValue createStringNode(String type, String value) {
		if (!type.equals("java.lang.String")) {
			throw new RuntimeException("Cannot create a string node for type " + type);
		}

		System.out.println("PrimitiveNodeFactory.get() node value for" + type + " -- " + value);

		String theValue = null;

		if (value != null) {

			// Read the value from value
			XStream xStream = new XStream();
			try {
				theValue = (String) xStream
						.fromXML(new String(Files.readAllBytes(Paths.get(value)), StandardCharsets.UTF_8));
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException("Cannot create NodeValue for " + type + " -- " + value, e);
			}
		}
		return new PrimitiveValue(uniqueId.incrementAndGet(), type, theValue);
	}

}

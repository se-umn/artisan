package de.unipassau.abc.tracing;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thoughtworks.xstream.XStream;

/**
 * Methods to dump values and objects to files.
 * 
 * We need to link this to trace and object instances, but we cannot get to
 * MethodInvocations statically, so we create a data structure which computes
 * the HASH of each file relative to the use of each instance. Everytime we
 * invoke a method on an instance we increment the dump. Purity checks and such
 * might get in the way but we might be able to solve issues there. The
 * alternative is to: send back the dump, and include the reference in the
 * trace.
 * 
 * @author gambi
 *
 */
public class XMLDumper {

	private static File dumpDirectory;
	static {
		try {

			// USE A CONSTANT FOR THIS !
			if (System.getProperty("dump.output") != null) {
				dumpDirectory = new File(System.getProperty("dump.output"));
			} else if (System.getenv("dump.output") != null) {
				dumpDirectory = new File(System.getenv("dump.output"));
			} else {
				dumpDirectory = Files.createTempDirectory("tmp-dump").toFile();
			}
			System.out.println("**** Dump XML Values to " + dumpDirectory);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// This is to avoid adding complexity to the instrumentation
	// Use String instead of File to further simplify the interfaces
	// TODO How to handle null values ? we do not create a file for them? or we
	// create an empty file?
	public static String dumpObject(String jimpleMethod, Object object) throws IOException {

		if (object == null) {
			System.out.println("XMLDumper.dumpObject() Null object for " + jimpleMethod);
			return null;
		}
		// Here there might be different strategies, but since we link this back
		// also a random ID is ok.
		File xmlFile = new File(dumpDirectory, UUID.randomUUID().toString() + ".xml");
		XStream xstream = new XStream();
		xstream.toXML(object, new FileWriter(xmlFile));
		return xmlFile.getAbsolutePath();
	}

	public static Object loadObject(String xmlFile) throws IOException {
		XStream xstream = new XStream();
		return xstream.fromXML(new File(xmlFile));
	}

}

package de.unipassau.abc.tracing;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collection;
import java.util.UUID;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.security.NoTypePermission;
import com.thoughtworks.xstream.security.NullPermission;
import com.thoughtworks.xstream.security.PrimitiveTypePermission;

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

	public static final String DUMP_DIR_PROPERTY_NAME = "dump.output";
	private static File dumpDirectory;
	static {
		try {

			// USE A CONSTANT FOR THIS !
			if (System.getProperty(DUMP_DIR_PROPERTY_NAME) != null) {
				dumpDirectory = new File(System.getProperty(DUMP_DIR_PROPERTY_NAME));
			} else if (System.getenv(DUMP_DIR_PROPERTY_NAME) != null) {
				dumpDirectory = new File(System.getenv(DUMP_DIR_PROPERTY_NAME));
			} else {
				dumpDirectory = Files.createTempDirectory("tmp-dump").toFile();
			}
			System.out.println("**** Dump and Load XML Values directory " + dumpDirectory);
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
		
		// clear out existing permissions and set own ones
		xstream.addPermission(NoTypePermission.NONE);
		// allow some basics
		xstream.addPermission(NullPermission.NULL);
		xstream.addPermission(PrimitiveTypePermission.PRIMITIVES);
		xstream.allowTypesByWildcard(new String[] {
			    "java.lang.**"
			});
		//
		return xstream.fromXML(new File(xmlFile));
	}

}

package de.unipassau.abc.tracing;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.security.NoTypePermission;
import com.thoughtworks.xstream.security.NullPermission;
import com.thoughtworks.xstream.security.PrimitiveTypePermission;

import de.unipassau.abc.carving.MethodInvocation;
import de.unipassau.abc.carving.MethodInvocationMatcher;
import de.unipassau.abc.carving.ObjectInstance;

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
	// Filter objects to dump
	public static final String DUMP_BY_PROPERTY_NAME = "dump.by";
	
	private static File dumpDirectory;
	private static MethodInvocationMatcher methodInvocationMatcher ;
	
	//
	private static MethodInvocationMatcher abcMethodInvocationMatcher=getMatcherFor("package=abc");
	
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
			methodInvocationMatcher = getMatcherFor(System.getProperty(DUMP_BY_PROPERTY_NAME));
			
			// Do not output anything on console
			// System.out.println("**** Dump and Load XML Values directory " + dumpDirectory);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static MethodInvocationMatcher getMatcherFor(String dumpBy) {
		if( dumpBy == null ){
			return MethodInvocationMatcher.alwaysMatch();
		}
		
		final String type = dumpBy.split("=")[0];
		final String regEx = dumpBy.split("=")[1];
		
		switch (type) {
		case "package":
			return MethodInvocationMatcher.byPackage(regEx);
		case "class":
			// return MethodInvocationMatcher.byClass(regEx);
			return MethodInvocationMatcher.byClassLiteral(regEx);
		case "method":
			// PAY ATTENTION TO THIS
			return MethodInvocationMatcher.byMethodLiteral(regEx);
		case "regex":
			return MethodInvocationMatcher.byMethod(regEx);
		case "invocation":
			return MethodInvocationMatcher.byMethodInvocation(regEx);
		case "return":
			return MethodInvocationMatcher.byReturnType(regEx);
		case "instance":
			// TODO how to rule out primitive, null, and possibly Strings ?
			return MethodInvocationMatcher.byInstance(new ObjectInstance(regEx));
		default:
			throw new RuntimeException("Unknown matcher specification " + type + "=" + regEx);
		}
	}

	/**
	 * Dumps to XML file the object, either owner or return type, of the given method invocation
	 * 
	 * @param jimpleMethod
	 * @param object
	 * @return
	 * @throws IOException
	 */
	public static String dumpObject(String jimpleMethod, Object object) throws IOException {

		if (object == null) {
//			System.out.println("XMLDumper.dumpObject() Null object for " + jimpleMethod);
			return null;
		}
		
		if( ! ( 
				methodInvocationMatcher.matches( new MethodInvocation(jimpleMethod, -1) ) ||
				abcMethodInvocationMatcher.matches( new MethodInvocation(jimpleMethod, -1) )
			) 
				&& ! (object instanceof String) ){
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
		// xstream.addPermission(AnyTypePermission.ANY);
		xstream.allowTypeHierarchy(Object.class);

		// Allow ALL The objects
		xstream.addPermission(NullPermission.NULL);
		xstream.addPermission(PrimitiveTypePermission.PRIMITIVES);
		xstream.allowTypesByWildcard(new String[] { "java.lang.**" });
		xstream.allowTypesByWildcard(new String[] { "*.**" });
		//
		return xstream.fromXML(new File(xmlFile));
	}

}

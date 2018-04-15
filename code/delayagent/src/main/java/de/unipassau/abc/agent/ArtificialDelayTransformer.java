package de.unipassau.abc.agent;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

/**
 * visit visitSource? visitOuterClass? ( visitAnnotation | visitAttribute )* (
 * visitInnerClass | visitField | visitMethod )* visitEnd
 * 
 * @author gambi
 *
 */
public class ArtificialDelayTransformer implements ClassFileTransformer {

	public byte[] transform(ClassLoader loader, String className, Class redefiningClass, ProtectionDomain domain,
			byte[] bytes) throws IllegalClassFormatException {

		ClassReader cr = new ClassReader(bytes);

		// Instrument java.sql classes to introduce a delay before invocation
		// if (!className.startsWith("java/sql/PreparedStatement") ||
		// className.startsWith("java/sql/Statement")
		// ) {
		// return bytes;
		// }
		if (!className.startsWith("org/hotelme")) {
			return bytes;
		}
		System.out.println("Instrumenting...... " + className);
		byte[] result = bytes;
		try {
			// Create a reader for the existing bytes.
			ClassReader reader = new ClassReader(bytes);
			// Create a writer which uses the reader

			ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
			// Create our class adapter, pointing to the class writer
			// and then tell the reader to notify our visitor of all
			// bytecode instructions

			reader.accept(new AddDelayAdapter(writer), ClassReader.EXPAND_FRAMES);

			//
			// get the result from the writer.
			result = writer.toByteArray();
		}
		// Runtime exceptions thrown by the above code disappear
		// This catch ensures that they are at least reported.
		catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

}

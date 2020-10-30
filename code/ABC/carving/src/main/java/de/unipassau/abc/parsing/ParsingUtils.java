package de.unipassau.abc.parsing;

import java.io.File;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.unipassau.abc.data.JimpleUtils;
import de.unipassau.abc.data.MethodInvocation;
import edu.emory.mathcs.backport.java.util.Arrays;
import soot.G;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.options.Options;

public class ParsingUtils {

	private static final Logger logger = LoggerFactory.getLogger(ParsingUtils.class);

	//
	public static boolean isArrayMethod(String methodSignature) {
		return JimpleUtils.getClassNameForMethod(methodSignature).endsWith("[]");
	}

	public static SootMethod getSootMethodFor(String methodSignature) {
		if (isArrayMethod(methodSignature)) {
			return null;
		}

		try {

			return Scene.v().getMethod(methodSignature);
		} catch (Throwable e) {
			SootClass sootClass = Scene.v().getSootClass(JimpleUtils.getClassNameForMethod(methodSignature));
			logger.warn("Cannot find method " + methodSignature + " in class " + sootClass);
			for (SootMethod sootMethod : sootClass.getMethods()) {
				System.out.println("- " + sootMethod);
			}
			return null;
		}
	}

	// TODO Do we really need soot to parse the trace ?
	public static void setupSoot(File androidJar, File apk) {
		G.reset();
		Options.v().set_allow_phantom_refs(true);
		Options.v().set_whole_program(true);

		Options.v().set_soot_classpath(androidJar.getAbsolutePath());
		Options.v().set_process_dir(Arrays.asList(new String[] { apk.getAbsolutePath() }));
		Options.v().set_src_prec(soot.options.Options.src_prec_apk);

		soot.options.Options.v().set_allow_phantom_refs(true);
	}

	public static String prettyPrint(List<MethodInvocation> subsumingMethods) {
		StringBuffer stringBuffer = new StringBuffer();
		for (MethodInvocation methodInvocation : subsumingMethods) {
			stringBuffer.append(methodInvocation).append("\n");
		}
		return null;
	}

}

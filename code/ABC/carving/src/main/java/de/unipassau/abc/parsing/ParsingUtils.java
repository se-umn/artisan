package de.unipassau.abc.parsing;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import de.unipassau.abc.instrumentation.SceneInstrumenterWithMethodParameters;
import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.unipassau.abc.data.JimpleUtils;
import de.unipassau.abc.data.MethodInvocation;
import edu.emory.mathcs.backport.java.util.Arrays;
import soot.*;
import soot.jimple.infoflow.android.manifest.ProcessManifest;
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

		System.out.println(apk.getAbsolutePath());
		System.out.println(androidJar.getAbsolutePath());

		try{
			// Setup soot
			G.reset();
			Options.v().set_allow_phantom_refs(true);

			// Input is an APK
			soot.options.Options.v().set_src_prec(soot.options.Options.src_prec_apk);

			// Specifiy the APK
			List<String> necessaryJar = new ArrayList<String>();
			necessaryJar.add(apk.getAbsolutePath());
			Options.v().set_process_dir(necessaryJar);

			//
			// Output is an APK, too//-f J
			Options.v().set_output_format(soot.options.Options.output_format_dex);
			Options.v().set_force_overwrite(true);

			// Specify output location
			Options.v().set_output_dir("/tmp");

			// Register our instrumentation code. We need the official appPackageName, so we
			// get it from the APK manifest
			ProcessManifest processMan = new ProcessManifest(apk.getAbsolutePath());
			String appPackageName = processMan.getPackageName();

			System.out.println("Main.main() DEBUG: MIN SDK VERSION = " + processMan.getMinSdkVersion());
			System.out.println("Main.main() DEBUG: TARGET SDK VERSION = " + processMan.targetSdkVersion());
			// TODO Change this using some option to force it ... or some way to automatically enable it?
			if (processMan.getMinSdkVersion() < 22 || processMan.targetSdkVersion() < 22) {
				// Soot breaks if the minSdkVersion is smaller than 22 because of multidex
				// However, with multidex disabled, an instrumented APK will crash on start
				throw new RuntimeException(String.format("The SDK level of the APK is %d. Must be >= 22",
						processMan.getMinSdkVersion()));
			}

			// We must set it manually, otherwise Soot will assume the default API version = 15
			Options.v().set_android_api_version(processMan.targetSdkVersion());

			Options.v().set_process_multiple_dex(true);

			// This is where the instrumentation takes place.
			//SceneInstrumenterWithMethodParameters abcInstrumentation = new SceneInstrumenterWithMethodParameters(appPackageName);
			//abcInstrumentation.setPackageFilters(cli.getPackageFilters());
			//PackManager.v().getPack("wjtp").add(new Transform("wjtp.mt", abcInstrumentation));

			// Make sure Soot knows the classes our instrumentation will use:
			//Scene.v().addBasicClass(utils.Constants.MONITOR_CLASS);
			// TODO Where is this really used?
			//Scene.v().addBasicClass("utils.logicClock");

			String pathArray[] = SystemUtils.JAVA_CLASS_PATH.split(":");
			String validPath = "";
			for(String path:pathArray){
				if((new File(path)).exists()){
					validPath = validPath + path + File.pathSeparatorChar;
				}
			}
			validPath = validPath + androidJar.getAbsolutePath();
			String sootCP = validPath;

			String[] sootArgs = new String[] { //
					"-w", // This should be the same as setting the "Whole program analysis" flag
					"-cp", sootCP }; // The classpath that Soot uses for its analysis
//				"-debug" };
			soot.Main.main(sootArgs);
		}
		catch(Exception e){
			e.printStackTrace();
			throw new RuntimeException("Could not initialize soot");
		}
	}

	public static String prettyPrint(List<MethodInvocation> subsumingMethods) {
		StringBuffer stringBuffer = new StringBuffer();
		for (MethodInvocation methodInvocation : subsumingMethods) {
			stringBuffer.append(methodInvocation).append("\n");
		}
		return null;
	}

}

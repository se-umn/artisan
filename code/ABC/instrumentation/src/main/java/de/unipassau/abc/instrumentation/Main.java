package de.unipassau.abc.instrumentation;

import com.lexicalscope.jewel.cli.ArgumentValidationException;
import com.lexicalscope.jewel.cli.CliFactory;
import com.lexicalscope.jewel.cli.Option;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.SystemUtils;
import org.xmlpull.v1.XmlPullParserException;
import soot.G;
import soot.PackManager;
import soot.Scene;
import soot.Transform;
import soot.jimple.infoflow.android.manifest.ProcessManifest;
import soot.options.Options;

/**
 * Replace the cgInstr.sh script. This should only take the input APK and the
 * possibly the outputDir
 */
public class Main {

	public interface CLI {
		@Option(longName = "apk")
		public File getAPK();

		@Option(longName = "output-to", defaultValue = "./apk-instrumented/")
		public File getOutputDir();

		@Option(longName = "android-jar")
		public File getAndroidJar();

		@Option(longName = "filter-package", defaultValue = {})
		public List<String> getPackageFilters();
		
		@Option(longName = "debug")
		public boolean getDebug();
		
		@Option(longName = "multi-thread")
        public boolean getMultiThread();
		
	}

	public static void main(String args[]) throws IOException, XmlPullParserException {
		CLI cli = null;
		try {
			cli = CliFactory.parseArguments(CLI.class, args);
		} catch (ArgumentValidationException e) {
			e.printStackTrace();
			System.exit(1);
		}

		// Setup soot
		G.reset();
		/*
		 * In https://github.com/soot-oss/soot/issues/1152 soot does not seem to be able
		 * to correctly repackage few apps even if no instrumentation takes place.
		 * AnyMemo10.10.1 might be one of those The command suggested is:
		 * 
		 * java -cp sootclasses-trunk-jar-with-dependencies.jar soot.Main -src-prec apk
		 * -f dex -android-jars $ANDROID_HOME/platforms -process-dir
		 * com.totrix.glmobile.apk -process-multiple-dex -w -allow-phantom-refs
		 */

		Options.v().set_allow_phantom_refs(true);

		// Input is an APK
		soot.options.Options.v().set_src_prec(soot.options.Options.src_prec_apk);

		// Specifiy the APK
		List<String> necessaryJar = new ArrayList<String>();
		necessaryJar.add(cli.getAPK().getAbsolutePath());
		Options.v().set_process_dir(necessaryJar);

		//
		// Output is an APK, too//-f J
		Options.v().set_output_format(soot.options.Options.output_format_dex);
		Options.v().set_force_overwrite(true);
		// Specify output location
		//
		// TODO Does this need to exist?
		Options.v().set_output_dir(cli.getOutputDir().getAbsolutePath());

		// Register our instrumentation code. We need the official appPackageName, so we
		// get it from the APK manifest
		ProcessManifest processMan = new ProcessManifest(cli.getAPK().getAbsolutePath());
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
		SceneInstrumenterWithMethodParameters abcInstrumentation = new SceneInstrumenterWithMethodParameters(appPackageName);
		// Configure the class
		abcInstrumentation.setPackageFilters(cli.getPackageFilters());
//		abcInstrumentation.setDebug(cli.getDebug());
//		abcInstrumentation.setMultiThreadedExecution(cli.getMultiThread());


		PackManager.v().getPack("wjtp").add(new Transform("wjtp.mt", abcInstrumentation));

		// Make sure Soot knows the classes our instrumentation will use:
		Scene.v().addBasicClass(utils.Constants.MONITOR_CLASS);
		// TODO Where is this really used?
		Scene.v().addBasicClass("utils.logicClock");

		String sootCP = SystemUtils.JAVA_CLASS_PATH.concat("" + File.pathSeparatorChar)
				.concat(cli.getAndroidJar().getAbsolutePath());

		String[] sootArgs = new String[] { //
				"-w", // This should be the same as setting the "Whole program analysis" flag
				"-cp", sootCP }; // The classpath that Soot uses for its analysis
//				"-debug" };
		soot.Main.main(sootArgs);
	}
}

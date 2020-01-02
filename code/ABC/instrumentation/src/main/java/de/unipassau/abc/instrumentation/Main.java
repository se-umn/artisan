package de.unipassau.abc.instrumentation;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.SystemUtils;
import org.xmlpull.v1.XmlPullParserException;

import com.lexicalscope.jewel.cli.ArgumentValidationException;
import com.lexicalscope.jewel.cli.CliFactory;
import com.lexicalscope.jewel.cli.Option;

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

		Options.v().set_allow_phantom_refs(true);
//		Options.v().set_whole_program(true);

		// Input is an APK
		soot.options.Options.v().set_src_prec(soot.options.Options.src_prec_apk);
		// Specifiy the APK
		List<String> necessaryJar = new ArrayList<String>();
		necessaryJar.add(cli.getAPK().getAbsolutePath());
		Options.v().set_process_dir(necessaryJar);

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

		SceneInstrumenterWithMethodParameters icgins = new SceneInstrumenterWithMethodParameters(appPackageName);
		PackManager.v().getPack("wjtp").add(new Transform("wjtp.mt", icgins));

		// Tell Soot about the classes our instrumentation needs:
		Scene.v().addBasicClass(utils.Constants.MONITOR_CLASS);
		// TODO Where is this really used?
		Scene.v().addBasicClass("utils.logicClock");

		String sootCP = SystemUtils.JAVA_CLASS_PATH.concat("" + File.pathSeparatorChar)
				.concat(cli.getAndroidJar().getAbsolutePath()).toString();

		String[] sootArgs = new String[] { "-w", // No idea what's this
				"-cp", sootCP, //
				"-p", "cg", "verbose:false,implicit-entry:true", //
				"-debug" // No idea what's this
		};

		soot.Main.main(sootArgs);
	}
}

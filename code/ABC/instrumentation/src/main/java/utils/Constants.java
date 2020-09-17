package utils;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;

import de.unipassau.abc.instrumentation.Monitor;
import soot.jimple.infoflow.android.manifest.ProcessManifest;

public class Constants {

	/**
	 * This is the class which implements the Trace/Monitoring interface. Since,
	 * this is meta-programming we will get reference to the methods implemented by
	 * this class and call them from the instrumentation code
	 */
	public static final String MONITOR_CLASS = Monitor.class.getName();

	public static String getAPKName() throws IOException, XmlPullParserException {
		String apkPath = soot.options.Options.v().process_dir().get(0);
		ProcessManifest manifest = new ProcessManifest(apkPath);
		return manifest.getPackageName();
		// GlobalRef.apkVersionCode = manifest.getVersionCode();
		// GlobalRef.apkVersionName = manifest.getVersionName();
		// GlobalRef.apkMinSdkVersion = manifest.getMinSdkVersion();
		// GlobalRef.apkPermissions = manifest.getPermissions();
	}

}

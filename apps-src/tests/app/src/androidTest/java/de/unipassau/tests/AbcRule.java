package de.unipassau.tests;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static androidx.test.platform.app.InstrumentationRegistry.getArguments;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.BySelector;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject2;
import androidx.test.uiautomator.Until;
import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public class AbcRule implements TestRule {

  public static final long TIMEOUT = 5000;
  private static final Pattern KEEPS_STOPPING_PATTERN = Pattern
      .compile("[A-Za-z\\s]+keeps stopping");
  private String packageName;
  UiDevice device;

  public AbcRule(String packageName) {
    this.packageName = packageName;
  }

  private void startMainActivityFromHomeScreen() {
    // Initialize UiDevice instance
    device = UiDevice.getInstance(getInstrumentation());

    // Start from the home screen
    device.pressHome();

    // Wait for launcher
    final String launcherPackage = getLauncherPackageName();
    assertThat(launcherPackage, notNullValue());
    device.wait(Until.hasObject(By.pkg(launcherPackage).depth(0)), TIMEOUT);

    // Launch the blueprint app
    Context context = getApplicationContext();
    final Intent intent = context.getPackageManager()
        .getLaunchIntentForPackage(packageName);
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);    // Clear out any previous instances
    context.startActivity(intent);

    // Wait for the app to appear
    device.wait(Until.hasObject(By.pkg(packageName).depth(0)), TIMEOUT);
  }

  private void shutDown() {
    UiObject2 alert = device.findObject(By.text(KEEPS_STOPPING_PATTERN));
    if (alert != null) {
      findObjectOrWait(By.text("Close app")).click();
    }
  }

  private void givePermissions() {
    UiObject2 permissionRequest = device.findObject(By.text("ALLOW"));
    while (permissionRequest != null) {
      permissionRequest.click();
      permissionRequest = device.findObject(By.text("ALLOW"));
    }
  }

  public UiObject2 findObjectOrWait(BySelector selector) {
    device.wait(Until.hasObject(selector), TIMEOUT);
    return device.findObject(selector);
  }

  public List<UiObject2> findObjectsOrWait(BySelector selector) {
    device.wait(Until.hasObject(selector), TIMEOUT);
    return device.findObjects(selector);
  }

  /**
   * Reinstalls the apk from the path that is given from the command line by
   * `adb shell am instrument -w -e apk-path someApk.apk ...`.
   */
  private void reinstallApk() {
    String apkPath = getArguments().getString("apk-path");
    if (apkPath == null || apkPath.isEmpty()) {
      return;
    }

    try {
      // Reinstall the apk, but retain data
      InstrumentationRegistry
          .getInstrumentation().getUiAutomation().executeShellCommand("pm install -r " + apkPath).close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Uses package manager to find the package name of the device launcher. Usually this package is
   * "com.android.launcher" but can be different at times. This is a generic solution which works on
   * all platforms.`
   */
  private String getLauncherPackageName() {
    // Create launcher Intent
    final Intent intent = new Intent(Intent.ACTION_MAIN);
    intent.addCategory(Intent.CATEGORY_HOME);

    // Use PackageManager to get the launcher package name
    PackageManager pm = getApplicationContext().getPackageManager();
    ResolveInfo resolveInfo = pm.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
    return resolveInfo.activityInfo.packageName;
  }



  @Override
  public Statement apply(final Statement base, Description description) {
    return new Statement() {
      @Override
      public void evaluate() throws Throwable {
        reinstallApk();
        startMainActivityFromHomeScreen();
        givePermissions();
        try {
          base.evaluate();
        } finally {
          shutDown();
        }
      }
    };
  }
}

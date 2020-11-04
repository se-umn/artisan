package de.unipassau.tests;

import android.os.RemoteException;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SdkSuppress;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiObject2;
import androidx.test.uiautomator.Until;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@SdkSuppress(minSdkVersion = 18)
public class GnuCash207Test {
  private static final String TARGET_PACKAGE_NAME = "org.gnucash.android";

  @Rule
  public AbcRule rule = new AbcRule(TARGET_PACKAGE_NAME);

  @Test
  public void crash() throws RemoteException {
    UiObject2 nextButton = rule.findObjectOrWait(By.text("NEXT"));
    if(nextButton != null){
      nextButton.click();
      rule.findObjectOrWait(By.text("NEXT")).click();
      rule.findObjectOrWait(By.text("NEXT")).click();
      rule.findObjectOrWait(By.text("Automatically send crash reports")).click();
      rule.findObjectOrWait(By.text("NEXT")).click();
      rule.findObjectOrWait(By.text("DONE")).click();
      rule.findObjectOrWait(By.text("ALLOW")).click();
    }
    rule.findObjectOrWait(By.desc("Navigation drawer opened")).click();
    rule.findObjectOrWait(By.text("Reports")).click();
    rule.findObjectOrWait(By.text("PIE CHART")).click();
    rule.findObjectOrWait(By.text("Last 3 months")).click();
    rule.findObjectOrWait(By.text("Custom range…")).clickAndWait(Until.newWindow(),1000);

    rule.device.setOrientationLeft();

    // Unfreeze Rotation so it doesn't affect following tests
    rule.device.unfreezeRotation();
  }
}

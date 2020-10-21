package de.unipassau.tests;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SdkSuppress;
import androidx.test.uiautomator.By;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@SdkSuppress(minSdkVersion = 18)
public class BasicCalculator10Test {
  private static final String TARGET_PACKAGE_NAME = "de.unipassau.calculator";

  @Rule
  public AbcRule rule = new AbcRule(TARGET_PACKAGE_NAME);

  @Test
  public void nestedException() {
    rule.findObjectOrWait(By.text("Enter expression to evaluate")).setText("abcdsf,");
    rule.findObjectOrWait(By.text("CALCULATE")).click();
  }

  @Test
  public void simpleIllegalArgumentException() {
    rule.findObjectOrWait(By.text("Enter expression to evaluate")).setText("13");
    rule.findObjectOrWait(By.text("CALCULATE")).click();
  }

  @Test
  public void simpleNullPointerException() {
    rule.findObjectOrWait(By.text("Enter expression to evaluate")).setText("");
    rule.findObjectOrWait(By.text("CALCULATE")).click();
  }
}

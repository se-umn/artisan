package de.unipassau.tests;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SdkSuppress;
import androidx.test.uiautomator.By;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@SdkSuppress(minSdkVersion = 18)
public class BasicCalculator11Test {
  private static final String TARGET_PACKAGE_NAME = "de.unipassau.calculator";

  @Rule
  public AbcRule rule = new AbcRule(TARGET_PACKAGE_NAME);

  @Test
  public void illegalArgumentException() {
    rule.findObjectOrWait(By.res(TARGET_PACKAGE_NAME, "input")).setText("+");
    rule.findObjectOrWait(By.text("=")).click();
  }

  @Test
  public void illegalStateException() {
    rule.findObjectOrWait(By.res(TARGET_PACKAGE_NAME, "input")).setText("2");
    rule.findObjectOrWait(By.text("ERASE")).click();
    rule.findObjectOrWait(By.text("ERASE")).click();
  }

  @Test
  public void multipleUIActivities() {
    rule.findObjectOrWait(By.res(TARGET_PACKAGE_NAME, "input")).setText("((2,2),(3,4))*4");
    rule.findObjectOrWait(By.text("=")).click();
    rule.findObjectOrWait(By.res(TARGET_PACKAGE_NAME, "history_view")).click();
    rule.findObjectOrWait(By.desc("More options")).click();
    rule.findObjectOrWait(By.text("Settings")).click();
    // There are two clicks on settings in the original Test, however after clicking Settings once it disappears, and this would cause a null object reference
    // rule.findObjectOrWait(By.text("Settings")).click();
    rule.device.pressBack();
    rule.device.pressBack();
    rule.findObjectOrWait(By.res(TARGET_PACKAGE_NAME, "input")).setText("2=2");
    rule.findObjectOrWait(By.text("=")).click();
  }

  @Test
  public void nestedArrayIndexOutOfBoundsException() {
    rule.findObjectOrWait(By.res(TARGET_PACKAGE_NAME, "input")).setText("((1),(2,3))*2");
    rule.findObjectOrWait(By.text("=")).click();
  }

  @Test
  public void NestedException() {
    rule.findObjectOrWait(By.res(TARGET_PACKAGE_NAME, "input")).setText("asfsadf");
    rule.findObjectOrWait(By.text("=")).click();
  }

  @Test
  public void NumberFormatException() {
    rule.findObjectOrWait(By.res(TARGET_PACKAGE_NAME, "input")).setText("(2,2..)=)");
    rule.findObjectOrWait(By.text("=")).click();
  }

  @Test
  public void SimpleArrayIndexOutOfBoundsException() {
    rule.findObjectOrWait(By.res(TARGET_PACKAGE_NAME, "input")).setText("(2,2))");
    rule.findObjectOrWait(By.text("=")).click();
  }

  @Test
  public void SimpleIllegalArgumentException() {
    rule.findObjectOrWait(By.res(TARGET_PACKAGE_NAME, "input")).setText("13");
    rule.findObjectOrWait(By.text("=")).click();
  }

  @Test
  public void SimpleNullPointerException() {
    rule.findObjectOrWait(By.text("=")).click();
  }
}

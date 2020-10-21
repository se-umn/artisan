package de.unipassau.tests;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SdkSuppress;
import androidx.test.uiautomator.By;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@SdkSuppress(minSdkVersion = 18)
public class AnyMemo10101Test {

  private static final String TARGET_PACKAGE_NAME = "org.liberty.android.fantastischmemo";

  @Rule
  public AbcRule r = new AbcRule(TARGET_PACKAGE_NAME);

  @Test
  public void crash() {
    if (r.device.hasObject(By.text("New version installed"))) {
      r.findObjectOrWait(By.text("OK")).click();
    }

    r.findObjectOrWait(By.desc("Navigate up")).click();
    r.findObjectOrWait(By.text("Download")).click();
    r.findObjectOrWait(By.text("AnyMemo.org")).click();
    r.findObjectOrWait(By.text("Geography")).click();
    r.findObjectOrWait(By.text("Countries Capitals Flags Oceans of the world")).click();
    r.findObjectOrWait(By.text("YES")).click();
    r.device.pressBack();
    r.device.pressBack();
    r.findObjectOrWait(By.desc("Navigate up")).click();
    r.findObjectOrWait(By.text("Recent")).click();
    r.findObjectsOrWait(By.text("MORE")).get(0).click();
    r.findObjectOrWait(By.text("Quiz")).click();
    r.findObjectOrWait(By.text("START QUIZ")).click();
    r.findObjectOrWait(By.text("?\nShow answer")).click();
    r.findObjectOrWait(By.desc("More options")).click();
    r.findObjectOrWait(By.text("Paint")).click();
  }

}

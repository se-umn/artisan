package de.unipassau.tests;

import androidx.test.uiautomator.By;
import java.io.IOException;
import org.junit.Rule;
import org.junit.Test;

public class Poet117Test {

  private static final String TARGET_PACKAGE_NAME = "ca.rmen.android.poetassistant";

  @Rule
  public AbcRule r = new AbcRule(TARGET_PACKAGE_NAME);

  @Test
  public void crash() throws IOException, InterruptedException {
    r.findObjectOrWait(By.desc("More options")).click();
    r.findObjectOrWait(By.text("Settings")).click();
    r.findObjectOrWait(By.text("Theme")).click();
    r.findObjectOrWait(By.text("Dark")).click();
    r.findObjectOrWait(By.text("Theme")).click();
    r.findObjectOrWait(By.text("Light")).click();
    Runtime.getRuntime().exec("am start -a android.intent.action.VIEW -d poetassistant://thesaurus/hello");
  }
}

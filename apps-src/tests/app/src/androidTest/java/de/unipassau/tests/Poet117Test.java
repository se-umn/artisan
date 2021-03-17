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
  public void crash() throws IOException {
    r.findObjectOrWait(By.desc("More options")).click();
    r.findObjectOrWait(By.text("Settings")).click();
    r.findObjectOrWait(By.text("Theme")).click();
    r.findObjectOrWait(By.text("Light")).click();
    r.findObjectOrWait(By.text("Theme")).click();
    r.findObjectOrWait(By.text("Dark")).click();
    r.findObjectOrWait(By.text("Theme")).click();
    r.findObjectOrWait(By.text("Light")).click();
    String command = "am start -a android.intent.action.VIEW -d poetassistant://thesaurus/hello";
    InstrumentationRegistry.getInstrumentation().getUiAutomation().executeShellCommand(command).close();
  }

  @Test
  public void crash2() {
    String cmd = "am start -n ca.rmen.android.poetassistant/ca.rmen.android.poetassistant.main.MainActivity " +
            "-a android.intent.action.VIEW -c android.intent.category.DEFAULT -d poetassistant://rhymer/ --esn user_query";
    InstrumentationRegistry.getInstrumentation().getUiAutomation().executeShellCommand(cmd);
  }
}

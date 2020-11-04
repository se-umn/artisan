package de.unipassau.tests;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SdkSuppress;
import androidx.test.uiautomator.By;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@SdkSuppress(minSdkVersion = 18)
public class AnkiDroid27beta1Test {
  private static final String TARGET_PACKAGE_NAME = "com.ichi2.anki";

  @Rule
  public AbcRule rule = new AbcRule(TARGET_PACKAGE_NAME);

  //Only causes a crash if there is already cards in the default deck
  @Test
  public void ankiDroid() {
    rule.findObjectOrWait(By.desc("Navigate up")).click();
    rule.findObjectOrWait(By.text("Settings")).click();
    rule.findObjectOrWait(By.text("Gestures")).click();
    rule.findObjectOrWait(By.clazz("android.widget","CheckBox")).click();
    rule.findObjectOrWait(By.desc("Navigate up")).click();
    rule.findObjectOrWait(By.desc("Navigate up")).click();
    rule.findObjectOrWait(By.text("Settings")).click();
    rule.findObjectOrWait(By.text("Reviewing")).click();
    rule.findObjectOrWait(By.text("Fullscreen mode")).click();
    rule.findObjectOrWait(By.text("Hide the system bars and answer buttons")).click();
    rule.findObjectOrWait(By.desc("Navigate up")).click();
    // This assumes there is only one deck (default or custom named)
    String newCards = rule.findObjectOrWait(By.res(TARGET_PACKAGE_NAME, "deckpicker_new")).getText();
    String learnCards = rule.findObjectOrWait(By.res(TARGET_PACKAGE_NAME, "deckpicker_lrn")).getText();
    String revCards = rule.findObjectOrWait(By.res(TARGET_PACKAGE_NAME, "deckpicker_rev")).getText();

    // If the card deck is empty, add a new card
    if(newCards.equals("0") && learnCards.equals("0") && revCards.equals("0")){
      rule.findObjectOrWait(By.desc("Add")).click();
      rule.findObjectOrWait(By.res(TARGET_PACKAGE_NAME, "add_note_action")).click();
      rule.findObjectOrWait(By.desc("Front")).setText("front");
      rule.findObjectOrWait(By.desc("Back")).setText("back");
      rule.findObjectOrWait(By.desc("Save")).click();
      rule.findObjectOrWait(By.desc("Navigate up")).click();
    }

    rule.findObjectOrWait(By.res(TARGET_PACKAGE_NAME, "DeckPickerHoriz")).click();
  }
}

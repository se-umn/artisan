package de.unipassau.calculator;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import de.unipassau.calculator.fragments.SettingsFragment;

public class SettingsActivity extends AppCompatActivity {

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_settings);
    getSupportFragmentManager().beginTransaction()
        .replace(R.id.settings_container, new SettingsFragment())
        .commit();
  }
}

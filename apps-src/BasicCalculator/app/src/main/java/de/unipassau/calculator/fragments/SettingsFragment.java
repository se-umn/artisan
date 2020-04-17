package de.unipassau.calculator.fragments;

import android.os.Bundle;
import androidx.preference.PreferenceFragmentCompat;
import de.unipassau.calculator.R;

public class SettingsFragment extends PreferenceFragmentCompat {

  @Override
  public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
    setPreferencesFromResource(R.xml.preferences, rootKey);
  }
}

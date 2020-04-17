package de.unipassau.calculator;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import de.unipassau.calculator.fragments.CalculatorFragment;
import de.unipassau.calculator.fragments.HistoryFragment;
import de.unipassau.calculator.fragments.XmlClickable;

public class MainActivity extends AppCompatActivity {

  private XmlClickable clickableFragment;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    openFragment(new CalculatorFragment(), R.id.bottomContainer);
    openFragment(new HistoryFragment(), R.id.topContainer);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.toolbar_menu, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();

    if (id == R.id.action_settings) {
      Intent intent = new Intent(this, SettingsActivity.class);
      startActivity(intent);
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  public void openFragment(Fragment fragment, int id) {
    if (fragment instanceof XmlClickable) {
      clickableFragment = (XmlClickable) fragment;
    }

    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
    transaction.replace(id, fragment);
    transaction.addToBackStack(null);
    transaction.commit();
  }

  public void click(View view) {
    clickableFragment.click(view);
  }

}

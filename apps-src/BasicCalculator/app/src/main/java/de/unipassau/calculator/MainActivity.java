package de.unipassau.calculator;


import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.BottomNavigationView.OnNavigationItemSelectedListener;
import de.unipassau.calculator.fragments.CalculatorFragment;
import de.unipassau.calculator.fragments.HistoryFragment;
import de.unipassau.calculator.fragments.XmlClickable;
import de.unipassau.calculator.viewmodel.CalculationViewModel;

public class MainActivity extends AppCompatActivity {

  private XmlClickable clickableFragment;
  private BottomNavigationView bottomNavigation;
  private CalculationViewModel model;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    bottomNavigation = findViewById(R.id.bottom_navigation);
    bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

    openFragment(CalculatorFragment.newInstance());
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

  public void openFragment(Fragment fragment) {
    if (fragment instanceof XmlClickable) {
      clickableFragment = (XmlClickable) fragment;
    }

    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
    transaction.replace(R.id.container, fragment);
    transaction.addToBackStack(null);
    transaction.commit();
  }

  public void click(View view) {
    clickableFragment.click(view);
  }

  OnNavigationItemSelectedListener navigationItemSelectedListener = menuItem -> {
    switch (menuItem.getItemId()) {
      case R.id.navigation_calculator:
        openFragment(CalculatorFragment.newInstance());
        return true;
      case R.id.navigation_history:
        openFragment(HistoryFragment.newInstance());
        return true;
      default:
        return false;
    }
  };
}

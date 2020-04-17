package de.unipassau.calculator;

import android.app.Application;
import android.content.Context;
import androidx.multidex.MultiDex;

public class App extends Application {

  public static Context appContext;

  public static final int ERRONEOUS_INPUT = 13;

  @Override
  public void onCreate() {
    super.onCreate();

    appContext = getApplicationContext();
  }

  @Override
  protected void attachBaseContext(Context base) {
    super.attachBaseContext(base);
    MultiDex.install(this);
  }
}

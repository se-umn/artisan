/*   This file is part of My Expenses.
 *   My Expenses is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   My Expenses is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with My Expenses.  If not, see <http://www.gnu.org/licenses/>.
*/

package org.totschnig.myexpenses.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.KeyguardManager;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.annimon.stream.Optional;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.totschnig.myexpenses.MyApplication;
import org.totschnig.myexpenses.R;
import org.totschnig.myexpenses.dialog.ConfirmationDialogFragment;
import org.totschnig.myexpenses.dialog.DialogUtils;
import org.totschnig.myexpenses.dialog.HelpDialogFragment;
import org.totschnig.myexpenses.dialog.MessageDialogFragment;
import org.totschnig.myexpenses.dialog.MessageDialogFragment.MessageDialogListener;
import org.totschnig.myexpenses.dialog.ProgressDialogFragment;
import org.totschnig.myexpenses.feature.FeatureManager;
import org.totschnig.myexpenses.fragment.DbWriteFragment;
import org.totschnig.myexpenses.model.Account;
import org.totschnig.myexpenses.model.AggregateAccount;
import org.totschnig.myexpenses.model.ContribFeature;
import org.totschnig.myexpenses.model.CurrencyContext;
import org.totschnig.myexpenses.model.Model;
import org.totschnig.myexpenses.model.Transaction;
import org.totschnig.myexpenses.preference.PrefHandler;
import org.totschnig.myexpenses.preference.PrefKey;
import org.totschnig.myexpenses.provider.TransactionProvider;
import org.totschnig.myexpenses.service.DailyScheduler;
import org.totschnig.myexpenses.task.RestoreTask;
import org.totschnig.myexpenses.task.TaskExecutionFragment;
import org.totschnig.myexpenses.ui.AmountInput;
import org.totschnig.myexpenses.ui.SnackbarAction;
import org.totschnig.myexpenses.util.CurrencyFormatter;
import org.totschnig.myexpenses.util.DistributionHelper;
import org.totschnig.myexpenses.util.PermissionHelper;
import org.totschnig.myexpenses.util.PermissionHelper.PermissionGroup;
import org.totschnig.myexpenses.util.Result;
import org.totschnig.myexpenses.util.UiUtils;
import org.totschnig.myexpenses.util.Utils;
import org.totschnig.myexpenses.util.ads.AdHandlerFactory;
import org.totschnig.myexpenses.util.crashreporting.CrashHandler;
import org.totschnig.myexpenses.util.licence.LicenceHandler;
import org.totschnig.myexpenses.util.licence.LicenceStatus;
import org.totschnig.myexpenses.util.tracking.Tracker;
import org.totschnig.myexpenses.widget.AbstractWidgetKt;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Locale;

import javax.inject.Inject;

import androidx.annotation.CallSuper;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NavUtils;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import icepick.Icepick;
import timber.log.Timber;

import static org.totschnig.myexpenses.activity.ConstantsKt.CALCULATOR_REQUEST;
import static org.totschnig.myexpenses.activity.ConstantsKt.CONFIRM_DEVICE_CREDENTIALS_UNLOCK_REQUEST;
import static org.totschnig.myexpenses.activity.ConstantsKt.CONTRIB_REQUEST;
import static org.totschnig.myexpenses.activity.ConstantsKt.PREFERENCES_REQUEST;
import static org.totschnig.myexpenses.activity.ConstantsKt.RESTORE_REQUEST;
import static org.totschnig.myexpenses.activity.ContribInfoDialogActivity.KEY_FEATURE;
import static org.totschnig.myexpenses.preference.PrefKey.CRITERION_FUTURE;
import static org.totschnig.myexpenses.preference.PrefKey.CUSTOM_DATE_FORMAT;
import static org.totschnig.myexpenses.preference.PrefKey.GROUP_MONTH_STARTS;
import static org.totschnig.myexpenses.preference.PrefKey.GROUP_WEEK_STARTS;
import static org.totschnig.myexpenses.preference.PrefKey.HOME_CURRENCY;
import static org.totschnig.myexpenses.preference.PrefKey.PROTECTION_DEVICE_LOCK_SCREEN;
import static org.totschnig.myexpenses.preference.PrefKey.PROTECTION_LEGACY;
import static org.totschnig.myexpenses.preference.PrefKey.UI_FONTSIZE;
import static org.totschnig.myexpenses.preference.PrefKey.UI_LANGUAGE;
import static org.totschnig.myexpenses.provider.DatabaseConstants.KEY_AMOUNT;
import static org.totschnig.myexpenses.task.TaskExecutionFragment.TASK_RESTORE;
import static org.totschnig.myexpenses.util.DistributionHelper.getMarketSelfUri;
import static org.totschnig.myexpenses.util.DistributionHelper.getVersionInfo;
import static org.totschnig.myexpenses.util.TextUtils.concatResStrings;

public abstract class ProtectedFragmentActivity extends AppCompatActivity
    implements MessageDialogListener, OnSharedPreferenceChangeListener,
    ConfirmationDialogFragment.ConfirmationDialogListener,
    TaskExecutionFragment.TaskCallbacks, DbWriteFragment.TaskCallbacks,
    ProgressDialogFragment.ProgressDialogListener, AmountInput.Host {

  public static final String SAVE_TAG = "SAVE_TASK";
  public static final int RESULT_RESTORE_OK = RESULT_FIRST_USER + 1;
  public static final String EDIT_COLOR_DIALOG = "editColorDialog";

  public static final String ASYNC_TAG = "ASYNC_TASK";
  public static final String PROGRESS_TAG = "PROGRESS";

  private AlertDialog pwDialog;
  private boolean scheduledRestart = false;
  private Optional<Boolean> confirmCredentialResult = Optional.empty();
  private Enum<?> helpVariant = null;
  protected ColorStateList textColorSecondary;
  protected FloatingActionButton floatingActionButton;

  private Snackbar snackbar;

  @Inject
  protected Tracker tracker;

  @Inject
  protected CrashHandler crashHandler;

  @Inject
  protected AdHandlerFactory adHandlerFactory;

  @Inject
  protected LicenceHandler licenceHandler;

  @Inject
  protected CurrencyContext currencyContext;

  @Inject
  protected CurrencyFormatter currencyFormatter;

  @Inject
  protected FeatureManager featureManager;

  @Inject
  protected PrefHandler prefHandler;

  private Pair<Integer, Integer> focusAfterRestoreInstanceState;

  public ColorStateList getTextColorSecondary() {
    return textColorSecondary;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Icepick.restoreInstanceState(this, savedInstanceState);
    if (MyApplication.getInstance().isProtected()) {
      getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
          WindowManager.LayoutParams.FLAG_SECURE);
    }
    MyApplication.getInstance().getSettings().registerOnSharedPreferenceChangeListener(this);
    TypedArray themeArray = getTheme().obtainStyledAttributes(new int[]{android.R.attr.textColorSecondary});
    textColorSecondary = themeArray.getColorStateList(0);
    tracker.init(this);
  }

  protected void onSaveInstanceState(@NonNull Bundle outState) {
    super.onSaveInstanceState(outState);
    Icepick.saveInstanceState(this, outState);
  }

  @SuppressLint("NewApi")
  @Override
  protected void attachBaseContext(Context newBase) {
    final boolean legacy = Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1;
    super.attachBaseContext(legacy ? legacyConfig(newBase) : newBase);
    injectDependencies();
    if (!legacy) {
      applyOverrideConfiguration(newBase.getResources().getConfiguration());
    }
    featureManager.initActivity(this);
  }

  private Context legacyConfig(Context context) {
    Resources res = context.getResources();
    Configuration config = new Configuration(res.getConfiguration());
    config.locale = getUserPreferredLocale();
    //noinspection deprecation
    final MyApplication application = MyApplication.getInstance();
    config.fontScale = getFontScale(application.getAppComponent().prefHandler(), application.getContentResolver());
    res.updateConfiguration(config, res.getDisplayMetrics());
    return context;
  }

  @Override
  @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
  public void applyOverrideConfiguration(Configuration newConfig) {
    super.applyOverrideConfiguration(updateConfiguration(newConfig));
  }

  @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
  private Configuration updateConfiguration(Configuration config) {
    Locale locale = getUserPreferredLocale();
    config.setLocale(locale);
    config.fontScale = getFontScale(prefHandler, getContentResolver());
    Timber.d("Fontscale: %f", config.fontScale);
    return config;
  }

  private float getFontScale(PrefHandler prefHandler, ContentResolver contentResolver) {
    final int customFontScale = prefHandler.getInt(UI_FONTSIZE, 0);
    return Settings.System.getFloat(contentResolver, Settings.System.FONT_SCALE, 1.0f) * (1 + customFontScale / 10F);
  }

  private Locale getUserPreferredLocale() {
    //noinspection deprecation
    return MyApplication.getInstance().getAppComponent().userLocaleProvider().getUserPreferredLocale();
  }

  protected void injectDependencies() {
    ((MyApplication) getApplicationContext()).getAppComponent().inject(this);
  }

  protected void configureFloatingActionButton(int fabDescription) {
    configureFloatingActionButton(fabDescription, 0);
  }

  protected void configureFloatingActionButton(int fabDescription, int icon) {
    if (!requireFloatingActionButtonWithContentDescription(getString(fabDescription))) return;
    if (icon != 0) {
      floatingActionButton.setImageResource(icon);
    }
    UiUtils.setBackgroundTintListOnFab(floatingActionButton, UiUtils.themeIntAttr(this, R.attr.colorControlActivated));
  }

  protected boolean requireFloatingActionButtonWithContentDescription(String fabDescription) {
    floatingActionButton = findViewById(R.id.CREATE_COMMAND);
    if (floatingActionButton == null) return false;
    floatingActionButton.setContentDescription(fabDescription);
    return true;
  }

  protected Toolbar setupToolbar(boolean withHome) {
    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    if (withHome) {
      final ActionBar actionBar = getSupportActionBar();
      actionBar.setDisplayHomeAsUpEnabled(true);
    }
    return toolbar;
  }

  @Override
  protected void onPause() {
    super.onPause();
    MyApplication app = MyApplication.getInstance();
    if (app.isLocked() && pwDialog != null) {
      pwDialog.dismiss();
    } else {
      app.setLastPause(this);
    }
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    MyApplication.getInstance().getSettings().unregisterOnSharedPreferenceChangeListener(this);
  }

  @Override
  protected void onResume() {
    super.onResume();
    crashHandler.addBreadcrumb(getClass().getSimpleName());
    if (scheduledRestart) {
      scheduledRestart = false;
      recreate();
    } else {
      if (confirmCredentialResult.isPresent()) {
        if (!confirmCredentialResult.get()) {
          moveTaskToBack(true);
        }
        confirmCredentialResult = Optional.empty();
      } else {
        MyApplication app = MyApplication.getInstance();
        if (app.shouldLock(this)) {
          confirmCredentials(CONFIRM_DEVICE_CREDENTIALS_UNLOCK_REQUEST, null, true);
        }
      }
    }
  }

  protected void confirmCredentials(int requestCode, DialogUtils.PasswordDialogUnlockedCallback legacyUnlockCallback, boolean shouldHideWindow) {
    if (Utils.hasApiLevel(Build.VERSION_CODES.LOLLIPOP) && PROTECTION_DEVICE_LOCK_SCREEN.getBoolean(false)) {
      Intent intent = ((KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE))
          .createConfirmDeviceCredentialIntent(null, null);
      if (intent != null) {
        if (shouldHideWindow) hideWindow();
        try {
          startActivityForResult(intent, requestCode);
        } catch (ActivityNotFoundException e) {
          showSnackbar("No activity found for confirming device credentials", Snackbar.LENGTH_LONG);
        }
      } else {
        showDeviceLockScreenWarning();
        if (legacyUnlockCallback != null) {
          legacyUnlockCallback.onPasswordDialogUnlocked();
        }
      }
    } else if (PROTECTION_LEGACY.getBoolean(true)) {
      if (shouldHideWindow) hideWindow();
      if (pwDialog == null) {
        pwDialog = DialogUtils.passwordDialog(this, false);
      }
      DialogUtils.showPasswordDialog(this, pwDialog, legacyUnlockCallback);
    }
  }

  public void showDeviceLockScreenWarning() {
    showSnackbar(
        concatResStrings(this, " ", R.string.warning_device_lock_screen_not_set_up_1, R.string.warning_device_lock_screen_not_set_up_2),
        Snackbar.LENGTH_LONG);
  }

  @Override
  public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                                        String key) {
    if (prefHandler.matches(key, UI_LANGUAGE, UI_FONTSIZE, PROTECTION_LEGACY,
        PROTECTION_DEVICE_LOCK_SCREEN, GROUP_MONTH_STARTS, GROUP_WEEK_STARTS, HOME_CURRENCY, CUSTOM_DATE_FORMAT, CRITERION_FUTURE)) {
      scheduledRestart = true;
    }
  }

  @Override
  public void onMessageDialogDismissOrCancel() {
  }

  @Override
  public void onProgressDialogDismiss() {
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.help, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int itemId = item.getItemId();
    if (itemId != 0) {
      if (dispatchCommand(itemId, null)) {
        return true;
      }
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  @CallSuper
  public boolean dispatchCommand(int command, @Nullable Object tag) {
    Bundle bundle = new Bundle();
    String fullResourceName = getResources().getResourceName(command);
    bundle.putString(Tracker.EVENT_PARAM_ITEM_ID, fullResourceName.substring(fullResourceName.indexOf('/') + 1));
    logEvent(Tracker.EVENT_DISPATCH_COMMAND, bundle);
    Intent i;
    switch(command) {
      case R.id.RATE_COMMAND:
        i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(getMarketSelfUri()));
        if (Utils.isIntentAvailable(this, i)) {
          startActivity(i);
        } else {
          showSnackbar(R.string.error_accessing_market, Snackbar.LENGTH_LONG);
        }
        return true;
      case R.id.SETTINGS_COMMAND:
        i = new Intent(this, MyPreferenceActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if (tag != null) {
          i.putExtra(MyPreferenceActivity.KEY_OPEN_PREF_KEY, (String) tag);
        }
        startActivityForResult(i, PREFERENCES_REQUEST);
        return true;
      case R.id.FEEDBACK_COMMAND: {
        LicenceStatus licenceStatus = licenceHandler.getLicenceStatus();
        String licenceInfo = "";
        if (licenceStatus != null) {
          licenceInfo = "\nLICENCE: " + licenceStatus.name();
          String purchaseExtraInfo = licenceHandler.getPurchaseExtraInfo();
          if (!TextUtils.isEmpty(purchaseExtraInfo)) {
            licenceInfo += " (" + purchaseExtraInfo + ")";
          }
        }
        i = new Intent(android.content.Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{MyApplication.FEEDBACK_EMAIL});
        i.putExtra(android.content.Intent.EXTRA_SUBJECT,
            "[" + getString(R.string.app_name) + "] Feedback"
        );
        String messageBody = String.format(Locale.ROOT,
            "APP_VERSION:%s\nFIRST_INSTALL_VERSION:%d (DB_SCHEMA %d)\nANDROID_VERSION:%s\nBRAND:%s\nMODEL:%s\nCONFIGURATION:%s%s\n\n",
            getVersionInfo(this),
            prefHandler.getInt(PrefKey.FIRST_INSTALL_VERSION, 0),
            prefHandler.getInt(PrefKey.FIRST_INSTALL_DB_SCHEMA_VERSION, -1),
            Build.VERSION.RELEASE,
            Build.BRAND,
            Build.MODEL,
            ConfigurationHelper.configToJson(getResources().getConfiguration()),
            licenceInfo);
        Timber.d("Install info: %s", messageBody);
        i.putExtra(android.content.Intent.EXTRA_TEXT, messageBody);
        if (!Utils.isIntentAvailable(this, i)) {
          showSnackbar(R.string.no_app_handling_email_available, Snackbar.LENGTH_LONG);
        } else {
          startActivity(i);
        }
        break;
      }
      case R.id.CONTRIB_INFO_COMMAND:
        showContribDialog(null, null);
        return true;
      case R.id.WEB_COMMAND:
        i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(getString(R.string.website)));
        startActivity(i);
        return true;
      case R.id.HELP_COMMAND:
        doHelp((String) tag);
        return true;
      case R.id.REQUEST_LICENCE_MIGRATION_COMMAND:
        String androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        i = new Intent(android.content.Intent.ACTION_SEND);
        i.setType("plain/text");
        i.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{MyApplication.FEEDBACK_EMAIL});
        i.putExtra(android.content.Intent.EXTRA_SUBJECT,
            "[" + getString(R.string.app_name) + "] " +  getString(licenceHandler.getLicenceStatus().getResId()));
        String extraText = String.format(
            "Please send me a new licence key. Current key is %1$s for Android-Id %2$s\nLANGUAGE:%3$s\nVERSION:%4$s",
            PrefKey.LICENCE_LEGACY.getString(null), androidId,
            Locale.getDefault().toString(), DistributionHelper.getVersionInfo(this));
        i.putExtra(android.content.Intent.EXTRA_TEXT, extraText);
        if (!Utils.isIntentAvailable(this, i)) {
          showSnackbar(R.string.no_app_handling_email_available, Snackbar.LENGTH_LONG);
        } else {
          startActivity(i);
        }
        return true;
      case android.R.id.home:
        doHome();
        return true;
      case R.id.GDPR_CONSENT_COMMAND: {
        adHandlerFactory.setConsent(this, (Boolean) tag);
        return true;
      }
      case R.id.GDPR_NO_CONSENT_COMMAND: {
        adHandlerFactory.clearConsent();
        contribFeatureRequested(ContribFeature.AD_FREE, null);
        return true;
      }
    }
    return false;
  }

  public void showContribDialog(@Nullable ContribFeature feature, @Nullable Serializable tag) {
    Intent i = ContribInfoDialogActivity.getIntentFor(this, feature);
    i.putExtra(ContribInfoDialogActivity.KEY_TAG, tag);
    startActivityForResult(i, CONTRIB_REQUEST);
  }

  protected void doHelp(String variant) {
    Intent i;
    i = new Intent(this, Help.class);
    i.putExtra(HelpDialogFragment.KEY_VARIANT,
        variant != null ? variant : getHelpVariant());
    //for result is needed since it allows us to inspect the calling activity
    startActivityForResult(i, 0);
  }

  protected void doHome() {
    setResult(FragmentActivity.RESULT_CANCELED);
    finish();
  }

  public void dispatchCommand(View v) {
    dispatchCommand(v.getId(), v.getTag());
  }

  @Override
  public void onPreExecute() {
  }

  @Override
  public void onProgressUpdate(Object progress) {
    FragmentManager m = getSupportFragmentManager();
    ProgressDialogFragment f = ((ProgressDialogFragment) m.findFragmentByTag(PROGRESS_TAG));
    if (f != null) {
      if (progress instanceof Integer) {
        f.setProgress((Integer) progress);
      } else if (progress instanceof String) {
        f.appendToMessage((String) progress);
      } else if (progress instanceof Result) {
        String print = ((Result) progress).print0(this);
        if (print != null) {
          f.appendToMessage(print);
        }
      }
    }
  }

  @Override
  public void onCancelled() {
    removeAsyncTaskFragment(false);
  }

  protected boolean shouldKeepProgress(int taskId) {
    return false;
  }

  @Override
  public void onPostExecute(int taskId, @Nullable Object o) {
    removeAsyncTaskFragment(shouldKeepProgress(taskId));
    switch (taskId) {
      case TaskExecutionFragment.TASK_DELETE_TRANSACTION:
      case TaskExecutionFragment.TASK_DELETE_ACCOUNT:
      case TaskExecutionFragment.TASK_DELETE_PAYMENT_METHODS:
      case TaskExecutionFragment.TASK_DELETE_CATEGORY:
      case TaskExecutionFragment.TASK_DELETE_PAYEES:
      case TaskExecutionFragment.TASK_DELETE_TEMPLATES:
      case TaskExecutionFragment.TASK_UNDELETE_TRANSACTION: {
        Result result = (Result) o;
        if (!result.isSuccess()) {
          showSnackbar("There was an error deleting the object. Please contact support@myexenses.mobi !",
              Snackbar.LENGTH_LONG);
        }
        break;
      }
      case TASK_RESTORE: {
        onPostRestoreTask(((Result) o));
        break;
      }
    }
  }

  protected void onPostRestoreTask(Result result) {
    if (result.isSuccess()) {
      MyApplication.getInstance().getLicenceHandler().reset();
      // if the backup is password protected, we want to force the password
      // check
      // is it not enough to set mLastPause to zero, since it would be
      // overwritten by the callings activity onpause
      // hence we need to set isLocked if necessary
      MyApplication.getInstance().resetLastPause();
      MyApplication.getInstance().shouldLock(this);
    }
  }

  @Override
  public Model getObject() {
    return null;
  }

  @Override
  public void onPostExecute(Uri result) {
    FragmentManager m = getSupportFragmentManager();
    FragmentTransaction t = m.beginTransaction();
    t.remove(m.findFragmentByTag(SAVE_TAG));
    t.remove(m.findFragmentByTag(PROGRESS_TAG));
    t.commitAllowingStateLoss();
  }

  /**
   * starts the given task, only if no task is currently executed,
   * informs user through snackbar in that case
   *
   * @param taskId
   * @param objectIds
   * @param extra
   * @param progressMessage if 0 no progress dialog will be shown
   */
  public <T> void startTaskExecution(int taskId, T[] objectIds, Serializable extra,
                                     int progressMessage) {
    startTaskExecution(taskId, objectIds, extra, progressMessage, false);
  }
  public <T> void startTaskExecution(int taskId, T[] objectIds, Serializable extra,
                                     int progressMessage, boolean withButton) {
    FragmentManager m = getSupportFragmentManager();
    if (hasPendingTask(true)) {
      return;
    }
    if (m.isStateSaved()) {
      return;
    }
    //noinspection AndroidLintCommitTransaction
    FragmentTransaction ft = m.beginTransaction()
        .add(TaskExecutionFragment.newInstance(
            taskId,
            objectIds, extra),
            ASYNC_TAG);
    if (progressMessage != 0) {
      ft.add(ProgressDialogFragment.newInstance(getString(progressMessage), withButton), PROGRESS_TAG);
    }
    ft.commit();
  }

  public boolean hasPendingTask(boolean shouldWarn) {
    FragmentManager m = getSupportFragmentManager();
    final boolean result = m.findFragmentByTag(ASYNC_TAG) != null;
    if (result && shouldWarn) {
      showSnackbar("Previous task still executing, please try again later",
          Snackbar.LENGTH_LONG);
    }
    return result;
  }

  public void startTaskExecution(int taskId, @NonNull Bundle extras, int progressMessage) {
    FragmentManager m = getSupportFragmentManager();
    if (hasPendingTask(true)) {
      return;
    }
    //noinspection AndroidLintCommitTransaction
    FragmentTransaction ft = m.beginTransaction()
        .add(TaskExecutionFragment.newInstanceWithBundle(extras, taskId),
            ASYNC_TAG);
    if (progressMessage != 0) {
      ft.add(ProgressDialogFragment.newInstance(getString(progressMessage)), PROGRESS_TAG);
    }
    ft.commit();
  }

  private void removeAsyncTaskFragment(boolean keepProgress) {
    FragmentManager m = getSupportFragmentManager();
    FragmentTransaction t = m.beginTransaction();
    ProgressDialogFragment f = ((ProgressDialogFragment) m.findFragmentByTag(PROGRESS_TAG));
    if (f != null) {
      if (keepProgress) {
        f.onTaskCompleted();
      } else {
        t.remove(f);
      }
    }
    final Fragment asyncFragment = m.findFragmentByTag(ASYNC_TAG);
    if (asyncFragment != null) {
      t.remove(asyncFragment);
    }
    t.commitAllowingStateLoss();
    //we might want to call a new task immediately after executing the last one
    m.executePendingTransactions();
  }

  public void startDbWriteTask() {
    getSupportFragmentManager().beginTransaction()
        .add(DbWriteFragment.newInstance(), SAVE_TAG)
        .add(ProgressDialogFragment.newInstance(getString(R.string.progress_dialog_saving)),
            PROGRESS_TAG)
        .commitAllowingStateLoss();
  }

  public void recordUsage(ContribFeature f) {
    f.recordUsage(prefHandler);
  }

  /**
   * Workaround for broken {@link NavUtils#shouldUpRecreateTask(android.app.Activity, Intent)}
   *
   * @param from
   * @return
   * @see <a href="http://stackoverflow.com/a/20643984/1199911">http://stackoverflow.com/a/20643984/1199911</a>
   */
  protected final boolean shouldUpRecreateTask(Activity from) {
    return from.getIntent().getBooleanExtra(AbstractWidgetKt.EXTRA_START_FROM_WIDGET, false);
  }

  /*
   * @see android.support.v7.app.ActionBarActivity#onBackPressed()
   * https://code.google.com/p/android/issues/detail?id=25517
   */
  @Override
  public void onBackPressed() {
    try {
      super.onBackPressed();
    } catch (IllegalStateException e) {
      CrashHandler.report(e);
      finish();
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode,
                                  Intent intent) {
    super.onActivityResult(requestCode, resultCode, intent);
    if (requestCode == CONTRIB_REQUEST && intent != null) {
      ContribFeature contribFeature = ContribFeature.valueOf(intent.getStringExtra(KEY_FEATURE));
      if (resultCode == RESULT_OK) {
        ((ContribIFace) this).contribFeatureCalled(contribFeature,
            intent.getSerializableExtra(ContribInfoDialogActivity.KEY_TAG));
      } else if (resultCode == RESULT_CANCELED) {
        ((ContribIFace) this).contribFeatureNotCalled(contribFeature);
      }
    }
    if ((requestCode == PREFERENCES_REQUEST || requestCode == RESTORE_REQUEST) && resultCode == RESULT_RESTORE_OK) {
      restartAfterRestore();
    }
    if (requestCode == CONFIRM_DEVICE_CREDENTIALS_UNLOCK_REQUEST) {
      if (resultCode == RESULT_OK) {
        confirmCredentialResult = Optional.of(true);
        showWindow();
        MyApplication.getInstance().setLocked(false);
      } else {
        confirmCredentialResult = Optional.of(false);
      }
    }
    if (resultCode == RESULT_OK && requestCode == CALCULATOR_REQUEST && intent != null) {
      View target = findViewById(intent.getIntExtra(CalculatorInput.EXTRA_KEY_INPUT_ID, 0));
      if (target instanceof AmountInput) {
        ((AmountInput) target).setAmount(new BigDecimal(intent.getStringExtra(KEY_AMOUNT)), false);
      } else {
        showSnackbar("CALCULATOR_REQUEST launched with incorrect EXTRA_KEY_INPUT_ID", Snackbar.LENGTH_LONG);
      }
    }
  }

  protected void restartAfterRestore() {
    invalidateHomeCurrency();
    if (!isFinishing()) {
      Intent i = new Intent(this, MyExpenses.class);
      i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
      startActivity(i);
      finish();
    }
  }

  public void contribFeatureRequested(@NonNull ContribFeature feature, @Nullable Serializable tag) {
    if (feature.hasAccess()) {
      ((ContribIFace) this).contribFeatureCalled(feature, tag);
    } else {
      showContribDialog(feature, tag);
    }
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    boolean granted = PermissionHelper.allGranted(grantResults);
    storePermissionRequested(requestCode);
    if (granted) {
      switch (requestCode) {
        case PermissionHelper.PERMISSIONS_REQUEST_WRITE_CALENDAR: {
          DailyScheduler.updatePlannerAlarms(this, false, true);
        }
      }
    } else {
      if (permissions.length > 0 && ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0])) {
        showSnackbar(PermissionHelper.permissionRequestRationale(this, requestCode),
            Snackbar.LENGTH_LONG);
      }
    }
  }

  private void storePermissionRequested(int requestCode) {
    PermissionHelper.permissionRequestedKey(requestCode).putBoolean(true);
  }

  public boolean isCalendarPermissionPermanentlyDeclined() {
    return isPermissionPermanentlyDeclined(PermissionHelper.PermissionGroup.CALENDAR);
  }

  private boolean isPermissionPermanentlyDeclined(PermissionGroup permissionGroup) {
    if (prefHandler.getBoolean(permissionGroup.prefKey,false)) {
      if (!permissionGroup.hasPermission(this)) {
        if (!permissionGroup.shouldShowRequestPermissionRationale(this)) {
          return true;
        }
      }
    }
    return false;
  }

  public void requestCalendarPermission() {
    requestPermissionOrStartApplicationDetailSettings(PermissionGroup.CALENDAR);
  }

  public void requestStoragePermission() {
    requestPermissionOrStartApplicationDetailSettings(PermissionGroup.STORAGE);
  }

  private void requestPermissionOrStartApplicationDetailSettings(PermissionGroup permissionGroup) {
    if (isPermissionPermanentlyDeclined(permissionGroup)) {
      //noinspection InlinedApi
      Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
      Uri uri = Uri.fromParts("package", getPackageName(), null);
      intent.setData(uri);
      startActivity(intent);
    } else {
      requestPermission(permissionGroup);
    }
  }

  public void requestPermission(PermissionGroup permissionGroup) {
    ActivityCompat.requestPermissions(this, permissionGroup.androidPermissions,
        permissionGroup.requestCode);
  }

  @Override
  public void onPositive(Bundle args) {
    dispatchCommand(args.getInt(ConfirmationDialogFragment.KEY_COMMAND_POSITIVE), null);
  }

  protected void doRestore(Bundle args) {
    if (!args.containsKey(RestoreTask.KEY_PASSWORD)) {
      String password = prefHandler.getString(PrefKey.EXPORT_PASSWORD, null);
      if (!TextUtils.isEmpty(password)) {
        args.putString(RestoreTask.KEY_PASSWORD, password);
      }
    }
    getSupportFragmentManager()
        .beginTransaction()
        .add(TaskExecutionFragment.newInstanceWithBundle(args, TASK_RESTORE), ASYNC_TAG)
        .add(ProgressDialogFragment.newInstance(getString(R.string.pref_restore_title), true), PROGRESS_TAG).commit();
  }

  @Override
  public void onNegative(Bundle args) {
  }

  @Override
  public void onDismissOrCancel(Bundle args) {

  }

  public void setTrackingEnabled(boolean enabled) {
    tracker.setEnabled(enabled);
  }

  public void logEvent(String event, Bundle params) {
    tracker.logEvent(event, params);
  }

  @VisibleForTesting
  public Fragment getCurrentFragment() {
    return null;
  }

  public void hideWindow() {
    findViewById(android.R.id.content).setVisibility(View.GONE);
    final ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) actionBar.hide();
  }

  public void showWindow() {
    findViewById(android.R.id.content).setVisibility(View.VISIBLE);
    final ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) actionBar.show();
  }

  public void showDismissableSnackbar(int message) {
    showDismissableSnackbar(getText(message));
  }

  public void showDismissableSnackbar(CharSequence message) {
    showSnackbar(message, Snackbar.LENGTH_INDEFINITE,
        new SnackbarAction(R.string.snackbar_dismiss, v -> snackbar.dismiss()));
  }

  public void showSnackbar(int message, int duration) {
    showSnackbar(getText(message), duration);
  }

  public void showSnackbar(@NonNull CharSequence message, int duration) {
    showSnackbar(message, duration, null);
  }

  public void showSnackbar(@NonNull CharSequence message, int duration, SnackbarAction snackbarAction) {
    showSnackbar(message, duration, snackbarAction, null);
  }

  public void showSnackbar(@NonNull CharSequence message, int duration, SnackbarAction snackbarAction,
                           Snackbar.Callback callback) {
    View container = findViewById(getSnackbarContainerId());
    if (container == null) {
      CrashHandler.report(String.format("Class %s is unable to display snackbar", getClass()));
      Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    } else {
      showSnackbar(message, duration, snackbarAction, callback, container);
    }
  }

  protected void showSnackbar(@NonNull CharSequence message, int duration, SnackbarAction snackbarAction,
                            Snackbar.Callback callback, @NonNull View container) {
    snackbar = Snackbar.make(container, message, duration);
    UiUtils.increaseSnackbarMaxLines(snackbar);
    if (snackbarAction != null) {
      snackbar.setAction(snackbarAction.resId, snackbarAction.listener);
    }
    if (callback != null) {
      snackbar.addCallback(callback);
    }
    snackbar.show();
  }

  public void dismissSnackbar() {
    if (snackbar != null) {
      snackbar.dismiss();
    }
  }

  @IdRes
  protected int getSnackbarContainerId() {
    return R.id.fragment_container;
  }

  public void showMessage(int resId) {
    showMessage(getString(resId));
  }

  public void showMessage(CharSequence message) {
    MessageDialogFragment.newInstance(
        null,
        message,
        MessageDialogFragment.Button.okButton(),
        null, null)
        .show(getSupportFragmentManager(), "MESSAGE");
  }

  public void checkGdprConsent(boolean forceShow) {
    adHandlerFactory.gdprConsent(this, forceShow);
  }

  public String getHelpVariant() {
    return helpVariant != null ? helpVariant.name() : null;
  }

  protected void setHelpVariant(@Nullable Enum<?> helpVariant) {
    this.helpVariant = helpVariant;
    if (helpVariant != null) {
      crashHandler.addBreadcrumb(helpVariant.toString());
    }
  }

  public void invalidateHomeCurrency() {
    currencyContext.invalidateHomeCurrency();
    currencyFormatter.invalidate(AggregateAccount.AGGREGATE_HOME_CURRENCY_CODE, getContentResolver());
    Transaction.buildProjection(this);
    Account.buildProjection();
    getContentResolver().notifyChange(TransactionProvider.TRANSACTIONS_URI, null, false);
  }

  public void showCalculator(BigDecimal amount, int id) {
    Intent intent = new Intent(this, CalculatorInput.class);
    forwardDataEntryFromWidget(intent);
    if (amount != null) {
      intent.putExtra(KEY_AMOUNT, amount);
    }
    intent.putExtra(CalculatorInput.EXTRA_KEY_INPUT_ID, id);
    startActivityForResult(intent, CALCULATOR_REQUEST);
  }

  protected void forwardDataEntryFromWidget(Intent intent) {
    intent.putExtra(AbstractWidgetKt.EXTRA_START_FROM_WIDGET_DATA_ENTRY,
        getIntent().getBooleanExtra(AbstractWidgetKt.EXTRA_START_FROM_WIDGET_DATA_ENTRY, false));
  }

  @Override
  public void setFocusAfterRestoreInstanceState(Pair<Integer, Integer> focusView) {
    this.focusAfterRestoreInstanceState = focusView;
  }

  @Override
  protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
    super.onRestoreInstanceState(savedInstanceState);
    if (focusAfterRestoreInstanceState != null) {
      findViewById(focusAfterRestoreInstanceState.first).findViewById(focusAfterRestoreInstanceState.second).requestFocus();
    }
  }

  public enum ThemeType {
    dark, light
  }
}

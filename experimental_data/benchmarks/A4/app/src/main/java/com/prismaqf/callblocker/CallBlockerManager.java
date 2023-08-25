package com.prismaqf.callblocker;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Locale;

public class CallBlockerManager extends AppCompatActivity {

    private static final String TAG = CallBlockerManager.class.getCanonicalName();

    private TextView textDetectState;
    private CallEventReceiver callEventReceiver;
    private Button buttonReceived;
    private Button buttonTriggered;
    private boolean isBound;
    private Dialog aboutDlg;

    private final ServiceConnection myConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            CallDetectService.LocalBinder binder = (CallDetectService.LocalBinder) service;
            CallDetectService myService = binder.getService();
            isBound = true;
            buttonReceived.setText(String.valueOf(myService.getNumReceived()));
            buttonReceived.invalidate();
            buttonTriggered.setText(String.valueOf(myService.getNumTriggered()));
            buttonTriggered.invalidate();
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            isBound = false;
        }
    };

    /**
     * Broadcast receiver to receive intents when a call is detected
     */
    private class CallEventReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String number = intent.getStringExtra(context.getString(R.string.ky_number_called));
            int numReceived = intent.getIntExtra(context.getString(R.string.ky_received), 0);
            int numTriggered = intent.getIntExtra(context.getString(R.string.ky_triggered), 0);
            String message = String.format(Locale.getDefault(),
                                           "Incoming: %s, Num received: %d, Num triggered: %d",
                                           number, numReceived, numTriggered);
            Log.i(TAG,message);

            if (buttonReceived == null)
                buttonReceived = (Button) findViewById(R.id.button_received);
            if (buttonTriggered == null)
                buttonTriggered = (Button) findViewById(R.id.button_triggered);

            buttonReceived.setText(String.valueOf(numReceived));
            buttonReceived.invalidate();
            buttonTriggered.setText(String.valueOf(numTriggered));
            buttonTriggered.invalidate();
        }
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("mychannel", "channelname", importance);
            channel.setDescription("Description");
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.call_blocker_manager);
        createNotificationChannel();
        textDetectState = (TextView) findViewById(R.id.textDetectState);
        ToggleButton buttonToggleDetect = (ToggleButton) findViewById(R.id.buttonDetectToggle);
        if (isServiceRunning(this)) {
            textDetectState.setText(R.string.tx_detect);
            if (buttonToggleDetect!=null) buttonToggleDetect.setChecked(true);
        } else {  //service not running but check the state in SharedPreference
            SharedPreferences prefs = getSharedPreferences(getString(R.string.file_shared_prefs_name),
                                                           Context.MODE_PRIVATE);
            String state = prefs.getString(getString(R.string.pk_state), "not found");
            if (state.equals(getString(R.string.tx_state_running))) {
                if (buttonToggleDetect != null) buttonToggleDetect.setChecked(true);
                startService();
            }
        }
        Button buttonExit = (Button) findViewById(R.id.buttonExit);

        if (buttonToggleDetect != null) {
            buttonToggleDetect.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setDetectEnabled();
                }
            });
        }

        if (buttonExit != null) {
            buttonExit.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        }


        //call receiver
        callEventReceiver = new CallEventReceiver();
        IntentFilter filter = new IntentFilter(getString(R.string.ac_call));
        registerReceiver(callEventReceiver,filter);

        //call stats buttons
        buttonReceived = (Button) findViewById(R.id.button_received);
        buttonTriggered = (Button) findViewById(R.id.button_triggered);

        //register button events
        buttonReceived.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCalls();
            }
        });
        buttonTriggered.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTriggers();
            }
        });

        //grant permission to detect phone state chenges and read contacts
        PermissionHelper.checkPermissions(this);

        //check protected apps for Huawei devices
        //http://stackoverflow.com/questions/31638986/protected-apps-setting-on-huawei-phones-and-how-to-handle-it
        if (isHuawei(this))
            HuaweiAlert(this,false);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (isServiceRunning(this)) {
            Intent intent = new Intent(this, CallDetectService.class);
            bindService(intent, myConnection, Context.BIND_ABOVE_CLIENT);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (aboutDlg!=null && aboutDlg.isShowing())
            aboutDlg.dismiss();
        if (isBound){
            unbindService(myConnection);
            isBound = false;
        }
    }

    @Override
    protected void onDestroy(){
        unregisterReceiver(callEventReceiver);
        super.onDestroy();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_call_blocker_manager, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch(id) {
            case R.id.action_settings:
                showSettings();
                return true;
            case R.id.action_show_runs:
                showRuns();
                return true;
            case R.id.action_show_calls:
                showCalls();
                return true;
            case R.id.action_show_calendar_rules:
                showCalendarRules();
                return true;
            case R.id.action_show_filter_rules:
                showFilterRules();
                return true;
            case R.id.action_show_filters:
                showFilters();
                return true;
            case R.id.action_help:
                showHelp();
                return true;
            case R.id.action_about:
                showAbout();
                return true;
            case R.id.action_faq:
                showFAQ();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PermissionHelper.REQUEST_CODE_PERMISSION_TELEPHONY_STATE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Log.i(TAG,"Telephone state permission granted");
                } else {
                    Log.w(TAG,"Telephone state permission denied");
                }
            }

        }
    }
    private void setDetectEnabled() {

        if (!isServiceRunning(this)) {
            startService();
            //save the running state in a shared preference file
            Context ctx = getApplicationContext();
            SharedPreferences prefs = ctx.getSharedPreferences(
                    getString(R.string.file_shared_prefs_name),
                    Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(getString(R.string.pk_state),
                    getString(R.string.tx_state_running));
            editor.apply();
        } else {
            stopService();
            //save the idle state in a shared preference file
            Context ctx = getApplicationContext();
            SharedPreferences prefs = ctx.getSharedPreferences(
                    getString(R.string.file_shared_prefs_name),
                    Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(getString(R.string.pk_state),
                    getString(R.string.tx_state_idle));
            editor.apply();
        }
    }

    private void stopService() {
        if (isBound) {
            unbindService(myConnection);
            isBound = false;
        }
        Log.i(TAG, "Stopping the service");
        Intent intent = new Intent(this, CallDetectService.class);
        stopService(intent);
        textDetectState.setText(R.string.tx_no_detect);

    }

    private void startService() {
        Log.i(TAG, "Starting the service");
        Intent intent = new Intent(this, CallDetectService.class);


        startService(intent);
        textDetectState.setText((R.string.tx_detect));

    }


    public static boolean isServiceRunning(Context context){
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (service.service.getClassName().equals(CallDetectService.class.getName())) {
                return true;
            }
        }
        return false;
    }

    private void showRuns() {
        Intent intent = new Intent(this,ShowServiceRuns.class);
        startActivity(intent);
    }

    private void showCalls() {
        Intent intent = new Intent(this,ShowLoggedCalls.class);
        startActivity(intent);
    }

    private void showTriggers() {
        Intent intent = new Intent(this,ShowTriggerEvents.class);
        startActivity(intent);
    }

    private void showCalendarRules() {
        Intent intent = new Intent(this,EditCalendarRules.class);
        startActivity(intent);
    }

    private void showFilterRules() {
        Intent intent = new Intent(this,EditFilterRules.class);
        startActivity(intent);
    }


    private void showSettings() {
        Intent intent = new Intent(this,SettingActivity.class);
        startActivity(intent);
    }

    private void showFilters() {
        Intent intent = new Intent(this,EditFilters.class);
        startActivity(intent);
    }
    private void showHelp() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(R.string.tx_main_help_title);

        WebView wv = new WebView(this);
        wv.loadUrl("file:///android_asset/html/main.html");
        ScrollView scroll = new ScrollView(this);
        scroll.setVerticalScrollBarEnabled(true);
        scroll.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        scroll.addView(wv);

        alert.setView(scroll);
        alert.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        alert.show();
    }

    private void showFAQ() {
        Uri uri = Uri.parse(getString(R.string.uri_faq));
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    private void showAbout() {
        String version = "Unknown";
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            version = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.w(TAG,"Could not get package info");
        }
        View messageView = getLayoutInflater().inflate(R.layout.about, null, false);

        TextView versionView = (TextView) messageView.findViewById(R.id.about_version);
        versionView.setText(String.format(Locale.getDefault(),"Version: %s",version));

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.mipmap.police_launcher);
        builder.setTitle(R.string.app_name);
        builder.setView(messageView);
        builder.create();
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        aboutDlg = builder.create();
        aboutDlg.show();
    }

    static boolean isHuawei(Context ctx) {
        Intent intent = new Intent();
        intent.setClassName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity");
        List<ResolveInfo> list = ctx.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    static void HuaweiAlert(final Context ctx, boolean alwaysShow) {
        final SharedPreferences settings = ctx.getSharedPreferences(ctx.getString(R.string.file_shared_prefs_name),
                                                                Context.MODE_PRIVATE);
        final String saveIfSkip = ctx.getString(R.string.pk_skip_protected);
        boolean skipMessage = settings.getBoolean(saveIfSkip, false);
        if (!skipMessage || alwaysShow) {
            final SharedPreferences.Editor editor = settings.edit();

            final AppCompatCheckBox dontShowAgain = new AppCompatCheckBox(ctx);
            dontShowAgain.setText(R.string.tx_dont_show);
            dontShowAgain.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    editor.putBoolean(saveIfSkip, isChecked);
                    editor.apply();
                }
            });

            new AlertDialog.Builder(ctx)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle(R.string.tx_huawei_protected)
                    .setMessage(String.format("%s requires to be enabled in 'Protected Apps' to function properly.%n", ctx.getString(R.string.app_name)))
                    .setView(dontShowAgain)
                    .setPositiveButton(R.string.tx_list_protected, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            huaweiProtectedApps(ctx);
                            editor.putBoolean(saveIfSkip,dontShowAgain.isChecked());
                            editor.apply();
                        }
                    })
                    .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            editor.putBoolean(saveIfSkip,dontShowAgain.isChecked());
                            editor.apply();
                        }
                    })
                    .show();

        }
    }


    private static void huaweiProtectedApps(Context ctx) {
        try {
            String cmd = "am start -n com.huawei.systemmanager/.optimize.process.ProtectActivity";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                cmd += " --user " + getUserSerial(ctx);
            }
            Runtime.getRuntime().exec(cmd);
        } catch (IOException ignored) {
        }
    }

    private static String getUserSerial(Context ctx) {
        //noinspection ResourceType
        Object userManager = ctx.getSystemService("user");
        if (null == userManager) return "";

        try {
            Method myUserHandleMethod = android.os.Process.class.getMethod("myUserHandle", (Class<?>[]) null);
            Object myUserHandle = myUserHandleMethod.invoke(android.os.Process.class, (Object[]) null);
            Method getSerialNumberForUser = userManager.getClass().getMethod("getSerialNumberForUser", myUserHandle.getClass());
            Long userSerial = (Long) getSerialNumberForUser.invoke(userManager, myUserHandle);
            if (userSerial != null) {
                return String.valueOf(userSerial);
            } else {
                return "";
            }
        } catch (NoSuchMethodException | IllegalArgumentException | InvocationTargetException | IllegalAccessException ignored) {
        }
        return "";
    }


}

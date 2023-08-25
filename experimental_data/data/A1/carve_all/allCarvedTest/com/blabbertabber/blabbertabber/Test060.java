package com.blabbertabber.blabbertabber;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test060 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /Users/mattia/Faculty/Research/2020_android_test_carving/repositories/action-based-test-carving/apps-src/blabbertabber/traces/com.blabbertabber.blabbertabber.MainActivityTest#recordingButtonClickedTest/Trace-1650060770820.txt
     * Method invocation under test: <com.blabbertabber.blabbertabber.MainActivity: void launchRecordingActivity()>_25_49
     */
    @Test(timeout = 4000)
    public void test_com_blabbertabber_blabbertabber_MainActivity_launchRecordingActivity_002() throws Exception {
        org.robolectric.android.controller.ActivityController activitycontroller12 = org.robolectric.Robolectric.buildActivity(com.blabbertabber.blabbertabber.MainActivity.class);
        com.blabbertabber.blabbertabber.MainActivity mainactivity7 = (com.blabbertabber.blabbertabber.MainActivity) activitycontroller12.get();
        org.robolectric.android.controller.ActivityController activitycontroller13 = activitycontroller12.create();
        activitycontroller13.resume();
        mainactivity7.launchRecordingActivity();
    }
}

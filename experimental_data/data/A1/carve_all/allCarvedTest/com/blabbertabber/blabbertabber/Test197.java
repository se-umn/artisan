package com.blabbertabber.blabbertabber;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test197 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /Users/mattia/Faculty/Research/2020_android_test_carving/repositories/action-based-test-carving/apps-src/blabbertabber/traces/com.blabbertabber.blabbertabber.MainActivityTest#recordingButtonTest/Trace-1650060769048.txt
     * Method invocation under test: <com.blabbertabber.blabbertabber.MainActivity: void onPause()>_24_48
     */
    @Test(timeout = 4000)
    public void test_com_blabbertabber_blabbertabber_MainActivity_onPause_001() throws Exception {
        org.robolectric.android.controller.ActivityController activitycontroller8 = org.robolectric.Robolectric.buildActivity(com.blabbertabber.blabbertabber.MainActivity.class);
        activitycontroller8.get();
        org.robolectric.android.controller.ActivityController activitycontroller9 = activitycontroller8.create();
        org.robolectric.android.controller.ActivityController activitycontroller10 = activitycontroller9.resume();
        activitycontroller10.pause();
    }
}

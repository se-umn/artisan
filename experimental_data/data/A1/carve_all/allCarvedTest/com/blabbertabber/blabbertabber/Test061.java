package com.blabbertabber.blabbertabber;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test061 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /Users/mattia/Faculty/Research/2020_android_test_carving/repositories/action-based-test-carving/apps-src/blabbertabber/traces/com.blabbertabber.blabbertabber.MainActivityTest#recordingButtonClickedTest/Trace-1650060770820.txt
     * Method invocation under test: <com.blabbertabber.blabbertabber.MainActivity: void onPause()>_33_66
     */
    @Test(timeout = 4000)
    public void test_com_blabbertabber_blabbertabber_MainActivity_onPause_001() throws Exception {
        android.widget.Button button3 = org.mockito.Mockito.mock(android.widget.Button.class);
        org.robolectric.android.controller.ActivityController activitycontroller16 = org.robolectric.Robolectric.buildActivity(com.blabbertabber.blabbertabber.MainActivity.class);
        com.blabbertabber.blabbertabber.MainActivity mainactivity9 = (com.blabbertabber.blabbertabber.MainActivity) activitycontroller16.get();
        org.robolectric.android.controller.ActivityController activitycontroller17 = activitycontroller16.create();
        org.robolectric.android.controller.ActivityController activitycontroller18 = activitycontroller17.resume();
        mainactivity9.launchRecordingActivity(button3);
        activitycontroller18.pause();
    }
}

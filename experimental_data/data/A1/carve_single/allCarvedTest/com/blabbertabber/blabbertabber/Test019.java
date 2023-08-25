package com.blabbertabber.blabbertabber;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test019 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /Users/mattia/Faculty/Research/2020_android_test_carving/repositories/action-based-test-carving/apps-src/blabbertabber/traces/com.blabbertabber.blabbertabber.MainActivityTest#recordingButtonClickedTest/Trace-1650060770820.txt
     * Method invocation under test: <com.blabbertabber.blabbertabber.MainActivity: void launchRecordingActivity(android.view.View)>_24_48
     */
    @Test(timeout = 4000)
    public void test_com_blabbertabber_blabbertabber_MainActivity_launchRecordingActivity_001() throws Exception {
        android.widget.Button button1 = org.mockito.Mockito.mock(android.widget.Button.class);
        org.robolectric.android.controller.ActivityController activitycontroller8 = org.robolectric.Robolectric.buildActivity(com.blabbertabber.blabbertabber.MainActivity.class);
        com.blabbertabber.blabbertabber.MainActivity mainactivity5 = (com.blabbertabber.blabbertabber.MainActivity) activitycontroller8.get();
        org.robolectric.android.controller.ActivityController activitycontroller9 = activitycontroller8.create();
        activitycontroller9.resume();
        mainactivity5.launchRecordingActivity(button1);
    }
}

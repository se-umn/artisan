package com.blabbertabber.blabbertabber;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test033 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /Users/mattia/Faculty/Research/2020_android_test_carving/repositories/action-based-test-carving/apps-src/blabbertabber/traces/com.blabbertabber.blabbertabber.AboutActivityTest#buttonAckFinishTest/Trace-1650060773293.txt
     * Method invocation under test: <com.blabbertabber.blabbertabber.AboutActivity: void onCreate(android.os.Bundle)>_3_6
     */
    @Test(timeout = 4000)
    public void test_com_blabbertabber_blabbertabber_AboutActivity_onCreate_001() throws Exception {
        org.robolectric.android.controller.ActivityController activitycontroller1 = org.robolectric.Robolectric.buildActivity(com.blabbertabber.blabbertabber.AboutActivity.class);
        activitycontroller1.get();
        activitycontroller1.create();
    }
}

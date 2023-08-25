package com.blabbertabber.blabbertabber;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test067 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /Users/mattia/Faculty/Research/2020_android_test_carving/repositories/action-based-test-carving/apps-src/blabbertabber/traces/com.blabbertabber.blabbertabber.MainActivityTest#recordingButtonClickedTest/Trace-1650060770820.txt
     * Method invocation under test: <com.blabbertabber.blabbertabber.AudioEventProcessor: void <init>(android.content.Context)>_76_152
     */
    @Test(timeout = 4000)
    public void test_com_blabbertabber_blabbertabber_AudioEventProcessor_constructor_001() throws Exception {
        com.blabbertabber.blabbertabber.AudioEventProcessor audioeventprocessor0 = null;
        android.content.Context context1 = ApplicationProvider.getApplicationContext();
        audioeventprocessor0 = new com.blabbertabber.blabbertabber.AudioEventProcessor(context1);
    }
}

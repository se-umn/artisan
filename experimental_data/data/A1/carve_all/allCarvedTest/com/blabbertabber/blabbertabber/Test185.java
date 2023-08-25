package com.blabbertabber.blabbertabber;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;

public class Test185 {

    /**
     * Generated from /Users/mattia/Faculty/Research/2020_android_test_carving/repositories/action-based-test-carving/apps-src/blabbertabber/traces/com.blabbertabber.blabbertabber.RecordingActivityTest#whenIResetMeetingTheTimeShouldBeResetToZero/Trace-1650060739694.txt
     * Method invocation under test: <com.blabbertabber.blabbertabber.Timer: void stop()>_211_422
     */
    @Test(timeout = 4000)
    public void test_com_blabbertabber_blabbertabber_Timer_stop_002() throws Exception {
        com.blabbertabber.blabbertabber.Timer timer23 = null;
        timer23 = new com.blabbertabber.blabbertabber.Timer(0L);
        timer23.time();
        timer23.stop();
        timer23.time();
        timer23.time();
        timer23.time();
        timer23.time();
        timer23.time();
        timer23.start();
        timer23.time();
        timer23.time();
        timer23.time();
        timer23.time();
        timer23.time();
        timer23.time();
        timer23.time();
        timer23.time();
        timer23.time();
        timer23.time();
        timer23.time();
        timer23.time();
        timer23.stop();
    }
}

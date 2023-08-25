package com.blabbertabber.blabbertabber;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;

public class Test189 {

    /**
     * Generated from /Users/mattia/Faculty/Research/2020_android_test_carving/repositories/action-based-test-carving/apps-src/blabbertabber/traces/com.blabbertabber.blabbertabber.RecordingActivityTest#whenIResetMeetingTheTimeShouldBeResetToZero/Trace-1650060739694.txt
     * Method invocation under test: <com.blabbertabber.blabbertabber.Timer: void reset()>_242_484
     */
    @Test(timeout = 4000)
    public void test_com_blabbertabber_blabbertabber_Timer_reset_001() throws Exception {
        com.blabbertabber.blabbertabber.Timer timer27 = null;
        timer27 = new com.blabbertabber.blabbertabber.Timer(0L);
        timer27.time();
        timer27.stop();
        timer27.time();
        timer27.time();
        timer27.time();
        timer27.time();
        timer27.time();
        timer27.start();
        timer27.time();
        timer27.time();
        timer27.time();
        timer27.time();
        timer27.time();
        timer27.time();
        timer27.time();
        timer27.time();
        timer27.time();
        timer27.time();
        timer27.time();
        timer27.time();
        timer27.stop();
        timer27.time();
        timer27.time();
        timer27.time();
        timer27.reset();
    }
}

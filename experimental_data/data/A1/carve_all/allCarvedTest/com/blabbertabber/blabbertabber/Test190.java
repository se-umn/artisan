package com.blabbertabber.blabbertabber;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;

public class Test190 {

    /**
     * Generated from /Users/mattia/Faculty/Research/2020_android_test_carving/repositories/action-based-test-carving/apps-src/blabbertabber/traces/com.blabbertabber.blabbertabber.RecordingActivityTest#whenIResetMeetingTheTimeShouldBeResetToZero/Trace-1650060739694.txt
     * Method invocation under test: <com.blabbertabber.blabbertabber.Timer: long time()>_247_494
     */
    @Test(timeout = 4000)
    public void test_com_blabbertabber_blabbertabber_Timer_time_023() throws Exception {
        com.blabbertabber.blabbertabber.Timer timer28 = null;
        timer28 = new com.blabbertabber.blabbertabber.Timer(0L);
        timer28.time();
        timer28.stop();
        timer28.time();
        timer28.time();
        timer28.time();
        timer28.time();
        timer28.time();
        timer28.start();
        timer28.time();
        timer28.time();
        timer28.time();
        timer28.time();
        timer28.time();
        timer28.time();
        timer28.time();
        timer28.time();
        timer28.time();
        timer28.time();
        timer28.time();
        timer28.time();
        timer28.stop();
        timer28.time();
        timer28.time();
        timer28.time();
        timer28.reset();
        timer28.time();
    }
}

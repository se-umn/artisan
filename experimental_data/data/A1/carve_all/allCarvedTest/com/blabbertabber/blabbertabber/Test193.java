package com.blabbertabber.blabbertabber;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;

public class Test193 {

    /**
     * Generated from /Users/mattia/Faculty/Research/2020_android_test_carving/repositories/action-based-test-carving/apps-src/blabbertabber/traces/com.blabbertabber.blabbertabber.RecordingActivityTest#whenIResetMeetingTheTimeShouldBeResetToZero/Trace-1650060739694.txt
     * Method invocation under test: <com.blabbertabber.blabbertabber.Timer: long time()>_266_532
     */
    @Test(timeout = 4000)
    public void test_com_blabbertabber_blabbertabber_Timer_time_025() throws Exception {
        com.blabbertabber.blabbertabber.Timer timer31 = null;
        timer31 = new com.blabbertabber.blabbertabber.Timer(0L);
        timer31.time();
        timer31.stop();
        timer31.time();
        timer31.time();
        timer31.time();
        timer31.time();
        timer31.time();
        timer31.start();
        timer31.time();
        timer31.time();
        timer31.time();
        timer31.time();
        timer31.time();
        timer31.time();
        timer31.time();
        timer31.time();
        timer31.time();
        timer31.time();
        timer31.time();
        timer31.time();
        timer31.stop();
        timer31.time();
        timer31.time();
        timer31.time();
        timer31.reset();
        timer31.time();
        timer31.stop();
        timer31.time();
        timer31.time();
    }
}

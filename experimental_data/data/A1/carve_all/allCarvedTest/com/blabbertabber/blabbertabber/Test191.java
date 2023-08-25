package com.blabbertabber.blabbertabber;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;

public class Test191 {

    /**
     * Generated from /Users/mattia/Faculty/Research/2020_android_test_carving/repositories/action-based-test-carving/apps-src/blabbertabber/traces/com.blabbertabber.blabbertabber.RecordingActivityTest#whenIResetMeetingTheTimeShouldBeResetToZero/Trace-1650060739694.txt
     * Method invocation under test: <com.blabbertabber.blabbertabber.Timer: void stop()>_256_512
     */
    @Test(timeout = 4000)
    public void test_com_blabbertabber_blabbertabber_Timer_stop_003() throws Exception {
        com.blabbertabber.blabbertabber.Timer timer29 = null;
        timer29 = new com.blabbertabber.blabbertabber.Timer(0L);
        timer29.time();
        timer29.stop();
        timer29.time();
        timer29.time();
        timer29.time();
        timer29.time();
        timer29.time();
        timer29.start();
        timer29.time();
        timer29.time();
        timer29.time();
        timer29.time();
        timer29.time();
        timer29.time();
        timer29.time();
        timer29.time();
        timer29.time();
        timer29.time();
        timer29.time();
        timer29.time();
        timer29.stop();
        timer29.time();
        timer29.time();
        timer29.time();
        timer29.reset();
        timer29.time();
        timer29.stop();
    }
}

package com.blabbertabber.blabbertabber;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;

public class Test177 {

    /**
     * Generated from /Users/mattia/Faculty/Research/2020_android_test_carving/repositories/action-based-test-carving/apps-src/blabbertabber/traces/com.blabbertabber.blabbertabber.RecordingActivityTest#whenIResetMeetingTheTimeShouldBeResetToZero/Trace-1650060739694.txt
     * Method invocation under test: <com.blabbertabber.blabbertabber.Timer: long time()>_131_262
     */
    @Test(timeout = 4000)
    public void test_com_blabbertabber_blabbertabber_Timer_time_012() throws Exception {
        com.blabbertabber.blabbertabber.Timer timer15 = null;
        timer15 = new com.blabbertabber.blabbertabber.Timer(0L);
        timer15.time();
        timer15.stop();
        timer15.time();
        timer15.time();
        timer15.time();
        timer15.time();
        timer15.time();
        timer15.start();
        timer15.time();
        timer15.time();
        timer15.time();
        timer15.time();
        timer15.time();
    }
}

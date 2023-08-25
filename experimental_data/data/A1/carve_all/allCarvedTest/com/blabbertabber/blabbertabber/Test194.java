package com.blabbertabber.blabbertabber;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;

public class Test194 {

    /**
     * Generated from /Users/mattia/Faculty/Research/2020_android_test_carving/repositories/action-based-test-carving/apps-src/blabbertabber/traces/com.blabbertabber.blabbertabber.RecordingActivityTest#whenIResetMeetingTheTimeShouldBeResetToZero/Trace-1650060739694.txt
     * Method invocation under test: <com.blabbertabber.blabbertabber.Timer: long time()>_275_550
     */
    @Test(timeout = 4000)
    public void test_com_blabbertabber_blabbertabber_Timer_time_026() throws Exception {
        com.blabbertabber.blabbertabber.Timer timer32 = null;
        timer32 = new com.blabbertabber.blabbertabber.Timer(0L);
        timer32.time();
        timer32.stop();
        timer32.time();
        timer32.time();
        timer32.time();
        timer32.time();
        timer32.time();
        timer32.start();
        timer32.time();
        timer32.time();
        timer32.time();
        timer32.time();
        timer32.time();
        timer32.time();
        timer32.time();
        timer32.time();
        timer32.time();
        timer32.time();
        timer32.time();
        timer32.time();
        timer32.stop();
        timer32.time();
        timer32.time();
        timer32.time();
        timer32.reset();
        timer32.time();
        timer32.stop();
        timer32.time();
        timer32.time();
        timer32.time();
    }
}

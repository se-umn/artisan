package com.blabbertabber.blabbertabber;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;

public class Test101 {

    /**
     * Generated from /Users/mattia/Faculty/Research/2020_android_test_carving/repositories/action-based-test-carving/apps-src/blabbertabber/traces/com.blabbertabber.blabbertabber.RecordingActivityTest#whenIRotateTheTimerContinuesRunning/Trace-1650060751680.txt
     * Method invocation under test: <com.blabbertabber.blabbertabber.Timer: long time()>_235_470
     */
    @Test(timeout = 4000)
    public void test_com_blabbertabber_blabbertabber_Timer_time_021() throws Exception {
        com.blabbertabber.blabbertabber.Timer timer29 = null;
        timer29 = new com.blabbertabber.blabbertabber.Timer(200L);
        timer29.time();
        timer29.start();
        timer29.time();
        timer29.time();
        timer29.time();
        timer29.time();
        timer29.time();
        timer29.time();
    }
}

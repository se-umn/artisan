package com.blabbertabber.blabbertabber;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;

public class Test088 {

    /**
     * Generated from /Users/mattia/Faculty/Research/2020_android_test_carving/repositories/action-based-test-carving/apps-src/blabbertabber/traces/com.blabbertabber.blabbertabber.RecordingActivityTest#whenIRotateTheTimerContinuesRunning/Trace-1650060751680.txt
     * Method invocation under test: <com.blabbertabber.blabbertabber.Timer: long time()>_126_252
     */
    @Test(timeout = 4000)
    public void test_com_blabbertabber_blabbertabber_Timer_time_011() throws Exception {
        com.blabbertabber.blabbertabber.Timer timer16 = null;
        timer16 = new com.blabbertabber.blabbertabber.Timer(0L);
        timer16.time();
        timer16.stop();
        timer16.time();
        timer16.time();
        timer16.time();
        timer16.reset();
        timer16.time();
        timer16.stop();
        timer16.time();
        timer16.time();
        timer16.time();
        timer16.start();
        timer16.time();
        timer16.time();
    }
}

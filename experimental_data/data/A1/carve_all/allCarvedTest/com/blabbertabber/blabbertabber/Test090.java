package com.blabbertabber.blabbertabber;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;

public class Test090 {

    /**
     * Generated from /Users/mattia/Faculty/Research/2020_android_test_carving/repositories/action-based-test-carving/apps-src/blabbertabber/traces/com.blabbertabber.blabbertabber.RecordingActivityTest#whenIRotateTheTimerContinuesRunning/Trace-1650060751680.txt
     * Method invocation under test: <com.blabbertabber.blabbertabber.Timer: long time()>_146_292
     */
    @Test(timeout = 4000)
    public void test_com_blabbertabber_blabbertabber_Timer_time_013() throws Exception {
        com.blabbertabber.blabbertabber.Timer timer18 = null;
        timer18 = new com.blabbertabber.blabbertabber.Timer(0L);
        timer18.time();
        timer18.stop();
        timer18.time();
        timer18.time();
        timer18.time();
        timer18.reset();
        timer18.time();
        timer18.stop();
        timer18.time();
        timer18.time();
        timer18.time();
        timer18.start();
        timer18.time();
        timer18.time();
        timer18.time();
        timer18.time();
    }
}

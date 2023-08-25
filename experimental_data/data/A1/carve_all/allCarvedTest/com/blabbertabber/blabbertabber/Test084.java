package com.blabbertabber.blabbertabber;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;

public class Test084 {

    /**
     * Generated from /Users/mattia/Faculty/Research/2020_android_test_carving/repositories/action-based-test-carving/apps-src/blabbertabber/traces/com.blabbertabber.blabbertabber.RecordingActivityTest#whenIRotateTheTimerContinuesRunning/Trace-1650060751680.txt
     * Method invocation under test: <com.blabbertabber.blabbertabber.Timer: long time()>_93_186
     */
    @Test(timeout = 4000)
    public void test_com_blabbertabber_blabbertabber_Timer_time_008() throws Exception {
        com.blabbertabber.blabbertabber.Timer timer12 = null;
        timer12 = new com.blabbertabber.blabbertabber.Timer(0L);
        timer12.time();
        timer12.stop();
        timer12.time();
        timer12.time();
        timer12.time();
        timer12.reset();
        timer12.time();
        timer12.stop();
        timer12.time();
        timer12.time();
    }
}

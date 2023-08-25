package com.blabbertabber.blabbertabber;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;

public class Test080 {

    /**
     * Generated from /Users/mattia/Faculty/Research/2020_android_test_carving/repositories/action-based-test-carving/apps-src/blabbertabber/traces/com.blabbertabber.blabbertabber.RecordingActivityTest#whenIRotateTheTimerContinuesRunning/Trace-1650060751680.txt
     * Method invocation under test: <com.blabbertabber.blabbertabber.Timer: void reset()>_69_138
     */
    @Test(timeout = 4000)
    public void test_com_blabbertabber_blabbertabber_Timer_reset_001() throws Exception {
        com.blabbertabber.blabbertabber.Timer timer8 = null;
        timer8 = new com.blabbertabber.blabbertabber.Timer(0L);
        timer8.time();
        timer8.stop();
        timer8.time();
        timer8.time();
        timer8.time();
        timer8.reset();
    }
}

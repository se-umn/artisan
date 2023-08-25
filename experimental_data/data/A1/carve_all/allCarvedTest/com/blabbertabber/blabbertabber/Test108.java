package com.blabbertabber.blabbertabber;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;

public class Test108 {

    /**
     * Generated from /Users/mattia/Faculty/Research/2020_android_test_carving/repositories/action-based-test-carving/apps-src/blabbertabber/traces/com.blabbertabber.blabbertabber.RecordingActivityTest#whenIRotateTheTimerContinuesRunning/Trace-1650060751680.txt
     * Method invocation under test: <com.blabbertabber.blabbertabber.Timer: void start()>_290_580
     */
    @Test(timeout = 4000)
    public void test_com_blabbertabber_blabbertabber_Timer_start_003() throws Exception {
        com.blabbertabber.blabbertabber.Timer timer36 = null;
        timer36 = new com.blabbertabber.blabbertabber.Timer(1200L);
        timer36.time();
        timer36.start();
    }
}

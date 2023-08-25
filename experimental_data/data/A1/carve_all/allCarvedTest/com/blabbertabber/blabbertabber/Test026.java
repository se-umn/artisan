package com.blabbertabber.blabbertabber;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;

public class Test026 {

    /**
     * Generated from /Users/mattia/Faculty/Research/2020_android_test_carving/repositories/action-based-test-carving/apps-src/blabbertabber/traces/com.blabbertabber.blabbertabber.RecordingActivityTest#pushRecordingTest/Trace-1650060745294.txt
     * Method invocation under test: <com.blabbertabber.blabbertabber.Timer: void start()>_87_174
     */
    @Test(timeout = 4000)
    public void test_com_blabbertabber_blabbertabber_Timer_start_001() throws Exception {
        com.blabbertabber.blabbertabber.Timer timer10 = null;
        timer10 = new com.blabbertabber.blabbertabber.Timer(0L);
        timer10.time();
        timer10.stop();
        timer10.time();
        timer10.time();
        timer10.time();
        timer10.time();
        timer10.time();
        timer10.start();
    }
}

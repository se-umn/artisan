package com.blabbertabber.blabbertabber;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;

public class Test033 {

    /**
     * Generated from /Users/mattia/Faculty/Research/2020_android_test_carving/repositories/action-based-test-carving/apps-src/blabbertabber/traces/com.blabbertabber.blabbertabber.RecordingActivityTest#pushRecordingTest/Trace-1650060745294.txt
     * Method invocation under test: <com.blabbertabber.blabbertabber.Timer: long time()>_143_286
     */
    @Test(timeout = 4000)
    public void test_com_blabbertabber_blabbertabber_Timer_time_013() throws Exception {
        com.blabbertabber.blabbertabber.Timer timer17 = null;
        timer17 = new com.blabbertabber.blabbertabber.Timer(0L);
        timer17.time();
        timer17.stop();
        timer17.time();
        timer17.time();
        timer17.time();
        timer17.time();
        timer17.time();
        timer17.start();
        timer17.time();
        timer17.time();
        timer17.time();
        timer17.stop();
        timer17.time();
        timer17.time();
        timer17.time();
    }
}

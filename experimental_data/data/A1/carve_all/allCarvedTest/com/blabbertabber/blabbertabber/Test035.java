package com.blabbertabber.blabbertabber;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;

public class Test035 {

    /**
     * Generated from /Users/mattia/Faculty/Research/2020_android_test_carving/repositories/action-based-test-carving/apps-src/blabbertabber/traces/com.blabbertabber.blabbertabber.RecordingActivityTest#pushRecordingTest/Trace-1650060745294.txt
     * Method invocation under test: <com.blabbertabber.blabbertabber.Timer: long time()>_156_312
     */
    @Test(timeout = 4000)
    public void test_com_blabbertabber_blabbertabber_Timer_time_014() throws Exception {
        com.blabbertabber.blabbertabber.Timer timer19 = null;
        timer19 = new com.blabbertabber.blabbertabber.Timer(0L);
        timer19.time();
        timer19.stop();
        timer19.time();
        timer19.time();
        timer19.time();
        timer19.time();
        timer19.time();
        timer19.start();
        timer19.time();
        timer19.time();
        timer19.time();
        timer19.stop();
        timer19.time();
        timer19.time();
        timer19.time();
        timer19.start();
        timer19.time();
    }
}

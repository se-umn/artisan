package com.blabbertabber.blabbertabber;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;

public class Test030 {

    /**
     * Generated from /Users/mattia/Faculty/Research/2020_android_test_carving/repositories/action-based-test-carving/apps-src/blabbertabber/traces/com.blabbertabber.blabbertabber.RecordingActivityTest#pushRecordingTest/Trace-1650060745294.txt
     * Method invocation under test: <com.blabbertabber.blabbertabber.Timer: void stop()>_121_242
     */
    @Test(timeout = 4000)
    public void test_com_blabbertabber_blabbertabber_Timer_stop_002() throws Exception {
        com.blabbertabber.blabbertabber.Timer timer14 = null;
        timer14 = new com.blabbertabber.blabbertabber.Timer(0L);
        timer14.time();
        timer14.stop();
        timer14.time();
        timer14.time();
        timer14.time();
        timer14.time();
        timer14.time();
        timer14.start();
        timer14.time();
        timer14.time();
        timer14.time();
        timer14.stop();
    }
}

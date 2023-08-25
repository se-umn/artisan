package com.blabbertabber.blabbertabber;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;

public class Test068 {

    /**
     * Generated from /Users/mattia/Faculty/Research/2020_android_test_carving/repositories/action-based-test-carving/apps-src/blabbertabber/traces/com.blabbertabber.blabbertabber.MainActivityTest#recordingButtonClickedTest/Trace-1650060770820.txt
     * Method invocation under test: <com.blabbertabber.blabbertabber.Timer: long time()>_82_164
     */
    @Test(timeout = 4000)
    public void test_com_blabbertabber_blabbertabber_Timer_time_003() throws Exception {
        com.blabbertabber.blabbertabber.Timer timer5 = null;
        timer5 = new com.blabbertabber.blabbertabber.Timer(0L);
        timer5.time();
        timer5.stop();
        timer5.time();
    }
}

package com.blabbertabber.blabbertabber;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;

public class Test069 {

    /**
     * Generated from /Users/mattia/Faculty/Research/2020_android_test_carving/repositories/action-based-test-carving/apps-src/blabbertabber/traces/com.blabbertabber.blabbertabber.MainActivityTest#recordingButtonClickedTest/Trace-1650060770820.txt
     * Method invocation under test: <com.blabbertabber.blabbertabber.Timer: long time()>_91_182
     */
    @Test(timeout = 4000)
    public void test_com_blabbertabber_blabbertabber_Timer_time_004() throws Exception {
        com.blabbertabber.blabbertabber.Timer timer6 = null;
        timer6 = new com.blabbertabber.blabbertabber.Timer(0L);
        timer6.time();
        timer6.stop();
        timer6.time();
        timer6.time();
    }
}

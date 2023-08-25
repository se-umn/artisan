package com.blabbertabber.blabbertabber;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;

public class Test012 {

    /**
     * Generated from /Users/mattia/Faculty/Research/2020_android_test_carving/repositories/action-based-test-carving/apps-src/blabbertabber/traces/com.blabbertabber.blabbertabber.RecordingActivityTest#whenIResetMeetingItShouldBePaused/Trace-1650060766657.txt
     * Method invocation under test: <com.blabbertabber.blabbertabber.Timer: long time()>_75_150
     */
    @Test(timeout = 4000)
    public void test_com_blabbertabber_blabbertabber_Timer_time_006() throws Exception {
        com.blabbertabber.blabbertabber.Timer timer10 = null;
        timer10 = new com.blabbertabber.blabbertabber.Timer(0L);
        timer10.time();
        timer10.stop();
        timer10.time();
        timer10.time();
        timer10.reset();
        timer10.time();
        timer10.stop();
        timer10.time();
    }
}

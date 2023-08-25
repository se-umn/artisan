package com.blabbertabber.blabbertabber;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;

public class Test009 {

    /**
     * Generated from /Users/mattia/Faculty/Research/2020_android_test_carving/repositories/action-based-test-carving/apps-src/blabbertabber/traces/com.blabbertabber.blabbertabber.RecordingActivityTest#whenIResetMeetingItShouldBePaused/Trace-1650060766657.txt
     * Method invocation under test: <com.blabbertabber.blabbertabber.Timer: void reset()>_60_120
     */
    @Test(timeout = 4000)
    public void test_com_blabbertabber_blabbertabber_Timer_reset_001() throws Exception {
        com.blabbertabber.blabbertabber.Timer timer7 = null;
        timer7 = new com.blabbertabber.blabbertabber.Timer(0L);
        timer7.time();
        timer7.stop();
        timer7.time();
        timer7.time();
        timer7.reset();
    }
}

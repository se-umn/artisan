package com.blabbertabber.blabbertabber;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;

public class Test011 {

    /**
     * Generated from /Users/mattia/Faculty/Research/2020_android_test_carving/repositories/action-based-test-carving/apps-src/blabbertabber/traces/com.blabbertabber.blabbertabber.RecordingActivityTest#whenIResetMeetingItShouldBePaused/Trace-1650060766657.txt
     * Method invocation under test: <com.blabbertabber.blabbertabber.Timer: void stop()>_74_148
     */
    @Test(timeout = 4000)
    public void test_com_blabbertabber_blabbertabber_Timer_stop_002() throws Exception {
        com.blabbertabber.blabbertabber.Timer timer9 = null;
        timer9 = new com.blabbertabber.blabbertabber.Timer(0L);
        timer9.time();
        timer9.stop();
        timer9.time();
        timer9.time();
        timer9.reset();
        timer9.time();
        timer9.stop();
    }
}

package com.prismaqf.callblocker.rules;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;

public class Test134 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.EditFiltersTest#CreateAndSelectNewCalendarRule/Trace-1651091796523.txt
     * Method invocation under test: <com.prismaqf.callblocker.rules.CalendarRule: java.util.EnumSet getDayMask()>_562_1122
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_rules_CalendarRule_getDayMask_001() throws Exception {
        com.prismaqf.callblocker.rules.CalendarRule calendarrule3 = null;
        calendarrule3 = new com.prismaqf.callblocker.rules.CalendarRule();
        calendarrule3.getName();
        calendarrule3.setName("always");
        calendarrule3.getDayMask();
    }
}

package com.prismaqf.callblocker.rules;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;

public class Test1511 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.NewCalendarRuleTest#TestNoDays/Trace-1651091751878.txt
     * Method invocation under test: <com.prismaqf.callblocker.rules.CalendarRule: void setName(java.lang.String)>_229_454
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_rules_CalendarRule_setName_001() throws Exception {
        com.prismaqf.callblocker.rules.CalendarRule calendarrule2 = null;
        calendarrule2 = new com.prismaqf.callblocker.rules.CalendarRule();
        calendarrule2.getName();
        calendarrule2.setName("always");
    }
}

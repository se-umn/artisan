package com.prismaqf.callblocker.rules;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;

public class Test364 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.NewCalendarRuleTest#TestAllDays/Trace-1651091749870.txt
     * Method invocation under test: <com.prismaqf.callblocker.rules.CalendarRule: void setDayMask(java.util.EnumSet)>_294_587
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_rules_CalendarRule_setDayMask_001() throws Exception {
        java.lang.Object object1 = null;
        com.prismaqf.callblocker.rules.CalendarRule calendarrule13 = null;
        calendarrule13 = new com.prismaqf.callblocker.rules.CalendarRule();
        calendarrule13.getName();
        calendarrule13.setName("always");
        calendarrule13.getDayMask();
        calendarrule13.getDayMask();
        calendarrule13.getDayMask();
        calendarrule13.getDayMask();
        calendarrule13.getDayMask();
        calendarrule13.getDayMask();
        calendarrule13.getDayMask();
        calendarrule13.getStartTime();
        calendarrule13.getEndTime();
        calendarrule13.equals(object1);
        java.util.EnumSet enumset68 = java.util.EnumSet.noneOf(com.prismaqf.callblocker.rules.CalendarRule.DayOfWeek.class);
        calendarrule13.setDayMask(enumset68);
    }
}

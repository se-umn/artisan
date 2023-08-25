package com.prismaqf.callblocker.rules;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;

public class Test641 {

    /**
     * Generated from /Users/mattia/Faculty/Research/2020_android_test_carving/repositories/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.NewCalendarRuleTest#TestNoDays/Trace-1651091751878.txt
     * Method invocation under test: <com.prismaqf.callblocker.rules.CalendarRule: void setDayMask(java.util.EnumSet)>_294_587
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_rules_CalendarRule_setDayMask_001() throws Exception {
        java.lang.Object object1 = null;
        com.prismaqf.callblocker.rules.CalendarRule calendarrule7 = null;
        calendarrule7 = new com.prismaqf.callblocker.rules.CalendarRule();
        calendarrule7.getName();
        calendarrule7.setName("always");
        calendarrule7.getDayMask();
        calendarrule7.getDayMask();
        calendarrule7.getDayMask();
        calendarrule7.getDayMask();
        calendarrule7.getDayMask();
        calendarrule7.getDayMask();
        calendarrule7.getDayMask();
        calendarrule7.getStartTime();
        calendarrule7.getEndTime();
        calendarrule7.equals(object1);
        java.util.EnumSet enumset35 = java.util.EnumSet.noneOf(com.prismaqf.callblocker.rules.CalendarRule.DayOfWeek.class);
        calendarrule7.setDayMask(enumset35);
    }
}

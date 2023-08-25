package com.prismaqf.callblocker.filters;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;

public class Test805 {

    /**
     * Generated from /Users/mattia/Faculty/Research/2020_android_test_carving/repositories/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.EditFiltersTest#NewFilterTest/Trace-1651091782604.txt
     * Method invocation under test: <com.prismaqf.callblocker.filters.FilterHandle: java.lang.String getActionSimpleName()>_262_521
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_filters_FilterHandle_getActionSimpleName_001() throws Exception {
        java.lang.String string12 = null;
        com.prismaqf.callblocker.filters.FilterHandle filterhandle5 = null;
        filterhandle5 = new com.prismaqf.callblocker.filters.FilterHandle("dummy", string12, string12, string12);
        filterhandle5.getName();
        filterhandle5.setName("dummy");
        filterhandle5.getCalendarRuleName();
        filterhandle5.getFilterRuleName();
        filterhandle5.getActionSimpleName();
    }
}

package com.prismaqf.callblocker.filters;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;

public class Test1767 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.EditFiltersTest#NewFilterTest/Trace-1651091782604.txt
     * Method invocation under test: <com.prismaqf.callblocker.filters.FilterHandle: java.lang.String getCalendarRuleName()>_250_497
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_filters_FilterHandle_getCalendarRuleName_001() throws Exception {
        java.lang.String string5 = null;
        com.prismaqf.callblocker.filters.FilterHandle filterhandle3 = null;
        filterhandle3 = new com.prismaqf.callblocker.filters.FilterHandle("dummy", string5, string5, string5);
        filterhandle3.getName();
        filterhandle3.setName("dummy");
        filterhandle3.getCalendarRuleName();
    }
}

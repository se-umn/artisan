package com.prismaqf.callblocker.filters;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;

public class Test1768 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.EditFiltersTest#NewFilterTest/Trace-1651091782604.txt
     * Method invocation under test: <com.prismaqf.callblocker.filters.FilterHandle: java.lang.String getFilterRuleName()>_256_509
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_filters_FilterHandle_getFilterRuleName_001() throws Exception {
        java.lang.String string8 = null;
        com.prismaqf.callblocker.filters.FilterHandle filterhandle4 = null;
        filterhandle4 = new com.prismaqf.callblocker.filters.FilterHandle("dummy", string8, string8, string8);
        filterhandle4.getName();
        filterhandle4.setName("dummy");
        filterhandle4.getCalendarRuleName();
        filterhandle4.getFilterRuleName();
    }
}

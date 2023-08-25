package com.prismaqf.callblocker.filters;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;

public class Test806 {

    /**
     * Generated from /Users/mattia/Faculty/Research/2020_android_test_carving/repositories/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.EditFiltersTest#NewFilterTest/Trace-1651091782604.txt
     * Method invocation under test: <com.prismaqf.callblocker.filters.FilterHandle: boolean equals(java.lang.Object)>_267_529
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_filters_FilterHandle_equals_001() throws Exception {
        java.lang.String string17 = null;
        java.lang.Object object0 = null;
        com.prismaqf.callblocker.filters.FilterHandle filterhandle6 = null;
        filterhandle6 = new com.prismaqf.callblocker.filters.FilterHandle("dummy", string17, string17, string17);
        filterhandle6.getName();
        filterhandle6.setName("dummy");
        filterhandle6.getCalendarRuleName();
        filterhandle6.getFilterRuleName();
        filterhandle6.getActionSimpleName();
        filterhandle6.equals(object0);
    }
}

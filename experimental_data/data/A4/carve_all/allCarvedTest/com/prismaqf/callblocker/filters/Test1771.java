package com.prismaqf.callblocker.filters;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;

public class Test1771 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.EditFiltersTest#NewFilterTest/Trace-1651091782604.txt
     * Method invocation under test: <com.prismaqf.callblocker.filters.FilterHandle: void setName(java.lang.String)>_290_579
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_filters_FilterHandle_setName_002() throws Exception {
        java.lang.String string22 = null;
        java.lang.Object object1 = null;
        com.prismaqf.callblocker.filters.FilterHandle filterhandle7 = null;
        filterhandle7 = new com.prismaqf.callblocker.filters.FilterHandle("dummy", string22, string22, string22);
        filterhandle7.getName();
        filterhandle7.setName("dummy");
        filterhandle7.getCalendarRuleName();
        filterhandle7.getFilterRuleName();
        filterhandle7.getActionSimpleName();
        filterhandle7.equals(object1);
        filterhandle7.setName("dummy filter");
    }
}

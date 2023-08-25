package com.prismaqf.callblocker.filters;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;

public class Test1773 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.EditFiltersTest#NewFilterTest/Trace-1651091782604.txt
     * Method invocation under test: <com.prismaqf.callblocker.filters.FilterHandle: void setName(java.lang.String)>_304_607
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_filters_FilterHandle_setName_004() throws Exception {
        java.lang.String string32 = null;
        java.lang.Object object3 = null;
        com.prismaqf.callblocker.filters.FilterHandle filterhandle9 = null;
        filterhandle9 = new com.prismaqf.callblocker.filters.FilterHandle("dummy", string32, string32, string32);
        filterhandle9.getName();
        filterhandle9.setName("dummy");
        filterhandle9.getCalendarRuleName();
        filterhandle9.getFilterRuleName();
        filterhandle9.getActionSimpleName();
        filterhandle9.equals(object3);
        filterhandle9.setName("dummy filter");
        filterhandle9.setName("");
        filterhandle9.setName("a1b2c3");
    }
}

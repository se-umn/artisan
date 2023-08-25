package com.prismaqf.callblocker.filters;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;

public class Test1772 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.EditFiltersTest#NewFilterTest/Trace-1651091782604.txt
     * Method invocation under test: <com.prismaqf.callblocker.filters.FilterHandle: void setName(java.lang.String)>_298_595
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_filters_FilterHandle_setName_003() throws Exception {
        java.lang.String string27 = null;
        java.lang.Object object2 = null;
        com.prismaqf.callblocker.filters.FilterHandle filterhandle8 = null;
        filterhandle8 = new com.prismaqf.callblocker.filters.FilterHandle("dummy", string27, string27, string27);
        filterhandle8.getName();
        filterhandle8.setName("dummy");
        filterhandle8.getCalendarRuleName();
        filterhandle8.getFilterRuleName();
        filterhandle8.getActionSimpleName();
        filterhandle8.equals(object2);
        filterhandle8.setName("dummy filter");
        filterhandle8.setName("");
    }
}

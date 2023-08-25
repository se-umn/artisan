package com.prismaqf.callblocker.filters;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;

public class Test1766 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.EditFiltersTest#NewFilterTest/Trace-1651091782604.txt
     * Method invocation under test: <com.prismaqf.callblocker.filters.FilterHandle: void setName(java.lang.String)>_240_475
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_filters_FilterHandle_setName_001() throws Exception {
        java.lang.String string3 = null;
        com.prismaqf.callblocker.filters.FilterHandle filterhandle2 = null;
        filterhandle2 = new com.prismaqf.callblocker.filters.FilterHandle("dummy", string3, string3, string3);
        filterhandle2.getName();
        filterhandle2.setName("dummy");
    }
}

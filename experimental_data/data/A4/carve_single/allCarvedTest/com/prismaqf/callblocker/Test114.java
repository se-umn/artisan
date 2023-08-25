package com.prismaqf.callblocker;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test114 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /Users/mattia/Faculty/Research/2020_android_test_carving/repositories/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.EditFiltersTest#CreateAndSelectNewFilterRule/Trace-1651091784191.txt
     * Method invocation under test: <com.prismaqf.callblocker.NewEditFilter: void onCreate(android.os.Bundle)>_173_346
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_NewEditFilter_onCreate_001() throws Exception {
        android.content.Intent intent0 = null;
        com.prismaqf.callblocker.filters.FilterHandle filterhandle3 = org.mockito.Mockito.mock(com.prismaqf.callblocker.filters.FilterHandle.class);
        com.prismaqf.callblocker.filters.FilterHandle filterhandle4 = org.mockito.Mockito.mock(com.prismaqf.callblocker.filters.FilterHandle.class);
        org.mockito.stubbing.Stubber stubber1 = org.mockito.Mockito.doReturn(filterhandle3);
        com.prismaqf.callblocker.filters.FilterHandle filterhandle5 = stubber1.when(filterhandle4);
        filterhandle5.clone();
        android.content.Context context13 = ApplicationProvider.getApplicationContext();
        intent0 = new android.content.Intent(context13, com.prismaqf.callblocker.NewEditFilter.class);
        android.content.Intent intent1 = intent0.putExtra("com.prismaqf.callblocker:action", "com.prismaqf.callblocker:update");
        android.content.Intent intent2 = intent1.putExtra("com.prismaqft.callblocker:keyorig", (android.os.Parcelable) filterhandle5);
        android.content.Intent intent3 = intent2.putExtra("com.prismaqft.callblocker:ruleid", 1L);
        org.robolectric.android.controller.ActivityController activitycontroller31 = org.robolectric.Robolectric.buildActivity(com.prismaqf.callblocker.NewEditFilter.class, intent3);
        activitycontroller31.get();
        activitycontroller31.create();
    }
}

package com.prismaqf.callblocker;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test463 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /Users/mattia/Faculty/Research/2020_android_test_carving/repositories/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.EditFiltersTest#EditTheCalendarRule/Trace-1651091808719.txt
     * Method invocation under test: <com.prismaqf.callblocker.NewEditFilter: void onActivityResult(int,int,android.content.Intent)>_627_1254
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_NewEditFilter_onActivityResult_001() throws Exception {
        android.content.Intent intent26 = null;
        android.content.Intent intent27 = null;
        com.prismaqf.callblocker.filters.FilterHandle filterhandle81 = org.mockito.Mockito.mock(com.prismaqf.callblocker.filters.FilterHandle.class);
        org.mockito.stubbing.Stubber stubber177 = org.mockito.Mockito.doReturn("My calendar rule for testing");
        com.prismaqf.callblocker.filters.FilterHandle filterhandle82 = stubber177.when(filterhandle81);
        filterhandle82.getCalendarRuleName();
        org.mockito.stubbing.Stubber stubber178 = org.mockito.Mockito.doReturn("My filter with existing rules");
        com.prismaqf.callblocker.filters.FilterHandle filterhandle83 = stubber178.when(filterhandle81);
        filterhandle83.getName();
        org.mockito.stubbing.Stubber stubber179 = org.mockito.Mockito.doNothing();
        com.prismaqf.callblocker.filters.FilterHandle filterhandle84 = stubber179.when(filterhandle81);
        filterhandle84.setName("My filter with existing rules");
        org.mockito.stubbing.Stubber stubber180 = org.mockito.Mockito.doReturn("My calendar rule for testing");
        com.prismaqf.callblocker.filters.FilterHandle filterhandle85 = stubber180.when(filterhandle81);
        filterhandle85.getCalendarRuleName();
        org.mockito.stubbing.Stubber stubber181 = org.mockito.Mockito.doReturn("My filter rule for testing");
        com.prismaqf.callblocker.filters.FilterHandle filterhandle86 = stubber181.when(filterhandle81);
        filterhandle86.getFilterRuleName();
        org.mockito.stubbing.Stubber stubber182 = org.mockito.Mockito.doReturn("action name");
        com.prismaqf.callblocker.filters.FilterHandle filterhandle87 = stubber182.when(filterhandle81);
        filterhandle87.getActionSimpleName();
        android.os.Parcel parcel49 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber183 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel50 = stubber183.when(parcel49);
        parcel50.writeString("My filter with existing rules");
        org.mockito.stubbing.Stubber stubber184 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel51 = stubber184.when(parcel49);
        parcel51.writeString("My calendar rule for testing");
        org.mockito.stubbing.Stubber stubber185 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel52 = stubber185.when(parcel49);
        parcel52.writeString("My filter rule for testing");
        org.mockito.stubbing.Stubber stubber186 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel53 = stubber186.when(parcel49);
        parcel53.writeString("action name");
        com.prismaqf.callblocker.filters.FilterHandle filterhandle88 = org.mockito.Mockito.mock(com.prismaqf.callblocker.filters.FilterHandle.class);
        org.mockito.stubbing.Stubber stubber187 = org.mockito.Mockito.doNothing();
        com.prismaqf.callblocker.filters.FilterHandle filterhandle89 = stubber187.when(filterhandle88);
        filterhandle89.writeToParcel(parcel53, 0);
        org.mockito.stubbing.Stubber stubber188 = org.mockito.Mockito.doReturn(filterhandle87);
        com.prismaqf.callblocker.filters.FilterHandle filterhandle90 = stubber188.when(filterhandle88);
        filterhandle90.clone();
        org.mockito.stubbing.Stubber stubber189 = org.mockito.Mockito.doReturn("My filter with existing rules");
        com.prismaqf.callblocker.filters.FilterHandle filterhandle92 = stubber189.when(filterhandle88);
        filterhandle92.getName();
        org.mockito.stubbing.Stubber stubber190 = org.mockito.Mockito.doNothing();
        com.prismaqf.callblocker.filters.FilterHandle filterhandle93 = stubber190.when(filterhandle88);
        filterhandle93.setName("My filter with existing rules");
        org.mockito.stubbing.Stubber stubber191 = org.mockito.Mockito.doReturn("My calendar rule for testing");
        com.prismaqf.callblocker.filters.FilterHandle filterhandle94 = stubber191.when(filterhandle88);
        filterhandle94.getCalendarRuleName();
        org.mockito.stubbing.Stubber stubber192 = org.mockito.Mockito.doReturn("My filter rule for testing");
        com.prismaqf.callblocker.filters.FilterHandle filterhandle95 = stubber192.when(filterhandle88);
        filterhandle95.getFilterRuleName();
        org.mockito.stubbing.Stubber stubber193 = org.mockito.Mockito.doReturn("action name");
        com.prismaqf.callblocker.filters.FilterHandle filterhandle96 = stubber193.when(filterhandle88);
        filterhandle96.getActionSimpleName();
        android.content.Context context25 = ApplicationProvider.getApplicationContext();
        intent27 = new android.content.Intent(context25, com.prismaqf.callblocker.NewEditFilter.class);
        android.content.Intent intent28 = intent27.putExtra("com.prismaqf.callblocker:action", "com.prismaqf.callblocker:update");
        android.content.Intent intent29 = intent28.putExtra("com.prismaqft.callblocker:keyorig", (android.os.Parcelable) filterhandle96);
        android.content.Intent intent30 = intent29.putExtra("com.prismaqft.callblocker:ruleid", 2L);
        filterhandle96.writeToParcel(parcel53, 0);
        org.robolectric.android.controller.ActivityController activitycontroller53 = org.robolectric.Robolectric.buildActivity(com.prismaqf.callblocker.NewEditFilter.class, intent30);
        com.prismaqf.callblocker.NewEditFilter neweditfilter9 = (com.prismaqf.callblocker.NewEditFilter) activitycontroller53.get();
        org.robolectric.android.controller.ActivityController activitycontroller54 = activitycontroller53.create();
        activitycontroller54.visible();
        org.robolectric.shadows.ShadowActivity shadowactivity7 = org.robolectric.Shadows.shadowOf(neweditfilter9);
        shadowactivity7.clickMenuItem(2131230736);
        shadowactivity7.clickMenuItem(2131230742);
        neweditfilter9.onActivityResult(1004, -1, intent26);
    }
}

package com.prismaqf.callblocker;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test479 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /Users/mattia/Faculty/Research/2020_android_test_carving/repositories/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.EditFiltersTest#EditTheFilterRule/Trace-1651091813651.txt
     * Method invocation under test: <com.prismaqf.callblocker.NewEditFilter: void onActivityResult(int,int,android.content.Intent)>_589_1178
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_NewEditFilter_onActivityResult_001() throws Exception {
        android.content.Intent intent21 = null;
        android.content.Intent intent22 = null;
        com.prismaqf.callblocker.filters.FilterHandle filterhandle81 = org.mockito.Mockito.mock(com.prismaqf.callblocker.filters.FilterHandle.class);
        org.mockito.stubbing.Stubber stubber115 = org.mockito.Mockito.doReturn("My filter rule for testing");
        com.prismaqf.callblocker.filters.FilterHandle filterhandle82 = stubber115.when(filterhandle81);
        filterhandle82.getFilterRuleName();
        org.mockito.stubbing.Stubber stubber116 = org.mockito.Mockito.doReturn("My filter with existing rules");
        com.prismaqf.callblocker.filters.FilterHandle filterhandle83 = stubber116.when(filterhandle81);
        filterhandle83.getName();
        org.mockito.stubbing.Stubber stubber117 = org.mockito.Mockito.doNothing();
        com.prismaqf.callblocker.filters.FilterHandle filterhandle84 = stubber117.when(filterhandle81);
        filterhandle84.setName("My filter with existing rules");
        org.mockito.stubbing.Stubber stubber118 = org.mockito.Mockito.doReturn("My calendar rule for testing");
        com.prismaqf.callblocker.filters.FilterHandle filterhandle85 = stubber118.when(filterhandle81);
        filterhandle85.getCalendarRuleName();
        org.mockito.stubbing.Stubber stubber119 = org.mockito.Mockito.doReturn("My filter rule for testing");
        com.prismaqf.callblocker.filters.FilterHandle filterhandle86 = stubber119.when(filterhandle81);
        filterhandle86.getFilterRuleName();
        org.mockito.stubbing.Stubber stubber120 = org.mockito.Mockito.doReturn("action name");
        com.prismaqf.callblocker.filters.FilterHandle filterhandle87 = stubber120.when(filterhandle81);
        filterhandle87.getActionSimpleName();
        android.os.Parcel parcel35 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber121 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel36 = stubber121.when(parcel35);
        parcel36.writeString("My filter with existing rules");
        org.mockito.stubbing.Stubber stubber122 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel37 = stubber122.when(parcel35);
        parcel37.writeString("My calendar rule for testing");
        org.mockito.stubbing.Stubber stubber123 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel38 = stubber123.when(parcel35);
        parcel38.writeString("My filter rule for testing");
        org.mockito.stubbing.Stubber stubber124 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel39 = stubber124.when(parcel35);
        parcel39.writeString("action name");
        com.prismaqf.callblocker.filters.FilterHandle filterhandle88 = org.mockito.Mockito.mock(com.prismaqf.callblocker.filters.FilterHandle.class);
        org.mockito.stubbing.Stubber stubber125 = org.mockito.Mockito.doNothing();
        com.prismaqf.callblocker.filters.FilterHandle filterhandle89 = stubber125.when(filterhandle88);
        filterhandle89.writeToParcel(parcel39, 0);
        org.mockito.stubbing.Stubber stubber126 = org.mockito.Mockito.doReturn(filterhandle87);
        com.prismaqf.callblocker.filters.FilterHandle filterhandle90 = stubber126.when(filterhandle88);
        filterhandle90.clone();
        org.mockito.stubbing.Stubber stubber127 = org.mockito.Mockito.doReturn("My filter with existing rules");
        com.prismaqf.callblocker.filters.FilterHandle filterhandle92 = stubber127.when(filterhandle88);
        filterhandle92.getName();
        org.mockito.stubbing.Stubber stubber128 = org.mockito.Mockito.doNothing();
        com.prismaqf.callblocker.filters.FilterHandle filterhandle93 = stubber128.when(filterhandle88);
        filterhandle93.setName("My filter with existing rules");
        org.mockito.stubbing.Stubber stubber129 = org.mockito.Mockito.doReturn("My calendar rule for testing");
        com.prismaqf.callblocker.filters.FilterHandle filterhandle94 = stubber129.when(filterhandle88);
        filterhandle94.getCalendarRuleName();
        org.mockito.stubbing.Stubber stubber130 = org.mockito.Mockito.doReturn("My filter rule for testing");
        com.prismaqf.callblocker.filters.FilterHandle filterhandle95 = stubber130.when(filterhandle88);
        filterhandle95.getFilterRuleName();
        org.mockito.stubbing.Stubber stubber131 = org.mockito.Mockito.doReturn("action name");
        com.prismaqf.callblocker.filters.FilterHandle filterhandle96 = stubber131.when(filterhandle88);
        filterhandle96.getActionSimpleName();
        android.content.Context context23 = ApplicationProvider.getApplicationContext();
        intent22 = new android.content.Intent(context23, com.prismaqf.callblocker.NewEditFilter.class);
        android.content.Intent intent23 = intent22.putExtra("com.prismaqf.callblocker:action", "com.prismaqf.callblocker:update");
        android.content.Intent intent24 = intent23.putExtra("com.prismaqft.callblocker:keyorig", (android.os.Parcelable) filterhandle96);
        android.content.Intent intent25 = intent24.putExtra("com.prismaqft.callblocker:ruleid", 2L);
        filterhandle96.writeToParcel(parcel39, 0);
        org.robolectric.android.controller.ActivityController activitycontroller48 = org.robolectric.Robolectric.buildActivity(com.prismaqf.callblocker.NewEditFilter.class, intent25);
        com.prismaqf.callblocker.NewEditFilter neweditfilter9 = (com.prismaqf.callblocker.NewEditFilter) activitycontroller48.get();
        org.robolectric.android.controller.ActivityController activitycontroller49 = activitycontroller48.create();
        activitycontroller49.visible();
        org.robolectric.shadows.ShadowActivity shadowactivity5 = org.robolectric.Shadows.shadowOf(neweditfilter9);
        shadowactivity5.clickMenuItem(2131230736);
        shadowactivity5.clickMenuItem(2131230744);
        neweditfilter9.onActivityResult(1005, -1, intent21);
    }
}

package com.prismaqf.callblocker;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test1806 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.EditFiltersTest#PickActionTest/Trace-1651091791193.txt
     * Method invocation under test: <com.prismaqf.callblocker.NewEditFilter: void refreshWidgets(boolean)>_3562_7123
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_NewEditFilter_refreshWidgets_001() throws Exception {
        android.content.Intent intent25 = null;
        com.prismaqf.callblocker.filters.FilterHandle filterhandle128 = org.mockito.Mockito.mock(com.prismaqf.callblocker.filters.FilterHandle.class);
        org.mockito.stubbing.Stubber stubber152 = org.mockito.Mockito.doReturn("dummy filter");
        com.prismaqf.callblocker.filters.FilterHandle filterhandle129 = stubber152.when(filterhandle128);
        filterhandle129.getName();
        org.mockito.stubbing.Stubber stubber153 = org.mockito.Mockito.doNothing();
        com.prismaqf.callblocker.filters.FilterHandle filterhandle130 = stubber153.when(filterhandle128);
        filterhandle130.setName("dummy filter");
        org.mockito.stubbing.Stubber stubber154 = org.mockito.Mockito.doReturn("cal rule");
        com.prismaqf.callblocker.filters.FilterHandle filterhandle131 = stubber154.when(filterhandle128);
        filterhandle131.getCalendarRuleName();
        org.mockito.stubbing.Stubber stubber155 = org.mockito.Mockito.doReturn("patterns rule");
        com.prismaqf.callblocker.filters.FilterHandle filterhandle132 = stubber155.when(filterhandle128);
        filterhandle132.getFilterRuleName();
        org.mockito.stubbing.Stubber stubber156 = org.mockito.Mockito.doReturn("DropCallByEndCall");
        com.prismaqf.callblocker.filters.FilterHandle filterhandle133 = stubber156.when(filterhandle128);
        filterhandle133.getActionSimpleName();
        android.os.Parcel parcel55 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber157 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel56 = stubber157.when(parcel55);
        parcel56.writeString("dummy filter");
        org.mockito.stubbing.Stubber stubber158 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel57 = stubber158.when(parcel55);
        parcel57.writeString("cal rule");
        org.mockito.stubbing.Stubber stubber159 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel58 = stubber159.when(parcel55);
        parcel58.writeString("patterns rule");
        org.mockito.stubbing.Stubber stubber160 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel59 = stubber160.when(parcel55);
        parcel59.writeString("action name");
        com.prismaqf.callblocker.filters.FilterHandle filterhandle134 = org.mockito.Mockito.mock(com.prismaqf.callblocker.filters.FilterHandle.class);
        org.mockito.stubbing.Stubber stubber161 = org.mockito.Mockito.doNothing();
        com.prismaqf.callblocker.filters.FilterHandle filterhandle135 = stubber161.when(filterhandle134);
        filterhandle135.writeToParcel(parcel59, 0);
        org.mockito.stubbing.Stubber stubber162 = org.mockito.Mockito.doReturn(filterhandle133);
        com.prismaqf.callblocker.filters.FilterHandle filterhandle136 = stubber162.when(filterhandle134);
        filterhandle136.clone();
        org.mockito.stubbing.Stubber stubber163 = org.mockito.Mockito.doReturn("dummy filter");
        com.prismaqf.callblocker.filters.FilterHandle filterhandle138 = stubber163.when(filterhandle134);
        filterhandle138.getName();
        org.mockito.stubbing.Stubber stubber164 = org.mockito.Mockito.doNothing();
        com.prismaqf.callblocker.filters.FilterHandle filterhandle139 = stubber164.when(filterhandle134);
        filterhandle139.setName("dummy filter");
        org.mockito.stubbing.Stubber stubber165 = org.mockito.Mockito.doReturn("cal rule");
        com.prismaqf.callblocker.filters.FilterHandle filterhandle140 = stubber165.when(filterhandle134);
        filterhandle140.getCalendarRuleName();
        org.mockito.stubbing.Stubber stubber166 = org.mockito.Mockito.doReturn("patterns rule");
        com.prismaqf.callblocker.filters.FilterHandle filterhandle141 = stubber166.when(filterhandle134);
        filterhandle141.getFilterRuleName();
        org.mockito.stubbing.Stubber stubber167 = org.mockito.Mockito.doReturn("action name");
        com.prismaqf.callblocker.filters.FilterHandle filterhandle142 = stubber167.when(filterhandle134);
        filterhandle142.getActionSimpleName();
        android.content.Context context39 = ApplicationProvider.getApplicationContext();
        intent25 = new android.content.Intent(context39, com.prismaqf.callblocker.NewEditFilter.class);
        android.content.Intent intent26 = intent25.putExtra("com.prismaqf.callblocker:action", "com.prismaqf.callblocker:update");
        android.content.Intent intent27 = intent26.putExtra("com.prismaqft.callblocker:keyorig", (android.os.Parcelable) filterhandle142);
        android.content.Intent intent28 = intent27.putExtra("com.prismaqft.callblocker:ruleid", 1L);
        filterhandle142.writeToParcel(parcel59, 0);
        org.robolectric.android.controller.ActivityController activitycontroller61 = org.robolectric.Robolectric.buildActivity(com.prismaqf.callblocker.NewEditFilter.class, intent28);
        com.prismaqf.callblocker.NewEditFilter neweditfilter13 = (com.prismaqf.callblocker.NewEditFilter) activitycontroller61.get();
        org.robolectric.android.controller.ActivityController activitycontroller62 = activitycontroller61.create();
        activitycontroller62.visible();
        org.robolectric.shadows.ShadowActivity shadowactivity9 = org.robolectric.Shadows.shadowOf(neweditfilter13);
        shadowactivity9.clickMenuItem(2131230736);
        shadowactivity9.clickMenuItem(2131230755);
        neweditfilter13.refreshWidgets(true);
    }
}

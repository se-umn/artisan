package com.prismaqf.callblocker;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test778 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.EditFiltersTest#ChangeActionTest/Trace-1651091789188.txt
     * Method invocation under test: <com.prismaqf.callblocker.NewEditFilter: boolean onOptionsItemSelected(android.view.MenuItem)>_297_594
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_NewEditFilter_onOptionsItemSelected_001() throws Exception {
        android.content.Intent intent8 = null;
        com.prismaqf.callblocker.filters.FilterHandle filterhandle35 = org.mockito.Mockito.mock(com.prismaqf.callblocker.filters.FilterHandle.class);
        android.os.Parcel parcel15 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber35 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel16 = stubber35.when(parcel15);
        parcel16.writeString("dummy filter");
        org.mockito.stubbing.Stubber stubber36 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel17 = stubber36.when(parcel15);
        parcel17.writeString("cal rule");
        org.mockito.stubbing.Stubber stubber37 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel18 = stubber37.when(parcel15);
        parcel18.writeString("patterns rule");
        org.mockito.stubbing.Stubber stubber38 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel19 = stubber38.when(parcel15);
        parcel19.writeString("action name");
        com.prismaqf.callblocker.filters.FilterHandle filterhandle36 = org.mockito.Mockito.mock(com.prismaqf.callblocker.filters.FilterHandle.class);
        org.mockito.stubbing.Stubber stubber39 = org.mockito.Mockito.doNothing();
        com.prismaqf.callblocker.filters.FilterHandle filterhandle37 = stubber39.when(filterhandle36);
        filterhandle37.writeToParcel(parcel19, 0);
        org.mockito.stubbing.Stubber stubber40 = org.mockito.Mockito.doReturn(filterhandle35);
        com.prismaqf.callblocker.filters.FilterHandle filterhandle38 = stubber40.when(filterhandle36);
        filterhandle38.clone();
        org.mockito.stubbing.Stubber stubber41 = org.mockito.Mockito.doReturn("dummy filter");
        com.prismaqf.callblocker.filters.FilterHandle filterhandle40 = stubber41.when(filterhandle36);
        filterhandle40.getName();
        org.mockito.stubbing.Stubber stubber42 = org.mockito.Mockito.doNothing();
        com.prismaqf.callblocker.filters.FilterHandle filterhandle41 = stubber42.when(filterhandle36);
        filterhandle41.setName("dummy filter");
        org.mockito.stubbing.Stubber stubber43 = org.mockito.Mockito.doReturn("cal rule");
        com.prismaqf.callblocker.filters.FilterHandle filterhandle42 = stubber43.when(filterhandle36);
        filterhandle42.getCalendarRuleName();
        org.mockito.stubbing.Stubber stubber44 = org.mockito.Mockito.doReturn("patterns rule");
        com.prismaqf.callblocker.filters.FilterHandle filterhandle43 = stubber44.when(filterhandle36);
        filterhandle43.getFilterRuleName();
        org.mockito.stubbing.Stubber stubber45 = org.mockito.Mockito.doReturn("action name");
        com.prismaqf.callblocker.filters.FilterHandle filterhandle44 = stubber45.when(filterhandle36);
        filterhandle44.getActionSimpleName();
        android.content.Context context17 = ApplicationProvider.getApplicationContext();
        intent8 = new android.content.Intent(context17, com.prismaqf.callblocker.NewEditFilter.class);
        android.content.Intent intent9 = intent8.putExtra("com.prismaqf.callblocker:action", "com.prismaqf.callblocker:update");
        android.content.Intent intent10 = intent9.putExtra("com.prismaqft.callblocker:keyorig", (android.os.Parcelable) filterhandle44);
        android.content.Intent intent11 = intent10.putExtra("com.prismaqft.callblocker:ruleid", 1L);
        filterhandle44.writeToParcel(parcel19, 0);
        org.robolectric.android.controller.ActivityController activitycontroller38 = org.robolectric.Robolectric.buildActivity(com.prismaqf.callblocker.NewEditFilter.class, intent11);
        com.prismaqf.callblocker.NewEditFilter neweditfilter5 = (com.prismaqf.callblocker.NewEditFilter) activitycontroller38.get();
        org.robolectric.android.controller.ActivityController activitycontroller39 = activitycontroller38.create();
        activitycontroller39.visible();
        org.robolectric.shadows.ShadowActivity shadowactivity1 = org.robolectric.Shadows.shadowOf(neweditfilter5);
        shadowactivity1.clickMenuItem(2131230736);
    }
}

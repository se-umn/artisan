package com.prismaqf.callblocker;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test437 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.EditFiltersTest#NavigateUpIsContextSensitive/Trace-1651091805625.txt
     * Method invocation under test: <com.prismaqf.callblocker.NewEditFilter: boolean onOptionsItemSelected(android.view.MenuItem)>_325_650
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_NewEditFilter_onOptionsItemSelected_002() throws Exception {
        android.content.Intent intent12 = null;
        com.prismaqf.callblocker.filters.FilterHandle filterhandle54 = org.mockito.Mockito.mock(com.prismaqf.callblocker.filters.FilterHandle.class);
        android.os.Parcel parcel25 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber57 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel26 = stubber57.when(parcel25);
        parcel26.writeString("dummy filter");
        org.mockito.stubbing.Stubber stubber58 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel27 = stubber58.when(parcel25);
        parcel27.writeString("cal rule");
        org.mockito.stubbing.Stubber stubber59 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel28 = stubber59.when(parcel25);
        parcel28.writeString("patterns rule");
        org.mockito.stubbing.Stubber stubber60 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel29 = stubber60.when(parcel25);
        parcel29.writeString("action name");
        com.prismaqf.callblocker.filters.FilterHandle filterhandle55 = org.mockito.Mockito.mock(com.prismaqf.callblocker.filters.FilterHandle.class);
        org.mockito.stubbing.Stubber stubber61 = org.mockito.Mockito.doNothing();
        com.prismaqf.callblocker.filters.FilterHandle filterhandle56 = stubber61.when(filterhandle55);
        filterhandle56.writeToParcel(parcel29, 0);
        org.mockito.stubbing.Stubber stubber62 = org.mockito.Mockito.doReturn(filterhandle54);
        com.prismaqf.callblocker.filters.FilterHandle filterhandle57 = stubber62.when(filterhandle55);
        filterhandle57.clone();
        org.mockito.stubbing.Stubber stubber63 = org.mockito.Mockito.doReturn("dummy filter");
        com.prismaqf.callblocker.filters.FilterHandle filterhandle59 = stubber63.when(filterhandle55);
        filterhandle59.getName();
        org.mockito.stubbing.Stubber stubber64 = org.mockito.Mockito.doNothing();
        com.prismaqf.callblocker.filters.FilterHandle filterhandle60 = stubber64.when(filterhandle55);
        filterhandle60.setName("dummy filter");
        org.mockito.stubbing.Stubber stubber65 = org.mockito.Mockito.doReturn("cal rule");
        com.prismaqf.callblocker.filters.FilterHandle filterhandle61 = stubber65.when(filterhandle55);
        filterhandle61.getCalendarRuleName();
        org.mockito.stubbing.Stubber stubber66 = org.mockito.Mockito.doReturn("patterns rule");
        com.prismaqf.callblocker.filters.FilterHandle filterhandle62 = stubber66.when(filterhandle55);
        filterhandle62.getFilterRuleName();
        org.mockito.stubbing.Stubber stubber67 = org.mockito.Mockito.doReturn("action name");
        com.prismaqf.callblocker.filters.FilterHandle filterhandle63 = stubber67.when(filterhandle55);
        filterhandle63.getActionSimpleName();
        android.content.Context context19 = ApplicationProvider.getApplicationContext();
        intent12 = new android.content.Intent(context19, com.prismaqf.callblocker.NewEditFilter.class);
        android.content.Intent intent13 = intent12.putExtra("com.prismaqf.callblocker:action", "com.prismaqf.callblocker:update");
        android.content.Intent intent14 = intent13.putExtra("com.prismaqft.callblocker:keyorig", (android.os.Parcelable) filterhandle63);
        android.content.Intent intent15 = intent14.putExtra("com.prismaqft.callblocker:ruleid", 1L);
        filterhandle63.writeToParcel(parcel29, 0);
        org.robolectric.android.controller.ActivityController activitycontroller41 = org.robolectric.Robolectric.buildActivity(com.prismaqf.callblocker.NewEditFilter.class, intent15);
        com.prismaqf.callblocker.NewEditFilter neweditfilter7 = (com.prismaqf.callblocker.NewEditFilter) activitycontroller41.get();
        org.robolectric.android.controller.ActivityController activitycontroller42 = activitycontroller41.create();
        activitycontroller42.visible();
        org.robolectric.shadows.ShadowActivity shadowactivity3 = org.robolectric.Shadows.shadowOf(neweditfilter7);
        shadowactivity3.clickMenuItem(2131230736);
        shadowactivity3.clickMenuItem(2131230756);
    }
}

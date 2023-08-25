package com.prismaqf.callblocker;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test1565 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.EditFiltersTest#EditTheFilterRule/Trace-1651091813651.txt
     * Method invocation under test: <com.prismaqf.callblocker.NewEditFilter: boolean onOptionsItemSelected(android.view.MenuItem)>_325_650
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_NewEditFilter_onOptionsItemSelected_002() throws Exception {
        android.content.Intent intent12 = null;
        com.prismaqf.callblocker.filters.FilterHandle filterhandle55 = org.mockito.Mockito.mock(com.prismaqf.callblocker.filters.FilterHandle.class);
        org.mockito.stubbing.Stubber stubber58 = org.mockito.Mockito.doReturn("My filter rule for testing");
        com.prismaqf.callblocker.filters.FilterHandle filterhandle56 = stubber58.when(filterhandle55);
        filterhandle56.getFilterRuleName();
        android.os.Parcel parcel25 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber59 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel26 = stubber59.when(parcel25);
        parcel26.writeString("My filter with existing rules");
        org.mockito.stubbing.Stubber stubber60 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel27 = stubber60.when(parcel25);
        parcel27.writeString("My calendar rule for testing");
        org.mockito.stubbing.Stubber stubber61 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel28 = stubber61.when(parcel25);
        parcel28.writeString("My filter rule for testing");
        org.mockito.stubbing.Stubber stubber62 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel29 = stubber62.when(parcel25);
        parcel29.writeString("action name");
        com.prismaqf.callblocker.filters.FilterHandle filterhandle57 = org.mockito.Mockito.mock(com.prismaqf.callblocker.filters.FilterHandle.class);
        org.mockito.stubbing.Stubber stubber63 = org.mockito.Mockito.doNothing();
        com.prismaqf.callblocker.filters.FilterHandle filterhandle58 = stubber63.when(filterhandle57);
        filterhandle58.writeToParcel(parcel29, 0);
        org.mockito.stubbing.Stubber stubber64 = org.mockito.Mockito.doReturn(filterhandle56);
        com.prismaqf.callblocker.filters.FilterHandle filterhandle59 = stubber64.when(filterhandle57);
        filterhandle59.clone();
        org.mockito.stubbing.Stubber stubber65 = org.mockito.Mockito.doReturn("My filter with existing rules");
        com.prismaqf.callblocker.filters.FilterHandle filterhandle61 = stubber65.when(filterhandle57);
        filterhandle61.getName();
        org.mockito.stubbing.Stubber stubber66 = org.mockito.Mockito.doNothing();
        com.prismaqf.callblocker.filters.FilterHandle filterhandle62 = stubber66.when(filterhandle57);
        filterhandle62.setName("My filter with existing rules");
        org.mockito.stubbing.Stubber stubber67 = org.mockito.Mockito.doReturn("My calendar rule for testing");
        com.prismaqf.callblocker.filters.FilterHandle filterhandle63 = stubber67.when(filterhandle57);
        filterhandle63.getCalendarRuleName();
        org.mockito.stubbing.Stubber stubber68 = org.mockito.Mockito.doReturn("My filter rule for testing");
        com.prismaqf.callblocker.filters.FilterHandle filterhandle64 = stubber68.when(filterhandle57);
        filterhandle64.getFilterRuleName();
        org.mockito.stubbing.Stubber stubber69 = org.mockito.Mockito.doReturn("action name");
        com.prismaqf.callblocker.filters.FilterHandle filterhandle65 = stubber69.when(filterhandle57);
        filterhandle65.getActionSimpleName();
        android.content.Context context19 = ApplicationProvider.getApplicationContext();
        intent12 = new android.content.Intent(context19, com.prismaqf.callblocker.NewEditFilter.class);
        android.content.Intent intent13 = intent12.putExtra("com.prismaqf.callblocker:action", "com.prismaqf.callblocker:update");
        android.content.Intent intent14 = intent13.putExtra("com.prismaqft.callblocker:keyorig", (android.os.Parcelable) filterhandle65);
        android.content.Intent intent15 = intent14.putExtra("com.prismaqft.callblocker:ruleid", 2L);
        filterhandle65.writeToParcel(parcel29, 0);
        org.robolectric.android.controller.ActivityController activitycontroller41 = org.robolectric.Robolectric.buildActivity(com.prismaqf.callblocker.NewEditFilter.class, intent15);
        com.prismaqf.callblocker.NewEditFilter neweditfilter7 = (com.prismaqf.callblocker.NewEditFilter) activitycontroller41.get();
        org.robolectric.android.controller.ActivityController activitycontroller42 = activitycontroller41.create();
        activitycontroller42.visible();
        org.robolectric.shadows.ShadowActivity shadowactivity3 = org.robolectric.Shadows.shadowOf(neweditfilter7);
        shadowactivity3.clickMenuItem(2131230736);
        shadowactivity3.clickMenuItem(2131230744);
    }
}

package com.prismaqf.callblocker;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test692 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /Users/mattia/Faculty/Research/2020_android_test_carving/repositories/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.EditFiltersTest#CreateAndSelectNewCalendarRule/Trace-1651091796523.txt
     * Method invocation under test: <com.prismaqf.callblocker.NewEditFilter: boolean onCreateOptionsMenu(android.view.Menu)>_220_440
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_NewEditFilter_onCreateOptionsMenu_001() throws Exception {
        android.content.Intent intent4 = null;
        com.prismaqf.callblocker.filters.FilterHandle filterhandle16 = org.mockito.Mockito.mock(com.prismaqf.callblocker.filters.FilterHandle.class);
        android.os.Parcel parcel5 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber13 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel6 = stubber13.when(parcel5);
        parcel6.writeString("dummy filter");
        org.mockito.stubbing.Stubber stubber14 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel7 = stubber14.when(parcel5);
        parcel7.writeString("cal rule");
        org.mockito.stubbing.Stubber stubber15 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel8 = stubber15.when(parcel5);
        parcel8.writeString("patterns rule");
        org.mockito.stubbing.Stubber stubber16 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel9 = stubber16.when(parcel5);
        parcel9.writeString("action name");
        com.prismaqf.callblocker.filters.FilterHandle filterhandle17 = org.mockito.Mockito.mock(com.prismaqf.callblocker.filters.FilterHandle.class);
        org.mockito.stubbing.Stubber stubber17 = org.mockito.Mockito.doNothing();
        com.prismaqf.callblocker.filters.FilterHandle filterhandle18 = stubber17.when(filterhandle17);
        filterhandle18.writeToParcel(parcel9, 0);
        org.mockito.stubbing.Stubber stubber18 = org.mockito.Mockito.doReturn(filterhandle16);
        com.prismaqf.callblocker.filters.FilterHandle filterhandle19 = stubber18.when(filterhandle17);
        filterhandle19.clone();
        org.mockito.stubbing.Stubber stubber19 = org.mockito.Mockito.doReturn("dummy filter");
        com.prismaqf.callblocker.filters.FilterHandle filterhandle21 = stubber19.when(filterhandle17);
        filterhandle21.getName();
        org.mockito.stubbing.Stubber stubber20 = org.mockito.Mockito.doNothing();
        com.prismaqf.callblocker.filters.FilterHandle filterhandle22 = stubber20.when(filterhandle17);
        filterhandle22.setName("dummy filter");
        org.mockito.stubbing.Stubber stubber21 = org.mockito.Mockito.doReturn("cal rule");
        com.prismaqf.callblocker.filters.FilterHandle filterhandle23 = stubber21.when(filterhandle17);
        filterhandle23.getCalendarRuleName();
        org.mockito.stubbing.Stubber stubber22 = org.mockito.Mockito.doReturn("patterns rule");
        com.prismaqf.callblocker.filters.FilterHandle filterhandle24 = stubber22.when(filterhandle17);
        filterhandle24.getFilterRuleName();
        org.mockito.stubbing.Stubber stubber23 = org.mockito.Mockito.doReturn("action name");
        com.prismaqf.callblocker.filters.FilterHandle filterhandle25 = stubber23.when(filterhandle17);
        filterhandle25.getActionSimpleName();
        android.content.Context context15 = ApplicationProvider.getApplicationContext();
        intent4 = new android.content.Intent(context15, com.prismaqf.callblocker.NewEditFilter.class);
        android.content.Intent intent5 = intent4.putExtra("com.prismaqf.callblocker:action", "com.prismaqf.callblocker:update");
        android.content.Intent intent6 = intent5.putExtra("com.prismaqft.callblocker:keyorig", (android.os.Parcelable) filterhandle25);
        android.content.Intent intent7 = intent6.putExtra("com.prismaqft.callblocker:ruleid", 1L);
        filterhandle25.writeToParcel(parcel9, 0);
        org.robolectric.android.controller.ActivityController activitycontroller34 = org.robolectric.Robolectric.buildActivity(com.prismaqf.callblocker.NewEditFilter.class, intent7);
        activitycontroller34.get();
        org.robolectric.android.controller.ActivityController activitycontroller35 = activitycontroller34.create();
        activitycontroller35.visible();
    }
}

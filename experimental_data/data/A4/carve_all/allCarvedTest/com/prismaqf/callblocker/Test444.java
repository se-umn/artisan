package com.prismaqf.callblocker;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test444 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.EditFiltersTest#NavigateUpIsContextSensitive/Trace-1651091805625.txt
     * Method invocation under test: <com.prismaqf.callblocker.NewEditFilter: void onActivityResult(int,int,android.content.Intent)>_418_836
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_NewEditFilter_onActivityResult_001() throws Exception {
        android.content.Intent intent26 = null;
        android.content.Intent intent27 = null;
        com.prismaqf.callblocker.filters.FilterHandle filterhandle92 = org.mockito.Mockito.mock(com.prismaqf.callblocker.filters.FilterHandle.class);
        android.os.Parcel parcel45 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber115 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel46 = stubber115.when(parcel45);
        parcel46.writeString("dummy filter");
        org.mockito.stubbing.Stubber stubber116 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel47 = stubber116.when(parcel45);
        parcel47.writeString("cal rule");
        org.mockito.stubbing.Stubber stubber117 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel48 = stubber117.when(parcel45);
        parcel48.writeString("patterns rule");
        org.mockito.stubbing.Stubber stubber118 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel49 = stubber118.when(parcel45);
        parcel49.writeString("action name");
        com.prismaqf.callblocker.filters.FilterHandle filterhandle93 = org.mockito.Mockito.mock(com.prismaqf.callblocker.filters.FilterHandle.class);
        org.mockito.stubbing.Stubber stubber119 = org.mockito.Mockito.doNothing();
        com.prismaqf.callblocker.filters.FilterHandle filterhandle94 = stubber119.when(filterhandle93);
        filterhandle94.writeToParcel(parcel49, 0);
        org.mockito.stubbing.Stubber stubber120 = org.mockito.Mockito.doReturn(filterhandle92);
        com.prismaqf.callblocker.filters.FilterHandle filterhandle95 = stubber120.when(filterhandle93);
        filterhandle95.clone();
        org.mockito.stubbing.Stubber stubber121 = org.mockito.Mockito.doReturn("dummy filter");
        com.prismaqf.callblocker.filters.FilterHandle filterhandle97 = stubber121.when(filterhandle93);
        filterhandle97.getName();
        org.mockito.stubbing.Stubber stubber122 = org.mockito.Mockito.doNothing();
        com.prismaqf.callblocker.filters.FilterHandle filterhandle98 = stubber122.when(filterhandle93);
        filterhandle98.setName("dummy filter");
        org.mockito.stubbing.Stubber stubber123 = org.mockito.Mockito.doReturn("cal rule");
        com.prismaqf.callblocker.filters.FilterHandle filterhandle99 = stubber123.when(filterhandle93);
        filterhandle99.getCalendarRuleName();
        org.mockito.stubbing.Stubber stubber124 = org.mockito.Mockito.doReturn("patterns rule");
        com.prismaqf.callblocker.filters.FilterHandle filterhandle100 = stubber124.when(filterhandle93);
        filterhandle100.getFilterRuleName();
        org.mockito.stubbing.Stubber stubber125 = org.mockito.Mockito.doReturn("action name");
        com.prismaqf.callblocker.filters.FilterHandle filterhandle101 = stubber125.when(filterhandle93);
        filterhandle101.getActionSimpleName();
        android.content.Context context33 = ApplicationProvider.getApplicationContext();
        intent27 = new android.content.Intent(context33, com.prismaqf.callblocker.NewEditFilter.class);
        android.content.Intent intent28 = intent27.putExtra("com.prismaqf.callblocker:action", "com.prismaqf.callblocker:update");
        android.content.Intent intent29 = intent28.putExtra("com.prismaqft.callblocker:keyorig", (android.os.Parcelable) filterhandle101);
        android.content.Intent intent30 = intent29.putExtra("com.prismaqft.callblocker:ruleid", 1L);
        filterhandle101.writeToParcel(parcel49, 0);
        org.robolectric.android.controller.ActivityController activitycontroller60 = org.robolectric.Robolectric.buildActivity(com.prismaqf.callblocker.NewEditFilter.class, intent30);
        com.prismaqf.callblocker.NewEditFilter neweditfilter11 = (com.prismaqf.callblocker.NewEditFilter) activitycontroller60.get();
        org.robolectric.android.controller.ActivityController activitycontroller61 = activitycontroller60.create();
        activitycontroller61.visible();
        org.robolectric.shadows.ShadowActivity shadowactivity7 = org.robolectric.Shadows.shadowOf(neweditfilter11);
        shadowactivity7.clickMenuItem(2131230736);
        shadowactivity7.clickMenuItem(2131230756);
        neweditfilter11.onActivityResult(1001, 0, intent26);
    }
}

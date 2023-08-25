package com.prismaqf.callblocker;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test525 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.EditFiltersTest#EditTheCalendarRule/Trace-1651091808719.txt
     * Method invocation under test: <com.prismaqf.callblocker.NewEditFilter: void onActivityResult(int,int,android.content.Intent)>_627_1254
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_NewEditFilter_onActivityResult_001() throws Exception {
        android.content.Intent intent35 = null;
        android.content.Intent intent36 = null;
        com.prismaqf.callblocker.filters.FilterHandle filterhandle102 = org.mockito.Mockito.mock(com.prismaqf.callblocker.filters.FilterHandle.class);
        org.mockito.stubbing.Stubber stubber257 = org.mockito.Mockito.doReturn("My calendar rule for testing");
        com.prismaqf.callblocker.filters.FilterHandle filterhandle103 = stubber257.when(filterhandle102);
        filterhandle103.getCalendarRuleName();
        org.mockito.stubbing.Stubber stubber258 = org.mockito.Mockito.doReturn("My filter with existing rules");
        com.prismaqf.callblocker.filters.FilterHandle filterhandle104 = stubber258.when(filterhandle102);
        filterhandle104.getName();
        org.mockito.stubbing.Stubber stubber259 = org.mockito.Mockito.doNothing();
        com.prismaqf.callblocker.filters.FilterHandle filterhandle105 = stubber259.when(filterhandle102);
        filterhandle105.setName("My filter with existing rules");
        org.mockito.stubbing.Stubber stubber260 = org.mockito.Mockito.doReturn("My calendar rule for testing");
        com.prismaqf.callblocker.filters.FilterHandle filterhandle106 = stubber260.when(filterhandle102);
        filterhandle106.getCalendarRuleName();
        org.mockito.stubbing.Stubber stubber261 = org.mockito.Mockito.doReturn("My filter rule for testing");
        com.prismaqf.callblocker.filters.FilterHandle filterhandle107 = stubber261.when(filterhandle102);
        filterhandle107.getFilterRuleName();
        org.mockito.stubbing.Stubber stubber262 = org.mockito.Mockito.doReturn("action name");
        com.prismaqf.callblocker.filters.FilterHandle filterhandle108 = stubber262.when(filterhandle102);
        filterhandle108.getActionSimpleName();
        android.os.Parcel parcel73 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber263 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel74 = stubber263.when(parcel73);
        parcel74.writeString("My filter with existing rules");
        org.mockito.stubbing.Stubber stubber264 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel75 = stubber264.when(parcel73);
        parcel75.writeString("My calendar rule for testing");
        org.mockito.stubbing.Stubber stubber265 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel76 = stubber265.when(parcel73);
        parcel76.writeString("My filter rule for testing");
        org.mockito.stubbing.Stubber stubber266 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel77 = stubber266.when(parcel73);
        parcel77.writeString("action name");
        com.prismaqf.callblocker.filters.FilterHandle filterhandle109 = org.mockito.Mockito.mock(com.prismaqf.callblocker.filters.FilterHandle.class);
        org.mockito.stubbing.Stubber stubber267 = org.mockito.Mockito.doNothing();
        com.prismaqf.callblocker.filters.FilterHandle filterhandle110 = stubber267.when(filterhandle109);
        filterhandle110.writeToParcel(parcel77, 0);
        org.mockito.stubbing.Stubber stubber268 = org.mockito.Mockito.doReturn(filterhandle108);
        com.prismaqf.callblocker.filters.FilterHandle filterhandle111 = stubber268.when(filterhandle109);
        filterhandle111.clone();
        org.mockito.stubbing.Stubber stubber269 = org.mockito.Mockito.doReturn("My filter with existing rules");
        com.prismaqf.callblocker.filters.FilterHandle filterhandle113 = stubber269.when(filterhandle109);
        filterhandle113.getName();
        org.mockito.stubbing.Stubber stubber270 = org.mockito.Mockito.doNothing();
        com.prismaqf.callblocker.filters.FilterHandle filterhandle114 = stubber270.when(filterhandle109);
        filterhandle114.setName("My filter with existing rules");
        org.mockito.stubbing.Stubber stubber271 = org.mockito.Mockito.doReturn("My calendar rule for testing");
        com.prismaqf.callblocker.filters.FilterHandle filterhandle115 = stubber271.when(filterhandle109);
        filterhandle115.getCalendarRuleName();
        org.mockito.stubbing.Stubber stubber272 = org.mockito.Mockito.doReturn("My filter rule for testing");
        com.prismaqf.callblocker.filters.FilterHandle filterhandle116 = stubber272.when(filterhandle109);
        filterhandle116.getFilterRuleName();
        org.mockito.stubbing.Stubber stubber273 = org.mockito.Mockito.doReturn("action name");
        com.prismaqf.callblocker.filters.FilterHandle filterhandle117 = stubber273.when(filterhandle109);
        filterhandle117.getActionSimpleName();
        android.content.Context context29 = ApplicationProvider.getApplicationContext();
        intent36 = new android.content.Intent(context29, com.prismaqf.callblocker.NewEditFilter.class);
        android.content.Intent intent37 = intent36.putExtra("com.prismaqf.callblocker:action", "com.prismaqf.callblocker:update");
        android.content.Intent intent38 = intent37.putExtra("com.prismaqft.callblocker:keyorig", (android.os.Parcelable) filterhandle117);
        android.content.Intent intent39 = intent38.putExtra("com.prismaqft.callblocker:ruleid", 2L);
        filterhandle117.writeToParcel(parcel77, 0);
        org.robolectric.android.controller.ActivityController activitycontroller61 = org.robolectric.Robolectric.buildActivity(com.prismaqf.callblocker.NewEditFilter.class, intent39);
        com.prismaqf.callblocker.NewEditFilter neweditfilter11 = (com.prismaqf.callblocker.NewEditFilter) activitycontroller61.get();
        org.robolectric.android.controller.ActivityController activitycontroller62 = activitycontroller61.create();
        activitycontroller62.visible();
        org.robolectric.shadows.ShadowActivity shadowactivity11 = org.robolectric.Shadows.shadowOf(neweditfilter11);
        shadowactivity11.clickMenuItem(2131230736);
        shadowactivity11.clickMenuItem(2131230742);
        neweditfilter11.onActivityResult(1004, -1, intent35);
    }
}

package com.prismaqf.callblocker;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test1569 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.EditFiltersTest#EditTheFilterRule/Trace-1651091813651.txt
     * Method invocation under test: <com.prismaqf.callblocker.NewEditFilter: void onActivityResult(int,int,android.content.Intent)>_589_1178
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_NewEditFilter_onActivityResult_001() throws Exception {
        android.content.Intent intent30 = null;
        android.content.Intent intent31 = null;
        com.prismaqf.callblocker.filters.FilterHandle filterhandle102 = org.mockito.Mockito.mock(com.prismaqf.callblocker.filters.FilterHandle.class);
        org.mockito.stubbing.Stubber stubber181 = org.mockito.Mockito.doReturn("My filter rule for testing");
        com.prismaqf.callblocker.filters.FilterHandle filterhandle103 = stubber181.when(filterhandle102);
        filterhandle103.getFilterRuleName();
        org.mockito.stubbing.Stubber stubber182 = org.mockito.Mockito.doReturn("My filter with existing rules");
        com.prismaqf.callblocker.filters.FilterHandle filterhandle104 = stubber182.when(filterhandle102);
        filterhandle104.getName();
        org.mockito.stubbing.Stubber stubber183 = org.mockito.Mockito.doNothing();
        com.prismaqf.callblocker.filters.FilterHandle filterhandle105 = stubber183.when(filterhandle102);
        filterhandle105.setName("My filter with existing rules");
        org.mockito.stubbing.Stubber stubber184 = org.mockito.Mockito.doReturn("My calendar rule for testing");
        com.prismaqf.callblocker.filters.FilterHandle filterhandle106 = stubber184.when(filterhandle102);
        filterhandle106.getCalendarRuleName();
        org.mockito.stubbing.Stubber stubber185 = org.mockito.Mockito.doReturn("My filter rule for testing");
        com.prismaqf.callblocker.filters.FilterHandle filterhandle107 = stubber185.when(filterhandle102);
        filterhandle107.getFilterRuleName();
        org.mockito.stubbing.Stubber stubber186 = org.mockito.Mockito.doReturn("action name");
        com.prismaqf.callblocker.filters.FilterHandle filterhandle108 = stubber186.when(filterhandle102);
        filterhandle108.getActionSimpleName();
        android.os.Parcel parcel53 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber187 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel54 = stubber187.when(parcel53);
        parcel54.writeString("My filter with existing rules");
        org.mockito.stubbing.Stubber stubber188 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel55 = stubber188.when(parcel53);
        parcel55.writeString("My calendar rule for testing");
        org.mockito.stubbing.Stubber stubber189 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel56 = stubber189.when(parcel53);
        parcel56.writeString("My filter rule for testing");
        org.mockito.stubbing.Stubber stubber190 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel57 = stubber190.when(parcel53);
        parcel57.writeString("action name");
        com.prismaqf.callblocker.filters.FilterHandle filterhandle109 = org.mockito.Mockito.mock(com.prismaqf.callblocker.filters.FilterHandle.class);
        org.mockito.stubbing.Stubber stubber191 = org.mockito.Mockito.doNothing();
        com.prismaqf.callblocker.filters.FilterHandle filterhandle110 = stubber191.when(filterhandle109);
        filterhandle110.writeToParcel(parcel57, 0);
        org.mockito.stubbing.Stubber stubber192 = org.mockito.Mockito.doReturn(filterhandle108);
        com.prismaqf.callblocker.filters.FilterHandle filterhandle111 = stubber192.when(filterhandle109);
        filterhandle111.clone();
        org.mockito.stubbing.Stubber stubber193 = org.mockito.Mockito.doReturn("My filter with existing rules");
        com.prismaqf.callblocker.filters.FilterHandle filterhandle113 = stubber193.when(filterhandle109);
        filterhandle113.getName();
        org.mockito.stubbing.Stubber stubber194 = org.mockito.Mockito.doNothing();
        com.prismaqf.callblocker.filters.FilterHandle filterhandle114 = stubber194.when(filterhandle109);
        filterhandle114.setName("My filter with existing rules");
        org.mockito.stubbing.Stubber stubber195 = org.mockito.Mockito.doReturn("My calendar rule for testing");
        com.prismaqf.callblocker.filters.FilterHandle filterhandle115 = stubber195.when(filterhandle109);
        filterhandle115.getCalendarRuleName();
        org.mockito.stubbing.Stubber stubber196 = org.mockito.Mockito.doReturn("My filter rule for testing");
        com.prismaqf.callblocker.filters.FilterHandle filterhandle116 = stubber196.when(filterhandle109);
        filterhandle116.getFilterRuleName();
        org.mockito.stubbing.Stubber stubber197 = org.mockito.Mockito.doReturn("action name");
        com.prismaqf.callblocker.filters.FilterHandle filterhandle117 = stubber197.when(filterhandle109);
        filterhandle117.getActionSimpleName();
        android.content.Context context27 = ApplicationProvider.getApplicationContext();
        intent31 = new android.content.Intent(context27, com.prismaqf.callblocker.NewEditFilter.class);
        android.content.Intent intent32 = intent31.putExtra("com.prismaqf.callblocker:action", "com.prismaqf.callblocker:update");
        android.content.Intent intent33 = intent32.putExtra("com.prismaqft.callblocker:keyorig", (android.os.Parcelable) filterhandle117);
        android.content.Intent intent34 = intent33.putExtra("com.prismaqft.callblocker:ruleid", 2L);
        filterhandle117.writeToParcel(parcel57, 0);
        org.robolectric.android.controller.ActivityController activitycontroller56 = org.robolectric.Robolectric.buildActivity(com.prismaqf.callblocker.NewEditFilter.class, intent34);
        com.prismaqf.callblocker.NewEditFilter neweditfilter11 = (com.prismaqf.callblocker.NewEditFilter) activitycontroller56.get();
        org.robolectric.android.controller.ActivityController activitycontroller57 = activitycontroller56.create();
        activitycontroller57.visible();
        org.robolectric.shadows.ShadowActivity shadowactivity9 = org.robolectric.Shadows.shadowOf(neweditfilter11);
        shadowactivity9.clickMenuItem(2131230736);
        shadowactivity9.clickMenuItem(2131230744);
        neweditfilter11.onActivityResult(1005, -1, intent30);
    }
}

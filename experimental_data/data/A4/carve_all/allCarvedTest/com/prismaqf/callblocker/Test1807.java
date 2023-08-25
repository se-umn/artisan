package com.prismaqf.callblocker;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test1807 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.EditFiltersTest#PickActionTest/Trace-1651091791193.txt
     * Method invocation under test: <com.prismaqf.callblocker.NewEditFilter: void validateActions()>_3590_7177
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_NewEditFilter_validateActions_001() throws Exception {
        android.content.Intent intent29 = null;
        com.prismaqf.callblocker.filters.FilterHandle filterhandle152 = org.mockito.Mockito.mock(com.prismaqf.callblocker.filters.FilterHandle.class);
        android.os.Parcel parcel65 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber179 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel66 = stubber179.when(parcel65);
        parcel66.writeString("dummy filter");
        org.mockito.stubbing.Stubber stubber180 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel67 = stubber180.when(parcel65);
        parcel67.writeString("cal rule");
        org.mockito.stubbing.Stubber stubber181 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel68 = stubber181.when(parcel65);
        parcel68.writeString("patterns rule");
        org.mockito.stubbing.Stubber stubber182 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel69 = stubber182.when(parcel65);
        parcel69.writeString("action name");
        com.prismaqf.callblocker.filters.FilterHandle filterhandle153 = org.mockito.Mockito.mock(com.prismaqf.callblocker.filters.FilterHandle.class);
        org.mockito.stubbing.Stubber stubber183 = org.mockito.Mockito.doNothing();
        com.prismaqf.callblocker.filters.FilterHandle filterhandle154 = stubber183.when(filterhandle153);
        filterhandle154.writeToParcel(parcel69, 0);
        org.mockito.stubbing.Stubber stubber184 = org.mockito.Mockito.doReturn(filterhandle152);
        com.prismaqf.callblocker.filters.FilterHandle filterhandle155 = stubber184.when(filterhandle153);
        filterhandle155.clone();
        org.mockito.stubbing.Stubber stubber185 = org.mockito.Mockito.doReturn("dummy filter");
        com.prismaqf.callblocker.filters.FilterHandle filterhandle157 = stubber185.when(filterhandle153);
        filterhandle157.getName();
        org.mockito.stubbing.Stubber stubber186 = org.mockito.Mockito.doNothing();
        com.prismaqf.callblocker.filters.FilterHandle filterhandle158 = stubber186.when(filterhandle153);
        filterhandle158.setName("dummy filter");
        org.mockito.stubbing.Stubber stubber187 = org.mockito.Mockito.doReturn("cal rule");
        com.prismaqf.callblocker.filters.FilterHandle filterhandle159 = stubber187.when(filterhandle153);
        filterhandle159.getCalendarRuleName();
        org.mockito.stubbing.Stubber stubber188 = org.mockito.Mockito.doReturn("patterns rule");
        com.prismaqf.callblocker.filters.FilterHandle filterhandle160 = stubber188.when(filterhandle153);
        filterhandle160.getFilterRuleName();
        org.mockito.stubbing.Stubber stubber189 = org.mockito.Mockito.doReturn("action name");
        com.prismaqf.callblocker.filters.FilterHandle filterhandle161 = stubber189.when(filterhandle153);
        filterhandle161.getActionSimpleName();
        android.content.Context context41 = ApplicationProvider.getApplicationContext();
        intent29 = new android.content.Intent(context41, com.prismaqf.callblocker.NewEditFilter.class);
        android.content.Intent intent30 = intent29.putExtra("com.prismaqf.callblocker:action", "com.prismaqf.callblocker:update");
        android.content.Intent intent31 = intent30.putExtra("com.prismaqft.callblocker:keyorig", (android.os.Parcelable) filterhandle161);
        android.content.Intent intent32 = intent31.putExtra("com.prismaqft.callblocker:ruleid", 1L);
        filterhandle161.writeToParcel(parcel69, 0);
        org.robolectric.android.controller.ActivityController activitycontroller65 = org.robolectric.Robolectric.buildActivity(com.prismaqf.callblocker.NewEditFilter.class, intent32);
        com.prismaqf.callblocker.NewEditFilter neweditfilter15 = (com.prismaqf.callblocker.NewEditFilter) activitycontroller65.get();
        org.robolectric.android.controller.ActivityController activitycontroller66 = activitycontroller65.create();
        activitycontroller66.visible();
        org.robolectric.shadows.ShadowActivity shadowactivity11 = org.robolectric.Shadows.shadowOf(neweditfilter15);
        shadowactivity11.clickMenuItem(2131230736);
        shadowactivity11.clickMenuItem(2131230755);
        neweditfilter15.getString(2131558513);
        neweditfilter15.getString(2131558529);
        neweditfilter15.getString(2131558507);
        neweditfilter15.validateActions();
    }
}

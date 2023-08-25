package com.prismaqf.callblocker;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test968 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /Users/mattia/Faculty/Research/2020_android_test_carving/repositories/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.EditFiltersTest#NavigateUpIsContextSensitive/Trace-1651091805625.txt
     * Method invocation under test: <com.prismaqf.callblocker.NewEditFilter: void onActivityResult(int,int,android.content.Intent)>_418_836
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_NewEditFilter_onActivityResult_001() throws Exception {
        android.content.Intent intent22 = null;
        android.content.Intent intent23 = null;
        com.prismaqf.callblocker.filters.FilterHandle filterhandle73 = org.mockito.Mockito.mock(com.prismaqf.callblocker.filters.FilterHandle.class);
        android.os.Parcel parcel35 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber93 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel36 = stubber93.when(parcel35);
        parcel36.writeString("dummy filter");
        org.mockito.stubbing.Stubber stubber94 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel37 = stubber94.when(parcel35);
        parcel37.writeString("cal rule");
        org.mockito.stubbing.Stubber stubber95 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel38 = stubber95.when(parcel35);
        parcel38.writeString("patterns rule");
        org.mockito.stubbing.Stubber stubber96 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel39 = stubber96.when(parcel35);
        parcel39.writeString("action name");
        com.prismaqf.callblocker.filters.FilterHandle filterhandle74 = org.mockito.Mockito.mock(com.prismaqf.callblocker.filters.FilterHandle.class);
        org.mockito.stubbing.Stubber stubber97 = org.mockito.Mockito.doNothing();
        com.prismaqf.callblocker.filters.FilterHandle filterhandle75 = stubber97.when(filterhandle74);
        filterhandle75.writeToParcel(parcel39, 0);
        org.mockito.stubbing.Stubber stubber98 = org.mockito.Mockito.doReturn(filterhandle73);
        com.prismaqf.callblocker.filters.FilterHandle filterhandle76 = stubber98.when(filterhandle74);
        filterhandle76.clone();
        org.mockito.stubbing.Stubber stubber99 = org.mockito.Mockito.doReturn("dummy filter");
        com.prismaqf.callblocker.filters.FilterHandle filterhandle78 = stubber99.when(filterhandle74);
        filterhandle78.getName();
        org.mockito.stubbing.Stubber stubber100 = org.mockito.Mockito.doNothing();
        com.prismaqf.callblocker.filters.FilterHandle filterhandle79 = stubber100.when(filterhandle74);
        filterhandle79.setName("dummy filter");
        org.mockito.stubbing.Stubber stubber101 = org.mockito.Mockito.doReturn("cal rule");
        com.prismaqf.callblocker.filters.FilterHandle filterhandle80 = stubber101.when(filterhandle74);
        filterhandle80.getCalendarRuleName();
        org.mockito.stubbing.Stubber stubber102 = org.mockito.Mockito.doReturn("patterns rule");
        com.prismaqf.callblocker.filters.FilterHandle filterhandle81 = stubber102.when(filterhandle74);
        filterhandle81.getFilterRuleName();
        org.mockito.stubbing.Stubber stubber103 = org.mockito.Mockito.doReturn("action name");
        com.prismaqf.callblocker.filters.FilterHandle filterhandle82 = stubber103.when(filterhandle74);
        filterhandle82.getActionSimpleName();
        android.content.Context context27 = ApplicationProvider.getApplicationContext();
        intent23 = new android.content.Intent(context27, com.prismaqf.callblocker.NewEditFilter.class);
        android.content.Intent intent24 = intent23.putExtra("com.prismaqf.callblocker:action", "com.prismaqf.callblocker:update");
        android.content.Intent intent25 = intent24.putExtra("com.prismaqft.callblocker:keyorig", (android.os.Parcelable) filterhandle82);
        android.content.Intent intent26 = intent25.putExtra("com.prismaqft.callblocker:ruleid", 1L);
        filterhandle82.writeToParcel(parcel39, 0);
        org.robolectric.android.controller.ActivityController activitycontroller56 = org.robolectric.Robolectric.buildActivity(com.prismaqf.callblocker.NewEditFilter.class, intent26);
        com.prismaqf.callblocker.NewEditFilter neweditfilter9 = (com.prismaqf.callblocker.NewEditFilter) activitycontroller56.get();
        org.robolectric.android.controller.ActivityController activitycontroller57 = activitycontroller56.create();
        activitycontroller57.visible();
        org.robolectric.shadows.ShadowActivity shadowactivity5 = org.robolectric.Shadows.shadowOf(neweditfilter9);
        shadowactivity5.clickMenuItem(2131230736);
        shadowactivity5.clickMenuItem(2131230756);
        neweditfilter9.onActivityResult(1001, 0, intent22);
    }
}

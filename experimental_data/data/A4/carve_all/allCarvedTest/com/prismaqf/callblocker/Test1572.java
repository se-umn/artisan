package com.prismaqf.callblocker;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test1572 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.EditFiltersTest#EditTheFilterRule/Trace-1651091813651.txt
     * Method invocation under test: <com.prismaqf.callblocker.NewEditFilter: boolean onOptionsItemSelected(android.view.MenuItem)>_633_1266
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_NewEditFilter_onOptionsItemSelected_003() throws Exception {
        android.content.Intent intent43 = null;
        android.content.Intent intent44 = null;
        com.prismaqf.callblocker.filters.FilterHandle filterhandle186 = org.mockito.Mockito.mock(com.prismaqf.callblocker.filters.FilterHandle.class);
        org.mockito.stubbing.Stubber stubber436 = org.mockito.Mockito.doReturn("My filter rule for testing");
        com.prismaqf.callblocker.filters.FilterHandle filterhandle187 = stubber436.when(filterhandle186);
        filterhandle187.getFilterRuleName();
        org.mockito.stubbing.Stubber stubber437 = org.mockito.Mockito.doReturn("My filter with existing rules");
        com.prismaqf.callblocker.filters.FilterHandle filterhandle188 = stubber437.when(filterhandle186);
        filterhandle188.getName();
        org.mockito.stubbing.Stubber stubber438 = org.mockito.Mockito.doNothing();
        com.prismaqf.callblocker.filters.FilterHandle filterhandle189 = stubber438.when(filterhandle186);
        filterhandle189.setName("My filter with existing rules");
        org.mockito.stubbing.Stubber stubber439 = org.mockito.Mockito.doReturn("My calendar rule for testing");
        com.prismaqf.callblocker.filters.FilterHandle filterhandle190 = stubber439.when(filterhandle186);
        filterhandle190.getCalendarRuleName();
        org.mockito.stubbing.Stubber stubber440 = org.mockito.Mockito.doReturn("My filter rule for testing");
        com.prismaqf.callblocker.filters.FilterHandle filterhandle191 = stubber440.when(filterhandle186);
        filterhandle191.getFilterRuleName();
        org.mockito.stubbing.Stubber stubber441 = org.mockito.Mockito.doReturn("action name");
        com.prismaqf.callblocker.filters.FilterHandle filterhandle192 = stubber441.when(filterhandle186);
        filterhandle192.getActionSimpleName();
        org.mockito.stubbing.Stubber stubber442 = org.mockito.Mockito.doReturn("My filter rule for testing");
        com.prismaqf.callblocker.filters.FilterHandle filterhandle193 = stubber442.when(filterhandle186);
        filterhandle193.getFilterRuleName();
        android.os.Parcel parcel83 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber443 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel84 = stubber443.when(parcel83);
        parcel84.writeString("My filter with existing rules");
        org.mockito.stubbing.Stubber stubber444 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel85 = stubber444.when(parcel83);
        parcel85.writeString("My calendar rule for testing");
        org.mockito.stubbing.Stubber stubber445 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel86 = stubber445.when(parcel83);
        parcel86.writeString("My filter rule for testing");
        org.mockito.stubbing.Stubber stubber446 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel87 = stubber446.when(parcel83);
        parcel87.writeString("action name");
        com.prismaqf.callblocker.filters.FilterHandle filterhandle194 = org.mockito.Mockito.mock(com.prismaqf.callblocker.filters.FilterHandle.class);
        org.mockito.stubbing.Stubber stubber447 = org.mockito.Mockito.doNothing();
        com.prismaqf.callblocker.filters.FilterHandle filterhandle195 = stubber447.when(filterhandle194);
        filterhandle195.writeToParcel(parcel87, 0);
        org.mockito.stubbing.Stubber stubber448 = org.mockito.Mockito.doReturn(filterhandle193);
        com.prismaqf.callblocker.filters.FilterHandle filterhandle196 = stubber448.when(filterhandle194);
        filterhandle196.clone();
        org.mockito.stubbing.Stubber stubber449 = org.mockito.Mockito.doReturn("My filter with existing rules");
        com.prismaqf.callblocker.filters.FilterHandle filterhandle198 = stubber449.when(filterhandle194);
        filterhandle198.getName();
        org.mockito.stubbing.Stubber stubber450 = org.mockito.Mockito.doNothing();
        com.prismaqf.callblocker.filters.FilterHandle filterhandle199 = stubber450.when(filterhandle194);
        filterhandle199.setName("My filter with existing rules");
        org.mockito.stubbing.Stubber stubber451 = org.mockito.Mockito.doReturn("My calendar rule for testing");
        com.prismaqf.callblocker.filters.FilterHandle filterhandle200 = stubber451.when(filterhandle194);
        filterhandle200.getCalendarRuleName();
        org.mockito.stubbing.Stubber stubber452 = org.mockito.Mockito.doReturn("My filter rule for testing");
        com.prismaqf.callblocker.filters.FilterHandle filterhandle201 = stubber452.when(filterhandle194);
        filterhandle201.getFilterRuleName();
        org.mockito.stubbing.Stubber stubber453 = org.mockito.Mockito.doReturn("action name");
        com.prismaqf.callblocker.filters.FilterHandle filterhandle202 = stubber453.when(filterhandle194);
        filterhandle202.getActionSimpleName();
        android.content.Context context33 = ApplicationProvider.getApplicationContext();
        intent44 = new android.content.Intent(context33, com.prismaqf.callblocker.NewEditFilter.class);
        android.content.Intent intent45 = intent44.putExtra("com.prismaqf.callblocker:action", "com.prismaqf.callblocker:update");
        android.content.Intent intent46 = intent45.putExtra("com.prismaqft.callblocker:keyorig", (android.os.Parcelable) filterhandle202);
        android.content.Intent intent47 = intent46.putExtra("com.prismaqft.callblocker:ruleid", 2L);
        filterhandle202.writeToParcel(parcel87, 0);
        org.robolectric.android.controller.ActivityController activitycontroller60 = org.robolectric.Robolectric.buildActivity(com.prismaqf.callblocker.NewEditFilter.class, intent47);
        com.prismaqf.callblocker.NewEditFilter neweditfilter15 = (com.prismaqf.callblocker.NewEditFilter) activitycontroller60.get();
        org.robolectric.android.controller.ActivityController activitycontroller61 = activitycontroller60.create();
        activitycontroller61.visible();
        org.robolectric.shadows.ShadowActivity shadowactivity11 = org.robolectric.Shadows.shadowOf(neweditfilter15);
        shadowactivity11.clickMenuItem(2131230736);
        shadowactivity11.clickMenuItem(2131230744);
        neweditfilter15.onActivityResult(1005, -1, intent43);
        shadowactivity11.clickMenuItem(2131230744);
    }
}

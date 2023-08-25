package com.prismaqf.callblocker;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test528 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.EditFiltersTest#EditTheCalendarRule/Trace-1651091808719.txt
     * Method invocation under test: <com.prismaqf.callblocker.NewEditFilter: boolean onOptionsItemSelected(android.view.MenuItem)>_671_1342
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_NewEditFilter_onOptionsItemSelected_003() throws Exception {
        android.content.Intent intent48 = null;
        android.content.Intent intent49 = null;
        com.prismaqf.callblocker.filters.FilterHandle filterhandle186 = org.mockito.Mockito.mock(com.prismaqf.callblocker.filters.FilterHandle.class);
        org.mockito.stubbing.Stubber stubber512 = org.mockito.Mockito.doReturn("My calendar rule for testing");
        com.prismaqf.callblocker.filters.FilterHandle filterhandle187 = stubber512.when(filterhandle186);
        filterhandle187.getCalendarRuleName();
        org.mockito.stubbing.Stubber stubber513 = org.mockito.Mockito.doReturn("My filter with existing rules");
        com.prismaqf.callblocker.filters.FilterHandle filterhandle188 = stubber513.when(filterhandle186);
        filterhandle188.getName();
        org.mockito.stubbing.Stubber stubber514 = org.mockito.Mockito.doNothing();
        com.prismaqf.callblocker.filters.FilterHandle filterhandle189 = stubber514.when(filterhandle186);
        filterhandle189.setName("My filter with existing rules");
        org.mockito.stubbing.Stubber stubber515 = org.mockito.Mockito.doReturn("My calendar rule for testing");
        com.prismaqf.callblocker.filters.FilterHandle filterhandle190 = stubber515.when(filterhandle186);
        filterhandle190.getCalendarRuleName();
        org.mockito.stubbing.Stubber stubber516 = org.mockito.Mockito.doReturn("My filter rule for testing");
        com.prismaqf.callblocker.filters.FilterHandle filterhandle191 = stubber516.when(filterhandle186);
        filterhandle191.getFilterRuleName();
        org.mockito.stubbing.Stubber stubber517 = org.mockito.Mockito.doReturn("action name");
        com.prismaqf.callblocker.filters.FilterHandle filterhandle192 = stubber517.when(filterhandle186);
        filterhandle192.getActionSimpleName();
        org.mockito.stubbing.Stubber stubber518 = org.mockito.Mockito.doReturn("My calendar rule for testing");
        com.prismaqf.callblocker.filters.FilterHandle filterhandle193 = stubber518.when(filterhandle186);
        filterhandle193.getCalendarRuleName();
        android.os.Parcel parcel103 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber519 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel104 = stubber519.when(parcel103);
        parcel104.writeString("My filter with existing rules");
        org.mockito.stubbing.Stubber stubber520 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel105 = stubber520.when(parcel103);
        parcel105.writeString("My calendar rule for testing");
        org.mockito.stubbing.Stubber stubber521 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel106 = stubber521.when(parcel103);
        parcel106.writeString("My filter rule for testing");
        org.mockito.stubbing.Stubber stubber522 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel107 = stubber522.when(parcel103);
        parcel107.writeString("action name");
        com.prismaqf.callblocker.filters.FilterHandle filterhandle194 = org.mockito.Mockito.mock(com.prismaqf.callblocker.filters.FilterHandle.class);
        org.mockito.stubbing.Stubber stubber523 = org.mockito.Mockito.doNothing();
        com.prismaqf.callblocker.filters.FilterHandle filterhandle195 = stubber523.when(filterhandle194);
        filterhandle195.writeToParcel(parcel107, 0);
        org.mockito.stubbing.Stubber stubber524 = org.mockito.Mockito.doReturn(filterhandle193);
        com.prismaqf.callblocker.filters.FilterHandle filterhandle196 = stubber524.when(filterhandle194);
        filterhandle196.clone();
        org.mockito.stubbing.Stubber stubber525 = org.mockito.Mockito.doReturn("My filter with existing rules");
        com.prismaqf.callblocker.filters.FilterHandle filterhandle198 = stubber525.when(filterhandle194);
        filterhandle198.getName();
        org.mockito.stubbing.Stubber stubber526 = org.mockito.Mockito.doNothing();
        com.prismaqf.callblocker.filters.FilterHandle filterhandle199 = stubber526.when(filterhandle194);
        filterhandle199.setName("My filter with existing rules");
        org.mockito.stubbing.Stubber stubber527 = org.mockito.Mockito.doReturn("My calendar rule for testing");
        com.prismaqf.callblocker.filters.FilterHandle filterhandle200 = stubber527.when(filterhandle194);
        filterhandle200.getCalendarRuleName();
        org.mockito.stubbing.Stubber stubber528 = org.mockito.Mockito.doReturn("My filter rule for testing");
        com.prismaqf.callblocker.filters.FilterHandle filterhandle201 = stubber528.when(filterhandle194);
        filterhandle201.getFilterRuleName();
        org.mockito.stubbing.Stubber stubber529 = org.mockito.Mockito.doReturn("action name");
        com.prismaqf.callblocker.filters.FilterHandle filterhandle202 = stubber529.when(filterhandle194);
        filterhandle202.getActionSimpleName();
        android.content.Context context35 = ApplicationProvider.getApplicationContext();
        intent49 = new android.content.Intent(context35, com.prismaqf.callblocker.NewEditFilter.class);
        android.content.Intent intent50 = intent49.putExtra("com.prismaqf.callblocker:action", "com.prismaqf.callblocker:update");
        android.content.Intent intent51 = intent50.putExtra("com.prismaqft.callblocker:keyorig", (android.os.Parcelable) filterhandle202);
        android.content.Intent intent52 = intent51.putExtra("com.prismaqft.callblocker:ruleid", 2L);
        filterhandle202.writeToParcel(parcel107, 0);
        org.robolectric.android.controller.ActivityController activitycontroller65 = org.robolectric.Robolectric.buildActivity(com.prismaqf.callblocker.NewEditFilter.class, intent52);
        com.prismaqf.callblocker.NewEditFilter neweditfilter15 = (com.prismaqf.callblocker.NewEditFilter) activitycontroller65.get();
        org.robolectric.android.controller.ActivityController activitycontroller66 = activitycontroller65.create();
        activitycontroller66.visible();
        org.robolectric.shadows.ShadowActivity shadowactivity13 = org.robolectric.Shadows.shadowOf(neweditfilter15);
        shadowactivity13.clickMenuItem(2131230736);
        shadowactivity13.clickMenuItem(2131230742);
        neweditfilter15.onActivityResult(1004, -1, intent48);
        shadowactivity13.clickMenuItem(2131230742);
    }
}

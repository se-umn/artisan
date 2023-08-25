package com.prismaqf.callblocker.rules;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test255 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.ManageFilterPatternsTest#PickFromLogWithScreenRotation/Trace-1651091652973.txt
     * Method invocation under test: <com.prismaqf.callblocker.rules.FilterRule: void writeToParcel(android.os.Parcel,int)>_354_708
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_rules_FilterRule_writeToParcel_004() throws Exception {
        com.prismaqf.callblocker.rules.FilterRule filterrule92 = null;
        java.lang.Object object37 = null;
        android.os.Parcel parcel245 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber286 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel246 = stubber286.when(parcel245);
        parcel246.writeString("dummy");
        org.mockito.stubbing.Stubber stubber287 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel247 = stubber287.when(parcel245);
        parcel247.writeString("change me");
        org.mockito.stubbing.Stubber stubber288 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel248 = stubber288.when(parcel245);
        parcel248.writeInt(0);
        org.mockito.stubbing.Stubber stubber289 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel249 = stubber289.when(parcel245);
        parcel249.writeString("dummy");
        org.mockito.stubbing.Stubber stubber290 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel250 = stubber290.when(parcel245);
        parcel250.writeString("change me");
        org.mockito.stubbing.Stubber stubber291 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel251 = stubber291.when(parcel245);
        parcel251.writeInt(0);
        android.os.Parcel parcel252 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber292 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel253 = stubber292.when(parcel252);
        parcel253.writeString("dummy");
        org.mockito.stubbing.Stubber stubber293 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel254 = stubber293.when(parcel252);
        parcel254.writeString("change me");
        org.mockito.stubbing.Stubber stubber294 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel255 = stubber294.when(parcel252);
        parcel255.writeInt(0);
        android.os.Parcel parcel256 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber295 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel257 = stubber295.when(parcel256);
        parcel257.writeString("dummy");
        org.mockito.stubbing.Stubber stubber296 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel258 = stubber296.when(parcel256);
        parcel258.writeString("change me");
        org.mockito.stubbing.Stubber stubber297 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel259 = stubber297.when(parcel256);
        parcel259.writeInt(0);
        filterrule92 = new com.prismaqf.callblocker.rules.FilterRule("dummy", "change me");
        filterrule92.getPatternKeys();
        filterrule92.getPatternKeys();
        filterrule92.getName();
        filterrule92.setName("dummy");
        filterrule92.getDescription();
        filterrule92.setDescription("change me");
        filterrule92.equals(object37);
        filterrule92.getPatternKeys();
        filterrule92.getPatternKeys();
        filterrule92.equals(object37);
        filterrule92.writeToParcel(parcel251, 0);
        filterrule92.writeToParcel(parcel251, 0);
        filterrule92.writeToParcel(parcel259, 0);
        filterrule92.writeToParcel(parcel255, 0);
    }
}

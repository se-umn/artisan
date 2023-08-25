package com.prismaqf.callblocker.rules;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test1630 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.ManageFilterPatternsTest#PickFromLogRepeatedNumber/Trace-1651091681239.txt
     * Method invocation under test: <com.prismaqf.callblocker.rules.FilterRule: void writeToParcel(android.os.Parcel,int)>_268_536
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_rules_FilterRule_writeToParcel_003() throws Exception {
        java.lang.Object object28 = null;
        com.prismaqf.callblocker.rules.FilterRule filterrule54 = null;
        android.os.Parcel parcel75 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber123 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel76 = stubber123.when(parcel75);
        parcel76.writeString("dummy");
        org.mockito.stubbing.Stubber stubber124 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel77 = stubber124.when(parcel75);
        parcel77.writeString("change me");
        org.mockito.stubbing.Stubber stubber125 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel78 = stubber125.when(parcel75);
        parcel78.writeInt(0);
        org.mockito.stubbing.Stubber stubber126 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel79 = stubber126.when(parcel75);
        parcel79.writeString("dummy");
        org.mockito.stubbing.Stubber stubber127 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel80 = stubber127.when(parcel75);
        parcel80.writeString("change me");
        org.mockito.stubbing.Stubber stubber128 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel81 = stubber128.when(parcel75);
        parcel81.writeInt(0);
        android.os.Parcel parcel82 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber129 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel83 = stubber129.when(parcel82);
        parcel83.writeString("dummy");
        org.mockito.stubbing.Stubber stubber130 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel84 = stubber130.when(parcel82);
        parcel84.writeString("change me");
        org.mockito.stubbing.Stubber stubber131 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel85 = stubber131.when(parcel82);
        parcel85.writeInt(0);
        filterrule54 = new com.prismaqf.callblocker.rules.FilterRule("dummy", "change me");
        filterrule54.getPatternKeys();
        filterrule54.getPatternKeys();
        filterrule54.getName();
        filterrule54.setName("dummy");
        filterrule54.getDescription();
        filterrule54.setDescription("change me");
        filterrule54.equals(object28);
        filterrule54.getPatternKeys();
        filterrule54.getPatternKeys();
        filterrule54.equals(object28);
        filterrule54.writeToParcel(parcel81, 0);
        filterrule54.writeToParcel(parcel81, 0);
        filterrule54.writeToParcel(parcel85, 0);
    }
}

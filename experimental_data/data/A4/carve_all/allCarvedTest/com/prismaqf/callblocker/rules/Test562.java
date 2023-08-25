package com.prismaqf.callblocker.rules;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test562 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.NewFilterRuleTest#TestDynamicStateOnRotation/Trace-1651091708855.txt
     * Method invocation under test: <com.prismaqf.callblocker.rules.FilterRule: void writeToParcel(android.os.Parcel,int)>_366_732
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_rules_FilterRule_writeToParcel_003() throws Exception {
        java.lang.Object object31 = null;
        com.prismaqf.callblocker.rules.FilterRule filterrule57 = null;
        android.os.Parcel parcel75 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber125 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel76 = stubber125.when(parcel75);
        parcel76.writeString("Name");
        org.mockito.stubbing.Stubber stubber126 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel77 = stubber126.when(parcel75);
        parcel77.writeString("Description");
        org.mockito.stubbing.Stubber stubber127 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel78 = stubber127.when(parcel75);
        parcel78.writeInt(0);
        org.mockito.stubbing.Stubber stubber128 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel79 = stubber128.when(parcel75);
        parcel79.writeString("Name");
        org.mockito.stubbing.Stubber stubber129 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel80 = stubber129.when(parcel75);
        parcel80.writeString("Description");
        org.mockito.stubbing.Stubber stubber130 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel81 = stubber130.when(parcel75);
        parcel81.writeInt(0);
        android.os.Parcel parcel82 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber131 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel83 = stubber131.when(parcel82);
        parcel83.writeString("Name");
        org.mockito.stubbing.Stubber stubber132 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel84 = stubber132.when(parcel82);
        parcel84.writeString("Description");
        org.mockito.stubbing.Stubber stubber133 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel85 = stubber133.when(parcel82);
        parcel85.writeInt(0);
        filterrule57 = new com.prismaqf.callblocker.rules.FilterRule("dummy", "change me");
        filterrule57.getPatternKeys();
        filterrule57.getPatternKeys();
        filterrule57.getName();
        filterrule57.setName("dummy");
        filterrule57.getDescription();
        filterrule57.setDescription("change me");
        filterrule57.equals(object31);
        filterrule57.getPatternKeys();
        filterrule57.getPatternKeys();
        filterrule57.equals(object31);
        filterrule57.setName("Name");
        filterrule57.setDescription("Description");
        filterrule57.equals(object31);
        filterrule57.writeToParcel(parcel81, 0);
        filterrule57.writeToParcel(parcel81, 0);
        filterrule57.writeToParcel(parcel85, 0);
    }
}

package com.prismaqf.callblocker.rules;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test554 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.NewFilterRuleTest#TestDynamicStateOnRotation/Trace-1651091708855.txt
     * Method invocation under test: <com.prismaqf.callblocker.rules.FilterRule: void writeToParcel(android.os.Parcel,int)>_261_520
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_rules_FilterRule_writeToParcel_002() throws Exception {
        java.lang.Object object8 = null;
        com.prismaqf.callblocker.rules.FilterRule filterrule15 = null;
        android.os.Parcel parcel15 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber16 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel16 = stubber16.when(parcel15);
        parcel16.writeString("Name");
        org.mockito.stubbing.Stubber stubber17 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel17 = stubber17.when(parcel15);
        parcel17.writeString("Description");
        org.mockito.stubbing.Stubber stubber18 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel18 = stubber18.when(parcel15);
        parcel18.writeInt(0);
        org.mockito.stubbing.Stubber stubber19 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel19 = stubber19.when(parcel15);
        parcel19.writeString("Name");
        org.mockito.stubbing.Stubber stubber20 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel20 = stubber20.when(parcel15);
        parcel20.writeString("Description");
        org.mockito.stubbing.Stubber stubber21 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel21 = stubber21.when(parcel15);
        parcel21.writeInt(0);
        filterrule15 = new com.prismaqf.callblocker.rules.FilterRule("dummy", "change me");
        filterrule15.getPatternKeys();
        filterrule15.getPatternKeys();
        filterrule15.getName();
        filterrule15.setName("dummy");
        filterrule15.getDescription();
        filterrule15.setDescription("change me");
        filterrule15.equals(object8);
        filterrule15.getPatternKeys();
        filterrule15.getPatternKeys();
        filterrule15.equals(object8);
        filterrule15.setName("Name");
        filterrule15.setDescription("Description");
        filterrule15.equals(object8);
        filterrule15.writeToParcel(parcel21, 0);
        filterrule15.writeToParcel(parcel21, 0);
    }
}

package com.prismaqf.callblocker.rules;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test553 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.NewFilterRuleTest#TestDynamicStateOnRotation/Trace-1651091708855.txt
     * Method invocation under test: <com.prismaqf.callblocker.rules.FilterRule: void writeToParcel(android.os.Parcel,int)>_253_504
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_rules_FilterRule_writeToParcel_001() throws Exception {
        java.lang.Object object7 = null;
        com.prismaqf.callblocker.rules.FilterRule filterrule14 = null;
        android.os.Parcel parcel4 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber7 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel5 = stubber7.when(parcel4);
        parcel5.writeString("Name");
        org.mockito.stubbing.Stubber stubber8 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel6 = stubber8.when(parcel4);
        parcel6.writeString("Description");
        org.mockito.stubbing.Stubber stubber9 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel7 = stubber9.when(parcel4);
        parcel7.writeInt(0);
        filterrule14 = new com.prismaqf.callblocker.rules.FilterRule("dummy", "change me");
        filterrule14.getPatternKeys();
        filterrule14.getPatternKeys();
        filterrule14.getName();
        filterrule14.setName("dummy");
        filterrule14.getDescription();
        filterrule14.setDescription("change me");
        filterrule14.equals(object7);
        filterrule14.getPatternKeys();
        filterrule14.getPatternKeys();
        filterrule14.equals(object7);
        filterrule14.setName("Name");
        filterrule14.setDescription("Description");
        filterrule14.equals(object7);
        filterrule14.writeToParcel(parcel7, 0);
    }
}

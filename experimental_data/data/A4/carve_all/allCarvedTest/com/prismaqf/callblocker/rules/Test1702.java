package com.prismaqf.callblocker.rules;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test1702 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.NewFilterRuleTest#AfterUpdatingTheRulePatternsTheRuleShouldSaveWhenActivityIsLeft/Trace-1651091712732.txt
     * Method invocation under test: <com.prismaqf.callblocker.rules.FilterRule: void writeToParcel(android.os.Parcel,int)>_239_476
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_rules_FilterRule_writeToParcel_001() throws Exception {
        java.lang.Object object5 = null;
        com.prismaqf.callblocker.rules.FilterRule filterrule12 = null;
        android.os.Parcel parcel4 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber7 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel5 = stubber7.when(parcel4);
        parcel5.writeString("my dummy rule");
        org.mockito.stubbing.Stubber stubber8 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel6 = stubber8.when(parcel4);
        parcel6.writeString("change me");
        org.mockito.stubbing.Stubber stubber9 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel7 = stubber9.when(parcel4);
        parcel7.writeInt(0);
        filterrule12 = new com.prismaqf.callblocker.rules.FilterRule("dummy", "change me");
        filterrule12.getPatternKeys();
        filterrule12.getPatternKeys();
        filterrule12.getName();
        filterrule12.setName("dummy");
        filterrule12.getDescription();
        filterrule12.setDescription("change me");
        filterrule12.equals(object5);
        filterrule12.getPatternKeys();
        filterrule12.getPatternKeys();
        filterrule12.equals(object5);
        filterrule12.setName("my dummy rule");
        filterrule12.writeToParcel(parcel7, 0);
    }
}

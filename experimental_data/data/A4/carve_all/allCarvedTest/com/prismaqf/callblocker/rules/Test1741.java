package com.prismaqf.callblocker.rules;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test1741 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.NewFilterRuleTest#AfterUpdatingTheRulePatternsTheRuleShouldSaveWhenActivityIsLeft/Trace-1651091712732.txt
     * Method invocation under test: <com.prismaqf.callblocker.rules.FilterRule: boolean equals(java.lang.Object)>_587_1172
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_rules_FilterRule_equals_005() throws Exception {
        java.lang.Object object54 = null;
        com.prismaqf.callblocker.rules.FilterRule filterrule135 = null;
        android.os.Parcel parcel595 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber757 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel596 = stubber757.when(parcel595);
        parcel596.writeString("my dummy rule");
        org.mockito.stubbing.Stubber stubber758 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel597 = stubber758.when(parcel595);
        parcel597.writeString("change me");
        org.mockito.stubbing.Stubber stubber759 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel598 = stubber759.when(parcel595);
        parcel598.writeInt(0);
        org.mockito.stubbing.Stubber stubber760 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel599 = stubber760.when(parcel595);
        parcel599.writeString("my dummy rule");
        org.mockito.stubbing.Stubber stubber761 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel600 = stubber761.when(parcel595);
        parcel600.writeString("change me");
        org.mockito.stubbing.Stubber stubber762 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel601 = stubber762.when(parcel595);
        parcel601.writeInt(0);
        android.os.Parcel parcel602 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber763 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel603 = stubber763.when(parcel602);
        parcel603.writeString("my dummy rule");
        org.mockito.stubbing.Stubber stubber764 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel604 = stubber764.when(parcel602);
        parcel604.writeString("change me");
        org.mockito.stubbing.Stubber stubber765 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel605 = stubber765.when(parcel602);
        parcel605.writeInt(0);
        filterrule135 = new com.prismaqf.callblocker.rules.FilterRule("dummy", "change me");
        filterrule135.getPatternKeys();
        filterrule135.getPatternKeys();
        filterrule135.getName();
        filterrule135.setName("dummy");
        filterrule135.getDescription();
        filterrule135.setDescription("change me");
        filterrule135.equals(object54);
        filterrule135.getPatternKeys();
        filterrule135.getPatternKeys();
        filterrule135.equals(object54);
        filterrule135.setName("my dummy rule");
        filterrule135.writeToParcel(parcel601, 0);
        filterrule135.writeToParcel(parcel601, 0);
        filterrule135.writeToParcel(parcel605, 0);
        filterrule135.clearPatterns();
        filterrule135.addPattern("123");
        filterrule135.getName();
        filterrule135.setName("my dummy rule");
        filterrule135.getDescription();
        filterrule135.setDescription("change me");
        filterrule135.equals(object54);
        filterrule135.getPatternKeys();
        filterrule135.getPatternKeys();
        filterrule135.equals(object54);
        filterrule135.equals(object54);
    }
}

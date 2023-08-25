package com.prismaqf.callblocker.rules;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test1739 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.NewFilterRuleTest#AfterUpdatingTheRulePatternsTheRuleShouldSaveWhenActivityIsLeft/Trace-1651091712732.txt
     * Method invocation under test: <com.prismaqf.callblocker.rules.FilterRule: boolean equals(java.lang.Object)>_579_1154
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_rules_FilterRule_equals_004() throws Exception {
        java.lang.Object object53 = null;
        com.prismaqf.callblocker.rules.FilterRule filterrule134 = null;
        android.os.Parcel parcel573 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber677 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel574 = stubber677.when(parcel573);
        parcel574.writeString("my dummy rule");
        org.mockito.stubbing.Stubber stubber678 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel575 = stubber678.when(parcel573);
        parcel575.writeString("change me");
        org.mockito.stubbing.Stubber stubber679 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel576 = stubber679.when(parcel573);
        parcel576.writeInt(0);
        org.mockito.stubbing.Stubber stubber680 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel577 = stubber680.when(parcel573);
        parcel577.writeString("my dummy rule");
        org.mockito.stubbing.Stubber stubber681 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel578 = stubber681.when(parcel573);
        parcel578.writeString("change me");
        org.mockito.stubbing.Stubber stubber682 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel579 = stubber682.when(parcel573);
        parcel579.writeInt(0);
        android.os.Parcel parcel580 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber683 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel581 = stubber683.when(parcel580);
        parcel581.writeString("my dummy rule");
        org.mockito.stubbing.Stubber stubber684 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel582 = stubber684.when(parcel580);
        parcel582.writeString("change me");
        org.mockito.stubbing.Stubber stubber685 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel583 = stubber685.when(parcel580);
        parcel583.writeInt(0);
        filterrule134 = new com.prismaqf.callblocker.rules.FilterRule("dummy", "change me");
        filterrule134.getPatternKeys();
        filterrule134.getPatternKeys();
        filterrule134.getName();
        filterrule134.setName("dummy");
        filterrule134.getDescription();
        filterrule134.setDescription("change me");
        filterrule134.equals(object53);
        filterrule134.getPatternKeys();
        filterrule134.getPatternKeys();
        filterrule134.equals(object53);
        filterrule134.setName("my dummy rule");
        filterrule134.writeToParcel(parcel579, 0);
        filterrule134.writeToParcel(parcel579, 0);
        filterrule134.writeToParcel(parcel583, 0);
        filterrule134.clearPatterns();
        filterrule134.addPattern("123");
        filterrule134.getName();
        filterrule134.setName("my dummy rule");
        filterrule134.getDescription();
        filterrule134.setDescription("change me");
        filterrule134.equals(object53);
        filterrule134.getPatternKeys();
        filterrule134.getPatternKeys();
        filterrule134.equals(object53);
    }
}

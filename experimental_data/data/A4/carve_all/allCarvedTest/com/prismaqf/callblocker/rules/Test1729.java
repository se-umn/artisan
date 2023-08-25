package com.prismaqf.callblocker.rules;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test1729 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.NewFilterRuleTest#AfterUpdatingTheRulePatternsTheRuleShouldSaveWhenActivityIsLeft/Trace-1651091712732.txt
     * Method invocation under test: <com.prismaqf.callblocker.rules.FilterRule: void addPattern(java.lang.String)>_517_1033
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_rules_FilterRule_addPattern_001() throws Exception {
        java.lang.Object object45 = null;
        com.prismaqf.callblocker.rules.FilterRule filterrule126 = null;
        android.os.Parcel parcel397 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber421 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel398 = stubber421.when(parcel397);
        parcel398.writeString("my dummy rule");
        org.mockito.stubbing.Stubber stubber422 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel399 = stubber422.when(parcel397);
        parcel399.writeString("change me");
        org.mockito.stubbing.Stubber stubber423 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel400 = stubber423.when(parcel397);
        parcel400.writeInt(0);
        org.mockito.stubbing.Stubber stubber424 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel401 = stubber424.when(parcel397);
        parcel401.writeString("my dummy rule");
        org.mockito.stubbing.Stubber stubber425 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel402 = stubber425.when(parcel397);
        parcel402.writeString("change me");
        org.mockito.stubbing.Stubber stubber426 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel403 = stubber426.when(parcel397);
        parcel403.writeInt(0);
        android.os.Parcel parcel404 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber427 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel405 = stubber427.when(parcel404);
        parcel405.writeString("my dummy rule");
        org.mockito.stubbing.Stubber stubber428 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel406 = stubber428.when(parcel404);
        parcel406.writeString("change me");
        org.mockito.stubbing.Stubber stubber429 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel407 = stubber429.when(parcel404);
        parcel407.writeInt(0);
        filterrule126 = new com.prismaqf.callblocker.rules.FilterRule("dummy", "change me");
        filterrule126.getPatternKeys();
        filterrule126.getPatternKeys();
        filterrule126.getName();
        filterrule126.setName("dummy");
        filterrule126.getDescription();
        filterrule126.setDescription("change me");
        filterrule126.equals(object45);
        filterrule126.getPatternKeys();
        filterrule126.getPatternKeys();
        filterrule126.equals(object45);
        filterrule126.setName("my dummy rule");
        filterrule126.writeToParcel(parcel403, 0);
        filterrule126.writeToParcel(parcel403, 0);
        filterrule126.writeToParcel(parcel407, 0);
        filterrule126.clearPatterns();
        filterrule126.addPattern("123");
    }
}

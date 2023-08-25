package com.prismaqf.callblocker.rules;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test266 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /Users/mattia/Faculty/Research/2020_android_test_carving/repositories/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.NewFilterRuleTest#AfterUpdatingTheRulePatternsTheRuleShouldSaveWhenActivityIsLeft/Trace-1651091712732.txt
     * Method invocation under test: <com.prismaqf.callblocker.rules.FilterRule: void clearPatterns()>_510_1019
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_rules_FilterRule_clearPatterns_001() throws Exception {
        java.lang.Object object26 = null;
        com.prismaqf.callblocker.rules.FilterRule filterrule57 = null;
        android.os.Parcel parcel105 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber157 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel106 = stubber157.when(parcel105);
        parcel106.writeString("my dummy rule");
        org.mockito.stubbing.Stubber stubber158 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel107 = stubber158.when(parcel105);
        parcel107.writeString("change me");
        org.mockito.stubbing.Stubber stubber159 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel108 = stubber159.when(parcel105);
        parcel108.writeInt(0);
        org.mockito.stubbing.Stubber stubber160 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel109 = stubber160.when(parcel105);
        parcel109.writeString("my dummy rule");
        org.mockito.stubbing.Stubber stubber161 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel110 = stubber161.when(parcel105);
        parcel110.writeString("change me");
        org.mockito.stubbing.Stubber stubber162 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel111 = stubber162.when(parcel105);
        parcel111.writeInt(0);
        android.os.Parcel parcel112 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber163 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel113 = stubber163.when(parcel112);
        parcel113.writeString("my dummy rule");
        org.mockito.stubbing.Stubber stubber164 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel114 = stubber164.when(parcel112);
        parcel114.writeString("change me");
        org.mockito.stubbing.Stubber stubber165 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel115 = stubber165.when(parcel112);
        parcel115.writeInt(0);
        filterrule57 = new com.prismaqf.callblocker.rules.FilterRule("dummy", "change me");
        filterrule57.getPatternKeys();
        filterrule57.getPatternKeys();
        filterrule57.getName();
        filterrule57.setName("dummy");
        filterrule57.getDescription();
        filterrule57.setDescription("change me");
        filterrule57.equals(object26);
        filterrule57.getPatternKeys();
        filterrule57.getPatternKeys();
        filterrule57.equals(object26);
        filterrule57.setName("my dummy rule");
        filterrule57.writeToParcel(parcel111, 0);
        filterrule57.writeToParcel(parcel111, 0);
        filterrule57.writeToParcel(parcel115, 0);
        filterrule57.clearPatterns();
    }
}

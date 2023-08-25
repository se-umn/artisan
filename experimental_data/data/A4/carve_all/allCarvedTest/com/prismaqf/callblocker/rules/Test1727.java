package com.prismaqf.callblocker.rules;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test1727 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.NewFilterRuleTest#AfterUpdatingTheRulePatternsTheRuleShouldSaveWhenActivityIsLeft/Trace-1651091712732.txt
     * Method invocation under test: <com.prismaqf.callblocker.rules.FilterRule: void clearPatterns()>_510_1019
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_rules_FilterRule_clearPatterns_001() throws Exception {
        java.lang.Object object44 = null;
        com.prismaqf.callblocker.rules.FilterRule filterrule124 = null;
        android.os.Parcel parcel365 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber395 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel366 = stubber395.when(parcel365);
        parcel366.writeString("my dummy rule");
        org.mockito.stubbing.Stubber stubber396 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel367 = stubber396.when(parcel365);
        parcel367.writeString("change me");
        org.mockito.stubbing.Stubber stubber397 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel368 = stubber397.when(parcel365);
        parcel368.writeInt(0);
        org.mockito.stubbing.Stubber stubber398 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel369 = stubber398.when(parcel365);
        parcel369.writeString("my dummy rule");
        org.mockito.stubbing.Stubber stubber399 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel370 = stubber399.when(parcel365);
        parcel370.writeString("change me");
        org.mockito.stubbing.Stubber stubber400 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel371 = stubber400.when(parcel365);
        parcel371.writeInt(0);
        android.os.Parcel parcel372 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber401 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel373 = stubber401.when(parcel372);
        parcel373.writeString("my dummy rule");
        org.mockito.stubbing.Stubber stubber402 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel374 = stubber402.when(parcel372);
        parcel374.writeString("change me");
        org.mockito.stubbing.Stubber stubber403 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel375 = stubber403.when(parcel372);
        parcel375.writeInt(0);
        filterrule124 = new com.prismaqf.callblocker.rules.FilterRule("dummy", "change me");
        filterrule124.getPatternKeys();
        filterrule124.getPatternKeys();
        filterrule124.getName();
        filterrule124.setName("dummy");
        filterrule124.getDescription();
        filterrule124.setDescription("change me");
        filterrule124.equals(object44);
        filterrule124.getPatternKeys();
        filterrule124.getPatternKeys();
        filterrule124.equals(object44);
        filterrule124.setName("my dummy rule");
        filterrule124.writeToParcel(parcel371, 0);
        filterrule124.writeToParcel(parcel371, 0);
        filterrule124.writeToParcel(parcel375, 0);
        filterrule124.clearPatterns();
    }
}

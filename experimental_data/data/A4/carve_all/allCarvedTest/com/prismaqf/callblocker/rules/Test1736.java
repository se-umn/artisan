package com.prismaqf.callblocker.rules;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test1736 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.NewFilterRuleTest#AfterUpdatingTheRulePatternsTheRuleShouldSaveWhenActivityIsLeft/Trace-1651091712732.txt
     * Method invocation under test: <com.prismaqf.callblocker.rules.FilterRule: boolean equals(java.lang.Object)>_556_1107
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_rules_FilterRule_equals_003() throws Exception {
        java.lang.Object object50 = null;
        com.prismaqf.callblocker.rules.FilterRule filterrule131 = null;
        android.os.Parcel parcel507 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber623 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel508 = stubber623.when(parcel507);
        parcel508.writeString("my dummy rule");
        org.mockito.stubbing.Stubber stubber624 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel509 = stubber624.when(parcel507);
        parcel509.writeString("change me");
        org.mockito.stubbing.Stubber stubber625 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel510 = stubber625.when(parcel507);
        parcel510.writeInt(0);
        org.mockito.stubbing.Stubber stubber626 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel511 = stubber626.when(parcel507);
        parcel511.writeString("my dummy rule");
        org.mockito.stubbing.Stubber stubber627 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel512 = stubber627.when(parcel507);
        parcel512.writeString("change me");
        org.mockito.stubbing.Stubber stubber628 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel513 = stubber628.when(parcel507);
        parcel513.writeInt(0);
        android.os.Parcel parcel514 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber629 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel515 = stubber629.when(parcel514);
        parcel515.writeString("my dummy rule");
        org.mockito.stubbing.Stubber stubber630 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel516 = stubber630.when(parcel514);
        parcel516.writeString("change me");
        org.mockito.stubbing.Stubber stubber631 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel517 = stubber631.when(parcel514);
        parcel517.writeInt(0);
        filterrule131 = new com.prismaqf.callblocker.rules.FilterRule("dummy", "change me");
        filterrule131.getPatternKeys();
        filterrule131.getPatternKeys();
        filterrule131.getName();
        filterrule131.setName("dummy");
        filterrule131.getDescription();
        filterrule131.setDescription("change me");
        filterrule131.equals(object50);
        filterrule131.getPatternKeys();
        filterrule131.getPatternKeys();
        filterrule131.equals(object50);
        filterrule131.setName("my dummy rule");
        filterrule131.writeToParcel(parcel513, 0);
        filterrule131.writeToParcel(parcel513, 0);
        filterrule131.writeToParcel(parcel517, 0);
        filterrule131.clearPatterns();
        filterrule131.addPattern("123");
        filterrule131.getName();
        filterrule131.setName("my dummy rule");
        filterrule131.getDescription();
        filterrule131.setDescription("change me");
        filterrule131.equals(object50);
    }
}

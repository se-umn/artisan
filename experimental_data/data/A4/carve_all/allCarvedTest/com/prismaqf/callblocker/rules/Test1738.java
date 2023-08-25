package com.prismaqf.callblocker.rules;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test1738 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.NewFilterRuleTest#AfterUpdatingTheRulePatternsTheRuleShouldSaveWhenActivityIsLeft/Trace-1651091712732.txt
     * Method invocation under test: <com.prismaqf.callblocker.rules.FilterRule: java.util.Set getPatternKeys()>_570_1137
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_rules_FilterRule_getPatternKeys_007() throws Exception {
        java.lang.Object object52 = null;
        com.prismaqf.callblocker.rules.FilterRule filterrule133 = null;
        android.os.Parcel parcel551 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber659 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel552 = stubber659.when(parcel551);
        parcel552.writeString("my dummy rule");
        org.mockito.stubbing.Stubber stubber660 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel553 = stubber660.when(parcel551);
        parcel553.writeString("change me");
        org.mockito.stubbing.Stubber stubber661 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel554 = stubber661.when(parcel551);
        parcel554.writeInt(0);
        org.mockito.stubbing.Stubber stubber662 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel555 = stubber662.when(parcel551);
        parcel555.writeString("my dummy rule");
        org.mockito.stubbing.Stubber stubber663 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel556 = stubber663.when(parcel551);
        parcel556.writeString("change me");
        org.mockito.stubbing.Stubber stubber664 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel557 = stubber664.when(parcel551);
        parcel557.writeInt(0);
        android.os.Parcel parcel558 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber665 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel559 = stubber665.when(parcel558);
        parcel559.writeString("my dummy rule");
        org.mockito.stubbing.Stubber stubber666 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel560 = stubber666.when(parcel558);
        parcel560.writeString("change me");
        org.mockito.stubbing.Stubber stubber667 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel561 = stubber667.when(parcel558);
        parcel561.writeInt(0);
        filterrule133 = new com.prismaqf.callblocker.rules.FilterRule("dummy", "change me");
        filterrule133.getPatternKeys();
        filterrule133.getPatternKeys();
        filterrule133.getName();
        filterrule133.setName("dummy");
        filterrule133.getDescription();
        filterrule133.setDescription("change me");
        filterrule133.equals(object52);
        filterrule133.getPatternKeys();
        filterrule133.getPatternKeys();
        filterrule133.equals(object52);
        filterrule133.setName("my dummy rule");
        filterrule133.writeToParcel(parcel557, 0);
        filterrule133.writeToParcel(parcel557, 0);
        filterrule133.writeToParcel(parcel561, 0);
        filterrule133.clearPatterns();
        filterrule133.addPattern("123");
        filterrule133.getName();
        filterrule133.setName("my dummy rule");
        filterrule133.getDescription();
        filterrule133.setDescription("change me");
        filterrule133.equals(object52);
        filterrule133.getPatternKeys();
        filterrule133.getPatternKeys();
    }
}

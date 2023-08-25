package com.prismaqf.callblocker.rules;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test1732 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.NewFilterRuleTest#AfterUpdatingTheRulePatternsTheRuleShouldSaveWhenActivityIsLeft/Trace-1651091712732.txt
     * Method invocation under test: <com.prismaqf.callblocker.rules.FilterRule: void setName(java.lang.String)>_539_1074
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_rules_FilterRule_setName_003() throws Exception {
        java.lang.Object object47 = null;
        com.prismaqf.callblocker.rules.FilterRule filterrule128 = null;
        android.os.Parcel parcel441 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber519 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel442 = stubber519.when(parcel441);
        parcel442.writeString("my dummy rule");
        org.mockito.stubbing.Stubber stubber520 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel443 = stubber520.when(parcel441);
        parcel443.writeString("change me");
        org.mockito.stubbing.Stubber stubber521 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel444 = stubber521.when(parcel441);
        parcel444.writeInt(0);
        org.mockito.stubbing.Stubber stubber522 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel445 = stubber522.when(parcel441);
        parcel445.writeString("my dummy rule");
        org.mockito.stubbing.Stubber stubber523 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel446 = stubber523.when(parcel441);
        parcel446.writeString("change me");
        org.mockito.stubbing.Stubber stubber524 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel447 = stubber524.when(parcel441);
        parcel447.writeInt(0);
        android.os.Parcel parcel448 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber525 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel449 = stubber525.when(parcel448);
        parcel449.writeString("my dummy rule");
        org.mockito.stubbing.Stubber stubber526 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel450 = stubber526.when(parcel448);
        parcel450.writeString("change me");
        org.mockito.stubbing.Stubber stubber527 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel451 = stubber527.when(parcel448);
        parcel451.writeInt(0);
        filterrule128 = new com.prismaqf.callblocker.rules.FilterRule("dummy", "change me");
        filterrule128.getPatternKeys();
        filterrule128.getPatternKeys();
        filterrule128.getName();
        filterrule128.setName("dummy");
        filterrule128.getDescription();
        filterrule128.setDescription("change me");
        filterrule128.equals(object47);
        filterrule128.getPatternKeys();
        filterrule128.getPatternKeys();
        filterrule128.equals(object47);
        filterrule128.setName("my dummy rule");
        filterrule128.writeToParcel(parcel447, 0);
        filterrule128.writeToParcel(parcel447, 0);
        filterrule128.writeToParcel(parcel451, 0);
        filterrule128.clearPatterns();
        filterrule128.addPattern("123");
        filterrule128.getName();
        filterrule128.setName("my dummy rule");
    }
}

package com.prismaqf.callblocker.rules;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test1731 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.NewFilterRuleTest#AfterUpdatingTheRulePatternsTheRuleShouldSaveWhenActivityIsLeft/Trace-1651091712732.txt
     * Method invocation under test: <com.prismaqf.callblocker.rules.FilterRule: java.lang.String getName()>_536_1070
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_rules_FilterRule_getName_002() throws Exception {
        java.lang.Object object46 = null;
        com.prismaqf.callblocker.rules.FilterRule filterrule127 = null;
        android.os.Parcel parcel419 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber501 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel420 = stubber501.when(parcel419);
        parcel420.writeString("my dummy rule");
        org.mockito.stubbing.Stubber stubber502 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel421 = stubber502.when(parcel419);
        parcel421.writeString("change me");
        org.mockito.stubbing.Stubber stubber503 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel422 = stubber503.when(parcel419);
        parcel422.writeInt(0);
        org.mockito.stubbing.Stubber stubber504 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel423 = stubber504.when(parcel419);
        parcel423.writeString("my dummy rule");
        org.mockito.stubbing.Stubber stubber505 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel424 = stubber505.when(parcel419);
        parcel424.writeString("change me");
        org.mockito.stubbing.Stubber stubber506 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel425 = stubber506.when(parcel419);
        parcel425.writeInt(0);
        android.os.Parcel parcel426 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber507 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel427 = stubber507.when(parcel426);
        parcel427.writeString("my dummy rule");
        org.mockito.stubbing.Stubber stubber508 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel428 = stubber508.when(parcel426);
        parcel428.writeString("change me");
        org.mockito.stubbing.Stubber stubber509 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel429 = stubber509.when(parcel426);
        parcel429.writeInt(0);
        filterrule127 = new com.prismaqf.callblocker.rules.FilterRule("dummy", "change me");
        filterrule127.getPatternKeys();
        filterrule127.getPatternKeys();
        filterrule127.getName();
        filterrule127.setName("dummy");
        filterrule127.getDescription();
        filterrule127.setDescription("change me");
        filterrule127.equals(object46);
        filterrule127.getPatternKeys();
        filterrule127.getPatternKeys();
        filterrule127.equals(object46);
        filterrule127.setName("my dummy rule");
        filterrule127.writeToParcel(parcel425, 0);
        filterrule127.writeToParcel(parcel425, 0);
        filterrule127.writeToParcel(parcel429, 0);
        filterrule127.clearPatterns();
        filterrule127.addPattern("123");
        filterrule127.getName();
    }
}

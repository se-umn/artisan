package com.prismaqf.callblocker.rules;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test1733 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.NewFilterRuleTest#AfterUpdatingTheRulePatternsTheRuleShouldSaveWhenActivityIsLeft/Trace-1651091712732.txt
     * Method invocation under test: <com.prismaqf.callblocker.rules.FilterRule: java.lang.String getDescription()>_546_1090
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_rules_FilterRule_getDescription_002() throws Exception {
        java.lang.Object object48 = null;
        com.prismaqf.callblocker.rules.FilterRule filterrule129 = null;
        android.os.Parcel parcel463 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber537 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel464 = stubber537.when(parcel463);
        parcel464.writeString("my dummy rule");
        org.mockito.stubbing.Stubber stubber538 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel465 = stubber538.when(parcel463);
        parcel465.writeString("change me");
        org.mockito.stubbing.Stubber stubber539 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel466 = stubber539.when(parcel463);
        parcel466.writeInt(0);
        org.mockito.stubbing.Stubber stubber540 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel467 = stubber540.when(parcel463);
        parcel467.writeString("my dummy rule");
        org.mockito.stubbing.Stubber stubber541 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel468 = stubber541.when(parcel463);
        parcel468.writeString("change me");
        org.mockito.stubbing.Stubber stubber542 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel469 = stubber542.when(parcel463);
        parcel469.writeInt(0);
        android.os.Parcel parcel470 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber543 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel471 = stubber543.when(parcel470);
        parcel471.writeString("my dummy rule");
        org.mockito.stubbing.Stubber stubber544 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel472 = stubber544.when(parcel470);
        parcel472.writeString("change me");
        org.mockito.stubbing.Stubber stubber545 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel473 = stubber545.when(parcel470);
        parcel473.writeInt(0);
        filterrule129 = new com.prismaqf.callblocker.rules.FilterRule("dummy", "change me");
        filterrule129.getPatternKeys();
        filterrule129.getPatternKeys();
        filterrule129.getName();
        filterrule129.setName("dummy");
        filterrule129.getDescription();
        filterrule129.setDescription("change me");
        filterrule129.equals(object48);
        filterrule129.getPatternKeys();
        filterrule129.getPatternKeys();
        filterrule129.equals(object48);
        filterrule129.setName("my dummy rule");
        filterrule129.writeToParcel(parcel469, 0);
        filterrule129.writeToParcel(parcel469, 0);
        filterrule129.writeToParcel(parcel473, 0);
        filterrule129.clearPatterns();
        filterrule129.addPattern("123");
        filterrule129.getName();
        filterrule129.setName("my dummy rule");
        filterrule129.getDescription();
    }
}

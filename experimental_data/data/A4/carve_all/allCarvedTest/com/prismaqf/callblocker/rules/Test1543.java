package com.prismaqf.callblocker.rules;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test1543 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.NewFilterRuleTest#TestButtonFilterPatterns/Trace-1651091707269.txt
     * Method invocation under test: <com.prismaqf.callblocker.rules.FilterRule: void writeToParcel(android.os.Parcel,int)>_231_460
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_rules_FilterRule_writeToParcel_001() throws Exception {
        java.lang.Object object4 = null;
        com.prismaqf.callblocker.rules.FilterRule filterrule11 = null;
        android.os.Parcel parcel4 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber7 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel5 = stubber7.when(parcel4);
        parcel5.writeString("dummy");
        org.mockito.stubbing.Stubber stubber8 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel6 = stubber8.when(parcel4);
        parcel6.writeString("change me");
        org.mockito.stubbing.Stubber stubber9 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel7 = stubber9.when(parcel4);
        parcel7.writeInt(0);
        filterrule11 = new com.prismaqf.callblocker.rules.FilterRule("dummy", "change me");
        filterrule11.getPatternKeys();
        filterrule11.getPatternKeys();
        filterrule11.getName();
        filterrule11.setName("dummy");
        filterrule11.getDescription();
        filterrule11.setDescription("change me");
        filterrule11.equals(object4);
        filterrule11.getPatternKeys();
        filterrule11.getPatternKeys();
        filterrule11.equals(object4);
        filterrule11.writeToParcel(parcel7, 0);
    }
}

package com.prismaqf.callblocker.rules;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test1737 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.NewFilterRuleTest#AfterUpdatingTheRulePatternsTheRuleShouldSaveWhenActivityIsLeft/Trace-1651091712732.txt
     * Method invocation under test: <com.prismaqf.callblocker.rules.FilterRule: java.util.Set getPatternKeys()>_565_1127
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_rules_FilterRule_getPatternKeys_006() throws Exception {
        java.lang.Object object51 = null;
        com.prismaqf.callblocker.rules.FilterRule filterrule132 = null;
        android.os.Parcel parcel529 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber641 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel530 = stubber641.when(parcel529);
        parcel530.writeString("my dummy rule");
        org.mockito.stubbing.Stubber stubber642 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel531 = stubber642.when(parcel529);
        parcel531.writeString("change me");
        org.mockito.stubbing.Stubber stubber643 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel532 = stubber643.when(parcel529);
        parcel532.writeInt(0);
        org.mockito.stubbing.Stubber stubber644 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel533 = stubber644.when(parcel529);
        parcel533.writeString("my dummy rule");
        org.mockito.stubbing.Stubber stubber645 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel534 = stubber645.when(parcel529);
        parcel534.writeString("change me");
        org.mockito.stubbing.Stubber stubber646 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel535 = stubber646.when(parcel529);
        parcel535.writeInt(0);
        android.os.Parcel parcel536 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber647 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel537 = stubber647.when(parcel536);
        parcel537.writeString("my dummy rule");
        org.mockito.stubbing.Stubber stubber648 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel538 = stubber648.when(parcel536);
        parcel538.writeString("change me");
        org.mockito.stubbing.Stubber stubber649 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel539 = stubber649.when(parcel536);
        parcel539.writeInt(0);
        filterrule132 = new com.prismaqf.callblocker.rules.FilterRule("dummy", "change me");
        filterrule132.getPatternKeys();
        filterrule132.getPatternKeys();
        filterrule132.getName();
        filterrule132.setName("dummy");
        filterrule132.getDescription();
        filterrule132.setDescription("change me");
        filterrule132.equals(object51);
        filterrule132.getPatternKeys();
        filterrule132.getPatternKeys();
        filterrule132.equals(object51);
        filterrule132.setName("my dummy rule");
        filterrule132.writeToParcel(parcel535, 0);
        filterrule132.writeToParcel(parcel535, 0);
        filterrule132.writeToParcel(parcel539, 0);
        filterrule132.clearPatterns();
        filterrule132.addPattern("123");
        filterrule132.getName();
        filterrule132.setName("my dummy rule");
        filterrule132.getDescription();
        filterrule132.setDescription("change me");
        filterrule132.equals(object51);
        filterrule132.getPatternKeys();
    }
}

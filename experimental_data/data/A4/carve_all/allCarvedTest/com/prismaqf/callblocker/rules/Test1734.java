package com.prismaqf.callblocker.rules;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test1734 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.NewFilterRuleTest#AfterUpdatingTheRulePatternsTheRuleShouldSaveWhenActivityIsLeft/Trace-1651091712732.txt
     * Method invocation under test: <com.prismaqf.callblocker.rules.FilterRule: void setDescription(java.lang.String)>_553_1102
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_rules_FilterRule_setDescription_002() throws Exception {
        java.lang.Object object49 = null;
        com.prismaqf.callblocker.rules.FilterRule filterrule130 = null;
        android.os.Parcel parcel485 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber555 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel486 = stubber555.when(parcel485);
        parcel486.writeString("my dummy rule");
        org.mockito.stubbing.Stubber stubber556 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel487 = stubber556.when(parcel485);
        parcel487.writeString("change me");
        org.mockito.stubbing.Stubber stubber557 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel488 = stubber557.when(parcel485);
        parcel488.writeInt(0);
        org.mockito.stubbing.Stubber stubber558 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel489 = stubber558.when(parcel485);
        parcel489.writeString("my dummy rule");
        org.mockito.stubbing.Stubber stubber559 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel490 = stubber559.when(parcel485);
        parcel490.writeString("change me");
        org.mockito.stubbing.Stubber stubber560 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel491 = stubber560.when(parcel485);
        parcel491.writeInt(0);
        android.os.Parcel parcel492 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber561 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel493 = stubber561.when(parcel492);
        parcel493.writeString("my dummy rule");
        org.mockito.stubbing.Stubber stubber562 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel494 = stubber562.when(parcel492);
        parcel494.writeString("change me");
        org.mockito.stubbing.Stubber stubber563 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel495 = stubber563.when(parcel492);
        parcel495.writeInt(0);
        filterrule130 = new com.prismaqf.callblocker.rules.FilterRule("dummy", "change me");
        filterrule130.getPatternKeys();
        filterrule130.getPatternKeys();
        filterrule130.getName();
        filterrule130.setName("dummy");
        filterrule130.getDescription();
        filterrule130.setDescription("change me");
        filterrule130.equals(object49);
        filterrule130.getPatternKeys();
        filterrule130.getPatternKeys();
        filterrule130.equals(object49);
        filterrule130.setName("my dummy rule");
        filterrule130.writeToParcel(parcel491, 0);
        filterrule130.writeToParcel(parcel491, 0);
        filterrule130.writeToParcel(parcel495, 0);
        filterrule130.clearPatterns();
        filterrule130.addPattern("123");
        filterrule130.getName();
        filterrule130.setName("my dummy rule");
        filterrule130.getDescription();
        filterrule130.setDescription("change me");
    }
}

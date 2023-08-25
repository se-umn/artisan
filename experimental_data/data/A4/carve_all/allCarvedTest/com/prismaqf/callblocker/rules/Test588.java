package com.prismaqf.callblocker.rules;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test588 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.NewFilterRuleTest#TestDynamicStateOnRotation/Trace-1651091708855.txt
     * Method invocation under test: <com.prismaqf.callblocker.rules.FilterRule: java.util.Set getPatternKeys()>_585_1167
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_rules_FilterRule_getPatternKeys_006() throws Exception {
        com.prismaqf.callblocker.rules.FilterRule filterrule134 = null;
        java.lang.Object object53 = null;
        android.os.Parcel parcel595 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber727 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel596 = stubber727.when(parcel595);
        parcel596.writeString("Name");
        org.mockito.stubbing.Stubber stubber728 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel597 = stubber728.when(parcel595);
        parcel597.writeString("Description");
        org.mockito.stubbing.Stubber stubber729 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel598 = stubber729.when(parcel595);
        parcel598.writeInt(0);
        org.mockito.stubbing.Stubber stubber730 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel599 = stubber730.when(parcel595);
        parcel599.writeString("Name");
        org.mockito.stubbing.Stubber stubber731 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel600 = stubber731.when(parcel595);
        parcel600.writeString("Description");
        org.mockito.stubbing.Stubber stubber732 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel601 = stubber732.when(parcel595);
        parcel601.writeInt(0);
        android.os.Parcel parcel602 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber733 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel603 = stubber733.when(parcel602);
        parcel603.writeString("Name");
        org.mockito.stubbing.Stubber stubber734 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel604 = stubber734.when(parcel602);
        parcel604.writeString("Description");
        org.mockito.stubbing.Stubber stubber735 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel605 = stubber735.when(parcel602);
        parcel605.writeInt(0);
        org.mockito.stubbing.Stubber stubber736 = org.mockito.Mockito.doReturn("Name");
        android.os.Parcel parcel606 = stubber736.when(parcel602);
        parcel606.readString();
        org.mockito.stubbing.Stubber stubber737 = org.mockito.Mockito.doReturn("Description");
        android.os.Parcel parcel607 = stubber737.when(parcel602);
        parcel607.readString();
        org.mockito.stubbing.Stubber stubber738 = org.mockito.Mockito.doReturn(1);
        android.os.Parcel parcel608 = stubber738.when(parcel602);
        parcel608.readInt();
        org.mockito.stubbing.Stubber stubber739 = org.mockito.Mockito.doReturn("123*456");
        android.os.Parcel parcel609 = stubber739.when(parcel602);
        parcel609.readString();
        filterrule134 = new com.prismaqf.callblocker.rules.FilterRule("dummy", "change me");
        filterrule134.getPatternKeys();
        filterrule134.getPatternKeys();
        filterrule134.getName();
        filterrule134.setName("dummy");
        filterrule134.getDescription();
        filterrule134.setDescription("change me");
        filterrule134.equals(object53);
        filterrule134.getPatternKeys();
        filterrule134.getPatternKeys();
        filterrule134.equals(object53);
        filterrule134.setName("Name");
        filterrule134.setDescription("Description");
        filterrule134.equals(object53);
        filterrule134.writeToParcel(parcel601, 0);
        filterrule134.writeToParcel(parcel601, 0);
        filterrule134.writeToParcel(parcel609, 0);
        parcel609.readString();
        parcel609.readString();
        parcel609.readInt();
        parcel609.readString();
        filterrule134.clearPatterns();
        filterrule134.addPattern("123*456");
        filterrule134.getName();
        filterrule134.setName("Name");
        filterrule134.getDescription();
        filterrule134.setDescription("Description");
        filterrule134.equals(object53);
        filterrule134.getPatternKeys();
    }
}

package com.prismaqf.callblocker.rules;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test589 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.NewFilterRuleTest#TestDynamicStateOnRotation/Trace-1651091708855.txt
     * Method invocation under test: <com.prismaqf.callblocker.rules.FilterRule: java.util.Set getPatternKeys()>_590_1177
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_rules_FilterRule_getPatternKeys_007() throws Exception {
        com.prismaqf.callblocker.rules.FilterRule filterrule135 = null;
        java.lang.Object object54 = null;
        android.os.Parcel parcel625 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber753 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel626 = stubber753.when(parcel625);
        parcel626.writeString("Name");
        org.mockito.stubbing.Stubber stubber754 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel627 = stubber754.when(parcel625);
        parcel627.writeString("Description");
        org.mockito.stubbing.Stubber stubber755 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel628 = stubber755.when(parcel625);
        parcel628.writeInt(0);
        org.mockito.stubbing.Stubber stubber756 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel629 = stubber756.when(parcel625);
        parcel629.writeString("Name");
        org.mockito.stubbing.Stubber stubber757 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel630 = stubber757.when(parcel625);
        parcel630.writeString("Description");
        org.mockito.stubbing.Stubber stubber758 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel631 = stubber758.when(parcel625);
        parcel631.writeInt(0);
        android.os.Parcel parcel632 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber759 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel633 = stubber759.when(parcel632);
        parcel633.writeString("Name");
        org.mockito.stubbing.Stubber stubber760 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel634 = stubber760.when(parcel632);
        parcel634.writeString("Description");
        org.mockito.stubbing.Stubber stubber761 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel635 = stubber761.when(parcel632);
        parcel635.writeInt(0);
        org.mockito.stubbing.Stubber stubber762 = org.mockito.Mockito.doReturn("Name");
        android.os.Parcel parcel636 = stubber762.when(parcel632);
        parcel636.readString();
        org.mockito.stubbing.Stubber stubber763 = org.mockito.Mockito.doReturn("Description");
        android.os.Parcel parcel637 = stubber763.when(parcel632);
        parcel637.readString();
        org.mockito.stubbing.Stubber stubber764 = org.mockito.Mockito.doReturn(1);
        android.os.Parcel parcel638 = stubber764.when(parcel632);
        parcel638.readInt();
        org.mockito.stubbing.Stubber stubber765 = org.mockito.Mockito.doReturn("123*456");
        android.os.Parcel parcel639 = stubber765.when(parcel632);
        parcel639.readString();
        filterrule135 = new com.prismaqf.callblocker.rules.FilterRule("dummy", "change me");
        filterrule135.getPatternKeys();
        filterrule135.getPatternKeys();
        filterrule135.getName();
        filterrule135.setName("dummy");
        filterrule135.getDescription();
        filterrule135.setDescription("change me");
        filterrule135.equals(object54);
        filterrule135.getPatternKeys();
        filterrule135.getPatternKeys();
        filterrule135.equals(object54);
        filterrule135.setName("Name");
        filterrule135.setDescription("Description");
        filterrule135.equals(object54);
        filterrule135.writeToParcel(parcel631, 0);
        filterrule135.writeToParcel(parcel631, 0);
        filterrule135.writeToParcel(parcel639, 0);
        parcel639.readString();
        parcel639.readString();
        parcel639.readInt();
        parcel639.readString();
        filterrule135.clearPatterns();
        filterrule135.addPattern("123*456");
        filterrule135.getName();
        filterrule135.setName("Name");
        filterrule135.getDescription();
        filterrule135.setDescription("Description");
        filterrule135.equals(object54);
        filterrule135.getPatternKeys();
        filterrule135.getPatternKeys();
    }
}

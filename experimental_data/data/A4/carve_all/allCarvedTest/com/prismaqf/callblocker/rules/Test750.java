package com.prismaqf.callblocker.rules;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test750 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.ManageFilterPatternsTest#TestUpdate/Trace-1651091665345.txt
     * Method invocation under test: <com.prismaqf.callblocker.rules.FilterRule: boolean equals(java.lang.Object)>_499_994
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_rules_FilterRule_equals_004() throws Exception {
        com.prismaqf.callblocker.rules.FilterRule filterrule133 = null;
        java.lang.Object object52 = null;
        android.os.Parcel parcel655 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber757 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel656 = stubber757.when(parcel655);
        parcel656.writeString("dummy");
        org.mockito.stubbing.Stubber stubber758 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel657 = stubber758.when(parcel655);
        parcel657.writeString("change me");
        org.mockito.stubbing.Stubber stubber759 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel658 = stubber759.when(parcel655);
        parcel658.writeInt(0);
        org.mockito.stubbing.Stubber stubber760 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel659 = stubber760.when(parcel655);
        parcel659.writeString("dummy");
        org.mockito.stubbing.Stubber stubber761 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel660 = stubber761.when(parcel655);
        parcel660.writeString("change me");
        org.mockito.stubbing.Stubber stubber762 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel661 = stubber762.when(parcel655);
        parcel661.writeInt(0);
        android.os.Parcel parcel662 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber763 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel663 = stubber763.when(parcel662);
        parcel663.writeString("dummy");
        org.mockito.stubbing.Stubber stubber764 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel664 = stubber764.when(parcel662);
        parcel664.writeString("change me");
        org.mockito.stubbing.Stubber stubber765 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel665 = stubber765.when(parcel662);
        parcel665.writeInt(0);
        org.mockito.stubbing.Stubber stubber766 = org.mockito.Mockito.doReturn("dummy");
        android.os.Parcel parcel666 = stubber766.when(parcel662);
        parcel666.readString();
        org.mockito.stubbing.Stubber stubber767 = org.mockito.Mockito.doReturn("change me");
        android.os.Parcel parcel667 = stubber767.when(parcel662);
        parcel667.readString();
        org.mockito.stubbing.Stubber stubber768 = org.mockito.Mockito.doReturn(1);
        android.os.Parcel parcel668 = stubber768.when(parcel662);
        parcel668.readInt();
        org.mockito.stubbing.Stubber stubber769 = org.mockito.Mockito.doReturn("123*456");
        android.os.Parcel parcel669 = stubber769.when(parcel662);
        parcel669.readString();
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
        filterrule133.writeToParcel(parcel661, 0);
        filterrule133.writeToParcel(parcel661, 0);
        filterrule133.writeToParcel(parcel669, 0);
        parcel669.readString();
        parcel669.readString();
        parcel669.readInt();
        parcel669.readString();
        filterrule133.clearPatterns();
        filterrule133.addPattern("123*456");
        filterrule133.getName();
        filterrule133.setName("dummy");
        filterrule133.getDescription();
        filterrule133.setDescription("change me");
        filterrule133.equals(object52);
        filterrule133.getPatternKeys();
        filterrule133.getPatternKeys();
        filterrule133.equals(object52);
    }
}

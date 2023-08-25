package com.prismaqf.callblocker.rules;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test590 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.NewFilterRuleTest#TestDynamicStateOnRotation/Trace-1651091708855.txt
     * Method invocation under test: <com.prismaqf.callblocker.rules.FilterRule: boolean equals(java.lang.Object)>_599_1194
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_rules_FilterRule_equals_005() throws Exception {
        com.prismaqf.callblocker.rules.FilterRule filterrule136 = null;
        java.lang.Object object55 = null;
        android.os.Parcel parcel655 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber779 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel656 = stubber779.when(parcel655);
        parcel656.writeString("Name");
        org.mockito.stubbing.Stubber stubber780 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel657 = stubber780.when(parcel655);
        parcel657.writeString("Description");
        org.mockito.stubbing.Stubber stubber781 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel658 = stubber781.when(parcel655);
        parcel658.writeInt(0);
        org.mockito.stubbing.Stubber stubber782 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel659 = stubber782.when(parcel655);
        parcel659.writeString("Name");
        org.mockito.stubbing.Stubber stubber783 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel660 = stubber783.when(parcel655);
        parcel660.writeString("Description");
        org.mockito.stubbing.Stubber stubber784 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel661 = stubber784.when(parcel655);
        parcel661.writeInt(0);
        android.os.Parcel parcel662 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber785 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel663 = stubber785.when(parcel662);
        parcel663.writeString("Name");
        org.mockito.stubbing.Stubber stubber786 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel664 = stubber786.when(parcel662);
        parcel664.writeString("Description");
        org.mockito.stubbing.Stubber stubber787 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel665 = stubber787.when(parcel662);
        parcel665.writeInt(0);
        org.mockito.stubbing.Stubber stubber788 = org.mockito.Mockito.doReturn("Name");
        android.os.Parcel parcel666 = stubber788.when(parcel662);
        parcel666.readString();
        org.mockito.stubbing.Stubber stubber789 = org.mockito.Mockito.doReturn("Description");
        android.os.Parcel parcel667 = stubber789.when(parcel662);
        parcel667.readString();
        org.mockito.stubbing.Stubber stubber790 = org.mockito.Mockito.doReturn(1);
        android.os.Parcel parcel668 = stubber790.when(parcel662);
        parcel668.readInt();
        org.mockito.stubbing.Stubber stubber791 = org.mockito.Mockito.doReturn("123*456");
        android.os.Parcel parcel669 = stubber791.when(parcel662);
        parcel669.readString();
        filterrule136 = new com.prismaqf.callblocker.rules.FilterRule("dummy", "change me");
        filterrule136.getPatternKeys();
        filterrule136.getPatternKeys();
        filterrule136.getName();
        filterrule136.setName("dummy");
        filterrule136.getDescription();
        filterrule136.setDescription("change me");
        filterrule136.equals(object55);
        filterrule136.getPatternKeys();
        filterrule136.getPatternKeys();
        filterrule136.equals(object55);
        filterrule136.setName("Name");
        filterrule136.setDescription("Description");
        filterrule136.equals(object55);
        filterrule136.writeToParcel(parcel661, 0);
        filterrule136.writeToParcel(parcel661, 0);
        filterrule136.writeToParcel(parcel669, 0);
        parcel669.readString();
        parcel669.readString();
        parcel669.readInt();
        parcel669.readString();
        filterrule136.clearPatterns();
        filterrule136.addPattern("123*456");
        filterrule136.getName();
        filterrule136.setName("Name");
        filterrule136.getDescription();
        filterrule136.setDescription("Description");
        filterrule136.equals(object55);
        filterrule136.getPatternKeys();
        filterrule136.getPatternKeys();
        filterrule136.equals(object55);
    }
}

package com.prismaqf.callblocker.rules;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test599 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.NewFilterRuleTest#TestDynamicStateOnRotation/Trace-1651091708855.txt
     * Method invocation under test: <com.prismaqf.callblocker.rules.FilterRule: void setName(java.lang.String)>_726_1448
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_rules_FilterRule_setName_004() throws Exception {
        com.prismaqf.callblocker.rules.FilterRule filterrule183 = null;
        java.lang.Object object89 = null;
        android.os.Parcel parcel775 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber997 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel776 = stubber997.when(parcel775);
        parcel776.writeString("Name");
        org.mockito.stubbing.Stubber stubber998 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel777 = stubber998.when(parcel775);
        parcel777.writeString("Description");
        org.mockito.stubbing.Stubber stubber999 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel778 = stubber999.when(parcel775);
        parcel778.writeInt(0);
        org.mockito.stubbing.Stubber stubber1000 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel779 = stubber1000.when(parcel775);
        parcel779.writeString("Name");
        org.mockito.stubbing.Stubber stubber1001 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel780 = stubber1001.when(parcel775);
        parcel780.writeString("Description");
        org.mockito.stubbing.Stubber stubber1002 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel781 = stubber1002.when(parcel775);
        parcel781.writeInt(0);
        android.os.Parcel parcel782 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber1003 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel783 = stubber1003.when(parcel782);
        parcel783.writeString("Name");
        org.mockito.stubbing.Stubber stubber1004 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel784 = stubber1004.when(parcel782);
        parcel784.writeString("Description");
        org.mockito.stubbing.Stubber stubber1005 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel785 = stubber1005.when(parcel782);
        parcel785.writeInt(0);
        org.mockito.stubbing.Stubber stubber1006 = org.mockito.Mockito.doReturn("Name");
        android.os.Parcel parcel786 = stubber1006.when(parcel782);
        parcel786.readString();
        org.mockito.stubbing.Stubber stubber1007 = org.mockito.Mockito.doReturn("Description");
        android.os.Parcel parcel787 = stubber1007.when(parcel782);
        parcel787.readString();
        org.mockito.stubbing.Stubber stubber1008 = org.mockito.Mockito.doReturn(1);
        android.os.Parcel parcel788 = stubber1008.when(parcel782);
        parcel788.readInt();
        org.mockito.stubbing.Stubber stubber1009 = org.mockito.Mockito.doReturn("123*456");
        android.os.Parcel parcel789 = stubber1009.when(parcel782);
        parcel789.readString();
        filterrule183 = new com.prismaqf.callblocker.rules.FilterRule("dummy", "change me");
        filterrule183.getPatternKeys();
        filterrule183.getPatternKeys();
        filterrule183.getName();
        filterrule183.setName("dummy");
        filterrule183.getDescription();
        filterrule183.setDescription("change me");
        filterrule183.equals(object89);
        filterrule183.getPatternKeys();
        filterrule183.getPatternKeys();
        filterrule183.equals(object89);
        filterrule183.setName("Name");
        filterrule183.setDescription("Description");
        filterrule183.equals(object89);
        filterrule183.writeToParcel(parcel781, 0);
        filterrule183.writeToParcel(parcel781, 0);
        filterrule183.writeToParcel(parcel789, 0);
        parcel789.readString();
        parcel789.readString();
        parcel789.readInt();
        parcel789.readString();
        filterrule183.clearPatterns();
        filterrule183.addPattern("123*456");
        filterrule183.getName();
        filterrule183.setName("Name");
        filterrule183.getDescription();
        filterrule183.setDescription("Description");
        filterrule183.equals(object89);
        filterrule183.getPatternKeys();
        filterrule183.getPatternKeys();
        filterrule183.equals(object89);
        filterrule183.getName();
        filterrule183.setName("Name");
    }
}

package com.prismaqf.callblocker.rules;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test749 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.ManageFilterPatternsTest#TestUpdate/Trace-1651091665345.txt
     * Method invocation under test: <com.prismaqf.callblocker.rules.FilterRule: java.util.Set getPatternKeys()>_490_977
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_rules_FilterRule_getPatternKeys_007() throws Exception {
        com.prismaqf.callblocker.rules.FilterRule filterrule132 = null;
        java.lang.Object object51 = null;
        android.os.Parcel parcel625 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber731 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel626 = stubber731.when(parcel625);
        parcel626.writeString("dummy");
        org.mockito.stubbing.Stubber stubber732 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel627 = stubber732.when(parcel625);
        parcel627.writeString("change me");
        org.mockito.stubbing.Stubber stubber733 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel628 = stubber733.when(parcel625);
        parcel628.writeInt(0);
        org.mockito.stubbing.Stubber stubber734 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel629 = stubber734.when(parcel625);
        parcel629.writeString("dummy");
        org.mockito.stubbing.Stubber stubber735 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel630 = stubber735.when(parcel625);
        parcel630.writeString("change me");
        org.mockito.stubbing.Stubber stubber736 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel631 = stubber736.when(parcel625);
        parcel631.writeInt(0);
        android.os.Parcel parcel632 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber737 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel633 = stubber737.when(parcel632);
        parcel633.writeString("dummy");
        org.mockito.stubbing.Stubber stubber738 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel634 = stubber738.when(parcel632);
        parcel634.writeString("change me");
        org.mockito.stubbing.Stubber stubber739 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel635 = stubber739.when(parcel632);
        parcel635.writeInt(0);
        org.mockito.stubbing.Stubber stubber740 = org.mockito.Mockito.doReturn("dummy");
        android.os.Parcel parcel636 = stubber740.when(parcel632);
        parcel636.readString();
        org.mockito.stubbing.Stubber stubber741 = org.mockito.Mockito.doReturn("change me");
        android.os.Parcel parcel637 = stubber741.when(parcel632);
        parcel637.readString();
        org.mockito.stubbing.Stubber stubber742 = org.mockito.Mockito.doReturn(1);
        android.os.Parcel parcel638 = stubber742.when(parcel632);
        parcel638.readInt();
        org.mockito.stubbing.Stubber stubber743 = org.mockito.Mockito.doReturn("123*456");
        android.os.Parcel parcel639 = stubber743.when(parcel632);
        parcel639.readString();
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
        filterrule132.writeToParcel(parcel631, 0);
        filterrule132.writeToParcel(parcel631, 0);
        filterrule132.writeToParcel(parcel639, 0);
        parcel639.readString();
        parcel639.readString();
        parcel639.readInt();
        parcel639.readString();
        filterrule132.clearPatterns();
        filterrule132.addPattern("123*456");
        filterrule132.getName();
        filterrule132.setName("dummy");
        filterrule132.getDescription();
        filterrule132.setDescription("change me");
        filterrule132.equals(object51);
        filterrule132.getPatternKeys();
        filterrule132.getPatternKeys();
    }
}

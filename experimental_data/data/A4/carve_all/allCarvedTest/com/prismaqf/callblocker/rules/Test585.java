package com.prismaqf.callblocker.rules;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test585 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.NewFilterRuleTest#TestDynamicStateOnRotation/Trace-1651091708855.txt
     * Method invocation under test: <com.prismaqf.callblocker.rules.FilterRule: void setDescription(java.lang.String)>_573_1142
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_rules_FilterRule_setDescription_003() throws Exception {
        com.prismaqf.callblocker.rules.FilterRule filterrule132 = null;
        java.lang.Object object51 = null;
        android.os.Parcel parcel535 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber615 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel536 = stubber615.when(parcel535);
        parcel536.writeString("Name");
        org.mockito.stubbing.Stubber stubber616 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel537 = stubber616.when(parcel535);
        parcel537.writeString("Description");
        org.mockito.stubbing.Stubber stubber617 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel538 = stubber617.when(parcel535);
        parcel538.writeInt(0);
        org.mockito.stubbing.Stubber stubber618 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel539 = stubber618.when(parcel535);
        parcel539.writeString("Name");
        org.mockito.stubbing.Stubber stubber619 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel540 = stubber619.when(parcel535);
        parcel540.writeString("Description");
        org.mockito.stubbing.Stubber stubber620 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel541 = stubber620.when(parcel535);
        parcel541.writeInt(0);
        android.os.Parcel parcel542 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber621 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel543 = stubber621.when(parcel542);
        parcel543.writeString("Name");
        org.mockito.stubbing.Stubber stubber622 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel544 = stubber622.when(parcel542);
        parcel544.writeString("Description");
        org.mockito.stubbing.Stubber stubber623 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel545 = stubber623.when(parcel542);
        parcel545.writeInt(0);
        org.mockito.stubbing.Stubber stubber624 = org.mockito.Mockito.doReturn("Name");
        android.os.Parcel parcel546 = stubber624.when(parcel542);
        parcel546.readString();
        org.mockito.stubbing.Stubber stubber625 = org.mockito.Mockito.doReturn("Description");
        android.os.Parcel parcel547 = stubber625.when(parcel542);
        parcel547.readString();
        org.mockito.stubbing.Stubber stubber626 = org.mockito.Mockito.doReturn(1);
        android.os.Parcel parcel548 = stubber626.when(parcel542);
        parcel548.readInt();
        org.mockito.stubbing.Stubber stubber627 = org.mockito.Mockito.doReturn("123*456");
        android.os.Parcel parcel549 = stubber627.when(parcel542);
        parcel549.readString();
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
        filterrule132.setName("Name");
        filterrule132.setDescription("Description");
        filterrule132.equals(object51);
        filterrule132.writeToParcel(parcel541, 0);
        filterrule132.writeToParcel(parcel541, 0);
        filterrule132.writeToParcel(parcel549, 0);
        parcel549.readString();
        parcel549.readString();
        parcel549.readInt();
        parcel549.readString();
        filterrule132.clearPatterns();
        filterrule132.addPattern("123*456");
        filterrule132.getName();
        filterrule132.setName("Name");
        filterrule132.getDescription();
        filterrule132.setDescription("Description");
    }
}

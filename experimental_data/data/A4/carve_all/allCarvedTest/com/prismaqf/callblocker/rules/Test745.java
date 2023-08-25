package com.prismaqf.callblocker.rules;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test745 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.ManageFilterPatternsTest#TestUpdate/Trace-1651091665345.txt
     * Method invocation under test: <com.prismaqf.callblocker.rules.FilterRule: void setDescription(java.lang.String)>_473_942
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_rules_FilterRule_setDescription_002() throws Exception {
        com.prismaqf.callblocker.rules.FilterRule filterrule129 = null;
        java.lang.Object object48 = null;
        android.os.Parcel parcel535 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber603 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel536 = stubber603.when(parcel535);
        parcel536.writeString("dummy");
        org.mockito.stubbing.Stubber stubber604 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel537 = stubber604.when(parcel535);
        parcel537.writeString("change me");
        org.mockito.stubbing.Stubber stubber605 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel538 = stubber605.when(parcel535);
        parcel538.writeInt(0);
        org.mockito.stubbing.Stubber stubber606 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel539 = stubber606.when(parcel535);
        parcel539.writeString("dummy");
        org.mockito.stubbing.Stubber stubber607 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel540 = stubber607.when(parcel535);
        parcel540.writeString("change me");
        org.mockito.stubbing.Stubber stubber608 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel541 = stubber608.when(parcel535);
        parcel541.writeInt(0);
        android.os.Parcel parcel542 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber609 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel543 = stubber609.when(parcel542);
        parcel543.writeString("dummy");
        org.mockito.stubbing.Stubber stubber610 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel544 = stubber610.when(parcel542);
        parcel544.writeString("change me");
        org.mockito.stubbing.Stubber stubber611 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel545 = stubber611.when(parcel542);
        parcel545.writeInt(0);
        org.mockito.stubbing.Stubber stubber612 = org.mockito.Mockito.doReturn("dummy");
        android.os.Parcel parcel546 = stubber612.when(parcel542);
        parcel546.readString();
        org.mockito.stubbing.Stubber stubber613 = org.mockito.Mockito.doReturn("change me");
        android.os.Parcel parcel547 = stubber613.when(parcel542);
        parcel547.readString();
        org.mockito.stubbing.Stubber stubber614 = org.mockito.Mockito.doReturn(1);
        android.os.Parcel parcel548 = stubber614.when(parcel542);
        parcel548.readInt();
        org.mockito.stubbing.Stubber stubber615 = org.mockito.Mockito.doReturn("123*456");
        android.os.Parcel parcel549 = stubber615.when(parcel542);
        parcel549.readString();
        filterrule129 = new com.prismaqf.callblocker.rules.FilterRule("dummy", "change me");
        filterrule129.getPatternKeys();
        filterrule129.getPatternKeys();
        filterrule129.getName();
        filterrule129.setName("dummy");
        filterrule129.getDescription();
        filterrule129.setDescription("change me");
        filterrule129.equals(object48);
        filterrule129.getPatternKeys();
        filterrule129.getPatternKeys();
        filterrule129.equals(object48);
        filterrule129.writeToParcel(parcel541, 0);
        filterrule129.writeToParcel(parcel541, 0);
        filterrule129.writeToParcel(parcel549, 0);
        parcel549.readString();
        parcel549.readString();
        parcel549.readInt();
        parcel549.readString();
        filterrule129.clearPatterns();
        filterrule129.addPattern("123*456");
        filterrule129.getName();
        filterrule129.setName("dummy");
        filterrule129.getDescription();
        filterrule129.setDescription("change me");
    }
}

package com.prismaqf.callblocker.rules;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test747 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.ManageFilterPatternsTest#TestUpdate/Trace-1651091665345.txt
     * Method invocation under test: <com.prismaqf.callblocker.rules.FilterRule: boolean equals(java.lang.Object)>_476_947
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_rules_FilterRule_equals_003() throws Exception {
        com.prismaqf.callblocker.rules.FilterRule filterrule130 = null;
        java.lang.Object object49 = null;
        android.os.Parcel parcel565 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber679 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel566 = stubber679.when(parcel565);
        parcel566.writeString("dummy");
        org.mockito.stubbing.Stubber stubber680 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel567 = stubber680.when(parcel565);
        parcel567.writeString("change me");
        org.mockito.stubbing.Stubber stubber681 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel568 = stubber681.when(parcel565);
        parcel568.writeInt(0);
        org.mockito.stubbing.Stubber stubber682 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel569 = stubber682.when(parcel565);
        parcel569.writeString("dummy");
        org.mockito.stubbing.Stubber stubber683 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel570 = stubber683.when(parcel565);
        parcel570.writeString("change me");
        org.mockito.stubbing.Stubber stubber684 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel571 = stubber684.when(parcel565);
        parcel571.writeInt(0);
        android.os.Parcel parcel572 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber685 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel573 = stubber685.when(parcel572);
        parcel573.writeString("dummy");
        org.mockito.stubbing.Stubber stubber686 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel574 = stubber686.when(parcel572);
        parcel574.writeString("change me");
        org.mockito.stubbing.Stubber stubber687 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel575 = stubber687.when(parcel572);
        parcel575.writeInt(0);
        org.mockito.stubbing.Stubber stubber688 = org.mockito.Mockito.doReturn("dummy");
        android.os.Parcel parcel576 = stubber688.when(parcel572);
        parcel576.readString();
        org.mockito.stubbing.Stubber stubber689 = org.mockito.Mockito.doReturn("change me");
        android.os.Parcel parcel577 = stubber689.when(parcel572);
        parcel577.readString();
        org.mockito.stubbing.Stubber stubber690 = org.mockito.Mockito.doReturn(1);
        android.os.Parcel parcel578 = stubber690.when(parcel572);
        parcel578.readInt();
        org.mockito.stubbing.Stubber stubber691 = org.mockito.Mockito.doReturn("123*456");
        android.os.Parcel parcel579 = stubber691.when(parcel572);
        parcel579.readString();
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
        filterrule130.writeToParcel(parcel571, 0);
        filterrule130.writeToParcel(parcel571, 0);
        filterrule130.writeToParcel(parcel579, 0);
        parcel579.readString();
        parcel579.readString();
        parcel579.readInt();
        parcel579.readString();
        filterrule130.clearPatterns();
        filterrule130.addPattern("123*456");
        filterrule130.getName();
        filterrule130.setName("dummy");
        filterrule130.getDescription();
        filterrule130.setDescription("change me");
        filterrule130.equals(object49);
    }
}

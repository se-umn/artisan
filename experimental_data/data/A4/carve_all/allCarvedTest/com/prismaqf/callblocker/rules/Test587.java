package com.prismaqf.callblocker.rules;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test587 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.NewFilterRuleTest#TestDynamicStateOnRotation/Trace-1651091708855.txt
     * Method invocation under test: <com.prismaqf.callblocker.rules.FilterRule: boolean equals(java.lang.Object)>_576_1147
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_rules_FilterRule_equals_004() throws Exception {
        com.prismaqf.callblocker.rules.FilterRule filterrule133 = null;
        java.lang.Object object52 = null;
        android.os.Parcel parcel565 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber701 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel566 = stubber701.when(parcel565);
        parcel566.writeString("Name");
        org.mockito.stubbing.Stubber stubber702 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel567 = stubber702.when(parcel565);
        parcel567.writeString("Description");
        org.mockito.stubbing.Stubber stubber703 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel568 = stubber703.when(parcel565);
        parcel568.writeInt(0);
        org.mockito.stubbing.Stubber stubber704 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel569 = stubber704.when(parcel565);
        parcel569.writeString("Name");
        org.mockito.stubbing.Stubber stubber705 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel570 = stubber705.when(parcel565);
        parcel570.writeString("Description");
        org.mockito.stubbing.Stubber stubber706 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel571 = stubber706.when(parcel565);
        parcel571.writeInt(0);
        android.os.Parcel parcel572 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber707 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel573 = stubber707.when(parcel572);
        parcel573.writeString("Name");
        org.mockito.stubbing.Stubber stubber708 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel574 = stubber708.when(parcel572);
        parcel574.writeString("Description");
        org.mockito.stubbing.Stubber stubber709 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel575 = stubber709.when(parcel572);
        parcel575.writeInt(0);
        org.mockito.stubbing.Stubber stubber710 = org.mockito.Mockito.doReturn("Name");
        android.os.Parcel parcel576 = stubber710.when(parcel572);
        parcel576.readString();
        org.mockito.stubbing.Stubber stubber711 = org.mockito.Mockito.doReturn("Description");
        android.os.Parcel parcel577 = stubber711.when(parcel572);
        parcel577.readString();
        org.mockito.stubbing.Stubber stubber712 = org.mockito.Mockito.doReturn(1);
        android.os.Parcel parcel578 = stubber712.when(parcel572);
        parcel578.readInt();
        org.mockito.stubbing.Stubber stubber713 = org.mockito.Mockito.doReturn("123*456");
        android.os.Parcel parcel579 = stubber713.when(parcel572);
        parcel579.readString();
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
        filterrule133.setName("Name");
        filterrule133.setDescription("Description");
        filterrule133.equals(object52);
        filterrule133.writeToParcel(parcel571, 0);
        filterrule133.writeToParcel(parcel571, 0);
        filterrule133.writeToParcel(parcel579, 0);
        parcel579.readString();
        parcel579.readString();
        parcel579.readInt();
        parcel579.readString();
        filterrule133.clearPatterns();
        filterrule133.addPattern("123*456");
        filterrule133.getName();
        filterrule133.setName("Name");
        filterrule133.getDescription();
        filterrule133.setDescription("Description");
        filterrule133.equals(object52);
    }
}

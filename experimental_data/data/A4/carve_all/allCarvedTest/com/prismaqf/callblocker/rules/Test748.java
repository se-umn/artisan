package com.prismaqf.callblocker.rules;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test748 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.ManageFilterPatternsTest#TestUpdate/Trace-1651091665345.txt
     * Method invocation under test: <com.prismaqf.callblocker.rules.FilterRule: java.util.Set getPatternKeys()>_485_967
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_rules_FilterRule_getPatternKeys_006() throws Exception {
        com.prismaqf.callblocker.rules.FilterRule filterrule131 = null;
        java.lang.Object object50 = null;
        android.os.Parcel parcel595 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber705 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel596 = stubber705.when(parcel595);
        parcel596.writeString("dummy");
        org.mockito.stubbing.Stubber stubber706 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel597 = stubber706.when(parcel595);
        parcel597.writeString("change me");
        org.mockito.stubbing.Stubber stubber707 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel598 = stubber707.when(parcel595);
        parcel598.writeInt(0);
        org.mockito.stubbing.Stubber stubber708 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel599 = stubber708.when(parcel595);
        parcel599.writeString("dummy");
        org.mockito.stubbing.Stubber stubber709 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel600 = stubber709.when(parcel595);
        parcel600.writeString("change me");
        org.mockito.stubbing.Stubber stubber710 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel601 = stubber710.when(parcel595);
        parcel601.writeInt(0);
        android.os.Parcel parcel602 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber711 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel603 = stubber711.when(parcel602);
        parcel603.writeString("dummy");
        org.mockito.stubbing.Stubber stubber712 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel604 = stubber712.when(parcel602);
        parcel604.writeString("change me");
        org.mockito.stubbing.Stubber stubber713 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel605 = stubber713.when(parcel602);
        parcel605.writeInt(0);
        org.mockito.stubbing.Stubber stubber714 = org.mockito.Mockito.doReturn("dummy");
        android.os.Parcel parcel606 = stubber714.when(parcel602);
        parcel606.readString();
        org.mockito.stubbing.Stubber stubber715 = org.mockito.Mockito.doReturn("change me");
        android.os.Parcel parcel607 = stubber715.when(parcel602);
        parcel607.readString();
        org.mockito.stubbing.Stubber stubber716 = org.mockito.Mockito.doReturn(1);
        android.os.Parcel parcel608 = stubber716.when(parcel602);
        parcel608.readInt();
        org.mockito.stubbing.Stubber stubber717 = org.mockito.Mockito.doReturn("123*456");
        android.os.Parcel parcel609 = stubber717.when(parcel602);
        parcel609.readString();
        filterrule131 = new com.prismaqf.callblocker.rules.FilterRule("dummy", "change me");
        filterrule131.getPatternKeys();
        filterrule131.getPatternKeys();
        filterrule131.getName();
        filterrule131.setName("dummy");
        filterrule131.getDescription();
        filterrule131.setDescription("change me");
        filterrule131.equals(object50);
        filterrule131.getPatternKeys();
        filterrule131.getPatternKeys();
        filterrule131.equals(object50);
        filterrule131.writeToParcel(parcel601, 0);
        filterrule131.writeToParcel(parcel601, 0);
        filterrule131.writeToParcel(parcel609, 0);
        parcel609.readString();
        parcel609.readString();
        parcel609.readInt();
        parcel609.readString();
        filterrule131.clearPatterns();
        filterrule131.addPattern("123*456");
        filterrule131.getName();
        filterrule131.setName("dummy");
        filterrule131.getDescription();
        filterrule131.setDescription("change me");
        filterrule131.equals(object50);
        filterrule131.getPatternKeys();
    }
}

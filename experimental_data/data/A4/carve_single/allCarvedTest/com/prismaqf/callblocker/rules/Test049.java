package com.prismaqf.callblocker.rules;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test049 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /Users/mattia/Faculty/Research/2020_android_test_carving/repositories/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.ManageFilterPatternsTest#TestUpdate/Trace-1651091665345.txt
     * Method invocation under test: <com.prismaqf.callblocker.rules.FilterRule: void clearPatterns()>_430_859
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_rules_FilterRule_clearPatterns_001() throws Exception {
        com.prismaqf.callblocker.rules.FilterRule filterrule57 = null;
        java.lang.Object object26 = null;
        android.os.Parcel parcel109 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber159 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel110 = stubber159.when(parcel109);
        parcel110.writeString("dummy");
        org.mockito.stubbing.Stubber stubber160 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel111 = stubber160.when(parcel109);
        parcel111.writeString("change me");
        org.mockito.stubbing.Stubber stubber161 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel112 = stubber161.when(parcel109);
        parcel112.writeInt(0);
        org.mockito.stubbing.Stubber stubber162 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel113 = stubber162.when(parcel109);
        parcel113.writeString("dummy");
        org.mockito.stubbing.Stubber stubber163 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel114 = stubber163.when(parcel109);
        parcel114.writeString("change me");
        org.mockito.stubbing.Stubber stubber164 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel115 = stubber164.when(parcel109);
        parcel115.writeInt(0);
        android.os.Parcel parcel116 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber165 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel117 = stubber165.when(parcel116);
        parcel117.writeString("dummy");
        org.mockito.stubbing.Stubber stubber166 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel118 = stubber166.when(parcel116);
        parcel118.writeString("change me");
        org.mockito.stubbing.Stubber stubber167 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel119 = stubber167.when(parcel116);
        parcel119.writeInt(0);
        org.mockito.stubbing.Stubber stubber168 = org.mockito.Mockito.doReturn("dummy");
        android.os.Parcel parcel120 = stubber168.when(parcel116);
        parcel120.readString();
        org.mockito.stubbing.Stubber stubber169 = org.mockito.Mockito.doReturn("change me");
        android.os.Parcel parcel121 = stubber169.when(parcel116);
        parcel121.readString();
        org.mockito.stubbing.Stubber stubber170 = org.mockito.Mockito.doReturn(1);
        android.os.Parcel parcel122 = stubber170.when(parcel116);
        parcel122.readInt();
        org.mockito.stubbing.Stubber stubber171 = org.mockito.Mockito.doReturn("123*456");
        android.os.Parcel parcel123 = stubber171.when(parcel116);
        parcel123.readString();
        filterrule57 = new com.prismaqf.callblocker.rules.FilterRule("dummy", "change me");
        filterrule57.getPatternKeys();
        filterrule57.getPatternKeys();
        filterrule57.getName();
        filterrule57.setName("dummy");
        filterrule57.getDescription();
        filterrule57.setDescription("change me");
        filterrule57.equals(object26);
        filterrule57.getPatternKeys();
        filterrule57.getPatternKeys();
        filterrule57.equals(object26);
        filterrule57.writeToParcel(parcel115, 0);
        filterrule57.writeToParcel(parcel115, 0);
        filterrule57.writeToParcel(parcel123, 0);
        parcel123.readString();
        parcel123.readString();
        parcel123.readInt();
        parcel123.readString();
        filterrule57.clearPatterns();
    }
}

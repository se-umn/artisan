package com.prismaqf.callblocker.rules;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test817 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /Users/mattia/Faculty/Research/2020_android_test_carving/repositories/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.ManageFilterPatternsTest#TestCheckingPersistsWithRotation/Trace-1651091676947.txt
     * Method invocation under test: <com.prismaqf.callblocker.rules.FilterRule: void writeToParcel(android.os.Parcel,int)>_155_308
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_rules_FilterRule_writeToParcel_001() throws Exception {
        java.lang.Object object1 = null;
        com.prismaqf.callblocker.rules.FilterRule filterrule7 = null;
        android.os.Parcel parcel4 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber7 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel5 = stubber7.when(parcel4);
        parcel5.writeString("dummy");
        org.mockito.stubbing.Stubber stubber8 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel6 = stubber8.when(parcel4);
        parcel6.writeString("change me");
        org.mockito.stubbing.Stubber stubber9 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel7 = stubber9.when(parcel4);
        parcel7.writeInt(0);
        filterrule7 = new com.prismaqf.callblocker.rules.FilterRule("dummy", "change me");
        filterrule7.getPatternKeys();
        filterrule7.getPatternKeys();
        filterrule7.getName();
        filterrule7.setName("dummy");
        filterrule7.getDescription();
        filterrule7.setDescription("change me");
        filterrule7.equals(object1);
        filterrule7.getPatternKeys();
        filterrule7.getPatternKeys();
        filterrule7.equals(object1);
        filterrule7.writeToParcel(parcel7, 0);
    }
}

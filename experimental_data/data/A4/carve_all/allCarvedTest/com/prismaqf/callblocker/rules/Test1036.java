package com.prismaqf.callblocker.rules;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test1036 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.ManageFilterPatternsTest#TestDeleteChecked/Trace-1651091672045.txt
     * Method invocation under test: <com.prismaqf.callblocker.rules.FilterRule: void writeToParcel(android.os.Parcel,int)>_163_324
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_rules_FilterRule_writeToParcel_002() throws Exception {
        java.lang.Object object5 = null;
        com.prismaqf.callblocker.rules.FilterRule filterrule12 = null;
        android.os.Parcel parcel15 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber16 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel16 = stubber16.when(parcel15);
        parcel16.writeString("dummy");
        org.mockito.stubbing.Stubber stubber17 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel17 = stubber17.when(parcel15);
        parcel17.writeString("change me");
        org.mockito.stubbing.Stubber stubber18 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel18 = stubber18.when(parcel15);
        parcel18.writeInt(0);
        org.mockito.stubbing.Stubber stubber19 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel19 = stubber19.when(parcel15);
        parcel19.writeString("dummy");
        org.mockito.stubbing.Stubber stubber20 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel20 = stubber20.when(parcel15);
        parcel20.writeString("change me");
        org.mockito.stubbing.Stubber stubber21 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel21 = stubber21.when(parcel15);
        parcel21.writeInt(0);
        filterrule12 = new com.prismaqf.callblocker.rules.FilterRule("dummy", "change me");
        filterrule12.getPatternKeys();
        filterrule12.getPatternKeys();
        filterrule12.getName();
        filterrule12.setName("dummy");
        filterrule12.getDescription();
        filterrule12.setDescription("change me");
        filterrule12.equals(object5);
        filterrule12.getPatternKeys();
        filterrule12.getPatternKeys();
        filterrule12.equals(object5);
        filterrule12.writeToParcel(parcel21, 0);
        filterrule12.writeToParcel(parcel21, 0);
    }
}

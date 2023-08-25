package com.prismaqf.callblocker.rules;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test742 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.ManageFilterPatternsTest#TestUpdate/Trace-1651091665345.txt
     * Method invocation under test: <com.prismaqf.callblocker.rules.FilterRule: java.lang.String getName()>_458_914
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_rules_FilterRule_getName_002() throws Exception {
        com.prismaqf.callblocker.rules.FilterRule filterrule126 = null;
        java.lang.Object object45 = null;
        android.os.Parcel parcel445 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber525 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel446 = stubber525.when(parcel445);
        parcel446.writeString("dummy");
        org.mockito.stubbing.Stubber stubber526 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel447 = stubber526.when(parcel445);
        parcel447.writeString("change me");
        org.mockito.stubbing.Stubber stubber527 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel448 = stubber527.when(parcel445);
        parcel448.writeInt(0);
        org.mockito.stubbing.Stubber stubber528 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel449 = stubber528.when(parcel445);
        parcel449.writeString("dummy");
        org.mockito.stubbing.Stubber stubber529 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel450 = stubber529.when(parcel445);
        parcel450.writeString("change me");
        org.mockito.stubbing.Stubber stubber530 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel451 = stubber530.when(parcel445);
        parcel451.writeInt(0);
        android.os.Parcel parcel452 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber531 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel453 = stubber531.when(parcel452);
        parcel453.writeString("dummy");
        org.mockito.stubbing.Stubber stubber532 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel454 = stubber532.when(parcel452);
        parcel454.writeString("change me");
        org.mockito.stubbing.Stubber stubber533 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel455 = stubber533.when(parcel452);
        parcel455.writeInt(0);
        org.mockito.stubbing.Stubber stubber534 = org.mockito.Mockito.doReturn("dummy");
        android.os.Parcel parcel456 = stubber534.when(parcel452);
        parcel456.readString();
        org.mockito.stubbing.Stubber stubber535 = org.mockito.Mockito.doReturn("change me");
        android.os.Parcel parcel457 = stubber535.when(parcel452);
        parcel457.readString();
        org.mockito.stubbing.Stubber stubber536 = org.mockito.Mockito.doReturn(1);
        android.os.Parcel parcel458 = stubber536.when(parcel452);
        parcel458.readInt();
        org.mockito.stubbing.Stubber stubber537 = org.mockito.Mockito.doReturn("123*456");
        android.os.Parcel parcel459 = stubber537.when(parcel452);
        parcel459.readString();
        filterrule126 = new com.prismaqf.callblocker.rules.FilterRule("dummy", "change me");
        filterrule126.getPatternKeys();
        filterrule126.getPatternKeys();
        filterrule126.getName();
        filterrule126.setName("dummy");
        filterrule126.getDescription();
        filterrule126.setDescription("change me");
        filterrule126.equals(object45);
        filterrule126.getPatternKeys();
        filterrule126.getPatternKeys();
        filterrule126.equals(object45);
        filterrule126.writeToParcel(parcel451, 0);
        filterrule126.writeToParcel(parcel451, 0);
        filterrule126.writeToParcel(parcel459, 0);
        parcel459.readString();
        parcel459.readString();
        parcel459.readInt();
        parcel459.readString();
        filterrule126.clearPatterns();
        filterrule126.addPattern("123*456");
        filterrule126.getName();
    }
}

package com.prismaqf.callblocker.rules;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test582 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.NewFilterRuleTest#TestDynamicStateOnRotation/Trace-1651091708855.txt
     * Method invocation under test: <com.prismaqf.callblocker.rules.FilterRule: java.lang.String getName()>_556_1110
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_rules_FilterRule_getName_002() throws Exception {
        com.prismaqf.callblocker.rules.FilterRule filterrule129 = null;
        java.lang.Object object48 = null;
        android.os.Parcel parcel445 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber537 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel446 = stubber537.when(parcel445);
        parcel446.writeString("Name");
        org.mockito.stubbing.Stubber stubber538 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel447 = stubber538.when(parcel445);
        parcel447.writeString("Description");
        org.mockito.stubbing.Stubber stubber539 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel448 = stubber539.when(parcel445);
        parcel448.writeInt(0);
        org.mockito.stubbing.Stubber stubber540 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel449 = stubber540.when(parcel445);
        parcel449.writeString("Name");
        org.mockito.stubbing.Stubber stubber541 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel450 = stubber541.when(parcel445);
        parcel450.writeString("Description");
        org.mockito.stubbing.Stubber stubber542 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel451 = stubber542.when(parcel445);
        parcel451.writeInt(0);
        android.os.Parcel parcel452 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber543 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel453 = stubber543.when(parcel452);
        parcel453.writeString("Name");
        org.mockito.stubbing.Stubber stubber544 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel454 = stubber544.when(parcel452);
        parcel454.writeString("Description");
        org.mockito.stubbing.Stubber stubber545 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel455 = stubber545.when(parcel452);
        parcel455.writeInt(0);
        org.mockito.stubbing.Stubber stubber546 = org.mockito.Mockito.doReturn("Name");
        android.os.Parcel parcel456 = stubber546.when(parcel452);
        parcel456.readString();
        org.mockito.stubbing.Stubber stubber547 = org.mockito.Mockito.doReturn("Description");
        android.os.Parcel parcel457 = stubber547.when(parcel452);
        parcel457.readString();
        org.mockito.stubbing.Stubber stubber548 = org.mockito.Mockito.doReturn(1);
        android.os.Parcel parcel458 = stubber548.when(parcel452);
        parcel458.readInt();
        org.mockito.stubbing.Stubber stubber549 = org.mockito.Mockito.doReturn("123*456");
        android.os.Parcel parcel459 = stubber549.when(parcel452);
        parcel459.readString();
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
        filterrule129.setName("Name");
        filterrule129.setDescription("Description");
        filterrule129.equals(object48);
        filterrule129.writeToParcel(parcel451, 0);
        filterrule129.writeToParcel(parcel451, 0);
        filterrule129.writeToParcel(parcel459, 0);
        parcel459.readString();
        parcel459.readString();
        parcel459.readInt();
        parcel459.readString();
        filterrule129.clearPatterns();
        filterrule129.addPattern("123*456");
        filterrule129.getName();
    }
}

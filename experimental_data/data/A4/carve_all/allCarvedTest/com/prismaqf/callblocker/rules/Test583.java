package com.prismaqf.callblocker.rules;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test583 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.NewFilterRuleTest#TestDynamicStateOnRotation/Trace-1651091708855.txt
     * Method invocation under test: <com.prismaqf.callblocker.rules.FilterRule: void setName(java.lang.String)>_559_1114
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_rules_FilterRule_setName_003() throws Exception {
        com.prismaqf.callblocker.rules.FilterRule filterrule130 = null;
        java.lang.Object object49 = null;
        android.os.Parcel parcel475 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber563 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel476 = stubber563.when(parcel475);
        parcel476.writeString("Name");
        org.mockito.stubbing.Stubber stubber564 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel477 = stubber564.when(parcel475);
        parcel477.writeString("Description");
        org.mockito.stubbing.Stubber stubber565 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel478 = stubber565.when(parcel475);
        parcel478.writeInt(0);
        org.mockito.stubbing.Stubber stubber566 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel479 = stubber566.when(parcel475);
        parcel479.writeString("Name");
        org.mockito.stubbing.Stubber stubber567 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel480 = stubber567.when(parcel475);
        parcel480.writeString("Description");
        org.mockito.stubbing.Stubber stubber568 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel481 = stubber568.when(parcel475);
        parcel481.writeInt(0);
        android.os.Parcel parcel482 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber569 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel483 = stubber569.when(parcel482);
        parcel483.writeString("Name");
        org.mockito.stubbing.Stubber stubber570 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel484 = stubber570.when(parcel482);
        parcel484.writeString("Description");
        org.mockito.stubbing.Stubber stubber571 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel485 = stubber571.when(parcel482);
        parcel485.writeInt(0);
        org.mockito.stubbing.Stubber stubber572 = org.mockito.Mockito.doReturn("Name");
        android.os.Parcel parcel486 = stubber572.when(parcel482);
        parcel486.readString();
        org.mockito.stubbing.Stubber stubber573 = org.mockito.Mockito.doReturn("Description");
        android.os.Parcel parcel487 = stubber573.when(parcel482);
        parcel487.readString();
        org.mockito.stubbing.Stubber stubber574 = org.mockito.Mockito.doReturn(1);
        android.os.Parcel parcel488 = stubber574.when(parcel482);
        parcel488.readInt();
        org.mockito.stubbing.Stubber stubber575 = org.mockito.Mockito.doReturn("123*456");
        android.os.Parcel parcel489 = stubber575.when(parcel482);
        parcel489.readString();
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
        filterrule130.setName("Name");
        filterrule130.setDescription("Description");
        filterrule130.equals(object49);
        filterrule130.writeToParcel(parcel481, 0);
        filterrule130.writeToParcel(parcel481, 0);
        filterrule130.writeToParcel(parcel489, 0);
        parcel489.readString();
        parcel489.readString();
        parcel489.readInt();
        parcel489.readString();
        filterrule130.clearPatterns();
        filterrule130.addPattern("123*456");
        filterrule130.getName();
        filterrule130.setName("Name");
    }
}

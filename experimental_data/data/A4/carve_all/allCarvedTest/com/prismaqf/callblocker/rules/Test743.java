package com.prismaqf.callblocker.rules;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test743 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.ManageFilterPatternsTest#TestUpdate/Trace-1651091665345.txt
     * Method invocation under test: <com.prismaqf.callblocker.rules.FilterRule: void setName(java.lang.String)>_461_918
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_rules_FilterRule_setName_002() throws Exception {
        com.prismaqf.callblocker.rules.FilterRule filterrule127 = null;
        java.lang.Object object46 = null;
        android.os.Parcel parcel475 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber551 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel476 = stubber551.when(parcel475);
        parcel476.writeString("dummy");
        org.mockito.stubbing.Stubber stubber552 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel477 = stubber552.when(parcel475);
        parcel477.writeString("change me");
        org.mockito.stubbing.Stubber stubber553 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel478 = stubber553.when(parcel475);
        parcel478.writeInt(0);
        org.mockito.stubbing.Stubber stubber554 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel479 = stubber554.when(parcel475);
        parcel479.writeString("dummy");
        org.mockito.stubbing.Stubber stubber555 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel480 = stubber555.when(parcel475);
        parcel480.writeString("change me");
        org.mockito.stubbing.Stubber stubber556 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel481 = stubber556.when(parcel475);
        parcel481.writeInt(0);
        android.os.Parcel parcel482 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber557 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel483 = stubber557.when(parcel482);
        parcel483.writeString("dummy");
        org.mockito.stubbing.Stubber stubber558 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel484 = stubber558.when(parcel482);
        parcel484.writeString("change me");
        org.mockito.stubbing.Stubber stubber559 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel485 = stubber559.when(parcel482);
        parcel485.writeInt(0);
        org.mockito.stubbing.Stubber stubber560 = org.mockito.Mockito.doReturn("dummy");
        android.os.Parcel parcel486 = stubber560.when(parcel482);
        parcel486.readString();
        org.mockito.stubbing.Stubber stubber561 = org.mockito.Mockito.doReturn("change me");
        android.os.Parcel parcel487 = stubber561.when(parcel482);
        parcel487.readString();
        org.mockito.stubbing.Stubber stubber562 = org.mockito.Mockito.doReturn(1);
        android.os.Parcel parcel488 = stubber562.when(parcel482);
        parcel488.readInt();
        org.mockito.stubbing.Stubber stubber563 = org.mockito.Mockito.doReturn("123*456");
        android.os.Parcel parcel489 = stubber563.when(parcel482);
        parcel489.readString();
        filterrule127 = new com.prismaqf.callblocker.rules.FilterRule("dummy", "change me");
        filterrule127.getPatternKeys();
        filterrule127.getPatternKeys();
        filterrule127.getName();
        filterrule127.setName("dummy");
        filterrule127.getDescription();
        filterrule127.setDescription("change me");
        filterrule127.equals(object46);
        filterrule127.getPatternKeys();
        filterrule127.getPatternKeys();
        filterrule127.equals(object46);
        filterrule127.writeToParcel(parcel481, 0);
        filterrule127.writeToParcel(parcel481, 0);
        filterrule127.writeToParcel(parcel489, 0);
        parcel489.readString();
        parcel489.readString();
        parcel489.readInt();
        parcel489.readString();
        filterrule127.clearPatterns();
        filterrule127.addPattern("123*456");
        filterrule127.getName();
        filterrule127.setName("dummy");
    }
}

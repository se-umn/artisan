package com.prismaqf.callblocker.rules;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test617 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.NewFilterRuleTest#TestDynamicStateOnRotation/Trace-1651091708855.txt
     * Method invocation under test: <com.prismaqf.callblocker.rules.FilterRule: boolean equals(java.lang.Object)>_910_1815
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_rules_FilterRule_equals_008() throws Exception {
        com.prismaqf.callblocker.rules.FilterRule filterrule202 = null;
        java.lang.Object object103 = null;
        android.os.Parcel parcel1195 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber1421 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel1196 = stubber1421.when(parcel1195);
        parcel1196.writeString("Name");
        org.mockito.stubbing.Stubber stubber1422 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel1197 = stubber1422.when(parcel1195);
        parcel1197.writeString("Description");
        org.mockito.stubbing.Stubber stubber1423 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel1198 = stubber1423.when(parcel1195);
        parcel1198.writeInt(0);
        org.mockito.stubbing.Stubber stubber1424 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel1199 = stubber1424.when(parcel1195);
        parcel1199.writeString("Name");
        org.mockito.stubbing.Stubber stubber1425 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel1200 = stubber1425.when(parcel1195);
        parcel1200.writeString("Description");
        org.mockito.stubbing.Stubber stubber1426 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel1201 = stubber1426.when(parcel1195);
        parcel1201.writeInt(0);
        android.os.Parcel parcel1202 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber1427 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel1203 = stubber1427.when(parcel1202);
        parcel1203.writeString("Name");
        org.mockito.stubbing.Stubber stubber1428 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel1204 = stubber1428.when(parcel1202);
        parcel1204.writeString("Description");
        org.mockito.stubbing.Stubber stubber1429 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel1205 = stubber1429.when(parcel1202);
        parcel1205.writeInt(0);
        org.mockito.stubbing.Stubber stubber1430 = org.mockito.Mockito.doReturn("Name");
        android.os.Parcel parcel1206 = stubber1430.when(parcel1202);
        parcel1206.readString();
        org.mockito.stubbing.Stubber stubber1431 = org.mockito.Mockito.doReturn("Description");
        android.os.Parcel parcel1207 = stubber1431.when(parcel1202);
        parcel1207.readString();
        org.mockito.stubbing.Stubber stubber1432 = org.mockito.Mockito.doReturn(1);
        android.os.Parcel parcel1208 = stubber1432.when(parcel1202);
        parcel1208.readInt();
        org.mockito.stubbing.Stubber stubber1433 = org.mockito.Mockito.doReturn("123*456");
        android.os.Parcel parcel1209 = stubber1433.when(parcel1202);
        parcel1209.readString();
        filterrule202 = new com.prismaqf.callblocker.rules.FilterRule("dummy", "change me");
        filterrule202.getPatternKeys();
        filterrule202.getPatternKeys();
        filterrule202.getName();
        filterrule202.setName("dummy");
        filterrule202.getDescription();
        filterrule202.setDescription("change me");
        filterrule202.equals(object103);
        filterrule202.getPatternKeys();
        filterrule202.getPatternKeys();
        filterrule202.equals(object103);
        filterrule202.setName("Name");
        filterrule202.setDescription("Description");
        filterrule202.equals(object103);
        filterrule202.writeToParcel(parcel1201, 0);
        filterrule202.writeToParcel(parcel1201, 0);
        filterrule202.writeToParcel(parcel1209, 0);
        parcel1209.readString();
        parcel1209.readString();
        parcel1209.readInt();
        parcel1209.readString();
        filterrule202.clearPatterns();
        filterrule202.addPattern("123*456");
        filterrule202.getName();
        filterrule202.setName("Name");
        filterrule202.getDescription();
        filterrule202.setDescription("Description");
        filterrule202.equals(object103);
        filterrule202.getPatternKeys();
        filterrule202.getPatternKeys();
        filterrule202.equals(object103);
        filterrule202.getName();
        filterrule202.setName("Name");
        filterrule202.getDescription();
        filterrule202.setDescription("Description");
        filterrule202.equals(object103);
        filterrule202.getPatternKeys();
        filterrule202.getPatternKeys();
        filterrule202.equals(object103);
        filterrule202.getName();
        filterrule202.setName("Name");
        filterrule202.getDescription();
        filterrule202.setDescription("Description");
        filterrule202.equals(object103);
    }
}

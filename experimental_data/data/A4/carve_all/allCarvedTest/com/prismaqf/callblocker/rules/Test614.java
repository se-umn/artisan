package com.prismaqf.callblocker.rules;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test614 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.NewFilterRuleTest#TestDynamicStateOnRotation/Trace-1651091708855.txt
     * Method invocation under test: <com.prismaqf.callblocker.rules.FilterRule: void setName(java.lang.String)>_893_1782
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_rules_FilterRule_setName_005() throws Exception {
        com.prismaqf.callblocker.rules.FilterRule filterrule199 = null;
        java.lang.Object object100 = null;
        android.os.Parcel parcel1105 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber1343 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel1106 = stubber1343.when(parcel1105);
        parcel1106.writeString("Name");
        org.mockito.stubbing.Stubber stubber1344 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel1107 = stubber1344.when(parcel1105);
        parcel1107.writeString("Description");
        org.mockito.stubbing.Stubber stubber1345 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel1108 = stubber1345.when(parcel1105);
        parcel1108.writeInt(0);
        org.mockito.stubbing.Stubber stubber1346 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel1109 = stubber1346.when(parcel1105);
        parcel1109.writeString("Name");
        org.mockito.stubbing.Stubber stubber1347 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel1110 = stubber1347.when(parcel1105);
        parcel1110.writeString("Description");
        org.mockito.stubbing.Stubber stubber1348 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel1111 = stubber1348.when(parcel1105);
        parcel1111.writeInt(0);
        android.os.Parcel parcel1112 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber1349 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel1113 = stubber1349.when(parcel1112);
        parcel1113.writeString("Name");
        org.mockito.stubbing.Stubber stubber1350 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel1114 = stubber1350.when(parcel1112);
        parcel1114.writeString("Description");
        org.mockito.stubbing.Stubber stubber1351 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel1115 = stubber1351.when(parcel1112);
        parcel1115.writeInt(0);
        org.mockito.stubbing.Stubber stubber1352 = org.mockito.Mockito.doReturn("Name");
        android.os.Parcel parcel1116 = stubber1352.when(parcel1112);
        parcel1116.readString();
        org.mockito.stubbing.Stubber stubber1353 = org.mockito.Mockito.doReturn("Description");
        android.os.Parcel parcel1117 = stubber1353.when(parcel1112);
        parcel1117.readString();
        org.mockito.stubbing.Stubber stubber1354 = org.mockito.Mockito.doReturn(1);
        android.os.Parcel parcel1118 = stubber1354.when(parcel1112);
        parcel1118.readInt();
        org.mockito.stubbing.Stubber stubber1355 = org.mockito.Mockito.doReturn("123*456");
        android.os.Parcel parcel1119 = stubber1355.when(parcel1112);
        parcel1119.readString();
        filterrule199 = new com.prismaqf.callblocker.rules.FilterRule("dummy", "change me");
        filterrule199.getPatternKeys();
        filterrule199.getPatternKeys();
        filterrule199.getName();
        filterrule199.setName("dummy");
        filterrule199.getDescription();
        filterrule199.setDescription("change me");
        filterrule199.equals(object100);
        filterrule199.getPatternKeys();
        filterrule199.getPatternKeys();
        filterrule199.equals(object100);
        filterrule199.setName("Name");
        filterrule199.setDescription("Description");
        filterrule199.equals(object100);
        filterrule199.writeToParcel(parcel1111, 0);
        filterrule199.writeToParcel(parcel1111, 0);
        filterrule199.writeToParcel(parcel1119, 0);
        parcel1119.readString();
        parcel1119.readString();
        parcel1119.readInt();
        parcel1119.readString();
        filterrule199.clearPatterns();
        filterrule199.addPattern("123*456");
        filterrule199.getName();
        filterrule199.setName("Name");
        filterrule199.getDescription();
        filterrule199.setDescription("Description");
        filterrule199.equals(object100);
        filterrule199.getPatternKeys();
        filterrule199.getPatternKeys();
        filterrule199.equals(object100);
        filterrule199.getName();
        filterrule199.setName("Name");
        filterrule199.getDescription();
        filterrule199.setDescription("Description");
        filterrule199.equals(object100);
        filterrule199.getPatternKeys();
        filterrule199.getPatternKeys();
        filterrule199.equals(object100);
        filterrule199.getName();
        filterrule199.setName("Name");
    }
}

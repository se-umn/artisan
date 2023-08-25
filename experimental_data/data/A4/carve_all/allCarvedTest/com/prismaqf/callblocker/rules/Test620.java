package com.prismaqf.callblocker.rules;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test620 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.NewFilterRuleTest#TestDynamicStateOnRotation/Trace-1651091708855.txt
     * Method invocation under test: <com.prismaqf.callblocker.rules.FilterRule: boolean equals(java.lang.Object)>_933_1862
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_rules_FilterRule_equals_009() throws Exception {
        com.prismaqf.callblocker.rules.FilterRule filterrule205 = null;
        java.lang.Object object106 = null;
        android.os.Parcel parcel1285 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber1499 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel1286 = stubber1499.when(parcel1285);
        parcel1286.writeString("Name");
        org.mockito.stubbing.Stubber stubber1500 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel1287 = stubber1500.when(parcel1285);
        parcel1287.writeString("Description");
        org.mockito.stubbing.Stubber stubber1501 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel1288 = stubber1501.when(parcel1285);
        parcel1288.writeInt(0);
        org.mockito.stubbing.Stubber stubber1502 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel1289 = stubber1502.when(parcel1285);
        parcel1289.writeString("Name");
        org.mockito.stubbing.Stubber stubber1503 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel1290 = stubber1503.when(parcel1285);
        parcel1290.writeString("Description");
        org.mockito.stubbing.Stubber stubber1504 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel1291 = stubber1504.when(parcel1285);
        parcel1291.writeInt(0);
        android.os.Parcel parcel1292 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber1505 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel1293 = stubber1505.when(parcel1292);
        parcel1293.writeString("Name");
        org.mockito.stubbing.Stubber stubber1506 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel1294 = stubber1506.when(parcel1292);
        parcel1294.writeString("Description");
        org.mockito.stubbing.Stubber stubber1507 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel1295 = stubber1507.when(parcel1292);
        parcel1295.writeInt(0);
        org.mockito.stubbing.Stubber stubber1508 = org.mockito.Mockito.doReturn("Name");
        android.os.Parcel parcel1296 = stubber1508.when(parcel1292);
        parcel1296.readString();
        org.mockito.stubbing.Stubber stubber1509 = org.mockito.Mockito.doReturn("Description");
        android.os.Parcel parcel1297 = stubber1509.when(parcel1292);
        parcel1297.readString();
        org.mockito.stubbing.Stubber stubber1510 = org.mockito.Mockito.doReturn(1);
        android.os.Parcel parcel1298 = stubber1510.when(parcel1292);
        parcel1298.readInt();
        org.mockito.stubbing.Stubber stubber1511 = org.mockito.Mockito.doReturn("123*456");
        android.os.Parcel parcel1299 = stubber1511.when(parcel1292);
        parcel1299.readString();
        filterrule205 = new com.prismaqf.callblocker.rules.FilterRule("dummy", "change me");
        filterrule205.getPatternKeys();
        filterrule205.getPatternKeys();
        filterrule205.getName();
        filterrule205.setName("dummy");
        filterrule205.getDescription();
        filterrule205.setDescription("change me");
        filterrule205.equals(object106);
        filterrule205.getPatternKeys();
        filterrule205.getPatternKeys();
        filterrule205.equals(object106);
        filterrule205.setName("Name");
        filterrule205.setDescription("Description");
        filterrule205.equals(object106);
        filterrule205.writeToParcel(parcel1291, 0);
        filterrule205.writeToParcel(parcel1291, 0);
        filterrule205.writeToParcel(parcel1299, 0);
        parcel1299.readString();
        parcel1299.readString();
        parcel1299.readInt();
        parcel1299.readString();
        filterrule205.clearPatterns();
        filterrule205.addPattern("123*456");
        filterrule205.getName();
        filterrule205.setName("Name");
        filterrule205.getDescription();
        filterrule205.setDescription("Description");
        filterrule205.equals(object106);
        filterrule205.getPatternKeys();
        filterrule205.getPatternKeys();
        filterrule205.equals(object106);
        filterrule205.getName();
        filterrule205.setName("Name");
        filterrule205.getDescription();
        filterrule205.setDescription("Description");
        filterrule205.equals(object106);
        filterrule205.getPatternKeys();
        filterrule205.getPatternKeys();
        filterrule205.equals(object106);
        filterrule205.getName();
        filterrule205.setName("Name");
        filterrule205.getDescription();
        filterrule205.setDescription("Description");
        filterrule205.equals(object106);
        filterrule205.getPatternKeys();
        filterrule205.getPatternKeys();
        filterrule205.equals(object106);
    }
}

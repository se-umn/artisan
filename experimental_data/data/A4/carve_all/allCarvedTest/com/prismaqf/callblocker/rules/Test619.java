package com.prismaqf.callblocker.rules;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test619 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.NewFilterRuleTest#TestDynamicStateOnRotation/Trace-1651091708855.txt
     * Method invocation under test: <com.prismaqf.callblocker.rules.FilterRule: java.util.Set getPatternKeys()>_924_1845
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_rules_FilterRule_getPatternKeys_015() throws Exception {
        com.prismaqf.callblocker.rules.FilterRule filterrule204 = null;
        java.lang.Object object105 = null;
        android.os.Parcel parcel1255 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber1473 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel1256 = stubber1473.when(parcel1255);
        parcel1256.writeString("Name");
        org.mockito.stubbing.Stubber stubber1474 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel1257 = stubber1474.when(parcel1255);
        parcel1257.writeString("Description");
        org.mockito.stubbing.Stubber stubber1475 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel1258 = stubber1475.when(parcel1255);
        parcel1258.writeInt(0);
        org.mockito.stubbing.Stubber stubber1476 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel1259 = stubber1476.when(parcel1255);
        parcel1259.writeString("Name");
        org.mockito.stubbing.Stubber stubber1477 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel1260 = stubber1477.when(parcel1255);
        parcel1260.writeString("Description");
        org.mockito.stubbing.Stubber stubber1478 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel1261 = stubber1478.when(parcel1255);
        parcel1261.writeInt(0);
        android.os.Parcel parcel1262 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber1479 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel1263 = stubber1479.when(parcel1262);
        parcel1263.writeString("Name");
        org.mockito.stubbing.Stubber stubber1480 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel1264 = stubber1480.when(parcel1262);
        parcel1264.writeString("Description");
        org.mockito.stubbing.Stubber stubber1481 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel1265 = stubber1481.when(parcel1262);
        parcel1265.writeInt(0);
        org.mockito.stubbing.Stubber stubber1482 = org.mockito.Mockito.doReturn("Name");
        android.os.Parcel parcel1266 = stubber1482.when(parcel1262);
        parcel1266.readString();
        org.mockito.stubbing.Stubber stubber1483 = org.mockito.Mockito.doReturn("Description");
        android.os.Parcel parcel1267 = stubber1483.when(parcel1262);
        parcel1267.readString();
        org.mockito.stubbing.Stubber stubber1484 = org.mockito.Mockito.doReturn(1);
        android.os.Parcel parcel1268 = stubber1484.when(parcel1262);
        parcel1268.readInt();
        org.mockito.stubbing.Stubber stubber1485 = org.mockito.Mockito.doReturn("123*456");
        android.os.Parcel parcel1269 = stubber1485.when(parcel1262);
        parcel1269.readString();
        filterrule204 = new com.prismaqf.callblocker.rules.FilterRule("dummy", "change me");
        filterrule204.getPatternKeys();
        filterrule204.getPatternKeys();
        filterrule204.getName();
        filterrule204.setName("dummy");
        filterrule204.getDescription();
        filterrule204.setDescription("change me");
        filterrule204.equals(object105);
        filterrule204.getPatternKeys();
        filterrule204.getPatternKeys();
        filterrule204.equals(object105);
        filterrule204.setName("Name");
        filterrule204.setDescription("Description");
        filterrule204.equals(object105);
        filterrule204.writeToParcel(parcel1261, 0);
        filterrule204.writeToParcel(parcel1261, 0);
        filterrule204.writeToParcel(parcel1269, 0);
        parcel1269.readString();
        parcel1269.readString();
        parcel1269.readInt();
        parcel1269.readString();
        filterrule204.clearPatterns();
        filterrule204.addPattern("123*456");
        filterrule204.getName();
        filterrule204.setName("Name");
        filterrule204.getDescription();
        filterrule204.setDescription("Description");
        filterrule204.equals(object105);
        filterrule204.getPatternKeys();
        filterrule204.getPatternKeys();
        filterrule204.equals(object105);
        filterrule204.getName();
        filterrule204.setName("Name");
        filterrule204.getDescription();
        filterrule204.setDescription("Description");
        filterrule204.equals(object105);
        filterrule204.getPatternKeys();
        filterrule204.getPatternKeys();
        filterrule204.equals(object105);
        filterrule204.getName();
        filterrule204.setName("Name");
        filterrule204.getDescription();
        filterrule204.setDescription("Description");
        filterrule204.equals(object105);
        filterrule204.getPatternKeys();
        filterrule204.getPatternKeys();
    }
}

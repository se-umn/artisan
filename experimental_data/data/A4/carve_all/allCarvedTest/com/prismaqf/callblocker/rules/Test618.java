package com.prismaqf.callblocker.rules;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test618 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.NewFilterRuleTest#TestDynamicStateOnRotation/Trace-1651091708855.txt
     * Method invocation under test: <com.prismaqf.callblocker.rules.FilterRule: java.util.Set getPatternKeys()>_919_1835
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_rules_FilterRule_getPatternKeys_014() throws Exception {
        com.prismaqf.callblocker.rules.FilterRule filterrule203 = null;
        java.lang.Object object104 = null;
        android.os.Parcel parcel1225 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber1447 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel1226 = stubber1447.when(parcel1225);
        parcel1226.writeString("Name");
        org.mockito.stubbing.Stubber stubber1448 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel1227 = stubber1448.when(parcel1225);
        parcel1227.writeString("Description");
        org.mockito.stubbing.Stubber stubber1449 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel1228 = stubber1449.when(parcel1225);
        parcel1228.writeInt(0);
        org.mockito.stubbing.Stubber stubber1450 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel1229 = stubber1450.when(parcel1225);
        parcel1229.writeString("Name");
        org.mockito.stubbing.Stubber stubber1451 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel1230 = stubber1451.when(parcel1225);
        parcel1230.writeString("Description");
        org.mockito.stubbing.Stubber stubber1452 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel1231 = stubber1452.when(parcel1225);
        parcel1231.writeInt(0);
        android.os.Parcel parcel1232 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber1453 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel1233 = stubber1453.when(parcel1232);
        parcel1233.writeString("Name");
        org.mockito.stubbing.Stubber stubber1454 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel1234 = stubber1454.when(parcel1232);
        parcel1234.writeString("Description");
        org.mockito.stubbing.Stubber stubber1455 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel1235 = stubber1455.when(parcel1232);
        parcel1235.writeInt(0);
        org.mockito.stubbing.Stubber stubber1456 = org.mockito.Mockito.doReturn("Name");
        android.os.Parcel parcel1236 = stubber1456.when(parcel1232);
        parcel1236.readString();
        org.mockito.stubbing.Stubber stubber1457 = org.mockito.Mockito.doReturn("Description");
        android.os.Parcel parcel1237 = stubber1457.when(parcel1232);
        parcel1237.readString();
        org.mockito.stubbing.Stubber stubber1458 = org.mockito.Mockito.doReturn(1);
        android.os.Parcel parcel1238 = stubber1458.when(parcel1232);
        parcel1238.readInt();
        org.mockito.stubbing.Stubber stubber1459 = org.mockito.Mockito.doReturn("123*456");
        android.os.Parcel parcel1239 = stubber1459.when(parcel1232);
        parcel1239.readString();
        filterrule203 = new com.prismaqf.callblocker.rules.FilterRule("dummy", "change me");
        filterrule203.getPatternKeys();
        filterrule203.getPatternKeys();
        filterrule203.getName();
        filterrule203.setName("dummy");
        filterrule203.getDescription();
        filterrule203.setDescription("change me");
        filterrule203.equals(object104);
        filterrule203.getPatternKeys();
        filterrule203.getPatternKeys();
        filterrule203.equals(object104);
        filterrule203.setName("Name");
        filterrule203.setDescription("Description");
        filterrule203.equals(object104);
        filterrule203.writeToParcel(parcel1231, 0);
        filterrule203.writeToParcel(parcel1231, 0);
        filterrule203.writeToParcel(parcel1239, 0);
        parcel1239.readString();
        parcel1239.readString();
        parcel1239.readInt();
        parcel1239.readString();
        filterrule203.clearPatterns();
        filterrule203.addPattern("123*456");
        filterrule203.getName();
        filterrule203.setName("Name");
        filterrule203.getDescription();
        filterrule203.setDescription("Description");
        filterrule203.equals(object104);
        filterrule203.getPatternKeys();
        filterrule203.getPatternKeys();
        filterrule203.equals(object104);
        filterrule203.getName();
        filterrule203.setName("Name");
        filterrule203.getDescription();
        filterrule203.setDescription("Description");
        filterrule203.equals(object104);
        filterrule203.getPatternKeys();
        filterrule203.getPatternKeys();
        filterrule203.equals(object104);
        filterrule203.getName();
        filterrule203.setName("Name");
        filterrule203.getDescription();
        filterrule203.setDescription("Description");
        filterrule203.equals(object104);
        filterrule203.getPatternKeys();
    }
}

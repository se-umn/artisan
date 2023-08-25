package com.prismaqf.callblocker.rules;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test615 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.NewFilterRuleTest#TestDynamicStateOnRotation/Trace-1651091708855.txt
     * Method invocation under test: <com.prismaqf.callblocker.rules.FilterRule: java.lang.String getDescription()>_900_1798
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_rules_FilterRule_getDescription_004() throws Exception {
        com.prismaqf.callblocker.rules.FilterRule filterrule200 = null;
        java.lang.Object object101 = null;
        android.os.Parcel parcel1135 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber1369 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel1136 = stubber1369.when(parcel1135);
        parcel1136.writeString("Name");
        org.mockito.stubbing.Stubber stubber1370 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel1137 = stubber1370.when(parcel1135);
        parcel1137.writeString("Description");
        org.mockito.stubbing.Stubber stubber1371 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel1138 = stubber1371.when(parcel1135);
        parcel1138.writeInt(0);
        org.mockito.stubbing.Stubber stubber1372 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel1139 = stubber1372.when(parcel1135);
        parcel1139.writeString("Name");
        org.mockito.stubbing.Stubber stubber1373 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel1140 = stubber1373.when(parcel1135);
        parcel1140.writeString("Description");
        org.mockito.stubbing.Stubber stubber1374 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel1141 = stubber1374.when(parcel1135);
        parcel1141.writeInt(0);
        android.os.Parcel parcel1142 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber1375 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel1143 = stubber1375.when(parcel1142);
        parcel1143.writeString("Name");
        org.mockito.stubbing.Stubber stubber1376 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel1144 = stubber1376.when(parcel1142);
        parcel1144.writeString("Description");
        org.mockito.stubbing.Stubber stubber1377 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel1145 = stubber1377.when(parcel1142);
        parcel1145.writeInt(0);
        org.mockito.stubbing.Stubber stubber1378 = org.mockito.Mockito.doReturn("Name");
        android.os.Parcel parcel1146 = stubber1378.when(parcel1142);
        parcel1146.readString();
        org.mockito.stubbing.Stubber stubber1379 = org.mockito.Mockito.doReturn("Description");
        android.os.Parcel parcel1147 = stubber1379.when(parcel1142);
        parcel1147.readString();
        org.mockito.stubbing.Stubber stubber1380 = org.mockito.Mockito.doReturn(1);
        android.os.Parcel parcel1148 = stubber1380.when(parcel1142);
        parcel1148.readInt();
        org.mockito.stubbing.Stubber stubber1381 = org.mockito.Mockito.doReturn("123*456");
        android.os.Parcel parcel1149 = stubber1381.when(parcel1142);
        parcel1149.readString();
        filterrule200 = new com.prismaqf.callblocker.rules.FilterRule("dummy", "change me");
        filterrule200.getPatternKeys();
        filterrule200.getPatternKeys();
        filterrule200.getName();
        filterrule200.setName("dummy");
        filterrule200.getDescription();
        filterrule200.setDescription("change me");
        filterrule200.equals(object101);
        filterrule200.getPatternKeys();
        filterrule200.getPatternKeys();
        filterrule200.equals(object101);
        filterrule200.setName("Name");
        filterrule200.setDescription("Description");
        filterrule200.equals(object101);
        filterrule200.writeToParcel(parcel1141, 0);
        filterrule200.writeToParcel(parcel1141, 0);
        filterrule200.writeToParcel(parcel1149, 0);
        parcel1149.readString();
        parcel1149.readString();
        parcel1149.readInt();
        parcel1149.readString();
        filterrule200.clearPatterns();
        filterrule200.addPattern("123*456");
        filterrule200.getName();
        filterrule200.setName("Name");
        filterrule200.getDescription();
        filterrule200.setDescription("Description");
        filterrule200.equals(object101);
        filterrule200.getPatternKeys();
        filterrule200.getPatternKeys();
        filterrule200.equals(object101);
        filterrule200.getName();
        filterrule200.setName("Name");
        filterrule200.getDescription();
        filterrule200.setDescription("Description");
        filterrule200.equals(object101);
        filterrule200.getPatternKeys();
        filterrule200.getPatternKeys();
        filterrule200.equals(object101);
        filterrule200.getName();
        filterrule200.setName("Name");
        filterrule200.getDescription();
    }
}

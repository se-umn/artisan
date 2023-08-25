package com.prismaqf.callblocker.rules;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test613 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.NewFilterRuleTest#TestDynamicStateOnRotation/Trace-1651091708855.txt
     * Method invocation under test: <com.prismaqf.callblocker.rules.FilterRule: java.lang.String getName()>_890_1778
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_rules_FilterRule_getName_004() throws Exception {
        com.prismaqf.callblocker.rules.FilterRule filterrule198 = null;
        java.lang.Object object99 = null;
        android.os.Parcel parcel1075 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber1317 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel1076 = stubber1317.when(parcel1075);
        parcel1076.writeString("Name");
        org.mockito.stubbing.Stubber stubber1318 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel1077 = stubber1318.when(parcel1075);
        parcel1077.writeString("Description");
        org.mockito.stubbing.Stubber stubber1319 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel1078 = stubber1319.when(parcel1075);
        parcel1078.writeInt(0);
        org.mockito.stubbing.Stubber stubber1320 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel1079 = stubber1320.when(parcel1075);
        parcel1079.writeString("Name");
        org.mockito.stubbing.Stubber stubber1321 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel1080 = stubber1321.when(parcel1075);
        parcel1080.writeString("Description");
        org.mockito.stubbing.Stubber stubber1322 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel1081 = stubber1322.when(parcel1075);
        parcel1081.writeInt(0);
        android.os.Parcel parcel1082 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber1323 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel1083 = stubber1323.when(parcel1082);
        parcel1083.writeString("Name");
        org.mockito.stubbing.Stubber stubber1324 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel1084 = stubber1324.when(parcel1082);
        parcel1084.writeString("Description");
        org.mockito.stubbing.Stubber stubber1325 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel1085 = stubber1325.when(parcel1082);
        parcel1085.writeInt(0);
        org.mockito.stubbing.Stubber stubber1326 = org.mockito.Mockito.doReturn("Name");
        android.os.Parcel parcel1086 = stubber1326.when(parcel1082);
        parcel1086.readString();
        org.mockito.stubbing.Stubber stubber1327 = org.mockito.Mockito.doReturn("Description");
        android.os.Parcel parcel1087 = stubber1327.when(parcel1082);
        parcel1087.readString();
        org.mockito.stubbing.Stubber stubber1328 = org.mockito.Mockito.doReturn(1);
        android.os.Parcel parcel1088 = stubber1328.when(parcel1082);
        parcel1088.readInt();
        org.mockito.stubbing.Stubber stubber1329 = org.mockito.Mockito.doReturn("123*456");
        android.os.Parcel parcel1089 = stubber1329.when(parcel1082);
        parcel1089.readString();
        filterrule198 = new com.prismaqf.callblocker.rules.FilterRule("dummy", "change me");
        filterrule198.getPatternKeys();
        filterrule198.getPatternKeys();
        filterrule198.getName();
        filterrule198.setName("dummy");
        filterrule198.getDescription();
        filterrule198.setDescription("change me");
        filterrule198.equals(object99);
        filterrule198.getPatternKeys();
        filterrule198.getPatternKeys();
        filterrule198.equals(object99);
        filterrule198.setName("Name");
        filterrule198.setDescription("Description");
        filterrule198.equals(object99);
        filterrule198.writeToParcel(parcel1081, 0);
        filterrule198.writeToParcel(parcel1081, 0);
        filterrule198.writeToParcel(parcel1089, 0);
        parcel1089.readString();
        parcel1089.readString();
        parcel1089.readInt();
        parcel1089.readString();
        filterrule198.clearPatterns();
        filterrule198.addPattern("123*456");
        filterrule198.getName();
        filterrule198.setName("Name");
        filterrule198.getDescription();
        filterrule198.setDescription("Description");
        filterrule198.equals(object99);
        filterrule198.getPatternKeys();
        filterrule198.getPatternKeys();
        filterrule198.equals(object99);
        filterrule198.getName();
        filterrule198.setName("Name");
        filterrule198.getDescription();
        filterrule198.setDescription("Description");
        filterrule198.equals(object99);
        filterrule198.getPatternKeys();
        filterrule198.getPatternKeys();
        filterrule198.equals(object99);
        filterrule198.getName();
    }
}

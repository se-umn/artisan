package com.prismaqf.callblocker.rules;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test616 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.NewFilterRuleTest#TestDynamicStateOnRotation/Trace-1651091708855.txt
     * Method invocation under test: <com.prismaqf.callblocker.rules.FilterRule: void setDescription(java.lang.String)>_907_1810
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_rules_FilterRule_setDescription_007() throws Exception {
        com.prismaqf.callblocker.rules.FilterRule filterrule201 = null;
        java.lang.Object object102 = null;
        android.os.Parcel parcel1165 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber1395 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel1166 = stubber1395.when(parcel1165);
        parcel1166.writeString("Name");
        org.mockito.stubbing.Stubber stubber1396 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel1167 = stubber1396.when(parcel1165);
        parcel1167.writeString("Description");
        org.mockito.stubbing.Stubber stubber1397 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel1168 = stubber1397.when(parcel1165);
        parcel1168.writeInt(0);
        org.mockito.stubbing.Stubber stubber1398 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel1169 = stubber1398.when(parcel1165);
        parcel1169.writeString("Name");
        org.mockito.stubbing.Stubber stubber1399 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel1170 = stubber1399.when(parcel1165);
        parcel1170.writeString("Description");
        org.mockito.stubbing.Stubber stubber1400 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel1171 = stubber1400.when(parcel1165);
        parcel1171.writeInt(0);
        android.os.Parcel parcel1172 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber1401 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel1173 = stubber1401.when(parcel1172);
        parcel1173.writeString("Name");
        org.mockito.stubbing.Stubber stubber1402 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel1174 = stubber1402.when(parcel1172);
        parcel1174.writeString("Description");
        org.mockito.stubbing.Stubber stubber1403 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel1175 = stubber1403.when(parcel1172);
        parcel1175.writeInt(0);
        org.mockito.stubbing.Stubber stubber1404 = org.mockito.Mockito.doReturn("Name");
        android.os.Parcel parcel1176 = stubber1404.when(parcel1172);
        parcel1176.readString();
        org.mockito.stubbing.Stubber stubber1405 = org.mockito.Mockito.doReturn("Description");
        android.os.Parcel parcel1177 = stubber1405.when(parcel1172);
        parcel1177.readString();
        org.mockito.stubbing.Stubber stubber1406 = org.mockito.Mockito.doReturn(1);
        android.os.Parcel parcel1178 = stubber1406.when(parcel1172);
        parcel1178.readInt();
        org.mockito.stubbing.Stubber stubber1407 = org.mockito.Mockito.doReturn("123*456");
        android.os.Parcel parcel1179 = stubber1407.when(parcel1172);
        parcel1179.readString();
        filterrule201 = new com.prismaqf.callblocker.rules.FilterRule("dummy", "change me");
        filterrule201.getPatternKeys();
        filterrule201.getPatternKeys();
        filterrule201.getName();
        filterrule201.setName("dummy");
        filterrule201.getDescription();
        filterrule201.setDescription("change me");
        filterrule201.equals(object102);
        filterrule201.getPatternKeys();
        filterrule201.getPatternKeys();
        filterrule201.equals(object102);
        filterrule201.setName("Name");
        filterrule201.setDescription("Description");
        filterrule201.equals(object102);
        filterrule201.writeToParcel(parcel1171, 0);
        filterrule201.writeToParcel(parcel1171, 0);
        filterrule201.writeToParcel(parcel1179, 0);
        parcel1179.readString();
        parcel1179.readString();
        parcel1179.readInt();
        parcel1179.readString();
        filterrule201.clearPatterns();
        filterrule201.addPattern("123*456");
        filterrule201.getName();
        filterrule201.setName("Name");
        filterrule201.getDescription();
        filterrule201.setDescription("Description");
        filterrule201.equals(object102);
        filterrule201.getPatternKeys();
        filterrule201.getPatternKeys();
        filterrule201.equals(object102);
        filterrule201.getName();
        filterrule201.setName("Name");
        filterrule201.getDescription();
        filterrule201.setDescription("Description");
        filterrule201.equals(object102);
        filterrule201.getPatternKeys();
        filterrule201.getPatternKeys();
        filterrule201.equals(object102);
        filterrule201.getName();
        filterrule201.setName("Name");
        filterrule201.getDescription();
        filterrule201.setDescription("Description");
    }
}

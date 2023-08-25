package com.prismaqf.callblocker.rules;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test605 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.NewFilterRuleTest#TestDynamicStateOnRotation/Trace-1651091708855.txt
     * Method invocation under test: <com.prismaqf.callblocker.rules.FilterRule: boolean equals(java.lang.Object)>_766_1528
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_rules_FilterRule_equals_007() throws Exception {
        com.prismaqf.callblocker.rules.FilterRule filterrule189 = null;
        java.lang.Object object95 = null;
        android.os.Parcel parcel955 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber1153 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel956 = stubber1153.when(parcel955);
        parcel956.writeString("Name");
        org.mockito.stubbing.Stubber stubber1154 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel957 = stubber1154.when(parcel955);
        parcel957.writeString("Description");
        org.mockito.stubbing.Stubber stubber1155 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel958 = stubber1155.when(parcel955);
        parcel958.writeInt(0);
        org.mockito.stubbing.Stubber stubber1156 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel959 = stubber1156.when(parcel955);
        parcel959.writeString("Name");
        org.mockito.stubbing.Stubber stubber1157 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel960 = stubber1157.when(parcel955);
        parcel960.writeString("Description");
        org.mockito.stubbing.Stubber stubber1158 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel961 = stubber1158.when(parcel955);
        parcel961.writeInt(0);
        android.os.Parcel parcel962 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber1159 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel963 = stubber1159.when(parcel962);
        parcel963.writeString("Name");
        org.mockito.stubbing.Stubber stubber1160 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel964 = stubber1160.when(parcel962);
        parcel964.writeString("Description");
        org.mockito.stubbing.Stubber stubber1161 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel965 = stubber1161.when(parcel962);
        parcel965.writeInt(0);
        org.mockito.stubbing.Stubber stubber1162 = org.mockito.Mockito.doReturn("Name");
        android.os.Parcel parcel966 = stubber1162.when(parcel962);
        parcel966.readString();
        org.mockito.stubbing.Stubber stubber1163 = org.mockito.Mockito.doReturn("Description");
        android.os.Parcel parcel967 = stubber1163.when(parcel962);
        parcel967.readString();
        org.mockito.stubbing.Stubber stubber1164 = org.mockito.Mockito.doReturn(1);
        android.os.Parcel parcel968 = stubber1164.when(parcel962);
        parcel968.readInt();
        org.mockito.stubbing.Stubber stubber1165 = org.mockito.Mockito.doReturn("123*456");
        android.os.Parcel parcel969 = stubber1165.when(parcel962);
        parcel969.readString();
        filterrule189 = new com.prismaqf.callblocker.rules.FilterRule("dummy", "change me");
        filterrule189.getPatternKeys();
        filterrule189.getPatternKeys();
        filterrule189.getName();
        filterrule189.setName("dummy");
        filterrule189.getDescription();
        filterrule189.setDescription("change me");
        filterrule189.equals(object95);
        filterrule189.getPatternKeys();
        filterrule189.getPatternKeys();
        filterrule189.equals(object95);
        filterrule189.setName("Name");
        filterrule189.setDescription("Description");
        filterrule189.equals(object95);
        filterrule189.writeToParcel(parcel961, 0);
        filterrule189.writeToParcel(parcel961, 0);
        filterrule189.writeToParcel(parcel969, 0);
        parcel969.readString();
        parcel969.readString();
        parcel969.readInt();
        parcel969.readString();
        filterrule189.clearPatterns();
        filterrule189.addPattern("123*456");
        filterrule189.getName();
        filterrule189.setName("Name");
        filterrule189.getDescription();
        filterrule189.setDescription("Description");
        filterrule189.equals(object95);
        filterrule189.getPatternKeys();
        filterrule189.getPatternKeys();
        filterrule189.equals(object95);
        filterrule189.getName();
        filterrule189.setName("Name");
        filterrule189.getDescription();
        filterrule189.setDescription("Description");
        filterrule189.equals(object95);
        filterrule189.getPatternKeys();
        filterrule189.getPatternKeys();
        filterrule189.equals(object95);
    }
}

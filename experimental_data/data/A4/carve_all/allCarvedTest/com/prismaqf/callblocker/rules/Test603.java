package com.prismaqf.callblocker.rules;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test603 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.NewFilterRuleTest#TestDynamicStateOnRotation/Trace-1651091708855.txt
     * Method invocation under test: <com.prismaqf.callblocker.rules.FilterRule: java.util.Set getPatternKeys()>_752_1501
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_rules_FilterRule_getPatternKeys_010() throws Exception {
        com.prismaqf.callblocker.rules.FilterRule filterrule187 = null;
        java.lang.Object object93 = null;
        android.os.Parcel parcel895 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber1101 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel896 = stubber1101.when(parcel895);
        parcel896.writeString("Name");
        org.mockito.stubbing.Stubber stubber1102 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel897 = stubber1102.when(parcel895);
        parcel897.writeString("Description");
        org.mockito.stubbing.Stubber stubber1103 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel898 = stubber1103.when(parcel895);
        parcel898.writeInt(0);
        org.mockito.stubbing.Stubber stubber1104 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel899 = stubber1104.when(parcel895);
        parcel899.writeString("Name");
        org.mockito.stubbing.Stubber stubber1105 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel900 = stubber1105.when(parcel895);
        parcel900.writeString("Description");
        org.mockito.stubbing.Stubber stubber1106 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel901 = stubber1106.when(parcel895);
        parcel901.writeInt(0);
        android.os.Parcel parcel902 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber1107 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel903 = stubber1107.when(parcel902);
        parcel903.writeString("Name");
        org.mockito.stubbing.Stubber stubber1108 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel904 = stubber1108.when(parcel902);
        parcel904.writeString("Description");
        org.mockito.stubbing.Stubber stubber1109 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel905 = stubber1109.when(parcel902);
        parcel905.writeInt(0);
        org.mockito.stubbing.Stubber stubber1110 = org.mockito.Mockito.doReturn("Name");
        android.os.Parcel parcel906 = stubber1110.when(parcel902);
        parcel906.readString();
        org.mockito.stubbing.Stubber stubber1111 = org.mockito.Mockito.doReturn("Description");
        android.os.Parcel parcel907 = stubber1111.when(parcel902);
        parcel907.readString();
        org.mockito.stubbing.Stubber stubber1112 = org.mockito.Mockito.doReturn(1);
        android.os.Parcel parcel908 = stubber1112.when(parcel902);
        parcel908.readInt();
        org.mockito.stubbing.Stubber stubber1113 = org.mockito.Mockito.doReturn("123*456");
        android.os.Parcel parcel909 = stubber1113.when(parcel902);
        parcel909.readString();
        filterrule187 = new com.prismaqf.callblocker.rules.FilterRule("dummy", "change me");
        filterrule187.getPatternKeys();
        filterrule187.getPatternKeys();
        filterrule187.getName();
        filterrule187.setName("dummy");
        filterrule187.getDescription();
        filterrule187.setDescription("change me");
        filterrule187.equals(object93);
        filterrule187.getPatternKeys();
        filterrule187.getPatternKeys();
        filterrule187.equals(object93);
        filterrule187.setName("Name");
        filterrule187.setDescription("Description");
        filterrule187.equals(object93);
        filterrule187.writeToParcel(parcel901, 0);
        filterrule187.writeToParcel(parcel901, 0);
        filterrule187.writeToParcel(parcel909, 0);
        parcel909.readString();
        parcel909.readString();
        parcel909.readInt();
        parcel909.readString();
        filterrule187.clearPatterns();
        filterrule187.addPattern("123*456");
        filterrule187.getName();
        filterrule187.setName("Name");
        filterrule187.getDescription();
        filterrule187.setDescription("Description");
        filterrule187.equals(object93);
        filterrule187.getPatternKeys();
        filterrule187.getPatternKeys();
        filterrule187.equals(object93);
        filterrule187.getName();
        filterrule187.setName("Name");
        filterrule187.getDescription();
        filterrule187.setDescription("Description");
        filterrule187.equals(object93);
        filterrule187.getPatternKeys();
    }
}

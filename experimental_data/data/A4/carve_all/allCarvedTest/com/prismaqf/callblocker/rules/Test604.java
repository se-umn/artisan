package com.prismaqf.callblocker.rules;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test604 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.NewFilterRuleTest#TestDynamicStateOnRotation/Trace-1651091708855.txt
     * Method invocation under test: <com.prismaqf.callblocker.rules.FilterRule: java.util.Set getPatternKeys()>_757_1511
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_rules_FilterRule_getPatternKeys_011() throws Exception {
        com.prismaqf.callblocker.rules.FilterRule filterrule188 = null;
        java.lang.Object object94 = null;
        android.os.Parcel parcel925 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber1127 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel926 = stubber1127.when(parcel925);
        parcel926.writeString("Name");
        org.mockito.stubbing.Stubber stubber1128 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel927 = stubber1128.when(parcel925);
        parcel927.writeString("Description");
        org.mockito.stubbing.Stubber stubber1129 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel928 = stubber1129.when(parcel925);
        parcel928.writeInt(0);
        org.mockito.stubbing.Stubber stubber1130 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel929 = stubber1130.when(parcel925);
        parcel929.writeString("Name");
        org.mockito.stubbing.Stubber stubber1131 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel930 = stubber1131.when(parcel925);
        parcel930.writeString("Description");
        org.mockito.stubbing.Stubber stubber1132 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel931 = stubber1132.when(parcel925);
        parcel931.writeInt(0);
        android.os.Parcel parcel932 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber1133 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel933 = stubber1133.when(parcel932);
        parcel933.writeString("Name");
        org.mockito.stubbing.Stubber stubber1134 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel934 = stubber1134.when(parcel932);
        parcel934.writeString("Description");
        org.mockito.stubbing.Stubber stubber1135 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel935 = stubber1135.when(parcel932);
        parcel935.writeInt(0);
        org.mockito.stubbing.Stubber stubber1136 = org.mockito.Mockito.doReturn("Name");
        android.os.Parcel parcel936 = stubber1136.when(parcel932);
        parcel936.readString();
        org.mockito.stubbing.Stubber stubber1137 = org.mockito.Mockito.doReturn("Description");
        android.os.Parcel parcel937 = stubber1137.when(parcel932);
        parcel937.readString();
        org.mockito.stubbing.Stubber stubber1138 = org.mockito.Mockito.doReturn(1);
        android.os.Parcel parcel938 = stubber1138.when(parcel932);
        parcel938.readInt();
        org.mockito.stubbing.Stubber stubber1139 = org.mockito.Mockito.doReturn("123*456");
        android.os.Parcel parcel939 = stubber1139.when(parcel932);
        parcel939.readString();
        filterrule188 = new com.prismaqf.callblocker.rules.FilterRule("dummy", "change me");
        filterrule188.getPatternKeys();
        filterrule188.getPatternKeys();
        filterrule188.getName();
        filterrule188.setName("dummy");
        filterrule188.getDescription();
        filterrule188.setDescription("change me");
        filterrule188.equals(object94);
        filterrule188.getPatternKeys();
        filterrule188.getPatternKeys();
        filterrule188.equals(object94);
        filterrule188.setName("Name");
        filterrule188.setDescription("Description");
        filterrule188.equals(object94);
        filterrule188.writeToParcel(parcel931, 0);
        filterrule188.writeToParcel(parcel931, 0);
        filterrule188.writeToParcel(parcel939, 0);
        parcel939.readString();
        parcel939.readString();
        parcel939.readInt();
        parcel939.readString();
        filterrule188.clearPatterns();
        filterrule188.addPattern("123*456");
        filterrule188.getName();
        filterrule188.setName("Name");
        filterrule188.getDescription();
        filterrule188.setDescription("Description");
        filterrule188.equals(object94);
        filterrule188.getPatternKeys();
        filterrule188.getPatternKeys();
        filterrule188.equals(object94);
        filterrule188.getName();
        filterrule188.setName("Name");
        filterrule188.getDescription();
        filterrule188.setDescription("Description");
        filterrule188.equals(object94);
        filterrule188.getPatternKeys();
        filterrule188.getPatternKeys();
    }
}

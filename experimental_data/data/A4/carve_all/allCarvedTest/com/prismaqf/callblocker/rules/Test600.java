package com.prismaqf.callblocker.rules;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test600 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.NewFilterRuleTest#TestDynamicStateOnRotation/Trace-1651091708855.txt
     * Method invocation under test: <com.prismaqf.callblocker.rules.FilterRule: java.lang.String getDescription()>_733_1464
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_rules_FilterRule_getDescription_003() throws Exception {
        com.prismaqf.callblocker.rules.FilterRule filterrule184 = null;
        java.lang.Object object90 = null;
        android.os.Parcel parcel805 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber1023 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel806 = stubber1023.when(parcel805);
        parcel806.writeString("Name");
        org.mockito.stubbing.Stubber stubber1024 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel807 = stubber1024.when(parcel805);
        parcel807.writeString("Description");
        org.mockito.stubbing.Stubber stubber1025 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel808 = stubber1025.when(parcel805);
        parcel808.writeInt(0);
        org.mockito.stubbing.Stubber stubber1026 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel809 = stubber1026.when(parcel805);
        parcel809.writeString("Name");
        org.mockito.stubbing.Stubber stubber1027 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel810 = stubber1027.when(parcel805);
        parcel810.writeString("Description");
        org.mockito.stubbing.Stubber stubber1028 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel811 = stubber1028.when(parcel805);
        parcel811.writeInt(0);
        android.os.Parcel parcel812 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber1029 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel813 = stubber1029.when(parcel812);
        parcel813.writeString("Name");
        org.mockito.stubbing.Stubber stubber1030 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel814 = stubber1030.when(parcel812);
        parcel814.writeString("Description");
        org.mockito.stubbing.Stubber stubber1031 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel815 = stubber1031.when(parcel812);
        parcel815.writeInt(0);
        org.mockito.stubbing.Stubber stubber1032 = org.mockito.Mockito.doReturn("Name");
        android.os.Parcel parcel816 = stubber1032.when(parcel812);
        parcel816.readString();
        org.mockito.stubbing.Stubber stubber1033 = org.mockito.Mockito.doReturn("Description");
        android.os.Parcel parcel817 = stubber1033.when(parcel812);
        parcel817.readString();
        org.mockito.stubbing.Stubber stubber1034 = org.mockito.Mockito.doReturn(1);
        android.os.Parcel parcel818 = stubber1034.when(parcel812);
        parcel818.readInt();
        org.mockito.stubbing.Stubber stubber1035 = org.mockito.Mockito.doReturn("123*456");
        android.os.Parcel parcel819 = stubber1035.when(parcel812);
        parcel819.readString();
        filterrule184 = new com.prismaqf.callblocker.rules.FilterRule("dummy", "change me");
        filterrule184.getPatternKeys();
        filterrule184.getPatternKeys();
        filterrule184.getName();
        filterrule184.setName("dummy");
        filterrule184.getDescription();
        filterrule184.setDescription("change me");
        filterrule184.equals(object90);
        filterrule184.getPatternKeys();
        filterrule184.getPatternKeys();
        filterrule184.equals(object90);
        filterrule184.setName("Name");
        filterrule184.setDescription("Description");
        filterrule184.equals(object90);
        filterrule184.writeToParcel(parcel811, 0);
        filterrule184.writeToParcel(parcel811, 0);
        filterrule184.writeToParcel(parcel819, 0);
        parcel819.readString();
        parcel819.readString();
        parcel819.readInt();
        parcel819.readString();
        filterrule184.clearPatterns();
        filterrule184.addPattern("123*456");
        filterrule184.getName();
        filterrule184.setName("Name");
        filterrule184.getDescription();
        filterrule184.setDescription("Description");
        filterrule184.equals(object90);
        filterrule184.getPatternKeys();
        filterrule184.getPatternKeys();
        filterrule184.equals(object90);
        filterrule184.getName();
        filterrule184.setName("Name");
        filterrule184.getDescription();
    }
}

package com.prismaqf.callblocker.rules;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test601 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.NewFilterRuleTest#TestDynamicStateOnRotation/Trace-1651091708855.txt
     * Method invocation under test: <com.prismaqf.callblocker.rules.FilterRule: void setDescription(java.lang.String)>_740_1476
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_rules_FilterRule_setDescription_005() throws Exception {
        com.prismaqf.callblocker.rules.FilterRule filterrule185 = null;
        java.lang.Object object91 = null;
        android.os.Parcel parcel835 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber1049 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel836 = stubber1049.when(parcel835);
        parcel836.writeString("Name");
        org.mockito.stubbing.Stubber stubber1050 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel837 = stubber1050.when(parcel835);
        parcel837.writeString("Description");
        org.mockito.stubbing.Stubber stubber1051 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel838 = stubber1051.when(parcel835);
        parcel838.writeInt(0);
        org.mockito.stubbing.Stubber stubber1052 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel839 = stubber1052.when(parcel835);
        parcel839.writeString("Name");
        org.mockito.stubbing.Stubber stubber1053 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel840 = stubber1053.when(parcel835);
        parcel840.writeString("Description");
        org.mockito.stubbing.Stubber stubber1054 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel841 = stubber1054.when(parcel835);
        parcel841.writeInt(0);
        android.os.Parcel parcel842 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber1055 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel843 = stubber1055.when(parcel842);
        parcel843.writeString("Name");
        org.mockito.stubbing.Stubber stubber1056 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel844 = stubber1056.when(parcel842);
        parcel844.writeString("Description");
        org.mockito.stubbing.Stubber stubber1057 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel845 = stubber1057.when(parcel842);
        parcel845.writeInt(0);
        org.mockito.stubbing.Stubber stubber1058 = org.mockito.Mockito.doReturn("Name");
        android.os.Parcel parcel846 = stubber1058.when(parcel842);
        parcel846.readString();
        org.mockito.stubbing.Stubber stubber1059 = org.mockito.Mockito.doReturn("Description");
        android.os.Parcel parcel847 = stubber1059.when(parcel842);
        parcel847.readString();
        org.mockito.stubbing.Stubber stubber1060 = org.mockito.Mockito.doReturn(1);
        android.os.Parcel parcel848 = stubber1060.when(parcel842);
        parcel848.readInt();
        org.mockito.stubbing.Stubber stubber1061 = org.mockito.Mockito.doReturn("123*456");
        android.os.Parcel parcel849 = stubber1061.when(parcel842);
        parcel849.readString();
        filterrule185 = new com.prismaqf.callblocker.rules.FilterRule("dummy", "change me");
        filterrule185.getPatternKeys();
        filterrule185.getPatternKeys();
        filterrule185.getName();
        filterrule185.setName("dummy");
        filterrule185.getDescription();
        filterrule185.setDescription("change me");
        filterrule185.equals(object91);
        filterrule185.getPatternKeys();
        filterrule185.getPatternKeys();
        filterrule185.equals(object91);
        filterrule185.setName("Name");
        filterrule185.setDescription("Description");
        filterrule185.equals(object91);
        filterrule185.writeToParcel(parcel841, 0);
        filterrule185.writeToParcel(parcel841, 0);
        filterrule185.writeToParcel(parcel849, 0);
        parcel849.readString();
        parcel849.readString();
        parcel849.readInt();
        parcel849.readString();
        filterrule185.clearPatterns();
        filterrule185.addPattern("123*456");
        filterrule185.getName();
        filterrule185.setName("Name");
        filterrule185.getDescription();
        filterrule185.setDescription("Description");
        filterrule185.equals(object91);
        filterrule185.getPatternKeys();
        filterrule185.getPatternKeys();
        filterrule185.equals(object91);
        filterrule185.getName();
        filterrule185.setName("Name");
        filterrule185.getDescription();
        filterrule185.setDescription("Description");
    }
}

package com.prismaqf.callblocker.rules;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test602 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.NewFilterRuleTest#TestDynamicStateOnRotation/Trace-1651091708855.txt
     * Method invocation under test: <com.prismaqf.callblocker.rules.FilterRule: boolean equals(java.lang.Object)>_743_1481
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_rules_FilterRule_equals_006() throws Exception {
        com.prismaqf.callblocker.rules.FilterRule filterrule186 = null;
        java.lang.Object object92 = null;
        android.os.Parcel parcel865 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber1075 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel866 = stubber1075.when(parcel865);
        parcel866.writeString("Name");
        org.mockito.stubbing.Stubber stubber1076 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel867 = stubber1076.when(parcel865);
        parcel867.writeString("Description");
        org.mockito.stubbing.Stubber stubber1077 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel868 = stubber1077.when(parcel865);
        parcel868.writeInt(0);
        org.mockito.stubbing.Stubber stubber1078 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel869 = stubber1078.when(parcel865);
        parcel869.writeString("Name");
        org.mockito.stubbing.Stubber stubber1079 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel870 = stubber1079.when(parcel865);
        parcel870.writeString("Description");
        org.mockito.stubbing.Stubber stubber1080 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel871 = stubber1080.when(parcel865);
        parcel871.writeInt(0);
        android.os.Parcel parcel872 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber1081 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel873 = stubber1081.when(parcel872);
        parcel873.writeString("Name");
        org.mockito.stubbing.Stubber stubber1082 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel874 = stubber1082.when(parcel872);
        parcel874.writeString("Description");
        org.mockito.stubbing.Stubber stubber1083 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel875 = stubber1083.when(parcel872);
        parcel875.writeInt(0);
        org.mockito.stubbing.Stubber stubber1084 = org.mockito.Mockito.doReturn("Name");
        android.os.Parcel parcel876 = stubber1084.when(parcel872);
        parcel876.readString();
        org.mockito.stubbing.Stubber stubber1085 = org.mockito.Mockito.doReturn("Description");
        android.os.Parcel parcel877 = stubber1085.when(parcel872);
        parcel877.readString();
        org.mockito.stubbing.Stubber stubber1086 = org.mockito.Mockito.doReturn(1);
        android.os.Parcel parcel878 = stubber1086.when(parcel872);
        parcel878.readInt();
        org.mockito.stubbing.Stubber stubber1087 = org.mockito.Mockito.doReturn("123*456");
        android.os.Parcel parcel879 = stubber1087.when(parcel872);
        parcel879.readString();
        filterrule186 = new com.prismaqf.callblocker.rules.FilterRule("dummy", "change me");
        filterrule186.getPatternKeys();
        filterrule186.getPatternKeys();
        filterrule186.getName();
        filterrule186.setName("dummy");
        filterrule186.getDescription();
        filterrule186.setDescription("change me");
        filterrule186.equals(object92);
        filterrule186.getPatternKeys();
        filterrule186.getPatternKeys();
        filterrule186.equals(object92);
        filterrule186.setName("Name");
        filterrule186.setDescription("Description");
        filterrule186.equals(object92);
        filterrule186.writeToParcel(parcel871, 0);
        filterrule186.writeToParcel(parcel871, 0);
        filterrule186.writeToParcel(parcel879, 0);
        parcel879.readString();
        parcel879.readString();
        parcel879.readInt();
        parcel879.readString();
        filterrule186.clearPatterns();
        filterrule186.addPattern("123*456");
        filterrule186.getName();
        filterrule186.setName("Name");
        filterrule186.getDescription();
        filterrule186.setDescription("Description");
        filterrule186.equals(object92);
        filterrule186.getPatternKeys();
        filterrule186.getPatternKeys();
        filterrule186.equals(object92);
        filterrule186.getName();
        filterrule186.setName("Name");
        filterrule186.getDescription();
        filterrule186.setDescription("Description");
        filterrule186.equals(object92);
    }
}

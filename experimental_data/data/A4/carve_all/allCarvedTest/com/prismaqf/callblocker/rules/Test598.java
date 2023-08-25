package com.prismaqf.callblocker.rules;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test598 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.NewFilterRuleTest#TestDynamicStateOnRotation/Trace-1651091708855.txt
     * Method invocation under test: <com.prismaqf.callblocker.rules.FilterRule: java.lang.String getName()>_723_1444
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_rules_FilterRule_getName_003() throws Exception {
        com.prismaqf.callblocker.rules.FilterRule filterrule182 = null;
        java.lang.Object object88 = null;
        android.os.Parcel parcel745 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber971 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel746 = stubber971.when(parcel745);
        parcel746.writeString("Name");
        org.mockito.stubbing.Stubber stubber972 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel747 = stubber972.when(parcel745);
        parcel747.writeString("Description");
        org.mockito.stubbing.Stubber stubber973 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel748 = stubber973.when(parcel745);
        parcel748.writeInt(0);
        org.mockito.stubbing.Stubber stubber974 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel749 = stubber974.when(parcel745);
        parcel749.writeString("Name");
        org.mockito.stubbing.Stubber stubber975 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel750 = stubber975.when(parcel745);
        parcel750.writeString("Description");
        org.mockito.stubbing.Stubber stubber976 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel751 = stubber976.when(parcel745);
        parcel751.writeInt(0);
        android.os.Parcel parcel752 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber977 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel753 = stubber977.when(parcel752);
        parcel753.writeString("Name");
        org.mockito.stubbing.Stubber stubber978 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel754 = stubber978.when(parcel752);
        parcel754.writeString("Description");
        org.mockito.stubbing.Stubber stubber979 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel755 = stubber979.when(parcel752);
        parcel755.writeInt(0);
        org.mockito.stubbing.Stubber stubber980 = org.mockito.Mockito.doReturn("Name");
        android.os.Parcel parcel756 = stubber980.when(parcel752);
        parcel756.readString();
        org.mockito.stubbing.Stubber stubber981 = org.mockito.Mockito.doReturn("Description");
        android.os.Parcel parcel757 = stubber981.when(parcel752);
        parcel757.readString();
        org.mockito.stubbing.Stubber stubber982 = org.mockito.Mockito.doReturn(1);
        android.os.Parcel parcel758 = stubber982.when(parcel752);
        parcel758.readInt();
        org.mockito.stubbing.Stubber stubber983 = org.mockito.Mockito.doReturn("123*456");
        android.os.Parcel parcel759 = stubber983.when(parcel752);
        parcel759.readString();
        filterrule182 = new com.prismaqf.callblocker.rules.FilterRule("dummy", "change me");
        filterrule182.getPatternKeys();
        filterrule182.getPatternKeys();
        filterrule182.getName();
        filterrule182.setName("dummy");
        filterrule182.getDescription();
        filterrule182.setDescription("change me");
        filterrule182.equals(object88);
        filterrule182.getPatternKeys();
        filterrule182.getPatternKeys();
        filterrule182.equals(object88);
        filterrule182.setName("Name");
        filterrule182.setDescription("Description");
        filterrule182.equals(object88);
        filterrule182.writeToParcel(parcel751, 0);
        filterrule182.writeToParcel(parcel751, 0);
        filterrule182.writeToParcel(parcel759, 0);
        parcel759.readString();
        parcel759.readString();
        parcel759.readInt();
        parcel759.readString();
        filterrule182.clearPatterns();
        filterrule182.addPattern("123*456");
        filterrule182.getName();
        filterrule182.setName("Name");
        filterrule182.getDescription();
        filterrule182.setDescription("Description");
        filterrule182.equals(object88);
        filterrule182.getPatternKeys();
        filterrule182.getPatternKeys();
        filterrule182.equals(object88);
        filterrule182.getName();
    }
}

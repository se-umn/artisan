package com.prismaqf.callblocker.rules;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test578 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.NewFilterRuleTest#TestDynamicStateOnRotation/Trace-1651091708855.txt
     * Method invocation under test: <com.prismaqf.callblocker.rules.FilterRule: void clearPatterns()>_528_1055
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_rules_FilterRule_clearPatterns_001() throws Exception {
        com.prismaqf.callblocker.rules.FilterRule filterrule126 = null;
        java.lang.Object object46 = null;
        android.os.Parcel parcel369 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber399 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel370 = stubber399.when(parcel369);
        parcel370.writeString("Name");
        org.mockito.stubbing.Stubber stubber400 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel371 = stubber400.when(parcel369);
        parcel371.writeString("Description");
        org.mockito.stubbing.Stubber stubber401 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel372 = stubber401.when(parcel369);
        parcel372.writeInt(0);
        org.mockito.stubbing.Stubber stubber402 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel373 = stubber402.when(parcel369);
        parcel373.writeString("Name");
        org.mockito.stubbing.Stubber stubber403 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel374 = stubber403.when(parcel369);
        parcel374.writeString("Description");
        org.mockito.stubbing.Stubber stubber404 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel375 = stubber404.when(parcel369);
        parcel375.writeInt(0);
        android.os.Parcel parcel376 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber405 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel377 = stubber405.when(parcel376);
        parcel377.writeString("Name");
        org.mockito.stubbing.Stubber stubber406 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel378 = stubber406.when(parcel376);
        parcel378.writeString("Description");
        org.mockito.stubbing.Stubber stubber407 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel379 = stubber407.when(parcel376);
        parcel379.writeInt(0);
        org.mockito.stubbing.Stubber stubber408 = org.mockito.Mockito.doReturn("Name");
        android.os.Parcel parcel380 = stubber408.when(parcel376);
        parcel380.readString();
        org.mockito.stubbing.Stubber stubber409 = org.mockito.Mockito.doReturn("Description");
        android.os.Parcel parcel381 = stubber409.when(parcel376);
        parcel381.readString();
        org.mockito.stubbing.Stubber stubber410 = org.mockito.Mockito.doReturn(1);
        android.os.Parcel parcel382 = stubber410.when(parcel376);
        parcel382.readInt();
        org.mockito.stubbing.Stubber stubber411 = org.mockito.Mockito.doReturn("123*456");
        android.os.Parcel parcel383 = stubber411.when(parcel376);
        parcel383.readString();
        filterrule126 = new com.prismaqf.callblocker.rules.FilterRule("dummy", "change me");
        filterrule126.getPatternKeys();
        filterrule126.getPatternKeys();
        filterrule126.getName();
        filterrule126.setName("dummy");
        filterrule126.getDescription();
        filterrule126.setDescription("change me");
        filterrule126.equals(object46);
        filterrule126.getPatternKeys();
        filterrule126.getPatternKeys();
        filterrule126.equals(object46);
        filterrule126.setName("Name");
        filterrule126.setDescription("Description");
        filterrule126.equals(object46);
        filterrule126.writeToParcel(parcel375, 0);
        filterrule126.writeToParcel(parcel375, 0);
        filterrule126.writeToParcel(parcel383, 0);
        parcel383.readString();
        parcel383.readString();
        parcel383.readInt();
        parcel383.readString();
        filterrule126.clearPatterns();
    }
}

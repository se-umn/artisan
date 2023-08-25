package com.prismaqf.callblocker.rules;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test584 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.NewFilterRuleTest#TestDynamicStateOnRotation/Trace-1651091708855.txt
     * Method invocation under test: <com.prismaqf.callblocker.rules.FilterRule: java.lang.String getDescription()>_566_1130
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_rules_FilterRule_getDescription_002() throws Exception {
        com.prismaqf.callblocker.rules.FilterRule filterrule131 = null;
        java.lang.Object object50 = null;
        android.os.Parcel parcel505 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber589 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel506 = stubber589.when(parcel505);
        parcel506.writeString("Name");
        org.mockito.stubbing.Stubber stubber590 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel507 = stubber590.when(parcel505);
        parcel507.writeString("Description");
        org.mockito.stubbing.Stubber stubber591 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel508 = stubber591.when(parcel505);
        parcel508.writeInt(0);
        org.mockito.stubbing.Stubber stubber592 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel509 = stubber592.when(parcel505);
        parcel509.writeString("Name");
        org.mockito.stubbing.Stubber stubber593 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel510 = stubber593.when(parcel505);
        parcel510.writeString("Description");
        org.mockito.stubbing.Stubber stubber594 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel511 = stubber594.when(parcel505);
        parcel511.writeInt(0);
        android.os.Parcel parcel512 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber595 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel513 = stubber595.when(parcel512);
        parcel513.writeString("Name");
        org.mockito.stubbing.Stubber stubber596 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel514 = stubber596.when(parcel512);
        parcel514.writeString("Description");
        org.mockito.stubbing.Stubber stubber597 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel515 = stubber597.when(parcel512);
        parcel515.writeInt(0);
        org.mockito.stubbing.Stubber stubber598 = org.mockito.Mockito.doReturn("Name");
        android.os.Parcel parcel516 = stubber598.when(parcel512);
        parcel516.readString();
        org.mockito.stubbing.Stubber stubber599 = org.mockito.Mockito.doReturn("Description");
        android.os.Parcel parcel517 = stubber599.when(parcel512);
        parcel517.readString();
        org.mockito.stubbing.Stubber stubber600 = org.mockito.Mockito.doReturn(1);
        android.os.Parcel parcel518 = stubber600.when(parcel512);
        parcel518.readInt();
        org.mockito.stubbing.Stubber stubber601 = org.mockito.Mockito.doReturn("123*456");
        android.os.Parcel parcel519 = stubber601.when(parcel512);
        parcel519.readString();
        filterrule131 = new com.prismaqf.callblocker.rules.FilterRule("dummy", "change me");
        filterrule131.getPatternKeys();
        filterrule131.getPatternKeys();
        filterrule131.getName();
        filterrule131.setName("dummy");
        filterrule131.getDescription();
        filterrule131.setDescription("change me");
        filterrule131.equals(object50);
        filterrule131.getPatternKeys();
        filterrule131.getPatternKeys();
        filterrule131.equals(object50);
        filterrule131.setName("Name");
        filterrule131.setDescription("Description");
        filterrule131.equals(object50);
        filterrule131.writeToParcel(parcel511, 0);
        filterrule131.writeToParcel(parcel511, 0);
        filterrule131.writeToParcel(parcel519, 0);
        parcel519.readString();
        parcel519.readString();
        parcel519.readInt();
        parcel519.readString();
        filterrule131.clearPatterns();
        filterrule131.addPattern("123*456");
        filterrule131.getName();
        filterrule131.setName("Name");
        filterrule131.getDescription();
    }
}

package com.prismaqf.callblocker.rules;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test744 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.ManageFilterPatternsTest#TestUpdate/Trace-1651091665345.txt
     * Method invocation under test: <com.prismaqf.callblocker.rules.FilterRule: java.lang.String getDescription()>_466_930
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_rules_FilterRule_getDescription_002() throws Exception {
        com.prismaqf.callblocker.rules.FilterRule filterrule128 = null;
        java.lang.Object object47 = null;
        android.os.Parcel parcel505 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber577 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel506 = stubber577.when(parcel505);
        parcel506.writeString("dummy");
        org.mockito.stubbing.Stubber stubber578 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel507 = stubber578.when(parcel505);
        parcel507.writeString("change me");
        org.mockito.stubbing.Stubber stubber579 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel508 = stubber579.when(parcel505);
        parcel508.writeInt(0);
        org.mockito.stubbing.Stubber stubber580 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel509 = stubber580.when(parcel505);
        parcel509.writeString("dummy");
        org.mockito.stubbing.Stubber stubber581 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel510 = stubber581.when(parcel505);
        parcel510.writeString("change me");
        org.mockito.stubbing.Stubber stubber582 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel511 = stubber582.when(parcel505);
        parcel511.writeInt(0);
        android.os.Parcel parcel512 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber583 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel513 = stubber583.when(parcel512);
        parcel513.writeString("dummy");
        org.mockito.stubbing.Stubber stubber584 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel514 = stubber584.when(parcel512);
        parcel514.writeString("change me");
        org.mockito.stubbing.Stubber stubber585 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel515 = stubber585.when(parcel512);
        parcel515.writeInt(0);
        org.mockito.stubbing.Stubber stubber586 = org.mockito.Mockito.doReturn("dummy");
        android.os.Parcel parcel516 = stubber586.when(parcel512);
        parcel516.readString();
        org.mockito.stubbing.Stubber stubber587 = org.mockito.Mockito.doReturn("change me");
        android.os.Parcel parcel517 = stubber587.when(parcel512);
        parcel517.readString();
        org.mockito.stubbing.Stubber stubber588 = org.mockito.Mockito.doReturn(1);
        android.os.Parcel parcel518 = stubber588.when(parcel512);
        parcel518.readInt();
        org.mockito.stubbing.Stubber stubber589 = org.mockito.Mockito.doReturn("123*456");
        android.os.Parcel parcel519 = stubber589.when(parcel512);
        parcel519.readString();
        filterrule128 = new com.prismaqf.callblocker.rules.FilterRule("dummy", "change me");
        filterrule128.getPatternKeys();
        filterrule128.getPatternKeys();
        filterrule128.getName();
        filterrule128.setName("dummy");
        filterrule128.getDescription();
        filterrule128.setDescription("change me");
        filterrule128.equals(object47);
        filterrule128.getPatternKeys();
        filterrule128.getPatternKeys();
        filterrule128.equals(object47);
        filterrule128.writeToParcel(parcel511, 0);
        filterrule128.writeToParcel(parcel511, 0);
        filterrule128.writeToParcel(parcel519, 0);
        parcel519.readString();
        parcel519.readString();
        parcel519.readInt();
        parcel519.readString();
        filterrule128.clearPatterns();
        filterrule128.addPattern("123*456");
        filterrule128.getName();
        filterrule128.setName("dummy");
        filterrule128.getDescription();
    }
}

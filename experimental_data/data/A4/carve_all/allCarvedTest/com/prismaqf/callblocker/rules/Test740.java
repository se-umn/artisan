package com.prismaqf.callblocker.rules;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test740 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.ManageFilterPatternsTest#TestUpdate/Trace-1651091665345.txt
     * Method invocation under test: <com.prismaqf.callblocker.rules.FilterRule: void addPattern(java.lang.String)>_437_873
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_rules_FilterRule_addPattern_001() throws Exception {
        com.prismaqf.callblocker.rules.FilterRule filterrule125 = null;
        java.lang.Object object44 = null;
        android.os.Parcel parcel415 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber437 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel416 = stubber437.when(parcel415);
        parcel416.writeString("dummy");
        org.mockito.stubbing.Stubber stubber438 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel417 = stubber438.when(parcel415);
        parcel417.writeString("change me");
        org.mockito.stubbing.Stubber stubber439 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel418 = stubber439.when(parcel415);
        parcel418.writeInt(0);
        org.mockito.stubbing.Stubber stubber440 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel419 = stubber440.when(parcel415);
        parcel419.writeString("dummy");
        org.mockito.stubbing.Stubber stubber441 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel420 = stubber441.when(parcel415);
        parcel420.writeString("change me");
        org.mockito.stubbing.Stubber stubber442 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel421 = stubber442.when(parcel415);
        parcel421.writeInt(0);
        android.os.Parcel parcel422 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber443 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel423 = stubber443.when(parcel422);
        parcel423.writeString("dummy");
        org.mockito.stubbing.Stubber stubber444 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel424 = stubber444.when(parcel422);
        parcel424.writeString("change me");
        org.mockito.stubbing.Stubber stubber445 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel425 = stubber445.when(parcel422);
        parcel425.writeInt(0);
        org.mockito.stubbing.Stubber stubber446 = org.mockito.Mockito.doReturn("dummy");
        android.os.Parcel parcel426 = stubber446.when(parcel422);
        parcel426.readString();
        org.mockito.stubbing.Stubber stubber447 = org.mockito.Mockito.doReturn("change me");
        android.os.Parcel parcel427 = stubber447.when(parcel422);
        parcel427.readString();
        org.mockito.stubbing.Stubber stubber448 = org.mockito.Mockito.doReturn(1);
        android.os.Parcel parcel428 = stubber448.when(parcel422);
        parcel428.readInt();
        org.mockito.stubbing.Stubber stubber449 = org.mockito.Mockito.doReturn("123*456");
        android.os.Parcel parcel429 = stubber449.when(parcel422);
        parcel429.readString();
        filterrule125 = new com.prismaqf.callblocker.rules.FilterRule("dummy", "change me");
        filterrule125.getPatternKeys();
        filterrule125.getPatternKeys();
        filterrule125.getName();
        filterrule125.setName("dummy");
        filterrule125.getDescription();
        filterrule125.setDescription("change me");
        filterrule125.equals(object44);
        filterrule125.getPatternKeys();
        filterrule125.getPatternKeys();
        filterrule125.equals(object44);
        filterrule125.writeToParcel(parcel421, 0);
        filterrule125.writeToParcel(parcel421, 0);
        filterrule125.writeToParcel(parcel429, 0);
        parcel429.readString();
        parcel429.readString();
        parcel429.readInt();
        parcel429.readString();
        filterrule125.clearPatterns();
        filterrule125.addPattern("123*456");
    }
}

package com.prismaqf.callblocker.rules;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test1642 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.ManageFilterPatternsTest#PickFromLogRepeatedNumber/Trace-1651091681239.txt
     * Method invocation under test: <com.prismaqf.callblocker.rules.FilterRule: void writeToParcel(android.os.Parcel,int)>_362_724
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_rules_FilterRule_writeToParcel_005() throws Exception {
        com.prismaqf.callblocker.rules.FilterRule filterrule93 = null;
        java.lang.Object object38 = null;
        android.os.Parcel parcel278 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber313 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel279 = stubber313.when(parcel278);
        parcel279.writeString("dummy");
        org.mockito.stubbing.Stubber stubber314 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel280 = stubber314.when(parcel278);
        parcel280.writeString("change me");
        org.mockito.stubbing.Stubber stubber315 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel281 = stubber315.when(parcel278);
        parcel281.writeInt(0);
        org.mockito.stubbing.Stubber stubber316 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel282 = stubber316.when(parcel278);
        parcel282.writeString("dummy");
        org.mockito.stubbing.Stubber stubber317 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel283 = stubber317.when(parcel278);
        parcel283.writeString("change me");
        org.mockito.stubbing.Stubber stubber318 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel284 = stubber318.when(parcel278);
        parcel284.writeInt(0);
        android.os.Parcel parcel285 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber319 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel286 = stubber319.when(parcel285);
        parcel286.writeString("dummy");
        org.mockito.stubbing.Stubber stubber320 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel287 = stubber320.when(parcel285);
        parcel287.writeString("change me");
        org.mockito.stubbing.Stubber stubber321 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel288 = stubber321.when(parcel285);
        parcel288.writeInt(0);
        android.os.Parcel parcel289 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber322 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel290 = stubber322.when(parcel289);
        parcel290.writeString("dummy");
        org.mockito.stubbing.Stubber stubber323 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel291 = stubber323.when(parcel289);
        parcel291.writeString("change me");
        org.mockito.stubbing.Stubber stubber324 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel292 = stubber324.when(parcel289);
        parcel292.writeInt(0);
        org.mockito.stubbing.Stubber stubber325 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel293 = stubber325.when(parcel289);
        parcel293.writeString("dummy");
        org.mockito.stubbing.Stubber stubber326 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel294 = stubber326.when(parcel289);
        parcel294.writeString("change me");
        org.mockito.stubbing.Stubber stubber327 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel295 = stubber327.when(parcel289);
        parcel295.writeInt(0);
        filterrule93 = new com.prismaqf.callblocker.rules.FilterRule("dummy", "change me");
        filterrule93.getPatternKeys();
        filterrule93.getPatternKeys();
        filterrule93.getName();
        filterrule93.setName("dummy");
        filterrule93.getDescription();
        filterrule93.setDescription("change me");
        filterrule93.equals(object38);
        filterrule93.getPatternKeys();
        filterrule93.getPatternKeys();
        filterrule93.equals(object38);
        filterrule93.writeToParcel(parcel284, 0);
        filterrule93.writeToParcel(parcel284, 0);
        filterrule93.writeToParcel(parcel288, 0);
        filterrule93.writeToParcel(parcel295, 0);
        filterrule93.writeToParcel(parcel295, 0);
    }
}

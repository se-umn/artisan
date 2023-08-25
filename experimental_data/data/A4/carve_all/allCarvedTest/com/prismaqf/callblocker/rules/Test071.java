package com.prismaqf.callblocker.rules;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test071 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.ManageFilterPatternsTest#TestCheckingPersistsWithRotation/Trace-1651091676947.txt
     * Method invocation under test: <com.prismaqf.callblocker.rules.FilterRule: void writeToParcel(android.os.Parcel,int)>_545_1090
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_rules_FilterRule_writeToParcel_006() throws Exception {
        com.prismaqf.callblocker.rules.FilterRule filterrule183 = null;
        java.lang.Object object73 = null;
        android.os.Parcel parcel922 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber1004 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel923 = stubber1004.when(parcel922);
        parcel923.writeString("dummy");
        org.mockito.stubbing.Stubber stubber1005 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel924 = stubber1005.when(parcel922);
        parcel924.writeString("change me");
        org.mockito.stubbing.Stubber stubber1006 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel925 = stubber1006.when(parcel922);
        parcel925.writeInt(0);
        org.mockito.stubbing.Stubber stubber1007 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel926 = stubber1007.when(parcel922);
        parcel926.writeString("dummy");
        org.mockito.stubbing.Stubber stubber1008 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel927 = stubber1008.when(parcel922);
        parcel927.writeString("change me");
        org.mockito.stubbing.Stubber stubber1009 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel928 = stubber1009.when(parcel922);
        parcel928.writeInt(0);
        android.os.Parcel parcel929 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber1010 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel930 = stubber1010.when(parcel929);
        parcel930.writeString("dummy");
        org.mockito.stubbing.Stubber stubber1011 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel931 = stubber1011.when(parcel929);
        parcel931.writeString("change me");
        org.mockito.stubbing.Stubber stubber1012 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel932 = stubber1012.when(parcel929);
        parcel932.writeInt(0);
        android.os.Parcel parcel933 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber1013 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel934 = stubber1013.when(parcel933);
        parcel934.writeString("dummy");
        org.mockito.stubbing.Stubber stubber1014 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel935 = stubber1014.when(parcel933);
        parcel935.writeString("change me");
        org.mockito.stubbing.Stubber stubber1015 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel936 = stubber1015.when(parcel933);
        parcel936.writeInt(0);
        org.mockito.stubbing.Stubber stubber1016 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel937 = stubber1016.when(parcel933);
        parcel937.writeString("dummy");
        org.mockito.stubbing.Stubber stubber1017 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel938 = stubber1017.when(parcel933);
        parcel938.writeString("change me");
        org.mockito.stubbing.Stubber stubber1018 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel939 = stubber1018.when(parcel933);
        parcel939.writeInt(0);
        org.mockito.stubbing.Stubber stubber1019 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel940 = stubber1019.when(parcel933);
        parcel940.writeString("dummy");
        org.mockito.stubbing.Stubber stubber1020 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel941 = stubber1020.when(parcel933);
        parcel941.writeString("change me");
        org.mockito.stubbing.Stubber stubber1021 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel942 = stubber1021.when(parcel933);
        parcel942.writeInt(0);
        org.mockito.stubbing.Stubber stubber1022 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel943 = stubber1022.when(parcel933);
        parcel943.writeString("dummy");
        org.mockito.stubbing.Stubber stubber1023 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel944 = stubber1023.when(parcel933);
        parcel944.writeString("change me");
        org.mockito.stubbing.Stubber stubber1024 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel945 = stubber1024.when(parcel933);
        parcel945.writeInt(0);
        android.os.Parcel parcel946 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber1025 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel947 = stubber1025.when(parcel946);
        parcel947.writeString("dummy");
        org.mockito.stubbing.Stubber stubber1026 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel948 = stubber1026.when(parcel946);
        parcel948.writeString("change me");
        org.mockito.stubbing.Stubber stubber1027 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel949 = stubber1027.when(parcel946);
        parcel949.writeInt(0);
        filterrule183 = new com.prismaqf.callblocker.rules.FilterRule("dummy", "change me");
        filterrule183.getPatternKeys();
        filterrule183.getPatternKeys();
        filterrule183.getName();
        filterrule183.setName("dummy");
        filterrule183.getDescription();
        filterrule183.setDescription("change me");
        filterrule183.equals(object73);
        filterrule183.getPatternKeys();
        filterrule183.getPatternKeys();
        filterrule183.equals(object73);
        filterrule183.writeToParcel(parcel928, 0);
        filterrule183.writeToParcel(parcel928, 0);
        filterrule183.writeToParcel(parcel949, 0);
        filterrule183.writeToParcel(parcel945, 0);
        filterrule183.writeToParcel(parcel945, 0);
        parcel945.writeString("dummy");
        parcel945.writeString("change me");
        parcel945.writeInt(0);
        parcel945.writeString("dummy");
        parcel945.writeString("change me");
        parcel945.writeInt(0);
        filterrule183.writeToParcel(parcel932, 0);
    }
}

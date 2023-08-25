package com.prismaqf.callblocker.rules;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test072 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.ManageFilterPatternsTest#TestCheckingPersistsWithRotation/Trace-1651091676947.txt
     * Method invocation under test: <com.prismaqf.callblocker.rules.FilterRule: void writeToParcel(android.os.Parcel,int)>_553_1106
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_rules_FilterRule_writeToParcel_007() throws Exception {
        com.prismaqf.callblocker.rules.FilterRule filterrule184 = null;
        java.lang.Object object74 = null;
        android.os.Parcel parcel981 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber1055 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel982 = stubber1055.when(parcel981);
        parcel982.writeString("dummy");
        org.mockito.stubbing.Stubber stubber1056 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel983 = stubber1056.when(parcel981);
        parcel983.writeString("change me");
        org.mockito.stubbing.Stubber stubber1057 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel984 = stubber1057.when(parcel981);
        parcel984.writeInt(0);
        org.mockito.stubbing.Stubber stubber1058 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel985 = stubber1058.when(parcel981);
        parcel985.writeString("dummy");
        org.mockito.stubbing.Stubber stubber1059 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel986 = stubber1059.when(parcel981);
        parcel986.writeString("change me");
        org.mockito.stubbing.Stubber stubber1060 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel987 = stubber1060.when(parcel981);
        parcel987.writeInt(0);
        android.os.Parcel parcel988 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber1061 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel989 = stubber1061.when(parcel988);
        parcel989.writeString("dummy");
        org.mockito.stubbing.Stubber stubber1062 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel990 = stubber1062.when(parcel988);
        parcel990.writeString("change me");
        org.mockito.stubbing.Stubber stubber1063 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel991 = stubber1063.when(parcel988);
        parcel991.writeInt(0);
        org.mockito.stubbing.Stubber stubber1064 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel992 = stubber1064.when(parcel988);
        parcel992.writeString("dummy");
        org.mockito.stubbing.Stubber stubber1065 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel993 = stubber1065.when(parcel988);
        parcel993.writeString("change me");
        org.mockito.stubbing.Stubber stubber1066 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel994 = stubber1066.when(parcel988);
        parcel994.writeInt(0);
        android.os.Parcel parcel995 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber1067 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel996 = stubber1067.when(parcel995);
        parcel996.writeString("dummy");
        org.mockito.stubbing.Stubber stubber1068 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel997 = stubber1068.when(parcel995);
        parcel997.writeString("change me");
        org.mockito.stubbing.Stubber stubber1069 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel998 = stubber1069.when(parcel995);
        parcel998.writeInt(0);
        org.mockito.stubbing.Stubber stubber1070 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel999 = stubber1070.when(parcel995);
        parcel999.writeString("dummy");
        org.mockito.stubbing.Stubber stubber1071 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel1000 = stubber1071.when(parcel995);
        parcel1000.writeString("change me");
        org.mockito.stubbing.Stubber stubber1072 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel1001 = stubber1072.when(parcel995);
        parcel1001.writeInt(0);
        org.mockito.stubbing.Stubber stubber1073 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel1002 = stubber1073.when(parcel995);
        parcel1002.writeString("dummy");
        org.mockito.stubbing.Stubber stubber1074 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel1003 = stubber1074.when(parcel995);
        parcel1003.writeString("change me");
        org.mockito.stubbing.Stubber stubber1075 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel1004 = stubber1075.when(parcel995);
        parcel1004.writeInt(0);
        org.mockito.stubbing.Stubber stubber1076 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel1005 = stubber1076.when(parcel995);
        parcel1005.writeString("dummy");
        org.mockito.stubbing.Stubber stubber1077 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel1006 = stubber1077.when(parcel995);
        parcel1006.writeString("change me");
        org.mockito.stubbing.Stubber stubber1078 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel1007 = stubber1078.when(parcel995);
        parcel1007.writeInt(0);
        android.os.Parcel parcel1008 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber1079 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel1009 = stubber1079.when(parcel1008);
        parcel1009.writeString("dummy");
        org.mockito.stubbing.Stubber stubber1080 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel1010 = stubber1080.when(parcel1008);
        parcel1010.writeString("change me");
        org.mockito.stubbing.Stubber stubber1081 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel1011 = stubber1081.when(parcel1008);
        parcel1011.writeInt(0);
        filterrule184 = new com.prismaqf.callblocker.rules.FilterRule("dummy", "change me");
        filterrule184.getPatternKeys();
        filterrule184.getPatternKeys();
        filterrule184.getName();
        filterrule184.setName("dummy");
        filterrule184.getDescription();
        filterrule184.setDescription("change me");
        filterrule184.equals(object74);
        filterrule184.getPatternKeys();
        filterrule184.getPatternKeys();
        filterrule184.equals(object74);
        filterrule184.writeToParcel(parcel987, 0);
        filterrule184.writeToParcel(parcel987, 0);
        filterrule184.writeToParcel(parcel1011, 0);
        filterrule184.writeToParcel(parcel1007, 0);
        filterrule184.writeToParcel(parcel1007, 0);
        parcel1007.writeString("dummy");
        parcel1007.writeString("change me");
        parcel1007.writeInt(0);
        parcel1007.writeString("dummy");
        parcel1007.writeString("change me");
        parcel1007.writeInt(0);
        filterrule184.writeToParcel(parcel994, 0);
        filterrule184.writeToParcel(parcel994, 0);
    }
}

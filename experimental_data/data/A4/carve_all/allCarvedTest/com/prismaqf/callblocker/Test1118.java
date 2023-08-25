package com.prismaqf.callblocker;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test1118 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.ManageFilterPatternsTest#PickFromLog/Trace-1651091669275.txt
     * Method invocation under test: <com.prismaqf.callblocker.EditFilterPatterns: void onCreate(android.os.Bundle)>_173_346
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_EditFilterPatterns_onCreate_001() throws Exception {
        android.content.Intent intent2 = null;
        java.lang.Object object6 = null;
        com.prismaqf.callblocker.rules.FilterRule filterrule13 = null;
        android.content.Context context1 = ApplicationProvider.getApplicationContext();
        filterrule13 = new com.prismaqf.callblocker.rules.FilterRule("dummy", "change me");
        filterrule13.getPatternKeys();
        filterrule13.getPatternKeys();
        filterrule13.getName();
        filterrule13.setName("dummy");
        filterrule13.getDescription();
        filterrule13.setDescription("change me");
        filterrule13.equals(object6);
        filterrule13.getPatternKeys();
        filterrule13.getPatternKeys();
        filterrule13.equals(object6);
        intent2 = new android.content.Intent(context1, com.prismaqf.callblocker.EditFilterPatterns.class);
        android.content.Intent intent3 = intent2.putExtra("com.prismaqft.callblocker:ptrule", (android.os.Parcelable) filterrule13);
        android.content.Intent intent4 = intent3.putExtra("com.prismaqft.callblocker:keyorig", (android.os.Parcelable) filterrule13);
        org.robolectric.android.controller.ActivityController activitycontroller8 = org.robolectric.Robolectric.buildActivity(com.prismaqf.callblocker.EditFilterPatterns.class, intent4);
        activitycontroller8.get();
        activitycontroller8.create();
    }
}

package com.prismaqf.callblocker;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test368 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /Users/mattia/Faculty/Research/2020_android_test_carving/repositories/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.ManageFilterPatternsTest#TestAddPatternWithExtraChars/Trace-1651091688218.txt
     * Method invocation under test: <com.prismaqf.callblocker.EditFilterPatterns: void onCreate(android.os.Bundle)>_173_346
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_EditFilterPatterns_onCreate_001() throws Exception {
        android.content.Intent intent2 = null;
        java.lang.Object object2 = null;
        com.prismaqf.callblocker.rules.FilterRule filterrule8 = null;
        android.content.Context context1 = ApplicationProvider.getApplicationContext();
        filterrule8 = new com.prismaqf.callblocker.rules.FilterRule("dummy", "change me");
        filterrule8.getPatternKeys();
        filterrule8.getPatternKeys();
        filterrule8.getName();
        filterrule8.setName("dummy");
        filterrule8.getDescription();
        filterrule8.setDescription("change me");
        filterrule8.equals(object2);
        filterrule8.getPatternKeys();
        filterrule8.getPatternKeys();
        filterrule8.equals(object2);
        intent2 = new android.content.Intent(context1, com.prismaqf.callblocker.EditFilterPatterns.class);
        android.content.Intent intent3 = intent2.putExtra("com.prismaqft.callblocker:ptrule", (android.os.Parcelable) filterrule8);
        android.content.Intent intent4 = intent3.putExtra("com.prismaqft.callblocker:keyorig", (android.os.Parcelable) filterrule8);
        org.robolectric.android.controller.ActivityController activitycontroller8 = org.robolectric.Robolectric.buildActivity(com.prismaqf.callblocker.EditFilterPatterns.class, intent4);
        activitycontroller8.get();
        activitycontroller8.create();
    }
}

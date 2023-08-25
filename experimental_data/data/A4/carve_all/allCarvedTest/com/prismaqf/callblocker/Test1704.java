package com.prismaqf.callblocker;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test1704 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.NewFilterRuleTest#AfterUpdatingTheRulePatternsTheRuleShouldSaveWhenActivityIsLeft/Trace-1651091712732.txt
     * Method invocation under test: <com.prismaqf.callblocker.EditFilterPatterns: void onCreate(android.os.Bundle)>_257_514
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_EditFilterPatterns_onCreate_001() throws Exception {
        java.lang.Object object7 = null;
        com.prismaqf.callblocker.rules.FilterRule filterrule14 = null;
        android.content.Intent intent4 = null;
        android.content.Context context5 = ApplicationProvider.getApplicationContext();
        filterrule14 = new com.prismaqf.callblocker.rules.FilterRule("dummy", "change me");
        filterrule14.getPatternKeys();
        filterrule14.getPatternKeys();
        filterrule14.getName();
        filterrule14.setName("dummy");
        filterrule14.getDescription();
        filterrule14.setDescription("change me");
        filterrule14.equals(object7);
        filterrule14.getPatternKeys();
        filterrule14.getPatternKeys();
        filterrule14.equals(object7);
        filterrule14.setName("my dummy rule");
        intent4 = new android.content.Intent(context5, com.prismaqf.callblocker.EditFilterPatterns.class);
        android.content.Intent intent5 = intent4.putExtra("com.prismaqft.callblocker:ptrule", (android.os.Parcelable) filterrule14);
        android.content.Intent intent6 = intent5.putExtra("com.prismaqft.callblocker:keyorig", (android.os.Parcelable) filterrule14);
        org.robolectric.android.controller.ActivityController activitycontroller15 = org.robolectric.Robolectric.buildActivity(com.prismaqf.callblocker.EditFilterPatterns.class, intent6);
        activitycontroller15.get();
        activitycontroller15.create();
    }
}

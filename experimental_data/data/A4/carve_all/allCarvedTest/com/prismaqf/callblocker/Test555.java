package com.prismaqf.callblocker;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test555 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.NewFilterRuleTest#TestDynamicStateOnRotation/Trace-1651091708855.txt
     * Method invocation under test: <com.prismaqf.callblocker.EditFilterPatterns: void onCreate(android.os.Bundle)>_271_542
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_EditFilterPatterns_onCreate_001() throws Exception {
        java.lang.Object object9 = null;
        com.prismaqf.callblocker.rules.FilterRule filterrule16 = null;
        android.content.Intent intent7 = null;
        android.content.Context context5 = ApplicationProvider.getApplicationContext();
        filterrule16 = new com.prismaqf.callblocker.rules.FilterRule("dummy", "change me");
        filterrule16.getPatternKeys();
        filterrule16.getPatternKeys();
        filterrule16.getName();
        filterrule16.setName("dummy");
        filterrule16.getDescription();
        filterrule16.setDescription("change me");
        filterrule16.equals(object9);
        filterrule16.getPatternKeys();
        filterrule16.getPatternKeys();
        filterrule16.equals(object9);
        filterrule16.setName("Name");
        filterrule16.setDescription("Description");
        filterrule16.equals(object9);
        intent7 = new android.content.Intent(context5, com.prismaqf.callblocker.EditFilterPatterns.class);
        android.content.Intent intent8 = intent7.putExtra("com.prismaqft.callblocker:ptrule", (android.os.Parcelable) filterrule16);
        android.content.Intent intent9 = intent8.putExtra("com.prismaqft.callblocker:keyorig", (android.os.Parcelable) filterrule16);
        org.robolectric.android.controller.ActivityController activitycontroller19 = org.robolectric.Robolectric.buildActivity(com.prismaqf.callblocker.EditFilterPatterns.class, intent9);
        activitycontroller19.get();
        activitycontroller19.create();
    }
}

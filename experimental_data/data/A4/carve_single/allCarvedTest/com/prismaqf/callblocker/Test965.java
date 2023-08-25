package com.prismaqf.callblocker;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test965 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /Users/mattia/Faculty/Research/2020_android_test_carving/repositories/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.EditFiltersTest#NavigateUpIsContextSensitive/Trace-1651091805625.txt
     * Method invocation under test: <com.prismaqf.callblocker.EditCalendarRules: boolean onCreateOptionsMenu(android.view.Menu)>_391_782
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_EditCalendarRules_onCreateOptionsMenu_001() throws Exception {
        android.content.Intent intent14 = null;
        android.content.Context context21 = ApplicationProvider.getApplicationContext();
        intent14 = new android.content.Intent(context21, com.prismaqf.callblocker.EditCalendarRules.class);
        android.content.Intent intent15 = intent14.putExtra("com.prismaqf.callblocker:context", "com.prismaqf.callblocker:pick");
        org.robolectric.android.controller.ActivityController activitycontroller44 = org.robolectric.Robolectric.buildActivity(com.prismaqf.callblocker.EditCalendarRules.class, intent15);
        activitycontroller44.get();
        org.robolectric.android.controller.ActivityController activitycontroller45 = activitycontroller44.create();
        activitycontroller45.visible();
    }
}

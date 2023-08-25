package com.prismaqf.callblocker;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test1271 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.EditFiltersTest#UndoActionTest/Trace-1651091801855.txt
     * Method invocation under test: <com.prismaqf.callblocker.EditCalendarRules: boolean onCreateOptionsMenu(android.view.Menu)>_391_782
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_EditCalendarRules_onCreateOptionsMenu_001() throws Exception {
        android.content.Intent intent18 = null;
        android.content.Context context27 = ApplicationProvider.getApplicationContext();
        intent18 = new android.content.Intent(context27, com.prismaqf.callblocker.EditCalendarRules.class);
        android.content.Intent intent19 = intent18.putExtra("com.prismaqf.callblocker:context", "com.prismaqf.callblocker:pick");
        org.robolectric.android.controller.ActivityController activitycontroller49 = org.robolectric.Robolectric.buildActivity(com.prismaqf.callblocker.EditCalendarRules.class, intent19);
        activitycontroller49.get();
        org.robolectric.android.controller.ActivityController activitycontroller50 = activitycontroller49.create();
        activitycontroller50.visible();
    }
}

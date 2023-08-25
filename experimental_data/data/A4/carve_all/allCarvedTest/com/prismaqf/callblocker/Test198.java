package com.prismaqf.callblocker;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test198 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.EditFiltersTest#CreateAndSelectNewFilterRule/Trace-1651091784191.txt
     * Method invocation under test: <com.prismaqf.callblocker.EditFilterRules: boolean onCreateOptionsMenu(android.view.Menu)>_391_782
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_EditFilterRules_onCreateOptionsMenu_001() throws Exception {
        android.content.Intent intent18 = null;
        android.content.Context context27 = ApplicationProvider.getApplicationContext();
        intent18 = new android.content.Intent(context27, com.prismaqf.callblocker.EditFilterRules.class);
        android.content.Intent intent19 = intent18.putExtra("com.prismaqf.callblocker:context", "com.prismaqf.callblocker:pick");
        org.robolectric.android.controller.ActivityController activitycontroller49 = org.robolectric.Robolectric.buildActivity(com.prismaqf.callblocker.EditFilterRules.class, intent19);
        activitycontroller49.get();
        org.robolectric.android.controller.ActivityController activitycontroller50 = activitycontroller49.create();
        activitycontroller50.visible();
    }
}

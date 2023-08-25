package com.prismaqf.callblocker;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test789 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /Users/mattia/Faculty/Research/2020_android_test_carving/repositories/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.EditFiltersTest#NewFilterTest/Trace-1651091782604.txt
     * Method invocation under test: <com.prismaqf.callblocker.CallBlockerManager: boolean onCreateOptionsMenu(android.view.Menu)>_76_152
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_CallBlockerManager_onCreateOptionsMenu_001() throws Exception {
        ApplicationProvider.getApplicationContext();
        org.robolectric.android.controller.ActivityController activitycontroller8 = org.robolectric.Robolectric.buildActivity(com.prismaqf.callblocker.CallBlockerManager.class);
        activitycontroller8.get();
        org.robolectric.android.controller.ActivityController activitycontroller9 = activitycontroller8.create();
        org.robolectric.android.controller.ActivityController activitycontroller10 = activitycontroller9.start();
        activitycontroller10.visible();
    }
}

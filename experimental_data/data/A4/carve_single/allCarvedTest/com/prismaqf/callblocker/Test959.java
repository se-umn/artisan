package com.prismaqf.callblocker;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test959 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /Users/mattia/Faculty/Research/2020_android_test_carving/repositories/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.EditFiltersTest#NavigateUpIsContextSensitive/Trace-1651091805625.txt
     * Method invocation under test: <com.prismaqf.callblocker.EditFilters: boolean onCreateOptionsMenu(android.view.Menu)>_135_270
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_EditFilters_onCreateOptionsMenu_001() throws Exception {
        org.robolectric.android.controller.ActivityController activitycontroller20 = org.robolectric.Robolectric.buildActivity(com.prismaqf.callblocker.EditFilters.class);
        activitycontroller20.get();
        org.robolectric.android.controller.ActivityController activitycontroller21 = activitycontroller20.create();
        activitycontroller21.visible();
    }
}

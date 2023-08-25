package com.prismaqf.callblocker;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test1759 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.EditFiltersTest#NewFilterTest/Trace-1651091782604.txt
     * Method invocation under test: <com.prismaqf.callblocker.EditFilters: boolean onOptionsItemSelected(android.view.MenuItem)>_143_286
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_EditFilters_onOptionsItemSelected_001() throws Exception {
        org.robolectric.android.controller.ActivityController activitycontroller31 = org.robolectric.Robolectric.buildActivity(com.prismaqf.callblocker.EditFilters.class);
        com.prismaqf.callblocker.EditFilters editfilters5 = (com.prismaqf.callblocker.EditFilters) activitycontroller31.get();
        org.robolectric.android.controller.ActivityController activitycontroller32 = activitycontroller31.create();
        activitycontroller32.visible();
        org.robolectric.shadows.ShadowActivity shadowactivity1 = org.robolectric.Shadows.shadowOf(editfilters5);
        shadowactivity1.clickMenuItem(2131230754);
    }
}

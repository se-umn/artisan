package com.prismaqf.callblocker;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test800 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /Users/mattia/Faculty/Research/2020_android_test_carving/repositories/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.EditFiltersTest#NewFilterTest/Trace-1651091782604.txt
     * Method invocation under test: <com.prismaqf.callblocker.NewEditFilter: boolean onCreateOptionsMenu(android.view.Menu)>_207_414
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_NewEditFilter_onCreateOptionsMenu_001() throws Exception {
        android.content.Intent intent3 = null;
        java.util.ArrayList arraylist3 = org.mockito.Mockito.mock(java.util.ArrayList.class);
        android.content.Context context17 = ApplicationProvider.getApplicationContext();
        intent3 = new android.content.Intent(context17, com.prismaqf.callblocker.NewEditFilter.class);
        android.content.Intent intent4 = intent3.putExtra("com.prismaqf.callblocker:action", "com.prismaqf.callblocker:create");
        android.content.Intent intent5 = intent4.putStringArrayListExtra("com.prismaqft.callblocker:rulenames", arraylist3);
        org.robolectric.android.controller.ActivityController activitycontroller38 = org.robolectric.Robolectric.buildActivity(com.prismaqf.callblocker.NewEditFilter.class, intent5);
        activitycontroller38.get();
        org.robolectric.android.controller.ActivityController activitycontroller39 = activitycontroller38.create();
        activitycontroller39.visible();
    }
}

package com.prismaqf.callblocker.utils;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test092 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /Users/mattia/Faculty/Research/2020_android_test_carving/repositories/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.EditFiltersTest#PickActionTest/Trace-1651091791193.txt
     * Method invocation under test: <com.prismaqf.callblocker.utils.ActionAdapter: void <init>(android.content.Context)>_352_703
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_utils_ActionAdapter_constructor_001() throws Exception {
        com.prismaqf.callblocker.utils.ActionAdapter actionadapter0 = null;
        android.content.Context context19 = ApplicationProvider.getApplicationContext();
        actionadapter0 = new com.prismaqf.callblocker.utils.ActionAdapter(context19);
    }
}

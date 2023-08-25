package com.prismaqf.callblocker;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test122 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /Users/mattia/Faculty/Research/2020_android_test_carving/repositories/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.EditFiltersTest#CreateAndSelectNewFilterRule/Trace-1651091784191.txt
     * Method invocation under test: <com.prismaqf.callblocker.NewEditFilterRule: void onCreate(android.os.Bundle)>_442_884
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_NewEditFilterRule_onCreate_001() throws Exception {
        android.content.Intent intent22 = null;
        java.util.ArrayList arraylist2 = org.mockito.Mockito.mock(java.util.ArrayList.class);
        android.content.Context context29 = ApplicationProvider.getApplicationContext();
        intent22 = new android.content.Intent(context29, com.prismaqf.callblocker.NewEditFilterRule.class);
        android.content.Intent intent23 = intent22.putExtra("com.prismaqf.callblocker:action", "com.prismaqf.callblocker:create");
        android.content.Intent intent24 = intent23.putStringArrayListExtra("com.prismaqft.callblocker:rulenames", arraylist2);
        android.content.Intent intent25 = intent24.putExtra("com.prismaqf.callblocker:context", "com.prismaqf.callblocker:pick");
        org.robolectric.android.controller.ActivityController activitycontroller57 = org.robolectric.Robolectric.buildActivity(com.prismaqf.callblocker.NewEditFilterRule.class, intent25);
        activitycontroller57.get();
        activitycontroller57.create();
    }
}

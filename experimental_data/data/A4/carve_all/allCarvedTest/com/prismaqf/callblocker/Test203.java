package com.prismaqf.callblocker;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test203 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.EditFiltersTest#CreateAndSelectNewFilterRule/Trace-1651091784191.txt
     * Method invocation under test: <com.prismaqf.callblocker.NewEditFilterRule: void onCreate(android.os.Bundle)>_442_884
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_NewEditFilterRule_onCreate_001() throws Exception {
        android.content.Intent intent26 = null;
        java.util.ArrayList arraylist2 = org.mockito.Mockito.mock(java.util.ArrayList.class);
        android.content.Context context37 = ApplicationProvider.getApplicationContext();
        intent26 = new android.content.Intent(context37, com.prismaqf.callblocker.NewEditFilterRule.class);
        android.content.Intent intent27 = intent26.putExtra("com.prismaqf.callblocker:action", "com.prismaqf.callblocker:create");
        android.content.Intent intent28 = intent27.putStringArrayListExtra("com.prismaqft.callblocker:rulenames", arraylist2);
        android.content.Intent intent29 = intent28.putExtra("com.prismaqf.callblocker:context", "com.prismaqf.callblocker:pick");
        org.robolectric.android.controller.ActivityController activitycontroller61 = org.robolectric.Robolectric.buildActivity(com.prismaqf.callblocker.NewEditFilterRule.class, intent29);
        activitycontroller61.get();
        activitycontroller61.create();
    }
}

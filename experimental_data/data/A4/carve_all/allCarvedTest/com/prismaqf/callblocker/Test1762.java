package com.prismaqf.callblocker;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test1762 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.EditFiltersTest#NewFilterTest/Trace-1651091782604.txt
     * Method invocation under test: <com.prismaqf.callblocker.NewEditFilter: void onCreate(android.os.Bundle)>_170_340
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_NewEditFilter_onCreate_001() throws Exception {
        android.content.Intent intent0 = null;
        java.util.ArrayList arraylist1 = org.mockito.Mockito.mock(java.util.ArrayList.class);
        android.content.Context context17 = ApplicationProvider.getApplicationContext();
        intent0 = new android.content.Intent(context17, com.prismaqf.callblocker.NewEditFilter.class);
        android.content.Intent intent1 = intent0.putExtra("com.prismaqf.callblocker:action", "com.prismaqf.callblocker:create");
        android.content.Intent intent2 = intent1.putStringArrayListExtra("com.prismaqft.callblocker:rulenames", arraylist1);
        org.robolectric.android.controller.ActivityController activitycontroller35 = org.robolectric.Robolectric.buildActivity(com.prismaqf.callblocker.NewEditFilter.class, intent2);
        activitycontroller35.get();
        activitycontroller35.create();
    }
}

package com.prismaqf.callblocker.sql;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test483 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.EditFiltersTest#SelectFilterTest/Trace-1651091800209.txt
     * Method invocation under test: <com.prismaqf.callblocker.sql.DbHelper: void <init>(android.content.Context)>_105_209
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_sql_DbHelper_constructor_001() throws Exception {
        com.prismaqf.callblocker.sql.DbHelper dbhelper0 = null;
        android.content.Context context7 = ApplicationProvider.getApplicationContext();
        dbhelper0 = new com.prismaqf.callblocker.sql.DbHelper(context7);
    }
}

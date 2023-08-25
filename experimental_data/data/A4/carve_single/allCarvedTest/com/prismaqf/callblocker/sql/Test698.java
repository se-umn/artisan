package com.prismaqf.callblocker.sql;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test698 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /Users/mattia/Faculty/Research/2020_android_test_carving/repositories/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.EditFiltersTest#CreateAndSelectNewCalendarRule/Trace-1651091796523.txt
     * Method invocation under test: <com.prismaqf.callblocker.sql.DbHelper: android.database.sqlite.SQLiteDatabase getReadableDatabase()>_420_838
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_sql_DbHelper_getReadableDatabase_001() throws Exception {
        com.prismaqf.callblocker.sql.DbHelper dbhelper2 = null;
        android.content.Context context27 = ApplicationProvider.getApplicationContext();
        dbhelper2 = new com.prismaqf.callblocker.sql.DbHelper(context27);
        dbhelper2.getReadableDatabase();
    }
}

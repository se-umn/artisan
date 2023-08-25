package com.prismaqf.callblocker.sql;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test1478 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.EditFiltersTest#DeleteActionTest/Trace-1651091794492.txt
     * Method invocation under test: <com.prismaqf.callblocker.sql.DbHelper: android.database.sqlite.SQLiteDatabase getWritableDatabase()>_107_213
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_sql_DbHelper_getWritableDatabase_001() throws Exception {
        com.prismaqf.callblocker.sql.DbHelper dbhelper1 = null;
        android.content.Context context9 = ApplicationProvider.getApplicationContext();
        dbhelper1 = new com.prismaqf.callblocker.sql.DbHelper(context9);
        dbhelper1.getWritableDatabase();
    }
}

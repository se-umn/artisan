package com.prismaqf.callblocker.sql;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test1132 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.ManageFilterPatternsTest#PickFromLog/Trace-1651091669275.txt
     * Method invocation under test: <com.prismaqf.callblocker.sql.DbHelper: android.database.sqlite.SQLiteDatabase getReadableDatabase()>_318_635
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_sql_DbHelper_getReadableDatabase_001() throws Exception {
        com.prismaqf.callblocker.sql.DbHelper dbhelper1 = null;
        android.content.Context context25 = ApplicationProvider.getApplicationContext();
        dbhelper1 = new com.prismaqf.callblocker.sql.DbHelper(context25);
        dbhelper1.getReadableDatabase();
    }
}

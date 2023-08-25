package com.prismaqf.callblocker.sql;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test067 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.ManageFilterPatternsTest#TestCheckingPersistsWithRotation/Trace-1651091676947.txt
     * Method invocation under test: <com.prismaqf.callblocker.sql.DbHelper: android.database.sqlite.SQLiteDatabase getReadableDatabase()>_509_1017
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_sql_DbHelper_getReadableDatabase_002() throws Exception {
        com.prismaqf.callblocker.sql.DbHelper dbhelper3 = null;
        android.content.Context context63 = ApplicationProvider.getApplicationContext();
        dbhelper3 = new com.prismaqf.callblocker.sql.DbHelper(context63);
        dbhelper3.getReadableDatabase();
    }
}

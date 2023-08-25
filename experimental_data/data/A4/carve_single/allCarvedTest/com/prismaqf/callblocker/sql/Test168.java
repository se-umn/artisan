package com.prismaqf.callblocker.sql;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test168 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /Users/mattia/Faculty/Research/2020_android_test_carving/repositories/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.NewCalendarRuleTest#TestEmptyNameShouldFlag/Trace-1651091735786.txt
     * Method invocation under test: <com.prismaqf.callblocker.sql.DbHelper: android.database.sqlite.SQLiteDatabase getWritableDatabase()>_31_60
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_sql_DbHelper_getWritableDatabase_001() throws Exception {
        com.prismaqf.callblocker.sql.DbHelper dbhelper1 = null;
        android.content.Context context3 = ApplicationProvider.getApplicationContext();
        dbhelper1 = new com.prismaqf.callblocker.sql.DbHelper(context3);
        dbhelper1.getWritableDatabase();
    }
}

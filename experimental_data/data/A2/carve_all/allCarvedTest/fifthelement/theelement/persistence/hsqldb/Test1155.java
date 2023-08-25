package fifthelement.theelement.persistence.hsqldb;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;
import fifthelement.theelement.application.Main;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

public class Test1155 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.CustomizeTests#checkStatsPage/Trace-1650046501797.txt
     * Method invocation under test: <fifthelement.theelement.persistence.hsqldb.AlbumPersistenceHSQLDB: void <init>(java.lang.String)>_71_138
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_persistence_hsqldb_AlbumPersistenceHSQLDB_constructor_001() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        fifthelement.theelement.persistence.hsqldb.AlbumPersistenceHSQLDB albumpersistencehsqldb0 = null;
        albumpersistencehsqldb0 = new fifthelement.theelement.persistence.hsqldb.AlbumPersistenceHSQLDB(Main.getDBPathName());
    }
}

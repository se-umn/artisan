package fifthelement.theelement.persistence.hsqldb;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;
import fifthelement.theelement.application.Main;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

public class Test8404 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.CustomizeTests#deleteSong/Trace-1650046523752.txt
     * Method invocation under test: <fifthelement.theelement.persistence.hsqldb.SongPersistenceHSQLDB: java.util.List getAllSongs()>_192_382
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_persistence_hsqldb_SongPersistenceHSQLDB_getAllSongs_002() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        fifthelement.theelement.persistence.hsqldb.SongPersistenceHSQLDB songpersistencehsqldb2 = null;
        songpersistencehsqldb2 = new fifthelement.theelement.persistence.hsqldb.SongPersistenceHSQLDB(Main.getDBPathName());
        songpersistencehsqldb2.getAllSongs();
        songpersistencehsqldb2.getAllSongs();
    }
}

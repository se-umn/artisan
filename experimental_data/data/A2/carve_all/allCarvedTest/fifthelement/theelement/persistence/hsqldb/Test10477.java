package fifthelement.theelement.persistence.hsqldb;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;
import fifthelement.theelement.application.Main;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

public class Test10477 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.MusicLibraryTests#organizeMusicCollectionTest/Trace-1650046539302.txt
     * Method invocation under test: <fifthelement.theelement.persistence.hsqldb.SongPersistenceHSQLDB: java.util.List getAllSongs()>_672_1341
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_persistence_hsqldb_SongPersistenceHSQLDB_getAllSongs_005() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        fifthelement.theelement.persistence.hsqldb.SongPersistenceHSQLDB songpersistencehsqldb5 = null;
        songpersistencehsqldb5 = new fifthelement.theelement.persistence.hsqldb.SongPersistenceHSQLDB(Main.getDBPathName());
        songpersistencehsqldb5.getAllSongs();
        songpersistencehsqldb5.getAllSongs();
        songpersistencehsqldb5.getAllSongs();
        songpersistencehsqldb5.getAllSongs();
        songpersistencehsqldb5.getAllSongs();
    }
}

package fifthelement.theelement.persistence.hsqldb;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;
import fifthelement.theelement.application.Main;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

public class Test9558 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.PlayMusicTests#playPausingTest/Trace-1650046475193.txt
     * Method invocation under test: <fifthelement.theelement.persistence.hsqldb.AuthorPersistenceHSQLDB: fifthelement.theelement.objects.Author getAuthorByUUID(java.util.UUID)>_254_506
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_persistence_hsqldb_AuthorPersistenceHSQLDB_getAuthorByUUID_005() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        fifthelement.theelement.objects.Author author129 = null;
        fifthelement.theelement.persistence.hsqldb.AuthorPersistenceHSQLDB authorpersistencehsqldb5 = null;
        authorpersistencehsqldb5 = new fifthelement.theelement.persistence.hsqldb.AuthorPersistenceHSQLDB(Main.getDBPathName());
        java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid1305 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid1309 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.randomUUID();
        authorpersistencehsqldb5.getAuthorByUUID(uuid1305);
        authorpersistencehsqldb5.getAuthorByUUID(uuid1309);
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID uuid1313 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        authorpersistencehsqldb5.getAuthorByUUID(uuid1313);
        java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid1315 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author129 = new fifthelement.theelement.objects.Author(uuid1315, "");
        java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid1319 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        java.util.UUID uuid1320 = author129.getUUID();
        authorpersistencehsqldb5.getAuthorByUUID(uuid1320);
        authorpersistencehsqldb5.getAuthorByUUID(uuid1319);
    }
}

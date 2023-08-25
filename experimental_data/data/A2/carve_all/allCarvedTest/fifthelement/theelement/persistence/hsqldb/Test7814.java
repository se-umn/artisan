package fifthelement.theelement.persistence.hsqldb;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;
import fifthelement.theelement.application.Main;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

public class Test7814 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.SettingsTest#deleteSongsTest/Trace-1650046437980.txt
     * Method invocation under test: <fifthelement.theelement.persistence.hsqldb.AuthorPersistenceHSQLDB: fifthelement.theelement.objects.Author getAuthorByUUID(java.util.UUID)>_1299_2594
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_persistence_hsqldb_AuthorPersistenceHSQLDB_getAuthorByUUID_019() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        fifthelement.theelement.objects.Author author1198 = null;
        fifthelement.theelement.objects.Author author1199 = null;
        fifthelement.theelement.objects.Author author1201 = null;
        fifthelement.theelement.objects.Author author1202 = null;
        fifthelement.theelement.objects.Author author1204 = null;
        fifthelement.theelement.objects.Author author1209 = null;
        fifthelement.theelement.objects.Author author1210 = null;
        fifthelement.theelement.objects.Author author1215 = null;
        fifthelement.theelement.objects.Author author1219 = null;
        fifthelement.theelement.objects.Author author1224 = null;
        fifthelement.theelement.persistence.hsqldb.AuthorPersistenceHSQLDB authorpersistencehsqldb23 = null;
        fifthelement.theelement.objects.Author author1225 = null;
        authorpersistencehsqldb23 = new fifthelement.theelement.persistence.hsqldb.AuthorPersistenceHSQLDB(Main.getDBPathName());
        java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid33223 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid33227 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.randomUUID();
        authorpersistencehsqldb23.getAuthorByUUID(uuid33223);
        authorpersistencehsqldb23.getAuthorByUUID(uuid33227);
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID uuid33231 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        authorpersistencehsqldb23.getAuthorByUUID(uuid33231);
        java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid33233 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author1209 = new fifthelement.theelement.objects.Author(uuid33233, "");
        java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid33237 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.randomUUID();
        java.util.UUID uuid33240 = author1209.getUUID();
        authorpersistencehsqldb23.getAuthorByUUID(uuid33240);
        authorpersistencehsqldb23.getAuthorByUUID(uuid33237);
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID uuid33242 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author1204 = new fifthelement.theelement.objects.Author(uuid33242, "");
        java.util.UUID uuid33243 = author1204.getUUID();
        authorpersistencehsqldb23.getAuthorByUUID(uuid33243);
        java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid33245 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid33249 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author1198 = new fifthelement.theelement.objects.Author(uuid33249, "");
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.randomUUID();
        authorpersistencehsqldb23.getAuthorByUUID(uuid33245);
        java.util.UUID uuid33252 = author1198.getUUID();
        authorpersistencehsqldb23.getAuthorByUUID(uuid33252);
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID uuid33254 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author1201 = new fifthelement.theelement.objects.Author(uuid33254, "");
        java.util.UUID uuid33255 = author1201.getUUID();
        authorpersistencehsqldb23.getAuthorByUUID(uuid33255);
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        authorpersistencehsqldb23.getAllAuthors();
        java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid33261 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid33265 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author1224 = new fifthelement.theelement.objects.Author(uuid33265, "");
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.randomUUID();
        authorpersistencehsqldb23.getAuthorByUUID(uuid33261);
        java.util.UUID uuid33268 = author1224.getUUID();
        authorpersistencehsqldb23.getAuthorByUUID(uuid33268);
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID uuid33270 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        authorpersistencehsqldb23.getAuthorByUUID(uuid33270);
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        authorpersistencehsqldb23.getAllAuthors();
        java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid33276 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author1202 = new fifthelement.theelement.objects.Author(uuid33276, "");
        java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid33280 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author1225 = new fifthelement.theelement.objects.Author(uuid33280, "");
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.randomUUID();
        java.util.UUID uuid33283 = author1202.getUUID();
        authorpersistencehsqldb23.getAuthorByUUID(uuid33283);
        java.util.UUID uuid33284 = author1225.getUUID();
        authorpersistencehsqldb23.getAuthorByUUID(uuid33284);
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID uuid33286 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author1210 = new fifthelement.theelement.objects.Author(uuid33286, "");
        java.util.UUID uuid33287 = author1210.getUUID();
        authorpersistencehsqldb23.getAuthorByUUID(uuid33287);
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        authorpersistencehsqldb23.getAllAuthors();
        authorpersistencehsqldb23.getAllAuthors();
        java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid33293 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid33297 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author1199 = new fifthelement.theelement.objects.Author(uuid33297, "");
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.randomUUID();
        authorpersistencehsqldb23.getAuthorByUUID(uuid33293);
        java.util.UUID uuid33300 = author1199.getUUID();
        authorpersistencehsqldb23.getAuthorByUUID(uuid33300);
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID uuid33302 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author1215 = new fifthelement.theelement.objects.Author(uuid33302, "");
        java.util.UUID uuid33303 = author1215.getUUID();
        authorpersistencehsqldb23.getAuthorByUUID(uuid33303);
        java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid33305 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author1219 = new fifthelement.theelement.objects.Author(uuid33305, "");
        java.util.UUID uuid33306 = author1219.getUUID();
        authorpersistencehsqldb23.getAuthorByUUID(uuid33306);
    }
}

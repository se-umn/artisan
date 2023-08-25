package fifthelement.theelement.persistence.hsqldb;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;
import fifthelement.theelement.application.Main;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

public class Test666 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.CustomizeTests#skipSongWithSongPlayedCheck/Trace-1650046492781.txt
     * Method invocation under test: <fifthelement.theelement.persistence.hsqldb.AuthorPersistenceHSQLDB: boolean updateAuthor(fifthelement.theelement.objects.Author)>_2166_4329
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_persistence_hsqldb_AuthorPersistenceHSQLDB_updateAuthor_001() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        fifthelement.theelement.objects.Author author8127 = null;
        fifthelement.theelement.objects.Author author8129 = null;
        fifthelement.theelement.objects.Author author8132 = null;
        fifthelement.theelement.objects.Author author8133 = null;
        fifthelement.theelement.objects.Author author8134 = null;
        fifthelement.theelement.objects.Author author8135 = null;
        fifthelement.theelement.objects.Author author8141 = null;
        fifthelement.theelement.persistence.hsqldb.AuthorPersistenceHSQLDB authorpersistencehsqldb34 = null;
        fifthelement.theelement.objects.Author author8142 = null;
        fifthelement.theelement.objects.Author author8144 = null;
        fifthelement.theelement.objects.Author author8145 = null;
        fifthelement.theelement.objects.Author author8149 = null;
        fifthelement.theelement.objects.Author author8158 = null;
        fifthelement.theelement.objects.Author author8160 = null;
        fifthelement.theelement.objects.Author author8162 = null;
        fifthelement.theelement.objects.Author author8168 = null;
        fifthelement.theelement.objects.Author author8170 = null;
        authorpersistencehsqldb34 = new fifthelement.theelement.persistence.hsqldb.AuthorPersistenceHSQLDB(Main.getDBPathName());
        java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid99359 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid99363 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.randomUUID();
        authorpersistencehsqldb34.getAuthorByUUID(uuid99359);
        authorpersistencehsqldb34.getAuthorByUUID(uuid99363);
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID uuid99367 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        authorpersistencehsqldb34.getAuthorByUUID(uuid99367);
        java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid99369 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author8133 = new fifthelement.theelement.objects.Author(uuid99369, "");
        java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid99373 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.randomUUID();
        java.util.UUID uuid99376 = author8133.getUUID();
        authorpersistencehsqldb34.getAuthorByUUID(uuid99376);
        authorpersistencehsqldb34.getAuthorByUUID(uuid99373);
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID uuid99378 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author8162 = new fifthelement.theelement.objects.Author(uuid99378, "");
        java.util.UUID uuid99379 = author8162.getUUID();
        authorpersistencehsqldb34.getAuthorByUUID(uuid99379);
        java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid99381 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid99385 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author8149 = new fifthelement.theelement.objects.Author(uuid99385, "");
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.randomUUID();
        authorpersistencehsqldb34.getAuthorByUUID(uuid99381);
        java.util.UUID uuid99388 = author8149.getUUID();
        authorpersistencehsqldb34.getAuthorByUUID(uuid99388);
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID uuid99390 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author8135 = new fifthelement.theelement.objects.Author(uuid99390, "");
        java.util.UUID uuid99391 = author8135.getUUID();
        authorpersistencehsqldb34.getAuthorByUUID(uuid99391);
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        authorpersistencehsqldb34.getAllAuthors();
        java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid99397 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid99401 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author8127 = new fifthelement.theelement.objects.Author(uuid99401, "");
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.randomUUID();
        authorpersistencehsqldb34.getAuthorByUUID(uuid99397);
        java.util.UUID uuid99404 = author8127.getUUID();
        authorpersistencehsqldb34.getAuthorByUUID(uuid99404);
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID uuid99406 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        authorpersistencehsqldb34.getAuthorByUUID(uuid99406);
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        authorpersistencehsqldb34.getAllAuthors();
        java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid99412 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author8134 = new fifthelement.theelement.objects.Author(uuid99412, "");
        java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid99416 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author8168 = new fifthelement.theelement.objects.Author(uuid99416, "");
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.randomUUID();
        java.util.UUID uuid99419 = author8134.getUUID();
        authorpersistencehsqldb34.getAuthorByUUID(uuid99419);
        java.util.UUID uuid99420 = author8168.getUUID();
        authorpersistencehsqldb34.getAuthorByUUID(uuid99420);
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID uuid99422 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author8132 = new fifthelement.theelement.objects.Author(uuid99422, "");
        java.util.UUID uuid99423 = author8132.getUUID();
        authorpersistencehsqldb34.getAuthorByUUID(uuid99423);
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        authorpersistencehsqldb34.getAllAuthors();
        authorpersistencehsqldb34.getAllAuthors();
        java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid99429 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid99433 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author8160 = new fifthelement.theelement.objects.Author(uuid99433, "");
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.randomUUID();
        authorpersistencehsqldb34.getAuthorByUUID(uuid99429);
        java.util.UUID uuid99436 = author8160.getUUID();
        authorpersistencehsqldb34.getAuthorByUUID(uuid99436);
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID uuid99438 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author8158 = new fifthelement.theelement.objects.Author(uuid99438, "");
        java.util.UUID uuid99439 = author8158.getUUID();
        authorpersistencehsqldb34.getAuthorByUUID(uuid99439);
        java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid99441 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author8141 = new fifthelement.theelement.objects.Author(uuid99441, "");
        java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid99445 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author8129 = new fifthelement.theelement.objects.Author(uuid99445, "");
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.randomUUID();
        java.util.UUID uuid99448 = author8141.getUUID();
        authorpersistencehsqldb34.getAuthorByUUID(uuid99448);
        java.util.UUID uuid99449 = author8129.getUUID();
        authorpersistencehsqldb34.getAuthorByUUID(uuid99449);
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID uuid99451 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        authorpersistencehsqldb34.getAuthorByUUID(uuid99451);
        java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid99453 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author8144 = new fifthelement.theelement.objects.Author(uuid99453, "");
        java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid99457 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.randomUUID();
        java.util.UUID uuid99460 = author8144.getUUID();
        authorpersistencehsqldb34.getAuthorByUUID(uuid99460);
        authorpersistencehsqldb34.getAuthorByUUID(uuid99457);
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID uuid99462 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        authorpersistencehsqldb34.getAuthorByUUID(uuid99462);
        java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid99464 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author8142 = new fifthelement.theelement.objects.Author(uuid99464, "");
        java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid99468 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author8170 = new fifthelement.theelement.objects.Author(uuid99468, "");
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.randomUUID();
        java.util.UUID uuid99471 = author8142.getUUID();
        authorpersistencehsqldb34.getAuthorByUUID(uuid99471);
        java.util.UUID uuid99472 = author8170.getUUID();
        authorpersistencehsqldb34.getAuthorByUUID(uuid99472);
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID uuid99474 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author8145 = new fifthelement.theelement.objects.Author(uuid99474, "");
        java.util.UUID uuid99475 = author8145.getUUID();
        authorpersistencehsqldb34.getAuthorByUUID(uuid99475);
        java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid99477 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.randomUUID();
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        fifthelement.theelement.objects.Author author8198 = authorpersistencehsqldb34.getAuthorByUUID(uuid99477);
        java.util.UUID uuid99482 = author8198.getUUID();
        fifthelement.theelement.objects.Author author8199 = authorpersistencehsqldb34.getAuthorByUUID(uuid99482);
        author8199.incrNumPlayed();
        authorpersistencehsqldb34.updateAuthor(author8199);
    }
}

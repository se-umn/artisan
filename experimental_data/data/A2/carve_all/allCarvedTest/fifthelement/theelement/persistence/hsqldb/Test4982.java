package fifthelement.theelement.persistence.hsqldb;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;
import fifthelement.theelement.application.Main;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

public class Test4982 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.CustomizeTests#playSongWithSongPlayedCheck/Trace-1650046505062.txt
     * Method invocation under test: <fifthelement.theelement.persistence.hsqldb.AuthorPersistenceHSQLDB: boolean updateAuthor(fifthelement.theelement.objects.Author)>_2792_5581
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_persistence_hsqldb_AuthorPersistenceHSQLDB_updateAuthor_002() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        fifthelement.theelement.objects.Author author12665 = null;
        fifthelement.theelement.objects.Author author12667 = null;
        fifthelement.theelement.objects.Author author12670 = null;
        fifthelement.theelement.objects.Author author12673 = null;
        fifthelement.theelement.objects.Author author12674 = null;
        fifthelement.theelement.objects.Author author12675 = null;
        fifthelement.theelement.objects.Author author12681 = null;
        fifthelement.theelement.persistence.hsqldb.AuthorPersistenceHSQLDB authorpersistencehsqldb40 = null;
        fifthelement.theelement.objects.Author author12682 = null;
        fifthelement.theelement.objects.Author author12684 = null;
        fifthelement.theelement.objects.Author author12685 = null;
        fifthelement.theelement.objects.Author author12691 = null;
        fifthelement.theelement.objects.Author author12700 = null;
        fifthelement.theelement.objects.Author author12702 = null;
        fifthelement.theelement.objects.Author author12705 = null;
        fifthelement.theelement.objects.Author author12711 = null;
        fifthelement.theelement.objects.Author author12713 = null;
        java.util.UUID uuid151073 = org.mockito.Mockito.mock(java.util.UUID.class);
        org.mockito.stubbing.Stubber stubber24853 = org.mockito.Mockito.doReturn("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        java.util.UUID uuid151074 = stubber24853.when(uuid151073);
        uuid151074.toString();
        authorpersistencehsqldb40 = new fifthelement.theelement.persistence.hsqldb.AuthorPersistenceHSQLDB(Main.getDBPathName());
        java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid151076 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid151080 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.randomUUID();
        authorpersistencehsqldb40.getAuthorByUUID(uuid151076);
        authorpersistencehsqldb40.getAuthorByUUID(uuid151080);
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID uuid151084 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        authorpersistencehsqldb40.getAuthorByUUID(uuid151084);
        java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid151086 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author12673 = new fifthelement.theelement.objects.Author(uuid151086, "");
        java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid151090 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.randomUUID();
        java.util.UUID uuid151093 = author12673.getUUID();
        authorpersistencehsqldb40.getAuthorByUUID(uuid151093);
        authorpersistencehsqldb40.getAuthorByUUID(uuid151090);
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID uuid151095 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author12705 = new fifthelement.theelement.objects.Author(uuid151095, "");
        java.util.UUID uuid151096 = author12705.getUUID();
        authorpersistencehsqldb40.getAuthorByUUID(uuid151096);
        java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid151098 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid151102 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author12691 = new fifthelement.theelement.objects.Author(uuid151102, "");
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.randomUUID();
        authorpersistencehsqldb40.getAuthorByUUID(uuid151098);
        java.util.UUID uuid151105 = author12691.getUUID();
        authorpersistencehsqldb40.getAuthorByUUID(uuid151105);
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID uuid151107 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author12675 = new fifthelement.theelement.objects.Author(uuid151107, "");
        java.util.UUID uuid151108 = author12675.getUUID();
        authorpersistencehsqldb40.getAuthorByUUID(uuid151108);
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        authorpersistencehsqldb40.getAllAuthors();
        java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid151114 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid151118 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author12665 = new fifthelement.theelement.objects.Author(uuid151118, "");
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.randomUUID();
        authorpersistencehsqldb40.getAuthorByUUID(uuid151114);
        java.util.UUID uuid151121 = author12665.getUUID();
        authorpersistencehsqldb40.getAuthorByUUID(uuid151121);
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID uuid151123 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        authorpersistencehsqldb40.getAuthorByUUID(uuid151123);
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        authorpersistencehsqldb40.getAllAuthors();
        java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid151129 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author12674 = new fifthelement.theelement.objects.Author(uuid151129, "");
        java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid151133 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author12711 = new fifthelement.theelement.objects.Author(uuid151133, "");
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.randomUUID();
        java.util.UUID uuid151136 = author12674.getUUID();
        authorpersistencehsqldb40.getAuthorByUUID(uuid151136);
        java.util.UUID uuid151137 = author12711.getUUID();
        authorpersistencehsqldb40.getAuthorByUUID(uuid151137);
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID uuid151139 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author12670 = new fifthelement.theelement.objects.Author(uuid151139, "");
        java.util.UUID uuid151140 = author12670.getUUID();
        authorpersistencehsqldb40.getAuthorByUUID(uuid151140);
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        authorpersistencehsqldb40.getAllAuthors();
        authorpersistencehsqldb40.getAllAuthors();
        java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid151146 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid151150 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author12702 = new fifthelement.theelement.objects.Author(uuid151150, "");
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.randomUUID();
        authorpersistencehsqldb40.getAuthorByUUID(uuid151146);
        java.util.UUID uuid151153 = author12702.getUUID();
        authorpersistencehsqldb40.getAuthorByUUID(uuid151153);
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID uuid151155 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author12700 = new fifthelement.theelement.objects.Author(uuid151155, "");
        java.util.UUID uuid151156 = author12700.getUUID();
        authorpersistencehsqldb40.getAuthorByUUID(uuid151156);
        java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid151158 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author12681 = new fifthelement.theelement.objects.Author(uuid151158, "");
        java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid151162 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author12667 = new fifthelement.theelement.objects.Author(uuid151162, "");
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.randomUUID();
        java.util.UUID uuid151165 = author12681.getUUID();
        authorpersistencehsqldb40.getAuthorByUUID(uuid151165);
        java.util.UUID uuid151166 = author12667.getUUID();
        authorpersistencehsqldb40.getAuthorByUUID(uuid151166);
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID uuid151168 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        authorpersistencehsqldb40.getAuthorByUUID(uuid151168);
        java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid151170 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author12684 = new fifthelement.theelement.objects.Author(uuid151170, "");
        java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid151174 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.randomUUID();
        java.util.UUID uuid151177 = author12684.getUUID();
        authorpersistencehsqldb40.getAuthorByUUID(uuid151177);
        authorpersistencehsqldb40.getAuthorByUUID(uuid151174);
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID uuid151179 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        authorpersistencehsqldb40.getAuthorByUUID(uuid151179);
        java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid151181 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author12682 = new fifthelement.theelement.objects.Author(uuid151181, "");
        java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid151185 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author12713 = new fifthelement.theelement.objects.Author(uuid151185, "");
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.randomUUID();
        java.util.UUID uuid151188 = author12682.getUUID();
        authorpersistencehsqldb40.getAuthorByUUID(uuid151188);
        java.util.UUID uuid151189 = author12713.getUUID();
        authorpersistencehsqldb40.getAuthorByUUID(uuid151189);
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID uuid151191 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author12685 = new fifthelement.theelement.objects.Author(uuid151191, "");
        java.util.UUID uuid151192 = author12685.getUUID();
        authorpersistencehsqldb40.getAuthorByUUID(uuid151192);
        java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid151194 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.randomUUID();
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        fifthelement.theelement.objects.Author author12741 = authorpersistencehsqldb40.getAuthorByUUID(uuid151194);
        java.util.UUID uuid151199 = author12741.getUUID();
        fifthelement.theelement.objects.Author author12742 = authorpersistencehsqldb40.getAuthorByUUID(uuid151199);
        author12742.incrNumPlayed();
        authorpersistencehsqldb40.updateAuthor(author12742);
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.randomUUID();
        uuid151199.toString();
        java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid151209 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid151213 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.randomUUID();
        authorpersistencehsqldb40.getAuthorByUUID(uuid151209);
        authorpersistencehsqldb40.getAuthorByUUID(uuid151213);
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID uuid151217 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        authorpersistencehsqldb40.getAuthorByUUID(uuid151217);
        java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid151219 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.randomUUID();
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        authorpersistencehsqldb40.getAuthorByUUID(uuid151219);
        fifthelement.theelement.objects.Author author12747 = authorpersistencehsqldb40.getAuthorByUUID(uuid151074);
        author12747.incrNumPlayed();
        authorpersistencehsqldb40.updateAuthor(author12747);
    }
}

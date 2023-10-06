package fifthelement.theelement.persistence.hsqldb;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;
import fifthelement.theelement.application.Main;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

public class Test608 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.CustomizeTests#skipSongWithSongPlayedCheck/Trace-1650046492781.txt
     * Method invocation under test: <fifthelement.theelement.persistence.hsqldb.AuthorPersistenceHSQLDB: fifthelement.theelement.objects.Author getAuthorByUUID(java.util.UUID)>_2018_4034
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_persistence_hsqldb_AuthorPersistenceHSQLDB_getAuthorByUUID_026() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        fifthelement.theelement.objects.Author author7351 = null;
        fifthelement.theelement.objects.Author author7353 = null;
        fifthelement.theelement.objects.Author author7356 = null;
        fifthelement.theelement.objects.Author author7357 = null;
        fifthelement.theelement.objects.Author author7358 = null;
        fifthelement.theelement.objects.Author author7359 = null;
        fifthelement.theelement.objects.Author author7364 = null;
        fifthelement.theelement.persistence.hsqldb.AuthorPersistenceHSQLDB authorpersistencehsqldb30 = null;
        fifthelement.theelement.objects.Author author7365 = null;
        fifthelement.theelement.objects.Author author7367 = null;
        fifthelement.theelement.objects.Author author7371 = null;
        fifthelement.theelement.objects.Author author7379 = null;
        fifthelement.theelement.objects.Author author7381 = null;
        fifthelement.theelement.objects.Author author7383 = null;
        fifthelement.theelement.objects.Author author7388 = null;
        fifthelement.theelement.objects.Author author7390 = null;
        authorpersistencehsqldb30 = new fifthelement.theelement.persistence.hsqldb.AuthorPersistenceHSQLDB(Main.getDBPathName());
        java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid84606 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid84610 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.randomUUID();
        authorpersistencehsqldb30.getAuthorByUUID(uuid84606);
        authorpersistencehsqldb30.getAuthorByUUID(uuid84610);
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID uuid84614 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        authorpersistencehsqldb30.getAuthorByUUID(uuid84614);
        java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid84616 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author7357 = new fifthelement.theelement.objects.Author(uuid84616, "");
        java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid84620 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.randomUUID();
        java.util.UUID uuid84623 = author7357.getUUID();
        authorpersistencehsqldb30.getAuthorByUUID(uuid84623);
        authorpersistencehsqldb30.getAuthorByUUID(uuid84620);
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID uuid84625 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author7383 = new fifthelement.theelement.objects.Author(uuid84625, "");
        java.util.UUID uuid84626 = author7383.getUUID();
        authorpersistencehsqldb30.getAuthorByUUID(uuid84626);
        java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid84628 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid84632 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author7371 = new fifthelement.theelement.objects.Author(uuid84632, "");
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.randomUUID();
        authorpersistencehsqldb30.getAuthorByUUID(uuid84628);
        java.util.UUID uuid84635 = author7371.getUUID();
        authorpersistencehsqldb30.getAuthorByUUID(uuid84635);
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID uuid84637 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author7359 = new fifthelement.theelement.objects.Author(uuid84637, "");
        java.util.UUID uuid84638 = author7359.getUUID();
        authorpersistencehsqldb30.getAuthorByUUID(uuid84638);
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        authorpersistencehsqldb30.getAllAuthors();
        java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid84644 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid84648 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author7351 = new fifthelement.theelement.objects.Author(uuid84648, "");
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.randomUUID();
        authorpersistencehsqldb30.getAuthorByUUID(uuid84644);
        java.util.UUID uuid84651 = author7351.getUUID();
        authorpersistencehsqldb30.getAuthorByUUID(uuid84651);
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID uuid84653 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        authorpersistencehsqldb30.getAuthorByUUID(uuid84653);
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        authorpersistencehsqldb30.getAllAuthors();
        java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid84659 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author7358 = new fifthelement.theelement.objects.Author(uuid84659, "");
        java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid84663 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author7388 = new fifthelement.theelement.objects.Author(uuid84663, "");
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.randomUUID();
        java.util.UUID uuid84666 = author7358.getUUID();
        authorpersistencehsqldb30.getAuthorByUUID(uuid84666);
        java.util.UUID uuid84667 = author7388.getUUID();
        authorpersistencehsqldb30.getAuthorByUUID(uuid84667);
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID uuid84669 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author7356 = new fifthelement.theelement.objects.Author(uuid84669, "");
        java.util.UUID uuid84670 = author7356.getUUID();
        authorpersistencehsqldb30.getAuthorByUUID(uuid84670);
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        authorpersistencehsqldb30.getAllAuthors();
        authorpersistencehsqldb30.getAllAuthors();
        java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid84676 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid84680 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author7381 = new fifthelement.theelement.objects.Author(uuid84680, "");
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.randomUUID();
        authorpersistencehsqldb30.getAuthorByUUID(uuid84676);
        java.util.UUID uuid84683 = author7381.getUUID();
        authorpersistencehsqldb30.getAuthorByUUID(uuid84683);
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID uuid84685 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author7379 = new fifthelement.theelement.objects.Author(uuid84685, "");
        java.util.UUID uuid84686 = author7379.getUUID();
        authorpersistencehsqldb30.getAuthorByUUID(uuid84686);
        java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid84688 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author7364 = new fifthelement.theelement.objects.Author(uuid84688, "");
        java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid84692 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author7353 = new fifthelement.theelement.objects.Author(uuid84692, "");
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.randomUUID();
        java.util.UUID uuid84695 = author7364.getUUID();
        authorpersistencehsqldb30.getAuthorByUUID(uuid84695);
        java.util.UUID uuid84696 = author7353.getUUID();
        authorpersistencehsqldb30.getAuthorByUUID(uuid84696);
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID uuid84698 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        authorpersistencehsqldb30.getAuthorByUUID(uuid84698);
        java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid84700 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author7367 = new fifthelement.theelement.objects.Author(uuid84700, "");
        java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid84704 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.randomUUID();
        java.util.UUID uuid84707 = author7367.getUUID();
        authorpersistencehsqldb30.getAuthorByUUID(uuid84707);
        authorpersistencehsqldb30.getAuthorByUUID(uuid84704);
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID uuid84709 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        authorpersistencehsqldb30.getAuthorByUUID(uuid84709);
        java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid84711 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author7365 = new fifthelement.theelement.objects.Author(uuid84711, "");
        java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid84715 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author7390 = new fifthelement.theelement.objects.Author(uuid84715, "");
        java.util.UUID uuid84716 = author7365.getUUID();
        authorpersistencehsqldb30.getAuthorByUUID(uuid84716);
        java.util.UUID uuid84717 = author7390.getUUID();
        authorpersistencehsqldb30.getAuthorByUUID(uuid84717);
    }
}
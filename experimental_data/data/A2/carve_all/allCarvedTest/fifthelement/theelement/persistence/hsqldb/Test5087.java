package fifthelement.theelement.persistence.hsqldb;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;
import fifthelement.theelement.application.Main;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

public class Test5087 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.CustomizeTests#playSongWithSongPlayedCheck/Trace-1650046505062.txt
     * Method invocation under test: <fifthelement.theelement.persistence.hsqldb.AuthorPersistenceHSQLDB: fifthelement.theelement.objects.Author getAuthorByUUID(java.util.UUID)>_3094_6185
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_persistence_hsqldb_AuthorPersistenceHSQLDB_getAuthorByUUID_037() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        fifthelement.theelement.objects.Author author14470 = null;
        fifthelement.theelement.objects.Author author14471 = null;
        fifthelement.theelement.persistence.hsqldb.AuthorPersistenceHSQLDB authorpersistencehsqldb43 = null;
        fifthelement.theelement.objects.Author author14475 = null;
        fifthelement.theelement.objects.Author author14477 = null;
        fifthelement.theelement.objects.Author author14478 = null;
        fifthelement.theelement.objects.Author author14481 = null;
        fifthelement.theelement.objects.Author author14490 = null;
        fifthelement.theelement.objects.Author author14491 = null;
        fifthelement.theelement.objects.Author author14493 = null;
        fifthelement.theelement.objects.Author author14497 = null;
        fifthelement.theelement.objects.Author author14498 = null;
        fifthelement.theelement.objects.Author author14503 = null;
        fifthelement.theelement.objects.Author author14504 = null;
        fifthelement.theelement.objects.Author author14507 = null;
        fifthelement.theelement.objects.Author author14510 = null;
        fifthelement.theelement.objects.Author author14520 = null;
        fifthelement.theelement.objects.Author author14521 = null;
        java.util.UUID uuid183331 = org.mockito.Mockito.mock(java.util.UUID.class);
        org.mockito.stubbing.Stubber stubber29886 = org.mockito.Mockito.doReturn("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        java.util.UUID uuid183332 = stubber29886.when(uuid183331);
        uuid183332.toString();
        org.mockito.stubbing.Stubber stubber29887 = org.mockito.Mockito.doReturn("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        java.util.UUID uuid183333 = stubber29887.when(uuid183331);
        uuid183333.toString();
        authorpersistencehsqldb43 = new fifthelement.theelement.persistence.hsqldb.AuthorPersistenceHSQLDB(Main.getDBPathName());
        java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid183335 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid183339 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.randomUUID();
        authorpersistencehsqldb43.getAuthorByUUID(uuid183335);
        authorpersistencehsqldb43.getAuthorByUUID(uuid183339);
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID uuid183343 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        authorpersistencehsqldb43.getAuthorByUUID(uuid183343);
        java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid183345 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author14503 = new fifthelement.theelement.objects.Author(uuid183345, "");
        java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid183349 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.randomUUID();
        java.util.UUID uuid183352 = author14503.getUUID();
        authorpersistencehsqldb43.getAuthorByUUID(uuid183352);
        authorpersistencehsqldb43.getAuthorByUUID(uuid183349);
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID uuid183354 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author14493 = new fifthelement.theelement.objects.Author(uuid183354, "");
        java.util.UUID uuid183355 = author14493.getUUID();
        authorpersistencehsqldb43.getAuthorByUUID(uuid183355);
        java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid183357 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid183361 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author14510 = new fifthelement.theelement.objects.Author(uuid183361, "");
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.randomUUID();
        authorpersistencehsqldb43.getAuthorByUUID(uuid183357);
        java.util.UUID uuid183364 = author14510.getUUID();
        authorpersistencehsqldb43.getAuthorByUUID(uuid183364);
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID uuid183366 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author14471 = new fifthelement.theelement.objects.Author(uuid183366, "");
        java.util.UUID uuid183367 = author14471.getUUID();
        authorpersistencehsqldb43.getAuthorByUUID(uuid183367);
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        authorpersistencehsqldb43.getAllAuthors();
        java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid183373 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid183377 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author14497 = new fifthelement.theelement.objects.Author(uuid183377, "");
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.randomUUID();
        authorpersistencehsqldb43.getAuthorByUUID(uuid183373);
        java.util.UUID uuid183380 = author14497.getUUID();
        authorpersistencehsqldb43.getAuthorByUUID(uuid183380);
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID uuid183382 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        authorpersistencehsqldb43.getAuthorByUUID(uuid183382);
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        authorpersistencehsqldb43.getAllAuthors();
        java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid183388 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author14504 = new fifthelement.theelement.objects.Author(uuid183388, "");
        java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid183392 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author14520 = new fifthelement.theelement.objects.Author(uuid183392, "");
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.randomUUID();
        java.util.UUID uuid183395 = author14504.getUUID();
        authorpersistencehsqldb43.getAuthorByUUID(uuid183395);
        java.util.UUID uuid183396 = author14520.getUUID();
        authorpersistencehsqldb43.getAuthorByUUID(uuid183396);
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID uuid183398 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author14470 = new fifthelement.theelement.objects.Author(uuid183398, "");
        java.util.UUID uuid183399 = author14470.getUUID();
        authorpersistencehsqldb43.getAuthorByUUID(uuid183399);
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        authorpersistencehsqldb43.getAllAuthors();
        authorpersistencehsqldb43.getAllAuthors();
        java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid183405 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid183409 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author14491 = new fifthelement.theelement.objects.Author(uuid183409, "");
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.randomUUID();
        authorpersistencehsqldb43.getAuthorByUUID(uuid183405);
        java.util.UUID uuid183412 = author14491.getUUID();
        authorpersistencehsqldb43.getAuthorByUUID(uuid183412);
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID uuid183414 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author14490 = new fifthelement.theelement.objects.Author(uuid183414, "");
        java.util.UUID uuid183415 = author14490.getUUID();
        authorpersistencehsqldb43.getAuthorByUUID(uuid183415);
        java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid183417 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author14507 = new fifthelement.theelement.objects.Author(uuid183417, "");
        java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid183421 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author14498 = new fifthelement.theelement.objects.Author(uuid183421, "");
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.randomUUID();
        java.util.UUID uuid183424 = author14507.getUUID();
        authorpersistencehsqldb43.getAuthorByUUID(uuid183424);
        java.util.UUID uuid183425 = author14498.getUUID();
        authorpersistencehsqldb43.getAuthorByUUID(uuid183425);
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID uuid183427 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        authorpersistencehsqldb43.getAuthorByUUID(uuid183427);
        java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid183429 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author14477 = new fifthelement.theelement.objects.Author(uuid183429, "");
        java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid183433 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.randomUUID();
        java.util.UUID uuid183436 = author14477.getUUID();
        authorpersistencehsqldb43.getAuthorByUUID(uuid183436);
        authorpersistencehsqldb43.getAuthorByUUID(uuid183433);
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID uuid183438 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        authorpersistencehsqldb43.getAuthorByUUID(uuid183438);
        java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid183440 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author14475 = new fifthelement.theelement.objects.Author(uuid183440, "");
        java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid183444 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author14521 = new fifthelement.theelement.objects.Author(uuid183444, "");
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.randomUUID();
        java.util.UUID uuid183447 = author14475.getUUID();
        authorpersistencehsqldb43.getAuthorByUUID(uuid183447);
        java.util.UUID uuid183448 = author14521.getUUID();
        authorpersistencehsqldb43.getAuthorByUUID(uuid183448);
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID uuid183450 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author14478 = new fifthelement.theelement.objects.Author(uuid183450, "");
        java.util.UUID uuid183451 = author14478.getUUID();
        authorpersistencehsqldb43.getAuthorByUUID(uuid183451);
        java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid183453 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.randomUUID();
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        fifthelement.theelement.objects.Author author14549 = authorpersistencehsqldb43.getAuthorByUUID(uuid183453);
        java.util.UUID uuid183458 = author14549.getUUID();
        fifthelement.theelement.objects.Author author14550 = authorpersistencehsqldb43.getAuthorByUUID(uuid183458);
        author14550.incrNumPlayed();
        authorpersistencehsqldb43.updateAuthor(author14550);
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.randomUUID();
        uuid183458.toString();
        java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid183468 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid183472 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.randomUUID();
        authorpersistencehsqldb43.getAuthorByUUID(uuid183468);
        authorpersistencehsqldb43.getAuthorByUUID(uuid183472);
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID uuid183476 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        authorpersistencehsqldb43.getAuthorByUUID(uuid183476);
        java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid183478 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.randomUUID();
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        authorpersistencehsqldb43.getAuthorByUUID(uuid183478);
        fifthelement.theelement.objects.Author author14555 = authorpersistencehsqldb43.getAuthorByUUID(uuid183333);
        author14555.incrNumPlayed();
        authorpersistencehsqldb43.updateAuthor(author14555);
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.randomUUID();
        uuid183333.toString();
        java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid183492 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author14481 = new fifthelement.theelement.objects.Author(uuid183492, "");
        java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid183496 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.randomUUID();
        java.util.UUID uuid183499 = author14481.getUUID();
        authorpersistencehsqldb43.getAuthorByUUID(uuid183499);
        authorpersistencehsqldb43.getAuthorByUUID(uuid183496);
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID uuid183501 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        authorpersistencehsqldb43.getAuthorByUUID(uuid183501);
    }
}
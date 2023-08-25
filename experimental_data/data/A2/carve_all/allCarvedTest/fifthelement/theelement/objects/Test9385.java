package fifthelement.theelement.objects;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

public class Test9385 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.PlayMusicTests#shuffleSongTest/Trace-1650046488177.txt
     * Method invocation under test: <fifthelement.theelement.objects.Song: java.lang.String getPath()>_1123_2243
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_objects_Song_getPath_001() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        fifthelement.theelement.objects.Song song3666 = null;
        java.util.List list74 = null;
        fifthelement.theelement.objects.Author author3822 = null;
        fifthelement.theelement.objects.Album album1788 = null;
        fifthelement.theelement.objects.Author author3823 = null;
        fifthelement.theelement.objects.Album album1789 = null;
        fifthelement.theelement.objects.Author author3824 = null;
        fifthelement.theelement.objects.Author author3825 = null;
        java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        java.util.UUID uuid23537 = java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid23538 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author3823 = new fifthelement.theelement.objects.Author(uuid23538, "");
        java.util.UUID uuid23539 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        album1788 = new fifthelement.theelement.objects.Album(uuid23539, "");
        song3666 = new fifthelement.theelement.objects.Song(uuid23537, "Adventure of a Lifetime", "android.resource://fifthelement.theelement/raw/coldplay_adventure_of_a_lifetime", author3823, album1788, "Pop", 10, 3.5);
        java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        song3666.getAuthor();
        fifthelement.theelement.objects.Author author3827 = song3666.getAuthor();
        author3827.getUUID();
        java.util.UUID uuid23542 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author3824 = new fifthelement.theelement.objects.Author(uuid23542, "Coldplay", 10);
        song3666.setAuthor(author3824);
        song3666.getAlbum();
        fifthelement.theelement.objects.Album album1791 = song3666.getAlbum();
        album1791.getUUID();
        java.util.UUID uuid23544 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID uuid23545 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author3822 = new fifthelement.theelement.objects.Author(uuid23545, "");
        album1789 = new fifthelement.theelement.objects.Album(uuid23544, "A Head Full of Dreams", author3822, list74, 10);
        album1789.getAuthor();
        fifthelement.theelement.objects.Author author3829 = album1789.getAuthor();
        author3829.getUUID();
        java.util.UUID uuid23547 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author3825 = new fifthelement.theelement.objects.Author(uuid23547, "Coldplay", 10);
        album1789.setAuthor(author3825);
        song3666.setAlbum(album1789);
        song3666.getPath();
    }
}

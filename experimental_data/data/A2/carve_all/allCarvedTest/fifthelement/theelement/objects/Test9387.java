package fifthelement.theelement.objects;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

public class Test9387 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.PlayMusicTests#shuffleSongTest/Trace-1650046488177.txt
     * Method invocation under test: <fifthelement.theelement.objects.Song: java.util.UUID getUUID()>_1145_2289
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_objects_Song_getUUID_001() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        fifthelement.theelement.objects.Song song3668 = null;
        java.util.List list76 = null;
        fifthelement.theelement.objects.Author author3838 = null;
        fifthelement.theelement.objects.Album album1796 = null;
        fifthelement.theelement.objects.Author author3839 = null;
        fifthelement.theelement.objects.Album album1797 = null;
        fifthelement.theelement.objects.Author author3840 = null;
        fifthelement.theelement.objects.Author author3841 = null;
        java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        java.util.UUID uuid23591 = java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid23592 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author3839 = new fifthelement.theelement.objects.Author(uuid23592, "");
        java.util.UUID uuid23593 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        album1796 = new fifthelement.theelement.objects.Album(uuid23593, "");
        song3668 = new fifthelement.theelement.objects.Song(uuid23591, "Adventure of a Lifetime", "android.resource://fifthelement.theelement/raw/coldplay_adventure_of_a_lifetime", author3839, album1796, "Pop", 10, 3.5);
        java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        song3668.getAuthor();
        fifthelement.theelement.objects.Author author3843 = song3668.getAuthor();
        author3843.getUUID();
        java.util.UUID uuid23596 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author3840 = new fifthelement.theelement.objects.Author(uuid23596, "Coldplay", 10);
        song3668.setAuthor(author3840);
        song3668.getAlbum();
        fifthelement.theelement.objects.Album album1799 = song3668.getAlbum();
        album1799.getUUID();
        java.util.UUID uuid23598 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID uuid23599 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author3838 = new fifthelement.theelement.objects.Author(uuid23599, "");
        album1797 = new fifthelement.theelement.objects.Album(uuid23598, "A Head Full of Dreams", author3838, list76, 10);
        album1797.getAuthor();
        fifthelement.theelement.objects.Author author3845 = album1797.getAuthor();
        author3845.getUUID();
        java.util.UUID uuid23601 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author3841 = new fifthelement.theelement.objects.Author(uuid23601, "Coldplay", 10);
        album1797.setAuthor(author3841);
        song3668.setAlbum(album1797);
        song3668.getPath();
        song3668.getUUID();
    }
}

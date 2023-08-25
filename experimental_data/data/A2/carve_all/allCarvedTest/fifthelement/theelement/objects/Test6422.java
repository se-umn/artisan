package fifthelement.theelement.objects;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

public class Test6422 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.MusicLibraryTests#playSongFromSongsList/Trace-1650046532385.txt
     * Method invocation under test: <fifthelement.theelement.objects.Song: fifthelement.theelement.objects.Album getAlbum()>_158_312
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_objects_Song_getAlbum_004() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        fifthelement.theelement.objects.Song song15 = null;
        fifthelement.theelement.objects.Album album19 = null;
        fifthelement.theelement.objects.Author author47 = null;
        fifthelement.theelement.objects.Author author48 = null;
        java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        java.util.UUID uuid268 = java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid269 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author47 = new fifthelement.theelement.objects.Author(uuid269, "");
        java.util.UUID uuid270 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        album19 = new fifthelement.theelement.objects.Album(uuid270, "");
        song15 = new fifthelement.theelement.objects.Song(uuid268, "Adventure of a Lifetime", "android.resource://fifthelement.theelement/raw/coldplay_adventure_of_a_lifetime", author47, album19, "Pop", 10, 3.5);
        java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        song15.getAuthor();
        fifthelement.theelement.objects.Author author50 = song15.getAuthor();
        author50.getUUID();
        java.util.UUID uuid273 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author48 = new fifthelement.theelement.objects.Author(uuid273, "Coldplay", 10);
        song15.setAuthor(author48);
        song15.getAlbum();
    }
}

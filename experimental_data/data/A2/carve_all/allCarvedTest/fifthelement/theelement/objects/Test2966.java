package fifthelement.theelement.objects;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

public class Test2966 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.PlayMusicTests#skipSongsTest/Trace-1650046483856.txt
     * Method invocation under test: <fifthelement.theelement.objects.Song: void setAlbum(fifthelement.theelement.objects.Album)>_182_360
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_objects_Song_setAlbum_001() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        fifthelement.theelement.objects.Song song17 = null;
        java.util.List list4 = null;
        fifthelement.theelement.objects.Author author74 = null;
        fifthelement.theelement.objects.Album album31 = null;
        fifthelement.theelement.objects.Author author75 = null;
        fifthelement.theelement.objects.Album album32 = null;
        fifthelement.theelement.objects.Author author76 = null;
        fifthelement.theelement.objects.Author author77 = null;
        java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        java.util.UUID uuid532 = java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid533 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author75 = new fifthelement.theelement.objects.Author(uuid533, "");
        java.util.UUID uuid534 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        album31 = new fifthelement.theelement.objects.Album(uuid534, "");
        song17 = new fifthelement.theelement.objects.Song(uuid532, "Adventure of a Lifetime", "android.resource://fifthelement.theelement/raw/coldplay_adventure_of_a_lifetime", author75, album31, "Pop", 10, 3.5);
        java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        song17.getAuthor();
        fifthelement.theelement.objects.Author author79 = song17.getAuthor();
        author79.getUUID();
        java.util.UUID uuid537 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author76 = new fifthelement.theelement.objects.Author(uuid537, "Coldplay", 10);
        song17.setAuthor(author76);
        song17.getAlbum();
        fifthelement.theelement.objects.Album album34 = song17.getAlbum();
        album34.getUUID();
        java.util.UUID uuid539 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID uuid540 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author74 = new fifthelement.theelement.objects.Author(uuid540, "");
        album32 = new fifthelement.theelement.objects.Album(uuid539, "A Head Full of Dreams", author74, list4, 10);
        album32.getAuthor();
        fifthelement.theelement.objects.Author author81 = album32.getAuthor();
        author81.getUUID();
        java.util.UUID uuid542 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author77 = new fifthelement.theelement.objects.Author(uuid542, "Coldplay", 10);
        album32.setAuthor(author77);
        song17.setAlbum(album32);
    }
}

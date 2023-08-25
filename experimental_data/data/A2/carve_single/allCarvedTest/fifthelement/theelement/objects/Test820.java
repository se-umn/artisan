package fifthelement.theelement.objects;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

public class Test820 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.PlayMusicTests#seekTest/Trace-1650046478932.txt
     * Method invocation under test: <fifthelement.theelement.objects.Song: void setAlbum(fifthelement.theelement.objects.Album)>_182_360
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_objects_Song_setAlbum_001() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        fifthelement.theelement.objects.Song song4 = null;
        java.util.List list3 = null;
        fifthelement.theelement.objects.Author author24 = null;
        fifthelement.theelement.objects.Album album12 = null;
        fifthelement.theelement.objects.Author author25 = null;
        fifthelement.theelement.objects.Album album13 = null;
        fifthelement.theelement.objects.Author author26 = null;
        fifthelement.theelement.objects.Author author27 = null;
        java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        java.util.UUID uuid211 = java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid212 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author25 = new fifthelement.theelement.objects.Author(uuid212, "");
        java.util.UUID uuid213 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        album12 = new fifthelement.theelement.objects.Album(uuid213, "");
        song4 = new fifthelement.theelement.objects.Song(uuid211, "Adventure of a Lifetime", "android.resource://fifthelement.theelement/raw/coldplay_adventure_of_a_lifetime", author25, album12, "Pop", 10, 3.5);
        java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        song4.getAuthor();
        fifthelement.theelement.objects.Author author29 = song4.getAuthor();
        author29.getUUID();
        java.util.UUID uuid216 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author26 = new fifthelement.theelement.objects.Author(uuid216, "Coldplay", 10);
        song4.setAuthor(author26);
        song4.getAlbum();
        fifthelement.theelement.objects.Album album15 = song4.getAlbum();
        album15.getUUID();
        java.util.UUID uuid218 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID uuid219 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author24 = new fifthelement.theelement.objects.Author(uuid219, "");
        album13 = new fifthelement.theelement.objects.Album(uuid218, "A Head Full of Dreams", author24, list3, 10);
        album13.getAuthor();
        fifthelement.theelement.objects.Author author31 = album13.getAuthor();
        author31.getUUID();
        java.util.UUID uuid221 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author27 = new fifthelement.theelement.objects.Author(uuid221, "Coldplay", 10);
        album13.setAuthor(author27);
        song4.setAlbum(album13);
    }
}

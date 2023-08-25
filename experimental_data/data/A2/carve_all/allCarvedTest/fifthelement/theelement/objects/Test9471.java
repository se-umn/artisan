package fifthelement.theelement.objects;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

public class Test9471 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.PlayMusicTests#shuffleSongTest/Trace-1650046488177.txt
     * Method invocation under test: <fifthelement.theelement.objects.Song: java.lang.String getPath()>_1409_2814
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_objects_Song_getPath_008() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        fifthelement.theelement.objects.Song song3766 = null;
        java.util.List list119 = null;
        fifthelement.theelement.objects.Author author4258 = null;
        fifthelement.theelement.objects.Album album2061 = null;
        fifthelement.theelement.objects.Author author4259 = null;
        fifthelement.theelement.objects.Album album2062 = null;
        fifthelement.theelement.objects.Author author4260 = null;
        fifthelement.theelement.objects.Author author4261 = null;
        java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        java.util.UUID uuid33032 = java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid33033 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author4259 = new fifthelement.theelement.objects.Author(uuid33033, "");
        java.util.UUID uuid33034 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        album2061 = new fifthelement.theelement.objects.Album(uuid33034, "");
        song3766 = new fifthelement.theelement.objects.Song(uuid33032, "Adventure of a Lifetime", "android.resource://fifthelement.theelement/raw/coldplay_adventure_of_a_lifetime", author4259, album2061, "Pop", 10, 3.5);
        java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        song3766.getAuthor();
        fifthelement.theelement.objects.Author author4263 = song3766.getAuthor();
        author4263.getUUID();
        java.util.UUID uuid33037 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author4260 = new fifthelement.theelement.objects.Author(uuid33037, "Coldplay", 10);
        song3766.setAuthor(author4260);
        song3766.getAlbum();
        fifthelement.theelement.objects.Album album2064 = song3766.getAlbum();
        album2064.getUUID();
        java.util.UUID uuid33039 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID uuid33040 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author4258 = new fifthelement.theelement.objects.Author(uuid33040, "");
        album2062 = new fifthelement.theelement.objects.Album(uuid33039, "A Head Full of Dreams", author4258, list119, 10);
        album2062.getAuthor();
        fifthelement.theelement.objects.Author author4265 = album2062.getAuthor();
        author4265.getUUID();
        java.util.UUID uuid33042 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author4261 = new fifthelement.theelement.objects.Author(uuid33042, "Coldplay", 10);
        album2062.setAuthor(author4261);
        song3766.setAlbum(album2062);
        song3766.getPath();
        java.util.UUID uuid33043 = song3766.getUUID();
        uuid33043.toString();
        song3766.getName();
        song3766.getPath();
        song3766.getPath();
        song3766.getPath();
        song3766.getPath();
        song3766.getName();
        song3766.getName();
        song3766.getPath();
        song3766.getPath();
    }
}

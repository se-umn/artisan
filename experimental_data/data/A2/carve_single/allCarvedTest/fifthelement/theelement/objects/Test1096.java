package fifthelement.theelement.objects;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

public class Test1096 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.PlayMusicTests#shuffleSongTest/Trace-1650046488177.txt
     * Method invocation under test: <fifthelement.theelement.objects.Song: java.lang.String getPath()>_1123_2243
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_objects_Song_getPath_001() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        fifthelement.theelement.objects.Song song220 = null;
        java.util.List list8 = null;
        fifthelement.theelement.objects.Author author259 = null;
        fifthelement.theelement.objects.Album album116 = null;
        fifthelement.theelement.objects.Author author260 = null;
        fifthelement.theelement.objects.Album album117 = null;
        fifthelement.theelement.objects.Author author261 = null;
        fifthelement.theelement.objects.Author author262 = null;
        java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        java.util.UUID uuid1088 = java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid1089 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author260 = new fifthelement.theelement.objects.Author(uuid1089, "");
        java.util.UUID uuid1090 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        album116 = new fifthelement.theelement.objects.Album(uuid1090, "");
        song220 = new fifthelement.theelement.objects.Song(uuid1088, "Adventure of a Lifetime", "android.resource://fifthelement.theelement/raw/coldplay_adventure_of_a_lifetime", author260, album116, "Pop", 10, 3.5);
        java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        song220.getAuthor();
        fifthelement.theelement.objects.Author author264 = song220.getAuthor();
        author264.getUUID();
        java.util.UUID uuid1093 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author261 = new fifthelement.theelement.objects.Author(uuid1093, "Coldplay", 10);
        song220.setAuthor(author261);
        song220.getAlbum();
        fifthelement.theelement.objects.Album album119 = song220.getAlbum();
        album119.getUUID();
        java.util.UUID uuid1095 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID uuid1096 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author259 = new fifthelement.theelement.objects.Author(uuid1096, "");
        album117 = new fifthelement.theelement.objects.Album(uuid1095, "A Head Full of Dreams", author259, list8, 10);
        album117.getAuthor();
        fifthelement.theelement.objects.Author author266 = album117.getAuthor();
        author266.getUUID();
        java.util.UUID uuid1098 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author262 = new fifthelement.theelement.objects.Author(uuid1098, "Coldplay", 10);
        album117.setAuthor(author262);
        song220.setAlbum(album117);
        song220.getPath();
    }
}

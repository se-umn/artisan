package fifthelement.theelement.objects;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

public class Test1098 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.PlayMusicTests#shuffleSongTest/Trace-1650046488177.txt
     * Method invocation under test: <fifthelement.theelement.objects.Song: java.util.UUID getUUID()>_1145_2289
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_objects_Song_getUUID_001() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        fifthelement.theelement.objects.Song song222 = null;
        java.util.List list10 = null;
        fifthelement.theelement.objects.Author author275 = null;
        fifthelement.theelement.objects.Album album124 = null;
        fifthelement.theelement.objects.Author author276 = null;
        fifthelement.theelement.objects.Album album125 = null;
        fifthelement.theelement.objects.Author author277 = null;
        fifthelement.theelement.objects.Author author278 = null;
        java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        java.util.UUID uuid1142 = java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid1143 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author276 = new fifthelement.theelement.objects.Author(uuid1143, "");
        java.util.UUID uuid1144 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        album124 = new fifthelement.theelement.objects.Album(uuid1144, "");
        song222 = new fifthelement.theelement.objects.Song(uuid1142, "Adventure of a Lifetime", "android.resource://fifthelement.theelement/raw/coldplay_adventure_of_a_lifetime", author276, album124, "Pop", 10, 3.5);
        java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        song222.getAuthor();
        fifthelement.theelement.objects.Author author280 = song222.getAuthor();
        author280.getUUID();
        java.util.UUID uuid1147 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author277 = new fifthelement.theelement.objects.Author(uuid1147, "Coldplay", 10);
        song222.setAuthor(author277);
        song222.getAlbum();
        fifthelement.theelement.objects.Album album127 = song222.getAlbum();
        album127.getUUID();
        java.util.UUID uuid1149 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID uuid1150 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author275 = new fifthelement.theelement.objects.Author(uuid1150, "");
        album125 = new fifthelement.theelement.objects.Album(uuid1149, "A Head Full of Dreams", author275, list10, 10);
        album125.getAuthor();
        fifthelement.theelement.objects.Author author282 = album125.getAuthor();
        author282.getUUID();
        java.util.UUID uuid1152 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author278 = new fifthelement.theelement.objects.Author(uuid1152, "Coldplay", 10);
        album125.setAuthor(author278);
        song222.setAlbum(album125);
        song222.getPath();
        song222.getUUID();
    }
}

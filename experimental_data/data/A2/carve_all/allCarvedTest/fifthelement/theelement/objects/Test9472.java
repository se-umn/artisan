package fifthelement.theelement.objects;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

public class Test9472 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.PlayMusicTests#shuffleSongTest/Trace-1650046488177.txt
     * Method invocation under test: <fifthelement.theelement.objects.Song: java.lang.String getPath()>_1421_2838
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_objects_Song_getPath_009() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        fifthelement.theelement.objects.Song song3767 = null;
        java.util.List list120 = null;
        fifthelement.theelement.objects.Author author4266 = null;
        fifthelement.theelement.objects.Album album2065 = null;
        fifthelement.theelement.objects.Author author4267 = null;
        fifthelement.theelement.objects.Album album2066 = null;
        fifthelement.theelement.objects.Author author4268 = null;
        fifthelement.theelement.objects.Author author4269 = null;
        java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        java.util.UUID uuid33060 = java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid33061 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author4267 = new fifthelement.theelement.objects.Author(uuid33061, "");
        java.util.UUID uuid33062 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        album2065 = new fifthelement.theelement.objects.Album(uuid33062, "");
        song3767 = new fifthelement.theelement.objects.Song(uuid33060, "Adventure of a Lifetime", "android.resource://fifthelement.theelement/raw/coldplay_adventure_of_a_lifetime", author4267, album2065, "Pop", 10, 3.5);
        java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        song3767.getAuthor();
        fifthelement.theelement.objects.Author author4271 = song3767.getAuthor();
        author4271.getUUID();
        java.util.UUID uuid33065 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author4268 = new fifthelement.theelement.objects.Author(uuid33065, "Coldplay", 10);
        song3767.setAuthor(author4268);
        song3767.getAlbum();
        fifthelement.theelement.objects.Album album2068 = song3767.getAlbum();
        album2068.getUUID();
        java.util.UUID uuid33067 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID uuid33068 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author4266 = new fifthelement.theelement.objects.Author(uuid33068, "");
        album2066 = new fifthelement.theelement.objects.Album(uuid33067, "A Head Full of Dreams", author4266, list120, 10);
        album2066.getAuthor();
        fifthelement.theelement.objects.Author author4273 = album2066.getAuthor();
        author4273.getUUID();
        java.util.UUID uuid33070 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author4269 = new fifthelement.theelement.objects.Author(uuid33070, "Coldplay", 10);
        album2066.setAuthor(author4269);
        song3767.setAlbum(album2066);
        song3767.getPath();
        java.util.UUID uuid33071 = song3767.getUUID();
        uuid33071.toString();
        song3767.getName();
        song3767.getPath();
        song3767.getPath();
        song3767.getPath();
        song3767.getPath();
        song3767.getName();
        song3767.getName();
        song3767.getPath();
        song3767.getPath();
        song3767.getPath();
    }
}

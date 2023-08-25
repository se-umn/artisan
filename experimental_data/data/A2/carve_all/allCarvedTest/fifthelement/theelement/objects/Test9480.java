package fifthelement.theelement.objects;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

public class Test9480 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.PlayMusicTests#shuffleSongTest/Trace-1650046488177.txt
     * Method invocation under test: <fifthelement.theelement.objects.Song: fifthelement.theelement.objects.Album getAlbum()>_1444_2885
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_objects_Song_getAlbum_033() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        fifthelement.theelement.objects.Song song3773 = null;
        java.util.List list126 = null;
        fifthelement.theelement.objects.Author author4325 = null;
        fifthelement.theelement.objects.Album album2090 = null;
        fifthelement.theelement.objects.Author author4326 = null;
        fifthelement.theelement.objects.Album album2091 = null;
        fifthelement.theelement.objects.Author author4327 = null;
        fifthelement.theelement.objects.Author author4328 = null;
        java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        java.util.UUID uuid33268 = java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid33269 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author4326 = new fifthelement.theelement.objects.Author(uuid33269, "");
        java.util.UUID uuid33270 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        album2090 = new fifthelement.theelement.objects.Album(uuid33270, "");
        song3773 = new fifthelement.theelement.objects.Song(uuid33268, "Adventure of a Lifetime", "android.resource://fifthelement.theelement/raw/coldplay_adventure_of_a_lifetime", author4326, album2090, "Pop", 10, 3.5);
        java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        song3773.getAuthor();
        fifthelement.theelement.objects.Author author4330 = song3773.getAuthor();
        author4330.getUUID();
        java.util.UUID uuid33273 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author4327 = new fifthelement.theelement.objects.Author(uuid33273, "Coldplay", 10);
        song3773.setAuthor(author4327);
        song3773.getAlbum();
        fifthelement.theelement.objects.Album album2093 = song3773.getAlbum();
        album2093.getUUID();
        java.util.UUID uuid33275 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID uuid33276 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author4325 = new fifthelement.theelement.objects.Author(uuid33276, "");
        album2091 = new fifthelement.theelement.objects.Album(uuid33275, "A Head Full of Dreams", author4325, list126, 10);
        album2091.getAuthor();
        fifthelement.theelement.objects.Author author4332 = album2091.getAuthor();
        author4332.getUUID();
        java.util.UUID uuid33278 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author4328 = new fifthelement.theelement.objects.Author(uuid33278, "Coldplay", 10);
        album2091.setAuthor(author4328);
        song3773.setAlbum(album2091);
        song3773.getPath();
        java.util.UUID uuid33279 = song3773.getUUID();
        uuid33279.toString();
        song3773.getName();
        song3773.getPath();
        song3773.getPath();
        song3773.getPath();
        song3773.getPath();
        song3773.getName();
        song3773.getName();
        song3773.getPath();
        song3773.getPath();
        song3773.getPath();
        song3773.getPath();
        song3773.getAuthor();
        fifthelement.theelement.objects.Author author4334 = song3773.getAuthor();
        author4334.getName();
        fifthelement.theelement.objects.Author author4335 = song3773.getAuthor();
        author4335.getName();
        song3773.getAlbum();
        song3773.getAlbum();
    }
}

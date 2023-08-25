package fifthelement.theelement.objects;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

public class Test9477 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.PlayMusicTests#shuffleSongTest/Trace-1650046488177.txt
     * Method invocation under test: <fifthelement.theelement.objects.Song: fifthelement.theelement.objects.Author getAuthor()>_1438_2873
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_objects_Song_getAuthor_059() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        fifthelement.theelement.objects.Song song3771 = null;
        java.util.List list124 = null;
        fifthelement.theelement.objects.Author author4302 = null;
        fifthelement.theelement.objects.Album album2081 = null;
        fifthelement.theelement.objects.Author author4303 = null;
        fifthelement.theelement.objects.Album album2082 = null;
        fifthelement.theelement.objects.Author author4304 = null;
        fifthelement.theelement.objects.Author author4305 = null;
        java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        java.util.UUID uuid33192 = java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid33193 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author4303 = new fifthelement.theelement.objects.Author(uuid33193, "");
        java.util.UUID uuid33194 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        album2081 = new fifthelement.theelement.objects.Album(uuid33194, "");
        song3771 = new fifthelement.theelement.objects.Song(uuid33192, "Adventure of a Lifetime", "android.resource://fifthelement.theelement/raw/coldplay_adventure_of_a_lifetime", author4303, album2081, "Pop", 10, 3.5);
        java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        song3771.getAuthor();
        fifthelement.theelement.objects.Author author4307 = song3771.getAuthor();
        author4307.getUUID();
        java.util.UUID uuid33197 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author4304 = new fifthelement.theelement.objects.Author(uuid33197, "Coldplay", 10);
        song3771.setAuthor(author4304);
        song3771.getAlbum();
        fifthelement.theelement.objects.Album album2084 = song3771.getAlbum();
        album2084.getUUID();
        java.util.UUID uuid33199 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID uuid33200 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author4302 = new fifthelement.theelement.objects.Author(uuid33200, "");
        album2082 = new fifthelement.theelement.objects.Album(uuid33199, "A Head Full of Dreams", author4302, list124, 10);
        album2082.getAuthor();
        fifthelement.theelement.objects.Author author4309 = album2082.getAuthor();
        author4309.getUUID();
        java.util.UUID uuid33202 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author4305 = new fifthelement.theelement.objects.Author(uuid33202, "Coldplay", 10);
        album2082.setAuthor(author4305);
        song3771.setAlbum(album2082);
        song3771.getPath();
        java.util.UUID uuid33203 = song3771.getUUID();
        uuid33203.toString();
        song3771.getName();
        song3771.getPath();
        song3771.getPath();
        song3771.getPath();
        song3771.getPath();
        song3771.getName();
        song3771.getName();
        song3771.getPath();
        song3771.getPath();
        song3771.getPath();
        song3771.getPath();
        song3771.getAuthor();
        fifthelement.theelement.objects.Author author4311 = song3771.getAuthor();
        author4311.getName();
        song3771.getAuthor();
    }
}

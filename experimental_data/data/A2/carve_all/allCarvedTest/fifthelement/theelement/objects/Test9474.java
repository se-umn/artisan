package fifthelement.theelement.objects;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

public class Test9474 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.PlayMusicTests#shuffleSongTest/Trace-1650046488177.txt
     * Method invocation under test: <fifthelement.theelement.objects.Song: fifthelement.theelement.objects.Author getAuthor()>_1432_2861
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_objects_Song_getAuthor_057() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        fifthelement.theelement.objects.Song song3769 = null;
        java.util.List list122 = null;
        fifthelement.theelement.objects.Author author4282 = null;
        fifthelement.theelement.objects.Album album2073 = null;
        fifthelement.theelement.objects.Author author4283 = null;
        fifthelement.theelement.objects.Album album2074 = null;
        fifthelement.theelement.objects.Author author4284 = null;
        fifthelement.theelement.objects.Author author4285 = null;
        java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        java.util.UUID uuid33116 = java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid33117 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author4283 = new fifthelement.theelement.objects.Author(uuid33117, "");
        java.util.UUID uuid33118 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        album2073 = new fifthelement.theelement.objects.Album(uuid33118, "");
        song3769 = new fifthelement.theelement.objects.Song(uuid33116, "Adventure of a Lifetime", "android.resource://fifthelement.theelement/raw/coldplay_adventure_of_a_lifetime", author4283, album2073, "Pop", 10, 3.5);
        java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        song3769.getAuthor();
        fifthelement.theelement.objects.Author author4287 = song3769.getAuthor();
        author4287.getUUID();
        java.util.UUID uuid33121 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author4284 = new fifthelement.theelement.objects.Author(uuid33121, "Coldplay", 10);
        song3769.setAuthor(author4284);
        song3769.getAlbum();
        fifthelement.theelement.objects.Album album2076 = song3769.getAlbum();
        album2076.getUUID();
        java.util.UUID uuid33123 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID uuid33124 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author4282 = new fifthelement.theelement.objects.Author(uuid33124, "");
        album2074 = new fifthelement.theelement.objects.Album(uuid33123, "A Head Full of Dreams", author4282, list122, 10);
        album2074.getAuthor();
        fifthelement.theelement.objects.Author author4289 = album2074.getAuthor();
        author4289.getUUID();
        java.util.UUID uuid33126 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author4285 = new fifthelement.theelement.objects.Author(uuid33126, "Coldplay", 10);
        album2074.setAuthor(author4285);
        song3769.setAlbum(album2074);
        song3769.getPath();
        java.util.UUID uuid33127 = song3769.getUUID();
        uuid33127.toString();
        song3769.getName();
        song3769.getPath();
        song3769.getPath();
        song3769.getPath();
        song3769.getPath();
        song3769.getName();
        song3769.getName();
        song3769.getPath();
        song3769.getPath();
        song3769.getPath();
        song3769.getPath();
        song3769.getAuthor();
    }
}

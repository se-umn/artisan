package fifthelement.theelement.objects;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

public class Test634 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.SettingsTest#playSongWithSongPlayedCheck/Trace-1650046467562.txt
     * Method invocation under test: <fifthelement.theelement.objects.Song: fifthelement.theelement.objects.Album getAlbum()>_135_266
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_objects_Song_getAlbum_001() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        fifthelement.theelement.objects.Author author13 = null;
        fifthelement.theelement.objects.Album album4 = null;
        fifthelement.theelement.objects.Author author14 = null;
        fifthelement.theelement.objects.Song song3 = null;
        java.util.UUID uuid81 = java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid82 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author13 = new fifthelement.theelement.objects.Author(uuid82, "");
        song3 = new fifthelement.theelement.objects.Song(uuid81, "This Is America", "android.resource://fifthelement.theelement/raw/childish_gambino_this_is_america", author13, album4, "Hiphop", 3, 1.5);
        java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.randomUUID();
        song3.getAuthor();
        fifthelement.theelement.objects.Author author16 = song3.getAuthor();
        author16.getUUID();
        java.util.UUID uuid90 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author14 = new fifthelement.theelement.objects.Author(uuid90, "Childish Gambino", 3);
        song3.setAuthor(author14);
        song3.getAlbum();
    }
}

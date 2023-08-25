package fifthelement.theelement.objects;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

public class Test024 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.CustomizeTests#skipSongWithSongPlayedCheck/Trace-1650046492781.txt
     * Method invocation under test: <fifthelement.theelement.objects.Song: fifthelement.theelement.objects.Album getAlbum()>_135_266
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_objects_Song_getAlbum_001() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        fifthelement.theelement.objects.Song song7 = null;
        fifthelement.theelement.objects.Author author20 = null;
        fifthelement.theelement.objects.Album album8 = null;
        fifthelement.theelement.objects.Author author21 = null;
        java.util.UUID uuid125 = java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid126 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author21 = new fifthelement.theelement.objects.Author(uuid126, "");
        song7 = new fifthelement.theelement.objects.Song(uuid125, "This Is America", "android.resource://fifthelement.theelement/raw/childish_gambino_this_is_america", author21, album8, "Hiphop", 3, 1.5);
        java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.randomUUID();
        song7.getAuthor();
        fifthelement.theelement.objects.Author author23 = song7.getAuthor();
        author23.getUUID();
        java.util.UUID uuid134 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author20 = new fifthelement.theelement.objects.Author(uuid134, "Childish Gambino", 3);
        song7.setAuthor(author20);
        song7.getAlbum();
    }
}

package fifthelement.theelement.objects;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

public class Test9073 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.PlayMusicTests#shuffleSongTest/Trace-1650046488177.txt
     * Method invocation under test: <fifthelement.theelement.objects.Song: fifthelement.theelement.objects.Author getAuthor()>_124_244
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_objects_Song_getAuthor_002() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        fifthelement.theelement.objects.Album album6 = null;
        fifthelement.theelement.objects.Song song5 = null;
        fifthelement.theelement.objects.Author author9 = null;
        java.util.UUID uuid68 = java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid69 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author9 = new fifthelement.theelement.objects.Author(uuid69, "");
        song5 = new fifthelement.theelement.objects.Song(uuid68, "This Is America", "android.resource://fifthelement.theelement/raw/childish_gambino_this_is_america", author9, album6, "Hiphop", 3, 1.5);
        song5.getAuthor();
        song5.getAuthor();
    }
}

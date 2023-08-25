package fifthelement.theelement.objects;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

public class Test1225 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.MusicLibraryTests#searchSongsTest/Trace-1650046542039.txt
     * Method invocation under test: <fifthelement.theelement.objects.Song: fifthelement.theelement.objects.Author getAuthor()>_123_242
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_objects_Song_getAuthor_001() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        fifthelement.theelement.objects.Album album2 = null;
        fifthelement.theelement.objects.Song song1 = null;
        fifthelement.theelement.objects.Author author3 = null;
        java.util.UUID uuid24 = java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid25 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author3 = new fifthelement.theelement.objects.Author(uuid25, "");
        song1 = new fifthelement.theelement.objects.Song(uuid24, "This Is America", "android.resource://fifthelement.theelement/raw/childish_gambino_this_is_america", author3, album2, "Hiphop", 3, 1.5);
        song1.getAuthor();
    }
}

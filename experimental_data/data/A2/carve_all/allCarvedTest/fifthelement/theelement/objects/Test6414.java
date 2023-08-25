package fifthelement.theelement.objects;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

public class Test6414 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.MusicLibraryTests#playSongFromSongsList/Trace-1650046532385.txt
     * Method invocation under test: <fifthelement.theelement.objects.Song: fifthelement.theelement.objects.Author getAuthor()>_142_280
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_objects_Song_getAuthor_004() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        fifthelement.theelement.objects.Album album13 = null;
        fifthelement.theelement.objects.Author author28 = null;
        fifthelement.theelement.objects.Song song10 = null;
        java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        java.util.UUID uuid154 = java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        song10 = new fifthelement.theelement.objects.Song(uuid154, "Classical Music", "android.resource://fifthelement.theelement/raw/classical_music", author28, album13, "Classical", 4, 2.0);
        song10.getAuthor();
    }
}

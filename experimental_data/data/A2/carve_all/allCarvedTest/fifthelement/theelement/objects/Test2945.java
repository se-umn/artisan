package fifthelement.theelement.objects;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

public class Test2945 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.PlayMusicTests#skipSongsTest/Trace-1650046483856.txt
     * Method invocation under test: <fifthelement.theelement.objects.Song: fifthelement.theelement.objects.Album getAlbum()>_139_274
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_objects_Song_getAlbum_002() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        fifthelement.theelement.objects.Album album11 = null;
        fifthelement.theelement.objects.Author author26 = null;
        fifthelement.theelement.objects.Song song9 = null;
        java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        java.util.UUID uuid146 = java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        song9 = new fifthelement.theelement.objects.Song(uuid146, "Hall of Fame", "android.resource://fifthelement.theelement/raw/hall_of_fame", author26, album11, "", 3, 4.5);
        song9.getAuthor();
        song9.getAlbum();
    }
}

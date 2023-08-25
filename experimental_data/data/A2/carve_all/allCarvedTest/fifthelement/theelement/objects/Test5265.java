package fifthelement.theelement.objects;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

public class Test5265 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.SettingsTest#playSongWithSongPlayedCheck/Trace-1650046467562.txt
     * Method invocation under test: <fifthelement.theelement.objects.Song: void <init>(java.util.UUID,java.lang.String,java.lang.String,fifthelement.theelement.objects.Author,fifthelement.theelement.objects.Album,java.lang.String,int,double)>_99_192
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_objects_Song_constructor_002() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        fifthelement.theelement.objects.Album album1 = null;
        fifthelement.theelement.objects.Author author2 = null;
        fifthelement.theelement.objects.Song song1 = null;
        java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        java.util.UUID uuid13 = java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        song1 = new fifthelement.theelement.objects.Song(uuid13, "Hall of Fame", "android.resource://fifthelement.theelement/raw/hall_of_fame", author2, album1, "", 3, 4.5);
    }
}

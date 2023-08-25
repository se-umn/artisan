package fifthelement.theelement.objects;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

public class Test6922 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.PlayMusicTests#seekTest/Trace-1650046478932.txt
     * Method invocation under test: <fifthelement.theelement.objects.Song: void <init>(java.util.UUID,java.lang.String,java.lang.String,fifthelement.theelement.objects.Author,fifthelement.theelement.objects.Album,java.lang.String,int,double)>_95_184
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_objects_Song_constructor_001() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        fifthelement.theelement.objects.Album album0 = null;
        fifthelement.theelement.objects.Song song0 = null;
        fifthelement.theelement.objects.Author author1 = null;
        java.util.UUID uuid6 = java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid7 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author1 = new fifthelement.theelement.objects.Author(uuid7, "");
        song0 = new fifthelement.theelement.objects.Song(uuid6, "This Is America", "android.resource://fifthelement.theelement/raw/childish_gambino_this_is_america", author1, album0, "Hiphop", 3, 1.5);
    }
}

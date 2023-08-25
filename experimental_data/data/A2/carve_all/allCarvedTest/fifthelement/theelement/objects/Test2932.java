package fifthelement.theelement.objects;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

public class Test2932 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.PlayMusicTests#skipSongsTest/Trace-1650046483856.txt
     * Method invocation under test: <fifthelement.theelement.objects.Song: void <init>(java.util.UUID,java.lang.String,java.lang.String,fifthelement.theelement.objects.Author,fifthelement.theelement.objects.Album,java.lang.String,int,double)>_103_200
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_objects_Song_constructor_003() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        fifthelement.theelement.objects.Album album2 = null;
        fifthelement.theelement.objects.Author author3 = null;
        fifthelement.theelement.objects.Song song2 = null;
        java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        java.util.UUID uuid21 = java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        song2 = new fifthelement.theelement.objects.Song(uuid21, "Classical Music", "android.resource://fifthelement.theelement/raw/classical_music", author3, album2, "Classical", 4, 2.0);
    }
}

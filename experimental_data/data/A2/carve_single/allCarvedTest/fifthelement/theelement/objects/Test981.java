package fifthelement.theelement.objects;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

public class Test981 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.CustomizeTests#deleteSong/Trace-1650046523752.txt
     * Method invocation under test: <fifthelement.theelement.objects.Song: void setAuthor(fifthelement.theelement.objects.Author)>_133_262
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_objects_Song_setAuthor_001() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        fifthelement.theelement.objects.Song song2 = null;
        fifthelement.theelement.objects.Author author9 = null;
        fifthelement.theelement.objects.Album album3 = null;
        fifthelement.theelement.objects.Author author10 = null;
        java.util.UUID uuid62 = java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid63 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author10 = new fifthelement.theelement.objects.Author(uuid63, "");
        song2 = new fifthelement.theelement.objects.Song(uuid62, "This Is America", "android.resource://fifthelement.theelement/raw/childish_gambino_this_is_america", author10, album3, "Hiphop", 3, 1.5);
        java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.randomUUID();
        song2.getAuthor();
        fifthelement.theelement.objects.Author author12 = song2.getAuthor();
        author12.getUUID();
        java.util.UUID uuid71 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author9 = new fifthelement.theelement.objects.Author(uuid71, "Childish Gambino", 3);
        song2.setAuthor(author9);
    }
}

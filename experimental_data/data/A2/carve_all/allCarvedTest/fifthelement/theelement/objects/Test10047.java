package fifthelement.theelement.objects;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

public class Test10047 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.MusicLibraryTests#searchSongsTest/Trace-1650046542039.txt
     * Method invocation under test: <fifthelement.theelement.objects.Song: void setAuthor(fifthelement.theelement.objects.Author)>_156_308
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_objects_Song_setAuthor_002() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        fifthelement.theelement.objects.Song song14 = null;
        fifthelement.theelement.objects.Album album18 = null;
        fifthelement.theelement.objects.Author author43 = null;
        fifthelement.theelement.objects.Author author44 = null;
        java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        java.util.UUID uuid249 = java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid250 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author43 = new fifthelement.theelement.objects.Author(uuid250, "");
        java.util.UUID uuid251 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        album18 = new fifthelement.theelement.objects.Album(uuid251, "");
        song14 = new fifthelement.theelement.objects.Song(uuid249, "Adventure of a Lifetime", "android.resource://fifthelement.theelement/raw/coldplay_adventure_of_a_lifetime", author43, album18, "Pop", 10, 3.5);
        java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        song14.getAuthor();
        fifthelement.theelement.objects.Author author46 = song14.getAuthor();
        author46.getUUID();
        java.util.UUID uuid254 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author44 = new fifthelement.theelement.objects.Author(uuid254, "Coldplay", 10);
        song14.setAuthor(author44);
    }
}

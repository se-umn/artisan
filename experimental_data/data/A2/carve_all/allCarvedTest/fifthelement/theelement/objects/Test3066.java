package fifthelement.theelement.objects;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

public class Test3066 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.PlayMusicTests#skipSongsTest/Trace-1650046483856.txt
     * Method invocation under test: <fifthelement.theelement.objects.Song: int compareTo(java.lang.Object)>_531_1057
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_objects_Song_compareTo_002() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        fifthelement.theelement.objects.Song song69 = null;
        fifthelement.theelement.objects.Author author330 = null;
        fifthelement.theelement.objects.Author author331 = null;
        fifthelement.theelement.objects.Song song70 = null;
        fifthelement.theelement.objects.Author author332 = null;
        fifthelement.theelement.objects.Song song71 = null;
        fifthelement.theelement.objects.Album album123 = null;
        java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.randomUUID();
        java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.randomUUID();
        java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        java.util.UUID uuid4716 = java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid4717 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author331 = new fifthelement.theelement.objects.Author(uuid4717, "");
        song69 = new fifthelement.theelement.objects.Song(uuid4716, "This Is America", "android.resource://fifthelement.theelement/raw/childish_gambino_this_is_america", author331, album123, "Hiphop", 3, 1.5);
        java.util.UUID uuid4718 = java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        song70 = new fifthelement.theelement.objects.Song(uuid4718, "Hall of Fame", "android.resource://fifthelement.theelement/raw/hall_of_fame", author330, album123, "", 3, 4.5);
        java.util.UUID uuid4719 = java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        song71 = new fifthelement.theelement.objects.Song(uuid4719, "Classical Music", "android.resource://fifthelement.theelement/raw/classical_music", author330, album123, "Classical", 4, 2.0);
        java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.randomUUID();
        song69.getAuthor();
        fifthelement.theelement.objects.Author author334 = song69.getAuthor();
        author334.getUUID();
        java.util.UUID uuid4725 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author332 = new fifthelement.theelement.objects.Author(uuid4725, "Childish Gambino", 3);
        song69.setAuthor(author332);
        song69.getAlbum();
        song70.getAuthor();
        song70.getAlbum();
        song71.getAuthor();
        song71.getAlbum();
        song70.compareTo(song69);
        song71.compareTo(song70);
    }
}

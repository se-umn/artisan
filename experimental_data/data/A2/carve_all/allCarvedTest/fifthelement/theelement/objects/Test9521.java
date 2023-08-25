package fifthelement.theelement.objects;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

public class Test9521 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.PlayMusicTests#playPausingTest/Trace-1650046475193.txt
     * Method invocation under test: <fifthelement.theelement.objects.Song: fifthelement.theelement.objects.Album getAlbum()>_159_314
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_objects_Song_getAlbum_005() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        fifthelement.theelement.objects.Song song16 = null;
        fifthelement.theelement.objects.Album album21 = null;
        fifthelement.theelement.objects.Author author51 = null;
        fifthelement.theelement.objects.Author author52 = null;
        java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        java.util.UUID uuid287 = java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid288 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author51 = new fifthelement.theelement.objects.Author(uuid288, "");
        java.util.UUID uuid289 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        album21 = new fifthelement.theelement.objects.Album(uuid289, "");
        song16 = new fifthelement.theelement.objects.Song(uuid287, "Adventure of a Lifetime", "android.resource://fifthelement.theelement/raw/coldplay_adventure_of_a_lifetime", author51, album21, "Pop", 10, 3.5);
        java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        song16.getAuthor();
        fifthelement.theelement.objects.Author author54 = song16.getAuthor();
        author54.getUUID();
        java.util.UUID uuid292 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author52 = new fifthelement.theelement.objects.Author(uuid292, "Coldplay", 10);
        song16.setAuthor(author52);
        song16.getAlbum();
        song16.getAlbum();
    }
}

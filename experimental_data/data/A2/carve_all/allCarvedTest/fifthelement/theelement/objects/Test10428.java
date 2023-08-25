package fifthelement.theelement.objects;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

public class Test10428 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.MusicLibraryTests#organizeMusicCollectionTest/Trace-1650046539302.txt
     * Method invocation under test: <fifthelement.theelement.objects.Song: java.lang.String getName()>_536_1065
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_objects_Song_getName_003() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        fifthelement.theelement.objects.Song song78 = null;
        fifthelement.theelement.objects.Author author358 = null;
        fifthelement.theelement.objects.Song song79 = null;
        fifthelement.theelement.objects.Song song80 = null;
        fifthelement.theelement.objects.Author author359 = null;
        fifthelement.theelement.objects.Author author360 = null;
        fifthelement.theelement.objects.Album album138 = null;
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
        java.util.UUID uuid4938 = java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid4939 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author360 = new fifthelement.theelement.objects.Author(uuid4939, "");
        song78 = new fifthelement.theelement.objects.Song(uuid4938, "This Is America", "android.resource://fifthelement.theelement/raw/childish_gambino_this_is_america", author360, album138, "Hiphop", 3, 1.5);
        java.util.UUID uuid4940 = java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        song79 = new fifthelement.theelement.objects.Song(uuid4940, "Hall of Fame", "android.resource://fifthelement.theelement/raw/hall_of_fame", author358, album138, "", 3, 4.5);
        java.util.UUID uuid4941 = java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        song80 = new fifthelement.theelement.objects.Song(uuid4941, "Classical Music", "android.resource://fifthelement.theelement/raw/classical_music", author358, album138, "Classical", 4, 2.0);
        java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.randomUUID();
        song78.getAuthor();
        fifthelement.theelement.objects.Author author362 = song78.getAuthor();
        author362.getUUID();
        java.util.UUID uuid4947 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author359 = new fifthelement.theelement.objects.Author(uuid4947, "Childish Gambino", 3);
        song78.setAuthor(author359);
        song78.getAlbum();
        song79.getAuthor();
        song79.getAlbum();
        song80.getAuthor();
        song80.getAlbum();
        song79.compareTo(song78);
        song80.compareTo(song79);
        song80.getName();
    }
}

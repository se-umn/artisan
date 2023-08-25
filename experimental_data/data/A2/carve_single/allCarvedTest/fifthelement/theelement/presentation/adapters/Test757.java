package fifthelement.theelement.presentation.adapters;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

@RunWith(RobolectricTestRunner.class)
public class Test757 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.MusicLibraryTests#playSongFromSongsList/Trace-1650046532385.txt
     * Method invocation under test: <fifthelement.theelement.presentation.adapters.SongsListAdapter: int getCount()>_543_1082
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_presentation_adapters_SongsListAdapter_getCount_001() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        fifthelement.theelement.objects.Song song24 = null;
        fifthelement.theelement.objects.Author author53 = null;
        fifthelement.theelement.objects.Song song25 = null;
        fifthelement.theelement.presentation.adapters.SongsListAdapter songslistadapter1 = null;
        fifthelement.theelement.objects.Author author54 = null;
        fifthelement.theelement.objects.Song song26 = null;
        fifthelement.theelement.objects.Song song27 = null;
        java.util.ArrayList arraylist8 = null;
        fifthelement.theelement.objects.Author author55 = null;
        fifthelement.theelement.objects.Album album28 = null;
        fifthelement.theelement.objects.Album album29 = null;
        android.content.Context context13 = ApplicationProvider.getApplicationContext();
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
        arraylist8 = new java.util.ArrayList();
        java.util.UUID uuid541 = java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid542 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author55 = new fifthelement.theelement.objects.Author(uuid542, "");
        song24 = new fifthelement.theelement.objects.Song(uuid541, "This Is America", "android.resource://fifthelement.theelement/raw/childish_gambino_this_is_america", author55, album28, "Hiphop", 3, 1.5);
        arraylist8.add(song24);
        java.util.UUID uuid543 = java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        song25 = new fifthelement.theelement.objects.Song(uuid543, "Hall of Fame", "android.resource://fifthelement.theelement/raw/hall_of_fame", author53, album28, "", 3, 4.5);
        arraylist8.add(song25);
        java.util.UUID uuid544 = java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        song26 = new fifthelement.theelement.objects.Song(uuid544, "Classical Music", "android.resource://fifthelement.theelement/raw/classical_music", author53, album28, "Classical", 4, 2.0);
        arraylist8.add(song26);
        java.util.UUID uuid545 = java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid546 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author54 = new fifthelement.theelement.objects.Author(uuid546, "");
        java.util.UUID uuid547 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        album29 = new fifthelement.theelement.objects.Album(uuid547, "");
        song27 = new fifthelement.theelement.objects.Song(uuid545, "Adventure of a Lifetime", "android.resource://fifthelement.theelement/raw/coldplay_adventure_of_a_lifetime", author54, album29, "Pop", 10, 3.5);
        arraylist8.add(song27);
        arraylist8.iterator();
        songslistadapter1 = new fifthelement.theelement.presentation.adapters.SongsListAdapter(context13, arraylist8);
        songslistadapter1.getCount();
    }
}

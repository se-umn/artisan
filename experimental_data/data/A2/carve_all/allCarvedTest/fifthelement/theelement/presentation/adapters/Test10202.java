package fifthelement.theelement.presentation.adapters;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

@RunWith(RobolectricTestRunner.class)
public class Test10202 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.MusicLibraryTests#searchSongsTest/Trace-1650046542039.txt
     * Method invocation under test: <fifthelement.theelement.presentation.adapters.SongsListAdapter: int getCount()>_653_1303
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_presentation_adapters_SongsListAdapter_getCount_003() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        fifthelement.theelement.objects.Author author486 = null;
        fifthelement.theelement.objects.Song song96 = null;
        fifthelement.theelement.objects.Album album173 = null;
        java.util.ArrayList arraylist47 = null;
        fifthelement.theelement.objects.Author author487 = null;
        fifthelement.theelement.objects.Song song97 = null;
        fifthelement.theelement.objects.Song song98 = null;
        fifthelement.theelement.objects.Song song99 = null;
        fifthelement.theelement.objects.Album album174 = null;
        fifthelement.theelement.objects.Author author488 = null;
        fifthelement.theelement.presentation.adapters.SongsListAdapter songslistadapter4 = null;
        android.content.Context context25 = ApplicationProvider.getApplicationContext();
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
        arraylist47 = new java.util.ArrayList();
        java.util.UUID uuid8274 = java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid8275 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author487 = new fifthelement.theelement.objects.Author(uuid8275, "");
        song99 = new fifthelement.theelement.objects.Song(uuid8274, "This Is America", "android.resource://fifthelement.theelement/raw/childish_gambino_this_is_america", author487, album174, "Hiphop", 3, 1.5);
        arraylist47.add(song99);
        java.util.UUID uuid8276 = java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        song97 = new fifthelement.theelement.objects.Song(uuid8276, "Hall of Fame", "android.resource://fifthelement.theelement/raw/hall_of_fame", author486, album174, "", 3, 4.5);
        arraylist47.add(song97);
        java.util.UUID uuid8277 = java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        song98 = new fifthelement.theelement.objects.Song(uuid8277, "Classical Music", "android.resource://fifthelement.theelement/raw/classical_music", author486, album174, "Classical", 4, 2.0);
        arraylist47.add(song98);
        java.util.UUID uuid8278 = java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid8279 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author488 = new fifthelement.theelement.objects.Author(uuid8279, "");
        java.util.UUID uuid8280 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        album173 = new fifthelement.theelement.objects.Album(uuid8280, "");
        song96 = new fifthelement.theelement.objects.Song(uuid8278, "Adventure of a Lifetime", "android.resource://fifthelement.theelement/raw/coldplay_adventure_of_a_lifetime", author488, album173, "Pop", 10, 3.5);
        arraylist47.add(song96);
        arraylist47.iterator();
        songslistadapter4 = new fifthelement.theelement.presentation.adapters.SongsListAdapter(context25, arraylist47);
        songslistadapter4.getCount();
    }
}

package fifthelement.theelement.presentation.adapters;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

@RunWith(RobolectricTestRunner.class)
public class Test3878 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.MusicLibraryTests#deleteSongTest/Trace-1650046535485.txt
     * Method invocation under test: <fifthelement.theelement.presentation.adapters.SongsListAdapter: void <init>(android.content.Context,java.util.List)>_538_1073
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_presentation_adapters_SongsListAdapter_constructor_001() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        fifthelement.theelement.objects.Song song81 = null;
        fifthelement.theelement.objects.Author author365 = null;
        fifthelement.theelement.objects.Song song82 = null;
        fifthelement.theelement.presentation.adapters.SongsListAdapter songslistadapter0 = null;
        fifthelement.theelement.objects.Author author366 = null;
        fifthelement.theelement.objects.Song song83 = null;
        fifthelement.theelement.objects.Song song84 = null;
        java.util.ArrayList arraylist27 = null;
        fifthelement.theelement.objects.Author author367 = null;
        fifthelement.theelement.objects.Album album142 = null;
        fifthelement.theelement.objects.Album album143 = null;
        android.content.Context context17 = ApplicationProvider.getApplicationContext();
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
        arraylist27 = new java.util.ArrayList();
        java.util.UUID uuid5007 = java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid5008 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author367 = new fifthelement.theelement.objects.Author(uuid5008, "");
        song81 = new fifthelement.theelement.objects.Song(uuid5007, "This Is America", "android.resource://fifthelement.theelement/raw/childish_gambino_this_is_america", author367, album142, "Hiphop", 3, 1.5);
        arraylist27.add(song81);
        java.util.UUID uuid5009 = java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        song82 = new fifthelement.theelement.objects.Song(uuid5009, "Hall of Fame", "android.resource://fifthelement.theelement/raw/hall_of_fame", author365, album142, "", 3, 4.5);
        arraylist27.add(song82);
        java.util.UUID uuid5010 = java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        song83 = new fifthelement.theelement.objects.Song(uuid5010, "Classical Music", "android.resource://fifthelement.theelement/raw/classical_music", author365, album142, "Classical", 4, 2.0);
        arraylist27.add(song83);
        java.util.UUID uuid5011 = java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid5012 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author366 = new fifthelement.theelement.objects.Author(uuid5012, "");
        java.util.UUID uuid5013 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        album143 = new fifthelement.theelement.objects.Album(uuid5013, "");
        song84 = new fifthelement.theelement.objects.Song(uuid5011, "Adventure of a Lifetime", "android.resource://fifthelement.theelement/raw/coldplay_adventure_of_a_lifetime", author366, album143, "Pop", 10, 3.5);
        arraylist27.add(song84);
        arraylist27.iterator();
        songslistadapter0 = new fifthelement.theelement.presentation.adapters.SongsListAdapter(context17, arraylist27);
    }
}

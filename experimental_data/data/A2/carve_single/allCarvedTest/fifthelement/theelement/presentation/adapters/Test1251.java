package fifthelement.theelement.presentation.adapters;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

@RunWith(RobolectricTestRunner.class)
public class Test1251 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.MusicLibraryTests#searchSongsTest/Trace-1650046542039.txt
     * Method invocation under test: <fifthelement.theelement.presentation.adapters.SongsListAdapter: void <init>(android.content.Context,java.util.List)>_633_1265
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_presentation_adapters_SongsListAdapter_constructor_001() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        java.util.ArrayList arraylist7 = null;
        fifthelement.theelement.presentation.adapters.SongsListAdapter songslistadapter0 = null;
        fifthelement.theelement.objects.Album album20 = null;
        fifthelement.theelement.objects.Album album21 = null;
        fifthelement.theelement.objects.Author author38 = null;
        fifthelement.theelement.objects.Author author39 = null;
        fifthelement.theelement.objects.Author author40 = null;
        fifthelement.theelement.objects.Song song13 = null;
        fifthelement.theelement.objects.Song song14 = null;
        fifthelement.theelement.objects.Song song15 = null;
        fifthelement.theelement.objects.Song song16 = null;
        android.content.Context context11 = ApplicationProvider.getApplicationContext();
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
        arraylist7 = new java.util.ArrayList();
        java.util.UUID uuid335 = java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid336 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author40 = new fifthelement.theelement.objects.Author(uuid336, "");
        song13 = new fifthelement.theelement.objects.Song(uuid335, "This Is America", "android.resource://fifthelement.theelement/raw/childish_gambino_this_is_america", author40, album21, "Hiphop", 3, 1.5);
        arraylist7.add(song13);
        java.util.UUID uuid337 = java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        song14 = new fifthelement.theelement.objects.Song(uuid337, "Hall of Fame", "android.resource://fifthelement.theelement/raw/hall_of_fame", author39, album21, "", 3, 4.5);
        arraylist7.add(song14);
        java.util.UUID uuid338 = java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        song15 = new fifthelement.theelement.objects.Song(uuid338, "Classical Music", "android.resource://fifthelement.theelement/raw/classical_music", author39, album21, "Classical", 4, 2.0);
        arraylist7.add(song15);
        java.util.UUID uuid339 = java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid340 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author38 = new fifthelement.theelement.objects.Author(uuid340, "");
        java.util.UUID uuid341 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        album20 = new fifthelement.theelement.objects.Album(uuid341, "");
        song16 = new fifthelement.theelement.objects.Song(uuid339, "Adventure of a Lifetime", "android.resource://fifthelement.theelement/raw/coldplay_adventure_of_a_lifetime", author38, album20, "Pop", 10, 3.5);
        arraylist7.add(song16);
        arraylist7.iterator();
        songslistadapter0 = new fifthelement.theelement.presentation.adapters.SongsListAdapter(context11, arraylist7);
    }
}

package fifthelement.theelement.presentation.adapters;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

@RunWith(RobolectricTestRunner.class)
public class Test1088 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.PlayMusicTests#shuffleSongTest/Trace-1650046488177.txt
     * Method invocation under test: <fifthelement.theelement.presentation.adapters.SongsListAdapter: void <init>(android.content.Context,java.util.List)>_538_1073
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_presentation_adapters_SongsListAdapter_constructor_001() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        fifthelement.theelement.objects.Song song20 = null;
        fifthelement.theelement.objects.Author author50 = null;
        fifthelement.theelement.objects.Author author51 = null;
        fifthelement.theelement.objects.Song song21 = null;
        java.util.ArrayList arraylist7 = null;
        fifthelement.theelement.objects.Author author52 = null;
        fifthelement.theelement.objects.Album album26 = null;
        fifthelement.theelement.presentation.adapters.SongsListAdapter songslistadapter0 = null;
        fifthelement.theelement.objects.Song song22 = null;
        fifthelement.theelement.objects.Song song23 = null;
        fifthelement.theelement.objects.Album album27 = null;
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
        arraylist7 = new java.util.ArrayList();
        java.util.UUID uuid475 = java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid476 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author51 = new fifthelement.theelement.objects.Author(uuid476, "");
        song20 = new fifthelement.theelement.objects.Song(uuid475, "This Is America", "android.resource://fifthelement.theelement/raw/childish_gambino_this_is_america", author51, album27, "Hiphop", 3, 1.5);
        arraylist7.add(song20);
        java.util.UUID uuid477 = java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        song21 = new fifthelement.theelement.objects.Song(uuid477, "Hall of Fame", "android.resource://fifthelement.theelement/raw/hall_of_fame", author50, album27, "", 3, 4.5);
        arraylist7.add(song21);
        java.util.UUID uuid478 = java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        song22 = new fifthelement.theelement.objects.Song(uuid478, "Classical Music", "android.resource://fifthelement.theelement/raw/classical_music", author50, album27, "Classical", 4, 2.0);
        arraylist7.add(song22);
        java.util.UUID uuid479 = java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid480 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author52 = new fifthelement.theelement.objects.Author(uuid480, "");
        java.util.UUID uuid481 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        album26 = new fifthelement.theelement.objects.Album(uuid481, "");
        song23 = new fifthelement.theelement.objects.Song(uuid479, "Adventure of a Lifetime", "android.resource://fifthelement.theelement/raw/coldplay_adventure_of_a_lifetime", author52, album26, "Pop", 10, 3.5);
        arraylist7.add(song23);
        arraylist7.iterator();
        songslistadapter0 = new fifthelement.theelement.presentation.adapters.SongsListAdapter(context11, arraylist7);
    }
}

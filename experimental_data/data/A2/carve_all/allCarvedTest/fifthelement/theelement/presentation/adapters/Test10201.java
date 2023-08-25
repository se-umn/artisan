package fifthelement.theelement.presentation.adapters;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

@RunWith(RobolectricTestRunner.class)
public class Test10201 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.MusicLibraryTests#searchSongsTest/Trace-1650046542039.txt
     * Method invocation under test: <fifthelement.theelement.presentation.adapters.SongsListAdapter: void <init>(android.content.Context,java.util.List)>_649_1296
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_presentation_adapters_SongsListAdapter_constructor_002() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        fifthelement.theelement.objects.Author author483 = null;
        fifthelement.theelement.objects.Song song92 = null;
        fifthelement.theelement.objects.Album album171 = null;
        java.util.ArrayList arraylist46 = null;
        fifthelement.theelement.objects.Author author484 = null;
        fifthelement.theelement.objects.Song song93 = null;
        fifthelement.theelement.objects.Song song94 = null;
        fifthelement.theelement.objects.Song song95 = null;
        fifthelement.theelement.objects.Album album172 = null;
        fifthelement.theelement.objects.Author author485 = null;
        fifthelement.theelement.presentation.adapters.SongsListAdapter songslistadapter3 = null;
        android.content.Context context23 = ApplicationProvider.getApplicationContext();
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
        arraylist46 = new java.util.ArrayList();
        java.util.UUID uuid8208 = java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid8209 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author484 = new fifthelement.theelement.objects.Author(uuid8209, "");
        song95 = new fifthelement.theelement.objects.Song(uuid8208, "This Is America", "android.resource://fifthelement.theelement/raw/childish_gambino_this_is_america", author484, album172, "Hiphop", 3, 1.5);
        arraylist46.add(song95);
        java.util.UUID uuid8210 = java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        song93 = new fifthelement.theelement.objects.Song(uuid8210, "Hall of Fame", "android.resource://fifthelement.theelement/raw/hall_of_fame", author483, album172, "", 3, 4.5);
        arraylist46.add(song93);
        java.util.UUID uuid8211 = java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        song94 = new fifthelement.theelement.objects.Song(uuid8211, "Classical Music", "android.resource://fifthelement.theelement/raw/classical_music", author483, album172, "Classical", 4, 2.0);
        arraylist46.add(song94);
        java.util.UUID uuid8212 = java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid8213 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author485 = new fifthelement.theelement.objects.Author(uuid8213, "");
        java.util.UUID uuid8214 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        album171 = new fifthelement.theelement.objects.Album(uuid8214, "");
        song92 = new fifthelement.theelement.objects.Song(uuid8212, "Adventure of a Lifetime", "android.resource://fifthelement.theelement/raw/coldplay_adventure_of_a_lifetime", author485, album171, "Pop", 10, 3.5);
        arraylist46.add(song92);
        arraylist46.iterator();
        songslistadapter3 = new fifthelement.theelement.presentation.adapters.SongsListAdapter(context23, arraylist46);
    }
}

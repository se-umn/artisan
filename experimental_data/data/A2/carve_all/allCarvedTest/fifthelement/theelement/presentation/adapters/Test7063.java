package fifthelement.theelement.presentation.adapters;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

@RunWith(RobolectricTestRunner.class)
public class Test7063 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.PlayMusicTests#seekTest/Trace-1650046478932.txt
     * Method invocation under test: <fifthelement.theelement.presentation.adapters.SongsListAdapter: int getCount()>_543_1082
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_presentation_adapters_SongsListAdapter_getCount_001() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        fifthelement.theelement.objects.Song song85 = null;
        fifthelement.theelement.objects.Author author368 = null;
        fifthelement.theelement.objects.Author author369 = null;
        fifthelement.theelement.objects.Song song86 = null;
        java.util.ArrayList arraylist28 = null;
        fifthelement.theelement.objects.Author author370 = null;
        fifthelement.theelement.objects.Album album144 = null;
        fifthelement.theelement.presentation.adapters.SongsListAdapter songslistadapter1 = null;
        fifthelement.theelement.objects.Song song87 = null;
        fifthelement.theelement.objects.Song song88 = null;
        fifthelement.theelement.objects.Album album145 = null;
        android.content.Context context19 = ApplicationProvider.getApplicationContext();
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
        arraylist28 = new java.util.ArrayList();
        java.util.UUID uuid5073 = java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid5074 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author369 = new fifthelement.theelement.objects.Author(uuid5074, "");
        song85 = new fifthelement.theelement.objects.Song(uuid5073, "This Is America", "android.resource://fifthelement.theelement/raw/childish_gambino_this_is_america", author369, album145, "Hiphop", 3, 1.5);
        arraylist28.add(song85);
        java.util.UUID uuid5075 = java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        song86 = new fifthelement.theelement.objects.Song(uuid5075, "Hall of Fame", "android.resource://fifthelement.theelement/raw/hall_of_fame", author368, album145, "", 3, 4.5);
        arraylist28.add(song86);
        java.util.UUID uuid5076 = java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        song87 = new fifthelement.theelement.objects.Song(uuid5076, "Classical Music", "android.resource://fifthelement.theelement/raw/classical_music", author368, album145, "Classical", 4, 2.0);
        arraylist28.add(song87);
        java.util.UUID uuid5077 = java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid5078 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author370 = new fifthelement.theelement.objects.Author(uuid5078, "");
        java.util.UUID uuid5079 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        album144 = new fifthelement.theelement.objects.Album(uuid5079, "");
        song88 = new fifthelement.theelement.objects.Song(uuid5077, "Adventure of a Lifetime", "android.resource://fifthelement.theelement/raw/coldplay_adventure_of_a_lifetime", author370, album144, "Pop", 10, 3.5);
        arraylist28.add(song88);
        arraylist28.iterator();
        songslistadapter1 = new fifthelement.theelement.presentation.adapters.SongsListAdapter(context19, arraylist28);
        songslistadapter1.getCount();
    }
}

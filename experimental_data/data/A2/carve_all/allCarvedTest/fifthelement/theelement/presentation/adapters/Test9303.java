package fifthelement.theelement.presentation.adapters;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

@RunWith(RobolectricTestRunner.class)
public class Test9303 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.PlayMusicTests#shuffleSongTest/Trace-1650046488177.txt
     * Method invocation under test: <fifthelement.theelement.presentation.adapters.SongsListAdapter: int getCount()>_793_1586
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_presentation_adapters_SongsListAdapter_getCount_008() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        fifthelement.theelement.objects.Album album318 = null;
        fifthelement.theelement.objects.Song song192 = null;
        fifthelement.theelement.objects.Author author841 = null;
        fifthelement.theelement.presentation.adapters.SongsListAdapter songslistadapter10 = null;
        fifthelement.theelement.objects.Author author842 = null;
        fifthelement.theelement.objects.Album album319 = null;
        fifthelement.theelement.objects.Album album320 = null;
        fifthelement.theelement.objects.Author author843 = null;
        fifthelement.theelement.objects.Song song193 = null;
        fifthelement.theelement.objects.Author author844 = null;
        fifthelement.theelement.objects.Song song194 = null;
        fifthelement.theelement.objects.Song song195 = null;
        fifthelement.theelement.objects.Author author845 = null;
        java.util.List list33 = null;
        java.util.ArrayList arraylist78 = null;
        fifthelement.theelement.objects.Author author846 = null;
        fifthelement.theelement.objects.Author author847 = null;
        android.content.Context context37 = ApplicationProvider.getApplicationContext();
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
        arraylist78 = new java.util.ArrayList();
        java.util.UUID uuid14649 = java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid14650 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author846 = new fifthelement.theelement.objects.Author(uuid14650, "");
        song193 = new fifthelement.theelement.objects.Song(uuid14649, "This Is America", "android.resource://fifthelement.theelement/raw/childish_gambino_this_is_america", author846, album319, "Hiphop", 3, 1.5);
        arraylist78.add(song193);
        java.util.UUID uuid14651 = java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        song194 = new fifthelement.theelement.objects.Song(uuid14651, "Hall of Fame", "android.resource://fifthelement.theelement/raw/hall_of_fame", author843, album319, "", 3, 4.5);
        arraylist78.add(song194);
        java.util.UUID uuid14652 = java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        song195 = new fifthelement.theelement.objects.Song(uuid14652, "Classical Music", "android.resource://fifthelement.theelement/raw/classical_music", author843, album319, "Classical", 4, 2.0);
        arraylist78.add(song195);
        java.util.UUID uuid14653 = java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid14654 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author844 = new fifthelement.theelement.objects.Author(uuid14654, "");
        java.util.UUID uuid14655 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        album318 = new fifthelement.theelement.objects.Album(uuid14655, "");
        song192 = new fifthelement.theelement.objects.Song(uuid14653, "Adventure of a Lifetime", "android.resource://fifthelement.theelement/raw/coldplay_adventure_of_a_lifetime", author844, album318, "Pop", 10, 3.5);
        arraylist78.add(song192);
        arraylist78.iterator();
        song193.getAuthor();
        fifthelement.theelement.objects.Author author849 = song193.getAuthor();
        author849.getUUID();
        java.util.UUID uuid14657 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author847 = new fifthelement.theelement.objects.Author(uuid14657, "Childish Gambino", 3);
        song193.setAuthor(author847);
        song193.getAlbum();
        song194.getAuthor();
        song194.getAlbum();
        song195.getAuthor();
        song195.getAlbum();
        song192.getAuthor();
        fifthelement.theelement.objects.Author author853 = song192.getAuthor();
        author853.getUUID();
        java.util.UUID uuid14659 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author841 = new fifthelement.theelement.objects.Author(uuid14659, "Coldplay", 10);
        song192.setAuthor(author841);
        song192.getAlbum();
        fifthelement.theelement.objects.Album album325 = song192.getAlbum();
        album325.getUUID();
        java.util.UUID uuid14661 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID uuid14662 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author842 = new fifthelement.theelement.objects.Author(uuid14662, "");
        album320 = new fifthelement.theelement.objects.Album(uuid14661, "A Head Full of Dreams", author842, list33, 10);
        album320.getAuthor();
        fifthelement.theelement.objects.Author author855 = album320.getAuthor();
        author855.getUUID();
        java.util.UUID uuid14664 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author845 = new fifthelement.theelement.objects.Author(uuid14664, "Coldplay", 10);
        album320.setAuthor(author845);
        song192.setAlbum(album320);
        song194.compareTo(song193);
        song195.compareTo(song194);
        song192.compareTo(song195);
        songslistadapter10 = new fifthelement.theelement.presentation.adapters.SongsListAdapter(context37, arraylist78);
        songslistadapter10.getCount();
        songslistadapter10.getCount();
        songslistadapter10.getCount();
        songslistadapter10.getCount();
    }
}

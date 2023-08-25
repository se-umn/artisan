package fifthelement.theelement.presentation.adapters;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

@RunWith(RobolectricTestRunner.class)
public class Test10206 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.MusicLibraryTests#searchSongsTest/Trace-1650046542039.txt
     * Method invocation under test: <fifthelement.theelement.presentation.adapters.SongsListAdapter: int getCount()>_665_1330
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_presentation_adapters_SongsListAdapter_getCount_006() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        fifthelement.theelement.objects.Author author522 = null;
        fifthelement.theelement.objects.Album album193 = null;
        java.util.ArrayList arraylist53 = null;
        fifthelement.theelement.objects.Author author523 = null;
        fifthelement.theelement.objects.Author author524 = null;
        fifthelement.theelement.objects.Song song112 = null;
        fifthelement.theelement.objects.Album album194 = null;
        fifthelement.theelement.objects.Author author525 = null;
        fifthelement.theelement.objects.Album album195 = null;
        fifthelement.theelement.objects.Author author526 = null;
        fifthelement.theelement.presentation.adapters.SongsListAdapter songslistadapter7 = null;
        fifthelement.theelement.objects.Author author527 = null;
        fifthelement.theelement.objects.Song song113 = null;
        fifthelement.theelement.objects.Author author528 = null;
        fifthelement.theelement.objects.Song song114 = null;
        fifthelement.theelement.objects.Song song115 = null;
        java.util.List list23 = null;
        android.content.Context context31 = ApplicationProvider.getApplicationContext();
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
        arraylist53 = new java.util.ArrayList();
        java.util.UUID uuid8519 = java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid8520 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author528 = new fifthelement.theelement.objects.Author(uuid8520, "");
        song115 = new fifthelement.theelement.objects.Song(uuid8519, "This Is America", "android.resource://fifthelement.theelement/raw/childish_gambino_this_is_america", author528, album195, "Hiphop", 3, 1.5);
        arraylist53.add(song115);
        java.util.UUID uuid8521 = java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        song112 = new fifthelement.theelement.objects.Song(uuid8521, "Hall of Fame", "android.resource://fifthelement.theelement/raw/hall_of_fame", author527, album195, "", 3, 4.5);
        arraylist53.add(song112);
        java.util.UUID uuid8522 = java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        song114 = new fifthelement.theelement.objects.Song(uuid8522, "Classical Music", "android.resource://fifthelement.theelement/raw/classical_music", author527, album195, "Classical", 4, 2.0);
        arraylist53.add(song114);
        java.util.UUID uuid8523 = java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid8524 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author526 = new fifthelement.theelement.objects.Author(uuid8524, "");
        java.util.UUID uuid8525 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        album193 = new fifthelement.theelement.objects.Album(uuid8525, "");
        song113 = new fifthelement.theelement.objects.Song(uuid8523, "Adventure of a Lifetime", "android.resource://fifthelement.theelement/raw/coldplay_adventure_of_a_lifetime", author526, album193, "Pop", 10, 3.5);
        arraylist53.add(song113);
        arraylist53.iterator();
        song115.getAuthor();
        fifthelement.theelement.objects.Author author530 = song115.getAuthor();
        author530.getUUID();
        java.util.UUID uuid8527 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author522 = new fifthelement.theelement.objects.Author(uuid8527, "Childish Gambino", 3);
        song115.setAuthor(author522);
        song115.getAlbum();
        song112.getAuthor();
        song112.getAlbum();
        song114.getAuthor();
        song114.getAlbum();
        song113.getAuthor();
        fifthelement.theelement.objects.Author author534 = song113.getAuthor();
        author534.getUUID();
        java.util.UUID uuid8529 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author523 = new fifthelement.theelement.objects.Author(uuid8529, "Coldplay", 10);
        song113.setAuthor(author523);
        song113.getAlbum();
        fifthelement.theelement.objects.Album album200 = song113.getAlbum();
        album200.getUUID();
        java.util.UUID uuid8531 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID uuid8532 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author524 = new fifthelement.theelement.objects.Author(uuid8532, "");
        album194 = new fifthelement.theelement.objects.Album(uuid8531, "A Head Full of Dreams", author524, list23, 10);
        album194.getAuthor();
        fifthelement.theelement.objects.Author author536 = album194.getAuthor();
        author536.getUUID();
        java.util.UUID uuid8534 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author525 = new fifthelement.theelement.objects.Author(uuid8534, "Coldplay", 10);
        album194.setAuthor(author525);
        song113.setAlbum(album194);
        songslistadapter7 = new fifthelement.theelement.presentation.adapters.SongsListAdapter(context31, arraylist53);
        songslistadapter7.getCount();
        songslistadapter7.getCount();
        songslistadapter7.getCount();
        songslistadapter7.getCount();
    }
}

package fifthelement.theelement.presentation.adapters;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

@RunWith(RobolectricTestRunner.class)
public class Test10205 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.MusicLibraryTests#searchSongsTest/Trace-1650046542039.txt
     * Method invocation under test: <fifthelement.theelement.presentation.adapters.SongsListAdapter: int getCount()>_663_1326
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_presentation_adapters_SongsListAdapter_getCount_005() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        fifthelement.theelement.objects.Author author507 = null;
        fifthelement.theelement.objects.Album album185 = null;
        java.util.ArrayList arraylist52 = null;
        fifthelement.theelement.objects.Author author508 = null;
        fifthelement.theelement.objects.Author author509 = null;
        fifthelement.theelement.objects.Song song108 = null;
        fifthelement.theelement.objects.Album album186 = null;
        fifthelement.theelement.objects.Author author510 = null;
        fifthelement.theelement.objects.Album album187 = null;
        fifthelement.theelement.objects.Author author511 = null;
        fifthelement.theelement.presentation.adapters.SongsListAdapter songslistadapter6 = null;
        fifthelement.theelement.objects.Author author512 = null;
        fifthelement.theelement.objects.Song song109 = null;
        fifthelement.theelement.objects.Author author513 = null;
        fifthelement.theelement.objects.Song song110 = null;
        fifthelement.theelement.objects.Song song111 = null;
        java.util.List list22 = null;
        android.content.Context context29 = ApplicationProvider.getApplicationContext();
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
        arraylist52 = new java.util.ArrayList();
        java.util.UUID uuid8439 = java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid8440 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author513 = new fifthelement.theelement.objects.Author(uuid8440, "");
        song111 = new fifthelement.theelement.objects.Song(uuid8439, "This Is America", "android.resource://fifthelement.theelement/raw/childish_gambino_this_is_america", author513, album187, "Hiphop", 3, 1.5);
        arraylist52.add(song111);
        java.util.UUID uuid8441 = java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        song108 = new fifthelement.theelement.objects.Song(uuid8441, "Hall of Fame", "android.resource://fifthelement.theelement/raw/hall_of_fame", author512, album187, "", 3, 4.5);
        arraylist52.add(song108);
        java.util.UUID uuid8442 = java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        song110 = new fifthelement.theelement.objects.Song(uuid8442, "Classical Music", "android.resource://fifthelement.theelement/raw/classical_music", author512, album187, "Classical", 4, 2.0);
        arraylist52.add(song110);
        java.util.UUID uuid8443 = java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid8444 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author511 = new fifthelement.theelement.objects.Author(uuid8444, "");
        java.util.UUID uuid8445 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        album185 = new fifthelement.theelement.objects.Album(uuid8445, "");
        song109 = new fifthelement.theelement.objects.Song(uuid8443, "Adventure of a Lifetime", "android.resource://fifthelement.theelement/raw/coldplay_adventure_of_a_lifetime", author511, album185, "Pop", 10, 3.5);
        arraylist52.add(song109);
        arraylist52.iterator();
        song111.getAuthor();
        fifthelement.theelement.objects.Author author515 = song111.getAuthor();
        author515.getUUID();
        java.util.UUID uuid8447 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author507 = new fifthelement.theelement.objects.Author(uuid8447, "Childish Gambino", 3);
        song111.setAuthor(author507);
        song111.getAlbum();
        song108.getAuthor();
        song108.getAlbum();
        song110.getAuthor();
        song110.getAlbum();
        song109.getAuthor();
        fifthelement.theelement.objects.Author author519 = song109.getAuthor();
        author519.getUUID();
        java.util.UUID uuid8449 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author508 = new fifthelement.theelement.objects.Author(uuid8449, "Coldplay", 10);
        song109.setAuthor(author508);
        song109.getAlbum();
        fifthelement.theelement.objects.Album album192 = song109.getAlbum();
        album192.getUUID();
        java.util.UUID uuid8451 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID uuid8452 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author509 = new fifthelement.theelement.objects.Author(uuid8452, "");
        album186 = new fifthelement.theelement.objects.Album(uuid8451, "A Head Full of Dreams", author509, list22, 10);
        album186.getAuthor();
        fifthelement.theelement.objects.Author author521 = album186.getAuthor();
        author521.getUUID();
        java.util.UUID uuid8454 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author510 = new fifthelement.theelement.objects.Author(uuid8454, "Coldplay", 10);
        album186.setAuthor(author510);
        song109.setAlbum(album186);
        songslistadapter6 = new fifthelement.theelement.presentation.adapters.SongsListAdapter(context29, arraylist52);
        songslistadapter6.getCount();
        songslistadapter6.getCount();
        songslistadapter6.getCount();
    }
}

package fifthelement.theelement.presentation.adapters;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

@RunWith(RobolectricTestRunner.class)
public class Test10475 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.MusicLibraryTests#organizeMusicCollectionTest/Trace-1650046539302.txt
     * Method invocation under test: <fifthelement.theelement.presentation.adapters.SongsListAdapter: int getCount()>_664_1324
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_presentation_adapters_SongsListAdapter_getCount_004() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        fifthelement.theelement.objects.Song song127 = null;
        fifthelement.theelement.objects.Album album210 = null;
        java.util.ArrayList arraylist50 = null;
        fifthelement.theelement.objects.Song song128 = null;
        fifthelement.theelement.objects.Album album211 = null;
        fifthelement.theelement.objects.Author author557 = null;
        fifthelement.theelement.objects.Author author558 = null;
        fifthelement.theelement.objects.Album album212 = null;
        fifthelement.theelement.objects.Author author559 = null;
        fifthelement.theelement.objects.Author author560 = null;
        fifthelement.theelement.objects.Song song129 = null;
        fifthelement.theelement.objects.Author author561 = null;
        fifthelement.theelement.objects.Author author562 = null;
        fifthelement.theelement.presentation.adapters.SongsListAdapter songslistadapter5 = null;
        java.util.List list23 = null;
        fifthelement.theelement.objects.Author author563 = null;
        fifthelement.theelement.objects.Song song130 = null;
        android.content.Context context27 = ApplicationProvider.getApplicationContext();
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
        arraylist50 = new java.util.ArrayList();
        java.util.UUID uuid8861 = java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid8862 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author558 = new fifthelement.theelement.objects.Author(uuid8862, "");
        song128 = new fifthelement.theelement.objects.Song(uuid8861, "This Is America", "android.resource://fifthelement.theelement/raw/childish_gambino_this_is_america", author558, album212, "Hiphop", 3, 1.5);
        arraylist50.add(song128);
        java.util.UUID uuid8863 = java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        song129 = new fifthelement.theelement.objects.Song(uuid8863, "Hall of Fame", "android.resource://fifthelement.theelement/raw/hall_of_fame", author560, album212, "", 3, 4.5);
        arraylist50.add(song129);
        java.util.UUID uuid8864 = java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        song127 = new fifthelement.theelement.objects.Song(uuid8864, "Classical Music", "android.resource://fifthelement.theelement/raw/classical_music", author560, album212, "Classical", 4, 2.0);
        arraylist50.add(song127);
        java.util.UUID uuid8865 = java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid8866 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author563 = new fifthelement.theelement.objects.Author(uuid8866, "");
        java.util.UUID uuid8867 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        album210 = new fifthelement.theelement.objects.Album(uuid8867, "");
        song130 = new fifthelement.theelement.objects.Song(uuid8865, "Adventure of a Lifetime", "android.resource://fifthelement.theelement/raw/coldplay_adventure_of_a_lifetime", author563, album210, "Pop", 10, 3.5);
        arraylist50.add(song130);
        arraylist50.iterator();
        song128.getAuthor();
        fifthelement.theelement.objects.Author author565 = song128.getAuthor();
        author565.getUUID();
        java.util.UUID uuid8869 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author559 = new fifthelement.theelement.objects.Author(uuid8869, "Childish Gambino", 3);
        song128.setAuthor(author559);
        song128.getAlbum();
        song129.getAuthor();
        song129.getAlbum();
        song127.getAuthor();
        song127.getAlbum();
        song130.getAuthor();
        fifthelement.theelement.objects.Author author569 = song130.getAuthor();
        author569.getUUID();
        java.util.UUID uuid8871 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author562 = new fifthelement.theelement.objects.Author(uuid8871, "Coldplay", 10);
        song130.setAuthor(author562);
        song130.getAlbum();
        fifthelement.theelement.objects.Album album217 = song130.getAlbum();
        album217.getUUID();
        java.util.UUID uuid8873 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID uuid8874 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author561 = new fifthelement.theelement.objects.Author(uuid8874, "");
        album211 = new fifthelement.theelement.objects.Album(uuid8873, "A Head Full of Dreams", author561, list23, 10);
        album211.getAuthor();
        fifthelement.theelement.objects.Author author571 = album211.getAuthor();
        author571.getUUID();
        java.util.UUID uuid8876 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author557 = new fifthelement.theelement.objects.Author(uuid8876, "Coldplay", 10);
        album211.setAuthor(author557);
        song130.setAlbum(album211);
        songslistadapter5 = new fifthelement.theelement.presentation.adapters.SongsListAdapter(context27, arraylist50);
        songslistadapter5.getCount();
        songslistadapter5.getCount();
    }
}

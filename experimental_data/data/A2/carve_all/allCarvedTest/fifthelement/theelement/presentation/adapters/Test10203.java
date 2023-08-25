package fifthelement.theelement.presentation.adapters;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

@RunWith(RobolectricTestRunner.class)
public class Test10203 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.MusicLibraryTests#searchSongsTest/Trace-1650046542039.txt
     * Method invocation under test: <fifthelement.theelement.presentation.adapters.SongsListAdapter: int getCount()>_655_1307
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_presentation_adapters_SongsListAdapter_getCount_004() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        fifthelement.theelement.objects.Author author489 = null;
        fifthelement.theelement.objects.Album album175 = null;
        java.util.ArrayList arraylist48 = null;
        fifthelement.theelement.objects.Author author490 = null;
        fifthelement.theelement.objects.Author author491 = null;
        fifthelement.theelement.objects.Song song100 = null;
        fifthelement.theelement.objects.Album album176 = null;
        fifthelement.theelement.objects.Author author492 = null;
        fifthelement.theelement.objects.Album album177 = null;
        fifthelement.theelement.objects.Author author493 = null;
        fifthelement.theelement.presentation.adapters.SongsListAdapter songslistadapter5 = null;
        fifthelement.theelement.objects.Author author494 = null;
        fifthelement.theelement.objects.Song song101 = null;
        fifthelement.theelement.objects.Author author495 = null;
        fifthelement.theelement.objects.Song song102 = null;
        fifthelement.theelement.objects.Song song103 = null;
        java.util.List list21 = null;
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
        arraylist48 = new java.util.ArrayList();
        java.util.UUID uuid8345 = java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid8346 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author495 = new fifthelement.theelement.objects.Author(uuid8346, "");
        song103 = new fifthelement.theelement.objects.Song(uuid8345, "This Is America", "android.resource://fifthelement.theelement/raw/childish_gambino_this_is_america", author495, album177, "Hiphop", 3, 1.5);
        arraylist48.add(song103);
        java.util.UUID uuid8347 = java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        song100 = new fifthelement.theelement.objects.Song(uuid8347, "Hall of Fame", "android.resource://fifthelement.theelement/raw/hall_of_fame", author494, album177, "", 3, 4.5);
        arraylist48.add(song100);
        java.util.UUID uuid8348 = java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        song102 = new fifthelement.theelement.objects.Song(uuid8348, "Classical Music", "android.resource://fifthelement.theelement/raw/classical_music", author494, album177, "Classical", 4, 2.0);
        arraylist48.add(song102);
        java.util.UUID uuid8349 = java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid8350 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author493 = new fifthelement.theelement.objects.Author(uuid8350, "");
        java.util.UUID uuid8351 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        album175 = new fifthelement.theelement.objects.Album(uuid8351, "");
        song101 = new fifthelement.theelement.objects.Song(uuid8349, "Adventure of a Lifetime", "android.resource://fifthelement.theelement/raw/coldplay_adventure_of_a_lifetime", author493, album175, "Pop", 10, 3.5);
        arraylist48.add(song101);
        arraylist48.iterator();
        song103.getAuthor();
        fifthelement.theelement.objects.Author author497 = song103.getAuthor();
        author497.getUUID();
        java.util.UUID uuid8353 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author489 = new fifthelement.theelement.objects.Author(uuid8353, "Childish Gambino", 3);
        song103.setAuthor(author489);
        song103.getAlbum();
        song100.getAuthor();
        song100.getAlbum();
        song102.getAuthor();
        song102.getAlbum();
        song101.getAuthor();
        fifthelement.theelement.objects.Author author501 = song101.getAuthor();
        author501.getUUID();
        java.util.UUID uuid8355 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author490 = new fifthelement.theelement.objects.Author(uuid8355, "Coldplay", 10);
        song101.setAuthor(author490);
        song101.getAlbum();
        fifthelement.theelement.objects.Album album182 = song101.getAlbum();
        album182.getUUID();
        java.util.UUID uuid8357 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID uuid8358 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author491 = new fifthelement.theelement.objects.Author(uuid8358, "");
        album176 = new fifthelement.theelement.objects.Album(uuid8357, "A Head Full of Dreams", author491, list21, 10);
        album176.getAuthor();
        fifthelement.theelement.objects.Author author503 = album176.getAuthor();
        author503.getUUID();
        java.util.UUID uuid8360 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author492 = new fifthelement.theelement.objects.Author(uuid8360, "Coldplay", 10);
        album176.setAuthor(author492);
        song101.setAlbum(album176);
        songslistadapter5 = new fifthelement.theelement.presentation.adapters.SongsListAdapter(context27, arraylist48);
        songslistadapter5.getCount();
        songslistadapter5.getCount();
    }
}

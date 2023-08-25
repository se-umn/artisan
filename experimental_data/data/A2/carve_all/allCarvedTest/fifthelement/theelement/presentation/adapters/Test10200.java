package fifthelement.theelement.presentation.adapters;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

@RunWith(RobolectricTestRunner.class)
public class Test10200 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.MusicLibraryTests#searchSongsTest/Trace-1650046542039.txt
     * Method invocation under test: <fifthelement.theelement.presentation.adapters.SongsListAdapter: int getCount()>_640_1278
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_presentation_adapters_SongsListAdapter_getCount_002() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        fifthelement.theelement.objects.Author author468 = null;
        java.util.ArrayList arraylist45 = null;
        fifthelement.theelement.presentation.adapters.SongsListAdapter songslistadapter2 = null;
        fifthelement.theelement.objects.Author author469 = null;
        fifthelement.theelement.objects.Album album163 = null;
        fifthelement.theelement.objects.Album album164 = null;
        fifthelement.theelement.objects.Album album165 = null;
        fifthelement.theelement.objects.Author author470 = null;
        fifthelement.theelement.objects.Author author471 = null;
        fifthelement.theelement.objects.Author author472 = null;
        fifthelement.theelement.objects.Author author473 = null;
        fifthelement.theelement.objects.Song song88 = null;
        fifthelement.theelement.objects.Song song89 = null;
        fifthelement.theelement.objects.Song song90 = null;
        java.util.List list20 = null;
        fifthelement.theelement.objects.Author author474 = null;
        fifthelement.theelement.objects.Song song91 = null;
        android.content.Context context21 = ApplicationProvider.getApplicationContext();
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
        arraylist45 = new java.util.ArrayList();
        java.util.UUID uuid8133 = java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid8134 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author473 = new fifthelement.theelement.objects.Author(uuid8134, "");
        song88 = new fifthelement.theelement.objects.Song(uuid8133, "This Is America", "android.resource://fifthelement.theelement/raw/childish_gambino_this_is_america", author473, album165, "Hiphop", 3, 1.5);
        arraylist45.add(song88);
        java.util.UUID uuid8135 = java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        song89 = new fifthelement.theelement.objects.Song(uuid8135, "Hall of Fame", "android.resource://fifthelement.theelement/raw/hall_of_fame", author472, album165, "", 3, 4.5);
        arraylist45.add(song89);
        java.util.UUID uuid8136 = java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        song90 = new fifthelement.theelement.objects.Song(uuid8136, "Classical Music", "android.resource://fifthelement.theelement/raw/classical_music", author472, album165, "Classical", 4, 2.0);
        arraylist45.add(song90);
        java.util.UUID uuid8137 = java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid8138 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author470 = new fifthelement.theelement.objects.Author(uuid8138, "");
        java.util.UUID uuid8139 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        album163 = new fifthelement.theelement.objects.Album(uuid8139, "");
        song91 = new fifthelement.theelement.objects.Song(uuid8137, "Adventure of a Lifetime", "android.resource://fifthelement.theelement/raw/coldplay_adventure_of_a_lifetime", author470, album163, "Pop", 10, 3.5);
        arraylist45.add(song91);
        arraylist45.iterator();
        song88.getAuthor();
        fifthelement.theelement.objects.Author author476 = song88.getAuthor();
        author476.getUUID();
        java.util.UUID uuid8141 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author468 = new fifthelement.theelement.objects.Author(uuid8141, "Childish Gambino", 3);
        song88.setAuthor(author468);
        song88.getAlbum();
        song89.getAuthor();
        song89.getAlbum();
        song90.getAuthor();
        song90.getAlbum();
        song91.getAuthor();
        fifthelement.theelement.objects.Author author480 = song91.getAuthor();
        author480.getUUID();
        java.util.UUID uuid8143 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author469 = new fifthelement.theelement.objects.Author(uuid8143, "Coldplay", 10);
        song91.setAuthor(author469);
        song91.getAlbum();
        fifthelement.theelement.objects.Album album170 = song91.getAlbum();
        album170.getUUID();
        java.util.UUID uuid8145 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID uuid8146 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author474 = new fifthelement.theelement.objects.Author(uuid8146, "");
        album164 = new fifthelement.theelement.objects.Album(uuid8145, "A Head Full of Dreams", author474, list20, 10);
        album164.getAuthor();
        fifthelement.theelement.objects.Author author482 = album164.getAuthor();
        author482.getUUID();
        java.util.UUID uuid8148 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author471 = new fifthelement.theelement.objects.Author(uuid8148, "Coldplay", 10);
        album164.setAuthor(author471);
        song91.setAlbum(album164);
        songslistadapter2 = new fifthelement.theelement.presentation.adapters.SongsListAdapter(context21, arraylist45);
        songslistadapter2.getCount();
        songslistadapter2.getCount();
    }
}

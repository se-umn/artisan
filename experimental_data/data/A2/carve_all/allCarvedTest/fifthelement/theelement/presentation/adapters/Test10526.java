package fifthelement.theelement.presentation.adapters;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

@RunWith(RobolectricTestRunner.class)
public class Test10526 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.MusicLibraryTests#organizeMusicCollectionTest/Trace-1650046539302.txt
     * Method invocation under test: <fifthelement.theelement.presentation.adapters.SongsListAdapter: int getCount()>_791_1582
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_presentation_adapters_SongsListAdapter_getCount_007() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        fifthelement.theelement.objects.Song song188 = null;
        fifthelement.theelement.objects.Album album310 = null;
        fifthelement.theelement.objects.Author author826 = null;
        fifthelement.theelement.objects.Author author827 = null;
        java.util.ArrayList arraylist77 = null;
        fifthelement.theelement.objects.Album album311 = null;
        fifthelement.theelement.objects.Album album312 = null;
        fifthelement.theelement.objects.Song song189 = null;
        fifthelement.theelement.objects.Author author828 = null;
        fifthelement.theelement.objects.Author author829 = null;
        fifthelement.theelement.presentation.adapters.SongsListAdapter songslistadapter9 = null;
        fifthelement.theelement.objects.Author author830 = null;
        fifthelement.theelement.objects.Song song190 = null;
        fifthelement.theelement.objects.Song song191 = null;
        java.util.List list32 = null;
        fifthelement.theelement.objects.Author author831 = null;
        fifthelement.theelement.objects.Author author832 = null;
        android.content.Context context35 = ApplicationProvider.getApplicationContext();
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
        arraylist77 = new java.util.ArrayList();
        java.util.UUID uuid14517 = java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid14518 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author827 = new fifthelement.theelement.objects.Author(uuid14518, "");
        song191 = new fifthelement.theelement.objects.Song(uuid14517, "This Is America", "android.resource://fifthelement.theelement/raw/childish_gambino_this_is_america", author827, album312, "Hiphop", 3, 1.5);
        arraylist77.add(song191);
        java.util.UUID uuid14519 = java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        song188 = new fifthelement.theelement.objects.Song(uuid14519, "Hall of Fame", "android.resource://fifthelement.theelement/raw/hall_of_fame", author829, album312, "", 3, 4.5);
        arraylist77.add(song188);
        java.util.UUID uuid14520 = java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        song190 = new fifthelement.theelement.objects.Song(uuid14520, "Classical Music", "android.resource://fifthelement.theelement/raw/classical_music", author829, album312, "Classical", 4, 2.0);
        arraylist77.add(song190);
        java.util.UUID uuid14521 = java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid14522 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author830 = new fifthelement.theelement.objects.Author(uuid14522, "");
        java.util.UUID uuid14523 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        album311 = new fifthelement.theelement.objects.Album(uuid14523, "");
        song189 = new fifthelement.theelement.objects.Song(uuid14521, "Adventure of a Lifetime", "android.resource://fifthelement.theelement/raw/coldplay_adventure_of_a_lifetime", author830, album311, "Pop", 10, 3.5);
        arraylist77.add(song189);
        arraylist77.iterator();
        song191.getAuthor();
        fifthelement.theelement.objects.Author author834 = song191.getAuthor();
        author834.getUUID();
        java.util.UUID uuid14525 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author831 = new fifthelement.theelement.objects.Author(uuid14525, "Childish Gambino", 3);
        song191.setAuthor(author831);
        song191.getAlbum();
        song188.getAuthor();
        song188.getAlbum();
        song190.getAuthor();
        song190.getAlbum();
        song189.getAuthor();
        fifthelement.theelement.objects.Author author838 = song189.getAuthor();
        author838.getUUID();
        java.util.UUID uuid14527 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author828 = new fifthelement.theelement.objects.Author(uuid14527, "Coldplay", 10);
        song189.setAuthor(author828);
        song189.getAlbum();
        fifthelement.theelement.objects.Album album317 = song189.getAlbum();
        album317.getUUID();
        java.util.UUID uuid14529 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID uuid14530 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author826 = new fifthelement.theelement.objects.Author(uuid14530, "");
        album310 = new fifthelement.theelement.objects.Album(uuid14529, "A Head Full of Dreams", author826, list32, 10);
        album310.getAuthor();
        fifthelement.theelement.objects.Author author840 = album310.getAuthor();
        author840.getUUID();
        java.util.UUID uuid14532 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author832 = new fifthelement.theelement.objects.Author(uuid14532, "Coldplay", 10);
        album310.setAuthor(author832);
        song189.setAlbum(album310);
        song188.compareTo(song191);
        song190.compareTo(song188);
        song189.compareTo(song190);
        songslistadapter9 = new fifthelement.theelement.presentation.adapters.SongsListAdapter(context35, arraylist77);
        songslistadapter9.getCount();
        songslistadapter9.getCount();
        songslistadapter9.getCount();
    }
}

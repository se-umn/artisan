package fifthelement.theelement.presentation.adapters;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

@RunWith(RobolectricTestRunner.class)
public class Test10431 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.MusicLibraryTests#organizeMusicCollectionTest/Trace-1650046539302.txt
     * Method invocation under test: <fifthelement.theelement.presentation.adapters.SongsListAdapter: int getCount()>_545_1086
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_presentation_adapters_SongsListAdapter_getCount_002() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        fifthelement.theelement.objects.Song song89 = null;
        fifthelement.theelement.objects.Author author371 = null;
        fifthelement.theelement.objects.Author author372 = null;
        fifthelement.theelement.objects.Album album146 = null;
        fifthelement.theelement.objects.Author author373 = null;
        fifthelement.theelement.objects.Author author374 = null;
        fifthelement.theelement.objects.Song song90 = null;
        fifthelement.theelement.objects.Album album147 = null;
        fifthelement.theelement.presentation.adapters.SongsListAdapter songslistadapter2 = null;
        fifthelement.theelement.objects.Author author375 = null;
        fifthelement.theelement.objects.Song song91 = null;
        fifthelement.theelement.objects.Author author376 = null;
        fifthelement.theelement.objects.Song song92 = null;
        java.util.ArrayList arraylist29 = null;
        fifthelement.theelement.objects.Author author377 = null;
        java.util.List list16 = null;
        fifthelement.theelement.objects.Album album148 = null;
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
        arraylist29 = new java.util.ArrayList();
        java.util.UUID uuid5144 = java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid5145 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author377 = new fifthelement.theelement.objects.Author(uuid5145, "");
        song89 = new fifthelement.theelement.objects.Song(uuid5144, "This Is America", "android.resource://fifthelement.theelement/raw/childish_gambino_this_is_america", author377, album146, "Hiphop", 3, 1.5);
        arraylist29.add(song89);
        java.util.UUID uuid5146 = java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        song90 = new fifthelement.theelement.objects.Song(uuid5146, "Hall of Fame", "android.resource://fifthelement.theelement/raw/hall_of_fame", author374, album146, "", 3, 4.5);
        arraylist29.add(song90);
        java.util.UUID uuid5147 = java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        song91 = new fifthelement.theelement.objects.Song(uuid5147, "Classical Music", "android.resource://fifthelement.theelement/raw/classical_music", author374, album146, "Classical", 4, 2.0);
        arraylist29.add(song91);
        java.util.UUID uuid5148 = java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid5149 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author375 = new fifthelement.theelement.objects.Author(uuid5149, "");
        java.util.UUID uuid5150 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        album148 = new fifthelement.theelement.objects.Album(uuid5150, "");
        song92 = new fifthelement.theelement.objects.Song(uuid5148, "Adventure of a Lifetime", "android.resource://fifthelement.theelement/raw/coldplay_adventure_of_a_lifetime", author375, album148, "Pop", 10, 3.5);
        arraylist29.add(song92);
        arraylist29.iterator();
        song89.getAuthor();
        fifthelement.theelement.objects.Author author379 = song89.getAuthor();
        author379.getUUID();
        java.util.UUID uuid5152 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author376 = new fifthelement.theelement.objects.Author(uuid5152, "Childish Gambino", 3);
        song89.setAuthor(author376);
        song89.getAlbum();
        song90.getAuthor();
        song90.getAlbum();
        song91.getAuthor();
        song91.getAlbum();
        song92.getAuthor();
        fifthelement.theelement.objects.Author author383 = song92.getAuthor();
        author383.getUUID();
        java.util.UUID uuid5154 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author373 = new fifthelement.theelement.objects.Author(uuid5154, "Coldplay", 10);
        song92.setAuthor(author373);
        song92.getAlbum();
        fifthelement.theelement.objects.Album album153 = song92.getAlbum();
        album153.getUUID();
        java.util.UUID uuid5156 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID uuid5157 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author372 = new fifthelement.theelement.objects.Author(uuid5157, "");
        album147 = new fifthelement.theelement.objects.Album(uuid5156, "A Head Full of Dreams", author372, list16, 10);
        album147.getAuthor();
        fifthelement.theelement.objects.Author author385 = album147.getAuthor();
        author385.getUUID();
        java.util.UUID uuid5159 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author371 = new fifthelement.theelement.objects.Author(uuid5159, "Coldplay", 10);
        album147.setAuthor(author371);
        song92.setAlbum(album147);
        song90.compareTo(song89);
        song91.compareTo(song90);
        song92.compareTo(song91);
        songslistadapter2 = new fifthelement.theelement.presentation.adapters.SongsListAdapter(context21, arraylist29);
        songslistadapter2.getCount();
        songslistadapter2.getCount();
    }
}

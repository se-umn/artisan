package fifthelement.theelement.presentation.adapters;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

@RunWith(RobolectricTestRunner.class)
public class Test3166 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.PlayMusicTests#skipSongsTest/Trace-1650046483856.txt
     * Method invocation under test: <fifthelement.theelement.presentation.adapters.SongsListAdapter: int getCount()>_789_1575
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_presentation_adapters_SongsListAdapter_getCount_006() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        fifthelement.theelement.objects.Album album302 = null;
        fifthelement.theelement.objects.Song song184 = null;
        fifthelement.theelement.objects.Author author811 = null;
        fifthelement.theelement.presentation.adapters.SongsListAdapter songslistadapter8 = null;
        fifthelement.theelement.objects.Author author812 = null;
        fifthelement.theelement.objects.Album album303 = null;
        fifthelement.theelement.objects.Album album304 = null;
        fifthelement.theelement.objects.Author author813 = null;
        fifthelement.theelement.objects.Song song185 = null;
        fifthelement.theelement.objects.Author author814 = null;
        fifthelement.theelement.objects.Song song186 = null;
        fifthelement.theelement.objects.Song song187 = null;
        fifthelement.theelement.objects.Author author815 = null;
        java.util.List list31 = null;
        java.util.ArrayList arraylist76 = null;
        fifthelement.theelement.objects.Author author816 = null;
        fifthelement.theelement.objects.Author author817 = null;
        android.content.Context context33 = ApplicationProvider.getApplicationContext();
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
        arraylist76 = new java.util.ArrayList();
        java.util.UUID uuid14385 = java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid14386 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author816 = new fifthelement.theelement.objects.Author(uuid14386, "");
        song185 = new fifthelement.theelement.objects.Song(uuid14385, "This Is America", "android.resource://fifthelement.theelement/raw/childish_gambino_this_is_america", author816, album303, "Hiphop", 3, 1.5);
        arraylist76.add(song185);
        java.util.UUID uuid14387 = java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        song186 = new fifthelement.theelement.objects.Song(uuid14387, "Hall of Fame", "android.resource://fifthelement.theelement/raw/hall_of_fame", author813, album303, "", 3, 4.5);
        arraylist76.add(song186);
        java.util.UUID uuid14388 = java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        song187 = new fifthelement.theelement.objects.Song(uuid14388, "Classical Music", "android.resource://fifthelement.theelement/raw/classical_music", author813, album303, "Classical", 4, 2.0);
        arraylist76.add(song187);
        java.util.UUID uuid14389 = java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid14390 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author814 = new fifthelement.theelement.objects.Author(uuid14390, "");
        java.util.UUID uuid14391 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        album302 = new fifthelement.theelement.objects.Album(uuid14391, "");
        song184 = new fifthelement.theelement.objects.Song(uuid14389, "Adventure of a Lifetime", "android.resource://fifthelement.theelement/raw/coldplay_adventure_of_a_lifetime", author814, album302, "Pop", 10, 3.5);
        arraylist76.add(song184);
        arraylist76.iterator();
        song185.getAuthor();
        fifthelement.theelement.objects.Author author819 = song185.getAuthor();
        author819.getUUID();
        java.util.UUID uuid14393 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author817 = new fifthelement.theelement.objects.Author(uuid14393, "Childish Gambino", 3);
        song185.setAuthor(author817);
        song185.getAlbum();
        song186.getAuthor();
        song186.getAlbum();
        song187.getAuthor();
        song187.getAlbum();
        song184.getAuthor();
        fifthelement.theelement.objects.Author author823 = song184.getAuthor();
        author823.getUUID();
        java.util.UUID uuid14395 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author811 = new fifthelement.theelement.objects.Author(uuid14395, "Coldplay", 10);
        song184.setAuthor(author811);
        song184.getAlbum();
        fifthelement.theelement.objects.Album album309 = song184.getAlbum();
        album309.getUUID();
        java.util.UUID uuid14397 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID uuid14398 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author812 = new fifthelement.theelement.objects.Author(uuid14398, "");
        album304 = new fifthelement.theelement.objects.Album(uuid14397, "A Head Full of Dreams", author812, list31, 10);
        album304.getAuthor();
        fifthelement.theelement.objects.Author author825 = album304.getAuthor();
        author825.getUUID();
        java.util.UUID uuid14400 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author815 = new fifthelement.theelement.objects.Author(uuid14400, "Coldplay", 10);
        album304.setAuthor(author815);
        song184.setAlbum(album304);
        song186.compareTo(song185);
        song187.compareTo(song186);
        song184.compareTo(song187);
        songslistadapter8 = new fifthelement.theelement.presentation.adapters.SongsListAdapter(context33, arraylist76);
        songslistadapter8.getCount();
        songslistadapter8.getCount();
    }
}

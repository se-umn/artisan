package fifthelement.theelement.business.services;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

public class Test10204 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.MusicLibraryTests#searchSongsTest/Trace-1650046542039.txt
     * Method invocation under test: <fifthelement.theelement.business.services.SongListService: java.util.List getCurrentSongsList()>_661_1321
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_business_services_SongListService_getCurrentSongsList_002() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        java.util.ArrayList arraylist49 = null;
        fifthelement.theelement.objects.Author author504 = null;
        fifthelement.theelement.objects.Song song104 = null;
        fifthelement.theelement.objects.Author author505 = null;
        fifthelement.theelement.objects.Author author506 = null;
        fifthelement.theelement.objects.Album album183 = null;
        fifthelement.theelement.objects.Album album184 = null;
        fifthelement.theelement.business.services.SongListService songlistservice3 = null;
        fifthelement.theelement.objects.Song song105 = null;
        fifthelement.theelement.objects.Song song106 = null;
        fifthelement.theelement.objects.Song song107 = null;
        songlistservice3 = new fifthelement.theelement.business.services.SongListService();
        arraylist49 = new java.util.ArrayList();
        java.util.UUID uuid8368 = java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid8369 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author506 = new fifthelement.theelement.objects.Author(uuid8369, "");
        song105 = new fifthelement.theelement.objects.Song(uuid8368, "This Is America", "android.resource://fifthelement.theelement/raw/childish_gambino_this_is_america", author506, album183, "Hiphop", 3, 1.5);
        arraylist49.add(song105);
        java.util.UUID uuid8370 = java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        song104 = new fifthelement.theelement.objects.Song(uuid8370, "Hall of Fame", "android.resource://fifthelement.theelement/raw/hall_of_fame", author504, album183, "", 3, 4.5);
        arraylist49.add(song104);
        java.util.UUID uuid8371 = java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        song107 = new fifthelement.theelement.objects.Song(uuid8371, "Classical Music", "android.resource://fifthelement.theelement/raw/classical_music", author504, album183, "Classical", 4, 2.0);
        arraylist49.add(song107);
        java.util.UUID uuid8372 = java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid8373 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author505 = new fifthelement.theelement.objects.Author(uuid8373, "");
        java.util.UUID uuid8374 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        album184 = new fifthelement.theelement.objects.Album(uuid8374, "");
        song106 = new fifthelement.theelement.objects.Song(uuid8372, "Adventure of a Lifetime", "android.resource://fifthelement.theelement/raw/coldplay_adventure_of_a_lifetime", author505, album184, "Pop", 10, 3.5);
        arraylist49.add(song106);
        arraylist49.iterator();
        songlistservice3.setCurrentSongsList(arraylist49);
        songlistservice3.getCurrentSongsList();
        songlistservice3.getCurrentSongsList();
    }
}

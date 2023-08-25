package fifthelement.theelement.business.services;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

public class Test10117 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.MusicLibraryTests#searchSongsTest/Trace-1650046542039.txt
     * Method invocation under test: <fifthelement.theelement.business.services.SongListService: java.util.List getCurrentSongsList()>_421_841
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_business_services_SongListService_getCurrentSongsList_001() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        java.util.ArrayList arraylist13 = null;
        fifthelement.theelement.objects.Author author189 = null;
        fifthelement.theelement.objects.Song song40 = null;
        fifthelement.theelement.objects.Author author190 = null;
        fifthelement.theelement.objects.Author author191 = null;
        fifthelement.theelement.objects.Album album75 = null;
        fifthelement.theelement.objects.Album album76 = null;
        fifthelement.theelement.business.services.SongListService songlistservice2 = null;
        fifthelement.theelement.objects.Song song41 = null;
        fifthelement.theelement.objects.Song song42 = null;
        fifthelement.theelement.objects.Song song43 = null;
        songlistservice2 = new fifthelement.theelement.business.services.SongListService();
        arraylist13 = new java.util.ArrayList();
        java.util.UUID uuid2052 = java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid2053 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author191 = new fifthelement.theelement.objects.Author(uuid2053, "");
        song41 = new fifthelement.theelement.objects.Song(uuid2052, "This Is America", "android.resource://fifthelement.theelement/raw/childish_gambino_this_is_america", author191, album75, "Hiphop", 3, 1.5);
        arraylist13.add(song41);
        java.util.UUID uuid2054 = java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        song40 = new fifthelement.theelement.objects.Song(uuid2054, "Hall of Fame", "android.resource://fifthelement.theelement/raw/hall_of_fame", author189, album75, "", 3, 4.5);
        arraylist13.add(song40);
        java.util.UUID uuid2055 = java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        song43 = new fifthelement.theelement.objects.Song(uuid2055, "Classical Music", "android.resource://fifthelement.theelement/raw/classical_music", author189, album75, "Classical", 4, 2.0);
        arraylist13.add(song43);
        java.util.UUID uuid2056 = java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid2057 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author190 = new fifthelement.theelement.objects.Author(uuid2057, "");
        java.util.UUID uuid2058 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        album76 = new fifthelement.theelement.objects.Album(uuid2058, "");
        song42 = new fifthelement.theelement.objects.Song(uuid2056, "Adventure of a Lifetime", "android.resource://fifthelement.theelement/raw/coldplay_adventure_of_a_lifetime", author190, album76, "Pop", 10, 3.5);
        arraylist13.add(song42);
        arraylist13.iterator();
        songlistservice2.setCurrentSongsList(arraylist13);
        songlistservice2.getCurrentSongsList();
    }
}

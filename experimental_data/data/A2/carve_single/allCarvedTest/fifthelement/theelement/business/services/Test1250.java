package fifthelement.theelement.business.services;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

public class Test1250 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.MusicLibraryTests#searchSongsTest/Trace-1650046542039.txt
     * Method invocation under test: <fifthelement.theelement.business.services.SongListService: java.util.List getCurrentSongsList()>_421_841
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_business_services_SongListService_getCurrentSongsList_001() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        java.util.ArrayList arraylist5 = null;
        fifthelement.theelement.objects.Author author35 = null;
        fifthelement.theelement.objects.Song song9 = null;
        fifthelement.theelement.objects.Author author36 = null;
        fifthelement.theelement.objects.Author author37 = null;
        fifthelement.theelement.objects.Album album18 = null;
        fifthelement.theelement.objects.Album album19 = null;
        fifthelement.theelement.business.services.SongListService songlistservice2 = null;
        fifthelement.theelement.objects.Song song10 = null;
        fifthelement.theelement.objects.Song song11 = null;
        fifthelement.theelement.objects.Song song12 = null;
        songlistservice2 = new fifthelement.theelement.business.services.SongListService();
        arraylist5 = new java.util.ArrayList();
        java.util.UUID uuid243 = java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid244 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author37 = new fifthelement.theelement.objects.Author(uuid244, "");
        song10 = new fifthelement.theelement.objects.Song(uuid243, "This Is America", "android.resource://fifthelement.theelement/raw/childish_gambino_this_is_america", author37, album18, "Hiphop", 3, 1.5);
        arraylist5.add(song10);
        java.util.UUID uuid245 = java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        song9 = new fifthelement.theelement.objects.Song(uuid245, "Hall of Fame", "android.resource://fifthelement.theelement/raw/hall_of_fame", author35, album18, "", 3, 4.5);
        arraylist5.add(song9);
        java.util.UUID uuid246 = java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        song12 = new fifthelement.theelement.objects.Song(uuid246, "Classical Music", "android.resource://fifthelement.theelement/raw/classical_music", author35, album18, "Classical", 4, 2.0);
        arraylist5.add(song12);
        java.util.UUID uuid247 = java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid248 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author36 = new fifthelement.theelement.objects.Author(uuid248, "");
        java.util.UUID uuid249 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        album19 = new fifthelement.theelement.objects.Album(uuid249, "");
        song11 = new fifthelement.theelement.objects.Song(uuid247, "Adventure of a Lifetime", "android.resource://fifthelement.theelement/raw/coldplay_adventure_of_a_lifetime", author36, album19, "Pop", 10, 3.5);
        arraylist5.add(song11);
        arraylist5.iterator();
        songlistservice2.setCurrentSongsList(arraylist5);
        songlistservice2.getCurrentSongsList();
    }
}

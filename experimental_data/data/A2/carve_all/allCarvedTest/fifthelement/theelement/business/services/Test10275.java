package fifthelement.theelement.business.services;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

public class Test10275 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.MusicLibraryTests#searchSongsTest/Trace-1650046542039.txt
     * Method invocation under test: <fifthelement.theelement.business.services.SongListService: void setCurrentSongsList(java.util.List)>_891_1781
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_business_services_SongListService_setCurrentSongsList_002() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        java.util.ArrayList arraylist309 = null;
        fifthelement.theelement.objects.Author author1242 = null;
        fifthelement.theelement.objects.Song song705 = null;
        fifthelement.theelement.objects.Author author1243 = null;
        fifthelement.theelement.objects.Author author1244 = null;
        fifthelement.theelement.objects.Album album524 = null;
        fifthelement.theelement.objects.Album album525 = null;
        fifthelement.theelement.business.services.SongListService songlistservice4 = null;
        fifthelement.theelement.objects.Song song706 = null;
        fifthelement.theelement.objects.Song song707 = null;
        fifthelement.theelement.objects.Song song708 = null;
        songlistservice4 = new fifthelement.theelement.business.services.SongListService();
        arraylist309 = new java.util.ArrayList();
        java.util.UUID uuid15603 = java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid15604 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author1244 = new fifthelement.theelement.objects.Author(uuid15604, "");
        song706 = new fifthelement.theelement.objects.Song(uuid15603, "This Is America", "android.resource://fifthelement.theelement/raw/childish_gambino_this_is_america", author1244, album524, "Hiphop", 3, 1.5);
        arraylist309.add(song706);
        java.util.UUID uuid15605 = java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        song705 = new fifthelement.theelement.objects.Song(uuid15605, "Hall of Fame", "android.resource://fifthelement.theelement/raw/hall_of_fame", author1242, album524, "", 3, 4.5);
        arraylist309.add(song705);
        java.util.UUID uuid15606 = java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        song708 = new fifthelement.theelement.objects.Song(uuid15606, "Classical Music", "android.resource://fifthelement.theelement/raw/classical_music", author1242, album524, "Classical", 4, 2.0);
        arraylist309.add(song708);
        java.util.UUID uuid15607 = java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid15608 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author1243 = new fifthelement.theelement.objects.Author(uuid15608, "");
        java.util.UUID uuid15609 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        album525 = new fifthelement.theelement.objects.Album(uuid15609, "");
        song707 = new fifthelement.theelement.objects.Song(uuid15607, "Adventure of a Lifetime", "android.resource://fifthelement.theelement/raw/coldplay_adventure_of_a_lifetime", author1243, album525, "Pop", 10, 3.5);
        arraylist309.add(song707);
        arraylist309.iterator();
        songlistservice4.setCurrentSongsList(arraylist309);
        songlistservice4.getCurrentSongsList();
        java.util.ArrayList arraylist311 = (java.util.ArrayList) songlistservice4.getCurrentSongsList();
        songlistservice4.setCurrentSongsList(arraylist311);
    }
}

package fifthelement.theelement.business.services;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

public class Test9574 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.PlayMusicTests#playPausingTest/Trace-1650046475193.txt
     * Method invocation under test: <fifthelement.theelement.business.services.SongListService: void setCurrentSongsList(java.util.List)>_290_579
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_business_services_SongListService_setCurrentSongsList_001() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        java.util.ArrayList arraylist12 = null;
        fifthelement.theelement.objects.Author author186 = null;
        fifthelement.theelement.objects.Song song36 = null;
        fifthelement.theelement.objects.Author author187 = null;
        fifthelement.theelement.objects.Author author188 = null;
        fifthelement.theelement.objects.Album album73 = null;
        fifthelement.theelement.objects.Album album74 = null;
        fifthelement.theelement.business.services.SongListService songlistservice1 = null;
        fifthelement.theelement.objects.Song song37 = null;
        fifthelement.theelement.objects.Song song38 = null;
        fifthelement.theelement.objects.Song song39 = null;
        songlistservice1 = new fifthelement.theelement.business.services.SongListService();
        arraylist12 = new java.util.ArrayList();
        java.util.UUID uuid2038 = java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid2039 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author188 = new fifthelement.theelement.objects.Author(uuid2039, "");
        song37 = new fifthelement.theelement.objects.Song(uuid2038, "This Is America", "android.resource://fifthelement.theelement/raw/childish_gambino_this_is_america", author188, album73, "Hiphop", 3, 1.5);
        arraylist12.add(song37);
        java.util.UUID uuid2040 = java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        song36 = new fifthelement.theelement.objects.Song(uuid2040, "Hall of Fame", "android.resource://fifthelement.theelement/raw/hall_of_fame", author186, album73, "", 3, 4.5);
        arraylist12.add(song36);
        java.util.UUID uuid2041 = java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        song39 = new fifthelement.theelement.objects.Song(uuid2041, "Classical Music", "android.resource://fifthelement.theelement/raw/classical_music", author186, album73, "Classical", 4, 2.0);
        arraylist12.add(song39);
        java.util.UUID uuid2042 = java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid2043 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author187 = new fifthelement.theelement.objects.Author(uuid2043, "");
        java.util.UUID uuid2044 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        album74 = new fifthelement.theelement.objects.Album(uuid2044, "");
        song38 = new fifthelement.theelement.objects.Song(uuid2042, "Adventure of a Lifetime", "android.resource://fifthelement.theelement/raw/coldplay_adventure_of_a_lifetime", author187, album74, "Pop", 10, 3.5);
        arraylist12.add(song38);
        arraylist12.iterator();
        songlistservice1.setCurrentSongsList(arraylist12);
    }
}

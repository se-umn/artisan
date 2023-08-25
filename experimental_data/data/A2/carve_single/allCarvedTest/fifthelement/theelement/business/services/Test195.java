package fifthelement.theelement.business.services;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

public class Test195 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.SettingsTest#SelectTheme2Test/Trace-1650046463827.txt
     * Method invocation under test: <fifthelement.theelement.business.services.SongListService: void setCurrentSongsList(java.util.List)>_290_579
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_business_services_SongListService_setCurrentSongsList_001() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        fifthelement.theelement.objects.Song song5 = null;
        fifthelement.theelement.objects.Album album16 = null;
        fifthelement.theelement.objects.Author author32 = null;
        fifthelement.theelement.objects.Author author33 = null;
        fifthelement.theelement.business.services.SongListService songlistservice1 = null;
        java.util.ArrayList arraylist4 = null;
        fifthelement.theelement.objects.Album album17 = null;
        fifthelement.theelement.objects.Author author34 = null;
        fifthelement.theelement.objects.Song song6 = null;
        fifthelement.theelement.objects.Song song7 = null;
        fifthelement.theelement.objects.Song song8 = null;
        songlistservice1 = new fifthelement.theelement.business.services.SongListService();
        arraylist4 = new java.util.ArrayList();
        java.util.UUID uuid229 = java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid230 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author34 = new fifthelement.theelement.objects.Author(uuid230, "");
        song8 = new fifthelement.theelement.objects.Song(uuid229, "This Is America", "android.resource://fifthelement.theelement/raw/childish_gambino_this_is_america", author34, album17, "Hiphop", 3, 1.5);
        arraylist4.add(song8);
        java.util.UUID uuid231 = java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        song5 = new fifthelement.theelement.objects.Song(uuid231, "Hall of Fame", "android.resource://fifthelement.theelement/raw/hall_of_fame", author33, album17, "", 3, 4.5);
        arraylist4.add(song5);
        java.util.UUID uuid232 = java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        song6 = new fifthelement.theelement.objects.Song(uuid232, "Classical Music", "android.resource://fifthelement.theelement/raw/classical_music", author33, album17, "Classical", 4, 2.0);
        arraylist4.add(song6);
        java.util.UUID uuid233 = java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid234 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author32 = new fifthelement.theelement.objects.Author(uuid234, "");
        java.util.UUID uuid235 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        album16 = new fifthelement.theelement.objects.Album(uuid235, "");
        song7 = new fifthelement.theelement.objects.Song(uuid233, "Adventure of a Lifetime", "android.resource://fifthelement.theelement/raw/coldplay_adventure_of_a_lifetime", author32, album16, "Pop", 10, 3.5);
        arraylist4.add(song7);
        arraylist4.iterator();
        songlistservice1.setCurrentSongsList(arraylist4);
    }
}

package fifthelement.theelement.business.services;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

public class Test1762 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.SettingsTest#selectTheme3Test/Trace-1650046433616.txt
     * Method invocation under test: <fifthelement.theelement.business.services.SongListService: void setCurrentSongsList(java.util.List)>_615_1229
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_business_services_SongListService_setCurrentSongsList_002() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        fifthelement.theelement.objects.Album album115 = null;
        fifthelement.theelement.objects.Song song58 = null;
        fifthelement.theelement.objects.Author author313 = null;
        fifthelement.theelement.objects.Author author314 = null;
        fifthelement.theelement.objects.Author author315 = null;
        fifthelement.theelement.objects.Song song59 = null;
        fifthelement.theelement.objects.Album album116 = null;
        fifthelement.theelement.objects.Song song60 = null;
        fifthelement.theelement.objects.Song song61 = null;
        java.util.ArrayList arraylist25 = null;
        fifthelement.theelement.objects.Author author316 = null;
        fifthelement.theelement.business.services.SongListService songlistservice2 = null;
        fifthelement.theelement.objects.Song song62 = null;
        java.util.ArrayList arraylist26 = null;
        fifthelement.theelement.objects.Album album117 = null;
        fifthelement.theelement.objects.Song song63 = null;
        fifthelement.theelement.objects.Song song64 = null;
        fifthelement.theelement.objects.Song song65 = null;
        fifthelement.theelement.objects.Author author317 = null;
        songlistservice2 = new fifthelement.theelement.business.services.SongListService();
        arraylist26 = new java.util.ArrayList();
        java.util.UUID uuid4492 = java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid4493 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author315 = new fifthelement.theelement.objects.Author(uuid4493, "");
        song59 = new fifthelement.theelement.objects.Song(uuid4492, "This Is America", "android.resource://fifthelement.theelement/raw/childish_gambino_this_is_america", author315, album117, "Hiphop", 3, 1.5);
        arraylist26.add(song59);
        java.util.UUID uuid4494 = java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        song60 = new fifthelement.theelement.objects.Song(uuid4494, "Hall of Fame", "android.resource://fifthelement.theelement/raw/hall_of_fame", author314, album117, "", 3, 4.5);
        arraylist26.add(song60);
        java.util.UUID uuid4495 = java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        song64 = new fifthelement.theelement.objects.Song(uuid4495, "Classical Music", "android.resource://fifthelement.theelement/raw/classical_music", author314, album117, "Classical", 4, 2.0);
        arraylist26.add(song64);
        java.util.UUID uuid4496 = java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid4497 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author313 = new fifthelement.theelement.objects.Author(uuid4497, "");
        java.util.UUID uuid4498 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        album115 = new fifthelement.theelement.objects.Album(uuid4498, "");
        song65 = new fifthelement.theelement.objects.Song(uuid4496, "Adventure of a Lifetime", "android.resource://fifthelement.theelement/raw/coldplay_adventure_of_a_lifetime", author313, album115, "Pop", 10, 3.5);
        arraylist26.add(song65);
        arraylist26.iterator();
        java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        songlistservice2.setCurrentSongsList(arraylist26);
        arraylist25 = new java.util.ArrayList();
        java.util.UUID uuid4504 = java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid4505 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author316 = new fifthelement.theelement.objects.Author(uuid4505, "");
        song62 = new fifthelement.theelement.objects.Song(uuid4504, "This Is America", "android.resource://fifthelement.theelement/raw/childish_gambino_this_is_america", author316, album117, "Hiphop", 3, 1.5);
        arraylist25.add(song62);
        java.util.UUID uuid4506 = java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        song63 = new fifthelement.theelement.objects.Song(uuid4506, "Hall of Fame", "android.resource://fifthelement.theelement/raw/hall_of_fame", author314, album117, "", 3, 4.5);
        arraylist25.add(song63);
        java.util.UUID uuid4507 = java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        song61 = new fifthelement.theelement.objects.Song(uuid4507, "Classical Music", "android.resource://fifthelement.theelement/raw/classical_music", author314, album117, "Classical", 4, 2.0);
        arraylist25.add(song61);
        java.util.UUID uuid4508 = java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid4509 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author317 = new fifthelement.theelement.objects.Author(uuid4509, "");
        java.util.UUID uuid4510 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        album116 = new fifthelement.theelement.objects.Album(uuid4510, "");
        song58 = new fifthelement.theelement.objects.Song(uuid4508, "Adventure of a Lifetime", "android.resource://fifthelement.theelement/raw/coldplay_adventure_of_a_lifetime", author317, album116, "Pop", 10, 3.5);
        arraylist25.add(song58);
        arraylist25.iterator();
        songlistservice2.setCurrentSongsList(arraylist25);
    }
}

package fifthelement.theelement.business.services;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

public class Test938 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.SettingsTest#deleteSongsTest/Trace-1650046437980.txt
     * Method invocation under test: <fifthelement.theelement.business.services.SongListService: void sortSongs(java.util.List)>_1227_2451
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_business_services_SongListService_sortSongs_001() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        fifthelement.theelement.objects.Author author79 = null;
        fifthelement.theelement.objects.Song song15 = null;
        fifthelement.theelement.objects.Author author80 = null;
        fifthelement.theelement.objects.Song song16 = null;
        fifthelement.theelement.objects.Album album39 = null;
        fifthelement.theelement.objects.Song song17 = null;
        fifthelement.theelement.objects.Author author81 = null;
        java.util.ArrayList arraylist42 = null;
        fifthelement.theelement.objects.Author author82 = null;
        fifthelement.theelement.objects.Song song18 = null;
        fifthelement.theelement.objects.Song song19 = null;
        java.util.ArrayList arraylist43 = null;
        fifthelement.theelement.objects.Song song20 = null;
        fifthelement.theelement.objects.Album album40 = null;
        fifthelement.theelement.business.services.SongListService songlistservice2 = null;
        fifthelement.theelement.objects.Album album41 = null;
        fifthelement.theelement.objects.Song song21 = null;
        fifthelement.theelement.objects.Author author83 = null;
        fifthelement.theelement.objects.Song song22 = null;
        songlistservice2 = new fifthelement.theelement.business.services.SongListService();
        arraylist43 = new java.util.ArrayList();
        java.util.UUID uuid1358 = java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid1359 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author82 = new fifthelement.theelement.objects.Author(uuid1359, "");
        song18 = new fifthelement.theelement.objects.Song(uuid1358, "This Is America", "android.resource://fifthelement.theelement/raw/childish_gambino_this_is_america", author82, album41, "Hiphop", 3, 1.5);
        arraylist43.add(song18);
        java.util.UUID uuid1360 = java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        song19 = new fifthelement.theelement.objects.Song(uuid1360, "Hall of Fame", "android.resource://fifthelement.theelement/raw/hall_of_fame", author83, album41, "", 3, 4.5);
        arraylist43.add(song19);
        java.util.UUID uuid1361 = java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        song21 = new fifthelement.theelement.objects.Song(uuid1361, "Classical Music", "android.resource://fifthelement.theelement/raw/classical_music", author83, album41, "Classical", 4, 2.0);
        arraylist43.add(song21);
        java.util.UUID uuid1362 = java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid1363 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author81 = new fifthelement.theelement.objects.Author(uuid1363, "");
        java.util.UUID uuid1364 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        album39 = new fifthelement.theelement.objects.Album(uuid1364, "");
        song16 = new fifthelement.theelement.objects.Song(uuid1362, "Adventure of a Lifetime", "android.resource://fifthelement.theelement/raw/coldplay_adventure_of_a_lifetime", author81, album39, "Pop", 10, 3.5);
        arraylist43.add(song16);
        arraylist43.iterator();
        java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        songlistservice2.setCurrentSongsList(arraylist43);
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
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
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
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
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
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        arraylist42 = new java.util.ArrayList();
        java.util.UUID uuid1429 = java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid1430 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author80 = new fifthelement.theelement.objects.Author(uuid1430, "");
        song17 = new fifthelement.theelement.objects.Song(uuid1429, "This Is America", "android.resource://fifthelement.theelement/raw/childish_gambino_this_is_america", author80, album41, "Hiphop", 3, 1.5);
        arraylist42.add(song17);
        java.util.UUID uuid1431 = java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        song15 = new fifthelement.theelement.objects.Song(uuid1431, "Hall of Fame", "android.resource://fifthelement.theelement/raw/hall_of_fame", author83, album41, "", 3, 4.5);
        arraylist42.add(song15);
        java.util.UUID uuid1432 = java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        song20 = new fifthelement.theelement.objects.Song(uuid1432, "Classical Music", "android.resource://fifthelement.theelement/raw/classical_music", author83, album41, "Classical", 4, 2.0);
        arraylist42.add(song20);
        java.util.UUID uuid1433 = java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid1434 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author79 = new fifthelement.theelement.objects.Author(uuid1434, "");
        java.util.UUID uuid1435 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        album40 = new fifthelement.theelement.objects.Album(uuid1435, "");
        song22 = new fifthelement.theelement.objects.Song(uuid1433, "Adventure of a Lifetime", "android.resource://fifthelement.theelement/raw/coldplay_adventure_of_a_lifetime", author79, album40, "Pop", 10, 3.5);
        arraylist42.add(song22);
        arraylist42.iterator();
        songlistservice2.sortSongs(arraylist42);
    }
}

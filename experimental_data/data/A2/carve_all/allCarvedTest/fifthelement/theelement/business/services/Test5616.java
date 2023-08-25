package fifthelement.theelement.business.services;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

public class Test5616 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.SettingsTest#playSongWithSongPlayedCheck/Trace-1650046467562.txt
     * Method invocation under test: <fifthelement.theelement.business.services.SongListService: boolean getAutoplayEnabled()>_1250_2497
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_business_services_SongListService_getAutoplayEnabled_001() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        fifthelement.theelement.objects.Author author1168 = null;
        fifthelement.theelement.objects.Song song172 = null;
        fifthelement.theelement.objects.Song song173 = null;
        fifthelement.theelement.objects.Album album418 = null;
        fifthelement.theelement.objects.Author author1169 = null;
        fifthelement.theelement.objects.Author author1170 = null;
        fifthelement.theelement.objects.Author author1171 = null;
        fifthelement.theelement.objects.Song song174 = null;
        fifthelement.theelement.objects.Song song175 = null;
        fifthelement.theelement.objects.Song song176 = null;
        java.util.ArrayList arraylist273 = null;
        fifthelement.theelement.objects.Song song177 = null;
        fifthelement.theelement.objects.Author author1172 = null;
        fifthelement.theelement.objects.Song song178 = null;
        fifthelement.theelement.objects.Author author1173 = null;
        fifthelement.theelement.business.services.SongListService songlistservice3 = null;
        fifthelement.theelement.objects.Album album419 = null;
        java.util.ArrayList arraylist274 = null;
        fifthelement.theelement.objects.Song song179 = null;
        fifthelement.theelement.objects.Album album420 = null;
        fifthelement.theelement.objects.Author author1174 = null;
        fifthelement.theelement.objects.Author author1175 = null;
        fifthelement.theelement.objects.Album album421 = null;
        java.util.List list66 = null;
        fifthelement.theelement.objects.Author author1176 = null;
        songlistservice3 = new fifthelement.theelement.business.services.SongListService();
        arraylist273 = new java.util.ArrayList();
        java.util.UUID uuid30950 = java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid30951 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author1171 = new fifthelement.theelement.objects.Author(uuid30951, "");
        song174 = new fifthelement.theelement.objects.Song(uuid30950, "This Is America", "android.resource://fifthelement.theelement/raw/childish_gambino_this_is_america", author1171, album419, "Hiphop", 3, 1.5);
        arraylist273.add(song174);
        java.util.UUID uuid30952 = java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        song175 = new fifthelement.theelement.objects.Song(uuid30952, "Hall of Fame", "android.resource://fifthelement.theelement/raw/hall_of_fame", author1174, album419, "", 3, 4.5);
        arraylist273.add(song175);
        java.util.UUID uuid30953 = java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        song179 = new fifthelement.theelement.objects.Song(uuid30953, "Classical Music", "android.resource://fifthelement.theelement/raw/classical_music", author1174, album419, "Classical", 4, 2.0);
        arraylist273.add(song179);
        java.util.UUID uuid30954 = java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid30955 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author1169 = new fifthelement.theelement.objects.Author(uuid30955, "");
        java.util.UUID uuid30956 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        album418 = new fifthelement.theelement.objects.Album(uuid30956, "");
        song173 = new fifthelement.theelement.objects.Song(uuid30954, "Adventure of a Lifetime", "android.resource://fifthelement.theelement/raw/coldplay_adventure_of_a_lifetime", author1169, album418, "Pop", 10, 3.5);
        arraylist273.add(song173);
        arraylist273.iterator();
        java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        songlistservice3.setCurrentSongsList(arraylist273);
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
        arraylist274 = new java.util.ArrayList();
        java.util.UUID uuid31021 = java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid31022 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author1172 = new fifthelement.theelement.objects.Author(uuid31022, "");
        song178 = new fifthelement.theelement.objects.Song(uuid31021, "This Is America", "android.resource://fifthelement.theelement/raw/childish_gambino_this_is_america", author1172, album419, "Hiphop", 3, 1.5);
        arraylist274.add(song178);
        java.util.UUID uuid31023 = java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        song177 = new fifthelement.theelement.objects.Song(uuid31023, "Hall of Fame", "android.resource://fifthelement.theelement/raw/hall_of_fame", author1174, album419, "", 3, 4.5);
        arraylist274.add(song177);
        java.util.UUID uuid31024 = java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        song172 = new fifthelement.theelement.objects.Song(uuid31024, "Classical Music", "android.resource://fifthelement.theelement/raw/classical_music", author1174, album419, "Classical", 4, 2.0);
        arraylist274.add(song172);
        java.util.UUID uuid31025 = java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid31026 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author1168 = new fifthelement.theelement.objects.Author(uuid31026, "");
        java.util.UUID uuid31027 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        album421 = new fifthelement.theelement.objects.Album(uuid31027, "");
        song176 = new fifthelement.theelement.objects.Song(uuid31025, "Adventure of a Lifetime", "android.resource://fifthelement.theelement/raw/coldplay_adventure_of_a_lifetime", author1168, album421, "Pop", 10, 3.5);
        arraylist274.add(song176);
        arraylist274.iterator();
        song178.getAuthor();
        fifthelement.theelement.objects.Author author1178 = song178.getAuthor();
        author1178.getUUID();
        java.util.UUID uuid31029 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author1173 = new fifthelement.theelement.objects.Author(uuid31029, "Childish Gambino", 3);
        song178.setAuthor(author1173);
        song178.getAlbum();
        song177.getAuthor();
        song177.getAlbum();
        song172.getAuthor();
        song172.getAlbum();
        song176.getAuthor();
        fifthelement.theelement.objects.Author author1182 = song176.getAuthor();
        author1182.getUUID();
        java.util.UUID uuid31031 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author1170 = new fifthelement.theelement.objects.Author(uuid31031, "Coldplay", 10);
        song176.setAuthor(author1170);
        song176.getAlbum();
        fifthelement.theelement.objects.Album album426 = song176.getAlbum();
        album426.getUUID();
        java.util.UUID uuid31033 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID uuid31034 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author1176 = new fifthelement.theelement.objects.Author(uuid31034, "");
        album420 = new fifthelement.theelement.objects.Album(uuid31033, "A Head Full of Dreams", author1176, list66, 10);
        album420.getAuthor();
        fifthelement.theelement.objects.Author author1184 = album420.getAuthor();
        author1184.getUUID();
        java.util.UUID uuid31036 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author1175 = new fifthelement.theelement.objects.Author(uuid31036, "Coldplay", 10);
        album420.setAuthor(author1175);
        song176.setAlbum(album420);
        songlistservice3.sortSongs(arraylist274);
        arraylist274.size();
        arraylist274.size();
        songlistservice3.getAutoplayEnabled();
    }
}

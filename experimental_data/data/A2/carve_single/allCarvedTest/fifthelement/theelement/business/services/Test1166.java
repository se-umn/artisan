package fifthelement.theelement.business.services;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

public class Test1166 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.PlayMusicTests#playPausingTest/Trace-1650046475193.txt
     * Method invocation under test: <fifthelement.theelement.business.services.SongListService: void sortSongs(java.util.List)>_526_1049
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_business_services_SongListService_sortSongs_001() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        fifthelement.theelement.objects.Song song9 = null;
        java.util.ArrayList arraylist5 = null;
        fifthelement.theelement.objects.Author author35 = null;
        fifthelement.theelement.objects.Author author36 = null;
        fifthelement.theelement.objects.Author author37 = null;
        fifthelement.theelement.objects.Song song10 = null;
        java.util.ArrayList arraylist6 = null;
        fifthelement.theelement.objects.Album album18 = null;
        fifthelement.theelement.objects.Author author38 = null;
        fifthelement.theelement.objects.Song song11 = null;
        fifthelement.theelement.objects.Album album19 = null;
        fifthelement.theelement.objects.Song song12 = null;
        fifthelement.theelement.objects.Song song13 = null;
        fifthelement.theelement.objects.Author author39 = null;
        fifthelement.theelement.objects.Song song14 = null;
        fifthelement.theelement.objects.Song song15 = null;
        fifthelement.theelement.objects.Album album20 = null;
        fifthelement.theelement.business.services.SongListService songlistservice2 = null;
        fifthelement.theelement.objects.Song song16 = null;
        songlistservice2 = new fifthelement.theelement.business.services.SongListService();
        arraylist5 = new java.util.ArrayList();
        java.util.UUID uuid255 = java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid256 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author37 = new fifthelement.theelement.objects.Author(uuid256, "");
        song11 = new fifthelement.theelement.objects.Song(uuid255, "This Is America", "android.resource://fifthelement.theelement/raw/childish_gambino_this_is_america", author37, album20, "Hiphop", 3, 1.5);
        arraylist5.add(song11);
        java.util.UUID uuid257 = java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        song13 = new fifthelement.theelement.objects.Song(uuid257, "Hall of Fame", "android.resource://fifthelement.theelement/raw/hall_of_fame", author35, album20, "", 3, 4.5);
        arraylist5.add(song13);
        java.util.UUID uuid258 = java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        song12 = new fifthelement.theelement.objects.Song(uuid258, "Classical Music", "android.resource://fifthelement.theelement/raw/classical_music", author35, album20, "Classical", 4, 2.0);
        arraylist5.add(song12);
        java.util.UUID uuid259 = java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid260 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author39 = new fifthelement.theelement.objects.Author(uuid260, "");
        java.util.UUID uuid261 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        album18 = new fifthelement.theelement.objects.Album(uuid261, "");
        song16 = new fifthelement.theelement.objects.Song(uuid259, "Adventure of a Lifetime", "android.resource://fifthelement.theelement/raw/coldplay_adventure_of_a_lifetime", author39, album18, "Pop", 10, 3.5);
        arraylist5.add(song16);
        arraylist5.iterator();
        java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        songlistservice2.setCurrentSongsList(arraylist5);
        arraylist6 = new java.util.ArrayList();
        java.util.UUID uuid267 = java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid268 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author36 = new fifthelement.theelement.objects.Author(uuid268, "");
        song9 = new fifthelement.theelement.objects.Song(uuid267, "This Is America", "android.resource://fifthelement.theelement/raw/childish_gambino_this_is_america", author36, album20, "Hiphop", 3, 1.5);
        arraylist6.add(song9);
        java.util.UUID uuid269 = java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        song10 = new fifthelement.theelement.objects.Song(uuid269, "Hall of Fame", "android.resource://fifthelement.theelement/raw/hall_of_fame", author35, album20, "", 3, 4.5);
        arraylist6.add(song10);
        java.util.UUID uuid270 = java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        song14 = new fifthelement.theelement.objects.Song(uuid270, "Classical Music", "android.resource://fifthelement.theelement/raw/classical_music", author35, album20, "Classical", 4, 2.0);
        arraylist6.add(song14);
        java.util.UUID uuid271 = java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid272 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author38 = new fifthelement.theelement.objects.Author(uuid272, "");
        java.util.UUID uuid273 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        album19 = new fifthelement.theelement.objects.Album(uuid273, "");
        song15 = new fifthelement.theelement.objects.Song(uuid271, "Adventure of a Lifetime", "android.resource://fifthelement.theelement/raw/coldplay_adventure_of_a_lifetime", author38, album19, "Pop", 10, 3.5);
        arraylist6.add(song15);
        arraylist6.iterator();
        songlistservice2.sortSongs(arraylist6);
    }
}

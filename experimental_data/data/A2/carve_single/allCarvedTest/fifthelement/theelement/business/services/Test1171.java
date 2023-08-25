package fifthelement.theelement.business.services;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

public class Test1171 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.PlayMusicTests#playPausingTest/Trace-1650046475193.txt
     * Method invocation under test: <fifthelement.theelement.business.services.SongListService: boolean getAutoplayEnabled()>_549_1095
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_business_services_SongListService_getAutoplayEnabled_001() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        fifthelement.theelement.objects.Song song28 = null;
        java.util.ArrayList arraylist9 = null;
        fifthelement.theelement.objects.Author author56 = null;
        fifthelement.theelement.objects.Song song29 = null;
        java.util.ArrayList arraylist10 = null;
        fifthelement.theelement.objects.Album album30 = null;
        fifthelement.theelement.objects.Author author57 = null;
        fifthelement.theelement.objects.Author author58 = null;
        fifthelement.theelement.objects.Song song30 = null;
        fifthelement.theelement.objects.Album album31 = null;
        fifthelement.theelement.business.services.SongListService songlistservice3 = null;
        fifthelement.theelement.objects.Author author59 = null;
        fifthelement.theelement.objects.Author author60 = null;
        fifthelement.theelement.objects.Author author61 = null;
        fifthelement.theelement.objects.Author author62 = null;
        fifthelement.theelement.objects.Author author63 = null;
        fifthelement.theelement.objects.Author author64 = null;
        fifthelement.theelement.objects.Song song31 = null;
        fifthelement.theelement.objects.Album album32 = null;
        fifthelement.theelement.objects.Song song32 = null;
        fifthelement.theelement.objects.Album album33 = null;
        fifthelement.theelement.objects.Song song33 = null;
        java.util.List list4 = null;
        fifthelement.theelement.objects.Song song34 = null;
        fifthelement.theelement.objects.Song song35 = null;
        songlistservice3 = new fifthelement.theelement.business.services.SongListService();
        arraylist9 = new java.util.ArrayList();
        java.util.UUID uuid572 = java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid573 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author63 = new fifthelement.theelement.objects.Author(uuid573, "");
        song31 = new fifthelement.theelement.objects.Song(uuid572, "This Is America", "android.resource://fifthelement.theelement/raw/childish_gambino_this_is_america", author63, album31, "Hiphop", 3, 1.5);
        arraylist9.add(song31);
        java.util.UUID uuid574 = java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        song33 = new fifthelement.theelement.objects.Song(uuid574, "Hall of Fame", "android.resource://fifthelement.theelement/raw/hall_of_fame", author61, album31, "", 3, 4.5);
        arraylist9.add(song33);
        java.util.UUID uuid575 = java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        song32 = new fifthelement.theelement.objects.Song(uuid575, "Classical Music", "android.resource://fifthelement.theelement/raw/classical_music", author61, album31, "Classical", 4, 2.0);
        arraylist9.add(song32);
        java.util.UUID uuid576 = java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid577 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author58 = new fifthelement.theelement.objects.Author(uuid577, "");
        java.util.UUID uuid578 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        album30 = new fifthelement.theelement.objects.Album(uuid578, "");
        song35 = new fifthelement.theelement.objects.Song(uuid576, "Adventure of a Lifetime", "android.resource://fifthelement.theelement/raw/coldplay_adventure_of_a_lifetime", author58, album30, "Pop", 10, 3.5);
        arraylist9.add(song35);
        arraylist9.iterator();
        java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        songlistservice3.setCurrentSongsList(arraylist9);
        arraylist10 = new java.util.ArrayList();
        java.util.UUID uuid584 = java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid585 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author62 = new fifthelement.theelement.objects.Author(uuid585, "");
        song28 = new fifthelement.theelement.objects.Song(uuid584, "This Is America", "android.resource://fifthelement.theelement/raw/childish_gambino_this_is_america", author62, album31, "Hiphop", 3, 1.5);
        arraylist10.add(song28);
        java.util.UUID uuid586 = java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        song29 = new fifthelement.theelement.objects.Song(uuid586, "Hall of Fame", "android.resource://fifthelement.theelement/raw/hall_of_fame", author61, album31, "", 3, 4.5);
        arraylist10.add(song29);
        java.util.UUID uuid587 = java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        song34 = new fifthelement.theelement.objects.Song(uuid587, "Classical Music", "android.resource://fifthelement.theelement/raw/classical_music", author61, album31, "Classical", 4, 2.0);
        arraylist10.add(song34);
        java.util.UUID uuid588 = java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid589 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author57 = new fifthelement.theelement.objects.Author(uuid589, "");
        java.util.UUID uuid590 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        album32 = new fifthelement.theelement.objects.Album(uuid590, "");
        song30 = new fifthelement.theelement.objects.Song(uuid588, "Adventure of a Lifetime", "android.resource://fifthelement.theelement/raw/coldplay_adventure_of_a_lifetime", author57, album32, "Pop", 10, 3.5);
        arraylist10.add(song30);
        arraylist10.iterator();
        song28.getAuthor();
        fifthelement.theelement.objects.Author author66 = song28.getAuthor();
        author66.getUUID();
        java.util.UUID uuid592 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author64 = new fifthelement.theelement.objects.Author(uuid592, "Childish Gambino", 3);
        song28.setAuthor(author64);
        song28.getAlbum();
        song29.getAuthor();
        song29.getAlbum();
        song34.getAuthor();
        song34.getAlbum();
        song30.getAuthor();
        fifthelement.theelement.objects.Author author70 = song30.getAuthor();
        author70.getUUID();
        java.util.UUID uuid594 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author56 = new fifthelement.theelement.objects.Author(uuid594, "Coldplay", 10);
        song30.setAuthor(author56);
        song30.getAlbum();
        fifthelement.theelement.objects.Album album38 = song30.getAlbum();
        album38.getUUID();
        java.util.UUID uuid596 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID uuid597 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author59 = new fifthelement.theelement.objects.Author(uuid597, "");
        album33 = new fifthelement.theelement.objects.Album(uuid596, "A Head Full of Dreams", author59, list4, 10);
        album33.getAuthor();
        fifthelement.theelement.objects.Author author72 = album33.getAuthor();
        author72.getUUID();
        java.util.UUID uuid599 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author60 = new fifthelement.theelement.objects.Author(uuid599, "Coldplay", 10);
        album33.setAuthor(author60);
        song30.setAlbum(album33);
        songlistservice3.sortSongs(arraylist10);
        arraylist10.size();
        arraylist10.size();
        songlistservice3.getAutoplayEnabled();
    }
}

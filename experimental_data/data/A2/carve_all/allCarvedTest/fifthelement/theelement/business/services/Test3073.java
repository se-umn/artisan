package fifthelement.theelement.business.services;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

public class Test3073 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.PlayMusicTests#skipSongsTest/Trace-1650046483856.txt
     * Method invocation under test: <fifthelement.theelement.business.services.SongListService: boolean getAutoplayEnabled()>_549_1095
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_business_services_SongListService_getAutoplayEnabled_001() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        fifthelement.theelement.objects.Song song93 = null;
        java.util.ArrayList arraylist30 = null;
        fifthelement.theelement.objects.Author author386 = null;
        fifthelement.theelement.objects.Song song94 = null;
        java.util.ArrayList arraylist31 = null;
        fifthelement.theelement.objects.Album album154 = null;
        fifthelement.theelement.objects.Author author387 = null;
        fifthelement.theelement.objects.Author author388 = null;
        fifthelement.theelement.objects.Song song95 = null;
        fifthelement.theelement.objects.Album album155 = null;
        fifthelement.theelement.business.services.SongListService songlistservice3 = null;
        fifthelement.theelement.objects.Author author389 = null;
        fifthelement.theelement.objects.Author author390 = null;
        fifthelement.theelement.objects.Author author391 = null;
        fifthelement.theelement.objects.Author author392 = null;
        fifthelement.theelement.objects.Author author393 = null;
        fifthelement.theelement.objects.Author author394 = null;
        fifthelement.theelement.objects.Song song96 = null;
        fifthelement.theelement.objects.Album album156 = null;
        fifthelement.theelement.objects.Song song97 = null;
        fifthelement.theelement.objects.Album album157 = null;
        fifthelement.theelement.objects.Song song98 = null;
        java.util.List list17 = null;
        fifthelement.theelement.objects.Song song99 = null;
        fifthelement.theelement.objects.Song song100 = null;
        songlistservice3 = new fifthelement.theelement.business.services.SongListService();
        arraylist30 = new java.util.ArrayList();
        java.util.UUID uuid5184 = java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid5185 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author393 = new fifthelement.theelement.objects.Author(uuid5185, "");
        song96 = new fifthelement.theelement.objects.Song(uuid5184, "This Is America", "android.resource://fifthelement.theelement/raw/childish_gambino_this_is_america", author393, album155, "Hiphop", 3, 1.5);
        arraylist30.add(song96);
        java.util.UUID uuid5186 = java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        song98 = new fifthelement.theelement.objects.Song(uuid5186, "Hall of Fame", "android.resource://fifthelement.theelement/raw/hall_of_fame", author391, album155, "", 3, 4.5);
        arraylist30.add(song98);
        java.util.UUID uuid5187 = java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        song97 = new fifthelement.theelement.objects.Song(uuid5187, "Classical Music", "android.resource://fifthelement.theelement/raw/classical_music", author391, album155, "Classical", 4, 2.0);
        arraylist30.add(song97);
        java.util.UUID uuid5188 = java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid5189 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author388 = new fifthelement.theelement.objects.Author(uuid5189, "");
        java.util.UUID uuid5190 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        album154 = new fifthelement.theelement.objects.Album(uuid5190, "");
        song100 = new fifthelement.theelement.objects.Song(uuid5188, "Adventure of a Lifetime", "android.resource://fifthelement.theelement/raw/coldplay_adventure_of_a_lifetime", author388, album154, "Pop", 10, 3.5);
        arraylist30.add(song100);
        arraylist30.iterator();
        java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        songlistservice3.setCurrentSongsList(arraylist30);
        arraylist31 = new java.util.ArrayList();
        java.util.UUID uuid5196 = java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid5197 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author392 = new fifthelement.theelement.objects.Author(uuid5197, "");
        song93 = new fifthelement.theelement.objects.Song(uuid5196, "This Is America", "android.resource://fifthelement.theelement/raw/childish_gambino_this_is_america", author392, album155, "Hiphop", 3, 1.5);
        arraylist31.add(song93);
        java.util.UUID uuid5198 = java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        song94 = new fifthelement.theelement.objects.Song(uuid5198, "Hall of Fame", "android.resource://fifthelement.theelement/raw/hall_of_fame", author391, album155, "", 3, 4.5);
        arraylist31.add(song94);
        java.util.UUID uuid5199 = java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        song99 = new fifthelement.theelement.objects.Song(uuid5199, "Classical Music", "android.resource://fifthelement.theelement/raw/classical_music", author391, album155, "Classical", 4, 2.0);
        arraylist31.add(song99);
        java.util.UUID uuid5200 = java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid5201 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author387 = new fifthelement.theelement.objects.Author(uuid5201, "");
        java.util.UUID uuid5202 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        album156 = new fifthelement.theelement.objects.Album(uuid5202, "");
        song95 = new fifthelement.theelement.objects.Song(uuid5200, "Adventure of a Lifetime", "android.resource://fifthelement.theelement/raw/coldplay_adventure_of_a_lifetime", author387, album156, "Pop", 10, 3.5);
        arraylist31.add(song95);
        arraylist31.iterator();
        song93.getAuthor();
        fifthelement.theelement.objects.Author author396 = song93.getAuthor();
        author396.getUUID();
        java.util.UUID uuid5204 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author394 = new fifthelement.theelement.objects.Author(uuid5204, "Childish Gambino", 3);
        song93.setAuthor(author394);
        song93.getAlbum();
        song94.getAuthor();
        song94.getAlbum();
        song99.getAuthor();
        song99.getAlbum();
        song95.getAuthor();
        fifthelement.theelement.objects.Author author400 = song95.getAuthor();
        author400.getUUID();
        java.util.UUID uuid5206 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author386 = new fifthelement.theelement.objects.Author(uuid5206, "Coldplay", 10);
        song95.setAuthor(author386);
        song95.getAlbum();
        fifthelement.theelement.objects.Album album162 = song95.getAlbum();
        album162.getUUID();
        java.util.UUID uuid5208 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID uuid5209 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author389 = new fifthelement.theelement.objects.Author(uuid5209, "");
        album157 = new fifthelement.theelement.objects.Album(uuid5208, "A Head Full of Dreams", author389, list17, 10);
        album157.getAuthor();
        fifthelement.theelement.objects.Author author402 = album157.getAuthor();
        author402.getUUID();
        java.util.UUID uuid5211 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author390 = new fifthelement.theelement.objects.Author(uuid5211, "Coldplay", 10);
        album157.setAuthor(author390);
        song95.setAlbum(album157);
        songlistservice3.sortSongs(arraylist31);
        arraylist31.size();
        arraylist31.size();
        songlistservice3.getAutoplayEnabled();
    }
}

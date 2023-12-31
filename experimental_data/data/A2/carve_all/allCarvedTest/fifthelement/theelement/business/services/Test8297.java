package fifthelement.theelement.business.services;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

public class Test8297 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.SettingsTest#deleteSongsTest/Trace-1650046437980.txt
     * Method invocation under test: <fifthelement.theelement.business.services.SongListService: void setCurrentSongsList(java.util.List)>_3003_6005
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_business_services_SongListService_setCurrentSongsList_002() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        fifthelement.theelement.objects.Author author8899 = null;
        java.util.ArrayList arraylist3418 = null;
        fifthelement.theelement.objects.Song song8302 = null;
        fifthelement.theelement.objects.Album album3387 = null;
        fifthelement.theelement.objects.Author author8900 = null;
        fifthelement.theelement.objects.Song song8303 = null;
        fifthelement.theelement.objects.Author author8901 = null;
        fifthelement.theelement.objects.Album album3388 = null;
        fifthelement.theelement.objects.Author author8902 = null;
        fifthelement.theelement.objects.Song song8304 = null;
        fifthelement.theelement.objects.Song song8305 = null;
        fifthelement.theelement.objects.Song song8306 = null;
        java.util.ArrayList arraylist3419 = null;
        fifthelement.theelement.objects.Song song8307 = null;
        fifthelement.theelement.objects.Album album3389 = null;
        fifthelement.theelement.objects.Author author8903 = null;
        fifthelement.theelement.objects.Author author8904 = null;
        java.util.ArrayList arraylist3420 = null;
        fifthelement.theelement.objects.Author author8905 = null;
        fifthelement.theelement.objects.Author author8906 = null;
        fifthelement.theelement.objects.Song song8308 = null;
        fifthelement.theelement.objects.Author author8907 = null;
        fifthelement.theelement.objects.Author author8908 = null;
        java.util.ArrayList arraylist3421 = null;
        fifthelement.theelement.objects.Author author8909 = null;
        fifthelement.theelement.objects.Author author8910 = null;
        fifthelement.theelement.objects.Author author8911 = null;
        java.util.ArrayList arraylist3422 = null;
        fifthelement.theelement.objects.Author author8912 = null;
        fifthelement.theelement.objects.Album album3390 = null;
        fifthelement.theelement.objects.Author author8913 = null;
        fifthelement.theelement.objects.Song song8309 = null;
        fifthelement.theelement.objects.Song song8310 = null;
        fifthelement.theelement.objects.Song song8311 = null;
        fifthelement.theelement.objects.Author author8914 = null;
        fifthelement.theelement.objects.Song song8312 = null;
        fifthelement.theelement.objects.Album album3391 = null;
        fifthelement.theelement.objects.Song song8313 = null;
        fifthelement.theelement.objects.Song song8314 = null;
        java.util.ArrayList arraylist3423 = null;
        fifthelement.theelement.objects.Author author8915 = null;
        fifthelement.theelement.objects.Author author8916 = null;
        fifthelement.theelement.objects.Author author8917 = null;
        fifthelement.theelement.objects.Song song8315 = null;
        fifthelement.theelement.objects.Author author8918 = null;
        fifthelement.theelement.objects.Song song8316 = null;
        fifthelement.theelement.objects.Author author8919 = null;
        fifthelement.theelement.objects.Album album3392 = null;
        fifthelement.theelement.objects.Author author8920 = null;
        fifthelement.theelement.objects.Song song8317 = null;
        fifthelement.theelement.business.services.SongListService songlistservice9 = null;
        fifthelement.theelement.objects.Album album3393 = null;
        fifthelement.theelement.objects.Author author8921 = null;
        fifthelement.theelement.objects.Song song8318 = null;
        fifthelement.theelement.objects.Song song8319 = null;
        java.util.List list149 = null;
        songlistservice9 = new fifthelement.theelement.business.services.SongListService();
        arraylist3419 = new java.util.ArrayList();
        java.util.UUID uuid149563 = java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid149564 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author8902 = new fifthelement.theelement.objects.Author(uuid149564, "");
        song8304 = new fifthelement.theelement.objects.Song(uuid149563, "This Is America", "android.resource://fifthelement.theelement/raw/childish_gambino_this_is_america", author8902, album3393, "Hiphop", 3, 1.5);
        arraylist3419.add(song8304);
        java.util.UUID uuid149565 = java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        song8305 = new fifthelement.theelement.objects.Song(uuid149565, "Hall of Fame", "android.resource://fifthelement.theelement/raw/hall_of_fame", author8909, album3393, "", 3, 4.5);
        arraylist3419.add(song8305);
        java.util.UUID uuid149566 = java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        song8308 = new fifthelement.theelement.objects.Song(uuid149566, "Classical Music", "android.resource://fifthelement.theelement/raw/classical_music", author8909, album3393, "Classical", 4, 2.0);
        arraylist3419.add(song8308);
        java.util.UUID uuid149567 = java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid149568 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author8901 = new fifthelement.theelement.objects.Author(uuid149568, "");
        java.util.UUID uuid149569 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        album3391 = new fifthelement.theelement.objects.Album(uuid149569, "");
        song8303 = new fifthelement.theelement.objects.Song(uuid149567, "Adventure of a Lifetime", "android.resource://fifthelement.theelement/raw/coldplay_adventure_of_a_lifetime", author8901, album3391, "Pop", 10, 3.5);
        arraylist3419.add(song8303);
        arraylist3419.iterator();
        song8304.getAuthor();
        fifthelement.theelement.objects.Author author8923 = song8304.getAuthor();
        author8923.getUUID();
        java.util.UUID uuid149571 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author8911 = new fifthelement.theelement.objects.Author(uuid149571, "Childish Gambino", 3);
        song8304.setAuthor(author8911);
        song8304.getAlbum();
        song8305.getAuthor();
        song8305.getAlbum();
        fifthelement.theelement.objects.Author author8925 = song8308.getAuthor();
        fifthelement.theelement.objects.Album album3396 = song8308.getAlbum();
        song8303.getAuthor();
        fifthelement.theelement.objects.Author author8927 = song8303.getAuthor();
        author8927.getUUID();
        java.util.UUID uuid149573 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author8903 = new fifthelement.theelement.objects.Author(uuid149573, "Coldplay", 10);
        song8303.setAuthor(author8903);
        song8303.getAlbum();
        fifthelement.theelement.objects.Album album3398 = song8303.getAlbum();
        album3398.getUUID();
        java.util.UUID uuid149575 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID uuid149576 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author8900 = new fifthelement.theelement.objects.Author(uuid149576, "");
        album3392 = new fifthelement.theelement.objects.Album(uuid149575, "A Head Full of Dreams", author8900, list149, 10);
        album3392.getAuthor();
        fifthelement.theelement.objects.Author author8929 = album3392.getAuthor();
        author8929.getUUID();
        java.util.UUID uuid149578 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author8915 = new fifthelement.theelement.objects.Author(uuid149578, "Coldplay", 10);
        album3392.setAuthor(author8915);
        song8303.setAlbum(album3392);
        songlistservice9.setCurrentSongsList(arraylist3419);
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
        arraylist3423 = new java.util.ArrayList();
        java.util.UUID uuid149638 = java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid149639 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author8913 = new fifthelement.theelement.objects.Author(uuid149639, "");
        song8313 = new fifthelement.theelement.objects.Song(uuid149638, "This Is America", "android.resource://fifthelement.theelement/raw/childish_gambino_this_is_america", author8913, album3396, "Hiphop", 3, 1.5);
        arraylist3423.add(song8313);
        java.util.UUID uuid149640 = java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        song8302 = new fifthelement.theelement.objects.Song(uuid149640, "Hall of Fame", "android.resource://fifthelement.theelement/raw/hall_of_fame", author8925, album3396, "", 3, 4.5);
        arraylist3423.add(song8302);
        java.util.UUID uuid149641 = java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        song8316 = new fifthelement.theelement.objects.Song(uuid149641, "Classical Music", "android.resource://fifthelement.theelement/raw/classical_music", author8925, album3396, "Classical", 4, 2.0);
        arraylist3423.add(song8316);
        java.util.UUID uuid149642 = java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid149643 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author8899 = new fifthelement.theelement.objects.Author(uuid149643, "");
        java.util.UUID uuid149644 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        album3389 = new fifthelement.theelement.objects.Album(uuid149644, "");
        song8318 = new fifthelement.theelement.objects.Song(uuid149642, "Adventure of a Lifetime", "android.resource://fifthelement.theelement/raw/coldplay_adventure_of_a_lifetime", author8899, album3389, "Pop", 10, 3.5);
        arraylist3423.add(song8318);
        arraylist3423.iterator();
        song8313.getAuthor();
        fifthelement.theelement.objects.Author author8931 = song8313.getAuthor();
        author8931.getUUID();
        java.util.UUID uuid149646 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author8920 = new fifthelement.theelement.objects.Author(uuid149646, "Childish Gambino", 3);
        song8313.setAuthor(author8920);
        song8313.getAlbum();
        song8302.getAuthor();
        song8302.getAlbum();
        fifthelement.theelement.objects.Author author8933 = song8316.getAuthor();
        fifthelement.theelement.objects.Album album3401 = song8316.getAlbum();
        song8318.getAuthor();
        fifthelement.theelement.objects.Author author8935 = song8318.getAuthor();
        author8935.getUUID();
        java.util.UUID uuid149648 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author8906 = new fifthelement.theelement.objects.Author(uuid149648, "Coldplay", 10);
        song8318.setAuthor(author8906);
        song8318.getAlbum();
        fifthelement.theelement.objects.Album album3403 = song8318.getAlbum();
        album3403.getUUID();
        java.util.UUID uuid149650 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID uuid149651 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author8904 = new fifthelement.theelement.objects.Author(uuid149651, "");
        album3387 = new fifthelement.theelement.objects.Album(uuid149650, "A Head Full of Dreams", author8904, list149, 10);
        album3387.getAuthor();
        fifthelement.theelement.objects.Author author8937 = album3387.getAuthor();
        author8937.getUUID();
        java.util.UUID uuid149653 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author8918 = new fifthelement.theelement.objects.Author(uuid149653, "Coldplay", 10);
        album3387.setAuthor(author8918);
        song8318.setAlbum(album3387);
        songlistservice9.sortSongs(arraylist3423);
        arraylist3423.size();
        arraylist3423.size();
        songlistservice9.getAutoplayEnabled();
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
        arraylist3421 = new java.util.ArrayList();
        java.util.UUID uuid149667 = java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid149668 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author8917 = new fifthelement.theelement.objects.Author(uuid149668, "");
        song8319 = new fifthelement.theelement.objects.Song(uuid149667, "This Is America", "android.resource://fifthelement.theelement/raw/childish_gambino_this_is_america", author8917, album3401, "Hiphop", 3, 1.5);
        arraylist3421.add(song8319);
        java.util.UUID uuid149669 = java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        song8311 = new fifthelement.theelement.objects.Song(uuid149669, "Hall of Fame", "android.resource://fifthelement.theelement/raw/hall_of_fame", author8933, album3401, "", 3, 4.5);
        arraylist3421.add(song8311);
        java.util.UUID uuid149670 = java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        song8309 = new fifthelement.theelement.objects.Song(uuid149670, "Classical Music", "android.resource://fifthelement.theelement/raw/classical_music", author8933, album3401, "Classical", 4, 2.0);
        arraylist3421.add(song8309);
        java.util.UUID uuid149671 = java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid149672 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author8905 = new fifthelement.theelement.objects.Author(uuid149672, "");
        java.util.UUID uuid149673 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        album3388 = new fifthelement.theelement.objects.Album(uuid149673, "");
        song8314 = new fifthelement.theelement.objects.Song(uuid149671, "Adventure of a Lifetime", "android.resource://fifthelement.theelement/raw/coldplay_adventure_of_a_lifetime", author8905, album3388, "Pop", 10, 3.5);
        arraylist3421.add(song8314);
        arraylist3421.iterator();
        song8319.getAuthor();
        fifthelement.theelement.objects.Author author8939 = song8319.getAuthor();
        author8939.getUUID();
        java.util.UUID uuid149675 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author8921 = new fifthelement.theelement.objects.Author(uuid149675, "Childish Gambino", 3);
        song8319.setAuthor(author8921);
        song8319.getAlbum();
        song8311.getAuthor();
        song8311.getAlbum();
        song8309.getAuthor();
        fifthelement.theelement.objects.Album album3406 = song8309.getAlbum();
        song8314.getAuthor();
        fifthelement.theelement.objects.Author author8943 = song8314.getAuthor();
        author8943.getUUID();
        java.util.UUID uuid149677 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author8914 = new fifthelement.theelement.objects.Author(uuid149677, "Coldplay", 10);
        song8314.setAuthor(author8914);
        song8314.getAlbum();
        fifthelement.theelement.objects.Album album3408 = song8314.getAlbum();
        album3408.getUUID();
        java.util.UUID uuid149679 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID uuid149680 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author8919 = new fifthelement.theelement.objects.Author(uuid149680, "");
        album3390 = new fifthelement.theelement.objects.Album(uuid149679, "A Head Full of Dreams", author8919, list149, 10);
        album3390.getAuthor();
        fifthelement.theelement.objects.Author author8945 = album3390.getAuthor();
        author8945.getUUID();
        java.util.UUID uuid149682 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author8910 = new fifthelement.theelement.objects.Author(uuid149682, "Coldplay", 10);
        album3390.setAuthor(author8910);
        song8314.setAlbum(album3390);
        songlistservice9.sortSongs(arraylist3421);
        arraylist3421.size();
        arraylist3421.size();
        arraylist3421.size();
        arraylist3421.size();
        fifthelement.theelement.objects.Song song8320 = (fifthelement.theelement.objects.Song) arraylist3421.get(0);
        fifthelement.theelement.objects.Author author8946 = song8320.getAuthor();
        author8946.getName();
        song8320.getName();
        fifthelement.theelement.objects.Song song8321 = (fifthelement.theelement.objects.Song) arraylist3421.get(1);
        song8321.getAuthor();
        song8321.getName();
        fifthelement.theelement.objects.Song song8322 = (fifthelement.theelement.objects.Song) arraylist3421.get(2);
        song8322.getAuthor();
        song8322.getName();
        fifthelement.theelement.objects.Song song8323 = (fifthelement.theelement.objects.Song) arraylist3421.get(3);
        fifthelement.theelement.objects.Author author8949 = song8323.getAuthor();
        author8949.getName();
        song8323.getName();
        arraylist3421.size();
        arraylist3421.size();
        fifthelement.theelement.objects.Song song8324 = (fifthelement.theelement.objects.Song) arraylist3421.get(0);
        fifthelement.theelement.objects.Author author8950 = song8324.getAuthor();
        author8950.getName();
        song8324.getName();
        fifthelement.theelement.objects.Song song8325 = (fifthelement.theelement.objects.Song) arraylist3421.get(1);
        song8325.getAuthor();
        song8325.getName();
        fifthelement.theelement.objects.Song song8326 = (fifthelement.theelement.objects.Song) arraylist3421.get(2);
        song8326.getAuthor();
        song8326.getName();
        fifthelement.theelement.objects.Song song8327 = (fifthelement.theelement.objects.Song) arraylist3421.get(3);
        fifthelement.theelement.objects.Author author8953 = song8327.getAuthor();
        author8953.getName();
        song8327.getName();
        arraylist3421.size();
        fifthelement.theelement.objects.Song song8328 = (fifthelement.theelement.objects.Song) arraylist3421.get(0);
        fifthelement.theelement.objects.Author author8954 = song8328.getAuthor();
        author8954.getName();
        song8328.getName();
        fifthelement.theelement.objects.Song song8329 = (fifthelement.theelement.objects.Song) arraylist3421.get(1);
        song8329.getAuthor();
        song8329.getName();
        fifthelement.theelement.objects.Song song8330 = (fifthelement.theelement.objects.Song) arraylist3421.get(2);
        song8330.getAuthor();
        song8330.getName();
        fifthelement.theelement.objects.Song song8331 = (fifthelement.theelement.objects.Song) arraylist3421.get(3);
        fifthelement.theelement.objects.Author author8957 = song8331.getAuthor();
        author8957.getName();
        song8331.getName();
        arraylist3421.size();
        arraylist3421.size();
        fifthelement.theelement.objects.Song song8332 = (fifthelement.theelement.objects.Song) arraylist3421.get(0);
        fifthelement.theelement.objects.Author author8958 = song8332.getAuthor();
        author8958.getName();
        song8332.getName();
        fifthelement.theelement.objects.Song song8333 = (fifthelement.theelement.objects.Song) arraylist3421.get(1);
        song8333.getAuthor();
        song8333.getName();
        fifthelement.theelement.objects.Song song8334 = (fifthelement.theelement.objects.Song) arraylist3421.get(2);
        song8334.getAuthor();
        song8334.getName();
        fifthelement.theelement.objects.Song song8335 = (fifthelement.theelement.objects.Song) arraylist3421.get(3);
        fifthelement.theelement.objects.Author author8961 = song8335.getAuthor();
        author8961.getName();
        song8335.getName();
        arraylist3421.size();
        arraylist3421.size();
        fifthelement.theelement.objects.Song song8336 = (fifthelement.theelement.objects.Song) arraylist3421.get(0);
        fifthelement.theelement.objects.Author author8962 = song8336.getAuthor();
        author8962.getName();
        song8336.getName();
        fifthelement.theelement.objects.Song song8337 = (fifthelement.theelement.objects.Song) arraylist3421.get(1);
        song8337.getAuthor();
        song8337.getName();
        fifthelement.theelement.objects.Song song8338 = (fifthelement.theelement.objects.Song) arraylist3421.get(2);
        song8338.getAuthor();
        song8338.getName();
        fifthelement.theelement.objects.Song song8339 = (fifthelement.theelement.objects.Song) arraylist3421.get(3);
        fifthelement.theelement.objects.Author author8965 = song8339.getAuthor();
        author8965.getName();
        song8339.getName();
        arraylist3421.size();
        song8336.getName();
        java.util.UUID uuid149683 = song8336.getUUID();
        uuid149683.toString();
        java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.randomUUID();
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        arraylist3421.remove(song8336);
        songlistservice9.removeSongFromList(song8336);
        arraylist3421.size();
        arraylist3421.size();
        arraylist3421.get(0);
        arraylist3421.size();
        arraylist3421.size();
        arraylist3421.size();
        fifthelement.theelement.objects.Song song8341 = (fifthelement.theelement.objects.Song) arraylist3421.get(0);
        song8341.getAuthor();
        song8341.getName();
        fifthelement.theelement.objects.Song song8342 = (fifthelement.theelement.objects.Song) arraylist3421.get(1);
        song8342.getAuthor();
        song8342.getName();
        fifthelement.theelement.objects.Song song8343 = (fifthelement.theelement.objects.Song) arraylist3421.get(2);
        fifthelement.theelement.objects.Author author8968 = song8343.getAuthor();
        author8968.getName();
        song8343.getName();
        arraylist3421.size();
        arraylist3421.size();
        fifthelement.theelement.objects.Song song8344 = (fifthelement.theelement.objects.Song) arraylist3421.get(0);
        song8344.getAuthor();
        song8344.getName();
        fifthelement.theelement.objects.Song song8345 = (fifthelement.theelement.objects.Song) arraylist3421.get(1);
        song8345.getAuthor();
        song8345.getName();
        fifthelement.theelement.objects.Song song8346 = (fifthelement.theelement.objects.Song) arraylist3421.get(2);
        fifthelement.theelement.objects.Author author8971 = song8346.getAuthor();
        author8971.getName();
        song8346.getName();
        arraylist3421.size();
        arraylist3421.get(0);
        arraylist3421.size();
        fifthelement.theelement.objects.Song song8348 = (fifthelement.theelement.objects.Song) arraylist3421.get(0);
        song8348.getAuthor();
        song8348.getName();
        fifthelement.theelement.objects.Song song8349 = (fifthelement.theelement.objects.Song) arraylist3421.get(1);
        fifthelement.theelement.objects.Author author8973 = song8349.getAuthor();
        song8349.getName();
        arraylist3421.get(2);
        java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        arraylist3418 = new java.util.ArrayList();
        java.util.UUID uuid149710 = java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid149711 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author8907 = new fifthelement.theelement.objects.Author(uuid149711, "");
        song8307 = new fifthelement.theelement.objects.Song(uuid149710, "This Is America", "android.resource://fifthelement.theelement/raw/childish_gambino_this_is_america", author8907, album3406, "Hiphop", 3, 1.5);
        arraylist3418.add(song8307);
        java.util.UUID uuid149712 = java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        song8317 = new fifthelement.theelement.objects.Song(uuid149712, "Hall of Fame", "android.resource://fifthelement.theelement/raw/hall_of_fame", author8973, album3406, "", 3, 4.5);
        arraylist3418.add(song8317);
        java.util.UUID uuid149713 = java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        song8315 = new fifthelement.theelement.objects.Song(uuid149713, "Classical Music", "android.resource://fifthelement.theelement/raw/classical_music", author8973, album3406, "Classical", 4, 2.0);
        arraylist3418.add(song8315);
        arraylist3418.iterator();
        song8307.getAuthor();
        fifthelement.theelement.objects.Author author8975 = song8307.getAuthor();
        author8975.getUUID();
        java.util.UUID uuid149715 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author8908 = new fifthelement.theelement.objects.Author(uuid149715, "Childish Gambino", 3);
        song8307.setAuthor(author8908);
        song8307.getAlbum();
        song8317.getAuthor();
        song8317.getAlbum();
        fifthelement.theelement.objects.Author author8977 = song8315.getAuthor();
        fifthelement.theelement.objects.Album album3411 = song8315.getAlbum();
        songlistservice9.sortSongs(arraylist3418);
        arraylist3418.size();
        arraylist3418.size();
        songlistservice9.getAutoplayEnabled();
        java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        arraylist3422 = new java.util.ArrayList();
        java.util.UUID uuid149721 = java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid149722 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author8916 = new fifthelement.theelement.objects.Author(uuid149722, "");
        song8312 = new fifthelement.theelement.objects.Song(uuid149721, "This Is America", "android.resource://fifthelement.theelement/raw/childish_gambino_this_is_america", author8916, album3411, "Hiphop", 3, 1.5);
        arraylist3422.add(song8312);
        java.util.UUID uuid149723 = java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        song8310 = new fifthelement.theelement.objects.Song(uuid149723, "Hall of Fame", "android.resource://fifthelement.theelement/raw/hall_of_fame", author8977, album3411, "", 3, 4.5);
        arraylist3422.add(song8310);
        java.util.UUID uuid149724 = java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        song8306 = new fifthelement.theelement.objects.Song(uuid149724, "Classical Music", "android.resource://fifthelement.theelement/raw/classical_music", author8977, album3411, "Classical", 4, 2.0);
        arraylist3422.add(song8306);
        arraylist3422.iterator();
        song8312.getAuthor();
        fifthelement.theelement.objects.Author author8979 = song8312.getAuthor();
        author8979.getUUID();
        java.util.UUID uuid149726 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author8912 = new fifthelement.theelement.objects.Author(uuid149726, "Childish Gambino", 3);
        song8312.setAuthor(author8912);
        song8312.getAlbum();
        song8310.getAuthor();
        song8310.getAlbum();
        song8306.getAuthor();
        song8306.getAlbum();
        songlistservice9.sortSongs(arraylist3422);
        arraylist3422.size();
        arraylist3422.size();
        arraylist3422.size();
        arraylist3422.size();
        fifthelement.theelement.objects.Song song8351 = (fifthelement.theelement.objects.Song) arraylist3422.get(0);
        song8351.getAuthor();
        song8351.getName();
        fifthelement.theelement.objects.Song song8352 = (fifthelement.theelement.objects.Song) arraylist3422.get(1);
        song8352.getAuthor();
        song8352.getName();
        fifthelement.theelement.objects.Song song8353 = (fifthelement.theelement.objects.Song) arraylist3422.get(2);
        fifthelement.theelement.objects.Author author8984 = song8353.getAuthor();
        author8984.getName();
        song8353.getName();
        arraylist3422.size();
        arraylist3422.size();
        fifthelement.theelement.objects.Song song8354 = (fifthelement.theelement.objects.Song) arraylist3422.get(0);
        song8354.getAuthor();
        song8354.getName();
        fifthelement.theelement.objects.Song song8355 = (fifthelement.theelement.objects.Song) arraylist3422.get(1);
        song8355.getAuthor();
        song8355.getName();
        fifthelement.theelement.objects.Song song8356 = (fifthelement.theelement.objects.Song) arraylist3422.get(2);
        fifthelement.theelement.objects.Author author8987 = song8356.getAuthor();
        author8987.getName();
        song8356.getName();
        arraylist3422.size();
        fifthelement.theelement.objects.Song song8357 = (fifthelement.theelement.objects.Song) arraylist3422.get(0);
        song8357.getAuthor();
        song8357.getName();
        fifthelement.theelement.objects.Song song8358 = (fifthelement.theelement.objects.Song) arraylist3422.get(1);
        song8358.getAuthor();
        song8358.getName();
        fifthelement.theelement.objects.Song song8359 = (fifthelement.theelement.objects.Song) arraylist3422.get(2);
        fifthelement.theelement.objects.Author author8990 = song8359.getAuthor();
        author8990.getName();
        song8359.getName();
        arraylist3422.size();
        arraylist3422.size();
        fifthelement.theelement.objects.Song song8360 = (fifthelement.theelement.objects.Song) arraylist3422.get(0);
        song8360.getAuthor();
        song8360.getName();
        fifthelement.theelement.objects.Song song8361 = (fifthelement.theelement.objects.Song) arraylist3422.get(1);
        song8361.getAuthor();
        song8361.getName();
        fifthelement.theelement.objects.Song song8362 = (fifthelement.theelement.objects.Song) arraylist3422.get(2);
        fifthelement.theelement.objects.Author author8993 = song8362.getAuthor();
        author8993.getName();
        song8362.getName();
        arraylist3422.size();
        arraylist3422.size();
        fifthelement.theelement.objects.Song song8363 = (fifthelement.theelement.objects.Song) arraylist3422.get(0);
        song8363.getAuthor();
        song8363.getName();
        fifthelement.theelement.objects.Song song8364 = (fifthelement.theelement.objects.Song) arraylist3422.get(1);
        song8364.getAuthor();
        song8364.getName();
        fifthelement.theelement.objects.Song song8365 = (fifthelement.theelement.objects.Song) arraylist3422.get(2);
        fifthelement.theelement.objects.Author author8996 = song8365.getAuthor();
        author8996.getName();
        song8365.getName();
        arraylist3422.size();
        arraylist3420 = new java.util.ArrayList();
        arraylist3420.iterator();
        songlistservice9.setCurrentSongsList(arraylist3420);
    }
}

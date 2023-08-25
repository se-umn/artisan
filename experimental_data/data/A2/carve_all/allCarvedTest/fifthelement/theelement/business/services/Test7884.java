package fifthelement.theelement.business.services;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

public class Test7884 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.SettingsTest#deleteSongsTest/Trace-1650046437980.txt
     * Method invocation under test: <fifthelement.theelement.business.services.SongListService: void sortSongs(java.util.List)>_1471_2940
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_business_services_SongListService_sortSongs_002() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        fifthelement.theelement.objects.Author author1678 = null;
        fifthelement.theelement.objects.Song song232 = null;
        fifthelement.theelement.objects.Author author1679 = null;
        fifthelement.theelement.objects.Song song233 = null;
        fifthelement.theelement.objects.Album album542 = null;
        fifthelement.theelement.objects.Song song234 = null;
        fifthelement.theelement.objects.Song song235 = null;
        fifthelement.theelement.objects.Album album543 = null;
        fifthelement.theelement.objects.Song song236 = null;
        fifthelement.theelement.objects.Author author1680 = null;
        java.util.ArrayList arraylist414 = null;
        fifthelement.theelement.objects.Song song237 = null;
        fifthelement.theelement.objects.Album album544 = null;
        fifthelement.theelement.objects.Author author1681 = null;
        fifthelement.theelement.objects.Song song238 = null;
        fifthelement.theelement.objects.Song song239 = null;
        java.util.ArrayList arraylist415 = null;
        fifthelement.theelement.objects.Author author1682 = null;
        fifthelement.theelement.objects.Author author1683 = null;
        fifthelement.theelement.objects.Song song240 = null;
        fifthelement.theelement.objects.Author author1684 = null;
        fifthelement.theelement.objects.Album album545 = null;
        fifthelement.theelement.objects.Author author1685 = null;
        fifthelement.theelement.objects.Author author1686 = null;
        fifthelement.theelement.business.services.SongListService songlistservice4 = null;
        fifthelement.theelement.objects.Author author1687 = null;
        fifthelement.theelement.objects.Album album546 = null;
        fifthelement.theelement.objects.Song song241 = null;
        java.util.ArrayList arraylist416 = null;
        fifthelement.theelement.objects.Author author1688 = null;
        fifthelement.theelement.objects.Song song242 = null;
        fifthelement.theelement.objects.Song song243 = null;
        java.util.List list78 = null;
        songlistservice4 = new fifthelement.theelement.business.services.SongListService();
        arraylist415 = new java.util.ArrayList();
        java.util.UUID uuid48127 = java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid48128 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author1681 = new fifthelement.theelement.objects.Author(uuid48128, "");
        song238 = new fifthelement.theelement.objects.Song(uuid48127, "This Is America", "android.resource://fifthelement.theelement/raw/childish_gambino_this_is_america", author1681, album546, "Hiphop", 3, 1.5);
        arraylist415.add(song238);
        java.util.UUID uuid48129 = java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        song239 = new fifthelement.theelement.objects.Song(uuid48129, "Hall of Fame", "android.resource://fifthelement.theelement/raw/hall_of_fame", author1688, album546, "", 3, 4.5);
        arraylist415.add(song239);
        java.util.UUID uuid48130 = java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        song241 = new fifthelement.theelement.objects.Song(uuid48130, "Classical Music", "android.resource://fifthelement.theelement/raw/classical_music", author1688, album546, "Classical", 4, 2.0);
        arraylist415.add(song241);
        java.util.UUID uuid48131 = java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid48132 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author1680 = new fifthelement.theelement.objects.Author(uuid48132, "");
        java.util.UUID uuid48133 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        album543 = new fifthelement.theelement.objects.Album(uuid48133, "");
        song235 = new fifthelement.theelement.objects.Song(uuid48131, "Adventure of a Lifetime", "android.resource://fifthelement.theelement/raw/coldplay_adventure_of_a_lifetime", author1680, album543, "Pop", 10, 3.5);
        arraylist415.add(song235);
        arraylist415.iterator();
        java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        songlistservice4.setCurrentSongsList(arraylist415);
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
        arraylist414 = new java.util.ArrayList();
        java.util.UUID uuid48198 = java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid48199 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author1679 = new fifthelement.theelement.objects.Author(uuid48199, "");
        song236 = new fifthelement.theelement.objects.Song(uuid48198, "This Is America", "android.resource://fifthelement.theelement/raw/childish_gambino_this_is_america", author1679, album546, "Hiphop", 3, 1.5);
        arraylist414.add(song236);
        java.util.UUID uuid48200 = java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        song232 = new fifthelement.theelement.objects.Song(uuid48200, "Hall of Fame", "android.resource://fifthelement.theelement/raw/hall_of_fame", author1688, album546, "", 3, 4.5);
        arraylist414.add(song232);
        java.util.UUID uuid48201 = java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        song240 = new fifthelement.theelement.objects.Song(uuid48201, "Classical Music", "android.resource://fifthelement.theelement/raw/classical_music", author1688, album546, "Classical", 4, 2.0);
        arraylist414.add(song240);
        java.util.UUID uuid48202 = java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid48203 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author1678 = new fifthelement.theelement.objects.Author(uuid48203, "");
        java.util.UUID uuid48204 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        album545 = new fifthelement.theelement.objects.Album(uuid48204, "");
        song242 = new fifthelement.theelement.objects.Song(uuid48202, "Adventure of a Lifetime", "android.resource://fifthelement.theelement/raw/coldplay_adventure_of_a_lifetime", author1678, album545, "Pop", 10, 3.5);
        arraylist414.add(song242);
        arraylist414.iterator();
        song236.getAuthor();
        fifthelement.theelement.objects.Author author1690 = song236.getAuthor();
        author1690.getUUID();
        java.util.UUID uuid48206 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author1684 = new fifthelement.theelement.objects.Author(uuid48206, "Childish Gambino", 3);
        song236.setAuthor(author1684);
        song236.getAlbum();
        song232.getAuthor();
        song232.getAlbum();
        fifthelement.theelement.objects.Author author1692 = song240.getAuthor();
        fifthelement.theelement.objects.Album album549 = song240.getAlbum();
        song242.getAuthor();
        fifthelement.theelement.objects.Author author1694 = song242.getAuthor();
        author1694.getUUID();
        java.util.UUID uuid48208 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author1687 = new fifthelement.theelement.objects.Author(uuid48208, "Coldplay", 10);
        song242.setAuthor(author1687);
        song242.getAlbum();
        fifthelement.theelement.objects.Album album551 = song242.getAlbum();
        album551.getUUID();
        java.util.UUID uuid48210 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID uuid48211 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author1685 = new fifthelement.theelement.objects.Author(uuid48211, "");
        album542 = new fifthelement.theelement.objects.Album(uuid48210, "A Head Full of Dreams", author1685, list78, 10);
        album542.getAuthor();
        fifthelement.theelement.objects.Author author1696 = album542.getAuthor();
        author1696.getUUID();
        java.util.UUID uuid48213 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author1683 = new fifthelement.theelement.objects.Author(uuid48213, "Coldplay", 10);
        album542.setAuthor(author1683);
        song242.setAlbum(album542);
        songlistservice4.sortSongs(arraylist414);
        arraylist414.size();
        arraylist414.size();
        songlistservice4.getAutoplayEnabled();
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
        arraylist416 = new java.util.ArrayList();
        java.util.UUID uuid48227 = java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid48228 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author1682 = new fifthelement.theelement.objects.Author(uuid48228, "");
        song243 = new fifthelement.theelement.objects.Song(uuid48227, "This Is America", "android.resource://fifthelement.theelement/raw/childish_gambino_this_is_america", author1682, album549, "Hiphop", 3, 1.5);
        arraylist416.add(song243);
        java.util.UUID uuid48229 = java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        song234 = new fifthelement.theelement.objects.Song(uuid48229, "Hall of Fame", "android.resource://fifthelement.theelement/raw/hall_of_fame", author1692, album549, "", 3, 4.5);
        arraylist416.add(song234);
        java.util.UUID uuid48230 = java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        song233 = new fifthelement.theelement.objects.Song(uuid48230, "Classical Music", "android.resource://fifthelement.theelement/raw/classical_music", author1692, album549, "Classical", 4, 2.0);
        arraylist416.add(song233);
        java.util.UUID uuid48231 = java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid48232 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author1686 = new fifthelement.theelement.objects.Author(uuid48232, "");
        java.util.UUID uuid48233 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        album544 = new fifthelement.theelement.objects.Album(uuid48233, "");
        song237 = new fifthelement.theelement.objects.Song(uuid48231, "Adventure of a Lifetime", "android.resource://fifthelement.theelement/raw/coldplay_adventure_of_a_lifetime", author1686, album544, "Pop", 10, 3.5);
        arraylist416.add(song237);
        arraylist416.iterator();
        songlistservice4.sortSongs(arraylist416);
    }
}

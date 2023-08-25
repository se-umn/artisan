package fifthelement.theelement.business.services;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

public class Test6077 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.SettingsTest#playSongWithSongPlayedCheck/Trace-1650046467562.txt
     * Method invocation under test: <fifthelement.theelement.business.services.SongListService: void setCurrentSongsList(java.util.List)>_2677_5353
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_business_services_SongListService_setCurrentSongsList_003() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        fifthelement.theelement.objects.Author author11793 = null;
        fifthelement.theelement.objects.Author author11794 = null;
        fifthelement.theelement.objects.Song song10946 = null;
        fifthelement.theelement.objects.Song song10947 = null;
        fifthelement.theelement.objects.Song song10948 = null;
        fifthelement.theelement.objects.Song song10949 = null;
        java.util.ArrayList arraylist3759 = null;
        fifthelement.theelement.objects.Album album5340 = null;
        fifthelement.theelement.objects.Author author11795 = null;
        fifthelement.theelement.objects.Author author11796 = null;
        fifthelement.theelement.objects.Author author11797 = null;
        fifthelement.theelement.objects.Author author11798 = null;
        fifthelement.theelement.objects.Song song10950 = null;
        java.util.ArrayList arraylist3760 = null;
        fifthelement.theelement.objects.Song song10951 = null;
        java.util.ArrayList arraylist3761 = null;
        fifthelement.theelement.objects.Album album5341 = null;
        fifthelement.theelement.objects.Author author11799 = null;
        fifthelement.theelement.objects.Song song10952 = null;
        fifthelement.theelement.objects.Author author11800 = null;
        fifthelement.theelement.objects.Song song10953 = null;
        fifthelement.theelement.objects.Song song10954 = null;
        fifthelement.theelement.objects.Song song10955 = null;
        java.util.ArrayList arraylist3762 = null;
        fifthelement.theelement.objects.Author author11801 = null;
        fifthelement.theelement.objects.Song song10956 = null;
        fifthelement.theelement.objects.Song song10957 = null;
        fifthelement.theelement.objects.Author author11802 = null;
        fifthelement.theelement.objects.Album album5342 = null;
        fifthelement.theelement.objects.Author author11803 = null;
        fifthelement.theelement.objects.Album album5343 = null;
        fifthelement.theelement.objects.Author author11804 = null;
        fifthelement.theelement.objects.Song song10958 = null;
        java.util.ArrayList arraylist3763 = null;
        fifthelement.theelement.objects.Song song10959 = null;
        fifthelement.theelement.objects.Author author11805 = null;
        fifthelement.theelement.objects.Song song10960 = null;
        fifthelement.theelement.objects.Album album5344 = null;
        fifthelement.theelement.objects.Song song10961 = null;
        fifthelement.theelement.objects.Album album5345 = null;
        fifthelement.theelement.objects.Author author11806 = null;
        fifthelement.theelement.objects.Song song10962 = null;
        fifthelement.theelement.objects.Author author11807 = null;
        fifthelement.theelement.objects.Author author11808 = null;
        fifthelement.theelement.objects.Song song10963 = null;
        fifthelement.theelement.objects.Author author11809 = null;
        fifthelement.theelement.objects.Author author11810 = null;
        fifthelement.theelement.business.services.SongListService songlistservice10 = null;
        fifthelement.theelement.objects.Album album5346 = null;
        fifthelement.theelement.objects.Album album5347 = null;
        fifthelement.theelement.objects.Author author11811 = null;
        fifthelement.theelement.objects.Author author11812 = null;
        fifthelement.theelement.objects.Author author11813 = null;
        fifthelement.theelement.objects.Song song10964 = null;
        fifthelement.theelement.objects.Author author11814 = null;
        fifthelement.theelement.objects.Song song10965 = null;
        fifthelement.theelement.objects.Album album5348 = null;
        fifthelement.theelement.objects.Author author11815 = null;
        java.util.List list259 = null;
        songlistservice10 = new fifthelement.theelement.business.services.SongListService();
        arraylist3761 = new java.util.ArrayList();
        java.util.UUID uuid140376 = java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid140377 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author11797 = new fifthelement.theelement.objects.Author(uuid140377, "");
        song10950 = new fifthelement.theelement.objects.Song(uuid140376, "This Is America", "android.resource://fifthelement.theelement/raw/childish_gambino_this_is_america", author11797, album5346, "Hiphop", 3, 1.5);
        arraylist3761.add(song10950);
        java.util.UUID uuid140378 = java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        song10951 = new fifthelement.theelement.objects.Song(uuid140378, "Hall of Fame", "android.resource://fifthelement.theelement/raw/hall_of_fame", author11802, album5346, "", 3, 4.5);
        arraylist3761.add(song10951);
        java.util.UUID uuid140379 = java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        song10957 = new fifthelement.theelement.objects.Song(uuid140379, "Classical Music", "android.resource://fifthelement.theelement/raw/classical_music", author11802, album5346, "Classical", 4, 2.0);
        arraylist3761.add(song10957);
        java.util.UUID uuid140380 = java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid140381 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author11795 = new fifthelement.theelement.objects.Author(uuid140381, "");
        java.util.UUID uuid140382 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        album5345 = new fifthelement.theelement.objects.Album(uuid140382, "");
        song10949 = new fifthelement.theelement.objects.Song(uuid140380, "Adventure of a Lifetime", "android.resource://fifthelement.theelement/raw/coldplay_adventure_of_a_lifetime", author11795, album5345, "Pop", 10, 3.5);
        arraylist3761.add(song10949);
        arraylist3761.iterator();
        java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        songlistservice10.setCurrentSongsList(arraylist3761);
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
        arraylist3762 = new java.util.ArrayList();
        java.util.UUID uuid140447 = java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid140448 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author11800 = new fifthelement.theelement.objects.Author(uuid140448, "");
        song10963 = new fifthelement.theelement.objects.Song(uuid140447, "This Is America", "android.resource://fifthelement.theelement/raw/childish_gambino_this_is_america", author11800, album5346, "Hiphop", 3, 1.5);
        arraylist3762.add(song10963);
        java.util.UUID uuid140449 = java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        song10952 = new fifthelement.theelement.objects.Song(uuid140449, "Hall of Fame", "android.resource://fifthelement.theelement/raw/hall_of_fame", author11802, album5346, "", 3, 4.5);
        arraylist3762.add(song10952);
        java.util.UUID uuid140450 = java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        song10948 = new fifthelement.theelement.objects.Song(uuid140450, "Classical Music", "android.resource://fifthelement.theelement/raw/classical_music", author11802, album5346, "Classical", 4, 2.0);
        arraylist3762.add(song10948);
        java.util.UUID uuid140451 = java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid140452 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author11805 = new fifthelement.theelement.objects.Author(uuid140452, "");
        java.util.UUID uuid140453 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        album5348 = new fifthelement.theelement.objects.Album(uuid140453, "");
        song10962 = new fifthelement.theelement.objects.Song(uuid140451, "Adventure of a Lifetime", "android.resource://fifthelement.theelement/raw/coldplay_adventure_of_a_lifetime", author11805, album5348, "Pop", 10, 3.5);
        arraylist3762.add(song10962);
        arraylist3762.iterator();
        song10963.getAuthor();
        fifthelement.theelement.objects.Author author11817 = song10963.getAuthor();
        author11817.getUUID();
        java.util.UUID uuid140455 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author11809 = new fifthelement.theelement.objects.Author(uuid140455, "Childish Gambino", 3);
        song10963.setAuthor(author11809);
        song10963.getAlbum();
        song10952.getAuthor();
        song10952.getAlbum();
        fifthelement.theelement.objects.Author author11819 = song10948.getAuthor();
        fifthelement.theelement.objects.Album album5351 = song10948.getAlbum();
        song10962.getAuthor();
        fifthelement.theelement.objects.Author author11821 = song10962.getAuthor();
        author11821.getUUID();
        java.util.UUID uuid140457 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author11796 = new fifthelement.theelement.objects.Author(uuid140457, "Coldplay", 10);
        song10962.setAuthor(author11796);
        song10962.getAlbum();
        fifthelement.theelement.objects.Album album5353 = song10962.getAlbum();
        album5353.getUUID();
        java.util.UUID uuid140459 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID uuid140460 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author11804 = new fifthelement.theelement.objects.Author(uuid140460, "");
        album5347 = new fifthelement.theelement.objects.Album(uuid140459, "A Head Full of Dreams", author11804, list259, 10);
        album5347.getAuthor();
        fifthelement.theelement.objects.Author author11823 = album5347.getAuthor();
        author11823.getUUID();
        java.util.UUID uuid140462 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author11803 = new fifthelement.theelement.objects.Author(uuid140462, "Coldplay", 10);
        album5347.setAuthor(author11803);
        song10962.setAlbum(album5347);
        songlistservice10.sortSongs(arraylist3762);
        arraylist3762.size();
        arraylist3762.size();
        songlistservice10.getAutoplayEnabled();
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
        arraylist3763 = new java.util.ArrayList();
        java.util.UUID uuid140476 = java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid140477 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author11813 = new fifthelement.theelement.objects.Author(uuid140477, "");
        song10954 = new fifthelement.theelement.objects.Song(uuid140476, "This Is America", "android.resource://fifthelement.theelement/raw/childish_gambino_this_is_america", author11813, album5351, "Hiphop", 3, 1.5);
        arraylist3763.add(song10954);
        java.util.UUID uuid140478 = java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        song10958 = new fifthelement.theelement.objects.Song(uuid140478, "Hall of Fame", "android.resource://fifthelement.theelement/raw/hall_of_fame", author11819, album5351, "", 3, 4.5);
        arraylist3763.add(song10958);
        java.util.UUID uuid140479 = java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        song10947 = new fifthelement.theelement.objects.Song(uuid140479, "Classical Music", "android.resource://fifthelement.theelement/raw/classical_music", author11819, album5351, "Classical", 4, 2.0);
        arraylist3763.add(song10947);
        java.util.UUID uuid140480 = java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid140481 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author11807 = new fifthelement.theelement.objects.Author(uuid140481, "");
        java.util.UUID uuid140482 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        album5341 = new fifthelement.theelement.objects.Album(uuid140482, "");
        song10953 = new fifthelement.theelement.objects.Song(uuid140480, "Adventure of a Lifetime", "android.resource://fifthelement.theelement/raw/coldplay_adventure_of_a_lifetime", author11807, album5341, "Pop", 10, 3.5);
        arraylist3763.add(song10953);
        arraylist3763.iterator();
        song10954.getAuthor();
        fifthelement.theelement.objects.Author author11825 = song10954.getAuthor();
        author11825.getUUID();
        java.util.UUID uuid140484 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author11810 = new fifthelement.theelement.objects.Author(uuid140484, "Childish Gambino", 3);
        song10954.setAuthor(author11810);
        song10954.getAlbum();
        song10958.getAuthor();
        song10958.getAlbum();
        song10947.getAuthor();
        fifthelement.theelement.objects.Album album5356 = song10947.getAlbum();
        song10953.getAuthor();
        fifthelement.theelement.objects.Author author11829 = song10953.getAuthor();
        author11829.getUUID();
        java.util.UUID uuid140486 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author11815 = new fifthelement.theelement.objects.Author(uuid140486, "Coldplay", 10);
        song10953.setAuthor(author11815);
        song10953.getAlbum();
        fifthelement.theelement.objects.Album album5358 = song10953.getAlbum();
        album5358.getUUID();
        java.util.UUID uuid140488 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID uuid140489 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author11814 = new fifthelement.theelement.objects.Author(uuid140489, "");
        album5340 = new fifthelement.theelement.objects.Album(uuid140488, "A Head Full of Dreams", author11814, list259, 10);
        album5340.getAuthor();
        fifthelement.theelement.objects.Author author11831 = album5340.getAuthor();
        author11831.getUUID();
        java.util.UUID uuid140491 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author11808 = new fifthelement.theelement.objects.Author(uuid140491, "Coldplay", 10);
        album5340.setAuthor(author11808);
        song10953.setAlbum(album5340);
        songlistservice10.sortSongs(arraylist3763);
        arraylist3763.size();
        arraylist3763.size();
        arraylist3763.size();
        arraylist3763.size();
        fifthelement.theelement.objects.Song song10966 = (fifthelement.theelement.objects.Song) arraylist3763.get(0);
        fifthelement.theelement.objects.Author author11832 = song10966.getAuthor();
        author11832.getName();
        song10966.getName();
        fifthelement.theelement.objects.Song song10967 = (fifthelement.theelement.objects.Song) arraylist3763.get(1);
        song10967.getAuthor();
        song10967.getName();
        fifthelement.theelement.objects.Song song10968 = (fifthelement.theelement.objects.Song) arraylist3763.get(2);
        song10968.getAuthor();
        song10968.getName();
        fifthelement.theelement.objects.Song song10969 = (fifthelement.theelement.objects.Song) arraylist3763.get(3);
        fifthelement.theelement.objects.Author author11835 = song10969.getAuthor();
        author11835.getName();
        song10969.getName();
        arraylist3763.size();
        arraylist3763.size();
        fifthelement.theelement.objects.Song song10970 = (fifthelement.theelement.objects.Song) arraylist3763.get(0);
        fifthelement.theelement.objects.Author author11836 = song10970.getAuthor();
        author11836.getName();
        song10970.getName();
        fifthelement.theelement.objects.Song song10971 = (fifthelement.theelement.objects.Song) arraylist3763.get(1);
        song10971.getAuthor();
        song10971.getName();
        fifthelement.theelement.objects.Song song10972 = (fifthelement.theelement.objects.Song) arraylist3763.get(2);
        song10972.getAuthor();
        song10972.getName();
        fifthelement.theelement.objects.Song song10973 = (fifthelement.theelement.objects.Song) arraylist3763.get(3);
        fifthelement.theelement.objects.Author author11839 = song10973.getAuthor();
        author11839.getName();
        song10973.getName();
        arraylist3763.size();
        fifthelement.theelement.objects.Song song10974 = (fifthelement.theelement.objects.Song) arraylist3763.get(0);
        fifthelement.theelement.objects.Author author11840 = song10974.getAuthor();
        author11840.getName();
        song10974.getName();
        fifthelement.theelement.objects.Song song10975 = (fifthelement.theelement.objects.Song) arraylist3763.get(1);
        song10975.getAuthor();
        song10975.getName();
        fifthelement.theelement.objects.Song song10976 = (fifthelement.theelement.objects.Song) arraylist3763.get(2);
        song10976.getAuthor();
        song10976.getName();
        fifthelement.theelement.objects.Song song10977 = (fifthelement.theelement.objects.Song) arraylist3763.get(3);
        fifthelement.theelement.objects.Author author11843 = song10977.getAuthor();
        author11843.getName();
        song10977.getName();
        arraylist3763.size();
        arraylist3763.size();
        fifthelement.theelement.objects.Song song10978 = (fifthelement.theelement.objects.Song) arraylist3763.get(0);
        fifthelement.theelement.objects.Author author11844 = song10978.getAuthor();
        author11844.getName();
        song10978.getName();
        fifthelement.theelement.objects.Song song10979 = (fifthelement.theelement.objects.Song) arraylist3763.get(1);
        song10979.getAuthor();
        song10979.getName();
        fifthelement.theelement.objects.Song song10980 = (fifthelement.theelement.objects.Song) arraylist3763.get(2);
        song10980.getAuthor();
        song10980.getName();
        fifthelement.theelement.objects.Song song10981 = (fifthelement.theelement.objects.Song) arraylist3763.get(3);
        fifthelement.theelement.objects.Author author11847 = song10981.getAuthor();
        author11847.getName();
        song10981.getName();
        arraylist3763.size();
        arraylist3763.size();
        fifthelement.theelement.objects.Song song10982 = (fifthelement.theelement.objects.Song) arraylist3763.get(0);
        fifthelement.theelement.objects.Author author11848 = song10982.getAuthor();
        author11848.getName();
        song10982.getName();
        fifthelement.theelement.objects.Song song10983 = (fifthelement.theelement.objects.Song) arraylist3763.get(1);
        song10983.getAuthor();
        song10983.getName();
        fifthelement.theelement.objects.Song song10984 = (fifthelement.theelement.objects.Song) arraylist3763.get(2);
        song10984.getAuthor();
        song10984.getName();
        fifthelement.theelement.objects.Song song10985 = (fifthelement.theelement.objects.Song) arraylist3763.get(3);
        fifthelement.theelement.objects.Author author11851 = song10985.getAuthor();
        author11851.getName();
        song10985.getName();
        arraylist3763.size();
        arraylist3763.get(0);
        arraylist3763.get(1);
        arraylist3763.get(2);
        arraylist3763.get(3);
        arraylist3763.size();
        arraylist3763.size();
        fifthelement.theelement.objects.Song song10990 = (fifthelement.theelement.objects.Song) arraylist3763.get(0);
        fifthelement.theelement.objects.Author author11852 = song10990.getAuthor();
        author11852.getName();
        song10990.getName();
        fifthelement.theelement.objects.Song song10991 = (fifthelement.theelement.objects.Song) arraylist3763.get(1);
        song10991.getAuthor();
        song10991.getName();
        fifthelement.theelement.objects.Song song10992 = (fifthelement.theelement.objects.Song) arraylist3763.get(2);
        song10992.getAuthor();
        song10992.getName();
        fifthelement.theelement.objects.Song song10993 = (fifthelement.theelement.objects.Song) arraylist3763.get(3);
        fifthelement.theelement.objects.Author author11855 = song10993.getAuthor();
        author11855.getName();
        song10993.getName();
        arraylist3763.size();
        arraylist3763.size();
        fifthelement.theelement.objects.Song song10994 = (fifthelement.theelement.objects.Song) arraylist3763.get(0);
        fifthelement.theelement.objects.Author author11856 = song10994.getAuthor();
        author11856.getName();
        song10994.getName();
        fifthelement.theelement.objects.Song song10995 = (fifthelement.theelement.objects.Song) arraylist3763.get(1);
        song10995.getAuthor();
        song10995.getName();
        fifthelement.theelement.objects.Song song10996 = (fifthelement.theelement.objects.Song) arraylist3763.get(2);
        fifthelement.theelement.objects.Author author11858 = song10996.getAuthor();
        song10996.getName();
        fifthelement.theelement.objects.Song song10997 = (fifthelement.theelement.objects.Song) arraylist3763.get(3);
        fifthelement.theelement.objects.Author author11859 = song10997.getAuthor();
        author11859.getName();
        song10997.getName();
        arraylist3763.size();
        arraylist3763.get(0);
        arraylist3763.get(1);
        arraylist3763.get(2);
        arraylist3763.get(3);
        arraylist3763.get(0);
        arraylist3763.size();
        arraylist3763.size();
        arraylist3763.get(0);
        arraylist3759 = new java.util.ArrayList();
        java.util.UUID uuid140492 = java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid140493 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author11811 = new fifthelement.theelement.objects.Author(uuid140493, "");
        song10959 = new fifthelement.theelement.objects.Song(uuid140492, "This Is America", "android.resource://fifthelement.theelement/raw/childish_gambino_this_is_america", author11811, album5356, "Hiphop", 3, 1.5);
        arraylist3759.add(song10959);
        java.util.UUID uuid140494 = java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        song10956 = new fifthelement.theelement.objects.Song(uuid140494, "Hall of Fame", "android.resource://fifthelement.theelement/raw/hall_of_fame", author11858, album5356, "", 3, 4.5);
        arraylist3759.add(song10956);
        java.util.UUID uuid140495 = java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        song10955 = new fifthelement.theelement.objects.Song(uuid140495, "Classical Music", "android.resource://fifthelement.theelement/raw/classical_music", author11858, album5356, "Classical", 4, 2.0);
        arraylist3759.add(song10955);
        java.util.UUID uuid140496 = java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid140497 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author11793 = new fifthelement.theelement.objects.Author(uuid140497, "");
        java.util.UUID uuid140498 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        album5342 = new fifthelement.theelement.objects.Album(uuid140498, "");
        song10961 = new fifthelement.theelement.objects.Song(uuid140496, "Adventure of a Lifetime", "android.resource://fifthelement.theelement/raw/coldplay_adventure_of_a_lifetime", author11793, album5342, "Pop", 10, 3.5);
        arraylist3759.add(song10961);
        arraylist3759.iterator();
        song10959.getAuthor();
        fifthelement.theelement.objects.Author author11861 = song10959.getAuthor();
        author11861.getUUID();
        java.util.UUID uuid140500 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author11806 = new fifthelement.theelement.objects.Author(uuid140500, "Childish Gambino", 3);
        song10959.setAuthor(author11806);
        song10959.getAlbum();
        song10956.getAuthor();
        song10956.getAlbum();
        song10955.getAuthor();
        fifthelement.theelement.objects.Album album5361 = song10955.getAlbum();
        song10961.getAuthor();
        fifthelement.theelement.objects.Author author11865 = song10961.getAuthor();
        author11865.getUUID();
        java.util.UUID uuid140502 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author11798 = new fifthelement.theelement.objects.Author(uuid140502, "Coldplay", 10);
        song10961.setAuthor(author11798);
        song10961.getAlbum();
        fifthelement.theelement.objects.Album album5363 = song10961.getAlbum();
        album5363.getUUID();
        java.util.UUID uuid140504 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID uuid140505 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author11812 = new fifthelement.theelement.objects.Author(uuid140505, "");
        album5343 = new fifthelement.theelement.objects.Album(uuid140504, "A Head Full of Dreams", author11812, list259, 10);
        album5343.getAuthor();
        fifthelement.theelement.objects.Author author11867 = album5343.getAuthor();
        author11867.getUUID();
        java.util.UUID uuid140507 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author11794 = new fifthelement.theelement.objects.Author(uuid140507, "Coldplay", 10);
        album5343.setAuthor(author11794);
        song10961.setAlbum(album5343);
        songlistservice10.sortSongs(arraylist3759);
        songlistservice10.setCurrentSongsList(arraylist3759);
        songlistservice10.getSongAtIndex(0);
        songlistservice10.setShuffleEnabled(false);
        java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.randomUUID();
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.randomUUID();
        arraylist3763.get(0);
        arraylist3763.get(1);
        arraylist3763.get(2);
        arraylist3763.get(3);
        arraylist3763.size();
        arraylist3763.size();
        fifthelement.theelement.objects.Song song11009 = (fifthelement.theelement.objects.Song) arraylist3763.get(0);
        fifthelement.theelement.objects.Author author11868 = song11009.getAuthor();
        author11868.getName();
        song11009.getName();
        fifthelement.theelement.objects.Song song11010 = (fifthelement.theelement.objects.Song) arraylist3763.get(1);
        song11010.getAuthor();
        song11010.getName();
        fifthelement.theelement.objects.Song song11011 = (fifthelement.theelement.objects.Song) arraylist3763.get(2);
        song11011.getAuthor();
        song11011.getName();
        fifthelement.theelement.objects.Song song11012 = (fifthelement.theelement.objects.Song) arraylist3763.get(3);
        fifthelement.theelement.objects.Author author11871 = song11012.getAuthor();
        author11871.getName();
        song11012.getName();
        arraylist3763.size();
        arraylist3763.size();
        fifthelement.theelement.objects.Song song11013 = (fifthelement.theelement.objects.Song) arraylist3763.get(0);
        fifthelement.theelement.objects.Author author11872 = song11013.getAuthor();
        author11872.getName();
        song11013.getName();
        fifthelement.theelement.objects.Song song11014 = (fifthelement.theelement.objects.Song) arraylist3763.get(1);
        song11014.getAuthor();
        song11014.getName();
        fifthelement.theelement.objects.Song song11015 = (fifthelement.theelement.objects.Song) arraylist3763.get(2);
        fifthelement.theelement.objects.Author author11874 = song11015.getAuthor();
        song11015.getName();
        fifthelement.theelement.objects.Song song11016 = (fifthelement.theelement.objects.Song) arraylist3763.get(3);
        fifthelement.theelement.objects.Author author11875 = song11016.getAuthor();
        author11875.getName();
        song11016.getName();
        arraylist3763.size();
        arraylist3763.get(0);
        arraylist3763.get(1);
        arraylist3763.get(2);
        arraylist3763.get(3);
        arraylist3763.get(0);
        arraylist3763.size();
        arraylist3763.size();
        arraylist3763.get(0);
        arraylist3760 = new java.util.ArrayList();
        java.util.UUID uuid140525 = java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid140526 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author11799 = new fifthelement.theelement.objects.Author(uuid140526, "");
        song10946 = new fifthelement.theelement.objects.Song(uuid140525, "This Is America", "android.resource://fifthelement.theelement/raw/childish_gambino_this_is_america", author11799, album5361, "Hiphop", 3, 1.5);
        arraylist3760.add(song10946);
        java.util.UUID uuid140527 = java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        song10964 = new fifthelement.theelement.objects.Song(uuid140527, "Hall of Fame", "android.resource://fifthelement.theelement/raw/hall_of_fame", author11874, album5361, "", 3, 4.5);
        arraylist3760.add(song10964);
        java.util.UUID uuid140528 = java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        song10965 = new fifthelement.theelement.objects.Song(uuid140528, "Classical Music", "android.resource://fifthelement.theelement/raw/classical_music", author11874, album5361, "Classical", 4, 2.0);
        arraylist3760.add(song10965);
        java.util.UUID uuid140529 = java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid140530 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author11801 = new fifthelement.theelement.objects.Author(uuid140530, "");
        java.util.UUID uuid140531 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        album5344 = new fifthelement.theelement.objects.Album(uuid140531, "");
        song10960 = new fifthelement.theelement.objects.Song(uuid140529, "Adventure of a Lifetime", "android.resource://fifthelement.theelement/raw/coldplay_adventure_of_a_lifetime", author11801, album5344, "Pop", 11, 3.5);
        arraylist3760.add(song10960);
        arraylist3760.iterator();
        songlistservice10.sortSongs(arraylist3760);
        songlistservice10.setCurrentSongsList(arraylist3760);
    }
}

package fifthelement.theelement.business.services;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

public class Test8202 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.SettingsTest#deleteSongsTest/Trace-1650046437980.txt
     * Method invocation under test: <fifthelement.theelement.business.services.SongListService: void sortSongs(java.util.List)>_2643_5284
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_business_services_SongListService_sortSongs_004() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        fifthelement.theelement.objects.Author author7469 = null;
        java.util.ArrayList arraylist2389 = null;
        fifthelement.theelement.objects.Song song5929 = null;
        fifthelement.theelement.objects.Album album2984 = null;
        fifthelement.theelement.objects.Author author7470 = null;
        fifthelement.theelement.objects.Song song5930 = null;
        fifthelement.theelement.objects.Author author7471 = null;
        fifthelement.theelement.objects.Album album2985 = null;
        fifthelement.theelement.objects.Author author7472 = null;
        fifthelement.theelement.objects.Song song5931 = null;
        fifthelement.theelement.objects.Song song5932 = null;
        fifthelement.theelement.objects.Song song5933 = null;
        java.util.ArrayList arraylist2390 = null;
        fifthelement.theelement.objects.Song song5934 = null;
        fifthelement.theelement.objects.Album album2986 = null;
        fifthelement.theelement.objects.Author author7473 = null;
        fifthelement.theelement.objects.Author author7474 = null;
        fifthelement.theelement.objects.Author author7475 = null;
        fifthelement.theelement.objects.Author author7476 = null;
        fifthelement.theelement.objects.Song song5935 = null;
        fifthelement.theelement.objects.Author author7477 = null;
        fifthelement.theelement.objects.Author author7478 = null;
        java.util.ArrayList arraylist2391 = null;
        fifthelement.theelement.objects.Author author7479 = null;
        fifthelement.theelement.objects.Author author7480 = null;
        fifthelement.theelement.objects.Author author7481 = null;
        java.util.ArrayList arraylist2392 = null;
        fifthelement.theelement.objects.Album album2987 = null;
        fifthelement.theelement.objects.Author author7482 = null;
        fifthelement.theelement.objects.Song song5936 = null;
        fifthelement.theelement.objects.Song song5937 = null;
        fifthelement.theelement.objects.Song song5938 = null;
        fifthelement.theelement.objects.Author author7483 = null;
        fifthelement.theelement.objects.Song song5939 = null;
        fifthelement.theelement.objects.Album album2988 = null;
        fifthelement.theelement.objects.Song song5940 = null;
        fifthelement.theelement.objects.Song song5941 = null;
        java.util.ArrayList arraylist2393 = null;
        fifthelement.theelement.objects.Author author7484 = null;
        fifthelement.theelement.objects.Author author7485 = null;
        fifthelement.theelement.objects.Author author7486 = null;
        fifthelement.theelement.objects.Song song5942 = null;
        fifthelement.theelement.objects.Author author7487 = null;
        fifthelement.theelement.objects.Song song5943 = null;
        fifthelement.theelement.objects.Author author7488 = null;
        fifthelement.theelement.objects.Album album2989 = null;
        fifthelement.theelement.objects.Author author7489 = null;
        fifthelement.theelement.objects.Song song5944 = null;
        fifthelement.theelement.business.services.SongListService songlistservice8 = null;
        fifthelement.theelement.objects.Album album2990 = null;
        fifthelement.theelement.objects.Author author7490 = null;
        fifthelement.theelement.objects.Song song5945 = null;
        fifthelement.theelement.objects.Song song5946 = null;
        java.util.List list146 = null;
        songlistservice8 = new fifthelement.theelement.business.services.SongListService();
        arraylist2390 = new java.util.ArrayList();
        java.util.UUID uuid121883 = java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid121884 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author7472 = new fifthelement.theelement.objects.Author(uuid121884, "");
        song5931 = new fifthelement.theelement.objects.Song(uuid121883, "This Is America", "android.resource://fifthelement.theelement/raw/childish_gambino_this_is_america", author7472, album2990, "Hiphop", 3, 1.5);
        arraylist2390.add(song5931);
        java.util.UUID uuid121885 = java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        song5932 = new fifthelement.theelement.objects.Song(uuid121885, "Hall of Fame", "android.resource://fifthelement.theelement/raw/hall_of_fame", author7479, album2990, "", 3, 4.5);
        arraylist2390.add(song5932);
        java.util.UUID uuid121886 = java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        song5935 = new fifthelement.theelement.objects.Song(uuid121886, "Classical Music", "android.resource://fifthelement.theelement/raw/classical_music", author7479, album2990, "Classical", 4, 2.0);
        arraylist2390.add(song5935);
        java.util.UUID uuid121887 = java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid121888 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author7471 = new fifthelement.theelement.objects.Author(uuid121888, "");
        java.util.UUID uuid121889 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        album2988 = new fifthelement.theelement.objects.Album(uuid121889, "");
        song5930 = new fifthelement.theelement.objects.Song(uuid121887, "Adventure of a Lifetime", "android.resource://fifthelement.theelement/raw/coldplay_adventure_of_a_lifetime", author7471, album2988, "Pop", 10, 3.5);
        arraylist2390.add(song5930);
        arraylist2390.iterator();
        song5931.getAuthor();
        fifthelement.theelement.objects.Author author7492 = song5931.getAuthor();
        author7492.getUUID();
        java.util.UUID uuid121891 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author7481 = new fifthelement.theelement.objects.Author(uuid121891, "Childish Gambino", 3);
        song5931.setAuthor(author7481);
        song5931.getAlbum();
        song5932.getAuthor();
        song5932.getAlbum();
        fifthelement.theelement.objects.Author author7494 = song5935.getAuthor();
        fifthelement.theelement.objects.Album album2993 = song5935.getAlbum();
        song5930.getAuthor();
        fifthelement.theelement.objects.Author author7496 = song5930.getAuthor();
        author7496.getUUID();
        java.util.UUID uuid121893 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author7473 = new fifthelement.theelement.objects.Author(uuid121893, "Coldplay", 10);
        song5930.setAuthor(author7473);
        song5930.getAlbum();
        fifthelement.theelement.objects.Album album2995 = song5930.getAlbum();
        album2995.getUUID();
        java.util.UUID uuid121895 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID uuid121896 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author7470 = new fifthelement.theelement.objects.Author(uuid121896, "");
        album2989 = new fifthelement.theelement.objects.Album(uuid121895, "A Head Full of Dreams", author7470, list146, 10);
        album2989.getAuthor();
        fifthelement.theelement.objects.Author author7498 = album2989.getAuthor();
        author7498.getUUID();
        java.util.UUID uuid121898 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author7484 = new fifthelement.theelement.objects.Author(uuid121898, "Coldplay", 10);
        album2989.setAuthor(author7484);
        song5930.setAlbum(album2989);
        songlistservice8.setCurrentSongsList(arraylist2390);
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
        arraylist2393 = new java.util.ArrayList();
        java.util.UUID uuid121958 = java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid121959 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author7482 = new fifthelement.theelement.objects.Author(uuid121959, "");
        song5940 = new fifthelement.theelement.objects.Song(uuid121958, "This Is America", "android.resource://fifthelement.theelement/raw/childish_gambino_this_is_america", author7482, album2993, "Hiphop", 3, 1.5);
        arraylist2393.add(song5940);
        java.util.UUID uuid121960 = java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        song5929 = new fifthelement.theelement.objects.Song(uuid121960, "Hall of Fame", "android.resource://fifthelement.theelement/raw/hall_of_fame", author7494, album2993, "", 3, 4.5);
        arraylist2393.add(song5929);
        java.util.UUID uuid121961 = java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        song5943 = new fifthelement.theelement.objects.Song(uuid121961, "Classical Music", "android.resource://fifthelement.theelement/raw/classical_music", author7494, album2993, "Classical", 4, 2.0);
        arraylist2393.add(song5943);
        java.util.UUID uuid121962 = java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid121963 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author7469 = new fifthelement.theelement.objects.Author(uuid121963, "");
        java.util.UUID uuid121964 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        album2986 = new fifthelement.theelement.objects.Album(uuid121964, "");
        song5945 = new fifthelement.theelement.objects.Song(uuid121962, "Adventure of a Lifetime", "android.resource://fifthelement.theelement/raw/coldplay_adventure_of_a_lifetime", author7469, album2986, "Pop", 10, 3.5);
        arraylist2393.add(song5945);
        arraylist2393.iterator();
        song5940.getAuthor();
        fifthelement.theelement.objects.Author author7500 = song5940.getAuthor();
        author7500.getUUID();
        java.util.UUID uuid121966 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author7489 = new fifthelement.theelement.objects.Author(uuid121966, "Childish Gambino", 3);
        song5940.setAuthor(author7489);
        song5940.getAlbum();
        song5929.getAuthor();
        song5929.getAlbum();
        fifthelement.theelement.objects.Author author7502 = song5943.getAuthor();
        fifthelement.theelement.objects.Album album2998 = song5943.getAlbum();
        song5945.getAuthor();
        fifthelement.theelement.objects.Author author7504 = song5945.getAuthor();
        author7504.getUUID();
        java.util.UUID uuid121968 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author7476 = new fifthelement.theelement.objects.Author(uuid121968, "Coldplay", 10);
        song5945.setAuthor(author7476);
        song5945.getAlbum();
        fifthelement.theelement.objects.Album album3000 = song5945.getAlbum();
        album3000.getUUID();
        java.util.UUID uuid121970 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID uuid121971 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author7474 = new fifthelement.theelement.objects.Author(uuid121971, "");
        album2984 = new fifthelement.theelement.objects.Album(uuid121970, "A Head Full of Dreams", author7474, list146, 10);
        album2984.getAuthor();
        fifthelement.theelement.objects.Author author7506 = album2984.getAuthor();
        author7506.getUUID();
        java.util.UUID uuid121973 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author7487 = new fifthelement.theelement.objects.Author(uuid121973, "Coldplay", 10);
        album2984.setAuthor(author7487);
        song5945.setAlbum(album2984);
        songlistservice8.sortSongs(arraylist2393);
        arraylist2393.size();
        arraylist2393.size();
        songlistservice8.getAutoplayEnabled();
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
        arraylist2391 = new java.util.ArrayList();
        java.util.UUID uuid121987 = java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid121988 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author7486 = new fifthelement.theelement.objects.Author(uuid121988, "");
        song5946 = new fifthelement.theelement.objects.Song(uuid121987, "This Is America", "android.resource://fifthelement.theelement/raw/childish_gambino_this_is_america", author7486, album2998, "Hiphop", 3, 1.5);
        arraylist2391.add(song5946);
        java.util.UUID uuid121989 = java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        song5938 = new fifthelement.theelement.objects.Song(uuid121989, "Hall of Fame", "android.resource://fifthelement.theelement/raw/hall_of_fame", author7502, album2998, "", 3, 4.5);
        arraylist2391.add(song5938);
        java.util.UUID uuid121990 = java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        song5936 = new fifthelement.theelement.objects.Song(uuid121990, "Classical Music", "android.resource://fifthelement.theelement/raw/classical_music", author7502, album2998, "Classical", 4, 2.0);
        arraylist2391.add(song5936);
        java.util.UUID uuid121991 = java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid121992 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author7475 = new fifthelement.theelement.objects.Author(uuid121992, "");
        java.util.UUID uuid121993 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        album2985 = new fifthelement.theelement.objects.Album(uuid121993, "");
        song5941 = new fifthelement.theelement.objects.Song(uuid121991, "Adventure of a Lifetime", "android.resource://fifthelement.theelement/raw/coldplay_adventure_of_a_lifetime", author7475, album2985, "Pop", 10, 3.5);
        arraylist2391.add(song5941);
        arraylist2391.iterator();
        song5946.getAuthor();
        fifthelement.theelement.objects.Author author7508 = song5946.getAuthor();
        author7508.getUUID();
        java.util.UUID uuid121995 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author7490 = new fifthelement.theelement.objects.Author(uuid121995, "Childish Gambino", 3);
        song5946.setAuthor(author7490);
        song5946.getAlbum();
        song5938.getAuthor();
        song5938.getAlbum();
        song5936.getAuthor();
        fifthelement.theelement.objects.Album album3003 = song5936.getAlbum();
        song5941.getAuthor();
        fifthelement.theelement.objects.Author author7512 = song5941.getAuthor();
        author7512.getUUID();
        java.util.UUID uuid121997 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author7483 = new fifthelement.theelement.objects.Author(uuid121997, "Coldplay", 10);
        song5941.setAuthor(author7483);
        song5941.getAlbum();
        fifthelement.theelement.objects.Album album3005 = song5941.getAlbum();
        album3005.getUUID();
        java.util.UUID uuid121999 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID uuid122000 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author7488 = new fifthelement.theelement.objects.Author(uuid122000, "");
        album2987 = new fifthelement.theelement.objects.Album(uuid121999, "A Head Full of Dreams", author7488, list146, 10);
        album2987.getAuthor();
        fifthelement.theelement.objects.Author author7514 = album2987.getAuthor();
        author7514.getUUID();
        java.util.UUID uuid122002 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author7480 = new fifthelement.theelement.objects.Author(uuid122002, "Coldplay", 10);
        album2987.setAuthor(author7480);
        song5941.setAlbum(album2987);
        songlistservice8.sortSongs(arraylist2391);
        arraylist2391.size();
        arraylist2391.size();
        arraylist2391.size();
        arraylist2391.size();
        fifthelement.theelement.objects.Song song5947 = (fifthelement.theelement.objects.Song) arraylist2391.get(0);
        fifthelement.theelement.objects.Author author7515 = song5947.getAuthor();
        author7515.getName();
        song5947.getName();
        fifthelement.theelement.objects.Song song5948 = (fifthelement.theelement.objects.Song) arraylist2391.get(1);
        song5948.getAuthor();
        song5948.getName();
        fifthelement.theelement.objects.Song song5949 = (fifthelement.theelement.objects.Song) arraylist2391.get(2);
        song5949.getAuthor();
        song5949.getName();
        fifthelement.theelement.objects.Song song5950 = (fifthelement.theelement.objects.Song) arraylist2391.get(3);
        fifthelement.theelement.objects.Author author7518 = song5950.getAuthor();
        author7518.getName();
        song5950.getName();
        arraylist2391.size();
        arraylist2391.size();
        fifthelement.theelement.objects.Song song5951 = (fifthelement.theelement.objects.Song) arraylist2391.get(0);
        fifthelement.theelement.objects.Author author7519 = song5951.getAuthor();
        author7519.getName();
        song5951.getName();
        fifthelement.theelement.objects.Song song5952 = (fifthelement.theelement.objects.Song) arraylist2391.get(1);
        song5952.getAuthor();
        song5952.getName();
        fifthelement.theelement.objects.Song song5953 = (fifthelement.theelement.objects.Song) arraylist2391.get(2);
        song5953.getAuthor();
        song5953.getName();
        fifthelement.theelement.objects.Song song5954 = (fifthelement.theelement.objects.Song) arraylist2391.get(3);
        fifthelement.theelement.objects.Author author7522 = song5954.getAuthor();
        author7522.getName();
        song5954.getName();
        arraylist2391.size();
        fifthelement.theelement.objects.Song song5955 = (fifthelement.theelement.objects.Song) arraylist2391.get(0);
        fifthelement.theelement.objects.Author author7523 = song5955.getAuthor();
        author7523.getName();
        song5955.getName();
        fifthelement.theelement.objects.Song song5956 = (fifthelement.theelement.objects.Song) arraylist2391.get(1);
        song5956.getAuthor();
        song5956.getName();
        fifthelement.theelement.objects.Song song5957 = (fifthelement.theelement.objects.Song) arraylist2391.get(2);
        song5957.getAuthor();
        song5957.getName();
        fifthelement.theelement.objects.Song song5958 = (fifthelement.theelement.objects.Song) arraylist2391.get(3);
        fifthelement.theelement.objects.Author author7526 = song5958.getAuthor();
        author7526.getName();
        song5958.getName();
        arraylist2391.size();
        arraylist2391.size();
        fifthelement.theelement.objects.Song song5959 = (fifthelement.theelement.objects.Song) arraylist2391.get(0);
        fifthelement.theelement.objects.Author author7527 = song5959.getAuthor();
        author7527.getName();
        song5959.getName();
        fifthelement.theelement.objects.Song song5960 = (fifthelement.theelement.objects.Song) arraylist2391.get(1);
        song5960.getAuthor();
        song5960.getName();
        fifthelement.theelement.objects.Song song5961 = (fifthelement.theelement.objects.Song) arraylist2391.get(2);
        song5961.getAuthor();
        song5961.getName();
        fifthelement.theelement.objects.Song song5962 = (fifthelement.theelement.objects.Song) arraylist2391.get(3);
        fifthelement.theelement.objects.Author author7530 = song5962.getAuthor();
        author7530.getName();
        song5962.getName();
        arraylist2391.size();
        arraylist2391.size();
        fifthelement.theelement.objects.Song song5963 = (fifthelement.theelement.objects.Song) arraylist2391.get(0);
        fifthelement.theelement.objects.Author author7531 = song5963.getAuthor();
        author7531.getName();
        song5963.getName();
        fifthelement.theelement.objects.Song song5964 = (fifthelement.theelement.objects.Song) arraylist2391.get(1);
        song5964.getAuthor();
        song5964.getName();
        fifthelement.theelement.objects.Song song5965 = (fifthelement.theelement.objects.Song) arraylist2391.get(2);
        song5965.getAuthor();
        song5965.getName();
        fifthelement.theelement.objects.Song song5966 = (fifthelement.theelement.objects.Song) arraylist2391.get(3);
        fifthelement.theelement.objects.Author author7534 = song5966.getAuthor();
        author7534.getName();
        song5966.getName();
        arraylist2391.size();
        song5963.getName();
        java.util.UUID uuid122003 = song5963.getUUID();
        uuid122003.toString();
        java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.randomUUID();
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        arraylist2391.remove(song5963);
        songlistservice8.removeSongFromList(song5963);
        arraylist2391.size();
        arraylist2391.size();
        arraylist2391.get(0);
        arraylist2391.size();
        arraylist2391.size();
        arraylist2391.size();
        fifthelement.theelement.objects.Song song5968 = (fifthelement.theelement.objects.Song) arraylist2391.get(0);
        song5968.getAuthor();
        song5968.getName();
        fifthelement.theelement.objects.Song song5969 = (fifthelement.theelement.objects.Song) arraylist2391.get(1);
        song5969.getAuthor();
        song5969.getName();
        fifthelement.theelement.objects.Song song5970 = (fifthelement.theelement.objects.Song) arraylist2391.get(2);
        fifthelement.theelement.objects.Author author7537 = song5970.getAuthor();
        author7537.getName();
        song5970.getName();
        arraylist2391.size();
        arraylist2391.size();
        fifthelement.theelement.objects.Song song5971 = (fifthelement.theelement.objects.Song) arraylist2391.get(0);
        song5971.getAuthor();
        song5971.getName();
        fifthelement.theelement.objects.Song song5972 = (fifthelement.theelement.objects.Song) arraylist2391.get(1);
        song5972.getAuthor();
        song5972.getName();
        fifthelement.theelement.objects.Song song5973 = (fifthelement.theelement.objects.Song) arraylist2391.get(2);
        fifthelement.theelement.objects.Author author7540 = song5973.getAuthor();
        author7540.getName();
        song5973.getName();
        arraylist2391.size();
        arraylist2391.get(0);
        arraylist2391.size();
        fifthelement.theelement.objects.Song song5975 = (fifthelement.theelement.objects.Song) arraylist2391.get(0);
        song5975.getAuthor();
        song5975.getName();
        fifthelement.theelement.objects.Song song5976 = (fifthelement.theelement.objects.Song) arraylist2391.get(1);
        fifthelement.theelement.objects.Author author7542 = song5976.getAuthor();
        song5976.getName();
        arraylist2391.get(2);
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
        arraylist2389 = new java.util.ArrayList();
        java.util.UUID uuid122030 = java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid122031 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author7477 = new fifthelement.theelement.objects.Author(uuid122031, "");
        song5934 = new fifthelement.theelement.objects.Song(uuid122030, "This Is America", "android.resource://fifthelement.theelement/raw/childish_gambino_this_is_america", author7477, album3003, "Hiphop", 3, 1.5);
        arraylist2389.add(song5934);
        java.util.UUID uuid122032 = java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        song5944 = new fifthelement.theelement.objects.Song(uuid122032, "Hall of Fame", "android.resource://fifthelement.theelement/raw/hall_of_fame", author7542, album3003, "", 3, 4.5);
        arraylist2389.add(song5944);
        java.util.UUID uuid122033 = java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        song5942 = new fifthelement.theelement.objects.Song(uuid122033, "Classical Music", "android.resource://fifthelement.theelement/raw/classical_music", author7542, album3003, "Classical", 4, 2.0);
        arraylist2389.add(song5942);
        arraylist2389.iterator();
        song5934.getAuthor();
        fifthelement.theelement.objects.Author author7544 = song5934.getAuthor();
        author7544.getUUID();
        java.util.UUID uuid122035 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author7478 = new fifthelement.theelement.objects.Author(uuid122035, "Childish Gambino", 3);
        song5934.setAuthor(author7478);
        song5934.getAlbum();
        song5944.getAuthor();
        song5944.getAlbum();
        fifthelement.theelement.objects.Author author7546 = song5942.getAuthor();
        fifthelement.theelement.objects.Album album3008 = song5942.getAlbum();
        songlistservice8.sortSongs(arraylist2389);
        arraylist2389.size();
        arraylist2389.size();
        songlistservice8.getAutoplayEnabled();
        java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        arraylist2392 = new java.util.ArrayList();
        java.util.UUID uuid122041 = java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid122042 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author7485 = new fifthelement.theelement.objects.Author(uuid122042, "");
        song5939 = new fifthelement.theelement.objects.Song(uuid122041, "This Is America", "android.resource://fifthelement.theelement/raw/childish_gambino_this_is_america", author7485, album3008, "Hiphop", 3, 1.5);
        arraylist2392.add(song5939);
        java.util.UUID uuid122043 = java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        song5937 = new fifthelement.theelement.objects.Song(uuid122043, "Hall of Fame", "android.resource://fifthelement.theelement/raw/hall_of_fame", author7546, album3008, "", 3, 4.5);
        arraylist2392.add(song5937);
        java.util.UUID uuid122044 = java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        song5933 = new fifthelement.theelement.objects.Song(uuid122044, "Classical Music", "android.resource://fifthelement.theelement/raw/classical_music", author7546, album3008, "Classical", 4, 2.0);
        arraylist2392.add(song5933);
        arraylist2392.iterator();
        songlistservice8.sortSongs(arraylist2392);
    }
}

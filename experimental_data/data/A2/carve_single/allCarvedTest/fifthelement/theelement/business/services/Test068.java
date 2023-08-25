package fifthelement.theelement.business.services;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

public class Test068 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.CustomizeTests#skipSongWithSongPlayedCheck/Trace-1650046492781.txt
     * Method invocation under test: <fifthelement.theelement.business.services.SongListService: fifthelement.theelement.objects.Song getSongAtIndex(int)>_2067_4133
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_business_services_SongListService_getSongAtIndex_001() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        fifthelement.theelement.objects.Song song395 = null;
        fifthelement.theelement.objects.Song song396 = null;
        fifthelement.theelement.objects.Song song397 = null;
        fifthelement.theelement.objects.Author author362 = null;
        java.util.ArrayList arraylist159 = null;
        fifthelement.theelement.objects.Album album193 = null;
        java.util.ArrayList arraylist160 = null;
        java.util.ArrayList arraylist161 = null;
        fifthelement.theelement.objects.Album album194 = null;
        fifthelement.theelement.objects.Song song398 = null;
        fifthelement.theelement.objects.Author author363 = null;
        fifthelement.theelement.objects.Author author364 = null;
        fifthelement.theelement.objects.Author author365 = null;
        fifthelement.theelement.objects.Author author366 = null;
        fifthelement.theelement.objects.Song song399 = null;
        fifthelement.theelement.objects.Song song400 = null;
        fifthelement.theelement.objects.Album album195 = null;
        fifthelement.theelement.objects.Album album196 = null;
        fifthelement.theelement.objects.Author author367 = null;
        fifthelement.theelement.objects.Author author368 = null;
        fifthelement.theelement.objects.Author author369 = null;
        fifthelement.theelement.objects.Author author370 = null;
        java.util.ArrayList arraylist162 = null;
        fifthelement.theelement.objects.Song song401 = null;
        fifthelement.theelement.objects.Song song402 = null;
        fifthelement.theelement.objects.Album album197 = null;
        fifthelement.theelement.objects.Author author371 = null;
        fifthelement.theelement.objects.Song song403 = null;
        fifthelement.theelement.objects.Song song404 = null;
        fifthelement.theelement.objects.Album album198 = null;
        fifthelement.theelement.objects.Song song405 = null;
        fifthelement.theelement.objects.Author author372 = null;
        fifthelement.theelement.objects.Album album199 = null;
        fifthelement.theelement.business.services.SongListService songlistservice4 = null;
        fifthelement.theelement.objects.Author author373 = null;
        fifthelement.theelement.objects.Author author374 = null;
        fifthelement.theelement.objects.Author author375 = null;
        fifthelement.theelement.objects.Author author376 = null;
        fifthelement.theelement.objects.Song song406 = null;
        fifthelement.theelement.objects.Song song407 = null;
        fifthelement.theelement.objects.Song song408 = null;
        java.util.List list15 = null;
        fifthelement.theelement.objects.Author author377 = null;
        fifthelement.theelement.objects.Song song409 = null;
        fifthelement.theelement.objects.Song song410 = null;
        fifthelement.theelement.objects.Author author378 = null;
        songlistservice4 = new fifthelement.theelement.business.services.SongListService();
        arraylist162 = new java.util.ArrayList();
        java.util.UUID uuid2936 = java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid2937 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author364 = new fifthelement.theelement.objects.Author(uuid2937, "");
        song406 = new fifthelement.theelement.objects.Song(uuid2936, "This Is America", "android.resource://fifthelement.theelement/raw/childish_gambino_this_is_america", author364, album199, "Hiphop", 3, 1.5);
        arraylist162.add(song406);
        java.util.UUID uuid2938 = java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        song408 = new fifthelement.theelement.objects.Song(uuid2938, "Hall of Fame", "android.resource://fifthelement.theelement/raw/hall_of_fame", author373, album199, "", 3, 4.5);
        arraylist162.add(song408);
        java.util.UUID uuid2939 = java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        song400 = new fifthelement.theelement.objects.Song(uuid2939, "Classical Music", "android.resource://fifthelement.theelement/raw/classical_music", author373, album199, "Classical", 4, 2.0);
        arraylist162.add(song400);
        java.util.UUID uuid2940 = java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid2941 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author362 = new fifthelement.theelement.objects.Author(uuid2941, "");
        java.util.UUID uuid2942 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        album197 = new fifthelement.theelement.objects.Album(uuid2942, "");
        song410 = new fifthelement.theelement.objects.Song(uuid2940, "Adventure of a Lifetime", "android.resource://fifthelement.theelement/raw/coldplay_adventure_of_a_lifetime", author362, album197, "Pop", 10, 3.5);
        arraylist162.add(song410);
        arraylist162.iterator();
        java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        songlistservice4.setCurrentSongsList(arraylist162);
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
        arraylist161 = new java.util.ArrayList();
        java.util.UUID uuid3007 = java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid3008 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author366 = new fifthelement.theelement.objects.Author(uuid3008, "");
        song397 = new fifthelement.theelement.objects.Song(uuid3007, "This Is America", "android.resource://fifthelement.theelement/raw/childish_gambino_this_is_america", author366, album199, "Hiphop", 3, 1.5);
        arraylist161.add(song397);
        java.util.UUID uuid3009 = java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        song399 = new fifthelement.theelement.objects.Song(uuid3009, "Hall of Fame", "android.resource://fifthelement.theelement/raw/hall_of_fame", author373, album199, "", 3, 4.5);
        arraylist161.add(song399);
        java.util.UUID uuid3010 = java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        song401 = new fifthelement.theelement.objects.Song(uuid3010, "Classical Music", "android.resource://fifthelement.theelement/raw/classical_music", author373, album199, "Classical", 4, 2.0);
        arraylist161.add(song401);
        java.util.UUID uuid3011 = java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid3012 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author376 = new fifthelement.theelement.objects.Author(uuid3012, "");
        java.util.UUID uuid3013 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        album193 = new fifthelement.theelement.objects.Album(uuid3013, "");
        song403 = new fifthelement.theelement.objects.Song(uuid3011, "Adventure of a Lifetime", "android.resource://fifthelement.theelement/raw/coldplay_adventure_of_a_lifetime", author376, album193, "Pop", 10, 3.5);
        arraylist161.add(song403);
        arraylist161.iterator();
        song397.getAuthor();
        fifthelement.theelement.objects.Author author380 = song397.getAuthor();
        author380.getUUID();
        java.util.UUID uuid3015 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author375 = new fifthelement.theelement.objects.Author(uuid3015, "Childish Gambino", 3);
        song397.setAuthor(author375);
        song397.getAlbum();
        song399.getAuthor();
        song399.getAlbum();
        fifthelement.theelement.objects.Author author382 = song401.getAuthor();
        fifthelement.theelement.objects.Album album202 = song401.getAlbum();
        song403.getAuthor();
        fifthelement.theelement.objects.Author author384 = song403.getAuthor();
        author384.getUUID();
        java.util.UUID uuid3017 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author377 = new fifthelement.theelement.objects.Author(uuid3017, "Coldplay", 10);
        song403.setAuthor(author377);
        song403.getAlbum();
        fifthelement.theelement.objects.Album album204 = song403.getAlbum();
        album204.getUUID();
        java.util.UUID uuid3019 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID uuid3020 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author374 = new fifthelement.theelement.objects.Author(uuid3020, "");
        album195 = new fifthelement.theelement.objects.Album(uuid3019, "A Head Full of Dreams", author374, list15, 10);
        album195.getAuthor();
        fifthelement.theelement.objects.Author author386 = album195.getAuthor();
        author386.getUUID();
        java.util.UUID uuid3022 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author372 = new fifthelement.theelement.objects.Author(uuid3022, "Coldplay", 10);
        album195.setAuthor(author372);
        song403.setAlbum(album195);
        songlistservice4.sortSongs(arraylist161);
        arraylist161.size();
        arraylist161.size();
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
        arraylist160 = new java.util.ArrayList();
        java.util.UUID uuid3036 = java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid3037 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author371 = new fifthelement.theelement.objects.Author(uuid3037, "");
        song395 = new fifthelement.theelement.objects.Song(uuid3036, "This Is America", "android.resource://fifthelement.theelement/raw/childish_gambino_this_is_america", author371, album202, "Hiphop", 3, 1.5);
        arraylist160.add(song395);
        java.util.UUID uuid3038 = java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        song407 = new fifthelement.theelement.objects.Song(uuid3038, "Hall of Fame", "android.resource://fifthelement.theelement/raw/hall_of_fame", author382, album202, "", 3, 4.5);
        arraylist160.add(song407);
        java.util.UUID uuid3039 = java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        song404 = new fifthelement.theelement.objects.Song(uuid3039, "Classical Music", "android.resource://fifthelement.theelement/raw/classical_music", author382, album202, "Classical", 4, 2.0);
        arraylist160.add(song404);
        java.util.UUID uuid3040 = java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid3041 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author365 = new fifthelement.theelement.objects.Author(uuid3041, "");
        java.util.UUID uuid3042 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        album196 = new fifthelement.theelement.objects.Album(uuid3042, "");
        song405 = new fifthelement.theelement.objects.Song(uuid3040, "Adventure of a Lifetime", "android.resource://fifthelement.theelement/raw/coldplay_adventure_of_a_lifetime", author365, album196, "Pop", 10, 3.5);
        arraylist160.add(song405);
        arraylist160.iterator();
        song395.getAuthor();
        fifthelement.theelement.objects.Author author388 = song395.getAuthor();
        author388.getUUID();
        java.util.UUID uuid3044 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author367 = new fifthelement.theelement.objects.Author(uuid3044, "Childish Gambino", 3);
        song395.setAuthor(author367);
        song395.getAlbum();
        song407.getAuthor();
        song407.getAlbum();
        song404.getAuthor();
        fifthelement.theelement.objects.Album album207 = song404.getAlbum();
        song405.getAuthor();
        fifthelement.theelement.objects.Author author392 = song405.getAuthor();
        author392.getUUID();
        java.util.UUID uuid3046 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author368 = new fifthelement.theelement.objects.Author(uuid3046, "Coldplay", 10);
        song405.setAuthor(author368);
        song405.getAlbum();
        fifthelement.theelement.objects.Album album209 = song405.getAlbum();
        album209.getUUID();
        java.util.UUID uuid3048 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID uuid3049 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author369 = new fifthelement.theelement.objects.Author(uuid3049, "");
        album194 = new fifthelement.theelement.objects.Album(uuid3048, "A Head Full of Dreams", author369, list15, 10);
        album194.getAuthor();
        fifthelement.theelement.objects.Author author394 = album194.getAuthor();
        author394.getUUID();
        java.util.UUID uuid3051 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author363 = new fifthelement.theelement.objects.Author(uuid3051, "Coldplay", 10);
        album194.setAuthor(author363);
        song405.setAlbum(album194);
        songlistservice4.sortSongs(arraylist160);
        arraylist160.size();
        arraylist160.size();
        arraylist160.size();
        arraylist160.size();
        fifthelement.theelement.objects.Song song411 = (fifthelement.theelement.objects.Song) arraylist160.get(0);
        fifthelement.theelement.objects.Author author395 = song411.getAuthor();
        author395.getName();
        song411.getName();
        fifthelement.theelement.objects.Song song412 = (fifthelement.theelement.objects.Song) arraylist160.get(1);
        song412.getAuthor();
        song412.getName();
        fifthelement.theelement.objects.Song song413 = (fifthelement.theelement.objects.Song) arraylist160.get(2);
        song413.getAuthor();
        song413.getName();
        fifthelement.theelement.objects.Song song414 = (fifthelement.theelement.objects.Song) arraylist160.get(3);
        fifthelement.theelement.objects.Author author398 = song414.getAuthor();
        author398.getName();
        song414.getName();
        arraylist160.size();
        arraylist160.size();
        fifthelement.theelement.objects.Song song415 = (fifthelement.theelement.objects.Song) arraylist160.get(0);
        fifthelement.theelement.objects.Author author399 = song415.getAuthor();
        author399.getName();
        song415.getName();
        fifthelement.theelement.objects.Song song416 = (fifthelement.theelement.objects.Song) arraylist160.get(1);
        song416.getAuthor();
        song416.getName();
        fifthelement.theelement.objects.Song song417 = (fifthelement.theelement.objects.Song) arraylist160.get(2);
        song417.getAuthor();
        song417.getName();
        fifthelement.theelement.objects.Song song418 = (fifthelement.theelement.objects.Song) arraylist160.get(3);
        fifthelement.theelement.objects.Author author402 = song418.getAuthor();
        author402.getName();
        song418.getName();
        arraylist160.size();
        fifthelement.theelement.objects.Song song419 = (fifthelement.theelement.objects.Song) arraylist160.get(0);
        fifthelement.theelement.objects.Author author403 = song419.getAuthor();
        author403.getName();
        song419.getName();
        fifthelement.theelement.objects.Song song420 = (fifthelement.theelement.objects.Song) arraylist160.get(1);
        song420.getAuthor();
        song420.getName();
        fifthelement.theelement.objects.Song song421 = (fifthelement.theelement.objects.Song) arraylist160.get(2);
        song421.getAuthor();
        song421.getName();
        fifthelement.theelement.objects.Song song422 = (fifthelement.theelement.objects.Song) arraylist160.get(3);
        fifthelement.theelement.objects.Author author406 = song422.getAuthor();
        author406.getName();
        song422.getName();
        arraylist160.size();
        arraylist160.size();
        fifthelement.theelement.objects.Song song423 = (fifthelement.theelement.objects.Song) arraylist160.get(0);
        fifthelement.theelement.objects.Author author407 = song423.getAuthor();
        author407.getName();
        song423.getName();
        fifthelement.theelement.objects.Song song424 = (fifthelement.theelement.objects.Song) arraylist160.get(1);
        song424.getAuthor();
        song424.getName();
        fifthelement.theelement.objects.Song song425 = (fifthelement.theelement.objects.Song) arraylist160.get(2);
        song425.getAuthor();
        song425.getName();
        fifthelement.theelement.objects.Song song426 = (fifthelement.theelement.objects.Song) arraylist160.get(3);
        fifthelement.theelement.objects.Author author410 = song426.getAuthor();
        author410.getName();
        song426.getName();
        arraylist160.size();
        arraylist160.size();
        fifthelement.theelement.objects.Song song427 = (fifthelement.theelement.objects.Song) arraylist160.get(0);
        fifthelement.theelement.objects.Author author411 = song427.getAuthor();
        author411.getName();
        song427.getName();
        fifthelement.theelement.objects.Song song428 = (fifthelement.theelement.objects.Song) arraylist160.get(1);
        song428.getAuthor();
        song428.getName();
        fifthelement.theelement.objects.Song song429 = (fifthelement.theelement.objects.Song) arraylist160.get(2);
        song429.getAuthor();
        song429.getName();
        fifthelement.theelement.objects.Song song430 = (fifthelement.theelement.objects.Song) arraylist160.get(3);
        fifthelement.theelement.objects.Author author414 = song430.getAuthor();
        author414.getName();
        song430.getName();
        arraylist160.size();
        arraylist160.get(0);
        arraylist160.get(1);
        arraylist160.get(2);
        arraylist160.get(3);
        arraylist160.size();
        arraylist160.size();
        fifthelement.theelement.objects.Song song435 = (fifthelement.theelement.objects.Song) arraylist160.get(0);
        fifthelement.theelement.objects.Author author415 = song435.getAuthor();
        author415.getName();
        song435.getName();
        fifthelement.theelement.objects.Song song436 = (fifthelement.theelement.objects.Song) arraylist160.get(1);
        song436.getAuthor();
        song436.getName();
        fifthelement.theelement.objects.Song song437 = (fifthelement.theelement.objects.Song) arraylist160.get(2);
        song437.getAuthor();
        song437.getName();
        fifthelement.theelement.objects.Song song438 = (fifthelement.theelement.objects.Song) arraylist160.get(3);
        fifthelement.theelement.objects.Author author418 = song438.getAuthor();
        author418.getName();
        song438.getName();
        arraylist160.size();
        arraylist160.size();
        fifthelement.theelement.objects.Song song439 = (fifthelement.theelement.objects.Song) arraylist160.get(0);
        fifthelement.theelement.objects.Author author419 = song439.getAuthor();
        author419.getName();
        song439.getName();
        fifthelement.theelement.objects.Song song440 = (fifthelement.theelement.objects.Song) arraylist160.get(1);
        song440.getAuthor();
        song440.getName();
        fifthelement.theelement.objects.Song song441 = (fifthelement.theelement.objects.Song) arraylist160.get(2);
        fifthelement.theelement.objects.Author author421 = song441.getAuthor();
        song441.getName();
        fifthelement.theelement.objects.Song song442 = (fifthelement.theelement.objects.Song) arraylist160.get(3);
        fifthelement.theelement.objects.Author author422 = song442.getAuthor();
        author422.getName();
        song442.getName();
        arraylist160.size();
        arraylist160.get(0);
        arraylist160.get(1);
        arraylist160.get(2);
        arraylist160.get(3);
        arraylist160.get(0);
        arraylist160.size();
        arraylist160.size();
        arraylist160.get(0);
        arraylist159 = new java.util.ArrayList();
        java.util.UUID uuid3052 = java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid3053 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author370 = new fifthelement.theelement.objects.Author(uuid3053, "");
        song402 = new fifthelement.theelement.objects.Song(uuid3052, "This Is America", "android.resource://fifthelement.theelement/raw/childish_gambino_this_is_america", author370, album207, "Hiphop", 3, 1.5);
        arraylist159.add(song402);
        java.util.UUID uuid3054 = java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        song398 = new fifthelement.theelement.objects.Song(uuid3054, "Hall of Fame", "android.resource://fifthelement.theelement/raw/hall_of_fame", author421, album207, "", 3, 4.5);
        arraylist159.add(song398);
        java.util.UUID uuid3055 = java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        song396 = new fifthelement.theelement.objects.Song(uuid3055, "Classical Music", "android.resource://fifthelement.theelement/raw/classical_music", author421, album207, "Classical", 4, 2.0);
        arraylist159.add(song396);
        java.util.UUID uuid3056 = java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid3057 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author378 = new fifthelement.theelement.objects.Author(uuid3057, "");
        java.util.UUID uuid3058 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        album198 = new fifthelement.theelement.objects.Album(uuid3058, "");
        song409 = new fifthelement.theelement.objects.Song(uuid3056, "Adventure of a Lifetime", "android.resource://fifthelement.theelement/raw/coldplay_adventure_of_a_lifetime", author378, album198, "Pop", 10, 3.5);
        arraylist159.add(song409);
        arraylist159.iterator();
        songlistservice4.sortSongs(arraylist159);
        songlistservice4.setCurrentSongsList(arraylist159);
        songlistservice4.getSongAtIndex(0);
    }
}

package fifthelement.theelement.business.services;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

public class Test1176 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.PlayMusicTests#playPausingTest/Trace-1650046475193.txt
     * Method invocation under test: <fifthelement.theelement.business.services.SongListService: fifthelement.theelement.objects.Song getSongAtIndex(int)>_1366_2731
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_business_services_SongListService_getSongAtIndex_001() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        fifthelement.theelement.objects.Song song390 = null;
        fifthelement.theelement.objects.Author author323 = null;
        java.util.ArrayList arraylist122 = null;
        fifthelement.theelement.objects.Author author324 = null;
        fifthelement.theelement.objects.Song song391 = null;
        fifthelement.theelement.objects.Song song392 = null;
        fifthelement.theelement.objects.Author author325 = null;
        fifthelement.theelement.objects.Author author326 = null;
        fifthelement.theelement.objects.Song song393 = null;
        fifthelement.theelement.objects.Author author327 = null;
        fifthelement.theelement.objects.Author author328 = null;
        fifthelement.theelement.objects.Author author329 = null;
        fifthelement.theelement.objects.Album album174 = null;
        fifthelement.theelement.objects.Song song394 = null;
        fifthelement.theelement.objects.Song song395 = null;
        fifthelement.theelement.objects.Author author330 = null;
        fifthelement.theelement.objects.Album album175 = null;
        fifthelement.theelement.objects.Album album176 = null;
        fifthelement.theelement.objects.Song song396 = null;
        java.util.ArrayList arraylist123 = null;
        fifthelement.theelement.objects.Album album177 = null;
        java.util.ArrayList arraylist124 = null;
        fifthelement.theelement.objects.Author author331 = null;
        fifthelement.theelement.objects.Album album178 = null;
        fifthelement.theelement.objects.Song song397 = null;
        fifthelement.theelement.objects.Author author332 = null;
        fifthelement.theelement.objects.Song song398 = null;
        fifthelement.theelement.objects.Album album179 = null;
        fifthelement.theelement.business.services.SongListService songlistservice4 = null;
        fifthelement.theelement.objects.Author author333 = null;
        fifthelement.theelement.objects.Author author334 = null;
        fifthelement.theelement.objects.Album album180 = null;
        fifthelement.theelement.objects.Author author335 = null;
        fifthelement.theelement.objects.Song song399 = null;
        fifthelement.theelement.objects.Song song400 = null;
        fifthelement.theelement.objects.Song song401 = null;
        fifthelement.theelement.objects.Song song402 = null;
        fifthelement.theelement.objects.Author author336 = null;
        fifthelement.theelement.objects.Song song403 = null;
        fifthelement.theelement.objects.Author author337 = null;
        fifthelement.theelement.objects.Song song404 = null;
        java.util.List list8 = null;
        java.util.ArrayList arraylist125 = null;
        fifthelement.theelement.objects.Song song405 = null;
        fifthelement.theelement.objects.Author author338 = null;
        fifthelement.theelement.objects.Author author339 = null;
        songlistservice4 = new fifthelement.theelement.business.services.SongListService();
        arraylist123 = new java.util.ArrayList();
        java.util.UUID uuid1084 = java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid1085 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author328 = new fifthelement.theelement.objects.Author(uuid1085, "");
        song399 = new fifthelement.theelement.objects.Song(uuid1084, "This Is America", "android.resource://fifthelement.theelement/raw/childish_gambino_this_is_america", author328, album179, "Hiphop", 3, 1.5);
        arraylist123.add(song399);
        java.util.UUID uuid1086 = java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        song404 = new fifthelement.theelement.objects.Song(uuid1086, "Hall of Fame", "android.resource://fifthelement.theelement/raw/hall_of_fame", author335, album179, "", 3, 4.5);
        arraylist123.add(song404);
        java.util.UUID uuid1087 = java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        song394 = new fifthelement.theelement.objects.Song(uuid1087, "Classical Music", "android.resource://fifthelement.theelement/raw/classical_music", author335, album179, "Classical", 4, 2.0);
        arraylist123.add(song394);
        java.util.UUID uuid1088 = java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid1089 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author325 = new fifthelement.theelement.objects.Author(uuid1089, "");
        java.util.UUID uuid1090 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        album178 = new fifthelement.theelement.objects.Album(uuid1090, "");
        song405 = new fifthelement.theelement.objects.Song(uuid1088, "Adventure of a Lifetime", "android.resource://fifthelement.theelement/raw/coldplay_adventure_of_a_lifetime", author325, album178, "Pop", 10, 3.5);
        arraylist123.add(song405);
        arraylist123.iterator();
        java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        songlistservice4.setCurrentSongsList(arraylist123);
        arraylist124 = new java.util.ArrayList();
        java.util.UUID uuid1096 = java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid1097 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author327 = new fifthelement.theelement.objects.Author(uuid1097, "");
        song390 = new fifthelement.theelement.objects.Song(uuid1096, "This Is America", "android.resource://fifthelement.theelement/raw/childish_gambino_this_is_america", author327, album179, "Hiphop", 3, 1.5);
        arraylist124.add(song390);
        java.util.UUID uuid1098 = java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        song391 = new fifthelement.theelement.objects.Song(uuid1098, "Hall of Fame", "android.resource://fifthelement.theelement/raw/hall_of_fame", author335, album179, "", 3, 4.5);
        arraylist124.add(song391);
        java.util.UUID uuid1099 = java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        song396 = new fifthelement.theelement.objects.Song(uuid1099, "Classical Music", "android.resource://fifthelement.theelement/raw/classical_music", author335, album179, "Classical", 4, 2.0);
        arraylist124.add(song396);
        java.util.UUID uuid1100 = java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid1101 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author331 = new fifthelement.theelement.objects.Author(uuid1101, "");
        java.util.UUID uuid1102 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        album174 = new fifthelement.theelement.objects.Album(uuid1102, "");
        song393 = new fifthelement.theelement.objects.Song(uuid1100, "Adventure of a Lifetime", "android.resource://fifthelement.theelement/raw/coldplay_adventure_of_a_lifetime", author331, album174, "Pop", 10, 3.5);
        arraylist124.add(song393);
        arraylist124.iterator();
        song390.getAuthor();
        fifthelement.theelement.objects.Author author341 = song390.getAuthor();
        author341.getUUID();
        java.util.UUID uuid1104 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author329 = new fifthelement.theelement.objects.Author(uuid1104, "Childish Gambino", 3);
        song390.setAuthor(author329);
        song390.getAlbum();
        song391.getAuthor();
        song391.getAlbum();
        fifthelement.theelement.objects.Author author343 = song396.getAuthor();
        fifthelement.theelement.objects.Album album183 = song396.getAlbum();
        song393.getAuthor();
        fifthelement.theelement.objects.Author author345 = song393.getAuthor();
        author345.getUUID();
        java.util.UUID uuid1106 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author324 = new fifthelement.theelement.objects.Author(uuid1106, "Coldplay", 10);
        song393.setAuthor(author324);
        song393.getAlbum();
        fifthelement.theelement.objects.Album album185 = song393.getAlbum();
        album185.getUUID();
        java.util.UUID uuid1108 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID uuid1109 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author333 = new fifthelement.theelement.objects.Author(uuid1109, "");
        album175 = new fifthelement.theelement.objects.Album(uuid1108, "A Head Full of Dreams", author333, list8, 10);
        album175.getAuthor();
        fifthelement.theelement.objects.Author author347 = album175.getAuthor();
        author347.getUUID();
        java.util.UUID uuid1111 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author334 = new fifthelement.theelement.objects.Author(uuid1111, "Coldplay", 10);
        album175.setAuthor(author334);
        song393.setAlbum(album175);
        songlistservice4.sortSongs(arraylist124);
        arraylist124.size();
        arraylist124.size();
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
        arraylist125 = new java.util.ArrayList();
        java.util.UUID uuid1125 = java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid1126 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author338 = new fifthelement.theelement.objects.Author(uuid1126, "");
        song402 = new fifthelement.theelement.objects.Song(uuid1125, "This Is America", "android.resource://fifthelement.theelement/raw/childish_gambino_this_is_america", author338, album183, "Hiphop", 3, 1.5);
        arraylist125.add(song402);
        java.util.UUID uuid1127 = java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        song395 = new fifthelement.theelement.objects.Song(uuid1127, "Hall of Fame", "android.resource://fifthelement.theelement/raw/hall_of_fame", author343, album183, "", 3, 4.5);
        arraylist125.add(song395);
        java.util.UUID uuid1128 = java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        song403 = new fifthelement.theelement.objects.Song(uuid1128, "Classical Music", "android.resource://fifthelement.theelement/raw/classical_music", author343, album183, "Classical", 4, 2.0);
        arraylist125.add(song403);
        java.util.UUID uuid1129 = java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid1130 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author336 = new fifthelement.theelement.objects.Author(uuid1130, "");
        java.util.UUID uuid1131 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        album177 = new fifthelement.theelement.objects.Album(uuid1131, "");
        song397 = new fifthelement.theelement.objects.Song(uuid1129, "Adventure of a Lifetime", "android.resource://fifthelement.theelement/raw/coldplay_adventure_of_a_lifetime", author336, album177, "Pop", 10, 3.5);
        arraylist125.add(song397);
        arraylist125.iterator();
        song402.getAuthor();
        fifthelement.theelement.objects.Author author349 = song402.getAuthor();
        author349.getUUID();
        java.util.UUID uuid1133 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author339 = new fifthelement.theelement.objects.Author(uuid1133, "Childish Gambino", 3);
        song402.setAuthor(author339);
        song402.getAlbum();
        song395.getAuthor();
        song395.getAlbum();
        song403.getAuthor();
        fifthelement.theelement.objects.Album album188 = song403.getAlbum();
        song397.getAuthor();
        fifthelement.theelement.objects.Author author353 = song397.getAuthor();
        author353.getUUID();
        java.util.UUID uuid1135 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author332 = new fifthelement.theelement.objects.Author(uuid1135, "Coldplay", 10);
        song397.setAuthor(author332);
        song397.getAlbum();
        fifthelement.theelement.objects.Album album190 = song397.getAlbum();
        album190.getUUID();
        java.util.UUID uuid1137 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID uuid1138 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author326 = new fifthelement.theelement.objects.Author(uuid1138, "");
        album180 = new fifthelement.theelement.objects.Album(uuid1137, "A Head Full of Dreams", author326, list8, 10);
        album180.getAuthor();
        fifthelement.theelement.objects.Author author355 = album180.getAuthor();
        author355.getUUID();
        java.util.UUID uuid1140 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author337 = new fifthelement.theelement.objects.Author(uuid1140, "Coldplay", 10);
        album180.setAuthor(author337);
        song397.setAlbum(album180);
        songlistservice4.sortSongs(arraylist125);
        arraylist125.size();
        arraylist125.size();
        arraylist125.size();
        arraylist125.size();
        fifthelement.theelement.objects.Song song406 = (fifthelement.theelement.objects.Song) arraylist125.get(0);
        fifthelement.theelement.objects.Author author356 = song406.getAuthor();
        author356.getName();
        song406.getName();
        fifthelement.theelement.objects.Song song407 = (fifthelement.theelement.objects.Song) arraylist125.get(1);
        song407.getAuthor();
        song407.getName();
        fifthelement.theelement.objects.Song song408 = (fifthelement.theelement.objects.Song) arraylist125.get(2);
        song408.getAuthor();
        song408.getName();
        fifthelement.theelement.objects.Song song409 = (fifthelement.theelement.objects.Song) arraylist125.get(3);
        fifthelement.theelement.objects.Author author359 = song409.getAuthor();
        author359.getName();
        song409.getName();
        arraylist125.size();
        arraylist125.size();
        fifthelement.theelement.objects.Song song410 = (fifthelement.theelement.objects.Song) arraylist125.get(0);
        fifthelement.theelement.objects.Author author360 = song410.getAuthor();
        author360.getName();
        song410.getName();
        fifthelement.theelement.objects.Song song411 = (fifthelement.theelement.objects.Song) arraylist125.get(1);
        song411.getAuthor();
        song411.getName();
        fifthelement.theelement.objects.Song song412 = (fifthelement.theelement.objects.Song) arraylist125.get(2);
        song412.getAuthor();
        song412.getName();
        fifthelement.theelement.objects.Song song413 = (fifthelement.theelement.objects.Song) arraylist125.get(3);
        fifthelement.theelement.objects.Author author363 = song413.getAuthor();
        author363.getName();
        song413.getName();
        arraylist125.size();
        fifthelement.theelement.objects.Song song414 = (fifthelement.theelement.objects.Song) arraylist125.get(0);
        fifthelement.theelement.objects.Author author364 = song414.getAuthor();
        author364.getName();
        song414.getName();
        fifthelement.theelement.objects.Song song415 = (fifthelement.theelement.objects.Song) arraylist125.get(1);
        song415.getAuthor();
        song415.getName();
        fifthelement.theelement.objects.Song song416 = (fifthelement.theelement.objects.Song) arraylist125.get(2);
        song416.getAuthor();
        song416.getName();
        fifthelement.theelement.objects.Song song417 = (fifthelement.theelement.objects.Song) arraylist125.get(3);
        fifthelement.theelement.objects.Author author367 = song417.getAuthor();
        author367.getName();
        song417.getName();
        arraylist125.size();
        arraylist125.size();
        fifthelement.theelement.objects.Song song418 = (fifthelement.theelement.objects.Song) arraylist125.get(0);
        fifthelement.theelement.objects.Author author368 = song418.getAuthor();
        author368.getName();
        song418.getName();
        fifthelement.theelement.objects.Song song419 = (fifthelement.theelement.objects.Song) arraylist125.get(1);
        song419.getAuthor();
        song419.getName();
        fifthelement.theelement.objects.Song song420 = (fifthelement.theelement.objects.Song) arraylist125.get(2);
        song420.getAuthor();
        song420.getName();
        fifthelement.theelement.objects.Song song421 = (fifthelement.theelement.objects.Song) arraylist125.get(3);
        fifthelement.theelement.objects.Author author371 = song421.getAuthor();
        author371.getName();
        song421.getName();
        arraylist125.size();
        arraylist125.size();
        fifthelement.theelement.objects.Song song422 = (fifthelement.theelement.objects.Song) arraylist125.get(0);
        fifthelement.theelement.objects.Author author372 = song422.getAuthor();
        author372.getName();
        song422.getName();
        fifthelement.theelement.objects.Song song423 = (fifthelement.theelement.objects.Song) arraylist125.get(1);
        song423.getAuthor();
        song423.getName();
        fifthelement.theelement.objects.Song song424 = (fifthelement.theelement.objects.Song) arraylist125.get(2);
        song424.getAuthor();
        song424.getName();
        fifthelement.theelement.objects.Song song425 = (fifthelement.theelement.objects.Song) arraylist125.get(3);
        fifthelement.theelement.objects.Author author375 = song425.getAuthor();
        author375.getName();
        song425.getName();
        arraylist125.size();
        arraylist125.get(0);
        arraylist125.get(1);
        arraylist125.get(2);
        arraylist125.get(3);
        arraylist125.size();
        arraylist125.size();
        fifthelement.theelement.objects.Song song430 = (fifthelement.theelement.objects.Song) arraylist125.get(0);
        fifthelement.theelement.objects.Author author376 = song430.getAuthor();
        author376.getName();
        song430.getName();
        fifthelement.theelement.objects.Song song431 = (fifthelement.theelement.objects.Song) arraylist125.get(1);
        song431.getAuthor();
        song431.getName();
        fifthelement.theelement.objects.Song song432 = (fifthelement.theelement.objects.Song) arraylist125.get(2);
        song432.getAuthor();
        song432.getName();
        fifthelement.theelement.objects.Song song433 = (fifthelement.theelement.objects.Song) arraylist125.get(3);
        fifthelement.theelement.objects.Author author379 = song433.getAuthor();
        author379.getName();
        song433.getName();
        arraylist125.size();
        arraylist125.size();
        fifthelement.theelement.objects.Song song434 = (fifthelement.theelement.objects.Song) arraylist125.get(0);
        fifthelement.theelement.objects.Author author380 = song434.getAuthor();
        author380.getName();
        song434.getName();
        fifthelement.theelement.objects.Song song435 = (fifthelement.theelement.objects.Song) arraylist125.get(1);
        song435.getAuthor();
        song435.getName();
        fifthelement.theelement.objects.Song song436 = (fifthelement.theelement.objects.Song) arraylist125.get(2);
        fifthelement.theelement.objects.Author author382 = song436.getAuthor();
        song436.getName();
        fifthelement.theelement.objects.Song song437 = (fifthelement.theelement.objects.Song) arraylist125.get(3);
        fifthelement.theelement.objects.Author author383 = song437.getAuthor();
        author383.getName();
        song437.getName();
        arraylist125.size();
        arraylist125.get(0);
        arraylist125.get(1);
        arraylist125.get(2);
        arraylist125.get(3);
        arraylist125.get(0);
        arraylist125.size();
        arraylist125.size();
        arraylist125.get(0);
        arraylist122 = new java.util.ArrayList();
        java.util.UUID uuid1141 = java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid1142 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author330 = new fifthelement.theelement.objects.Author(uuid1142, "");
        song400 = new fifthelement.theelement.objects.Song(uuid1141, "This Is America", "android.resource://fifthelement.theelement/raw/childish_gambino_this_is_america", author330, album188, "Hiphop", 3, 1.5);
        arraylist122.add(song400);
        java.util.UUID uuid1143 = java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        song398 = new fifthelement.theelement.objects.Song(uuid1143, "Hall of Fame", "android.resource://fifthelement.theelement/raw/hall_of_fame", author382, album188, "", 3, 4.5);
        arraylist122.add(song398);
        java.util.UUID uuid1144 = java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        song401 = new fifthelement.theelement.objects.Song(uuid1144, "Classical Music", "android.resource://fifthelement.theelement/raw/classical_music", author382, album188, "Classical", 4, 2.0);
        arraylist122.add(song401);
        java.util.UUID uuid1145 = java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid1146 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author323 = new fifthelement.theelement.objects.Author(uuid1146, "");
        java.util.UUID uuid1147 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        album176 = new fifthelement.theelement.objects.Album(uuid1147, "");
        song392 = new fifthelement.theelement.objects.Song(uuid1145, "Adventure of a Lifetime", "android.resource://fifthelement.theelement/raw/coldplay_adventure_of_a_lifetime", author323, album176, "Pop", 10, 3.5);
        arraylist122.add(song392);
        arraylist122.iterator();
        songlistservice4.sortSongs(arraylist122);
        songlistservice4.setCurrentSongsList(arraylist122);
        songlistservice4.getSongAtIndex(0);
    }
}

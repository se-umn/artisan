package fifthelement.theelement.business.services;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

public class Test3575 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.PlayMusicTests#skipSongsTest/Trace-1650046483856.txt
     * Method invocation under test: <fifthelement.theelement.business.services.SongListService: fifthelement.theelement.objects.Song getSongAtIndex(int)>_2057_4112
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_business_services_SongListService_getSongAtIndex_003() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        fifthelement.theelement.objects.Song song11065 = null;
        fifthelement.theelement.objects.Author author10170 = null;
        java.util.ArrayList arraylist5498 = null;
        fifthelement.theelement.objects.Author author10171 = null;
        fifthelement.theelement.objects.Song song11066 = null;
        fifthelement.theelement.objects.Song song11067 = null;
        fifthelement.theelement.objects.Author author10172 = null;
        fifthelement.theelement.objects.Album album5028 = null;
        fifthelement.theelement.objects.Author author10173 = null;
        fifthelement.theelement.objects.Author author10174 = null;
        fifthelement.theelement.objects.Song song11068 = null;
        fifthelement.theelement.objects.Author author10175 = null;
        fifthelement.theelement.objects.Author author10176 = null;
        fifthelement.theelement.objects.Author author10177 = null;
        fifthelement.theelement.objects.Album album5029 = null;
        fifthelement.theelement.objects.Song song11069 = null;
        fifthelement.theelement.objects.Song song11070 = null;
        fifthelement.theelement.objects.Author author10178 = null;
        fifthelement.theelement.objects.Album album5030 = null;
        fifthelement.theelement.objects.Album album5031 = null;
        fifthelement.theelement.objects.Song song11071 = null;
        java.util.ArrayList arraylist5499 = null;
        fifthelement.theelement.objects.Author author10179 = null;
        fifthelement.theelement.objects.Album album5032 = null;
        java.util.ArrayList arraylist5500 = null;
        fifthelement.theelement.objects.Author author10180 = null;
        fifthelement.theelement.objects.Album album5033 = null;
        fifthelement.theelement.objects.Song song11072 = null;
        fifthelement.theelement.objects.Author author10181 = null;
        fifthelement.theelement.objects.Song song11073 = null;
        fifthelement.theelement.objects.Album album5034 = null;
        fifthelement.theelement.business.services.SongListService songlistservice473 = null;
        fifthelement.theelement.objects.Author author10182 = null;
        fifthelement.theelement.objects.Author author10183 = null;
        fifthelement.theelement.objects.Album album5035 = null;
        fifthelement.theelement.objects.Author author10184 = null;
        fifthelement.theelement.objects.Author author10185 = null;
        fifthelement.theelement.objects.Song song11074 = null;
        fifthelement.theelement.objects.Song song11075 = null;
        fifthelement.theelement.objects.Song song11076 = null;
        fifthelement.theelement.objects.Song song11077 = null;
        fifthelement.theelement.objects.Author author10186 = null;
        fifthelement.theelement.objects.Song song11078 = null;
        fifthelement.theelement.objects.Author author10187 = null;
        fifthelement.theelement.objects.Song song11079 = null;
        java.util.List list230 = null;
        fifthelement.theelement.objects.Author author10188 = null;
        java.util.ArrayList arraylist5501 = null;
        fifthelement.theelement.objects.Song song11080 = null;
        fifthelement.theelement.objects.Author author10189 = null;
        fifthelement.theelement.objects.Author author10190 = null;
        songlistservice473 = new fifthelement.theelement.business.services.SongListService();
        arraylist5499 = new java.util.ArrayList();
        java.util.UUID uuid71533 = java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid71534 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author10176 = new fifthelement.theelement.objects.Author(uuid71534, "");
        song11074 = new fifthelement.theelement.objects.Song(uuid71533, "This Is America", "android.resource://fifthelement.theelement/raw/childish_gambino_this_is_america", author10176, album5034, "Hiphop", 3, 1.5);
        arraylist5499.add(song11074);
        java.util.UUID uuid71535 = java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        song11079 = new fifthelement.theelement.objects.Song(uuid71535, "Hall of Fame", "android.resource://fifthelement.theelement/raw/hall_of_fame", author10184, album5034, "", 3, 4.5);
        arraylist5499.add(song11079);
        java.util.UUID uuid71536 = java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        song11069 = new fifthelement.theelement.objects.Song(uuid71536, "Classical Music", "android.resource://fifthelement.theelement/raw/classical_music", author10184, album5034, "Classical", 4, 2.0);
        arraylist5499.add(song11069);
        java.util.UUID uuid71537 = java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid71538 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author10172 = new fifthelement.theelement.objects.Author(uuid71538, "");
        java.util.UUID uuid71539 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        album5033 = new fifthelement.theelement.objects.Album(uuid71539, "");
        song11080 = new fifthelement.theelement.objects.Song(uuid71537, "Adventure of a Lifetime", "android.resource://fifthelement.theelement/raw/coldplay_adventure_of_a_lifetime", author10172, album5033, "Pop", 10, 3.5);
        arraylist5499.add(song11080);
        arraylist5499.iterator();
        java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        songlistservice473.setCurrentSongsList(arraylist5499);
        arraylist5500 = new java.util.ArrayList();
        java.util.UUID uuid71545 = java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid71546 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author10175 = new fifthelement.theelement.objects.Author(uuid71546, "");
        song11065 = new fifthelement.theelement.objects.Song(uuid71545, "This Is America", "android.resource://fifthelement.theelement/raw/childish_gambino_this_is_america", author10175, album5034, "Hiphop", 3, 1.5);
        arraylist5500.add(song11065);
        java.util.UUID uuid71547 = java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        song11066 = new fifthelement.theelement.objects.Song(uuid71547, "Hall of Fame", "android.resource://fifthelement.theelement/raw/hall_of_fame", author10184, album5034, "", 3, 4.5);
        arraylist5500.add(song11066);
        java.util.UUID uuid71548 = java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        song11071 = new fifthelement.theelement.objects.Song(uuid71548, "Classical Music", "android.resource://fifthelement.theelement/raw/classical_music", author10184, album5034, "Classical", 4, 2.0);
        arraylist5500.add(song11071);
        java.util.UUID uuid71549 = java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid71550 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author10180 = new fifthelement.theelement.objects.Author(uuid71550, "");
        java.util.UUID uuid71551 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        album5029 = new fifthelement.theelement.objects.Album(uuid71551, "");
        song11068 = new fifthelement.theelement.objects.Song(uuid71549, "Adventure of a Lifetime", "android.resource://fifthelement.theelement/raw/coldplay_adventure_of_a_lifetime", author10180, album5029, "Pop", 10, 3.5);
        arraylist5500.add(song11068);
        arraylist5500.iterator();
        song11065.getAuthor();
        fifthelement.theelement.objects.Author author10192 = song11065.getAuthor();
        author10192.getUUID();
        java.util.UUID uuid71553 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author10177 = new fifthelement.theelement.objects.Author(uuid71553, "Childish Gambino", 3);
        song11065.setAuthor(author10177);
        song11065.getAlbum();
        song11066.getAuthor();
        song11066.getAlbum();
        fifthelement.theelement.objects.Author author10194 = song11071.getAuthor();
        fifthelement.theelement.objects.Album album5038 = song11071.getAlbum();
        song11068.getAuthor();
        fifthelement.theelement.objects.Author author10196 = song11068.getAuthor();
        author10196.getUUID();
        java.util.UUID uuid71555 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author10171 = new fifthelement.theelement.objects.Author(uuid71555, "Coldplay", 10);
        song11068.setAuthor(author10171);
        song11068.getAlbum();
        fifthelement.theelement.objects.Album album5040 = song11068.getAlbum();
        album5040.getUUID();
        java.util.UUID uuid71557 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID uuid71558 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author10182 = new fifthelement.theelement.objects.Author(uuid71558, "");
        album5030 = new fifthelement.theelement.objects.Album(uuid71557, "A Head Full of Dreams", author10182, list230, 10);
        album5030.getAuthor();
        fifthelement.theelement.objects.Author author10198 = album5030.getAuthor();
        author10198.getUUID();
        java.util.UUID uuid71560 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author10183 = new fifthelement.theelement.objects.Author(uuid71560, "Coldplay", 10);
        album5030.setAuthor(author10183);
        song11068.setAlbum(album5030);
        songlistservice473.sortSongs(arraylist5500);
        arraylist5500.size();
        arraylist5500.size();
        songlistservice473.getAutoplayEnabled();
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
        arraylist5501 = new java.util.ArrayList();
        java.util.UUID uuid71574 = java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid71575 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author10189 = new fifthelement.theelement.objects.Author(uuid71575, "");
        song11077 = new fifthelement.theelement.objects.Song(uuid71574, "This Is America", "android.resource://fifthelement.theelement/raw/childish_gambino_this_is_america", author10189, album5038, "Hiphop", 3, 1.5);
        arraylist5501.add(song11077);
        java.util.UUID uuid71576 = java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        song11070 = new fifthelement.theelement.objects.Song(uuid71576, "Hall of Fame", "android.resource://fifthelement.theelement/raw/hall_of_fame", author10194, album5038, "", 3, 4.5);
        arraylist5501.add(song11070);
        java.util.UUID uuid71577 = java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        song11078 = new fifthelement.theelement.objects.Song(uuid71577, "Classical Music", "android.resource://fifthelement.theelement/raw/classical_music", author10194, album5038, "Classical", 4, 2.0);
        arraylist5501.add(song11078);
        java.util.UUID uuid71578 = java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid71579 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author10186 = new fifthelement.theelement.objects.Author(uuid71579, "");
        java.util.UUID uuid71580 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        album5032 = new fifthelement.theelement.objects.Album(uuid71580, "");
        song11072 = new fifthelement.theelement.objects.Song(uuid71578, "Adventure of a Lifetime", "android.resource://fifthelement.theelement/raw/coldplay_adventure_of_a_lifetime", author10186, album5032, "Pop", 10, 3.5);
        arraylist5501.add(song11072);
        arraylist5501.iterator();
        song11077.getAuthor();
        fifthelement.theelement.objects.Author author10200 = song11077.getAuthor();
        author10200.getUUID();
        java.util.UUID uuid71582 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author10190 = new fifthelement.theelement.objects.Author(uuid71582, "Childish Gambino", 3);
        song11077.setAuthor(author10190);
        song11077.getAlbum();
        song11070.getAuthor();
        song11070.getAlbum();
        song11078.getAuthor();
        fifthelement.theelement.objects.Album album5043 = song11078.getAlbum();
        song11072.getAuthor();
        fifthelement.theelement.objects.Author author10204 = song11072.getAuthor();
        author10204.getUUID();
        java.util.UUID uuid71584 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author10181 = new fifthelement.theelement.objects.Author(uuid71584, "Coldplay", 10);
        song11072.setAuthor(author10181);
        song11072.getAlbum();
        fifthelement.theelement.objects.Album album5045 = song11072.getAlbum();
        album5045.getUUID();
        java.util.UUID uuid71586 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID uuid71587 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author10173 = new fifthelement.theelement.objects.Author(uuid71587, "");
        album5035 = new fifthelement.theelement.objects.Album(uuid71586, "A Head Full of Dreams", author10173, list230, 10);
        album5035.getAuthor();
        fifthelement.theelement.objects.Author author10206 = album5035.getAuthor();
        author10206.getUUID();
        java.util.UUID uuid71589 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author10187 = new fifthelement.theelement.objects.Author(uuid71589, "Coldplay", 10);
        album5035.setAuthor(author10187);
        song11072.setAlbum(album5035);
        songlistservice473.sortSongs(arraylist5501);
        arraylist5501.size();
        arraylist5501.size();
        arraylist5501.size();
        arraylist5501.size();
        fifthelement.theelement.objects.Song song11081 = (fifthelement.theelement.objects.Song) arraylist5501.get(0);
        fifthelement.theelement.objects.Author author10207 = song11081.getAuthor();
        author10207.getName();
        song11081.getName();
        fifthelement.theelement.objects.Song song11082 = (fifthelement.theelement.objects.Song) arraylist5501.get(1);
        song11082.getAuthor();
        song11082.getName();
        fifthelement.theelement.objects.Song song11083 = (fifthelement.theelement.objects.Song) arraylist5501.get(2);
        song11083.getAuthor();
        song11083.getName();
        fifthelement.theelement.objects.Song song11084 = (fifthelement.theelement.objects.Song) arraylist5501.get(3);
        fifthelement.theelement.objects.Author author10210 = song11084.getAuthor();
        author10210.getName();
        song11084.getName();
        arraylist5501.size();
        arraylist5501.size();
        fifthelement.theelement.objects.Song song11085 = (fifthelement.theelement.objects.Song) arraylist5501.get(0);
        fifthelement.theelement.objects.Author author10211 = song11085.getAuthor();
        author10211.getName();
        song11085.getName();
        fifthelement.theelement.objects.Song song11086 = (fifthelement.theelement.objects.Song) arraylist5501.get(1);
        song11086.getAuthor();
        song11086.getName();
        fifthelement.theelement.objects.Song song11087 = (fifthelement.theelement.objects.Song) arraylist5501.get(2);
        song11087.getAuthor();
        song11087.getName();
        fifthelement.theelement.objects.Song song11088 = (fifthelement.theelement.objects.Song) arraylist5501.get(3);
        fifthelement.theelement.objects.Author author10214 = song11088.getAuthor();
        author10214.getName();
        song11088.getName();
        arraylist5501.size();
        fifthelement.theelement.objects.Song song11089 = (fifthelement.theelement.objects.Song) arraylist5501.get(0);
        fifthelement.theelement.objects.Author author10215 = song11089.getAuthor();
        author10215.getName();
        song11089.getName();
        fifthelement.theelement.objects.Song song11090 = (fifthelement.theelement.objects.Song) arraylist5501.get(1);
        song11090.getAuthor();
        song11090.getName();
        fifthelement.theelement.objects.Song song11091 = (fifthelement.theelement.objects.Song) arraylist5501.get(2);
        song11091.getAuthor();
        song11091.getName();
        fifthelement.theelement.objects.Song song11092 = (fifthelement.theelement.objects.Song) arraylist5501.get(3);
        fifthelement.theelement.objects.Author author10218 = song11092.getAuthor();
        author10218.getName();
        song11092.getName();
        arraylist5501.size();
        arraylist5501.size();
        fifthelement.theelement.objects.Song song11093 = (fifthelement.theelement.objects.Song) arraylist5501.get(0);
        fifthelement.theelement.objects.Author author10219 = song11093.getAuthor();
        author10219.getName();
        song11093.getName();
        fifthelement.theelement.objects.Song song11094 = (fifthelement.theelement.objects.Song) arraylist5501.get(1);
        song11094.getAuthor();
        song11094.getName();
        fifthelement.theelement.objects.Song song11095 = (fifthelement.theelement.objects.Song) arraylist5501.get(2);
        song11095.getAuthor();
        song11095.getName();
        fifthelement.theelement.objects.Song song11096 = (fifthelement.theelement.objects.Song) arraylist5501.get(3);
        fifthelement.theelement.objects.Author author10222 = song11096.getAuthor();
        author10222.getName();
        song11096.getName();
        arraylist5501.size();
        arraylist5501.size();
        fifthelement.theelement.objects.Song song11097 = (fifthelement.theelement.objects.Song) arraylist5501.get(0);
        fifthelement.theelement.objects.Author author10223 = song11097.getAuthor();
        author10223.getName();
        song11097.getName();
        fifthelement.theelement.objects.Song song11098 = (fifthelement.theelement.objects.Song) arraylist5501.get(1);
        song11098.getAuthor();
        song11098.getName();
        fifthelement.theelement.objects.Song song11099 = (fifthelement.theelement.objects.Song) arraylist5501.get(2);
        song11099.getAuthor();
        song11099.getName();
        fifthelement.theelement.objects.Song song11100 = (fifthelement.theelement.objects.Song) arraylist5501.get(3);
        fifthelement.theelement.objects.Author author10226 = song11100.getAuthor();
        author10226.getName();
        song11100.getName();
        arraylist5501.size();
        arraylist5501.get(0);
        arraylist5501.get(1);
        arraylist5501.get(2);
        arraylist5501.get(3);
        arraylist5501.size();
        arraylist5501.size();
        fifthelement.theelement.objects.Song song11105 = (fifthelement.theelement.objects.Song) arraylist5501.get(0);
        fifthelement.theelement.objects.Author author10227 = song11105.getAuthor();
        author10227.getName();
        song11105.getName();
        fifthelement.theelement.objects.Song song11106 = (fifthelement.theelement.objects.Song) arraylist5501.get(1);
        song11106.getAuthor();
        song11106.getName();
        fifthelement.theelement.objects.Song song11107 = (fifthelement.theelement.objects.Song) arraylist5501.get(2);
        song11107.getAuthor();
        song11107.getName();
        fifthelement.theelement.objects.Song song11108 = (fifthelement.theelement.objects.Song) arraylist5501.get(3);
        fifthelement.theelement.objects.Author author10230 = song11108.getAuthor();
        author10230.getName();
        song11108.getName();
        arraylist5501.size();
        arraylist5501.size();
        fifthelement.theelement.objects.Song song11109 = (fifthelement.theelement.objects.Song) arraylist5501.get(0);
        fifthelement.theelement.objects.Author author10231 = song11109.getAuthor();
        author10231.getName();
        song11109.getName();
        fifthelement.theelement.objects.Song song11110 = (fifthelement.theelement.objects.Song) arraylist5501.get(1);
        song11110.getAuthor();
        song11110.getName();
        fifthelement.theelement.objects.Song song11111 = (fifthelement.theelement.objects.Song) arraylist5501.get(2);
        fifthelement.theelement.objects.Author author10233 = song11111.getAuthor();
        song11111.getName();
        fifthelement.theelement.objects.Song song11112 = (fifthelement.theelement.objects.Song) arraylist5501.get(3);
        fifthelement.theelement.objects.Author author10234 = song11112.getAuthor();
        author10234.getName();
        song11112.getName();
        arraylist5501.size();
        arraylist5501.get(0);
        arraylist5501.get(1);
        arraylist5501.get(2);
        arraylist5501.get(3);
        arraylist5501.get(0);
        arraylist5501.size();
        arraylist5501.size();
        arraylist5501.get(0);
        arraylist5498 = new java.util.ArrayList();
        java.util.UUID uuid71590 = java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid71591 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author10178 = new fifthelement.theelement.objects.Author(uuid71591, "");
        song11075 = new fifthelement.theelement.objects.Song(uuid71590, "This Is America", "android.resource://fifthelement.theelement/raw/childish_gambino_this_is_america", author10178, album5043, "Hiphop", 3, 1.5);
        arraylist5498.add(song11075);
        java.util.UUID uuid71592 = java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        song11073 = new fifthelement.theelement.objects.Song(uuid71592, "Hall of Fame", "android.resource://fifthelement.theelement/raw/hall_of_fame", author10233, album5043, "", 3, 4.5);
        arraylist5498.add(song11073);
        java.util.UUID uuid71593 = java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        song11076 = new fifthelement.theelement.objects.Song(uuid71593, "Classical Music", "android.resource://fifthelement.theelement/raw/classical_music", author10233, album5043, "Classical", 4, 2.0);
        arraylist5498.add(song11076);
        java.util.UUID uuid71594 = java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid71595 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author10170 = new fifthelement.theelement.objects.Author(uuid71595, "");
        java.util.UUID uuid71596 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        album5031 = new fifthelement.theelement.objects.Album(uuid71596, "");
        song11067 = new fifthelement.theelement.objects.Song(uuid71594, "Adventure of a Lifetime", "android.resource://fifthelement.theelement/raw/coldplay_adventure_of_a_lifetime", author10170, album5031, "Pop", 10, 3.5);
        arraylist5498.add(song11067);
        arraylist5498.iterator();
        song11075.getAuthor();
        fifthelement.theelement.objects.Author author10236 = song11075.getAuthor();
        author10236.getUUID();
        java.util.UUID uuid71598 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author10174 = new fifthelement.theelement.objects.Author(uuid71598, "Childish Gambino", 3);
        song11075.setAuthor(author10174);
        song11075.getAlbum();
        song11073.getAuthor();
        song11073.getAlbum();
        song11076.getAuthor();
        song11076.getAlbum();
        song11067.getAuthor();
        fifthelement.theelement.objects.Author author10240 = song11067.getAuthor();
        author10240.getUUID();
        java.util.UUID uuid71600 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author10185 = new fifthelement.theelement.objects.Author(uuid71600, "Coldplay", 10);
        song11067.setAuthor(author10185);
        song11067.getAlbum();
        fifthelement.theelement.objects.Album album5050 = song11067.getAlbum();
        album5050.getUUID();
        java.util.UUID uuid71602 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID uuid71603 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author10179 = new fifthelement.theelement.objects.Author(uuid71603, "");
        album5028 = new fifthelement.theelement.objects.Album(uuid71602, "A Head Full of Dreams", author10179, list230, 10);
        album5028.getAuthor();
        fifthelement.theelement.objects.Author author10242 = album5028.getAuthor();
        author10242.getUUID();
        java.util.UUID uuid71605 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author10188 = new fifthelement.theelement.objects.Author(uuid71605, "Coldplay", 10);
        album5028.setAuthor(author10188);
        song11067.setAlbum(album5028);
        songlistservice473.sortSongs(arraylist5498);
        songlistservice473.setCurrentSongsList(arraylist5498);
        fifthelement.theelement.objects.Song song11119 = songlistservice473.getSongAtIndex(0);
        song11119.getPath();
        songlistservice473.setShuffleEnabled(false);
        java.util.UUID uuid71606 = song11119.getUUID();
        uuid71606.toString();
        song11119.getName();
        song11119.getPath();
        song11119.getPath();
        song11119.getPath();
        song11119.getPath();
        song11119.getName();
        song11119.getName();
        song11119.getPath();
        song11119.getPath();
        song11119.getPath();
        song11119.getPath();
        song11119.getAuthor();
        fifthelement.theelement.objects.Author author10244 = song11119.getAuthor();
        author10244.getName();
        fifthelement.theelement.objects.Author author10245 = song11119.getAuthor();
        author10245.getName();
        song11119.getAlbum();
        fifthelement.theelement.objects.Album album5052 = song11119.getAlbum();
        album5052.getName();
        songlistservice473.getCurrentSongPlayingIndex();
        fifthelement.theelement.objects.Song song11120 = songlistservice473.getSongAtIndex(0);
        song11120.getUUID();
        songlistservice473.skipToNextSong();
        songlistservice473.getCurrentSongPlayingIndex();
        songlistservice473.getSongAtIndex(1);
    }
}

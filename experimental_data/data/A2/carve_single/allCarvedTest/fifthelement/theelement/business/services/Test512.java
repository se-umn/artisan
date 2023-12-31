package fifthelement.theelement.business.services;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

public class Test512 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.MusicLibraryTests#deleteSongTest/Trace-1650046535485.txt
     * Method invocation under test: <fifthelement.theelement.business.services.SongListService: void removeSongFromList(fifthelement.theelement.objects.Song)>_1184_2366
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_business_services_SongListService_removeSongFromList_001() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        fifthelement.theelement.objects.Song song303 = null;
        java.util.ArrayList arraylist90 = null;
        fifthelement.theelement.objects.Song song304 = null;
        fifthelement.theelement.objects.Album album137 = null;
        fifthelement.theelement.objects.Album album138 = null;
        fifthelement.theelement.objects.Author author323 = null;
        fifthelement.theelement.objects.Author author324 = null;
        fifthelement.theelement.objects.Author author325 = null;
        fifthelement.theelement.objects.Author author326 = null;
        java.util.ArrayList arraylist91 = null;
        fifthelement.theelement.objects.Author author327 = null;
        fifthelement.theelement.objects.Album album139 = null;
        fifthelement.theelement.objects.Album album140 = null;
        fifthelement.theelement.business.services.SongListService songlistservice4 = null;
        fifthelement.theelement.objects.Song song305 = null;
        fifthelement.theelement.objects.Author author328 = null;
        fifthelement.theelement.objects.Author author329 = null;
        fifthelement.theelement.objects.Author author330 = null;
        fifthelement.theelement.objects.Author author331 = null;
        fifthelement.theelement.objects.Author author332 = null;
        fifthelement.theelement.objects.Song song306 = null;
        fifthelement.theelement.objects.Album album141 = null;
        fifthelement.theelement.objects.Song song307 = null;
        fifthelement.theelement.objects.Author author333 = null;
        fifthelement.theelement.objects.Song song308 = null;
        fifthelement.theelement.objects.Song song309 = null;
        fifthelement.theelement.objects.Author author334 = null;
        fifthelement.theelement.objects.Song song310 = null;
        fifthelement.theelement.objects.Song song311 = null;
        java.util.ArrayList arraylist92 = null;
        fifthelement.theelement.objects.Song song312 = null;
        fifthelement.theelement.objects.Author author335 = null;
        fifthelement.theelement.objects.Song song313 = null;
        java.util.List list9 = null;
        fifthelement.theelement.objects.Author author336 = null;
        fifthelement.theelement.objects.Song song314 = null;
        fifthelement.theelement.objects.Album album142 = null;
        fifthelement.theelement.objects.Author author337 = null;
        songlistservice4 = new fifthelement.theelement.business.services.SongListService();
        arraylist90 = new java.util.ArrayList();
        java.util.UUID uuid1586 = java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid1587 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author332 = new fifthelement.theelement.objects.Author(uuid1587, "");
        song308 = new fifthelement.theelement.objects.Song(uuid1586, "This Is America", "android.resource://fifthelement.theelement/raw/childish_gambino_this_is_america", author332, album140, "Hiphop", 3, 1.5);
        arraylist90.add(song308);
        java.util.UUID uuid1588 = java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        song312 = new fifthelement.theelement.objects.Song(uuid1588, "Hall of Fame", "android.resource://fifthelement.theelement/raw/hall_of_fame", author330, album140, "", 3, 4.5);
        arraylist90.add(song312);
        java.util.UUID uuid1589 = java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        song311 = new fifthelement.theelement.objects.Song(uuid1589, "Classical Music", "android.resource://fifthelement.theelement/raw/classical_music", author330, album140, "Classical", 4, 2.0);
        arraylist90.add(song311);
        java.util.UUID uuid1590 = java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid1591 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author327 = new fifthelement.theelement.objects.Author(uuid1591, "");
        java.util.UUID uuid1592 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        album137 = new fifthelement.theelement.objects.Album(uuid1592, "");
        song314 = new fifthelement.theelement.objects.Song(uuid1590, "Adventure of a Lifetime", "android.resource://fifthelement.theelement/raw/coldplay_adventure_of_a_lifetime", author327, album137, "Pop", 10, 3.5);
        arraylist90.add(song314);
        arraylist90.iterator();
        java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        songlistservice4.setCurrentSongsList(arraylist90);
        arraylist92 = new java.util.ArrayList();
        java.util.UUID uuid1598 = java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid1599 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author335 = new fifthelement.theelement.objects.Author(uuid1599, "");
        song303 = new fifthelement.theelement.objects.Song(uuid1598, "This Is America", "android.resource://fifthelement.theelement/raw/childish_gambino_this_is_america", author335, album140, "Hiphop", 3, 1.5);
        arraylist92.add(song303);
        java.util.UUID uuid1600 = java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        song306 = new fifthelement.theelement.objects.Song(uuid1600, "Hall of Fame", "android.resource://fifthelement.theelement/raw/hall_of_fame", author330, album140, "", 3, 4.5);
        arraylist92.add(song306);
        java.util.UUID uuid1601 = java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        song309 = new fifthelement.theelement.objects.Song(uuid1601, "Classical Music", "android.resource://fifthelement.theelement/raw/classical_music", author330, album140, "Classical", 4, 2.0);
        arraylist92.add(song309);
        java.util.UUID uuid1602 = java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid1603 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author333 = new fifthelement.theelement.objects.Author(uuid1603, "");
        java.util.UUID uuid1604 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        album142 = new fifthelement.theelement.objects.Album(uuid1604, "");
        song310 = new fifthelement.theelement.objects.Song(uuid1602, "Adventure of a Lifetime", "android.resource://fifthelement.theelement/raw/coldplay_adventure_of_a_lifetime", author333, album142, "Pop", 10, 3.5);
        arraylist92.add(song310);
        arraylist92.iterator();
        song303.getAuthor();
        fifthelement.theelement.objects.Author author339 = song303.getAuthor();
        author339.getUUID();
        java.util.UUID uuid1606 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author334 = new fifthelement.theelement.objects.Author(uuid1606, "Childish Gambino", 3);
        song303.setAuthor(author334);
        song303.getAlbum();
        song306.getAuthor();
        song306.getAlbum();
        fifthelement.theelement.objects.Author author341 = song309.getAuthor();
        fifthelement.theelement.objects.Album album145 = song309.getAlbum();
        song310.getAuthor();
        fifthelement.theelement.objects.Author author343 = song310.getAuthor();
        author343.getUUID();
        java.util.UUID uuid1608 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author329 = new fifthelement.theelement.objects.Author(uuid1608, "Coldplay", 10);
        song310.setAuthor(author329);
        song310.getAlbum();
        fifthelement.theelement.objects.Album album147 = song310.getAlbum();
        album147.getUUID();
        java.util.UUID uuid1610 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID uuid1611 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author326 = new fifthelement.theelement.objects.Author(uuid1611, "");
        album141 = new fifthelement.theelement.objects.Album(uuid1610, "A Head Full of Dreams", author326, list9, 10);
        album141.getAuthor();
        fifthelement.theelement.objects.Author author345 = album141.getAuthor();
        author345.getUUID();
        java.util.UUID uuid1613 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author323 = new fifthelement.theelement.objects.Author(uuid1613, "Coldplay", 10);
        album141.setAuthor(author323);
        song310.setAlbum(album141);
        songlistservice4.sortSongs(arraylist92);
        arraylist92.size();
        arraylist92.size();
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
        arraylist91 = new java.util.ArrayList();
        java.util.UUID uuid1627 = java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid1628 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author325 = new fifthelement.theelement.objects.Author(uuid1628, "");
        song313 = new fifthelement.theelement.objects.Song(uuid1627, "This Is America", "android.resource://fifthelement.theelement/raw/childish_gambino_this_is_america", author325, album145, "Hiphop", 3, 1.5);
        arraylist91.add(song313);
        java.util.UUID uuid1629 = java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        song304 = new fifthelement.theelement.objects.Song(uuid1629, "Hall of Fame", "android.resource://fifthelement.theelement/raw/hall_of_fame", author341, album145, "", 3, 4.5);
        arraylist91.add(song304);
        java.util.UUID uuid1630 = java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        song307 = new fifthelement.theelement.objects.Song(uuid1630, "Classical Music", "android.resource://fifthelement.theelement/raw/classical_music", author341, album145, "Classical", 4, 2.0);
        arraylist91.add(song307);
        java.util.UUID uuid1631 = java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid1632 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author331 = new fifthelement.theelement.objects.Author(uuid1632, "");
        java.util.UUID uuid1633 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        album139 = new fifthelement.theelement.objects.Album(uuid1633, "");
        song305 = new fifthelement.theelement.objects.Song(uuid1631, "Adventure of a Lifetime", "android.resource://fifthelement.theelement/raw/coldplay_adventure_of_a_lifetime", author331, album139, "Pop", 10, 3.5);
        arraylist91.add(song305);
        arraylist91.iterator();
        song313.getAuthor();
        fifthelement.theelement.objects.Author author347 = song313.getAuthor();
        author347.getUUID();
        java.util.UUID uuid1635 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author336 = new fifthelement.theelement.objects.Author(uuid1635, "Childish Gambino", 3);
        song313.setAuthor(author336);
        song313.getAlbum();
        song304.getAuthor();
        song304.getAlbum();
        song307.getAuthor();
        song307.getAlbum();
        song305.getAuthor();
        fifthelement.theelement.objects.Author author351 = song305.getAuthor();
        author351.getUUID();
        java.util.UUID uuid1637 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author328 = new fifthelement.theelement.objects.Author(uuid1637, "Coldplay", 10);
        song305.setAuthor(author328);
        song305.getAlbum();
        fifthelement.theelement.objects.Album album152 = song305.getAlbum();
        album152.getUUID();
        java.util.UUID uuid1639 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID uuid1640 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author324 = new fifthelement.theelement.objects.Author(uuid1640, "");
        album138 = new fifthelement.theelement.objects.Album(uuid1639, "A Head Full of Dreams", author324, list9, 10);
        album138.getAuthor();
        fifthelement.theelement.objects.Author author353 = album138.getAuthor();
        author353.getUUID();
        java.util.UUID uuid1642 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author337 = new fifthelement.theelement.objects.Author(uuid1642, "Coldplay", 10);
        album138.setAuthor(author337);
        song305.setAlbum(album138);
        songlistservice4.sortSongs(arraylist91);
        arraylist91.size();
        arraylist91.size();
        arraylist91.size();
        arraylist91.size();
        fifthelement.theelement.objects.Song song315 = (fifthelement.theelement.objects.Song) arraylist91.get(0);
        fifthelement.theelement.objects.Author author354 = song315.getAuthor();
        author354.getName();
        song315.getName();
        fifthelement.theelement.objects.Song song316 = (fifthelement.theelement.objects.Song) arraylist91.get(1);
        song316.getAuthor();
        song316.getName();
        fifthelement.theelement.objects.Song song317 = (fifthelement.theelement.objects.Song) arraylist91.get(2);
        song317.getAuthor();
        song317.getName();
        fifthelement.theelement.objects.Song song318 = (fifthelement.theelement.objects.Song) arraylist91.get(3);
        fifthelement.theelement.objects.Author author357 = song318.getAuthor();
        author357.getName();
        song318.getName();
        arraylist91.size();
        arraylist91.size();
        fifthelement.theelement.objects.Song song319 = (fifthelement.theelement.objects.Song) arraylist91.get(0);
        fifthelement.theelement.objects.Author author358 = song319.getAuthor();
        author358.getName();
        song319.getName();
        fifthelement.theelement.objects.Song song320 = (fifthelement.theelement.objects.Song) arraylist91.get(1);
        song320.getAuthor();
        song320.getName();
        fifthelement.theelement.objects.Song song321 = (fifthelement.theelement.objects.Song) arraylist91.get(2);
        song321.getAuthor();
        song321.getName();
        fifthelement.theelement.objects.Song song322 = (fifthelement.theelement.objects.Song) arraylist91.get(3);
        fifthelement.theelement.objects.Author author361 = song322.getAuthor();
        author361.getName();
        song322.getName();
        arraylist91.size();
        fifthelement.theelement.objects.Song song323 = (fifthelement.theelement.objects.Song) arraylist91.get(0);
        fifthelement.theelement.objects.Author author362 = song323.getAuthor();
        author362.getName();
        song323.getName();
        fifthelement.theelement.objects.Song song324 = (fifthelement.theelement.objects.Song) arraylist91.get(1);
        song324.getAuthor();
        song324.getName();
        fifthelement.theelement.objects.Song song325 = (fifthelement.theelement.objects.Song) arraylist91.get(2);
        song325.getAuthor();
        song325.getName();
        fifthelement.theelement.objects.Song song326 = (fifthelement.theelement.objects.Song) arraylist91.get(3);
        fifthelement.theelement.objects.Author author365 = song326.getAuthor();
        author365.getName();
        song326.getName();
        arraylist91.size();
        arraylist91.size();
        fifthelement.theelement.objects.Song song327 = (fifthelement.theelement.objects.Song) arraylist91.get(0);
        fifthelement.theelement.objects.Author author366 = song327.getAuthor();
        author366.getName();
        song327.getName();
        fifthelement.theelement.objects.Song song328 = (fifthelement.theelement.objects.Song) arraylist91.get(1);
        song328.getAuthor();
        song328.getName();
        fifthelement.theelement.objects.Song song329 = (fifthelement.theelement.objects.Song) arraylist91.get(2);
        song329.getAuthor();
        song329.getName();
        fifthelement.theelement.objects.Song song330 = (fifthelement.theelement.objects.Song) arraylist91.get(3);
        fifthelement.theelement.objects.Author author369 = song330.getAuthor();
        author369.getName();
        song330.getName();
        arraylist91.size();
        arraylist91.size();
        fifthelement.theelement.objects.Song song331 = (fifthelement.theelement.objects.Song) arraylist91.get(0);
        fifthelement.theelement.objects.Author author370 = song331.getAuthor();
        author370.getName();
        song331.getName();
        fifthelement.theelement.objects.Song song332 = (fifthelement.theelement.objects.Song) arraylist91.get(1);
        song332.getAuthor();
        song332.getName();
        fifthelement.theelement.objects.Song song333 = (fifthelement.theelement.objects.Song) arraylist91.get(2);
        song333.getAuthor();
        song333.getName();
        fifthelement.theelement.objects.Song song334 = (fifthelement.theelement.objects.Song) arraylist91.get(3);
        fifthelement.theelement.objects.Author author373 = song334.getAuthor();
        author373.getName();
        song334.getName();
        arraylist91.size();
        song331.getName();
        java.util.UUID uuid1643 = song331.getUUID();
        uuid1643.toString();
        arraylist91.remove(song331);
        songlistservice4.removeSongFromList(song331);
    }
}

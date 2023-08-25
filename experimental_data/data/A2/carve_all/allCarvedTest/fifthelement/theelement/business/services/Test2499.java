package fifthelement.theelement.business.services;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

public class Test2499 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.SettingsTest#skipSongWithSongPlayedCheck/Trace-1650046455133.txt
     * Method invocation under test: <fifthelement.theelement.business.services.SongListService: int getCurrentSongPlayingIndex()>_2416_4830
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_business_services_SongListService_getCurrentSongPlayingIndex_001() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        fifthelement.theelement.objects.Album album3909 = null;
        fifthelement.theelement.objects.Author author8628 = null;
        fifthelement.theelement.objects.Song song7478 = null;
        fifthelement.theelement.objects.Song song7479 = null;
        fifthelement.theelement.objects.Author author8629 = null;
        fifthelement.theelement.objects.Author author8630 = null;
        fifthelement.theelement.objects.Song song7480 = null;
        fifthelement.theelement.objects.Album album3910 = null;
        fifthelement.theelement.objects.Author author8631 = null;
        fifthelement.theelement.objects.Song song7481 = null;
        fifthelement.theelement.objects.Song song7482 = null;
        fifthelement.theelement.objects.Song song7483 = null;
        fifthelement.theelement.objects.Album album3911 = null;
        java.util.ArrayList arraylist2677 = null;
        fifthelement.theelement.objects.Author author8632 = null;
        fifthelement.theelement.objects.Author author8633 = null;
        java.util.ArrayList arraylist2678 = null;
        fifthelement.theelement.objects.Author author8634 = null;
        fifthelement.theelement.objects.Song song7484 = null;
        fifthelement.theelement.objects.Author author8635 = null;
        fifthelement.theelement.objects.Song song7485 = null;
        fifthelement.theelement.objects.Author author8636 = null;
        fifthelement.theelement.objects.Author author8637 = null;
        fifthelement.theelement.objects.Author author8638 = null;
        fifthelement.theelement.objects.Album album3912 = null;
        fifthelement.theelement.objects.Song song7486 = null;
        java.util.ArrayList arraylist2679 = null;
        fifthelement.theelement.objects.Author author8639 = null;
        fifthelement.theelement.objects.Song song7487 = null;
        java.util.ArrayList arraylist2680 = null;
        fifthelement.theelement.objects.Song song7488 = null;
        fifthelement.theelement.objects.Author author8640 = null;
        fifthelement.theelement.objects.Author author8641 = null;
        fifthelement.theelement.objects.Song song7489 = null;
        fifthelement.theelement.objects.Album album3913 = null;
        fifthelement.theelement.objects.Author author8642 = null;
        fifthelement.theelement.business.services.SongListService songlistservice9 = null;
        fifthelement.theelement.objects.Album album3914 = null;
        fifthelement.theelement.objects.Album album3915 = null;
        fifthelement.theelement.objects.Song song7490 = null;
        fifthelement.theelement.objects.Author author8643 = null;
        fifthelement.theelement.objects.Author author8644 = null;
        fifthelement.theelement.objects.Author author8645 = null;
        fifthelement.theelement.objects.Author author8646 = null;
        fifthelement.theelement.objects.Song song7491 = null;
        fifthelement.theelement.objects.Author author8647 = null;
        fifthelement.theelement.objects.Song song7492 = null;
        java.util.List list220 = null;
        fifthelement.theelement.objects.Song song7493 = null;
        fifthelement.theelement.objects.Author author8648 = null;
        fifthelement.theelement.objects.Album album3916 = null;
        songlistservice9 = new fifthelement.theelement.business.services.SongListService();
        arraylist2679 = new java.util.ArrayList();
        java.util.UUID uuid116008 = java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid116009 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author8633 = new fifthelement.theelement.objects.Author(uuid116009, "");
        song7484 = new fifthelement.theelement.objects.Song(uuid116008, "This Is America", "android.resource://fifthelement.theelement/raw/childish_gambino_this_is_america", author8633, album3914, "Hiphop", 3, 1.5);
        arraylist2679.add(song7484);
        java.util.UUID uuid116010 = java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        song7485 = new fifthelement.theelement.objects.Song(uuid116010, "Hall of Fame", "android.resource://fifthelement.theelement/raw/hall_of_fame", author8644, album3914, "", 3, 4.5);
        arraylist2679.add(song7485);
        java.util.UUID uuid116011 = java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        song7490 = new fifthelement.theelement.objects.Song(uuid116011, "Classical Music", "android.resource://fifthelement.theelement/raw/classical_music", author8644, album3914, "Classical", 4, 2.0);
        arraylist2679.add(song7490);
        java.util.UUID uuid116012 = java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid116013 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author8631 = new fifthelement.theelement.objects.Author(uuid116013, "");
        java.util.UUID uuid116014 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        album3910 = new fifthelement.theelement.objects.Album(uuid116014, "");
        song7480 = new fifthelement.theelement.objects.Song(uuid116012, "Adventure of a Lifetime", "android.resource://fifthelement.theelement/raw/coldplay_adventure_of_a_lifetime", author8631, album3910, "Pop", 10, 3.5);
        arraylist2679.add(song7480);
        arraylist2679.iterator();
        java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        songlistservice9.setCurrentSongsList(arraylist2679);
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
        arraylist2678 = new java.util.ArrayList();
        java.util.UUID uuid116079 = java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid116080 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author8648 = new fifthelement.theelement.objects.Author(uuid116080, "");
        song7493 = new fifthelement.theelement.objects.Song(uuid116079, "This Is America", "android.resource://fifthelement.theelement/raw/childish_gambino_this_is_america", author8648, album3914, "Hiphop", 3, 1.5);
        arraylist2678.add(song7493);
        java.util.UUID uuid116081 = java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        song7489 = new fifthelement.theelement.objects.Song(uuid116081, "Hall of Fame", "android.resource://fifthelement.theelement/raw/hall_of_fame", author8644, album3914, "", 3, 4.5);
        arraylist2678.add(song7489);
        java.util.UUID uuid116082 = java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        song7487 = new fifthelement.theelement.objects.Song(uuid116082, "Classical Music", "android.resource://fifthelement.theelement/raw/classical_music", author8644, album3914, "Classical", 4, 2.0);
        arraylist2678.add(song7487);
        java.util.UUID uuid116083 = java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid116084 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author8629 = new fifthelement.theelement.objects.Author(uuid116084, "");
        java.util.UUID uuid116085 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        album3913 = new fifthelement.theelement.objects.Album(uuid116085, "");
        song7488 = new fifthelement.theelement.objects.Song(uuid116083, "Adventure of a Lifetime", "android.resource://fifthelement.theelement/raw/coldplay_adventure_of_a_lifetime", author8629, album3913, "Pop", 10, 3.5);
        arraylist2678.add(song7488);
        arraylist2678.iterator();
        song7493.getAuthor();
        fifthelement.theelement.objects.Author author8650 = song7493.getAuthor();
        author8650.getUUID();
        java.util.UUID uuid116087 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author8647 = new fifthelement.theelement.objects.Author(uuid116087, "Childish Gambino", 3);
        song7493.setAuthor(author8647);
        song7493.getAlbum();
        song7489.getAuthor();
        song7489.getAlbum();
        fifthelement.theelement.objects.Author author8652 = song7487.getAuthor();
        fifthelement.theelement.objects.Album album3919 = song7487.getAlbum();
        song7488.getAuthor();
        fifthelement.theelement.objects.Author author8654 = song7488.getAuthor();
        author8654.getUUID();
        java.util.UUID uuid116089 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author8628 = new fifthelement.theelement.objects.Author(uuid116089, "Coldplay", 10);
        song7488.setAuthor(author8628);
        song7488.getAlbum();
        fifthelement.theelement.objects.Album album3921 = song7488.getAlbum();
        album3921.getUUID();
        java.util.UUID uuid116091 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID uuid116092 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author8640 = new fifthelement.theelement.objects.Author(uuid116092, "");
        album3911 = new fifthelement.theelement.objects.Album(uuid116091, "A Head Full of Dreams", author8640, list220, 10);
        album3911.getAuthor();
        fifthelement.theelement.objects.Author author8656 = album3911.getAuthor();
        author8656.getUUID();
        java.util.UUID uuid116094 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author8634 = new fifthelement.theelement.objects.Author(uuid116094, "Coldplay", 10);
        album3911.setAuthor(author8634);
        song7488.setAlbum(album3911);
        songlistservice9.sortSongs(arraylist2678);
        arraylist2678.size();
        arraylist2678.size();
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
        arraylist2680 = new java.util.ArrayList();
        java.util.UUID uuid116108 = java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid116109 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author8642 = new fifthelement.theelement.objects.Author(uuid116109, "");
        song7483 = new fifthelement.theelement.objects.Song(uuid116108, "This Is America", "android.resource://fifthelement.theelement/raw/childish_gambino_this_is_america", author8642, album3919, "Hiphop", 3, 1.5);
        arraylist2680.add(song7483);
        java.util.UUID uuid116110 = java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        song7481 = new fifthelement.theelement.objects.Song(uuid116110, "Hall of Fame", "android.resource://fifthelement.theelement/raw/hall_of_fame", author8652, album3919, "", 3, 4.5);
        arraylist2680.add(song7481);
        java.util.UUID uuid116111 = java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        song7492 = new fifthelement.theelement.objects.Song(uuid116111, "Classical Music", "android.resource://fifthelement.theelement/raw/classical_music", author8652, album3919, "Classical", 4, 2.0);
        arraylist2680.add(song7492);
        java.util.UUID uuid116112 = java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid116113 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author8636 = new fifthelement.theelement.objects.Author(uuid116113, "");
        java.util.UUID uuid116114 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        album3915 = new fifthelement.theelement.objects.Album(uuid116114, "");
        song7479 = new fifthelement.theelement.objects.Song(uuid116112, "Adventure of a Lifetime", "android.resource://fifthelement.theelement/raw/coldplay_adventure_of_a_lifetime", author8636, album3915, "Pop", 10, 3.5);
        arraylist2680.add(song7479);
        arraylist2680.iterator();
        song7483.getAuthor();
        fifthelement.theelement.objects.Author author8658 = song7483.getAuthor();
        author8658.getUUID();
        java.util.UUID uuid116116 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author8635 = new fifthelement.theelement.objects.Author(uuid116116, "Childish Gambino", 3);
        song7483.setAuthor(author8635);
        song7483.getAlbum();
        song7481.getAuthor();
        song7481.getAlbum();
        song7492.getAuthor();
        fifthelement.theelement.objects.Album album3924 = song7492.getAlbum();
        song7479.getAuthor();
        fifthelement.theelement.objects.Author author8662 = song7479.getAuthor();
        author8662.getUUID();
        java.util.UUID uuid116118 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author8643 = new fifthelement.theelement.objects.Author(uuid116118, "Coldplay", 10);
        song7479.setAuthor(author8643);
        song7479.getAlbum();
        fifthelement.theelement.objects.Album album3926 = song7479.getAlbum();
        album3926.getUUID();
        java.util.UUID uuid116120 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID uuid116121 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author8637 = new fifthelement.theelement.objects.Author(uuid116121, "");
        album3912 = new fifthelement.theelement.objects.Album(uuid116120, "A Head Full of Dreams", author8637, list220, 10);
        album3912.getAuthor();
        fifthelement.theelement.objects.Author author8664 = album3912.getAuthor();
        author8664.getUUID();
        java.util.UUID uuid116123 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author8638 = new fifthelement.theelement.objects.Author(uuid116123, "Coldplay", 10);
        album3912.setAuthor(author8638);
        song7479.setAlbum(album3912);
        songlistservice9.sortSongs(arraylist2680);
        arraylist2680.size();
        arraylist2680.size();
        arraylist2680.size();
        arraylist2680.size();
        fifthelement.theelement.objects.Song song7494 = (fifthelement.theelement.objects.Song) arraylist2680.get(0);
        fifthelement.theelement.objects.Author author8665 = song7494.getAuthor();
        author8665.getName();
        song7494.getName();
        fifthelement.theelement.objects.Song song7495 = (fifthelement.theelement.objects.Song) arraylist2680.get(1);
        song7495.getAuthor();
        song7495.getName();
        fifthelement.theelement.objects.Song song7496 = (fifthelement.theelement.objects.Song) arraylist2680.get(2);
        song7496.getAuthor();
        song7496.getName();
        fifthelement.theelement.objects.Song song7497 = (fifthelement.theelement.objects.Song) arraylist2680.get(3);
        fifthelement.theelement.objects.Author author8668 = song7497.getAuthor();
        author8668.getName();
        song7497.getName();
        arraylist2680.size();
        arraylist2680.size();
        fifthelement.theelement.objects.Song song7498 = (fifthelement.theelement.objects.Song) arraylist2680.get(0);
        fifthelement.theelement.objects.Author author8669 = song7498.getAuthor();
        author8669.getName();
        song7498.getName();
        fifthelement.theelement.objects.Song song7499 = (fifthelement.theelement.objects.Song) arraylist2680.get(1);
        song7499.getAuthor();
        song7499.getName();
        fifthelement.theelement.objects.Song song7500 = (fifthelement.theelement.objects.Song) arraylist2680.get(2);
        song7500.getAuthor();
        song7500.getName();
        fifthelement.theelement.objects.Song song7501 = (fifthelement.theelement.objects.Song) arraylist2680.get(3);
        fifthelement.theelement.objects.Author author8672 = song7501.getAuthor();
        author8672.getName();
        song7501.getName();
        arraylist2680.size();
        fifthelement.theelement.objects.Song song7502 = (fifthelement.theelement.objects.Song) arraylist2680.get(0);
        fifthelement.theelement.objects.Author author8673 = song7502.getAuthor();
        author8673.getName();
        song7502.getName();
        fifthelement.theelement.objects.Song song7503 = (fifthelement.theelement.objects.Song) arraylist2680.get(1);
        song7503.getAuthor();
        song7503.getName();
        fifthelement.theelement.objects.Song song7504 = (fifthelement.theelement.objects.Song) arraylist2680.get(2);
        song7504.getAuthor();
        song7504.getName();
        fifthelement.theelement.objects.Song song7505 = (fifthelement.theelement.objects.Song) arraylist2680.get(3);
        fifthelement.theelement.objects.Author author8676 = song7505.getAuthor();
        author8676.getName();
        song7505.getName();
        arraylist2680.size();
        arraylist2680.size();
        fifthelement.theelement.objects.Song song7506 = (fifthelement.theelement.objects.Song) arraylist2680.get(0);
        fifthelement.theelement.objects.Author author8677 = song7506.getAuthor();
        author8677.getName();
        song7506.getName();
        fifthelement.theelement.objects.Song song7507 = (fifthelement.theelement.objects.Song) arraylist2680.get(1);
        song7507.getAuthor();
        song7507.getName();
        fifthelement.theelement.objects.Song song7508 = (fifthelement.theelement.objects.Song) arraylist2680.get(2);
        song7508.getAuthor();
        song7508.getName();
        fifthelement.theelement.objects.Song song7509 = (fifthelement.theelement.objects.Song) arraylist2680.get(3);
        fifthelement.theelement.objects.Author author8680 = song7509.getAuthor();
        author8680.getName();
        song7509.getName();
        arraylist2680.size();
        arraylist2680.size();
        fifthelement.theelement.objects.Song song7510 = (fifthelement.theelement.objects.Song) arraylist2680.get(0);
        fifthelement.theelement.objects.Author author8681 = song7510.getAuthor();
        author8681.getName();
        song7510.getName();
        fifthelement.theelement.objects.Song song7511 = (fifthelement.theelement.objects.Song) arraylist2680.get(1);
        song7511.getAuthor();
        song7511.getName();
        fifthelement.theelement.objects.Song song7512 = (fifthelement.theelement.objects.Song) arraylist2680.get(2);
        song7512.getAuthor();
        song7512.getName();
        fifthelement.theelement.objects.Song song7513 = (fifthelement.theelement.objects.Song) arraylist2680.get(3);
        fifthelement.theelement.objects.Author author8684 = song7513.getAuthor();
        author8684.getName();
        song7513.getName();
        arraylist2680.size();
        arraylist2680.get(0);
        arraylist2680.get(1);
        arraylist2680.get(2);
        arraylist2680.get(3);
        arraylist2680.size();
        arraylist2680.size();
        fifthelement.theelement.objects.Song song7518 = (fifthelement.theelement.objects.Song) arraylist2680.get(0);
        fifthelement.theelement.objects.Author author8685 = song7518.getAuthor();
        author8685.getName();
        song7518.getName();
        fifthelement.theelement.objects.Song song7519 = (fifthelement.theelement.objects.Song) arraylist2680.get(1);
        song7519.getAuthor();
        song7519.getName();
        fifthelement.theelement.objects.Song song7520 = (fifthelement.theelement.objects.Song) arraylist2680.get(2);
        song7520.getAuthor();
        song7520.getName();
        fifthelement.theelement.objects.Song song7521 = (fifthelement.theelement.objects.Song) arraylist2680.get(3);
        fifthelement.theelement.objects.Author author8688 = song7521.getAuthor();
        author8688.getName();
        song7521.getName();
        arraylist2680.size();
        arraylist2680.size();
        fifthelement.theelement.objects.Song song7522 = (fifthelement.theelement.objects.Song) arraylist2680.get(0);
        fifthelement.theelement.objects.Author author8689 = song7522.getAuthor();
        author8689.getName();
        song7522.getName();
        fifthelement.theelement.objects.Song song7523 = (fifthelement.theelement.objects.Song) arraylist2680.get(1);
        song7523.getAuthor();
        song7523.getName();
        fifthelement.theelement.objects.Song song7524 = (fifthelement.theelement.objects.Song) arraylist2680.get(2);
        fifthelement.theelement.objects.Author author8691 = song7524.getAuthor();
        song7524.getName();
        fifthelement.theelement.objects.Song song7525 = (fifthelement.theelement.objects.Song) arraylist2680.get(3);
        fifthelement.theelement.objects.Author author8692 = song7525.getAuthor();
        author8692.getName();
        song7525.getName();
        arraylist2680.size();
        arraylist2680.get(0);
        arraylist2680.get(1);
        arraylist2680.get(2);
        arraylist2680.get(3);
        arraylist2680.get(0);
        arraylist2680.size();
        arraylist2680.size();
        arraylist2680.get(0);
        arraylist2677 = new java.util.ArrayList();
        java.util.UUID uuid116124 = java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid116125 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author8632 = new fifthelement.theelement.objects.Author(uuid116125, "");
        song7482 = new fifthelement.theelement.objects.Song(uuid116124, "This Is America", "android.resource://fifthelement.theelement/raw/childish_gambino_this_is_america", author8632, album3924, "Hiphop", 3, 1.5);
        arraylist2677.add(song7482);
        java.util.UUID uuid116126 = java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        song7491 = new fifthelement.theelement.objects.Song(uuid116126, "Hall of Fame", "android.resource://fifthelement.theelement/raw/hall_of_fame", author8691, album3924, "", 3, 4.5);
        arraylist2677.add(song7491);
        java.util.UUID uuid116127 = java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        song7486 = new fifthelement.theelement.objects.Song(uuid116127, "Classical Music", "android.resource://fifthelement.theelement/raw/classical_music", author8691, album3924, "Classical", 4, 2.0);
        arraylist2677.add(song7486);
        java.util.UUID uuid116128 = java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid116129 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author8639 = new fifthelement.theelement.objects.Author(uuid116129, "");
        java.util.UUID uuid116130 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        album3916 = new fifthelement.theelement.objects.Album(uuid116130, "");
        song7478 = new fifthelement.theelement.objects.Song(uuid116128, "Adventure of a Lifetime", "android.resource://fifthelement.theelement/raw/coldplay_adventure_of_a_lifetime", author8639, album3916, "Pop", 10, 3.5);
        arraylist2677.add(song7478);
        arraylist2677.iterator();
        song7482.getAuthor();
        fifthelement.theelement.objects.Author author8694 = song7482.getAuthor();
        author8694.getUUID();
        java.util.UUID uuid116132 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author8630 = new fifthelement.theelement.objects.Author(uuid116132, "Childish Gambino", 3);
        song7482.setAuthor(author8630);
        song7482.getAlbum();
        song7491.getAuthor();
        song7491.getAlbum();
        song7486.getAuthor();
        song7486.getAlbum();
        song7478.getAuthor();
        fifthelement.theelement.objects.Author author8698 = song7478.getAuthor();
        author8698.getUUID();
        java.util.UUID uuid116134 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author8641 = new fifthelement.theelement.objects.Author(uuid116134, "Coldplay", 10);
        song7478.setAuthor(author8641);
        song7478.getAlbum();
        fifthelement.theelement.objects.Album album3931 = song7478.getAlbum();
        album3931.getUUID();
        java.util.UUID uuid116136 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID uuid116137 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author8646 = new fifthelement.theelement.objects.Author(uuid116137, "");
        album3909 = new fifthelement.theelement.objects.Album(uuid116136, "A Head Full of Dreams", author8646, list220, 10);
        album3909.getAuthor();
        fifthelement.theelement.objects.Author author8700 = album3909.getAuthor();
        author8700.getUUID();
        java.util.UUID uuid116139 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author8645 = new fifthelement.theelement.objects.Author(uuid116139, "Coldplay", 10);
        album3909.setAuthor(author8645);
        song7478.setAlbum(album3909);
        songlistservice9.sortSongs(arraylist2677);
        songlistservice9.setCurrentSongsList(arraylist2677);
        songlistservice9.getSongAtIndex(0);
        songlistservice9.setShuffleEnabled(false);
        songlistservice9.getCurrentSongPlayingIndex();
    }
}

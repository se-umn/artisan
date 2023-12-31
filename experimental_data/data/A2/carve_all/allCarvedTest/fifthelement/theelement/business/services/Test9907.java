package fifthelement.theelement.business.services;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

public class Test9907 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.PlayMusicTests#playPausingTest/Trace-1650046475193.txt
     * Method invocation under test: <fifthelement.theelement.business.services.SongListService: void setCurrentSongsList(java.util.List)>_1364_2727
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_business_services_SongListService_setCurrentSongsList_002() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        fifthelement.theelement.objects.Song song6913 = null;
        fifthelement.theelement.objects.Author author6606 = null;
        java.util.ArrayList arraylist2142 = null;
        fifthelement.theelement.objects.Author author6607 = null;
        fifthelement.theelement.objects.Song song6914 = null;
        fifthelement.theelement.objects.Song song6915 = null;
        fifthelement.theelement.objects.Author author6608 = null;
        fifthelement.theelement.objects.Author author6609 = null;
        fifthelement.theelement.objects.Song song6916 = null;
        fifthelement.theelement.objects.Author author6610 = null;
        fifthelement.theelement.objects.Author author6611 = null;
        fifthelement.theelement.objects.Author author6612 = null;
        fifthelement.theelement.objects.Album album3128 = null;
        fifthelement.theelement.objects.Song song6917 = null;
        fifthelement.theelement.objects.Song song6918 = null;
        fifthelement.theelement.objects.Author author6613 = null;
        fifthelement.theelement.objects.Album album3129 = null;
        fifthelement.theelement.objects.Album album3130 = null;
        fifthelement.theelement.objects.Song song6919 = null;
        java.util.ArrayList arraylist2143 = null;
        fifthelement.theelement.objects.Album album3131 = null;
        java.util.ArrayList arraylist2144 = null;
        fifthelement.theelement.objects.Author author6614 = null;
        fifthelement.theelement.objects.Album album3132 = null;
        fifthelement.theelement.objects.Song song6920 = null;
        fifthelement.theelement.objects.Author author6615 = null;
        fifthelement.theelement.objects.Song song6921 = null;
        fifthelement.theelement.objects.Album album3133 = null;
        fifthelement.theelement.business.services.SongListService songlistservice6 = null;
        fifthelement.theelement.objects.Author author6616 = null;
        fifthelement.theelement.objects.Author author6617 = null;
        fifthelement.theelement.objects.Album album3134 = null;
        fifthelement.theelement.objects.Author author6618 = null;
        fifthelement.theelement.objects.Song song6922 = null;
        fifthelement.theelement.objects.Song song6923 = null;
        fifthelement.theelement.objects.Song song6924 = null;
        fifthelement.theelement.objects.Song song6925 = null;
        fifthelement.theelement.objects.Author author6619 = null;
        fifthelement.theelement.objects.Song song6926 = null;
        fifthelement.theelement.objects.Author author6620 = null;
        fifthelement.theelement.objects.Song song6927 = null;
        java.util.List list110 = null;
        java.util.ArrayList arraylist2145 = null;
        fifthelement.theelement.objects.Song song6928 = null;
        fifthelement.theelement.objects.Author author6621 = null;
        fifthelement.theelement.objects.Author author6622 = null;
        songlistservice6 = new fifthelement.theelement.business.services.SongListService();
        arraylist2143 = new java.util.ArrayList();
        java.util.UUID uuid34407 = java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid34408 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author6611 = new fifthelement.theelement.objects.Author(uuid34408, "");
        song6922 = new fifthelement.theelement.objects.Song(uuid34407, "This Is America", "android.resource://fifthelement.theelement/raw/childish_gambino_this_is_america", author6611, album3133, "Hiphop", 3, 1.5);
        arraylist2143.add(song6922);
        java.util.UUID uuid34409 = java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        song6927 = new fifthelement.theelement.objects.Song(uuid34409, "Hall of Fame", "android.resource://fifthelement.theelement/raw/hall_of_fame", author6618, album3133, "", 3, 4.5);
        arraylist2143.add(song6927);
        java.util.UUID uuid34410 = java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        song6917 = new fifthelement.theelement.objects.Song(uuid34410, "Classical Music", "android.resource://fifthelement.theelement/raw/classical_music", author6618, album3133, "Classical", 4, 2.0);
        arraylist2143.add(song6917);
        java.util.UUID uuid34411 = java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid34412 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author6608 = new fifthelement.theelement.objects.Author(uuid34412, "");
        java.util.UUID uuid34413 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        album3132 = new fifthelement.theelement.objects.Album(uuid34413, "");
        song6928 = new fifthelement.theelement.objects.Song(uuid34411, "Adventure of a Lifetime", "android.resource://fifthelement.theelement/raw/coldplay_adventure_of_a_lifetime", author6608, album3132, "Pop", 10, 3.5);
        arraylist2143.add(song6928);
        arraylist2143.iterator();
        java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        songlistservice6.setCurrentSongsList(arraylist2143);
        arraylist2144 = new java.util.ArrayList();
        java.util.UUID uuid34419 = java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid34420 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author6610 = new fifthelement.theelement.objects.Author(uuid34420, "");
        song6913 = new fifthelement.theelement.objects.Song(uuid34419, "This Is America", "android.resource://fifthelement.theelement/raw/childish_gambino_this_is_america", author6610, album3133, "Hiphop", 3, 1.5);
        arraylist2144.add(song6913);
        java.util.UUID uuid34421 = java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        song6914 = new fifthelement.theelement.objects.Song(uuid34421, "Hall of Fame", "android.resource://fifthelement.theelement/raw/hall_of_fame", author6618, album3133, "", 3, 4.5);
        arraylist2144.add(song6914);
        java.util.UUID uuid34422 = java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        song6919 = new fifthelement.theelement.objects.Song(uuid34422, "Classical Music", "android.resource://fifthelement.theelement/raw/classical_music", author6618, album3133, "Classical", 4, 2.0);
        arraylist2144.add(song6919);
        java.util.UUID uuid34423 = java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid34424 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author6614 = new fifthelement.theelement.objects.Author(uuid34424, "");
        java.util.UUID uuid34425 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        album3128 = new fifthelement.theelement.objects.Album(uuid34425, "");
        song6916 = new fifthelement.theelement.objects.Song(uuid34423, "Adventure of a Lifetime", "android.resource://fifthelement.theelement/raw/coldplay_adventure_of_a_lifetime", author6614, album3128, "Pop", 10, 3.5);
        arraylist2144.add(song6916);
        arraylist2144.iterator();
        song6913.getAuthor();
        fifthelement.theelement.objects.Author author6624 = song6913.getAuthor();
        author6624.getUUID();
        java.util.UUID uuid34427 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author6612 = new fifthelement.theelement.objects.Author(uuid34427, "Childish Gambino", 3);
        song6913.setAuthor(author6612);
        song6913.getAlbum();
        song6914.getAuthor();
        song6914.getAlbum();
        fifthelement.theelement.objects.Author author6626 = song6919.getAuthor();
        fifthelement.theelement.objects.Album album3137 = song6919.getAlbum();
        song6916.getAuthor();
        fifthelement.theelement.objects.Author author6628 = song6916.getAuthor();
        author6628.getUUID();
        java.util.UUID uuid34429 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author6607 = new fifthelement.theelement.objects.Author(uuid34429, "Coldplay", 10);
        song6916.setAuthor(author6607);
        song6916.getAlbum();
        fifthelement.theelement.objects.Album album3139 = song6916.getAlbum();
        album3139.getUUID();
        java.util.UUID uuid34431 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID uuid34432 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author6616 = new fifthelement.theelement.objects.Author(uuid34432, "");
        album3129 = new fifthelement.theelement.objects.Album(uuid34431, "A Head Full of Dreams", author6616, list110, 10);
        album3129.getAuthor();
        fifthelement.theelement.objects.Author author6630 = album3129.getAuthor();
        author6630.getUUID();
        java.util.UUID uuid34434 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author6617 = new fifthelement.theelement.objects.Author(uuid34434, "Coldplay", 10);
        album3129.setAuthor(author6617);
        song6916.setAlbum(album3129);
        songlistservice6.sortSongs(arraylist2144);
        arraylist2144.size();
        arraylist2144.size();
        songlistservice6.getAutoplayEnabled();
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
        arraylist2145 = new java.util.ArrayList();
        java.util.UUID uuid34448 = java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid34449 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author6621 = new fifthelement.theelement.objects.Author(uuid34449, "");
        song6925 = new fifthelement.theelement.objects.Song(uuid34448, "This Is America", "android.resource://fifthelement.theelement/raw/childish_gambino_this_is_america", author6621, album3137, "Hiphop", 3, 1.5);
        arraylist2145.add(song6925);
        java.util.UUID uuid34450 = java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        song6918 = new fifthelement.theelement.objects.Song(uuid34450, "Hall of Fame", "android.resource://fifthelement.theelement/raw/hall_of_fame", author6626, album3137, "", 3, 4.5);
        arraylist2145.add(song6918);
        java.util.UUID uuid34451 = java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        song6926 = new fifthelement.theelement.objects.Song(uuid34451, "Classical Music", "android.resource://fifthelement.theelement/raw/classical_music", author6626, album3137, "Classical", 4, 2.0);
        arraylist2145.add(song6926);
        java.util.UUID uuid34452 = java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid34453 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author6619 = new fifthelement.theelement.objects.Author(uuid34453, "");
        java.util.UUID uuid34454 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        album3131 = new fifthelement.theelement.objects.Album(uuid34454, "");
        song6920 = new fifthelement.theelement.objects.Song(uuid34452, "Adventure of a Lifetime", "android.resource://fifthelement.theelement/raw/coldplay_adventure_of_a_lifetime", author6619, album3131, "Pop", 10, 3.5);
        arraylist2145.add(song6920);
        arraylist2145.iterator();
        song6925.getAuthor();
        fifthelement.theelement.objects.Author author6632 = song6925.getAuthor();
        author6632.getUUID();
        java.util.UUID uuid34456 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author6622 = new fifthelement.theelement.objects.Author(uuid34456, "Childish Gambino", 3);
        song6925.setAuthor(author6622);
        song6925.getAlbum();
        song6918.getAuthor();
        song6918.getAlbum();
        song6926.getAuthor();
        fifthelement.theelement.objects.Album album3142 = song6926.getAlbum();
        song6920.getAuthor();
        fifthelement.theelement.objects.Author author6636 = song6920.getAuthor();
        author6636.getUUID();
        java.util.UUID uuid34458 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author6615 = new fifthelement.theelement.objects.Author(uuid34458, "Coldplay", 10);
        song6920.setAuthor(author6615);
        song6920.getAlbum();
        fifthelement.theelement.objects.Album album3144 = song6920.getAlbum();
        album3144.getUUID();
        java.util.UUID uuid34460 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID uuid34461 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author6609 = new fifthelement.theelement.objects.Author(uuid34461, "");
        album3134 = new fifthelement.theelement.objects.Album(uuid34460, "A Head Full of Dreams", author6609, list110, 10);
        album3134.getAuthor();
        fifthelement.theelement.objects.Author author6638 = album3134.getAuthor();
        author6638.getUUID();
        java.util.UUID uuid34463 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author6620 = new fifthelement.theelement.objects.Author(uuid34463, "Coldplay", 10);
        album3134.setAuthor(author6620);
        song6920.setAlbum(album3134);
        songlistservice6.sortSongs(arraylist2145);
        arraylist2145.size();
        arraylist2145.size();
        arraylist2145.size();
        arraylist2145.size();
        fifthelement.theelement.objects.Song song6929 = (fifthelement.theelement.objects.Song) arraylist2145.get(0);
        fifthelement.theelement.objects.Author author6639 = song6929.getAuthor();
        author6639.getName();
        song6929.getName();
        fifthelement.theelement.objects.Song song6930 = (fifthelement.theelement.objects.Song) arraylist2145.get(1);
        song6930.getAuthor();
        song6930.getName();
        fifthelement.theelement.objects.Song song6931 = (fifthelement.theelement.objects.Song) arraylist2145.get(2);
        song6931.getAuthor();
        song6931.getName();
        fifthelement.theelement.objects.Song song6932 = (fifthelement.theelement.objects.Song) arraylist2145.get(3);
        fifthelement.theelement.objects.Author author6642 = song6932.getAuthor();
        author6642.getName();
        song6932.getName();
        arraylist2145.size();
        arraylist2145.size();
        fifthelement.theelement.objects.Song song6933 = (fifthelement.theelement.objects.Song) arraylist2145.get(0);
        fifthelement.theelement.objects.Author author6643 = song6933.getAuthor();
        author6643.getName();
        song6933.getName();
        fifthelement.theelement.objects.Song song6934 = (fifthelement.theelement.objects.Song) arraylist2145.get(1);
        song6934.getAuthor();
        song6934.getName();
        fifthelement.theelement.objects.Song song6935 = (fifthelement.theelement.objects.Song) arraylist2145.get(2);
        song6935.getAuthor();
        song6935.getName();
        fifthelement.theelement.objects.Song song6936 = (fifthelement.theelement.objects.Song) arraylist2145.get(3);
        fifthelement.theelement.objects.Author author6646 = song6936.getAuthor();
        author6646.getName();
        song6936.getName();
        arraylist2145.size();
        fifthelement.theelement.objects.Song song6937 = (fifthelement.theelement.objects.Song) arraylist2145.get(0);
        fifthelement.theelement.objects.Author author6647 = song6937.getAuthor();
        author6647.getName();
        song6937.getName();
        fifthelement.theelement.objects.Song song6938 = (fifthelement.theelement.objects.Song) arraylist2145.get(1);
        song6938.getAuthor();
        song6938.getName();
        fifthelement.theelement.objects.Song song6939 = (fifthelement.theelement.objects.Song) arraylist2145.get(2);
        song6939.getAuthor();
        song6939.getName();
        fifthelement.theelement.objects.Song song6940 = (fifthelement.theelement.objects.Song) arraylist2145.get(3);
        fifthelement.theelement.objects.Author author6650 = song6940.getAuthor();
        author6650.getName();
        song6940.getName();
        arraylist2145.size();
        arraylist2145.size();
        fifthelement.theelement.objects.Song song6941 = (fifthelement.theelement.objects.Song) arraylist2145.get(0);
        fifthelement.theelement.objects.Author author6651 = song6941.getAuthor();
        author6651.getName();
        song6941.getName();
        fifthelement.theelement.objects.Song song6942 = (fifthelement.theelement.objects.Song) arraylist2145.get(1);
        song6942.getAuthor();
        song6942.getName();
        fifthelement.theelement.objects.Song song6943 = (fifthelement.theelement.objects.Song) arraylist2145.get(2);
        song6943.getAuthor();
        song6943.getName();
        fifthelement.theelement.objects.Song song6944 = (fifthelement.theelement.objects.Song) arraylist2145.get(3);
        fifthelement.theelement.objects.Author author6654 = song6944.getAuthor();
        author6654.getName();
        song6944.getName();
        arraylist2145.size();
        arraylist2145.size();
        fifthelement.theelement.objects.Song song6945 = (fifthelement.theelement.objects.Song) arraylist2145.get(0);
        fifthelement.theelement.objects.Author author6655 = song6945.getAuthor();
        author6655.getName();
        song6945.getName();
        fifthelement.theelement.objects.Song song6946 = (fifthelement.theelement.objects.Song) arraylist2145.get(1);
        song6946.getAuthor();
        song6946.getName();
        fifthelement.theelement.objects.Song song6947 = (fifthelement.theelement.objects.Song) arraylist2145.get(2);
        song6947.getAuthor();
        song6947.getName();
        fifthelement.theelement.objects.Song song6948 = (fifthelement.theelement.objects.Song) arraylist2145.get(3);
        fifthelement.theelement.objects.Author author6658 = song6948.getAuthor();
        author6658.getName();
        song6948.getName();
        arraylist2145.size();
        arraylist2145.get(0);
        arraylist2145.get(1);
        arraylist2145.get(2);
        arraylist2145.get(3);
        arraylist2145.size();
        arraylist2145.size();
        fifthelement.theelement.objects.Song song6953 = (fifthelement.theelement.objects.Song) arraylist2145.get(0);
        fifthelement.theelement.objects.Author author6659 = song6953.getAuthor();
        author6659.getName();
        song6953.getName();
        fifthelement.theelement.objects.Song song6954 = (fifthelement.theelement.objects.Song) arraylist2145.get(1);
        song6954.getAuthor();
        song6954.getName();
        fifthelement.theelement.objects.Song song6955 = (fifthelement.theelement.objects.Song) arraylist2145.get(2);
        song6955.getAuthor();
        song6955.getName();
        fifthelement.theelement.objects.Song song6956 = (fifthelement.theelement.objects.Song) arraylist2145.get(3);
        fifthelement.theelement.objects.Author author6662 = song6956.getAuthor();
        author6662.getName();
        song6956.getName();
        arraylist2145.size();
        arraylist2145.size();
        fifthelement.theelement.objects.Song song6957 = (fifthelement.theelement.objects.Song) arraylist2145.get(0);
        fifthelement.theelement.objects.Author author6663 = song6957.getAuthor();
        author6663.getName();
        song6957.getName();
        fifthelement.theelement.objects.Song song6958 = (fifthelement.theelement.objects.Song) arraylist2145.get(1);
        song6958.getAuthor();
        song6958.getName();
        fifthelement.theelement.objects.Song song6959 = (fifthelement.theelement.objects.Song) arraylist2145.get(2);
        fifthelement.theelement.objects.Author author6665 = song6959.getAuthor();
        song6959.getName();
        fifthelement.theelement.objects.Song song6960 = (fifthelement.theelement.objects.Song) arraylist2145.get(3);
        fifthelement.theelement.objects.Author author6666 = song6960.getAuthor();
        author6666.getName();
        song6960.getName();
        arraylist2145.size();
        arraylist2145.get(0);
        arraylist2145.get(1);
        arraylist2145.get(2);
        arraylist2145.get(3);
        arraylist2145.get(0);
        arraylist2145.size();
        arraylist2145.size();
        arraylist2145.get(0);
        arraylist2142 = new java.util.ArrayList();
        java.util.UUID uuid34464 = java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid34465 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author6613 = new fifthelement.theelement.objects.Author(uuid34465, "");
        song6923 = new fifthelement.theelement.objects.Song(uuid34464, "This Is America", "android.resource://fifthelement.theelement/raw/childish_gambino_this_is_america", author6613, album3142, "Hiphop", 3, 1.5);
        arraylist2142.add(song6923);
        java.util.UUID uuid34466 = java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        song6921 = new fifthelement.theelement.objects.Song(uuid34466, "Hall of Fame", "android.resource://fifthelement.theelement/raw/hall_of_fame", author6665, album3142, "", 3, 4.5);
        arraylist2142.add(song6921);
        java.util.UUID uuid34467 = java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        song6924 = new fifthelement.theelement.objects.Song(uuid34467, "Classical Music", "android.resource://fifthelement.theelement/raw/classical_music", author6665, album3142, "Classical", 4, 2.0);
        arraylist2142.add(song6924);
        java.util.UUID uuid34468 = java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid34469 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author6606 = new fifthelement.theelement.objects.Author(uuid34469, "");
        java.util.UUID uuid34470 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        album3130 = new fifthelement.theelement.objects.Album(uuid34470, "");
        song6915 = new fifthelement.theelement.objects.Song(uuid34468, "Adventure of a Lifetime", "android.resource://fifthelement.theelement/raw/coldplay_adventure_of_a_lifetime", author6606, album3130, "Pop", 10, 3.5);
        arraylist2142.add(song6915);
        arraylist2142.iterator();
        songlistservice6.sortSongs(arraylist2142);
        songlistservice6.setCurrentSongsList(arraylist2142);
    }
}

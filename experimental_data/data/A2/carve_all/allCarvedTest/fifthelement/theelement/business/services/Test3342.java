package fifthelement.theelement.business.services;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

public class Test3342 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.PlayMusicTests#skipSongsTest/Trace-1650046483856.txt
     * Method invocation under test: <fifthelement.theelement.business.services.SongListService: fifthelement.theelement.objects.Song getSongAtIndex(int)>_1366_2731
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_business_services_SongListService_getSongAtIndex_001() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        fifthelement.theelement.objects.Song song6967 = null;
        fifthelement.theelement.objects.Author author6667 = null;
        java.util.ArrayList arraylist2146 = null;
        fifthelement.theelement.objects.Author author6668 = null;
        fifthelement.theelement.objects.Song song6968 = null;
        fifthelement.theelement.objects.Song song6969 = null;
        fifthelement.theelement.objects.Author author6669 = null;
        fifthelement.theelement.objects.Author author6670 = null;
        fifthelement.theelement.objects.Song song6970 = null;
        fifthelement.theelement.objects.Author author6671 = null;
        fifthelement.theelement.objects.Author author6672 = null;
        fifthelement.theelement.objects.Author author6673 = null;
        fifthelement.theelement.objects.Album album3145 = null;
        fifthelement.theelement.objects.Song song6971 = null;
        fifthelement.theelement.objects.Song song6972 = null;
        fifthelement.theelement.objects.Author author6674 = null;
        fifthelement.theelement.objects.Album album3146 = null;
        fifthelement.theelement.objects.Album album3147 = null;
        fifthelement.theelement.objects.Song song6973 = null;
        java.util.ArrayList arraylist2147 = null;
        fifthelement.theelement.objects.Album album3148 = null;
        java.util.ArrayList arraylist2148 = null;
        fifthelement.theelement.objects.Author author6675 = null;
        fifthelement.theelement.objects.Album album3149 = null;
        fifthelement.theelement.objects.Song song6974 = null;
        fifthelement.theelement.objects.Author author6676 = null;
        fifthelement.theelement.objects.Song song6975 = null;
        fifthelement.theelement.objects.Album album3150 = null;
        fifthelement.theelement.business.services.SongListService songlistservice7 = null;
        fifthelement.theelement.objects.Author author6677 = null;
        fifthelement.theelement.objects.Author author6678 = null;
        fifthelement.theelement.objects.Album album3151 = null;
        fifthelement.theelement.objects.Author author6679 = null;
        fifthelement.theelement.objects.Song song6976 = null;
        fifthelement.theelement.objects.Song song6977 = null;
        fifthelement.theelement.objects.Song song6978 = null;
        fifthelement.theelement.objects.Song song6979 = null;
        fifthelement.theelement.objects.Author author6680 = null;
        fifthelement.theelement.objects.Song song6980 = null;
        fifthelement.theelement.objects.Author author6681 = null;
        fifthelement.theelement.objects.Song song6981 = null;
        java.util.List list111 = null;
        java.util.ArrayList arraylist2149 = null;
        fifthelement.theelement.objects.Song song6982 = null;
        fifthelement.theelement.objects.Author author6682 = null;
        fifthelement.theelement.objects.Author author6683 = null;
        songlistservice7 = new fifthelement.theelement.business.services.SongListService();
        arraylist2147 = new java.util.ArrayList();
        java.util.UUID uuid34527 = java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid34528 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author6672 = new fifthelement.theelement.objects.Author(uuid34528, "");
        song6976 = new fifthelement.theelement.objects.Song(uuid34527, "This Is America", "android.resource://fifthelement.theelement/raw/childish_gambino_this_is_america", author6672, album3150, "Hiphop", 3, 1.5);
        arraylist2147.add(song6976);
        java.util.UUID uuid34529 = java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        song6981 = new fifthelement.theelement.objects.Song(uuid34529, "Hall of Fame", "android.resource://fifthelement.theelement/raw/hall_of_fame", author6679, album3150, "", 3, 4.5);
        arraylist2147.add(song6981);
        java.util.UUID uuid34530 = java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        song6971 = new fifthelement.theelement.objects.Song(uuid34530, "Classical Music", "android.resource://fifthelement.theelement/raw/classical_music", author6679, album3150, "Classical", 4, 2.0);
        arraylist2147.add(song6971);
        java.util.UUID uuid34531 = java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid34532 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author6669 = new fifthelement.theelement.objects.Author(uuid34532, "");
        java.util.UUID uuid34533 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        album3149 = new fifthelement.theelement.objects.Album(uuid34533, "");
        song6982 = new fifthelement.theelement.objects.Song(uuid34531, "Adventure of a Lifetime", "android.resource://fifthelement.theelement/raw/coldplay_adventure_of_a_lifetime", author6669, album3149, "Pop", 10, 3.5);
        arraylist2147.add(song6982);
        arraylist2147.iterator();
        java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        songlistservice7.setCurrentSongsList(arraylist2147);
        arraylist2148 = new java.util.ArrayList();
        java.util.UUID uuid34539 = java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid34540 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author6671 = new fifthelement.theelement.objects.Author(uuid34540, "");
        song6967 = new fifthelement.theelement.objects.Song(uuid34539, "This Is America", "android.resource://fifthelement.theelement/raw/childish_gambino_this_is_america", author6671, album3150, "Hiphop", 3, 1.5);
        arraylist2148.add(song6967);
        java.util.UUID uuid34541 = java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        song6968 = new fifthelement.theelement.objects.Song(uuid34541, "Hall of Fame", "android.resource://fifthelement.theelement/raw/hall_of_fame", author6679, album3150, "", 3, 4.5);
        arraylist2148.add(song6968);
        java.util.UUID uuid34542 = java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        song6973 = new fifthelement.theelement.objects.Song(uuid34542, "Classical Music", "android.resource://fifthelement.theelement/raw/classical_music", author6679, album3150, "Classical", 4, 2.0);
        arraylist2148.add(song6973);
        java.util.UUID uuid34543 = java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid34544 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author6675 = new fifthelement.theelement.objects.Author(uuid34544, "");
        java.util.UUID uuid34545 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        album3145 = new fifthelement.theelement.objects.Album(uuid34545, "");
        song6970 = new fifthelement.theelement.objects.Song(uuid34543, "Adventure of a Lifetime", "android.resource://fifthelement.theelement/raw/coldplay_adventure_of_a_lifetime", author6675, album3145, "Pop", 10, 3.5);
        arraylist2148.add(song6970);
        arraylist2148.iterator();
        song6967.getAuthor();
        fifthelement.theelement.objects.Author author6685 = song6967.getAuthor();
        author6685.getUUID();
        java.util.UUID uuid34547 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author6673 = new fifthelement.theelement.objects.Author(uuid34547, "Childish Gambino", 3);
        song6967.setAuthor(author6673);
        song6967.getAlbum();
        song6968.getAuthor();
        song6968.getAlbum();
        fifthelement.theelement.objects.Author author6687 = song6973.getAuthor();
        fifthelement.theelement.objects.Album album3154 = song6973.getAlbum();
        song6970.getAuthor();
        fifthelement.theelement.objects.Author author6689 = song6970.getAuthor();
        author6689.getUUID();
        java.util.UUID uuid34549 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author6668 = new fifthelement.theelement.objects.Author(uuid34549, "Coldplay", 10);
        song6970.setAuthor(author6668);
        song6970.getAlbum();
        fifthelement.theelement.objects.Album album3156 = song6970.getAlbum();
        album3156.getUUID();
        java.util.UUID uuid34551 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID uuid34552 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author6677 = new fifthelement.theelement.objects.Author(uuid34552, "");
        album3146 = new fifthelement.theelement.objects.Album(uuid34551, "A Head Full of Dreams", author6677, list111, 10);
        album3146.getAuthor();
        fifthelement.theelement.objects.Author author6691 = album3146.getAuthor();
        author6691.getUUID();
        java.util.UUID uuid34554 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author6678 = new fifthelement.theelement.objects.Author(uuid34554, "Coldplay", 10);
        album3146.setAuthor(author6678);
        song6970.setAlbum(album3146);
        songlistservice7.sortSongs(arraylist2148);
        arraylist2148.size();
        arraylist2148.size();
        songlistservice7.getAutoplayEnabled();
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
        arraylist2149 = new java.util.ArrayList();
        java.util.UUID uuid34568 = java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid34569 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author6682 = new fifthelement.theelement.objects.Author(uuid34569, "");
        song6979 = new fifthelement.theelement.objects.Song(uuid34568, "This Is America", "android.resource://fifthelement.theelement/raw/childish_gambino_this_is_america", author6682, album3154, "Hiphop", 3, 1.5);
        arraylist2149.add(song6979);
        java.util.UUID uuid34570 = java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        song6972 = new fifthelement.theelement.objects.Song(uuid34570, "Hall of Fame", "android.resource://fifthelement.theelement/raw/hall_of_fame", author6687, album3154, "", 3, 4.5);
        arraylist2149.add(song6972);
        java.util.UUID uuid34571 = java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        song6980 = new fifthelement.theelement.objects.Song(uuid34571, "Classical Music", "android.resource://fifthelement.theelement/raw/classical_music", author6687, album3154, "Classical", 4, 2.0);
        arraylist2149.add(song6980);
        java.util.UUID uuid34572 = java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid34573 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author6680 = new fifthelement.theelement.objects.Author(uuid34573, "");
        java.util.UUID uuid34574 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        album3148 = new fifthelement.theelement.objects.Album(uuid34574, "");
        song6974 = new fifthelement.theelement.objects.Song(uuid34572, "Adventure of a Lifetime", "android.resource://fifthelement.theelement/raw/coldplay_adventure_of_a_lifetime", author6680, album3148, "Pop", 10, 3.5);
        arraylist2149.add(song6974);
        arraylist2149.iterator();
        song6979.getAuthor();
        fifthelement.theelement.objects.Author author6693 = song6979.getAuthor();
        author6693.getUUID();
        java.util.UUID uuid34576 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author6683 = new fifthelement.theelement.objects.Author(uuid34576, "Childish Gambino", 3);
        song6979.setAuthor(author6683);
        song6979.getAlbum();
        song6972.getAuthor();
        song6972.getAlbum();
        song6980.getAuthor();
        fifthelement.theelement.objects.Album album3159 = song6980.getAlbum();
        song6974.getAuthor();
        fifthelement.theelement.objects.Author author6697 = song6974.getAuthor();
        author6697.getUUID();
        java.util.UUID uuid34578 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author6676 = new fifthelement.theelement.objects.Author(uuid34578, "Coldplay", 10);
        song6974.setAuthor(author6676);
        song6974.getAlbum();
        fifthelement.theelement.objects.Album album3161 = song6974.getAlbum();
        album3161.getUUID();
        java.util.UUID uuid34580 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID uuid34581 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author6670 = new fifthelement.theelement.objects.Author(uuid34581, "");
        album3151 = new fifthelement.theelement.objects.Album(uuid34580, "A Head Full of Dreams", author6670, list111, 10);
        album3151.getAuthor();
        fifthelement.theelement.objects.Author author6699 = album3151.getAuthor();
        author6699.getUUID();
        java.util.UUID uuid34583 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author6681 = new fifthelement.theelement.objects.Author(uuid34583, "Coldplay", 10);
        album3151.setAuthor(author6681);
        song6974.setAlbum(album3151);
        songlistservice7.sortSongs(arraylist2149);
        arraylist2149.size();
        arraylist2149.size();
        arraylist2149.size();
        arraylist2149.size();
        fifthelement.theelement.objects.Song song6983 = (fifthelement.theelement.objects.Song) arraylist2149.get(0);
        fifthelement.theelement.objects.Author author6700 = song6983.getAuthor();
        author6700.getName();
        song6983.getName();
        fifthelement.theelement.objects.Song song6984 = (fifthelement.theelement.objects.Song) arraylist2149.get(1);
        song6984.getAuthor();
        song6984.getName();
        fifthelement.theelement.objects.Song song6985 = (fifthelement.theelement.objects.Song) arraylist2149.get(2);
        song6985.getAuthor();
        song6985.getName();
        fifthelement.theelement.objects.Song song6986 = (fifthelement.theelement.objects.Song) arraylist2149.get(3);
        fifthelement.theelement.objects.Author author6703 = song6986.getAuthor();
        author6703.getName();
        song6986.getName();
        arraylist2149.size();
        arraylist2149.size();
        fifthelement.theelement.objects.Song song6987 = (fifthelement.theelement.objects.Song) arraylist2149.get(0);
        fifthelement.theelement.objects.Author author6704 = song6987.getAuthor();
        author6704.getName();
        song6987.getName();
        fifthelement.theelement.objects.Song song6988 = (fifthelement.theelement.objects.Song) arraylist2149.get(1);
        song6988.getAuthor();
        song6988.getName();
        fifthelement.theelement.objects.Song song6989 = (fifthelement.theelement.objects.Song) arraylist2149.get(2);
        song6989.getAuthor();
        song6989.getName();
        fifthelement.theelement.objects.Song song6990 = (fifthelement.theelement.objects.Song) arraylist2149.get(3);
        fifthelement.theelement.objects.Author author6707 = song6990.getAuthor();
        author6707.getName();
        song6990.getName();
        arraylist2149.size();
        fifthelement.theelement.objects.Song song6991 = (fifthelement.theelement.objects.Song) arraylist2149.get(0);
        fifthelement.theelement.objects.Author author6708 = song6991.getAuthor();
        author6708.getName();
        song6991.getName();
        fifthelement.theelement.objects.Song song6992 = (fifthelement.theelement.objects.Song) arraylist2149.get(1);
        song6992.getAuthor();
        song6992.getName();
        fifthelement.theelement.objects.Song song6993 = (fifthelement.theelement.objects.Song) arraylist2149.get(2);
        song6993.getAuthor();
        song6993.getName();
        fifthelement.theelement.objects.Song song6994 = (fifthelement.theelement.objects.Song) arraylist2149.get(3);
        fifthelement.theelement.objects.Author author6711 = song6994.getAuthor();
        author6711.getName();
        song6994.getName();
        arraylist2149.size();
        arraylist2149.size();
        fifthelement.theelement.objects.Song song6995 = (fifthelement.theelement.objects.Song) arraylist2149.get(0);
        fifthelement.theelement.objects.Author author6712 = song6995.getAuthor();
        author6712.getName();
        song6995.getName();
        fifthelement.theelement.objects.Song song6996 = (fifthelement.theelement.objects.Song) arraylist2149.get(1);
        song6996.getAuthor();
        song6996.getName();
        fifthelement.theelement.objects.Song song6997 = (fifthelement.theelement.objects.Song) arraylist2149.get(2);
        song6997.getAuthor();
        song6997.getName();
        fifthelement.theelement.objects.Song song6998 = (fifthelement.theelement.objects.Song) arraylist2149.get(3);
        fifthelement.theelement.objects.Author author6715 = song6998.getAuthor();
        author6715.getName();
        song6998.getName();
        arraylist2149.size();
        arraylist2149.size();
        fifthelement.theelement.objects.Song song6999 = (fifthelement.theelement.objects.Song) arraylist2149.get(0);
        fifthelement.theelement.objects.Author author6716 = song6999.getAuthor();
        author6716.getName();
        song6999.getName();
        fifthelement.theelement.objects.Song song7000 = (fifthelement.theelement.objects.Song) arraylist2149.get(1);
        song7000.getAuthor();
        song7000.getName();
        fifthelement.theelement.objects.Song song7001 = (fifthelement.theelement.objects.Song) arraylist2149.get(2);
        song7001.getAuthor();
        song7001.getName();
        fifthelement.theelement.objects.Song song7002 = (fifthelement.theelement.objects.Song) arraylist2149.get(3);
        fifthelement.theelement.objects.Author author6719 = song7002.getAuthor();
        author6719.getName();
        song7002.getName();
        arraylist2149.size();
        arraylist2149.get(0);
        arraylist2149.get(1);
        arraylist2149.get(2);
        arraylist2149.get(3);
        arraylist2149.size();
        arraylist2149.size();
        fifthelement.theelement.objects.Song song7007 = (fifthelement.theelement.objects.Song) arraylist2149.get(0);
        fifthelement.theelement.objects.Author author6720 = song7007.getAuthor();
        author6720.getName();
        song7007.getName();
        fifthelement.theelement.objects.Song song7008 = (fifthelement.theelement.objects.Song) arraylist2149.get(1);
        song7008.getAuthor();
        song7008.getName();
        fifthelement.theelement.objects.Song song7009 = (fifthelement.theelement.objects.Song) arraylist2149.get(2);
        song7009.getAuthor();
        song7009.getName();
        fifthelement.theelement.objects.Song song7010 = (fifthelement.theelement.objects.Song) arraylist2149.get(3);
        fifthelement.theelement.objects.Author author6723 = song7010.getAuthor();
        author6723.getName();
        song7010.getName();
        arraylist2149.size();
        arraylist2149.size();
        fifthelement.theelement.objects.Song song7011 = (fifthelement.theelement.objects.Song) arraylist2149.get(0);
        fifthelement.theelement.objects.Author author6724 = song7011.getAuthor();
        author6724.getName();
        song7011.getName();
        fifthelement.theelement.objects.Song song7012 = (fifthelement.theelement.objects.Song) arraylist2149.get(1);
        song7012.getAuthor();
        song7012.getName();
        fifthelement.theelement.objects.Song song7013 = (fifthelement.theelement.objects.Song) arraylist2149.get(2);
        fifthelement.theelement.objects.Author author6726 = song7013.getAuthor();
        song7013.getName();
        fifthelement.theelement.objects.Song song7014 = (fifthelement.theelement.objects.Song) arraylist2149.get(3);
        fifthelement.theelement.objects.Author author6727 = song7014.getAuthor();
        author6727.getName();
        song7014.getName();
        arraylist2149.size();
        arraylist2149.get(0);
        arraylist2149.get(1);
        arraylist2149.get(2);
        arraylist2149.get(3);
        arraylist2149.get(0);
        arraylist2149.size();
        arraylist2149.size();
        arraylist2149.get(0);
        arraylist2146 = new java.util.ArrayList();
        java.util.UUID uuid34584 = java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid34585 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author6674 = new fifthelement.theelement.objects.Author(uuid34585, "");
        song6977 = new fifthelement.theelement.objects.Song(uuid34584, "This Is America", "android.resource://fifthelement.theelement/raw/childish_gambino_this_is_america", author6674, album3159, "Hiphop", 3, 1.5);
        arraylist2146.add(song6977);
        java.util.UUID uuid34586 = java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        song6975 = new fifthelement.theelement.objects.Song(uuid34586, "Hall of Fame", "android.resource://fifthelement.theelement/raw/hall_of_fame", author6726, album3159, "", 3, 4.5);
        arraylist2146.add(song6975);
        java.util.UUID uuid34587 = java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        song6978 = new fifthelement.theelement.objects.Song(uuid34587, "Classical Music", "android.resource://fifthelement.theelement/raw/classical_music", author6726, album3159, "Classical", 4, 2.0);
        arraylist2146.add(song6978);
        java.util.UUID uuid34588 = java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid34589 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author6667 = new fifthelement.theelement.objects.Author(uuid34589, "");
        java.util.UUID uuid34590 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        album3147 = new fifthelement.theelement.objects.Album(uuid34590, "");
        song6969 = new fifthelement.theelement.objects.Song(uuid34588, "Adventure of a Lifetime", "android.resource://fifthelement.theelement/raw/coldplay_adventure_of_a_lifetime", author6667, album3147, "Pop", 10, 3.5);
        arraylist2146.add(song6969);
        arraylist2146.iterator();
        songlistservice7.sortSongs(arraylist2146);
        songlistservice7.setCurrentSongsList(arraylist2146);
        songlistservice7.getSongAtIndex(0);
    }
}

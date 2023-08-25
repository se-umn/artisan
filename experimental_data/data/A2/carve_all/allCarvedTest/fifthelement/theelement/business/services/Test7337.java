package fifthelement.theelement.business.services;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

public class Test7337 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.PlayMusicTests#seekTest/Trace-1650046478932.txt
     * Method invocation under test: <fifthelement.theelement.business.services.SongListService: void setShuffleEnabled(boolean)>_1387_2773
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_business_services_SongListService_setShuffleEnabled_001() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        fifthelement.theelement.objects.Song song7030 = null;
        fifthelement.theelement.objects.Author author6750 = null;
        java.util.ArrayList arraylist2150 = null;
        fifthelement.theelement.objects.Author author6751 = null;
        fifthelement.theelement.objects.Song song7031 = null;
        fifthelement.theelement.objects.Song song7032 = null;
        fifthelement.theelement.objects.Author author6752 = null;
        fifthelement.theelement.objects.Album album3173 = null;
        fifthelement.theelement.objects.Author author6753 = null;
        fifthelement.theelement.objects.Author author6754 = null;
        fifthelement.theelement.objects.Song song7033 = null;
        fifthelement.theelement.objects.Author author6755 = null;
        fifthelement.theelement.objects.Author author6756 = null;
        fifthelement.theelement.objects.Author author6757 = null;
        fifthelement.theelement.objects.Album album3174 = null;
        fifthelement.theelement.objects.Song song7034 = null;
        fifthelement.theelement.objects.Song song7035 = null;
        fifthelement.theelement.objects.Author author6758 = null;
        fifthelement.theelement.objects.Album album3175 = null;
        fifthelement.theelement.objects.Album album3176 = null;
        fifthelement.theelement.objects.Song song7036 = null;
        java.util.ArrayList arraylist2151 = null;
        fifthelement.theelement.objects.Author author6759 = null;
        fifthelement.theelement.objects.Album album3177 = null;
        java.util.ArrayList arraylist2152 = null;
        fifthelement.theelement.objects.Author author6760 = null;
        fifthelement.theelement.objects.Album album3178 = null;
        fifthelement.theelement.objects.Song song7037 = null;
        fifthelement.theelement.objects.Author author6761 = null;
        fifthelement.theelement.objects.Song song7038 = null;
        fifthelement.theelement.objects.Album album3179 = null;
        fifthelement.theelement.business.services.SongListService songlistservice8 = null;
        fifthelement.theelement.objects.Author author6762 = null;
        fifthelement.theelement.objects.Author author6763 = null;
        fifthelement.theelement.objects.Album album3180 = null;
        fifthelement.theelement.objects.Author author6764 = null;
        fifthelement.theelement.objects.Author author6765 = null;
        fifthelement.theelement.objects.Song song7039 = null;
        fifthelement.theelement.objects.Song song7040 = null;
        fifthelement.theelement.objects.Song song7041 = null;
        fifthelement.theelement.objects.Song song7042 = null;
        fifthelement.theelement.objects.Author author6766 = null;
        fifthelement.theelement.objects.Song song7043 = null;
        fifthelement.theelement.objects.Author author6767 = null;
        fifthelement.theelement.objects.Song song7044 = null;
        java.util.List list114 = null;
        fifthelement.theelement.objects.Author author6768 = null;
        java.util.ArrayList arraylist2153 = null;
        fifthelement.theelement.objects.Song song7045 = null;
        fifthelement.theelement.objects.Author author6769 = null;
        fifthelement.theelement.objects.Author author6770 = null;
        songlistservice8 = new fifthelement.theelement.business.services.SongListService();
        arraylist2151 = new java.util.ArrayList();
        java.util.UUID uuid34913 = java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid34914 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author6756 = new fifthelement.theelement.objects.Author(uuid34914, "");
        song7039 = new fifthelement.theelement.objects.Song(uuid34913, "This Is America", "android.resource://fifthelement.theelement/raw/childish_gambino_this_is_america", author6756, album3179, "Hiphop", 3, 1.5);
        arraylist2151.add(song7039);
        java.util.UUID uuid34915 = java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        song7044 = new fifthelement.theelement.objects.Song(uuid34915, "Hall of Fame", "android.resource://fifthelement.theelement/raw/hall_of_fame", author6764, album3179, "", 3, 4.5);
        arraylist2151.add(song7044);
        java.util.UUID uuid34916 = java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        song7034 = new fifthelement.theelement.objects.Song(uuid34916, "Classical Music", "android.resource://fifthelement.theelement/raw/classical_music", author6764, album3179, "Classical", 4, 2.0);
        arraylist2151.add(song7034);
        java.util.UUID uuid34917 = java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid34918 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author6752 = new fifthelement.theelement.objects.Author(uuid34918, "");
        java.util.UUID uuid34919 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        album3178 = new fifthelement.theelement.objects.Album(uuid34919, "");
        song7045 = new fifthelement.theelement.objects.Song(uuid34917, "Adventure of a Lifetime", "android.resource://fifthelement.theelement/raw/coldplay_adventure_of_a_lifetime", author6752, album3178, "Pop", 10, 3.5);
        arraylist2151.add(song7045);
        arraylist2151.iterator();
        java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        songlistservice8.setCurrentSongsList(arraylist2151);
        arraylist2152 = new java.util.ArrayList();
        java.util.UUID uuid34925 = java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid34926 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author6755 = new fifthelement.theelement.objects.Author(uuid34926, "");
        song7030 = new fifthelement.theelement.objects.Song(uuid34925, "This Is America", "android.resource://fifthelement.theelement/raw/childish_gambino_this_is_america", author6755, album3179, "Hiphop", 3, 1.5);
        arraylist2152.add(song7030);
        java.util.UUID uuid34927 = java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        song7031 = new fifthelement.theelement.objects.Song(uuid34927, "Hall of Fame", "android.resource://fifthelement.theelement/raw/hall_of_fame", author6764, album3179, "", 3, 4.5);
        arraylist2152.add(song7031);
        java.util.UUID uuid34928 = java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        song7036 = new fifthelement.theelement.objects.Song(uuid34928, "Classical Music", "android.resource://fifthelement.theelement/raw/classical_music", author6764, album3179, "Classical", 4, 2.0);
        arraylist2152.add(song7036);
        java.util.UUID uuid34929 = java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid34930 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author6760 = new fifthelement.theelement.objects.Author(uuid34930, "");
        java.util.UUID uuid34931 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        album3174 = new fifthelement.theelement.objects.Album(uuid34931, "");
        song7033 = new fifthelement.theelement.objects.Song(uuid34929, "Adventure of a Lifetime", "android.resource://fifthelement.theelement/raw/coldplay_adventure_of_a_lifetime", author6760, album3174, "Pop", 10, 3.5);
        arraylist2152.add(song7033);
        arraylist2152.iterator();
        song7030.getAuthor();
        fifthelement.theelement.objects.Author author6772 = song7030.getAuthor();
        author6772.getUUID();
        java.util.UUID uuid34933 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author6757 = new fifthelement.theelement.objects.Author(uuid34933, "Childish Gambino", 3);
        song7030.setAuthor(author6757);
        song7030.getAlbum();
        song7031.getAuthor();
        song7031.getAlbum();
        fifthelement.theelement.objects.Author author6774 = song7036.getAuthor();
        fifthelement.theelement.objects.Album album3183 = song7036.getAlbum();
        song7033.getAuthor();
        fifthelement.theelement.objects.Author author6776 = song7033.getAuthor();
        author6776.getUUID();
        java.util.UUID uuid34935 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author6751 = new fifthelement.theelement.objects.Author(uuid34935, "Coldplay", 10);
        song7033.setAuthor(author6751);
        song7033.getAlbum();
        fifthelement.theelement.objects.Album album3185 = song7033.getAlbum();
        album3185.getUUID();
        java.util.UUID uuid34937 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID uuid34938 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author6762 = new fifthelement.theelement.objects.Author(uuid34938, "");
        album3175 = new fifthelement.theelement.objects.Album(uuid34937, "A Head Full of Dreams", author6762, list114, 10);
        album3175.getAuthor();
        fifthelement.theelement.objects.Author author6778 = album3175.getAuthor();
        author6778.getUUID();
        java.util.UUID uuid34940 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author6763 = new fifthelement.theelement.objects.Author(uuid34940, "Coldplay", 10);
        album3175.setAuthor(author6763);
        song7033.setAlbum(album3175);
        songlistservice8.sortSongs(arraylist2152);
        arraylist2152.size();
        arraylist2152.size();
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
        arraylist2153 = new java.util.ArrayList();
        java.util.UUID uuid34954 = java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid34955 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author6769 = new fifthelement.theelement.objects.Author(uuid34955, "");
        song7042 = new fifthelement.theelement.objects.Song(uuid34954, "This Is America", "android.resource://fifthelement.theelement/raw/childish_gambino_this_is_america", author6769, album3183, "Hiphop", 3, 1.5);
        arraylist2153.add(song7042);
        java.util.UUID uuid34956 = java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        song7035 = new fifthelement.theelement.objects.Song(uuid34956, "Hall of Fame", "android.resource://fifthelement.theelement/raw/hall_of_fame", author6774, album3183, "", 3, 4.5);
        arraylist2153.add(song7035);
        java.util.UUID uuid34957 = java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        song7043 = new fifthelement.theelement.objects.Song(uuid34957, "Classical Music", "android.resource://fifthelement.theelement/raw/classical_music", author6774, album3183, "Classical", 4, 2.0);
        arraylist2153.add(song7043);
        java.util.UUID uuid34958 = java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid34959 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author6766 = new fifthelement.theelement.objects.Author(uuid34959, "");
        java.util.UUID uuid34960 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        album3177 = new fifthelement.theelement.objects.Album(uuid34960, "");
        song7037 = new fifthelement.theelement.objects.Song(uuid34958, "Adventure of a Lifetime", "android.resource://fifthelement.theelement/raw/coldplay_adventure_of_a_lifetime", author6766, album3177, "Pop", 10, 3.5);
        arraylist2153.add(song7037);
        arraylist2153.iterator();
        song7042.getAuthor();
        fifthelement.theelement.objects.Author author6780 = song7042.getAuthor();
        author6780.getUUID();
        java.util.UUID uuid34962 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author6770 = new fifthelement.theelement.objects.Author(uuid34962, "Childish Gambino", 3);
        song7042.setAuthor(author6770);
        song7042.getAlbum();
        song7035.getAuthor();
        song7035.getAlbum();
        song7043.getAuthor();
        fifthelement.theelement.objects.Album album3188 = song7043.getAlbum();
        song7037.getAuthor();
        fifthelement.theelement.objects.Author author6784 = song7037.getAuthor();
        author6784.getUUID();
        java.util.UUID uuid34964 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author6761 = new fifthelement.theelement.objects.Author(uuid34964, "Coldplay", 10);
        song7037.setAuthor(author6761);
        song7037.getAlbum();
        fifthelement.theelement.objects.Album album3190 = song7037.getAlbum();
        album3190.getUUID();
        java.util.UUID uuid34966 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID uuid34967 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author6753 = new fifthelement.theelement.objects.Author(uuid34967, "");
        album3180 = new fifthelement.theelement.objects.Album(uuid34966, "A Head Full of Dreams", author6753, list114, 10);
        album3180.getAuthor();
        fifthelement.theelement.objects.Author author6786 = album3180.getAuthor();
        author6786.getUUID();
        java.util.UUID uuid34969 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author6767 = new fifthelement.theelement.objects.Author(uuid34969, "Coldplay", 10);
        album3180.setAuthor(author6767);
        song7037.setAlbum(album3180);
        songlistservice8.sortSongs(arraylist2153);
        arraylist2153.size();
        arraylist2153.size();
        arraylist2153.size();
        arraylist2153.size();
        fifthelement.theelement.objects.Song song7046 = (fifthelement.theelement.objects.Song) arraylist2153.get(0);
        fifthelement.theelement.objects.Author author6787 = song7046.getAuthor();
        author6787.getName();
        song7046.getName();
        fifthelement.theelement.objects.Song song7047 = (fifthelement.theelement.objects.Song) arraylist2153.get(1);
        song7047.getAuthor();
        song7047.getName();
        fifthelement.theelement.objects.Song song7048 = (fifthelement.theelement.objects.Song) arraylist2153.get(2);
        song7048.getAuthor();
        song7048.getName();
        fifthelement.theelement.objects.Song song7049 = (fifthelement.theelement.objects.Song) arraylist2153.get(3);
        fifthelement.theelement.objects.Author author6790 = song7049.getAuthor();
        author6790.getName();
        song7049.getName();
        arraylist2153.size();
        arraylist2153.size();
        fifthelement.theelement.objects.Song song7050 = (fifthelement.theelement.objects.Song) arraylist2153.get(0);
        fifthelement.theelement.objects.Author author6791 = song7050.getAuthor();
        author6791.getName();
        song7050.getName();
        fifthelement.theelement.objects.Song song7051 = (fifthelement.theelement.objects.Song) arraylist2153.get(1);
        song7051.getAuthor();
        song7051.getName();
        fifthelement.theelement.objects.Song song7052 = (fifthelement.theelement.objects.Song) arraylist2153.get(2);
        song7052.getAuthor();
        song7052.getName();
        fifthelement.theelement.objects.Song song7053 = (fifthelement.theelement.objects.Song) arraylist2153.get(3);
        fifthelement.theelement.objects.Author author6794 = song7053.getAuthor();
        author6794.getName();
        song7053.getName();
        arraylist2153.size();
        fifthelement.theelement.objects.Song song7054 = (fifthelement.theelement.objects.Song) arraylist2153.get(0);
        fifthelement.theelement.objects.Author author6795 = song7054.getAuthor();
        author6795.getName();
        song7054.getName();
        fifthelement.theelement.objects.Song song7055 = (fifthelement.theelement.objects.Song) arraylist2153.get(1);
        song7055.getAuthor();
        song7055.getName();
        fifthelement.theelement.objects.Song song7056 = (fifthelement.theelement.objects.Song) arraylist2153.get(2);
        song7056.getAuthor();
        song7056.getName();
        fifthelement.theelement.objects.Song song7057 = (fifthelement.theelement.objects.Song) arraylist2153.get(3);
        fifthelement.theelement.objects.Author author6798 = song7057.getAuthor();
        author6798.getName();
        song7057.getName();
        arraylist2153.size();
        arraylist2153.size();
        fifthelement.theelement.objects.Song song7058 = (fifthelement.theelement.objects.Song) arraylist2153.get(0);
        fifthelement.theelement.objects.Author author6799 = song7058.getAuthor();
        author6799.getName();
        song7058.getName();
        fifthelement.theelement.objects.Song song7059 = (fifthelement.theelement.objects.Song) arraylist2153.get(1);
        song7059.getAuthor();
        song7059.getName();
        fifthelement.theelement.objects.Song song7060 = (fifthelement.theelement.objects.Song) arraylist2153.get(2);
        song7060.getAuthor();
        song7060.getName();
        fifthelement.theelement.objects.Song song7061 = (fifthelement.theelement.objects.Song) arraylist2153.get(3);
        fifthelement.theelement.objects.Author author6802 = song7061.getAuthor();
        author6802.getName();
        song7061.getName();
        arraylist2153.size();
        arraylist2153.size();
        fifthelement.theelement.objects.Song song7062 = (fifthelement.theelement.objects.Song) arraylist2153.get(0);
        fifthelement.theelement.objects.Author author6803 = song7062.getAuthor();
        author6803.getName();
        song7062.getName();
        fifthelement.theelement.objects.Song song7063 = (fifthelement.theelement.objects.Song) arraylist2153.get(1);
        song7063.getAuthor();
        song7063.getName();
        fifthelement.theelement.objects.Song song7064 = (fifthelement.theelement.objects.Song) arraylist2153.get(2);
        song7064.getAuthor();
        song7064.getName();
        fifthelement.theelement.objects.Song song7065 = (fifthelement.theelement.objects.Song) arraylist2153.get(3);
        fifthelement.theelement.objects.Author author6806 = song7065.getAuthor();
        author6806.getName();
        song7065.getName();
        arraylist2153.size();
        arraylist2153.get(0);
        arraylist2153.get(1);
        arraylist2153.get(2);
        arraylist2153.get(3);
        arraylist2153.size();
        arraylist2153.size();
        fifthelement.theelement.objects.Song song7070 = (fifthelement.theelement.objects.Song) arraylist2153.get(0);
        fifthelement.theelement.objects.Author author6807 = song7070.getAuthor();
        author6807.getName();
        song7070.getName();
        fifthelement.theelement.objects.Song song7071 = (fifthelement.theelement.objects.Song) arraylist2153.get(1);
        song7071.getAuthor();
        song7071.getName();
        fifthelement.theelement.objects.Song song7072 = (fifthelement.theelement.objects.Song) arraylist2153.get(2);
        song7072.getAuthor();
        song7072.getName();
        fifthelement.theelement.objects.Song song7073 = (fifthelement.theelement.objects.Song) arraylist2153.get(3);
        fifthelement.theelement.objects.Author author6810 = song7073.getAuthor();
        author6810.getName();
        song7073.getName();
        arraylist2153.size();
        arraylist2153.size();
        fifthelement.theelement.objects.Song song7074 = (fifthelement.theelement.objects.Song) arraylist2153.get(0);
        fifthelement.theelement.objects.Author author6811 = song7074.getAuthor();
        author6811.getName();
        song7074.getName();
        fifthelement.theelement.objects.Song song7075 = (fifthelement.theelement.objects.Song) arraylist2153.get(1);
        song7075.getAuthor();
        song7075.getName();
        fifthelement.theelement.objects.Song song7076 = (fifthelement.theelement.objects.Song) arraylist2153.get(2);
        fifthelement.theelement.objects.Author author6813 = song7076.getAuthor();
        song7076.getName();
        fifthelement.theelement.objects.Song song7077 = (fifthelement.theelement.objects.Song) arraylist2153.get(3);
        fifthelement.theelement.objects.Author author6814 = song7077.getAuthor();
        author6814.getName();
        song7077.getName();
        arraylist2153.size();
        arraylist2153.get(0);
        arraylist2153.get(1);
        arraylist2153.get(2);
        arraylist2153.get(3);
        arraylist2153.get(0);
        arraylist2153.size();
        arraylist2153.size();
        arraylist2153.get(0);
        arraylist2150 = new java.util.ArrayList();
        java.util.UUID uuid34970 = java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid34971 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author6758 = new fifthelement.theelement.objects.Author(uuid34971, "");
        song7040 = new fifthelement.theelement.objects.Song(uuid34970, "This Is America", "android.resource://fifthelement.theelement/raw/childish_gambino_this_is_america", author6758, album3188, "Hiphop", 3, 1.5);
        arraylist2150.add(song7040);
        java.util.UUID uuid34972 = java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        song7038 = new fifthelement.theelement.objects.Song(uuid34972, "Hall of Fame", "android.resource://fifthelement.theelement/raw/hall_of_fame", author6813, album3188, "", 3, 4.5);
        arraylist2150.add(song7038);
        java.util.UUID uuid34973 = java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        song7041 = new fifthelement.theelement.objects.Song(uuid34973, "Classical Music", "android.resource://fifthelement.theelement/raw/classical_music", author6813, album3188, "Classical", 4, 2.0);
        arraylist2150.add(song7041);
        java.util.UUID uuid34974 = java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid34975 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author6750 = new fifthelement.theelement.objects.Author(uuid34975, "");
        java.util.UUID uuid34976 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        album3176 = new fifthelement.theelement.objects.Album(uuid34976, "");
        song7032 = new fifthelement.theelement.objects.Song(uuid34974, "Adventure of a Lifetime", "android.resource://fifthelement.theelement/raw/coldplay_adventure_of_a_lifetime", author6750, album3176, "Pop", 10, 3.5);
        arraylist2150.add(song7032);
        arraylist2150.iterator();
        song7040.getAuthor();
        fifthelement.theelement.objects.Author author6816 = song7040.getAuthor();
        author6816.getUUID();
        java.util.UUID uuid34978 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author6754 = new fifthelement.theelement.objects.Author(uuid34978, "Childish Gambino", 3);
        song7040.setAuthor(author6754);
        song7040.getAlbum();
        song7038.getAuthor();
        song7038.getAlbum();
        song7041.getAuthor();
        song7041.getAlbum();
        song7032.getAuthor();
        fifthelement.theelement.objects.Author author6820 = song7032.getAuthor();
        author6820.getUUID();
        java.util.UUID uuid34980 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author6765 = new fifthelement.theelement.objects.Author(uuid34980, "Coldplay", 10);
        song7032.setAuthor(author6765);
        song7032.getAlbum();
        fifthelement.theelement.objects.Album album3195 = song7032.getAlbum();
        album3195.getUUID();
        java.util.UUID uuid34982 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID uuid34983 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author6759 = new fifthelement.theelement.objects.Author(uuid34983, "");
        album3173 = new fifthelement.theelement.objects.Album(uuid34982, "A Head Full of Dreams", author6759, list114, 10);
        album3173.getAuthor();
        fifthelement.theelement.objects.Author author6822 = album3173.getAuthor();
        author6822.getUUID();
        java.util.UUID uuid34985 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author6768 = new fifthelement.theelement.objects.Author(uuid34985, "Coldplay", 10);
        album3173.setAuthor(author6768);
        song7032.setAlbum(album3173);
        songlistservice8.sortSongs(arraylist2150);
        songlistservice8.setCurrentSongsList(arraylist2150);
        songlistservice8.getSongAtIndex(0);
        songlistservice8.setShuffleEnabled(false);
    }
}

package fifthelement.theelement.business.services;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

public class Test8919 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.CustomizeTests#deleteSong/Trace-1650046523752.txt
     * Method invocation under test: <fifthelement.theelement.business.services.SongListService: void removeSongFromList(fifthelement.theelement.objects.Song)>_1885_3768
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_business_services_SongListService_removeSongFromList_001() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        fifthelement.theelement.objects.Album album2114 = null;
        fifthelement.theelement.objects.Author author4895 = null;
        fifthelement.theelement.objects.Song song3844 = null;
        fifthelement.theelement.objects.Song song3845 = null;
        fifthelement.theelement.objects.Author author4896 = null;
        fifthelement.theelement.objects.Author author4897 = null;
        fifthelement.theelement.objects.Author author4898 = null;
        fifthelement.theelement.objects.Author author4899 = null;
        fifthelement.theelement.objects.Song song3846 = null;
        fifthelement.theelement.objects.Author author4900 = null;
        fifthelement.theelement.objects.Author author4901 = null;
        fifthelement.theelement.objects.Album album2115 = null;
        java.util.ArrayList arraylist1584 = null;
        fifthelement.theelement.objects.Song song3847 = null;
        fifthelement.theelement.objects.Author author4902 = null;
        fifthelement.theelement.objects.Song song3848 = null;
        fifthelement.theelement.objects.Album album2116 = null;
        fifthelement.theelement.objects.Song song3849 = null;
        fifthelement.theelement.objects.Author author4903 = null;
        fifthelement.theelement.objects.Author author4904 = null;
        fifthelement.theelement.objects.Album album2117 = null;
        java.util.ArrayList arraylist1585 = null;
        fifthelement.theelement.objects.Album album2118 = null;
        fifthelement.theelement.business.services.SongListService songlistservice5 = null;
        fifthelement.theelement.objects.Song song3850 = null;
        fifthelement.theelement.objects.Author author4905 = null;
        fifthelement.theelement.objects.Song song3851 = null;
        java.util.ArrayList arraylist1586 = null;
        fifthelement.theelement.objects.Song song3852 = null;
        java.util.List list126 = null;
        fifthelement.theelement.objects.Author author4906 = null;
        fifthelement.theelement.objects.Author author4907 = null;
        fifthelement.theelement.objects.Album album2119 = null;
        fifthelement.theelement.objects.Author author4908 = null;
        fifthelement.theelement.objects.Song song3853 = null;
        fifthelement.theelement.objects.Song song3854 = null;
        fifthelement.theelement.objects.Author author4909 = null;
        fifthelement.theelement.objects.Song song3855 = null;
        songlistservice5 = new fifthelement.theelement.business.services.SongListService();
        arraylist1584 = new java.util.ArrayList();
        java.util.UUID uuid73911 = java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid73912 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author4897 = new fifthelement.theelement.objects.Author(uuid73912, "");
        song3851 = new fifthelement.theelement.objects.Song(uuid73911, "This Is America", "android.resource://fifthelement.theelement/raw/childish_gambino_this_is_america", author4897, album2118, "Hiphop", 3, 1.5);
        arraylist1584.add(song3851);
        java.util.UUID uuid73913 = java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        song3852 = new fifthelement.theelement.objects.Song(uuid73913, "Hall of Fame", "android.resource://fifthelement.theelement/raw/hall_of_fame", author4905, album2118, "", 3, 4.5);
        arraylist1584.add(song3852);
        java.util.UUID uuid73914 = java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        song3846 = new fifthelement.theelement.objects.Song(uuid73914, "Classical Music", "android.resource://fifthelement.theelement/raw/classical_music", author4905, album2118, "Classical", 4, 2.0);
        arraylist1584.add(song3846);
        java.util.UUID uuid73915 = java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid73916 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author4896 = new fifthelement.theelement.objects.Author(uuid73916, "");
        java.util.UUID uuid73917 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        album2116 = new fifthelement.theelement.objects.Album(uuid73917, "");
        song3854 = new fifthelement.theelement.objects.Song(uuid73915, "Adventure of a Lifetime", "android.resource://fifthelement.theelement/raw/coldplay_adventure_of_a_lifetime", author4896, album2116, "Pop", 10, 3.5);
        arraylist1584.add(song3854);
        arraylist1584.iterator();
        java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        songlistservice5.setCurrentSongsList(arraylist1584);
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
        arraylist1585 = new java.util.ArrayList();
        java.util.UUID uuid73982 = java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid73983 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author4899 = new fifthelement.theelement.objects.Author(uuid73983, "");
        song3847 = new fifthelement.theelement.objects.Song(uuid73982, "This Is America", "android.resource://fifthelement.theelement/raw/childish_gambino_this_is_america", author4899, album2118, "Hiphop", 3, 1.5);
        arraylist1585.add(song3847);
        java.util.UUID uuid73984 = java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        song3845 = new fifthelement.theelement.objects.Song(uuid73984, "Hall of Fame", "android.resource://fifthelement.theelement/raw/hall_of_fame", author4905, album2118, "", 3, 4.5);
        arraylist1585.add(song3845);
        java.util.UUID uuid73985 = java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        song3848 = new fifthelement.theelement.objects.Song(uuid73985, "Classical Music", "android.resource://fifthelement.theelement/raw/classical_music", author4905, album2118, "Classical", 4, 2.0);
        arraylist1585.add(song3848);
        java.util.UUID uuid73986 = java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid73987 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author4902 = new fifthelement.theelement.objects.Author(uuid73987, "");
        java.util.UUID uuid73988 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        album2117 = new fifthelement.theelement.objects.Album(uuid73988, "");
        song3853 = new fifthelement.theelement.objects.Song(uuid73986, "Adventure of a Lifetime", "android.resource://fifthelement.theelement/raw/coldplay_adventure_of_a_lifetime", author4902, album2117, "Pop", 10, 3.5);
        arraylist1585.add(song3853);
        arraylist1585.iterator();
        song3847.getAuthor();
        fifthelement.theelement.objects.Author author4911 = song3847.getAuthor();
        author4911.getUUID();
        java.util.UUID uuid73990 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author4906 = new fifthelement.theelement.objects.Author(uuid73990, "Childish Gambino", 3);
        song3847.setAuthor(author4906);
        song3847.getAlbum();
        song3845.getAuthor();
        song3845.getAlbum();
        fifthelement.theelement.objects.Author author4913 = song3848.getAuthor();
        fifthelement.theelement.objects.Album album2122 = song3848.getAlbum();
        song3853.getAuthor();
        fifthelement.theelement.objects.Author author4915 = song3853.getAuthor();
        author4915.getUUID();
        java.util.UUID uuid73992 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author4907 = new fifthelement.theelement.objects.Author(uuid73992, "Coldplay", 10);
        song3853.setAuthor(author4907);
        song3853.getAlbum();
        fifthelement.theelement.objects.Album album2124 = song3853.getAlbum();
        album2124.getUUID();
        java.util.UUID uuid73994 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID uuid73995 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author4895 = new fifthelement.theelement.objects.Author(uuid73995, "");
        album2119 = new fifthelement.theelement.objects.Album(uuid73994, "A Head Full of Dreams", author4895, list126, 10);
        album2119.getAuthor();
        fifthelement.theelement.objects.Author author4917 = album2119.getAuthor();
        author4917.getUUID();
        java.util.UUID uuid73997 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author4908 = new fifthelement.theelement.objects.Author(uuid73997, "Coldplay", 10);
        album2119.setAuthor(author4908);
        song3853.setAlbum(album2119);
        songlistservice5.sortSongs(arraylist1585);
        arraylist1585.size();
        arraylist1585.size();
        songlistservice5.getAutoplayEnabled();
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
        arraylist1586 = new java.util.ArrayList();
        java.util.UUID uuid74011 = java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid74012 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author4898 = new fifthelement.theelement.objects.Author(uuid74012, "");
        song3855 = new fifthelement.theelement.objects.Song(uuid74011, "This Is America", "android.resource://fifthelement.theelement/raw/childish_gambino_this_is_america", author4898, album2122, "Hiphop", 3, 1.5);
        arraylist1586.add(song3855);
        java.util.UUID uuid74013 = java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        song3844 = new fifthelement.theelement.objects.Song(uuid74013, "Hall of Fame", "android.resource://fifthelement.theelement/raw/hall_of_fame", author4913, album2122, "", 3, 4.5);
        arraylist1586.add(song3844);
        java.util.UUID uuid74014 = java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        song3849 = new fifthelement.theelement.objects.Song(uuid74014, "Classical Music", "android.resource://fifthelement.theelement/raw/classical_music", author4913, album2122, "Classical", 4, 2.0);
        arraylist1586.add(song3849);
        java.util.UUID uuid74015 = java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid74016 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author4903 = new fifthelement.theelement.objects.Author(uuid74016, "");
        java.util.UUID uuid74017 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        album2114 = new fifthelement.theelement.objects.Album(uuid74017, "");
        song3850 = new fifthelement.theelement.objects.Song(uuid74015, "Adventure of a Lifetime", "android.resource://fifthelement.theelement/raw/coldplay_adventure_of_a_lifetime", author4903, album2114, "Pop", 10, 3.5);
        arraylist1586.add(song3850);
        arraylist1586.iterator();
        song3855.getAuthor();
        fifthelement.theelement.objects.Author author4919 = song3855.getAuthor();
        author4919.getUUID();
        java.util.UUID uuid74019 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author4901 = new fifthelement.theelement.objects.Author(uuid74019, "Childish Gambino", 3);
        song3855.setAuthor(author4901);
        song3855.getAlbum();
        song3844.getAuthor();
        song3844.getAlbum();
        song3849.getAuthor();
        song3849.getAlbum();
        song3850.getAuthor();
        fifthelement.theelement.objects.Author author4923 = song3850.getAuthor();
        author4923.getUUID();
        java.util.UUID uuid74021 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author4909 = new fifthelement.theelement.objects.Author(uuid74021, "Coldplay", 10);
        song3850.setAuthor(author4909);
        song3850.getAlbum();
        fifthelement.theelement.objects.Album album2129 = song3850.getAlbum();
        album2129.getUUID();
        java.util.UUID uuid74023 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID uuid74024 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author4904 = new fifthelement.theelement.objects.Author(uuid74024, "");
        album2115 = new fifthelement.theelement.objects.Album(uuid74023, "A Head Full of Dreams", author4904, list126, 10);
        album2115.getAuthor();
        fifthelement.theelement.objects.Author author4925 = album2115.getAuthor();
        author4925.getUUID();
        java.util.UUID uuid74026 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author4900 = new fifthelement.theelement.objects.Author(uuid74026, "Coldplay", 10);
        album2115.setAuthor(author4900);
        song3850.setAlbum(album2115);
        songlistservice5.sortSongs(arraylist1586);
        arraylist1586.size();
        arraylist1586.size();
        arraylist1586.size();
        arraylist1586.size();
        fifthelement.theelement.objects.Song song3856 = (fifthelement.theelement.objects.Song) arraylist1586.get(0);
        fifthelement.theelement.objects.Author author4926 = song3856.getAuthor();
        author4926.getName();
        song3856.getName();
        fifthelement.theelement.objects.Song song3857 = (fifthelement.theelement.objects.Song) arraylist1586.get(1);
        song3857.getAuthor();
        song3857.getName();
        fifthelement.theelement.objects.Song song3858 = (fifthelement.theelement.objects.Song) arraylist1586.get(2);
        song3858.getAuthor();
        song3858.getName();
        fifthelement.theelement.objects.Song song3859 = (fifthelement.theelement.objects.Song) arraylist1586.get(3);
        fifthelement.theelement.objects.Author author4929 = song3859.getAuthor();
        author4929.getName();
        song3859.getName();
        arraylist1586.size();
        arraylist1586.size();
        fifthelement.theelement.objects.Song song3860 = (fifthelement.theelement.objects.Song) arraylist1586.get(0);
        fifthelement.theelement.objects.Author author4930 = song3860.getAuthor();
        author4930.getName();
        song3860.getName();
        fifthelement.theelement.objects.Song song3861 = (fifthelement.theelement.objects.Song) arraylist1586.get(1);
        song3861.getAuthor();
        song3861.getName();
        fifthelement.theelement.objects.Song song3862 = (fifthelement.theelement.objects.Song) arraylist1586.get(2);
        song3862.getAuthor();
        song3862.getName();
        fifthelement.theelement.objects.Song song3863 = (fifthelement.theelement.objects.Song) arraylist1586.get(3);
        fifthelement.theelement.objects.Author author4933 = song3863.getAuthor();
        author4933.getName();
        song3863.getName();
        arraylist1586.size();
        fifthelement.theelement.objects.Song song3864 = (fifthelement.theelement.objects.Song) arraylist1586.get(0);
        fifthelement.theelement.objects.Author author4934 = song3864.getAuthor();
        author4934.getName();
        song3864.getName();
        fifthelement.theelement.objects.Song song3865 = (fifthelement.theelement.objects.Song) arraylist1586.get(1);
        song3865.getAuthor();
        song3865.getName();
        fifthelement.theelement.objects.Song song3866 = (fifthelement.theelement.objects.Song) arraylist1586.get(2);
        song3866.getAuthor();
        song3866.getName();
        fifthelement.theelement.objects.Song song3867 = (fifthelement.theelement.objects.Song) arraylist1586.get(3);
        fifthelement.theelement.objects.Author author4937 = song3867.getAuthor();
        author4937.getName();
        song3867.getName();
        arraylist1586.size();
        arraylist1586.size();
        fifthelement.theelement.objects.Song song3868 = (fifthelement.theelement.objects.Song) arraylist1586.get(0);
        fifthelement.theelement.objects.Author author4938 = song3868.getAuthor();
        author4938.getName();
        song3868.getName();
        fifthelement.theelement.objects.Song song3869 = (fifthelement.theelement.objects.Song) arraylist1586.get(1);
        song3869.getAuthor();
        song3869.getName();
        fifthelement.theelement.objects.Song song3870 = (fifthelement.theelement.objects.Song) arraylist1586.get(2);
        song3870.getAuthor();
        song3870.getName();
        fifthelement.theelement.objects.Song song3871 = (fifthelement.theelement.objects.Song) arraylist1586.get(3);
        fifthelement.theelement.objects.Author author4941 = song3871.getAuthor();
        author4941.getName();
        song3871.getName();
        arraylist1586.size();
        arraylist1586.size();
        fifthelement.theelement.objects.Song song3872 = (fifthelement.theelement.objects.Song) arraylist1586.get(0);
        fifthelement.theelement.objects.Author author4942 = song3872.getAuthor();
        author4942.getName();
        song3872.getName();
        fifthelement.theelement.objects.Song song3873 = (fifthelement.theelement.objects.Song) arraylist1586.get(1);
        song3873.getAuthor();
        song3873.getName();
        fifthelement.theelement.objects.Song song3874 = (fifthelement.theelement.objects.Song) arraylist1586.get(2);
        song3874.getAuthor();
        song3874.getName();
        fifthelement.theelement.objects.Song song3875 = (fifthelement.theelement.objects.Song) arraylist1586.get(3);
        fifthelement.theelement.objects.Author author4945 = song3875.getAuthor();
        author4945.getName();
        song3875.getName();
        arraylist1586.size();
        song3872.getName();
        java.util.UUID uuid74027 = song3872.getUUID();
        uuid74027.toString();
        arraylist1586.remove(song3872);
        songlistservice5.removeSongFromList(song3872);
    }
}

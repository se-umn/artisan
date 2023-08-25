package fifthelement.theelement.business.services;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

public class Test1094 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.PlayMusicTests#shuffleSongTest/Trace-1650046488177.txt
     * Method invocation under test: <fifthelement.theelement.business.services.SongListService: void shuffle()>_1103_2204
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_business_services_SongListService_shuffle_001() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        fifthelement.theelement.objects.Song song154 = null;
        java.util.ArrayList arraylist48 = null;
        fifthelement.theelement.objects.Author author157 = null;
        fifthelement.theelement.objects.Song song155 = null;
        fifthelement.theelement.objects.Album album84 = null;
        java.util.ArrayList arraylist49 = null;
        fifthelement.theelement.objects.Author author158 = null;
        fifthelement.theelement.objects.Album album85 = null;
        fifthelement.theelement.objects.Song song156 = null;
        fifthelement.theelement.objects.Author author159 = null;
        fifthelement.theelement.objects.Author author160 = null;
        fifthelement.theelement.objects.Author author161 = null;
        fifthelement.theelement.objects.Song song157 = null;
        fifthelement.theelement.objects.Album album86 = null;
        fifthelement.theelement.business.services.SongListService songlistservice4 = null;
        fifthelement.theelement.objects.Author author162 = null;
        fifthelement.theelement.objects.Author author163 = null;
        fifthelement.theelement.objects.Album album87 = null;
        fifthelement.theelement.objects.Author author164 = null;
        fifthelement.theelement.objects.Author author165 = null;
        fifthelement.theelement.objects.Author author166 = null;
        fifthelement.theelement.objects.Author author167 = null;
        fifthelement.theelement.objects.Song song158 = null;
        fifthelement.theelement.objects.Album album88 = null;
        fifthelement.theelement.objects.Song song159 = null;
        fifthelement.theelement.objects.Author author168 = null;
        fifthelement.theelement.objects.Song song160 = null;
        fifthelement.theelement.objects.Song song161 = null;
        fifthelement.theelement.objects.Album album89 = null;
        fifthelement.theelement.objects.Song song162 = null;
        fifthelement.theelement.objects.Author author169 = null;
        fifthelement.theelement.objects.Song song163 = null;
        java.util.List list6 = null;
        java.util.ArrayList arraylist50 = null;
        fifthelement.theelement.objects.Song song164 = null;
        fifthelement.theelement.objects.Song song165 = null;
        fifthelement.theelement.objects.Author author170 = null;
        fifthelement.theelement.objects.Author author171 = null;
        songlistservice4 = new fifthelement.theelement.business.services.SongListService();
        arraylist48 = new java.util.ArrayList();
        java.util.UUID uuid909 = java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid910 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author166 = new fifthelement.theelement.objects.Author(uuid910, "");
        song158 = new fifthelement.theelement.objects.Song(uuid909, "This Is America", "android.resource://fifthelement.theelement/raw/childish_gambino_this_is_america", author166, album86, "Hiphop", 3, 1.5);
        arraylist48.add(song158);
        java.util.UUID uuid911 = java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        song163 = new fifthelement.theelement.objects.Song(uuid911, "Hall of Fame", "android.resource://fifthelement.theelement/raw/hall_of_fame", author164, album86, "", 3, 4.5);
        arraylist48.add(song163);
        java.util.UUID uuid912 = java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        song160 = new fifthelement.theelement.objects.Song(uuid912, "Classical Music", "android.resource://fifthelement.theelement/raw/classical_music", author164, album86, "Classical", 4, 2.0);
        arraylist48.add(song160);
        java.util.UUID uuid913 = java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid914 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author160 = new fifthelement.theelement.objects.Author(uuid914, "");
        java.util.UUID uuid915 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        album85 = new fifthelement.theelement.objects.Album(uuid915, "");
        song165 = new fifthelement.theelement.objects.Song(uuid913, "Adventure of a Lifetime", "android.resource://fifthelement.theelement/raw/coldplay_adventure_of_a_lifetime", author160, album85, "Pop", 10, 3.5);
        arraylist48.add(song165);
        arraylist48.iterator();
        java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        songlistservice4.setCurrentSongsList(arraylist48);
        arraylist49 = new java.util.ArrayList();
        java.util.UUID uuid921 = java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid922 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author165 = new fifthelement.theelement.objects.Author(uuid922, "");
        song154 = new fifthelement.theelement.objects.Song(uuid921, "This Is America", "android.resource://fifthelement.theelement/raw/childish_gambino_this_is_america", author165, album86, "Hiphop", 3, 1.5);
        arraylist49.add(song154);
        java.util.UUID uuid923 = java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        song155 = new fifthelement.theelement.objects.Song(uuid923, "Hall of Fame", "android.resource://fifthelement.theelement/raw/hall_of_fame", author164, album86, "", 3, 4.5);
        arraylist49.add(song155);
        java.util.UUID uuid924 = java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        song164 = new fifthelement.theelement.objects.Song(uuid924, "Classical Music", "android.resource://fifthelement.theelement/raw/classical_music", author164, album86, "Classical", 4, 2.0);
        arraylist49.add(song164);
        java.util.UUID uuid925 = java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid926 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author158 = new fifthelement.theelement.objects.Author(uuid926, "");
        java.util.UUID uuid927 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        album88 = new fifthelement.theelement.objects.Album(uuid927, "");
        song157 = new fifthelement.theelement.objects.Song(uuid925, "Adventure of a Lifetime", "android.resource://fifthelement.theelement/raw/coldplay_adventure_of_a_lifetime", author158, album88, "Pop", 10, 3.5);
        arraylist49.add(song157);
        arraylist49.iterator();
        song154.getAuthor();
        fifthelement.theelement.objects.Author author173 = song154.getAuthor();
        author173.getUUID();
        java.util.UUID uuid929 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author167 = new fifthelement.theelement.objects.Author(uuid929, "Childish Gambino", 3);
        song154.setAuthor(author167);
        song154.getAlbum();
        song155.getAuthor();
        song155.getAlbum();
        fifthelement.theelement.objects.Author author175 = song164.getAuthor();
        fifthelement.theelement.objects.Album album92 = song164.getAlbum();
        song157.getAuthor();
        fifthelement.theelement.objects.Author author177 = song157.getAuthor();
        author177.getUUID();
        java.util.UUID uuid931 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author157 = new fifthelement.theelement.objects.Author(uuid931, "Coldplay", 10);
        song157.setAuthor(author157);
        song157.getAlbum();
        fifthelement.theelement.objects.Album album94 = song157.getAlbum();
        album94.getUUID();
        java.util.UUID uuid933 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID uuid934 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author162 = new fifthelement.theelement.objects.Author(uuid934, "");
        album89 = new fifthelement.theelement.objects.Album(uuid933, "A Head Full of Dreams", author162, list6, 10);
        album89.getAuthor();
        fifthelement.theelement.objects.Author author179 = album89.getAuthor();
        author179.getUUID();
        java.util.UUID uuid936 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author163 = new fifthelement.theelement.objects.Author(uuid936, "Coldplay", 10);
        album89.setAuthor(author163);
        song157.setAlbum(album89);
        songlistservice4.sortSongs(arraylist49);
        arraylist49.size();
        arraylist49.size();
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
        arraylist50 = new java.util.ArrayList();
        java.util.UUID uuid950 = java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid951 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author170 = new fifthelement.theelement.objects.Author(uuid951, "");
        song159 = new fifthelement.theelement.objects.Song(uuid950, "This Is America", "android.resource://fifthelement.theelement/raw/childish_gambino_this_is_america", author170, album92, "Hiphop", 3, 1.5);
        arraylist50.add(song159);
        java.util.UUID uuid952 = java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        song161 = new fifthelement.theelement.objects.Song(uuid952, "Hall of Fame", "android.resource://fifthelement.theelement/raw/hall_of_fame", author175, album92, "", 3, 4.5);
        arraylist50.add(song161);
        java.util.UUID uuid953 = java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        song162 = new fifthelement.theelement.objects.Song(uuid953, "Classical Music", "android.resource://fifthelement.theelement/raw/classical_music", author175, album92, "Classical", 4, 2.0);
        arraylist50.add(song162);
        java.util.UUID uuid954 = java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid955 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author168 = new fifthelement.theelement.objects.Author(uuid955, "");
        java.util.UUID uuid956 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        album84 = new fifthelement.theelement.objects.Album(uuid956, "");
        song156 = new fifthelement.theelement.objects.Song(uuid954, "Adventure of a Lifetime", "android.resource://fifthelement.theelement/raw/coldplay_adventure_of_a_lifetime", author168, album84, "Pop", 10, 3.5);
        arraylist50.add(song156);
        arraylist50.iterator();
        song159.getAuthor();
        fifthelement.theelement.objects.Author author181 = song159.getAuthor();
        author181.getUUID();
        java.util.UUID uuid958 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author171 = new fifthelement.theelement.objects.Author(uuid958, "Childish Gambino", 3);
        song159.setAuthor(author171);
        song159.getAlbum();
        song161.getAuthor();
        song161.getAlbum();
        song162.getAuthor();
        song162.getAlbum();
        song156.getAuthor();
        fifthelement.theelement.objects.Author author185 = song156.getAuthor();
        author185.getUUID();
        java.util.UUID uuid960 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author159 = new fifthelement.theelement.objects.Author(uuid960, "Coldplay", 10);
        song156.setAuthor(author159);
        song156.getAlbum();
        fifthelement.theelement.objects.Album album99 = song156.getAlbum();
        album99.getUUID();
        java.util.UUID uuid962 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID uuid963 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author161 = new fifthelement.theelement.objects.Author(uuid963, "");
        album87 = new fifthelement.theelement.objects.Album(uuid962, "A Head Full of Dreams", author161, list6, 10);
        album87.getAuthor();
        fifthelement.theelement.objects.Author author187 = album87.getAuthor();
        author187.getUUID();
        java.util.UUID uuid965 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author169 = new fifthelement.theelement.objects.Author(uuid965, "Coldplay", 10);
        album87.setAuthor(author169);
        song156.setAlbum(album87);
        songlistservice4.sortSongs(arraylist50);
        arraylist50.size();
        arraylist50.size();
        arraylist50.size();
        arraylist50.size();
        fifthelement.theelement.objects.Song song166 = (fifthelement.theelement.objects.Song) arraylist50.get(0);
        fifthelement.theelement.objects.Author author188 = song166.getAuthor();
        author188.getName();
        song166.getName();
        fifthelement.theelement.objects.Song song167 = (fifthelement.theelement.objects.Song) arraylist50.get(1);
        song167.getAuthor();
        song167.getName();
        fifthelement.theelement.objects.Song song168 = (fifthelement.theelement.objects.Song) arraylist50.get(2);
        song168.getAuthor();
        song168.getName();
        fifthelement.theelement.objects.Song song169 = (fifthelement.theelement.objects.Song) arraylist50.get(3);
        fifthelement.theelement.objects.Author author191 = song169.getAuthor();
        author191.getName();
        song169.getName();
        arraylist50.size();
        arraylist50.size();
        fifthelement.theelement.objects.Song song170 = (fifthelement.theelement.objects.Song) arraylist50.get(0);
        fifthelement.theelement.objects.Author author192 = song170.getAuthor();
        author192.getName();
        song170.getName();
        fifthelement.theelement.objects.Song song171 = (fifthelement.theelement.objects.Song) arraylist50.get(1);
        song171.getAuthor();
        song171.getName();
        fifthelement.theelement.objects.Song song172 = (fifthelement.theelement.objects.Song) arraylist50.get(2);
        song172.getAuthor();
        song172.getName();
        fifthelement.theelement.objects.Song song173 = (fifthelement.theelement.objects.Song) arraylist50.get(3);
        fifthelement.theelement.objects.Author author195 = song173.getAuthor();
        author195.getName();
        song173.getName();
        arraylist50.size();
        fifthelement.theelement.objects.Song song174 = (fifthelement.theelement.objects.Song) arraylist50.get(0);
        fifthelement.theelement.objects.Author author196 = song174.getAuthor();
        author196.getName();
        song174.getName();
        fifthelement.theelement.objects.Song song175 = (fifthelement.theelement.objects.Song) arraylist50.get(1);
        song175.getAuthor();
        song175.getName();
        fifthelement.theelement.objects.Song song176 = (fifthelement.theelement.objects.Song) arraylist50.get(2);
        song176.getAuthor();
        song176.getName();
        fifthelement.theelement.objects.Song song177 = (fifthelement.theelement.objects.Song) arraylist50.get(3);
        fifthelement.theelement.objects.Author author199 = song177.getAuthor();
        author199.getName();
        song177.getName();
        arraylist50.size();
        arraylist50.size();
        fifthelement.theelement.objects.Song song178 = (fifthelement.theelement.objects.Song) arraylist50.get(0);
        fifthelement.theelement.objects.Author author200 = song178.getAuthor();
        author200.getName();
        song178.getName();
        fifthelement.theelement.objects.Song song179 = (fifthelement.theelement.objects.Song) arraylist50.get(1);
        song179.getAuthor();
        song179.getName();
        fifthelement.theelement.objects.Song song180 = (fifthelement.theelement.objects.Song) arraylist50.get(2);
        song180.getAuthor();
        song180.getName();
        fifthelement.theelement.objects.Song song181 = (fifthelement.theelement.objects.Song) arraylist50.get(3);
        fifthelement.theelement.objects.Author author203 = song181.getAuthor();
        author203.getName();
        song181.getName();
        arraylist50.size();
        arraylist50.size();
        fifthelement.theelement.objects.Song song182 = (fifthelement.theelement.objects.Song) arraylist50.get(0);
        fifthelement.theelement.objects.Author author204 = song182.getAuthor();
        author204.getName();
        song182.getName();
        fifthelement.theelement.objects.Song song183 = (fifthelement.theelement.objects.Song) arraylist50.get(1);
        song183.getAuthor();
        song183.getName();
        fifthelement.theelement.objects.Song song184 = (fifthelement.theelement.objects.Song) arraylist50.get(2);
        song184.getAuthor();
        song184.getName();
        fifthelement.theelement.objects.Song song185 = (fifthelement.theelement.objects.Song) arraylist50.get(3);
        fifthelement.theelement.objects.Author author207 = song185.getAuthor();
        author207.getName();
        song185.getName();
        arraylist50.size();
        songlistservice4.shuffle();
    }
}

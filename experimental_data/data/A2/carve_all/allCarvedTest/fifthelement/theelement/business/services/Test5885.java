package fifthelement.theelement.business.services;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

public class Test5885 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.SettingsTest#playSongWithSongPlayedCheck/Trace-1650046467562.txt
     * Method invocation under test: <fifthelement.theelement.business.services.SongListService: fifthelement.theelement.objects.Song getSongAtIndex(int)>_2067_4133
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_business_services_SongListService_getSongAtIndex_001() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        java.util.ArrayList arraylist2539 = null;
        fifthelement.theelement.objects.Song song7052 = null;
        fifthelement.theelement.objects.Author author7688 = null;
        fifthelement.theelement.objects.Author author7689 = null;
        fifthelement.theelement.objects.Song song7053 = null;
        fifthelement.theelement.objects.Song song7054 = null;
        fifthelement.theelement.objects.Song song7055 = null;
        fifthelement.theelement.objects.Song song7056 = null;
        java.util.ArrayList arraylist2540 = null;
        fifthelement.theelement.objects.Album album3430 = null;
        fifthelement.theelement.objects.Album album3431 = null;
        fifthelement.theelement.objects.Author author7690 = null;
        fifthelement.theelement.objects.Author author7691 = null;
        fifthelement.theelement.objects.Author author7692 = null;
        fifthelement.theelement.objects.Song song7057 = null;
        fifthelement.theelement.objects.Song song7058 = null;
        fifthelement.theelement.objects.Song song7059 = null;
        fifthelement.theelement.objects.Author author7693 = null;
        java.util.ArrayList arraylist2541 = null;
        fifthelement.theelement.objects.Author author7694 = null;
        fifthelement.theelement.objects.Album album3432 = null;
        fifthelement.theelement.objects.Song song7060 = null;
        fifthelement.theelement.objects.Author author7695 = null;
        fifthelement.theelement.objects.Song song7061 = null;
        fifthelement.theelement.objects.Author author7696 = null;
        fifthelement.theelement.objects.Song song7062 = null;
        fifthelement.theelement.objects.Song song7063 = null;
        fifthelement.theelement.objects.Author author7697 = null;
        fifthelement.theelement.objects.Song song7064 = null;
        fifthelement.theelement.business.services.SongListService songlistservice7 = null;
        fifthelement.theelement.objects.Album album3433 = null;
        java.util.ArrayList arraylist2542 = null;
        fifthelement.theelement.objects.Song song7065 = null;
        fifthelement.theelement.objects.Song song7066 = null;
        fifthelement.theelement.objects.Album album3434 = null;
        fifthelement.theelement.objects.Author author7698 = null;
        fifthelement.theelement.objects.Author author7699 = null;
        fifthelement.theelement.objects.Album album3435 = null;
        fifthelement.theelement.objects.Author author7700 = null;
        fifthelement.theelement.objects.Author author7701 = null;
        fifthelement.theelement.objects.Author author7702 = null;
        fifthelement.theelement.objects.Album album3436 = null;
        fifthelement.theelement.objects.Author author7703 = null;
        java.util.List list160 = null;
        fifthelement.theelement.objects.Author author7704 = null;
        fifthelement.theelement.objects.Song song7067 = null;
        songlistservice7 = new fifthelement.theelement.business.services.SongListService();
        arraylist2541 = new java.util.ArrayList();
        java.util.UUID uuid90859 = java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid90860 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author7692 = new fifthelement.theelement.objects.Author(uuid90860, "");
        song7057 = new fifthelement.theelement.objects.Song(uuid90859, "This Is America", "android.resource://fifthelement.theelement/raw/childish_gambino_this_is_america", author7692, album3433, "Hiphop", 3, 1.5);
        arraylist2541.add(song7057);
        java.util.UUID uuid90861 = java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        song7058 = new fifthelement.theelement.objects.Song(uuid90861, "Hall of Fame", "android.resource://fifthelement.theelement/raw/hall_of_fame", author7698, album3433, "", 3, 4.5);
        arraylist2541.add(song7058);
        java.util.UUID uuid90862 = java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        song7066 = new fifthelement.theelement.objects.Song(uuid90862, "Classical Music", "android.resource://fifthelement.theelement/raw/classical_music", author7698, album3433, "Classical", 4, 2.0);
        arraylist2541.add(song7066);
        java.util.UUID uuid90863 = java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid90864 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author7690 = new fifthelement.theelement.objects.Author(uuid90864, "");
        java.util.UUID uuid90865 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        album3431 = new fifthelement.theelement.objects.Album(uuid90865, "");
        song7056 = new fifthelement.theelement.objects.Song(uuid90863, "Adventure of a Lifetime", "android.resource://fifthelement.theelement/raw/coldplay_adventure_of_a_lifetime", author7690, album3431, "Pop", 10, 3.5);
        arraylist2541.add(song7056);
        arraylist2541.iterator();
        java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        songlistservice7.setCurrentSongsList(arraylist2541);
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
        arraylist2542 = new java.util.ArrayList();
        java.util.UUID uuid90930 = java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid90931 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author7695 = new fifthelement.theelement.objects.Author(uuid90931, "");
        song7061 = new fifthelement.theelement.objects.Song(uuid90930, "This Is America", "android.resource://fifthelement.theelement/raw/childish_gambino_this_is_america", author7695, album3433, "Hiphop", 3, 1.5);
        arraylist2542.add(song7061);
        java.util.UUID uuid90932 = java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        song7060 = new fifthelement.theelement.objects.Song(uuid90932, "Hall of Fame", "android.resource://fifthelement.theelement/raw/hall_of_fame", author7698, album3433, "", 3, 4.5);
        arraylist2542.add(song7060);
        java.util.UUID uuid90933 = java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        song7054 = new fifthelement.theelement.objects.Song(uuid90933, "Classical Music", "android.resource://fifthelement.theelement/raw/classical_music", author7698, album3433, "Classical", 4, 2.0);
        arraylist2542.add(song7054);
        java.util.UUID uuid90934 = java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid90935 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author7689 = new fifthelement.theelement.objects.Author(uuid90935, "");
        java.util.UUID uuid90936 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        album3436 = new fifthelement.theelement.objects.Album(uuid90936, "");
        song7059 = new fifthelement.theelement.objects.Song(uuid90934, "Adventure of a Lifetime", "android.resource://fifthelement.theelement/raw/coldplay_adventure_of_a_lifetime", author7689, album3436, "Pop", 10, 3.5);
        arraylist2542.add(song7059);
        arraylist2542.iterator();
        song7061.getAuthor();
        fifthelement.theelement.objects.Author author7706 = song7061.getAuthor();
        author7706.getUUID();
        java.util.UUID uuid90938 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author7696 = new fifthelement.theelement.objects.Author(uuid90938, "Childish Gambino", 3);
        song7061.setAuthor(author7696);
        song7061.getAlbum();
        song7060.getAuthor();
        song7060.getAlbum();
        fifthelement.theelement.objects.Author author7708 = song7054.getAuthor();
        fifthelement.theelement.objects.Album album3439 = song7054.getAlbum();
        song7059.getAuthor();
        fifthelement.theelement.objects.Author author7710 = song7059.getAuthor();
        author7710.getUUID();
        java.util.UUID uuid90940 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author7691 = new fifthelement.theelement.objects.Author(uuid90940, "Coldplay", 10);
        song7059.setAuthor(author7691);
        song7059.getAlbum();
        fifthelement.theelement.objects.Album album3441 = song7059.getAlbum();
        album3441.getUUID();
        java.util.UUID uuid90942 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID uuid90943 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author7704 = new fifthelement.theelement.objects.Author(uuid90943, "");
        album3434 = new fifthelement.theelement.objects.Album(uuid90942, "A Head Full of Dreams", author7704, list160, 10);
        album3434.getAuthor();
        fifthelement.theelement.objects.Author author7712 = album3434.getAuthor();
        author7712.getUUID();
        java.util.UUID uuid90945 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author7702 = new fifthelement.theelement.objects.Author(uuid90945, "Coldplay", 10);
        album3434.setAuthor(author7702);
        song7059.setAlbum(album3434);
        songlistservice7.sortSongs(arraylist2542);
        arraylist2542.size();
        arraylist2542.size();
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
        arraylist2539 = new java.util.ArrayList();
        java.util.UUID uuid90959 = java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid90960 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author7700 = new fifthelement.theelement.objects.Author(uuid90960, "");
        song7063 = new fifthelement.theelement.objects.Song(uuid90959, "This Is America", "android.resource://fifthelement.theelement/raw/childish_gambino_this_is_america", author7700, album3439, "Hiphop", 3, 1.5);
        arraylist2539.add(song7063);
        java.util.UUID uuid90961 = java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        song7067 = new fifthelement.theelement.objects.Song(uuid90961, "Hall of Fame", "android.resource://fifthelement.theelement/raw/hall_of_fame", author7708, album3439, "", 3, 4.5);
        arraylist2539.add(song7067);
        java.util.UUID uuid90962 = java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        song7053 = new fifthelement.theelement.objects.Song(uuid90962, "Classical Music", "android.resource://fifthelement.theelement/raw/classical_music", author7708, album3439, "Classical", 4, 2.0);
        arraylist2539.add(song7053);
        java.util.UUID uuid90963 = java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid90964 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author7693 = new fifthelement.theelement.objects.Author(uuid90964, "");
        java.util.UUID uuid90965 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        album3432 = new fifthelement.theelement.objects.Album(uuid90965, "");
        song7062 = new fifthelement.theelement.objects.Song(uuid90963, "Adventure of a Lifetime", "android.resource://fifthelement.theelement/raw/coldplay_adventure_of_a_lifetime", author7693, album3432, "Pop", 10, 3.5);
        arraylist2539.add(song7062);
        arraylist2539.iterator();
        song7063.getAuthor();
        fifthelement.theelement.objects.Author author7714 = song7063.getAuthor();
        author7714.getUUID();
        java.util.UUID uuid90967 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author7697 = new fifthelement.theelement.objects.Author(uuid90967, "Childish Gambino", 3);
        song7063.setAuthor(author7697);
        song7063.getAlbum();
        song7067.getAuthor();
        song7067.getAlbum();
        song7053.getAuthor();
        fifthelement.theelement.objects.Album album3444 = song7053.getAlbum();
        song7062.getAuthor();
        fifthelement.theelement.objects.Author author7718 = song7062.getAuthor();
        author7718.getUUID();
        java.util.UUID uuid90969 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author7703 = new fifthelement.theelement.objects.Author(uuid90969, "Coldplay", 10);
        song7062.setAuthor(author7703);
        song7062.getAlbum();
        fifthelement.theelement.objects.Album album3446 = song7062.getAlbum();
        album3446.getUUID();
        java.util.UUID uuid90971 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID uuid90972 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author7701 = new fifthelement.theelement.objects.Author(uuid90972, "");
        album3430 = new fifthelement.theelement.objects.Album(uuid90971, "A Head Full of Dreams", author7701, list160, 10);
        album3430.getAuthor();
        fifthelement.theelement.objects.Author author7720 = album3430.getAuthor();
        author7720.getUUID();
        java.util.UUID uuid90974 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author7694 = new fifthelement.theelement.objects.Author(uuid90974, "Coldplay", 10);
        album3430.setAuthor(author7694);
        song7062.setAlbum(album3430);
        songlistservice7.sortSongs(arraylist2539);
        arraylist2539.size();
        arraylist2539.size();
        arraylist2539.size();
        arraylist2539.size();
        fifthelement.theelement.objects.Song song7068 = (fifthelement.theelement.objects.Song) arraylist2539.get(0);
        fifthelement.theelement.objects.Author author7721 = song7068.getAuthor();
        author7721.getName();
        song7068.getName();
        fifthelement.theelement.objects.Song song7069 = (fifthelement.theelement.objects.Song) arraylist2539.get(1);
        song7069.getAuthor();
        song7069.getName();
        fifthelement.theelement.objects.Song song7070 = (fifthelement.theelement.objects.Song) arraylist2539.get(2);
        song7070.getAuthor();
        song7070.getName();
        fifthelement.theelement.objects.Song song7071 = (fifthelement.theelement.objects.Song) arraylist2539.get(3);
        fifthelement.theelement.objects.Author author7724 = song7071.getAuthor();
        author7724.getName();
        song7071.getName();
        arraylist2539.size();
        arraylist2539.size();
        fifthelement.theelement.objects.Song song7072 = (fifthelement.theelement.objects.Song) arraylist2539.get(0);
        fifthelement.theelement.objects.Author author7725 = song7072.getAuthor();
        author7725.getName();
        song7072.getName();
        fifthelement.theelement.objects.Song song7073 = (fifthelement.theelement.objects.Song) arraylist2539.get(1);
        song7073.getAuthor();
        song7073.getName();
        fifthelement.theelement.objects.Song song7074 = (fifthelement.theelement.objects.Song) arraylist2539.get(2);
        song7074.getAuthor();
        song7074.getName();
        fifthelement.theelement.objects.Song song7075 = (fifthelement.theelement.objects.Song) arraylist2539.get(3);
        fifthelement.theelement.objects.Author author7728 = song7075.getAuthor();
        author7728.getName();
        song7075.getName();
        arraylist2539.size();
        fifthelement.theelement.objects.Song song7076 = (fifthelement.theelement.objects.Song) arraylist2539.get(0);
        fifthelement.theelement.objects.Author author7729 = song7076.getAuthor();
        author7729.getName();
        song7076.getName();
        fifthelement.theelement.objects.Song song7077 = (fifthelement.theelement.objects.Song) arraylist2539.get(1);
        song7077.getAuthor();
        song7077.getName();
        fifthelement.theelement.objects.Song song7078 = (fifthelement.theelement.objects.Song) arraylist2539.get(2);
        song7078.getAuthor();
        song7078.getName();
        fifthelement.theelement.objects.Song song7079 = (fifthelement.theelement.objects.Song) arraylist2539.get(3);
        fifthelement.theelement.objects.Author author7732 = song7079.getAuthor();
        author7732.getName();
        song7079.getName();
        arraylist2539.size();
        arraylist2539.size();
        fifthelement.theelement.objects.Song song7080 = (fifthelement.theelement.objects.Song) arraylist2539.get(0);
        fifthelement.theelement.objects.Author author7733 = song7080.getAuthor();
        author7733.getName();
        song7080.getName();
        fifthelement.theelement.objects.Song song7081 = (fifthelement.theelement.objects.Song) arraylist2539.get(1);
        song7081.getAuthor();
        song7081.getName();
        fifthelement.theelement.objects.Song song7082 = (fifthelement.theelement.objects.Song) arraylist2539.get(2);
        song7082.getAuthor();
        song7082.getName();
        fifthelement.theelement.objects.Song song7083 = (fifthelement.theelement.objects.Song) arraylist2539.get(3);
        fifthelement.theelement.objects.Author author7736 = song7083.getAuthor();
        author7736.getName();
        song7083.getName();
        arraylist2539.size();
        arraylist2539.size();
        fifthelement.theelement.objects.Song song7084 = (fifthelement.theelement.objects.Song) arraylist2539.get(0);
        fifthelement.theelement.objects.Author author7737 = song7084.getAuthor();
        author7737.getName();
        song7084.getName();
        fifthelement.theelement.objects.Song song7085 = (fifthelement.theelement.objects.Song) arraylist2539.get(1);
        song7085.getAuthor();
        song7085.getName();
        fifthelement.theelement.objects.Song song7086 = (fifthelement.theelement.objects.Song) arraylist2539.get(2);
        song7086.getAuthor();
        song7086.getName();
        fifthelement.theelement.objects.Song song7087 = (fifthelement.theelement.objects.Song) arraylist2539.get(3);
        fifthelement.theelement.objects.Author author7740 = song7087.getAuthor();
        author7740.getName();
        song7087.getName();
        arraylist2539.size();
        arraylist2539.get(0);
        arraylist2539.get(1);
        arraylist2539.get(2);
        arraylist2539.get(3);
        arraylist2539.size();
        arraylist2539.size();
        fifthelement.theelement.objects.Song song7092 = (fifthelement.theelement.objects.Song) arraylist2539.get(0);
        fifthelement.theelement.objects.Author author7741 = song7092.getAuthor();
        author7741.getName();
        song7092.getName();
        fifthelement.theelement.objects.Song song7093 = (fifthelement.theelement.objects.Song) arraylist2539.get(1);
        song7093.getAuthor();
        song7093.getName();
        fifthelement.theelement.objects.Song song7094 = (fifthelement.theelement.objects.Song) arraylist2539.get(2);
        song7094.getAuthor();
        song7094.getName();
        fifthelement.theelement.objects.Song song7095 = (fifthelement.theelement.objects.Song) arraylist2539.get(3);
        fifthelement.theelement.objects.Author author7744 = song7095.getAuthor();
        author7744.getName();
        song7095.getName();
        arraylist2539.size();
        arraylist2539.size();
        fifthelement.theelement.objects.Song song7096 = (fifthelement.theelement.objects.Song) arraylist2539.get(0);
        fifthelement.theelement.objects.Author author7745 = song7096.getAuthor();
        author7745.getName();
        song7096.getName();
        fifthelement.theelement.objects.Song song7097 = (fifthelement.theelement.objects.Song) arraylist2539.get(1);
        song7097.getAuthor();
        song7097.getName();
        fifthelement.theelement.objects.Song song7098 = (fifthelement.theelement.objects.Song) arraylist2539.get(2);
        fifthelement.theelement.objects.Author author7747 = song7098.getAuthor();
        song7098.getName();
        fifthelement.theelement.objects.Song song7099 = (fifthelement.theelement.objects.Song) arraylist2539.get(3);
        fifthelement.theelement.objects.Author author7748 = song7099.getAuthor();
        author7748.getName();
        song7099.getName();
        arraylist2539.size();
        arraylist2539.get(0);
        arraylist2539.get(1);
        arraylist2539.get(2);
        arraylist2539.get(3);
        arraylist2539.get(0);
        arraylist2539.size();
        arraylist2539.size();
        arraylist2539.get(0);
        arraylist2540 = new java.util.ArrayList();
        java.util.UUID uuid90975 = java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid90976 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author7699 = new fifthelement.theelement.objects.Author(uuid90976, "");
        song7052 = new fifthelement.theelement.objects.Song(uuid90975, "This Is America", "android.resource://fifthelement.theelement/raw/childish_gambino_this_is_america", author7699, album3444, "Hiphop", 3, 1.5);
        arraylist2540.add(song7052);
        java.util.UUID uuid90977 = java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        song7065 = new fifthelement.theelement.objects.Song(uuid90977, "Hall of Fame", "android.resource://fifthelement.theelement/raw/hall_of_fame", author7747, album3444, "", 3, 4.5);
        arraylist2540.add(song7065);
        java.util.UUID uuid90978 = java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        song7064 = new fifthelement.theelement.objects.Song(uuid90978, "Classical Music", "android.resource://fifthelement.theelement/raw/classical_music", author7747, album3444, "Classical", 4, 2.0);
        arraylist2540.add(song7064);
        java.util.UUID uuid90979 = java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid90980 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author7688 = new fifthelement.theelement.objects.Author(uuid90980, "");
        java.util.UUID uuid90981 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        album3435 = new fifthelement.theelement.objects.Album(uuid90981, "");
        song7055 = new fifthelement.theelement.objects.Song(uuid90979, "Adventure of a Lifetime", "android.resource://fifthelement.theelement/raw/coldplay_adventure_of_a_lifetime", author7688, album3435, "Pop", 10, 3.5);
        arraylist2540.add(song7055);
        arraylist2540.iterator();
        songlistservice7.sortSongs(arraylist2540);
        songlistservice7.setCurrentSongsList(arraylist2540);
        songlistservice7.getSongAtIndex(0);
    }
}
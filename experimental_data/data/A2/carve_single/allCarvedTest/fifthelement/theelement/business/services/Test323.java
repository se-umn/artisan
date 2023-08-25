package fifthelement.theelement.business.services;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

public class Test323 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.SettingsTest#skipSongWithSongPlayedCheck/Trace-1650046455133.txt
     * Method invocation under test: <fifthelement.theelement.business.services.SongListService: void setShuffleEnabled(boolean)>_2088_4175
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_business_services_SongListService_setShuffleEnabled_001() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        fifthelement.theelement.objects.Album album221 = null;
        fifthelement.theelement.objects.Author author445 = null;
        fifthelement.theelement.objects.Song song458 = null;
        fifthelement.theelement.objects.Song song459 = null;
        fifthelement.theelement.objects.Author author446 = null;
        fifthelement.theelement.objects.Author author447 = null;
        fifthelement.theelement.objects.Song song460 = null;
        fifthelement.theelement.objects.Album album222 = null;
        fifthelement.theelement.objects.Author author448 = null;
        fifthelement.theelement.objects.Song song461 = null;
        fifthelement.theelement.objects.Song song462 = null;
        fifthelement.theelement.objects.Song song463 = null;
        fifthelement.theelement.objects.Album album223 = null;
        java.util.ArrayList arraylist163 = null;
        fifthelement.theelement.objects.Author author449 = null;
        fifthelement.theelement.objects.Author author450 = null;
        java.util.ArrayList arraylist164 = null;
        fifthelement.theelement.objects.Author author451 = null;
        fifthelement.theelement.objects.Song song464 = null;
        fifthelement.theelement.objects.Author author452 = null;
        fifthelement.theelement.objects.Song song465 = null;
        fifthelement.theelement.objects.Author author453 = null;
        fifthelement.theelement.objects.Author author454 = null;
        fifthelement.theelement.objects.Author author455 = null;
        fifthelement.theelement.objects.Album album224 = null;
        fifthelement.theelement.objects.Song song466 = null;
        java.util.ArrayList arraylist165 = null;
        fifthelement.theelement.objects.Author author456 = null;
        fifthelement.theelement.objects.Song song467 = null;
        java.util.ArrayList arraylist166 = null;
        fifthelement.theelement.objects.Song song468 = null;
        fifthelement.theelement.objects.Author author457 = null;
        fifthelement.theelement.objects.Author author458 = null;
        fifthelement.theelement.objects.Song song469 = null;
        fifthelement.theelement.objects.Album album225 = null;
        fifthelement.theelement.objects.Author author459 = null;
        fifthelement.theelement.business.services.SongListService songlistservice5 = null;
        fifthelement.theelement.objects.Album album226 = null;
        fifthelement.theelement.objects.Album album227 = null;
        fifthelement.theelement.objects.Song song470 = null;
        fifthelement.theelement.objects.Author author460 = null;
        fifthelement.theelement.objects.Author author461 = null;
        fifthelement.theelement.objects.Author author462 = null;
        fifthelement.theelement.objects.Author author463 = null;
        fifthelement.theelement.objects.Song song471 = null;
        fifthelement.theelement.objects.Author author464 = null;
        fifthelement.theelement.objects.Song song472 = null;
        java.util.List list18 = null;
        fifthelement.theelement.objects.Song song473 = null;
        fifthelement.theelement.objects.Author author465 = null;
        fifthelement.theelement.objects.Album album228 = null;
        songlistservice5 = new fifthelement.theelement.business.services.SongListService();
        arraylist165 = new java.util.ArrayList();
        java.util.UUID uuid3617 = java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid3618 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author450 = new fifthelement.theelement.objects.Author(uuid3618, "");
        song464 = new fifthelement.theelement.objects.Song(uuid3617, "This Is America", "android.resource://fifthelement.theelement/raw/childish_gambino_this_is_america", author450, album226, "Hiphop", 3, 1.5);
        arraylist165.add(song464);
        java.util.UUID uuid3619 = java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        song465 = new fifthelement.theelement.objects.Song(uuid3619, "Hall of Fame", "android.resource://fifthelement.theelement/raw/hall_of_fame", author461, album226, "", 3, 4.5);
        arraylist165.add(song465);
        java.util.UUID uuid3620 = java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        song470 = new fifthelement.theelement.objects.Song(uuid3620, "Classical Music", "android.resource://fifthelement.theelement/raw/classical_music", author461, album226, "Classical", 4, 2.0);
        arraylist165.add(song470);
        java.util.UUID uuid3621 = java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid3622 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author448 = new fifthelement.theelement.objects.Author(uuid3622, "");
        java.util.UUID uuid3623 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        album222 = new fifthelement.theelement.objects.Album(uuid3623, "");
        song460 = new fifthelement.theelement.objects.Song(uuid3621, "Adventure of a Lifetime", "android.resource://fifthelement.theelement/raw/coldplay_adventure_of_a_lifetime", author448, album222, "Pop", 10, 3.5);
        arraylist165.add(song460);
        arraylist165.iterator();
        java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        songlistservice5.setCurrentSongsList(arraylist165);
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
        arraylist164 = new java.util.ArrayList();
        java.util.UUID uuid3688 = java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid3689 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author465 = new fifthelement.theelement.objects.Author(uuid3689, "");
        song473 = new fifthelement.theelement.objects.Song(uuid3688, "This Is America", "android.resource://fifthelement.theelement/raw/childish_gambino_this_is_america", author465, album226, "Hiphop", 3, 1.5);
        arraylist164.add(song473);
        java.util.UUID uuid3690 = java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        song469 = new fifthelement.theelement.objects.Song(uuid3690, "Hall of Fame", "android.resource://fifthelement.theelement/raw/hall_of_fame", author461, album226, "", 3, 4.5);
        arraylist164.add(song469);
        java.util.UUID uuid3691 = java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        song467 = new fifthelement.theelement.objects.Song(uuid3691, "Classical Music", "android.resource://fifthelement.theelement/raw/classical_music", author461, album226, "Classical", 4, 2.0);
        arraylist164.add(song467);
        java.util.UUID uuid3692 = java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid3693 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author446 = new fifthelement.theelement.objects.Author(uuid3693, "");
        java.util.UUID uuid3694 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        album225 = new fifthelement.theelement.objects.Album(uuid3694, "");
        song468 = new fifthelement.theelement.objects.Song(uuid3692, "Adventure of a Lifetime", "android.resource://fifthelement.theelement/raw/coldplay_adventure_of_a_lifetime", author446, album225, "Pop", 10, 3.5);
        arraylist164.add(song468);
        arraylist164.iterator();
        song473.getAuthor();
        fifthelement.theelement.objects.Author author467 = song473.getAuthor();
        author467.getUUID();
        java.util.UUID uuid3696 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author464 = new fifthelement.theelement.objects.Author(uuid3696, "Childish Gambino", 3);
        song473.setAuthor(author464);
        song473.getAlbum();
        song469.getAuthor();
        song469.getAlbum();
        fifthelement.theelement.objects.Author author469 = song467.getAuthor();
        fifthelement.theelement.objects.Album album231 = song467.getAlbum();
        song468.getAuthor();
        fifthelement.theelement.objects.Author author471 = song468.getAuthor();
        author471.getUUID();
        java.util.UUID uuid3698 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author445 = new fifthelement.theelement.objects.Author(uuid3698, "Coldplay", 10);
        song468.setAuthor(author445);
        song468.getAlbum();
        fifthelement.theelement.objects.Album album233 = song468.getAlbum();
        album233.getUUID();
        java.util.UUID uuid3700 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID uuid3701 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author457 = new fifthelement.theelement.objects.Author(uuid3701, "");
        album223 = new fifthelement.theelement.objects.Album(uuid3700, "A Head Full of Dreams", author457, list18, 10);
        album223.getAuthor();
        fifthelement.theelement.objects.Author author473 = album223.getAuthor();
        author473.getUUID();
        java.util.UUID uuid3703 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author451 = new fifthelement.theelement.objects.Author(uuid3703, "Coldplay", 10);
        album223.setAuthor(author451);
        song468.setAlbum(album223);
        songlistservice5.sortSongs(arraylist164);
        arraylist164.size();
        arraylist164.size();
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
        arraylist166 = new java.util.ArrayList();
        java.util.UUID uuid3717 = java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid3718 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author459 = new fifthelement.theelement.objects.Author(uuid3718, "");
        song463 = new fifthelement.theelement.objects.Song(uuid3717, "This Is America", "android.resource://fifthelement.theelement/raw/childish_gambino_this_is_america", author459, album231, "Hiphop", 3, 1.5);
        arraylist166.add(song463);
        java.util.UUID uuid3719 = java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        song461 = new fifthelement.theelement.objects.Song(uuid3719, "Hall of Fame", "android.resource://fifthelement.theelement/raw/hall_of_fame", author469, album231, "", 3, 4.5);
        arraylist166.add(song461);
        java.util.UUID uuid3720 = java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        song472 = new fifthelement.theelement.objects.Song(uuid3720, "Classical Music", "android.resource://fifthelement.theelement/raw/classical_music", author469, album231, "Classical", 4, 2.0);
        arraylist166.add(song472);
        java.util.UUID uuid3721 = java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid3722 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author453 = new fifthelement.theelement.objects.Author(uuid3722, "");
        java.util.UUID uuid3723 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        album227 = new fifthelement.theelement.objects.Album(uuid3723, "");
        song459 = new fifthelement.theelement.objects.Song(uuid3721, "Adventure of a Lifetime", "android.resource://fifthelement.theelement/raw/coldplay_adventure_of_a_lifetime", author453, album227, "Pop", 10, 3.5);
        arraylist166.add(song459);
        arraylist166.iterator();
        song463.getAuthor();
        fifthelement.theelement.objects.Author author475 = song463.getAuthor();
        author475.getUUID();
        java.util.UUID uuid3725 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author452 = new fifthelement.theelement.objects.Author(uuid3725, "Childish Gambino", 3);
        song463.setAuthor(author452);
        song463.getAlbum();
        song461.getAuthor();
        song461.getAlbum();
        song472.getAuthor();
        fifthelement.theelement.objects.Album album236 = song472.getAlbum();
        song459.getAuthor();
        fifthelement.theelement.objects.Author author479 = song459.getAuthor();
        author479.getUUID();
        java.util.UUID uuid3727 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author460 = new fifthelement.theelement.objects.Author(uuid3727, "Coldplay", 10);
        song459.setAuthor(author460);
        song459.getAlbum();
        fifthelement.theelement.objects.Album album238 = song459.getAlbum();
        album238.getUUID();
        java.util.UUID uuid3729 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID uuid3730 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author454 = new fifthelement.theelement.objects.Author(uuid3730, "");
        album224 = new fifthelement.theelement.objects.Album(uuid3729, "A Head Full of Dreams", author454, list18, 10);
        album224.getAuthor();
        fifthelement.theelement.objects.Author author481 = album224.getAuthor();
        author481.getUUID();
        java.util.UUID uuid3732 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author455 = new fifthelement.theelement.objects.Author(uuid3732, "Coldplay", 10);
        album224.setAuthor(author455);
        song459.setAlbum(album224);
        songlistservice5.sortSongs(arraylist166);
        arraylist166.size();
        arraylist166.size();
        arraylist166.size();
        arraylist166.size();
        fifthelement.theelement.objects.Song song474 = (fifthelement.theelement.objects.Song) arraylist166.get(0);
        fifthelement.theelement.objects.Author author482 = song474.getAuthor();
        author482.getName();
        song474.getName();
        fifthelement.theelement.objects.Song song475 = (fifthelement.theelement.objects.Song) arraylist166.get(1);
        song475.getAuthor();
        song475.getName();
        fifthelement.theelement.objects.Song song476 = (fifthelement.theelement.objects.Song) arraylist166.get(2);
        song476.getAuthor();
        song476.getName();
        fifthelement.theelement.objects.Song song477 = (fifthelement.theelement.objects.Song) arraylist166.get(3);
        fifthelement.theelement.objects.Author author485 = song477.getAuthor();
        author485.getName();
        song477.getName();
        arraylist166.size();
        arraylist166.size();
        fifthelement.theelement.objects.Song song478 = (fifthelement.theelement.objects.Song) arraylist166.get(0);
        fifthelement.theelement.objects.Author author486 = song478.getAuthor();
        author486.getName();
        song478.getName();
        fifthelement.theelement.objects.Song song479 = (fifthelement.theelement.objects.Song) arraylist166.get(1);
        song479.getAuthor();
        song479.getName();
        fifthelement.theelement.objects.Song song480 = (fifthelement.theelement.objects.Song) arraylist166.get(2);
        song480.getAuthor();
        song480.getName();
        fifthelement.theelement.objects.Song song481 = (fifthelement.theelement.objects.Song) arraylist166.get(3);
        fifthelement.theelement.objects.Author author489 = song481.getAuthor();
        author489.getName();
        song481.getName();
        arraylist166.size();
        fifthelement.theelement.objects.Song song482 = (fifthelement.theelement.objects.Song) arraylist166.get(0);
        fifthelement.theelement.objects.Author author490 = song482.getAuthor();
        author490.getName();
        song482.getName();
        fifthelement.theelement.objects.Song song483 = (fifthelement.theelement.objects.Song) arraylist166.get(1);
        song483.getAuthor();
        song483.getName();
        fifthelement.theelement.objects.Song song484 = (fifthelement.theelement.objects.Song) arraylist166.get(2);
        song484.getAuthor();
        song484.getName();
        fifthelement.theelement.objects.Song song485 = (fifthelement.theelement.objects.Song) arraylist166.get(3);
        fifthelement.theelement.objects.Author author493 = song485.getAuthor();
        author493.getName();
        song485.getName();
        arraylist166.size();
        arraylist166.size();
        fifthelement.theelement.objects.Song song486 = (fifthelement.theelement.objects.Song) arraylist166.get(0);
        fifthelement.theelement.objects.Author author494 = song486.getAuthor();
        author494.getName();
        song486.getName();
        fifthelement.theelement.objects.Song song487 = (fifthelement.theelement.objects.Song) arraylist166.get(1);
        song487.getAuthor();
        song487.getName();
        fifthelement.theelement.objects.Song song488 = (fifthelement.theelement.objects.Song) arraylist166.get(2);
        song488.getAuthor();
        song488.getName();
        fifthelement.theelement.objects.Song song489 = (fifthelement.theelement.objects.Song) arraylist166.get(3);
        fifthelement.theelement.objects.Author author497 = song489.getAuthor();
        author497.getName();
        song489.getName();
        arraylist166.size();
        arraylist166.size();
        fifthelement.theelement.objects.Song song490 = (fifthelement.theelement.objects.Song) arraylist166.get(0);
        fifthelement.theelement.objects.Author author498 = song490.getAuthor();
        author498.getName();
        song490.getName();
        fifthelement.theelement.objects.Song song491 = (fifthelement.theelement.objects.Song) arraylist166.get(1);
        song491.getAuthor();
        song491.getName();
        fifthelement.theelement.objects.Song song492 = (fifthelement.theelement.objects.Song) arraylist166.get(2);
        song492.getAuthor();
        song492.getName();
        fifthelement.theelement.objects.Song song493 = (fifthelement.theelement.objects.Song) arraylist166.get(3);
        fifthelement.theelement.objects.Author author501 = song493.getAuthor();
        author501.getName();
        song493.getName();
        arraylist166.size();
        arraylist166.get(0);
        arraylist166.get(1);
        arraylist166.get(2);
        arraylist166.get(3);
        arraylist166.size();
        arraylist166.size();
        fifthelement.theelement.objects.Song song498 = (fifthelement.theelement.objects.Song) arraylist166.get(0);
        fifthelement.theelement.objects.Author author502 = song498.getAuthor();
        author502.getName();
        song498.getName();
        fifthelement.theelement.objects.Song song499 = (fifthelement.theelement.objects.Song) arraylist166.get(1);
        song499.getAuthor();
        song499.getName();
        fifthelement.theelement.objects.Song song500 = (fifthelement.theelement.objects.Song) arraylist166.get(2);
        song500.getAuthor();
        song500.getName();
        fifthelement.theelement.objects.Song song501 = (fifthelement.theelement.objects.Song) arraylist166.get(3);
        fifthelement.theelement.objects.Author author505 = song501.getAuthor();
        author505.getName();
        song501.getName();
        arraylist166.size();
        arraylist166.size();
        fifthelement.theelement.objects.Song song502 = (fifthelement.theelement.objects.Song) arraylist166.get(0);
        fifthelement.theelement.objects.Author author506 = song502.getAuthor();
        author506.getName();
        song502.getName();
        fifthelement.theelement.objects.Song song503 = (fifthelement.theelement.objects.Song) arraylist166.get(1);
        song503.getAuthor();
        song503.getName();
        fifthelement.theelement.objects.Song song504 = (fifthelement.theelement.objects.Song) arraylist166.get(2);
        fifthelement.theelement.objects.Author author508 = song504.getAuthor();
        song504.getName();
        fifthelement.theelement.objects.Song song505 = (fifthelement.theelement.objects.Song) arraylist166.get(3);
        fifthelement.theelement.objects.Author author509 = song505.getAuthor();
        author509.getName();
        song505.getName();
        arraylist166.size();
        arraylist166.get(0);
        arraylist166.get(1);
        arraylist166.get(2);
        arraylist166.get(3);
        arraylist166.get(0);
        arraylist166.size();
        arraylist166.size();
        arraylist166.get(0);
        arraylist163 = new java.util.ArrayList();
        java.util.UUID uuid3733 = java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid3734 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author449 = new fifthelement.theelement.objects.Author(uuid3734, "");
        song462 = new fifthelement.theelement.objects.Song(uuid3733, "This Is America", "android.resource://fifthelement.theelement/raw/childish_gambino_this_is_america", author449, album236, "Hiphop", 3, 1.5);
        arraylist163.add(song462);
        java.util.UUID uuid3735 = java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        song471 = new fifthelement.theelement.objects.Song(uuid3735, "Hall of Fame", "android.resource://fifthelement.theelement/raw/hall_of_fame", author508, album236, "", 3, 4.5);
        arraylist163.add(song471);
        java.util.UUID uuid3736 = java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        song466 = new fifthelement.theelement.objects.Song(uuid3736, "Classical Music", "android.resource://fifthelement.theelement/raw/classical_music", author508, album236, "Classical", 4, 2.0);
        arraylist163.add(song466);
        java.util.UUID uuid3737 = java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid3738 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author456 = new fifthelement.theelement.objects.Author(uuid3738, "");
        java.util.UUID uuid3739 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        album228 = new fifthelement.theelement.objects.Album(uuid3739, "");
        song458 = new fifthelement.theelement.objects.Song(uuid3737, "Adventure of a Lifetime", "android.resource://fifthelement.theelement/raw/coldplay_adventure_of_a_lifetime", author456, album228, "Pop", 10, 3.5);
        arraylist163.add(song458);
        arraylist163.iterator();
        song462.getAuthor();
        fifthelement.theelement.objects.Author author511 = song462.getAuthor();
        author511.getUUID();
        java.util.UUID uuid3741 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author447 = new fifthelement.theelement.objects.Author(uuid3741, "Childish Gambino", 3);
        song462.setAuthor(author447);
        song462.getAlbum();
        song471.getAuthor();
        song471.getAlbum();
        song466.getAuthor();
        song466.getAlbum();
        song458.getAuthor();
        fifthelement.theelement.objects.Author author515 = song458.getAuthor();
        author515.getUUID();
        java.util.UUID uuid3743 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author458 = new fifthelement.theelement.objects.Author(uuid3743, "Coldplay", 10);
        song458.setAuthor(author458);
        song458.getAlbum();
        fifthelement.theelement.objects.Album album243 = song458.getAlbum();
        album243.getUUID();
        java.util.UUID uuid3745 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID uuid3746 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author463 = new fifthelement.theelement.objects.Author(uuid3746, "");
        album221 = new fifthelement.theelement.objects.Album(uuid3745, "A Head Full of Dreams", author463, list18, 10);
        album221.getAuthor();
        fifthelement.theelement.objects.Author author517 = album221.getAuthor();
        author517.getUUID();
        java.util.UUID uuid3748 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author462 = new fifthelement.theelement.objects.Author(uuid3748, "Coldplay", 10);
        album221.setAuthor(author462);
        song458.setAlbum(album221);
        songlistservice5.sortSongs(arraylist163);
        songlistservice5.setCurrentSongsList(arraylist163);
        songlistservice5.getSongAtIndex(0);
        songlistservice5.setShuffleEnabled(false);
    }
}

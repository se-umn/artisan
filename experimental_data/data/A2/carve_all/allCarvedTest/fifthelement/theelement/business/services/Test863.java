package fifthelement.theelement.business.services;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

public class Test863 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.CustomizeTests#skipSongWithSongPlayedCheck/Trace-1650046492781.txt
     * Method invocation under test: <fifthelement.theelement.business.services.SongListService: fifthelement.theelement.objects.Song getSongAtIndex(int)>_2751_5500
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_business_services_SongListService_getSongAtIndex_003() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        fifthelement.theelement.objects.Song song11564 = null;
        fifthelement.theelement.objects.Song song11565 = null;
        fifthelement.theelement.objects.Song song11566 = null;
        fifthelement.theelement.objects.Author author11575 = null;
        java.util.ArrayList arraylist6105 = null;
        fifthelement.theelement.objects.Album album5494 = null;
        java.util.ArrayList arraylist6106 = null;
        java.util.ArrayList arraylist6107 = null;
        fifthelement.theelement.objects.Album album5495 = null;
        fifthelement.theelement.objects.Song song11567 = null;
        fifthelement.theelement.objects.Author author11576 = null;
        fifthelement.theelement.objects.Author author11577 = null;
        fifthelement.theelement.objects.Author author11578 = null;
        fifthelement.theelement.objects.Author author11579 = null;
        fifthelement.theelement.objects.Song song11568 = null;
        fifthelement.theelement.objects.Song song11569 = null;
        fifthelement.theelement.objects.Album album5496 = null;
        fifthelement.theelement.objects.Album album5497 = null;
        fifthelement.theelement.objects.Author author11580 = null;
        fifthelement.theelement.objects.Author author11581 = null;
        fifthelement.theelement.objects.Author author11582 = null;
        fifthelement.theelement.objects.Author author11583 = null;
        java.util.ArrayList arraylist6108 = null;
        fifthelement.theelement.objects.Song song11570 = null;
        fifthelement.theelement.objects.Song song11571 = null;
        fifthelement.theelement.objects.Album album5498 = null;
        fifthelement.theelement.objects.Author author11584 = null;
        fifthelement.theelement.objects.Song song11572 = null;
        fifthelement.theelement.objects.Song song11573 = null;
        fifthelement.theelement.objects.Author author11585 = null;
        fifthelement.theelement.objects.Album album5499 = null;
        fifthelement.theelement.objects.Song song11574 = null;
        fifthelement.theelement.objects.Author author11586 = null;
        fifthelement.theelement.objects.Album album5500 = null;
        fifthelement.theelement.business.services.SongListService songlistservice473 = null;
        fifthelement.theelement.objects.Author author11587 = null;
        fifthelement.theelement.objects.Author author11588 = null;
        fifthelement.theelement.objects.Album album5501 = null;
        fifthelement.theelement.objects.Author author11589 = null;
        fifthelement.theelement.objects.Author author11590 = null;
        fifthelement.theelement.objects.Author author11591 = null;
        fifthelement.theelement.objects.Author author11592 = null;
        fifthelement.theelement.objects.Song song11575 = null;
        fifthelement.theelement.objects.Author author11593 = null;
        fifthelement.theelement.objects.Song song11576 = null;
        fifthelement.theelement.objects.Song song11577 = null;
        java.util.List list283 = null;
        fifthelement.theelement.objects.Author author11594 = null;
        fifthelement.theelement.objects.Song song11578 = null;
        fifthelement.theelement.objects.Song song11579 = null;
        fifthelement.theelement.objects.Author author11595 = null;
        songlistservice473 = new fifthelement.theelement.business.services.SongListService();
        arraylist6108 = new java.util.ArrayList();
        java.util.UUID uuid150561 = java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid150562 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author11577 = new fifthelement.theelement.objects.Author(uuid150562, "");
        song11575 = new fifthelement.theelement.objects.Song(uuid150561, "This Is America", "android.resource://fifthelement.theelement/raw/childish_gambino_this_is_america", author11577, album5500, "Hiphop", 3, 1.5);
        arraylist6108.add(song11575);
        java.util.UUID uuid150563 = java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        song11577 = new fifthelement.theelement.objects.Song(uuid150563, "Hall of Fame", "android.resource://fifthelement.theelement/raw/hall_of_fame", author11588, album5500, "", 3, 4.5);
        arraylist6108.add(song11577);
        java.util.UUID uuid150564 = java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        song11569 = new fifthelement.theelement.objects.Song(uuid150564, "Classical Music", "android.resource://fifthelement.theelement/raw/classical_music", author11588, album5500, "Classical", 4, 2.0);
        arraylist6108.add(song11569);
        java.util.UUID uuid150565 = java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid150566 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author11575 = new fifthelement.theelement.objects.Author(uuid150566, "");
        java.util.UUID uuid150567 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        album5498 = new fifthelement.theelement.objects.Album(uuid150567, "");
        song11579 = new fifthelement.theelement.objects.Song(uuid150565, "Adventure of a Lifetime", "android.resource://fifthelement.theelement/raw/coldplay_adventure_of_a_lifetime", author11575, album5498, "Pop", 10, 3.5);
        arraylist6108.add(song11579);
        arraylist6108.iterator();
        java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        songlistservice473.setCurrentSongsList(arraylist6108);
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
        arraylist6107 = new java.util.ArrayList();
        java.util.UUID uuid150632 = java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid150633 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author11579 = new fifthelement.theelement.objects.Author(uuid150633, "");
        song11566 = new fifthelement.theelement.objects.Song(uuid150632, "This Is America", "android.resource://fifthelement.theelement/raw/childish_gambino_this_is_america", author11579, album5500, "Hiphop", 3, 1.5);
        arraylist6107.add(song11566);
        java.util.UUID uuid150634 = java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        song11568 = new fifthelement.theelement.objects.Song(uuid150634, "Hall of Fame", "android.resource://fifthelement.theelement/raw/hall_of_fame", author11588, album5500, "", 3, 4.5);
        arraylist6107.add(song11568);
        java.util.UUID uuid150635 = java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        song11570 = new fifthelement.theelement.objects.Song(uuid150635, "Classical Music", "android.resource://fifthelement.theelement/raw/classical_music", author11588, album5500, "Classical", 4, 2.0);
        arraylist6107.add(song11570);
        java.util.UUID uuid150636 = java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid150637 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author11592 = new fifthelement.theelement.objects.Author(uuid150637, "");
        java.util.UUID uuid150638 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        album5494 = new fifthelement.theelement.objects.Album(uuid150638, "");
        song11572 = new fifthelement.theelement.objects.Song(uuid150636, "Adventure of a Lifetime", "android.resource://fifthelement.theelement/raw/coldplay_adventure_of_a_lifetime", author11592, album5494, "Pop", 10, 3.5);
        arraylist6107.add(song11572);
        arraylist6107.iterator();
        song11566.getAuthor();
        fifthelement.theelement.objects.Author author11597 = song11566.getAuthor();
        author11597.getUUID();
        java.util.UUID uuid150640 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author11591 = new fifthelement.theelement.objects.Author(uuid150640, "Childish Gambino", 3);
        song11566.setAuthor(author11591);
        song11566.getAlbum();
        song11568.getAuthor();
        song11568.getAlbum();
        fifthelement.theelement.objects.Author author11599 = song11570.getAuthor();
        fifthelement.theelement.objects.Album album5504 = song11570.getAlbum();
        song11572.getAuthor();
        fifthelement.theelement.objects.Author author11601 = song11572.getAuthor();
        author11601.getUUID();
        java.util.UUID uuid150642 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author11594 = new fifthelement.theelement.objects.Author(uuid150642, "Coldplay", 10);
        song11572.setAuthor(author11594);
        song11572.getAlbum();
        fifthelement.theelement.objects.Album album5506 = song11572.getAlbum();
        album5506.getUUID();
        java.util.UUID uuid150644 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID uuid150645 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author11590 = new fifthelement.theelement.objects.Author(uuid150645, "");
        album5496 = new fifthelement.theelement.objects.Album(uuid150644, "A Head Full of Dreams", author11590, list283, 10);
        album5496.getAuthor();
        fifthelement.theelement.objects.Author author11603 = album5496.getAuthor();
        author11603.getUUID();
        java.util.UUID uuid150647 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author11586 = new fifthelement.theelement.objects.Author(uuid150647, "Coldplay", 10);
        album5496.setAuthor(author11586);
        song11572.setAlbum(album5496);
        songlistservice473.sortSongs(arraylist6107);
        arraylist6107.size();
        arraylist6107.size();
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
        arraylist6106 = new java.util.ArrayList();
        java.util.UUID uuid150661 = java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid150662 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author11584 = new fifthelement.theelement.objects.Author(uuid150662, "");
        song11564 = new fifthelement.theelement.objects.Song(uuid150661, "This Is America", "android.resource://fifthelement.theelement/raw/childish_gambino_this_is_america", author11584, album5504, "Hiphop", 3, 1.5);
        arraylist6106.add(song11564);
        java.util.UUID uuid150663 = java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        song11576 = new fifthelement.theelement.objects.Song(uuid150663, "Hall of Fame", "android.resource://fifthelement.theelement/raw/hall_of_fame", author11599, album5504, "", 3, 4.5);
        arraylist6106.add(song11576);
        java.util.UUID uuid150664 = java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        song11573 = new fifthelement.theelement.objects.Song(uuid150664, "Classical Music", "android.resource://fifthelement.theelement/raw/classical_music", author11599, album5504, "Classical", 4, 2.0);
        arraylist6106.add(song11573);
        java.util.UUID uuid150665 = java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid150666 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author11578 = new fifthelement.theelement.objects.Author(uuid150666, "");
        java.util.UUID uuid150667 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        album5497 = new fifthelement.theelement.objects.Album(uuid150667, "");
        song11574 = new fifthelement.theelement.objects.Song(uuid150665, "Adventure of a Lifetime", "android.resource://fifthelement.theelement/raw/coldplay_adventure_of_a_lifetime", author11578, album5497, "Pop", 10, 3.5);
        arraylist6106.add(song11574);
        arraylist6106.iterator();
        song11564.getAuthor();
        fifthelement.theelement.objects.Author author11605 = song11564.getAuthor();
        author11605.getUUID();
        java.util.UUID uuid150669 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author11580 = new fifthelement.theelement.objects.Author(uuid150669, "Childish Gambino", 3);
        song11564.setAuthor(author11580);
        song11564.getAlbum();
        song11576.getAuthor();
        song11576.getAlbum();
        song11573.getAuthor();
        fifthelement.theelement.objects.Album album5509 = song11573.getAlbum();
        song11574.getAuthor();
        fifthelement.theelement.objects.Author author11609 = song11574.getAuthor();
        author11609.getUUID();
        java.util.UUID uuid150671 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author11581 = new fifthelement.theelement.objects.Author(uuid150671, "Coldplay", 10);
        song11574.setAuthor(author11581);
        song11574.getAlbum();
        fifthelement.theelement.objects.Album album5511 = song11574.getAlbum();
        album5511.getUUID();
        java.util.UUID uuid150673 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID uuid150674 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author11582 = new fifthelement.theelement.objects.Author(uuid150674, "");
        album5495 = new fifthelement.theelement.objects.Album(uuid150673, "A Head Full of Dreams", author11582, list283, 10);
        album5495.getAuthor();
        fifthelement.theelement.objects.Author author11611 = album5495.getAuthor();
        author11611.getUUID();
        java.util.UUID uuid150676 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author11576 = new fifthelement.theelement.objects.Author(uuid150676, "Coldplay", 10);
        album5495.setAuthor(author11576);
        song11574.setAlbum(album5495);
        songlistservice473.sortSongs(arraylist6106);
        arraylist6106.size();
        arraylist6106.size();
        arraylist6106.size();
        arraylist6106.size();
        fifthelement.theelement.objects.Song song11580 = (fifthelement.theelement.objects.Song) arraylist6106.get(0);
        fifthelement.theelement.objects.Author author11612 = song11580.getAuthor();
        author11612.getName();
        song11580.getName();
        fifthelement.theelement.objects.Song song11581 = (fifthelement.theelement.objects.Song) arraylist6106.get(1);
        song11581.getAuthor();
        song11581.getName();
        fifthelement.theelement.objects.Song song11582 = (fifthelement.theelement.objects.Song) arraylist6106.get(2);
        song11582.getAuthor();
        song11582.getName();
        fifthelement.theelement.objects.Song song11583 = (fifthelement.theelement.objects.Song) arraylist6106.get(3);
        fifthelement.theelement.objects.Author author11615 = song11583.getAuthor();
        author11615.getName();
        song11583.getName();
        arraylist6106.size();
        arraylist6106.size();
        fifthelement.theelement.objects.Song song11584 = (fifthelement.theelement.objects.Song) arraylist6106.get(0);
        fifthelement.theelement.objects.Author author11616 = song11584.getAuthor();
        author11616.getName();
        song11584.getName();
        fifthelement.theelement.objects.Song song11585 = (fifthelement.theelement.objects.Song) arraylist6106.get(1);
        song11585.getAuthor();
        song11585.getName();
        fifthelement.theelement.objects.Song song11586 = (fifthelement.theelement.objects.Song) arraylist6106.get(2);
        song11586.getAuthor();
        song11586.getName();
        fifthelement.theelement.objects.Song song11587 = (fifthelement.theelement.objects.Song) arraylist6106.get(3);
        fifthelement.theelement.objects.Author author11619 = song11587.getAuthor();
        author11619.getName();
        song11587.getName();
        arraylist6106.size();
        fifthelement.theelement.objects.Song song11588 = (fifthelement.theelement.objects.Song) arraylist6106.get(0);
        fifthelement.theelement.objects.Author author11620 = song11588.getAuthor();
        author11620.getName();
        song11588.getName();
        fifthelement.theelement.objects.Song song11589 = (fifthelement.theelement.objects.Song) arraylist6106.get(1);
        song11589.getAuthor();
        song11589.getName();
        fifthelement.theelement.objects.Song song11590 = (fifthelement.theelement.objects.Song) arraylist6106.get(2);
        song11590.getAuthor();
        song11590.getName();
        fifthelement.theelement.objects.Song song11591 = (fifthelement.theelement.objects.Song) arraylist6106.get(3);
        fifthelement.theelement.objects.Author author11623 = song11591.getAuthor();
        author11623.getName();
        song11591.getName();
        arraylist6106.size();
        arraylist6106.size();
        fifthelement.theelement.objects.Song song11592 = (fifthelement.theelement.objects.Song) arraylist6106.get(0);
        fifthelement.theelement.objects.Author author11624 = song11592.getAuthor();
        author11624.getName();
        song11592.getName();
        fifthelement.theelement.objects.Song song11593 = (fifthelement.theelement.objects.Song) arraylist6106.get(1);
        song11593.getAuthor();
        song11593.getName();
        fifthelement.theelement.objects.Song song11594 = (fifthelement.theelement.objects.Song) arraylist6106.get(2);
        song11594.getAuthor();
        song11594.getName();
        fifthelement.theelement.objects.Song song11595 = (fifthelement.theelement.objects.Song) arraylist6106.get(3);
        fifthelement.theelement.objects.Author author11627 = song11595.getAuthor();
        author11627.getName();
        song11595.getName();
        arraylist6106.size();
        arraylist6106.size();
        fifthelement.theelement.objects.Song song11596 = (fifthelement.theelement.objects.Song) arraylist6106.get(0);
        fifthelement.theelement.objects.Author author11628 = song11596.getAuthor();
        author11628.getName();
        song11596.getName();
        fifthelement.theelement.objects.Song song11597 = (fifthelement.theelement.objects.Song) arraylist6106.get(1);
        song11597.getAuthor();
        song11597.getName();
        fifthelement.theelement.objects.Song song11598 = (fifthelement.theelement.objects.Song) arraylist6106.get(2);
        song11598.getAuthor();
        song11598.getName();
        fifthelement.theelement.objects.Song song11599 = (fifthelement.theelement.objects.Song) arraylist6106.get(3);
        fifthelement.theelement.objects.Author author11631 = song11599.getAuthor();
        author11631.getName();
        song11599.getName();
        arraylist6106.size();
        arraylist6106.get(0);
        arraylist6106.get(1);
        arraylist6106.get(2);
        arraylist6106.get(3);
        arraylist6106.size();
        arraylist6106.size();
        fifthelement.theelement.objects.Song song11604 = (fifthelement.theelement.objects.Song) arraylist6106.get(0);
        fifthelement.theelement.objects.Author author11632 = song11604.getAuthor();
        author11632.getName();
        song11604.getName();
        fifthelement.theelement.objects.Song song11605 = (fifthelement.theelement.objects.Song) arraylist6106.get(1);
        song11605.getAuthor();
        song11605.getName();
        fifthelement.theelement.objects.Song song11606 = (fifthelement.theelement.objects.Song) arraylist6106.get(2);
        song11606.getAuthor();
        song11606.getName();
        fifthelement.theelement.objects.Song song11607 = (fifthelement.theelement.objects.Song) arraylist6106.get(3);
        fifthelement.theelement.objects.Author author11635 = song11607.getAuthor();
        author11635.getName();
        song11607.getName();
        arraylist6106.size();
        arraylist6106.size();
        fifthelement.theelement.objects.Song song11608 = (fifthelement.theelement.objects.Song) arraylist6106.get(0);
        fifthelement.theelement.objects.Author author11636 = song11608.getAuthor();
        author11636.getName();
        song11608.getName();
        fifthelement.theelement.objects.Song song11609 = (fifthelement.theelement.objects.Song) arraylist6106.get(1);
        song11609.getAuthor();
        song11609.getName();
        fifthelement.theelement.objects.Song song11610 = (fifthelement.theelement.objects.Song) arraylist6106.get(2);
        fifthelement.theelement.objects.Author author11638 = song11610.getAuthor();
        song11610.getName();
        fifthelement.theelement.objects.Song song11611 = (fifthelement.theelement.objects.Song) arraylist6106.get(3);
        fifthelement.theelement.objects.Author author11639 = song11611.getAuthor();
        author11639.getName();
        song11611.getName();
        arraylist6106.size();
        arraylist6106.get(0);
        arraylist6106.get(1);
        arraylist6106.get(2);
        arraylist6106.get(3);
        arraylist6106.get(0);
        arraylist6106.size();
        arraylist6106.size();
        arraylist6106.get(0);
        arraylist6105 = new java.util.ArrayList();
        java.util.UUID uuid150677 = java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid150678 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author11583 = new fifthelement.theelement.objects.Author(uuid150678, "");
        song11571 = new fifthelement.theelement.objects.Song(uuid150677, "This Is America", "android.resource://fifthelement.theelement/raw/childish_gambino_this_is_america", author11583, album5509, "Hiphop", 3, 1.5);
        arraylist6105.add(song11571);
        java.util.UUID uuid150679 = java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        song11567 = new fifthelement.theelement.objects.Song(uuid150679, "Hall of Fame", "android.resource://fifthelement.theelement/raw/hall_of_fame", author11638, album5509, "", 3, 4.5);
        arraylist6105.add(song11567);
        java.util.UUID uuid150680 = java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        song11565 = new fifthelement.theelement.objects.Song(uuid150680, "Classical Music", "android.resource://fifthelement.theelement/raw/classical_music", author11638, album5509, "Classical", 4, 2.0);
        arraylist6105.add(song11565);
        java.util.UUID uuid150681 = java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid150682 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author11595 = new fifthelement.theelement.objects.Author(uuid150682, "");
        java.util.UUID uuid150683 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        album5499 = new fifthelement.theelement.objects.Album(uuid150683, "");
        song11578 = new fifthelement.theelement.objects.Song(uuid150681, "Adventure of a Lifetime", "android.resource://fifthelement.theelement/raw/coldplay_adventure_of_a_lifetime", author11595, album5499, "Pop", 10, 3.5);
        arraylist6105.add(song11578);
        arraylist6105.iterator();
        song11571.getAuthor();
        fifthelement.theelement.objects.Author author11641 = song11571.getAuthor();
        author11641.getUUID();
        java.util.UUID uuid150685 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author11589 = new fifthelement.theelement.objects.Author(uuid150685, "Childish Gambino", 3);
        song11571.setAuthor(author11589);
        song11571.getAlbum();
        song11567.getAuthor();
        song11567.getAlbum();
        song11565.getAuthor();
        song11565.getAlbum();
        song11578.getAuthor();
        fifthelement.theelement.objects.Author author11645 = song11578.getAuthor();
        author11645.getUUID();
        java.util.UUID uuid150687 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author11593 = new fifthelement.theelement.objects.Author(uuid150687, "Coldplay", 10);
        song11578.setAuthor(author11593);
        song11578.getAlbum();
        fifthelement.theelement.objects.Album album5516 = song11578.getAlbum();
        album5516.getUUID();
        java.util.UUID uuid150689 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID uuid150690 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author11585 = new fifthelement.theelement.objects.Author(uuid150690, "");
        album5501 = new fifthelement.theelement.objects.Album(uuid150689, "A Head Full of Dreams", author11585, list283, 10);
        album5501.getAuthor();
        fifthelement.theelement.objects.Author author11647 = album5501.getAuthor();
        author11647.getUUID();
        java.util.UUID uuid150692 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author11587 = new fifthelement.theelement.objects.Author(uuid150692, "Coldplay", 10);
        album5501.setAuthor(author11587);
        song11578.setAlbum(album5501);
        songlistservice473.sortSongs(arraylist6105);
        songlistservice473.setCurrentSongsList(arraylist6105);
        fifthelement.theelement.objects.Song song11618 = songlistservice473.getSongAtIndex(0);
        song11618.getPath();
        songlistservice473.setShuffleEnabled(false);
        java.util.UUID uuid150693 = song11618.getUUID();
        uuid150693.toString();
        song11618.getName();
        song11618.getPath();
        song11618.getPath();
        song11618.getPath();
        song11618.getPath();
        song11618.getName();
        song11618.getName();
        song11618.getPath();
        song11618.getPath();
        song11618.getPath();
        song11618.getPath();
        song11618.getAuthor();
        fifthelement.theelement.objects.Author author11649 = song11618.getAuthor();
        author11649.getName();
        fifthelement.theelement.objects.Author author11650 = song11618.getAuthor();
        author11650.getName();
        song11618.getAlbum();
        fifthelement.theelement.objects.Album album5518 = song11618.getAlbum();
        album5518.getName();
        songlistservice473.getCurrentSongPlayingIndex();
        fifthelement.theelement.objects.Song song11619 = songlistservice473.getSongAtIndex(0);
        song11619.getUUID();
        songlistservice473.skipToNextSong();
        songlistservice473.getCurrentSongPlayingIndex();
        songlistservice473.getSongAtIndex(1);
    }
}

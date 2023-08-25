package fifthelement.theelement.business.services;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

public class Test884 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.CustomizeTests#skipSongWithSongPlayedCheck/Trace-1650046492781.txt
     * Method invocation under test: <fifthelement.theelement.business.services.SongListService: fifthelement.theelement.objects.Song skipToNextSong()>_2788_5574
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_business_services_SongListService_skipToNextSong_002() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        fifthelement.theelement.objects.Song song11857 = null;
        fifthelement.theelement.objects.Song song11858 = null;
        fifthelement.theelement.objects.Song song11859 = null;
        fifthelement.theelement.objects.Author author11821 = null;
        java.util.ArrayList arraylist6171 = null;
        fifthelement.theelement.objects.Album album5648 = null;
        java.util.ArrayList arraylist6172 = null;
        java.util.ArrayList arraylist6173 = null;
        fifthelement.theelement.objects.Album album5649 = null;
        fifthelement.theelement.objects.Song song11860 = null;
        fifthelement.theelement.objects.Author author11822 = null;
        fifthelement.theelement.objects.Author author11823 = null;
        fifthelement.theelement.objects.Author author11824 = null;
        fifthelement.theelement.objects.Author author11825 = null;
        fifthelement.theelement.objects.Song song11861 = null;
        fifthelement.theelement.objects.Song song11862 = null;
        fifthelement.theelement.objects.Album album5650 = null;
        fifthelement.theelement.objects.Album album5651 = null;
        fifthelement.theelement.objects.Author author11826 = null;
        fifthelement.theelement.objects.Author author11827 = null;
        fifthelement.theelement.objects.Author author11828 = null;
        fifthelement.theelement.objects.Author author11829 = null;
        java.util.ArrayList arraylist6174 = null;
        fifthelement.theelement.objects.Song song11863 = null;
        fifthelement.theelement.objects.Song song11864 = null;
        fifthelement.theelement.objects.Album album5652 = null;
        fifthelement.theelement.objects.Author author11830 = null;
        fifthelement.theelement.objects.Song song11865 = null;
        fifthelement.theelement.objects.Song song11866 = null;
        fifthelement.theelement.objects.Author author11831 = null;
        fifthelement.theelement.objects.Album album5653 = null;
        fifthelement.theelement.objects.Song song11867 = null;
        fifthelement.theelement.objects.Author author11832 = null;
        fifthelement.theelement.objects.Album album5654 = null;
        fifthelement.theelement.business.services.SongListService songlistservice474 = null;
        fifthelement.theelement.objects.Author author11833 = null;
        fifthelement.theelement.objects.Author author11834 = null;
        fifthelement.theelement.objects.Album album5655 = null;
        fifthelement.theelement.objects.Author author11835 = null;
        fifthelement.theelement.objects.Author author11836 = null;
        fifthelement.theelement.objects.Author author11837 = null;
        fifthelement.theelement.objects.Author author11838 = null;
        fifthelement.theelement.objects.Song song11868 = null;
        fifthelement.theelement.objects.Author author11839 = null;
        fifthelement.theelement.objects.Song song11869 = null;
        fifthelement.theelement.objects.Song song11870 = null;
        java.util.List list287 = null;
        fifthelement.theelement.objects.Author author11840 = null;
        fifthelement.theelement.objects.Song song11871 = null;
        fifthelement.theelement.objects.Song song11872 = null;
        fifthelement.theelement.objects.Author author11841 = null;
        songlistservice474 = new fifthelement.theelement.business.services.SongListService();
        arraylist6174 = new java.util.ArrayList();
        java.util.UUID uuid157141 = java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid157142 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author11823 = new fifthelement.theelement.objects.Author(uuid157142, "");
        song11868 = new fifthelement.theelement.objects.Song(uuid157141, "This Is America", "android.resource://fifthelement.theelement/raw/childish_gambino_this_is_america", author11823, album5654, "Hiphop", 3, 1.5);
        arraylist6174.add(song11868);
        java.util.UUID uuid157143 = java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        song11870 = new fifthelement.theelement.objects.Song(uuid157143, "Hall of Fame", "android.resource://fifthelement.theelement/raw/hall_of_fame", author11834, album5654, "", 3, 4.5);
        arraylist6174.add(song11870);
        java.util.UUID uuid157144 = java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        song11862 = new fifthelement.theelement.objects.Song(uuid157144, "Classical Music", "android.resource://fifthelement.theelement/raw/classical_music", author11834, album5654, "Classical", 4, 2.0);
        arraylist6174.add(song11862);
        java.util.UUID uuid157145 = java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid157146 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author11821 = new fifthelement.theelement.objects.Author(uuid157146, "");
        java.util.UUID uuid157147 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        album5652 = new fifthelement.theelement.objects.Album(uuid157147, "");
        song11872 = new fifthelement.theelement.objects.Song(uuid157145, "Adventure of a Lifetime", "android.resource://fifthelement.theelement/raw/coldplay_adventure_of_a_lifetime", author11821, album5652, "Pop", 10, 3.5);
        arraylist6174.add(song11872);
        arraylist6174.iterator();
        java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        songlistservice474.setCurrentSongsList(arraylist6174);
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
        arraylist6173 = new java.util.ArrayList();
        java.util.UUID uuid157212 = java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid157213 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author11825 = new fifthelement.theelement.objects.Author(uuid157213, "");
        song11859 = new fifthelement.theelement.objects.Song(uuid157212, "This Is America", "android.resource://fifthelement.theelement/raw/childish_gambino_this_is_america", author11825, album5654, "Hiphop", 3, 1.5);
        arraylist6173.add(song11859);
        java.util.UUID uuid157214 = java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        song11861 = new fifthelement.theelement.objects.Song(uuid157214, "Hall of Fame", "android.resource://fifthelement.theelement/raw/hall_of_fame", author11834, album5654, "", 3, 4.5);
        arraylist6173.add(song11861);
        java.util.UUID uuid157215 = java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        song11863 = new fifthelement.theelement.objects.Song(uuid157215, "Classical Music", "android.resource://fifthelement.theelement/raw/classical_music", author11834, album5654, "Classical", 4, 2.0);
        arraylist6173.add(song11863);
        java.util.UUID uuid157216 = java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid157217 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author11838 = new fifthelement.theelement.objects.Author(uuid157217, "");
        java.util.UUID uuid157218 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        album5648 = new fifthelement.theelement.objects.Album(uuid157218, "");
        song11865 = new fifthelement.theelement.objects.Song(uuid157216, "Adventure of a Lifetime", "android.resource://fifthelement.theelement/raw/coldplay_adventure_of_a_lifetime", author11838, album5648, "Pop", 10, 3.5);
        arraylist6173.add(song11865);
        arraylist6173.iterator();
        song11859.getAuthor();
        fifthelement.theelement.objects.Author author11843 = song11859.getAuthor();
        author11843.getUUID();
        java.util.UUID uuid157220 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author11837 = new fifthelement.theelement.objects.Author(uuid157220, "Childish Gambino", 3);
        song11859.setAuthor(author11837);
        song11859.getAlbum();
        song11861.getAuthor();
        song11861.getAlbum();
        fifthelement.theelement.objects.Author author11845 = song11863.getAuthor();
        fifthelement.theelement.objects.Album album5658 = song11863.getAlbum();
        song11865.getAuthor();
        fifthelement.theelement.objects.Author author11847 = song11865.getAuthor();
        author11847.getUUID();
        java.util.UUID uuid157222 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author11840 = new fifthelement.theelement.objects.Author(uuid157222, "Coldplay", 10);
        song11865.setAuthor(author11840);
        song11865.getAlbum();
        fifthelement.theelement.objects.Album album5660 = song11865.getAlbum();
        album5660.getUUID();
        java.util.UUID uuid157224 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID uuid157225 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author11836 = new fifthelement.theelement.objects.Author(uuid157225, "");
        album5650 = new fifthelement.theelement.objects.Album(uuid157224, "A Head Full of Dreams", author11836, list287, 10);
        album5650.getAuthor();
        fifthelement.theelement.objects.Author author11849 = album5650.getAuthor();
        author11849.getUUID();
        java.util.UUID uuid157227 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author11832 = new fifthelement.theelement.objects.Author(uuid157227, "Coldplay", 10);
        album5650.setAuthor(author11832);
        song11865.setAlbum(album5650);
        songlistservice474.sortSongs(arraylist6173);
        arraylist6173.size();
        arraylist6173.size();
        songlistservice474.getAutoplayEnabled();
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
        arraylist6172 = new java.util.ArrayList();
        java.util.UUID uuid157241 = java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid157242 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author11830 = new fifthelement.theelement.objects.Author(uuid157242, "");
        song11857 = new fifthelement.theelement.objects.Song(uuid157241, "This Is America", "android.resource://fifthelement.theelement/raw/childish_gambino_this_is_america", author11830, album5658, "Hiphop", 3, 1.5);
        arraylist6172.add(song11857);
        java.util.UUID uuid157243 = java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        song11869 = new fifthelement.theelement.objects.Song(uuid157243, "Hall of Fame", "android.resource://fifthelement.theelement/raw/hall_of_fame", author11845, album5658, "", 3, 4.5);
        arraylist6172.add(song11869);
        java.util.UUID uuid157244 = java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        song11866 = new fifthelement.theelement.objects.Song(uuid157244, "Classical Music", "android.resource://fifthelement.theelement/raw/classical_music", author11845, album5658, "Classical", 4, 2.0);
        arraylist6172.add(song11866);
        java.util.UUID uuid157245 = java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid157246 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author11824 = new fifthelement.theelement.objects.Author(uuid157246, "");
        java.util.UUID uuid157247 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        album5651 = new fifthelement.theelement.objects.Album(uuid157247, "");
        song11867 = new fifthelement.theelement.objects.Song(uuid157245, "Adventure of a Lifetime", "android.resource://fifthelement.theelement/raw/coldplay_adventure_of_a_lifetime", author11824, album5651, "Pop", 10, 3.5);
        arraylist6172.add(song11867);
        arraylist6172.iterator();
        song11857.getAuthor();
        fifthelement.theelement.objects.Author author11851 = song11857.getAuthor();
        author11851.getUUID();
        java.util.UUID uuid157249 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author11826 = new fifthelement.theelement.objects.Author(uuid157249, "Childish Gambino", 3);
        song11857.setAuthor(author11826);
        song11857.getAlbum();
        song11869.getAuthor();
        song11869.getAlbum();
        song11866.getAuthor();
        fifthelement.theelement.objects.Album album5663 = song11866.getAlbum();
        song11867.getAuthor();
        fifthelement.theelement.objects.Author author11855 = song11867.getAuthor();
        author11855.getUUID();
        java.util.UUID uuid157251 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author11827 = new fifthelement.theelement.objects.Author(uuid157251, "Coldplay", 10);
        song11867.setAuthor(author11827);
        song11867.getAlbum();
        fifthelement.theelement.objects.Album album5665 = song11867.getAlbum();
        album5665.getUUID();
        java.util.UUID uuid157253 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID uuid157254 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author11828 = new fifthelement.theelement.objects.Author(uuid157254, "");
        album5649 = new fifthelement.theelement.objects.Album(uuid157253, "A Head Full of Dreams", author11828, list287, 10);
        album5649.getAuthor();
        fifthelement.theelement.objects.Author author11857 = album5649.getAuthor();
        author11857.getUUID();
        java.util.UUID uuid157256 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author11822 = new fifthelement.theelement.objects.Author(uuid157256, "Coldplay", 10);
        album5649.setAuthor(author11822);
        song11867.setAlbum(album5649);
        songlistservice474.sortSongs(arraylist6172);
        arraylist6172.size();
        arraylist6172.size();
        arraylist6172.size();
        arraylist6172.size();
        fifthelement.theelement.objects.Song song11873 = (fifthelement.theelement.objects.Song) arraylist6172.get(0);
        fifthelement.theelement.objects.Author author11858 = song11873.getAuthor();
        author11858.getName();
        song11873.getName();
        fifthelement.theelement.objects.Song song11874 = (fifthelement.theelement.objects.Song) arraylist6172.get(1);
        song11874.getAuthor();
        song11874.getName();
        fifthelement.theelement.objects.Song song11875 = (fifthelement.theelement.objects.Song) arraylist6172.get(2);
        song11875.getAuthor();
        song11875.getName();
        fifthelement.theelement.objects.Song song11876 = (fifthelement.theelement.objects.Song) arraylist6172.get(3);
        fifthelement.theelement.objects.Author author11861 = song11876.getAuthor();
        author11861.getName();
        song11876.getName();
        arraylist6172.size();
        arraylist6172.size();
        fifthelement.theelement.objects.Song song11877 = (fifthelement.theelement.objects.Song) arraylist6172.get(0);
        fifthelement.theelement.objects.Author author11862 = song11877.getAuthor();
        author11862.getName();
        song11877.getName();
        fifthelement.theelement.objects.Song song11878 = (fifthelement.theelement.objects.Song) arraylist6172.get(1);
        song11878.getAuthor();
        song11878.getName();
        fifthelement.theelement.objects.Song song11879 = (fifthelement.theelement.objects.Song) arraylist6172.get(2);
        song11879.getAuthor();
        song11879.getName();
        fifthelement.theelement.objects.Song song11880 = (fifthelement.theelement.objects.Song) arraylist6172.get(3);
        fifthelement.theelement.objects.Author author11865 = song11880.getAuthor();
        author11865.getName();
        song11880.getName();
        arraylist6172.size();
        fifthelement.theelement.objects.Song song11881 = (fifthelement.theelement.objects.Song) arraylist6172.get(0);
        fifthelement.theelement.objects.Author author11866 = song11881.getAuthor();
        author11866.getName();
        song11881.getName();
        fifthelement.theelement.objects.Song song11882 = (fifthelement.theelement.objects.Song) arraylist6172.get(1);
        song11882.getAuthor();
        song11882.getName();
        fifthelement.theelement.objects.Song song11883 = (fifthelement.theelement.objects.Song) arraylist6172.get(2);
        song11883.getAuthor();
        song11883.getName();
        fifthelement.theelement.objects.Song song11884 = (fifthelement.theelement.objects.Song) arraylist6172.get(3);
        fifthelement.theelement.objects.Author author11869 = song11884.getAuthor();
        author11869.getName();
        song11884.getName();
        arraylist6172.size();
        arraylist6172.size();
        fifthelement.theelement.objects.Song song11885 = (fifthelement.theelement.objects.Song) arraylist6172.get(0);
        fifthelement.theelement.objects.Author author11870 = song11885.getAuthor();
        author11870.getName();
        song11885.getName();
        fifthelement.theelement.objects.Song song11886 = (fifthelement.theelement.objects.Song) arraylist6172.get(1);
        song11886.getAuthor();
        song11886.getName();
        fifthelement.theelement.objects.Song song11887 = (fifthelement.theelement.objects.Song) arraylist6172.get(2);
        song11887.getAuthor();
        song11887.getName();
        fifthelement.theelement.objects.Song song11888 = (fifthelement.theelement.objects.Song) arraylist6172.get(3);
        fifthelement.theelement.objects.Author author11873 = song11888.getAuthor();
        author11873.getName();
        song11888.getName();
        arraylist6172.size();
        arraylist6172.size();
        fifthelement.theelement.objects.Song song11889 = (fifthelement.theelement.objects.Song) arraylist6172.get(0);
        fifthelement.theelement.objects.Author author11874 = song11889.getAuthor();
        author11874.getName();
        song11889.getName();
        fifthelement.theelement.objects.Song song11890 = (fifthelement.theelement.objects.Song) arraylist6172.get(1);
        song11890.getAuthor();
        song11890.getName();
        fifthelement.theelement.objects.Song song11891 = (fifthelement.theelement.objects.Song) arraylist6172.get(2);
        song11891.getAuthor();
        song11891.getName();
        fifthelement.theelement.objects.Song song11892 = (fifthelement.theelement.objects.Song) arraylist6172.get(3);
        fifthelement.theelement.objects.Author author11877 = song11892.getAuthor();
        author11877.getName();
        song11892.getName();
        arraylist6172.size();
        arraylist6172.get(0);
        arraylist6172.get(1);
        arraylist6172.get(2);
        arraylist6172.get(3);
        arraylist6172.size();
        arraylist6172.size();
        fifthelement.theelement.objects.Song song11897 = (fifthelement.theelement.objects.Song) arraylist6172.get(0);
        fifthelement.theelement.objects.Author author11878 = song11897.getAuthor();
        author11878.getName();
        song11897.getName();
        fifthelement.theelement.objects.Song song11898 = (fifthelement.theelement.objects.Song) arraylist6172.get(1);
        song11898.getAuthor();
        song11898.getName();
        fifthelement.theelement.objects.Song song11899 = (fifthelement.theelement.objects.Song) arraylist6172.get(2);
        song11899.getAuthor();
        song11899.getName();
        fifthelement.theelement.objects.Song song11900 = (fifthelement.theelement.objects.Song) arraylist6172.get(3);
        fifthelement.theelement.objects.Author author11881 = song11900.getAuthor();
        author11881.getName();
        song11900.getName();
        arraylist6172.size();
        arraylist6172.size();
        fifthelement.theelement.objects.Song song11901 = (fifthelement.theelement.objects.Song) arraylist6172.get(0);
        fifthelement.theelement.objects.Author author11882 = song11901.getAuthor();
        author11882.getName();
        song11901.getName();
        fifthelement.theelement.objects.Song song11902 = (fifthelement.theelement.objects.Song) arraylist6172.get(1);
        song11902.getAuthor();
        song11902.getName();
        fifthelement.theelement.objects.Song song11903 = (fifthelement.theelement.objects.Song) arraylist6172.get(2);
        fifthelement.theelement.objects.Author author11884 = song11903.getAuthor();
        song11903.getName();
        fifthelement.theelement.objects.Song song11904 = (fifthelement.theelement.objects.Song) arraylist6172.get(3);
        fifthelement.theelement.objects.Author author11885 = song11904.getAuthor();
        author11885.getName();
        song11904.getName();
        arraylist6172.size();
        arraylist6172.get(0);
        arraylist6172.get(1);
        arraylist6172.get(2);
        arraylist6172.get(3);
        arraylist6172.get(0);
        arraylist6172.size();
        arraylist6172.size();
        arraylist6172.get(0);
        arraylist6171 = new java.util.ArrayList();
        java.util.UUID uuid157257 = java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID uuid157258 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author11829 = new fifthelement.theelement.objects.Author(uuid157258, "");
        song11864 = new fifthelement.theelement.objects.Song(uuid157257, "This Is America", "android.resource://fifthelement.theelement/raw/childish_gambino_this_is_america", author11829, album5663, "Hiphop", 3, 1.5);
        arraylist6171.add(song11864);
        java.util.UUID uuid157259 = java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        song11860 = new fifthelement.theelement.objects.Song(uuid157259, "Hall of Fame", "android.resource://fifthelement.theelement/raw/hall_of_fame", author11884, album5663, "", 3, 4.5);
        arraylist6171.add(song11860);
        java.util.UUID uuid157260 = java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        song11858 = new fifthelement.theelement.objects.Song(uuid157260, "Classical Music", "android.resource://fifthelement.theelement/raw/classical_music", author11884, album5663, "Classical", 4, 2.0);
        arraylist6171.add(song11858);
        java.util.UUID uuid157261 = java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid157262 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author11841 = new fifthelement.theelement.objects.Author(uuid157262, "");
        java.util.UUID uuid157263 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        album5653 = new fifthelement.theelement.objects.Album(uuid157263, "");
        song11871 = new fifthelement.theelement.objects.Song(uuid157261, "Adventure of a Lifetime", "android.resource://fifthelement.theelement/raw/coldplay_adventure_of_a_lifetime", author11841, album5653, "Pop", 10, 3.5);
        arraylist6171.add(song11871);
        arraylist6171.iterator();
        song11864.getAuthor();
        fifthelement.theelement.objects.Author author11887 = song11864.getAuthor();
        author11887.getUUID();
        java.util.UUID uuid157265 = java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        author11835 = new fifthelement.theelement.objects.Author(uuid157265, "Childish Gambino", 3);
        song11864.setAuthor(author11835);
        song11864.getAlbum();
        song11860.getAuthor();
        song11860.getAlbum();
        song11858.getAuthor();
        song11858.getAlbum();
        song11871.getAuthor();
        fifthelement.theelement.objects.Author author11891 = song11871.getAuthor();
        author11891.getUUID();
        java.util.UUID uuid157267 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author11839 = new fifthelement.theelement.objects.Author(uuid157267, "Coldplay", 10);
        song11871.setAuthor(author11839);
        song11871.getAlbum();
        fifthelement.theelement.objects.Album album5670 = song11871.getAlbum();
        album5670.getUUID();
        java.util.UUID uuid157269 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID uuid157270 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author11831 = new fifthelement.theelement.objects.Author(uuid157270, "");
        album5655 = new fifthelement.theelement.objects.Album(uuid157269, "A Head Full of Dreams", author11831, list287, 10);
        album5655.getAuthor();
        fifthelement.theelement.objects.Author author11893 = album5655.getAuthor();
        author11893.getUUID();
        java.util.UUID uuid157272 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author11833 = new fifthelement.theelement.objects.Author(uuid157272, "Coldplay", 10);
        album5655.setAuthor(author11833);
        song11871.setAlbum(album5655);
        songlistservice474.sortSongs(arraylist6171);
        songlistservice474.setCurrentSongsList(arraylist6171);
        fifthelement.theelement.objects.Song song11911 = songlistservice474.getSongAtIndex(0);
        song11911.getPath();
        songlistservice474.setShuffleEnabled(false);
        java.util.UUID uuid157273 = song11911.getUUID();
        uuid157273.toString();
        song11911.getName();
        song11911.getPath();
        song11911.getPath();
        song11911.getPath();
        song11911.getPath();
        song11911.getName();
        song11911.getName();
        song11911.getPath();
        song11911.getPath();
        song11911.getPath();
        song11911.getPath();
        song11911.getAuthor();
        fifthelement.theelement.objects.Author author11895 = song11911.getAuthor();
        author11895.getName();
        fifthelement.theelement.objects.Author author11896 = song11911.getAuthor();
        author11896.getName();
        song11911.getAlbum();
        fifthelement.theelement.objects.Album album5672 = song11911.getAlbum();
        album5672.getName();
        songlistservice474.getCurrentSongPlayingIndex();
        fifthelement.theelement.objects.Song song11912 = songlistservice474.getSongAtIndex(0);
        song11912.getUUID();
        fifthelement.theelement.objects.Song song11913 = songlistservice474.skipToNextSong();
        song11913.getPath();
        song11913.getName();
        song11913.getName();
        song11913.getPath();
        song11913.getPath();
        song11913.getPath();
        song11913.getPath();
        song11913.getAuthor();
        song11913.getAlbum();
        java.util.UUID uuid157275 = song11913.getUUID();
        uuid157275.toString();
        song11913.getName();
        song11913.getName();
        song11913.getName();
        song11913.getPath();
        song11913.getPath();
        song11913.getPath();
        song11913.getPath();
        song11913.getAuthor();
        song11913.getAlbum();
        songlistservice474.getCurrentSongPlayingIndex();
        songlistservice474.getSongAtIndex(1);
        songlistservice474.skipToNextSong();
    }
}

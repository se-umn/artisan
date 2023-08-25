package fifthelement.theelement.objects;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

public class Test9473 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.PlayMusicTests#shuffleSongTest/Trace-1650046488177.txt
     * Method invocation under test: <fifthelement.theelement.objects.Song: java.lang.String getPath()>_1422_2840
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_objects_Song_getPath_010() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        fifthelement.theelement.objects.Song song3768 = null;
        java.util.List list121 = null;
        fifthelement.theelement.objects.Author author4274 = null;
        fifthelement.theelement.objects.Album album2069 = null;
        fifthelement.theelement.objects.Author author4275 = null;
        fifthelement.theelement.objects.Album album2070 = null;
        fifthelement.theelement.objects.Author author4276 = null;
        fifthelement.theelement.objects.Author author4277 = null;
        java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        java.util.UUID uuid33088 = java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid33089 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author4275 = new fifthelement.theelement.objects.Author(uuid33089, "");
        java.util.UUID uuid33090 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        album2069 = new fifthelement.theelement.objects.Album(uuid33090, "");
        song3768 = new fifthelement.theelement.objects.Song(uuid33088, "Adventure of a Lifetime", "android.resource://fifthelement.theelement/raw/coldplay_adventure_of_a_lifetime", author4275, album2069, "Pop", 10, 3.5);
        java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        song3768.getAuthor();
        fifthelement.theelement.objects.Author author4279 = song3768.getAuthor();
        author4279.getUUID();
        java.util.UUID uuid33093 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author4276 = new fifthelement.theelement.objects.Author(uuid33093, "Coldplay", 10);
        song3768.setAuthor(author4276);
        song3768.getAlbum();
        fifthelement.theelement.objects.Album album2072 = song3768.getAlbum();
        album2072.getUUID();
        java.util.UUID uuid33095 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID uuid33096 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author4274 = new fifthelement.theelement.objects.Author(uuid33096, "");
        album2070 = new fifthelement.theelement.objects.Album(uuid33095, "A Head Full of Dreams", author4274, list121, 10);
        album2070.getAuthor();
        fifthelement.theelement.objects.Author author4281 = album2070.getAuthor();
        author4281.getUUID();
        java.util.UUID uuid33098 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author4277 = new fifthelement.theelement.objects.Author(uuid33098, "Coldplay", 10);
        album2070.setAuthor(author4277);
        song3768.setAlbum(album2070);
        song3768.getPath();
        java.util.UUID uuid33099 = song3768.getUUID();
        uuid33099.toString();
        song3768.getName();
        song3768.getPath();
        song3768.getPath();
        song3768.getPath();
        song3768.getPath();
        song3768.getName();
        song3768.getName();
        song3768.getPath();
        song3768.getPath();
        song3768.getPath();
        song3768.getPath();
    }
}

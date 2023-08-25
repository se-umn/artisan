package fifthelement.theelement.objects;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

public class Test9475 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.PlayMusicTests#shuffleSongTest/Trace-1650046488177.txt
     * Method invocation under test: <fifthelement.theelement.objects.Song: fifthelement.theelement.objects.Author getAuthor()>_1434_2865
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_objects_Song_getAuthor_058() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        fifthelement.theelement.objects.Song song3770 = null;
        java.util.List list123 = null;
        fifthelement.theelement.objects.Author author4291 = null;
        fifthelement.theelement.objects.Album album2077 = null;
        fifthelement.theelement.objects.Author author4292 = null;
        fifthelement.theelement.objects.Album album2078 = null;
        fifthelement.theelement.objects.Author author4293 = null;
        fifthelement.theelement.objects.Author author4294 = null;
        java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        java.util.UUID uuid33144 = java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid33145 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author4292 = new fifthelement.theelement.objects.Author(uuid33145, "");
        java.util.UUID uuid33146 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        album2077 = new fifthelement.theelement.objects.Album(uuid33146, "");
        song3770 = new fifthelement.theelement.objects.Song(uuid33144, "Adventure of a Lifetime", "android.resource://fifthelement.theelement/raw/coldplay_adventure_of_a_lifetime", author4292, album2077, "Pop", 10, 3.5);
        java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        song3770.getAuthor();
        fifthelement.theelement.objects.Author author4296 = song3770.getAuthor();
        author4296.getUUID();
        java.util.UUID uuid33149 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author4293 = new fifthelement.theelement.objects.Author(uuid33149, "Coldplay", 10);
        song3770.setAuthor(author4293);
        song3770.getAlbum();
        fifthelement.theelement.objects.Album album2080 = song3770.getAlbum();
        album2080.getUUID();
        java.util.UUID uuid33151 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID uuid33152 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author4291 = new fifthelement.theelement.objects.Author(uuid33152, "");
        album2078 = new fifthelement.theelement.objects.Album(uuid33151, "A Head Full of Dreams", author4291, list123, 10);
        album2078.getAuthor();
        fifthelement.theelement.objects.Author author4298 = album2078.getAuthor();
        author4298.getUUID();
        java.util.UUID uuid33154 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author4294 = new fifthelement.theelement.objects.Author(uuid33154, "Coldplay", 10);
        album2078.setAuthor(author4294);
        song3770.setAlbum(album2078);
        song3770.getPath();
        java.util.UUID uuid33155 = song3770.getUUID();
        uuid33155.toString();
        song3770.getName();
        song3770.getPath();
        song3770.getPath();
        song3770.getPath();
        song3770.getPath();
        song3770.getName();
        song3770.getName();
        song3770.getPath();
        song3770.getPath();
        song3770.getPath();
        song3770.getPath();
        song3770.getAuthor();
        song3770.getAuthor();
    }
}

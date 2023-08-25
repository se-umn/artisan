package fifthelement.theelement.objects;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

public class Test9470 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.PlayMusicTests#shuffleSongTest/Trace-1650046488177.txt
     * Method invocation under test: <fifthelement.theelement.objects.Song: java.lang.String getPath()>_1408_2812
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_objects_Song_getPath_007() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        fifthelement.theelement.objects.Song song3765 = null;
        java.util.List list118 = null;
        fifthelement.theelement.objects.Author author4250 = null;
        fifthelement.theelement.objects.Album album2057 = null;
        fifthelement.theelement.objects.Author author4251 = null;
        fifthelement.theelement.objects.Album album2058 = null;
        fifthelement.theelement.objects.Author author4252 = null;
        fifthelement.theelement.objects.Author author4253 = null;
        java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        java.util.UUID uuid33004 = java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid33005 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author4251 = new fifthelement.theelement.objects.Author(uuid33005, "");
        java.util.UUID uuid33006 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        album2057 = new fifthelement.theelement.objects.Album(uuid33006, "");
        song3765 = new fifthelement.theelement.objects.Song(uuid33004, "Adventure of a Lifetime", "android.resource://fifthelement.theelement/raw/coldplay_adventure_of_a_lifetime", author4251, album2057, "Pop", 10, 3.5);
        java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        song3765.getAuthor();
        fifthelement.theelement.objects.Author author4255 = song3765.getAuthor();
        author4255.getUUID();
        java.util.UUID uuid33009 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author4252 = new fifthelement.theelement.objects.Author(uuid33009, "Coldplay", 10);
        song3765.setAuthor(author4252);
        song3765.getAlbum();
        fifthelement.theelement.objects.Album album2060 = song3765.getAlbum();
        album2060.getUUID();
        java.util.UUID uuid33011 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID uuid33012 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author4250 = new fifthelement.theelement.objects.Author(uuid33012, "");
        album2058 = new fifthelement.theelement.objects.Album(uuid33011, "A Head Full of Dreams", author4250, list118, 10);
        album2058.getAuthor();
        fifthelement.theelement.objects.Author author4257 = album2058.getAuthor();
        author4257.getUUID();
        java.util.UUID uuid33014 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author4253 = new fifthelement.theelement.objects.Author(uuid33014, "Coldplay", 10);
        album2058.setAuthor(author4253);
        song3765.setAlbum(album2058);
        song3765.getPath();
        java.util.UUID uuid33015 = song3765.getUUID();
        uuid33015.toString();
        song3765.getName();
        song3765.getPath();
        song3765.getPath();
        song3765.getPath();
        song3765.getPath();
        song3765.getName();
        song3765.getName();
        song3765.getPath();
    }
}

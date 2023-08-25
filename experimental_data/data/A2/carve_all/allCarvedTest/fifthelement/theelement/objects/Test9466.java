package fifthelement.theelement.objects;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

public class Test9466 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.PlayMusicTests#shuffleSongTest/Trace-1650046488177.txt
     * Method invocation under test: <fifthelement.theelement.objects.Song: java.lang.String getPath()>_1351_2699
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_objects_Song_getPath_005() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        fifthelement.theelement.objects.Song song3761 = null;
        java.util.List list114 = null;
        fifthelement.theelement.objects.Author author4218 = null;
        fifthelement.theelement.objects.Album album2041 = null;
        fifthelement.theelement.objects.Author author4219 = null;
        fifthelement.theelement.objects.Album album2042 = null;
        fifthelement.theelement.objects.Author author4220 = null;
        fifthelement.theelement.objects.Author author4221 = null;
        java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        java.util.UUID uuid32892 = java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid32893 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author4219 = new fifthelement.theelement.objects.Author(uuid32893, "");
        java.util.UUID uuid32894 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        album2041 = new fifthelement.theelement.objects.Album(uuid32894, "");
        song3761 = new fifthelement.theelement.objects.Song(uuid32892, "Adventure of a Lifetime", "android.resource://fifthelement.theelement/raw/coldplay_adventure_of_a_lifetime", author4219, album2041, "Pop", 10, 3.5);
        java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        song3761.getAuthor();
        fifthelement.theelement.objects.Author author4223 = song3761.getAuthor();
        author4223.getUUID();
        java.util.UUID uuid32897 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author4220 = new fifthelement.theelement.objects.Author(uuid32897, "Coldplay", 10);
        song3761.setAuthor(author4220);
        song3761.getAlbum();
        fifthelement.theelement.objects.Album album2044 = song3761.getAlbum();
        album2044.getUUID();
        java.util.UUID uuid32899 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID uuid32900 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author4218 = new fifthelement.theelement.objects.Author(uuid32900, "");
        album2042 = new fifthelement.theelement.objects.Album(uuid32899, "A Head Full of Dreams", author4218, list114, 10);
        album2042.getAuthor();
        fifthelement.theelement.objects.Author author4225 = album2042.getAuthor();
        author4225.getUUID();
        java.util.UUID uuid32902 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author4221 = new fifthelement.theelement.objects.Author(uuid32902, "Coldplay", 10);
        album2042.setAuthor(author4221);
        song3761.setAlbum(album2042);
        song3761.getPath();
        java.util.UUID uuid32903 = song3761.getUUID();
        uuid32903.toString();
        song3761.getName();
        song3761.getPath();
        song3761.getPath();
        song3761.getPath();
    }
}

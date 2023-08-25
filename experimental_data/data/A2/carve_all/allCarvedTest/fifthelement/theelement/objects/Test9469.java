package fifthelement.theelement.objects;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

public class Test9469 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.PlayMusicTests#shuffleSongTest/Trace-1650046488177.txt
     * Method invocation under test: <fifthelement.theelement.objects.Song: java.lang.String getName()>_1403_2803
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_objects_Song_getName_030() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        fifthelement.theelement.objects.Song song3764 = null;
        java.util.List list117 = null;
        fifthelement.theelement.objects.Author author4242 = null;
        fifthelement.theelement.objects.Album album2053 = null;
        fifthelement.theelement.objects.Author author4243 = null;
        fifthelement.theelement.objects.Album album2054 = null;
        fifthelement.theelement.objects.Author author4244 = null;
        fifthelement.theelement.objects.Author author4245 = null;
        java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        java.util.UUID uuid32976 = java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid32977 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author4243 = new fifthelement.theelement.objects.Author(uuid32977, "");
        java.util.UUID uuid32978 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        album2053 = new fifthelement.theelement.objects.Album(uuid32978, "");
        song3764 = new fifthelement.theelement.objects.Song(uuid32976, "Adventure of a Lifetime", "android.resource://fifthelement.theelement/raw/coldplay_adventure_of_a_lifetime", author4243, album2053, "Pop", 10, 3.5);
        java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        song3764.getAuthor();
        fifthelement.theelement.objects.Author author4247 = song3764.getAuthor();
        author4247.getUUID();
        java.util.UUID uuid32981 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author4244 = new fifthelement.theelement.objects.Author(uuid32981, "Coldplay", 10);
        song3764.setAuthor(author4244);
        song3764.getAlbum();
        fifthelement.theelement.objects.Album album2056 = song3764.getAlbum();
        album2056.getUUID();
        java.util.UUID uuid32983 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID uuid32984 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author4242 = new fifthelement.theelement.objects.Author(uuid32984, "");
        album2054 = new fifthelement.theelement.objects.Album(uuid32983, "A Head Full of Dreams", author4242, list117, 10);
        album2054.getAuthor();
        fifthelement.theelement.objects.Author author4249 = album2054.getAuthor();
        author4249.getUUID();
        java.util.UUID uuid32986 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author4245 = new fifthelement.theelement.objects.Author(uuid32986, "Coldplay", 10);
        album2054.setAuthor(author4245);
        song3764.setAlbum(album2054);
        song3764.getPath();
        java.util.UUID uuid32987 = song3764.getUUID();
        uuid32987.toString();
        song3764.getName();
        song3764.getPath();
        song3764.getPath();
        song3764.getPath();
        song3764.getPath();
        song3764.getName();
        song3764.getName();
    }
}

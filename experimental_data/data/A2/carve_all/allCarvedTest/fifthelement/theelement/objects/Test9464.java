package fifthelement.theelement.objects;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

public class Test9464 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.PlayMusicTests#shuffleSongTest/Trace-1650046488177.txt
     * Method invocation under test: <fifthelement.theelement.objects.Song: java.lang.String getPath()>_1338_2673
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_objects_Song_getPath_003() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        fifthelement.theelement.objects.Song song3759 = null;
        java.util.List list112 = null;
        fifthelement.theelement.objects.Author author4202 = null;
        fifthelement.theelement.objects.Album album2033 = null;
        fifthelement.theelement.objects.Author author4203 = null;
        fifthelement.theelement.objects.Album album2034 = null;
        fifthelement.theelement.objects.Author author4204 = null;
        fifthelement.theelement.objects.Author author4205 = null;
        java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        java.util.UUID uuid32836 = java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid32837 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author4203 = new fifthelement.theelement.objects.Author(uuid32837, "");
        java.util.UUID uuid32838 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        album2033 = new fifthelement.theelement.objects.Album(uuid32838, "");
        song3759 = new fifthelement.theelement.objects.Song(uuid32836, "Adventure of a Lifetime", "android.resource://fifthelement.theelement/raw/coldplay_adventure_of_a_lifetime", author4203, album2033, "Pop", 10, 3.5);
        java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        song3759.getAuthor();
        fifthelement.theelement.objects.Author author4207 = song3759.getAuthor();
        author4207.getUUID();
        java.util.UUID uuid32841 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author4204 = new fifthelement.theelement.objects.Author(uuid32841, "Coldplay", 10);
        song3759.setAuthor(author4204);
        song3759.getAlbum();
        fifthelement.theelement.objects.Album album2036 = song3759.getAlbum();
        album2036.getUUID();
        java.util.UUID uuid32843 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID uuid32844 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author4202 = new fifthelement.theelement.objects.Author(uuid32844, "");
        album2034 = new fifthelement.theelement.objects.Album(uuid32843, "A Head Full of Dreams", author4202, list112, 10);
        album2034.getAuthor();
        fifthelement.theelement.objects.Author author4209 = album2034.getAuthor();
        author4209.getUUID();
        java.util.UUID uuid32846 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author4205 = new fifthelement.theelement.objects.Author(uuid32846, "Coldplay", 10);
        album2034.setAuthor(author4205);
        song3759.setAlbum(album2034);
        song3759.getPath();
        java.util.UUID uuid32847 = song3759.getUUID();
        uuid32847.toString();
        song3759.getName();
        song3759.getPath();
    }
}

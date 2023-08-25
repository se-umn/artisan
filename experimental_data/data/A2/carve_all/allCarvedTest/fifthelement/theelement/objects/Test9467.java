package fifthelement.theelement.objects;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

public class Test9467 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.PlayMusicTests#shuffleSongTest/Trace-1650046488177.txt
     * Method invocation under test: <fifthelement.theelement.objects.Song: java.lang.String getPath()>_1352_2701
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_objects_Song_getPath_006() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        fifthelement.theelement.objects.Song song3762 = null;
        java.util.List list115 = null;
        fifthelement.theelement.objects.Author author4226 = null;
        fifthelement.theelement.objects.Album album2045 = null;
        fifthelement.theelement.objects.Author author4227 = null;
        fifthelement.theelement.objects.Album album2046 = null;
        fifthelement.theelement.objects.Author author4228 = null;
        fifthelement.theelement.objects.Author author4229 = null;
        java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        java.util.UUID uuid32920 = java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid32921 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author4227 = new fifthelement.theelement.objects.Author(uuid32921, "");
        java.util.UUID uuid32922 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        album2045 = new fifthelement.theelement.objects.Album(uuid32922, "");
        song3762 = new fifthelement.theelement.objects.Song(uuid32920, "Adventure of a Lifetime", "android.resource://fifthelement.theelement/raw/coldplay_adventure_of_a_lifetime", author4227, album2045, "Pop", 10, 3.5);
        java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        song3762.getAuthor();
        fifthelement.theelement.objects.Author author4231 = song3762.getAuthor();
        author4231.getUUID();
        java.util.UUID uuid32925 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author4228 = new fifthelement.theelement.objects.Author(uuid32925, "Coldplay", 10);
        song3762.setAuthor(author4228);
        song3762.getAlbum();
        fifthelement.theelement.objects.Album album2048 = song3762.getAlbum();
        album2048.getUUID();
        java.util.UUID uuid32927 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID uuid32928 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author4226 = new fifthelement.theelement.objects.Author(uuid32928, "");
        album2046 = new fifthelement.theelement.objects.Album(uuid32927, "A Head Full of Dreams", author4226, list115, 10);
        album2046.getAuthor();
        fifthelement.theelement.objects.Author author4233 = album2046.getAuthor();
        author4233.getUUID();
        java.util.UUID uuid32930 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author4229 = new fifthelement.theelement.objects.Author(uuid32930, "Coldplay", 10);
        album2046.setAuthor(author4229);
        song3762.setAlbum(album2046);
        song3762.getPath();
        java.util.UUID uuid32931 = song3762.getUUID();
        uuid32931.toString();
        song3762.getName();
        song3762.getPath();
        song3762.getPath();
        song3762.getPath();
        song3762.getPath();
    }
}

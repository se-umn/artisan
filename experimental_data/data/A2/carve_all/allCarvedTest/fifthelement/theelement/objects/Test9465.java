package fifthelement.theelement.objects;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

public class Test9465 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.PlayMusicTests#shuffleSongTest/Trace-1650046488177.txt
     * Method invocation under test: <fifthelement.theelement.objects.Song: java.lang.String getPath()>_1339_2675
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_objects_Song_getPath_004() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        fifthelement.theelement.objects.Song song3760 = null;
        java.util.List list113 = null;
        fifthelement.theelement.objects.Author author4210 = null;
        fifthelement.theelement.objects.Album album2037 = null;
        fifthelement.theelement.objects.Author author4211 = null;
        fifthelement.theelement.objects.Album album2038 = null;
        fifthelement.theelement.objects.Author author4212 = null;
        fifthelement.theelement.objects.Author author4213 = null;
        java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        java.util.UUID uuid32864 = java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid32865 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author4211 = new fifthelement.theelement.objects.Author(uuid32865, "");
        java.util.UUID uuid32866 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        album2037 = new fifthelement.theelement.objects.Album(uuid32866, "");
        song3760 = new fifthelement.theelement.objects.Song(uuid32864, "Adventure of a Lifetime", "android.resource://fifthelement.theelement/raw/coldplay_adventure_of_a_lifetime", author4211, album2037, "Pop", 10, 3.5);
        java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        song3760.getAuthor();
        fifthelement.theelement.objects.Author author4215 = song3760.getAuthor();
        author4215.getUUID();
        java.util.UUID uuid32869 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author4212 = new fifthelement.theelement.objects.Author(uuid32869, "Coldplay", 10);
        song3760.setAuthor(author4212);
        song3760.getAlbum();
        fifthelement.theelement.objects.Album album2040 = song3760.getAlbum();
        album2040.getUUID();
        java.util.UUID uuid32871 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID uuid32872 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author4210 = new fifthelement.theelement.objects.Author(uuid32872, "");
        album2038 = new fifthelement.theelement.objects.Album(uuid32871, "A Head Full of Dreams", author4210, list113, 10);
        album2038.getAuthor();
        fifthelement.theelement.objects.Author author4217 = album2038.getAuthor();
        author4217.getUUID();
        java.util.UUID uuid32874 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author4213 = new fifthelement.theelement.objects.Author(uuid32874, "Coldplay", 10);
        album2038.setAuthor(author4213);
        song3760.setAlbum(album2038);
        song3760.getPath();
        java.util.UUID uuid32875 = song3760.getUUID();
        uuid32875.toString();
        song3760.getName();
        song3760.getPath();
        song3760.getPath();
    }
}

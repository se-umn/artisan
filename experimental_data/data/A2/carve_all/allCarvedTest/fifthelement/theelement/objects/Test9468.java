package fifthelement.theelement.objects;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

public class Test9468 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.PlayMusicTests#shuffleSongTest/Trace-1650046488177.txt
     * Method invocation under test: <fifthelement.theelement.objects.Song: java.lang.String getName()>_1400_2797
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_objects_Song_getName_029() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        fifthelement.theelement.objects.Song song3763 = null;
        java.util.List list116 = null;
        fifthelement.theelement.objects.Author author4234 = null;
        fifthelement.theelement.objects.Album album2049 = null;
        fifthelement.theelement.objects.Author author4235 = null;
        fifthelement.theelement.objects.Album album2050 = null;
        fifthelement.theelement.objects.Author author4236 = null;
        fifthelement.theelement.objects.Author author4237 = null;
        java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        java.util.UUID uuid32948 = java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid32949 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author4235 = new fifthelement.theelement.objects.Author(uuid32949, "");
        java.util.UUID uuid32950 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        album2049 = new fifthelement.theelement.objects.Album(uuid32950, "");
        song3763 = new fifthelement.theelement.objects.Song(uuid32948, "Adventure of a Lifetime", "android.resource://fifthelement.theelement/raw/coldplay_adventure_of_a_lifetime", author4235, album2049, "Pop", 10, 3.5);
        java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        song3763.getAuthor();
        fifthelement.theelement.objects.Author author4239 = song3763.getAuthor();
        author4239.getUUID();
        java.util.UUID uuid32953 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author4236 = new fifthelement.theelement.objects.Author(uuid32953, "Coldplay", 10);
        song3763.setAuthor(author4236);
        song3763.getAlbum();
        fifthelement.theelement.objects.Album album2052 = song3763.getAlbum();
        album2052.getUUID();
        java.util.UUID uuid32955 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID uuid32956 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author4234 = new fifthelement.theelement.objects.Author(uuid32956, "");
        album2050 = new fifthelement.theelement.objects.Album(uuid32955, "A Head Full of Dreams", author4234, list116, 10);
        album2050.getAuthor();
        fifthelement.theelement.objects.Author author4241 = album2050.getAuthor();
        author4241.getUUID();
        java.util.UUID uuid32958 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author4237 = new fifthelement.theelement.objects.Author(uuid32958, "Coldplay", 10);
        album2050.setAuthor(author4237);
        song3763.setAlbum(album2050);
        song3763.getPath();
        java.util.UUID uuid32959 = song3763.getUUID();
        uuid32959.toString();
        song3763.getName();
        song3763.getPath();
        song3763.getPath();
        song3763.getPath();
        song3763.getPath();
        song3763.getName();
    }
}

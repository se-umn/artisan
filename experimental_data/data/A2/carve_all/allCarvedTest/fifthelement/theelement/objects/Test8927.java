package fifthelement.theelement.objects;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

public class Test8927 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.CustomizeTests#deleteSong/Trace-1650046523752.txt
     * Method invocation under test: <fifthelement.theelement.objects.Song: java.util.UUID getUUID()>_1904_3805
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_objects_Song_getUUID_010() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        fifthelement.theelement.objects.Album album2169 = null;
        fifthelement.theelement.objects.Album album2170 = null;
        fifthelement.theelement.objects.Author author5083 = null;
        fifthelement.theelement.objects.Author author5084 = null;
        java.util.List list131 = null;
        fifthelement.theelement.objects.Author author5085 = null;
        fifthelement.theelement.objects.Author author5086 = null;
        fifthelement.theelement.objects.Song song3896 = null;
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
        java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        java.util.UUID uuid75199 = java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid75200 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author5085 = new fifthelement.theelement.objects.Author(uuid75200, "");
        java.util.UUID uuid75201 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        album2170 = new fifthelement.theelement.objects.Album(uuid75201, "");
        song3896 = new fifthelement.theelement.objects.Song(uuid75199, "Adventure of a Lifetime", "android.resource://fifthelement.theelement/raw/coldplay_adventure_of_a_lifetime", author5085, album2170, "Pop", 10, 3.5);
        java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        song3896.getAuthor();
        fifthelement.theelement.objects.Author author5088 = song3896.getAuthor();
        author5088.getUUID();
        java.util.UUID uuid75204 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author5084 = new fifthelement.theelement.objects.Author(uuid75204, "Coldplay", 10);
        song3896.setAuthor(author5084);
        song3896.getAlbum();
        fifthelement.theelement.objects.Album album2172 = song3896.getAlbum();
        album2172.getUUID();
        java.util.UUID uuid75206 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID uuid75207 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author5083 = new fifthelement.theelement.objects.Author(uuid75207, "");
        album2169 = new fifthelement.theelement.objects.Album(uuid75206, "A Head Full of Dreams", author5083, list131, 10);
        album2169.getAuthor();
        fifthelement.theelement.objects.Author author5090 = album2169.getAuthor();
        author5090.getUUID();
        java.util.UUID uuid75209 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author5086 = new fifthelement.theelement.objects.Author(uuid75209, "Coldplay", 10);
        album2169.setAuthor(author5086);
        song3896.setAlbum(album2169);
        song3896.getUUID();
    }
}

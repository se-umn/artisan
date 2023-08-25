package fifthelement.theelement.objects;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

public class Test9459 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.PlayMusicTests#shuffleSongTest/Trace-1650046488177.txt
     * Method invocation under test: <fifthelement.theelement.objects.Song: java.lang.String getName()>_1305_2609
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_objects_Song_getName_028() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        fifthelement.theelement.objects.Song song3757 = null;
        java.util.List list110 = null;
        fifthelement.theelement.objects.Author author4186 = null;
        fifthelement.theelement.objects.Album album2025 = null;
        fifthelement.theelement.objects.Author author4187 = null;
        fifthelement.theelement.objects.Album album2026 = null;
        fifthelement.theelement.objects.Author author4188 = null;
        fifthelement.theelement.objects.Author author4189 = null;
        java.util.UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        java.util.UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
        java.util.UUID.fromString("54947df8-0e9e-4471-a2f9-9af509fb5889");
        java.util.UUID uuid32781 = java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid32782 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author4187 = new fifthelement.theelement.objects.Author(uuid32782, "");
        java.util.UUID uuid32783 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        album2025 = new fifthelement.theelement.objects.Album(uuid32783, "");
        song3757 = new fifthelement.theelement.objects.Song(uuid32781, "Adventure of a Lifetime", "android.resource://fifthelement.theelement/raw/coldplay_adventure_of_a_lifetime", author4187, album2025, "Pop", 10, 3.5);
        java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        song3757.getAuthor();
        fifthelement.theelement.objects.Author author4191 = song3757.getAuthor();
        author4191.getUUID();
        java.util.UUID uuid32786 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author4188 = new fifthelement.theelement.objects.Author(uuid32786, "Coldplay", 10);
        song3757.setAuthor(author4188);
        song3757.getAlbum();
        fifthelement.theelement.objects.Album album2028 = song3757.getAlbum();
        album2028.getUUID();
        java.util.UUID uuid32788 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID uuid32789 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author4186 = new fifthelement.theelement.objects.Author(uuid32789, "");
        album2026 = new fifthelement.theelement.objects.Album(uuid32788, "A Head Full of Dreams", author4186, list110, 10);
        album2026.getAuthor();
        fifthelement.theelement.objects.Author author4193 = album2026.getAuthor();
        author4193.getUUID();
        java.util.UUID uuid32791 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author4189 = new fifthelement.theelement.objects.Author(uuid32791, "Coldplay", 10);
        album2026.setAuthor(author4189);
        song3757.setAlbum(album2026);
        song3757.getPath();
        java.util.UUID uuid32792 = song3757.getUUID();
        uuid32792.toString();
        song3757.getName();
    }
}

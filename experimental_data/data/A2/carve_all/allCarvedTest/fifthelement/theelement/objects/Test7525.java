package fifthelement.theelement.objects;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

public class Test7525 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.SettingsTest#deleteSongsTest/Trace-1650046437980.txt
     * Method invocation under test: <fifthelement.theelement.objects.Song: void setAlbum(fifthelement.theelement.objects.Album)>_287_572
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_objects_Song_setAlbum_002() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        fifthelement.theelement.objects.Album album69 = null;
        fifthelement.theelement.objects.Author author178 = null;
        fifthelement.theelement.objects.Author author179 = null;
        fifthelement.theelement.objects.Author author180 = null;
        java.util.List list9 = null;
        fifthelement.theelement.objects.Author author181 = null;
        fifthelement.theelement.objects.Song song35 = null;
        fifthelement.theelement.objects.Album album70 = null;
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
        java.util.UUID uuid2020 = java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid2021 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author178 = new fifthelement.theelement.objects.Author(uuid2021, "");
        java.util.UUID uuid2022 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        album69 = new fifthelement.theelement.objects.Album(uuid2022, "");
        song35 = new fifthelement.theelement.objects.Song(uuid2020, "Adventure of a Lifetime", "android.resource://fifthelement.theelement/raw/coldplay_adventure_of_a_lifetime", author178, album69, "Pop", 10, 3.5);
        java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        song35.getAuthor();
        fifthelement.theelement.objects.Author author183 = song35.getAuthor();
        author183.getUUID();
        java.util.UUID uuid2025 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author180 = new fifthelement.theelement.objects.Author(uuid2025, "Coldplay", 10);
        song35.setAuthor(author180);
        song35.getAlbum();
        fifthelement.theelement.objects.Album album72 = song35.getAlbum();
        album72.getUUID();
        java.util.UUID uuid2027 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID uuid2028 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author181 = new fifthelement.theelement.objects.Author(uuid2028, "");
        album70 = new fifthelement.theelement.objects.Album(uuid2027, "A Head Full of Dreams", author181, list9, 10);
        album70.getAuthor();
        fifthelement.theelement.objects.Author author185 = album70.getAuthor();
        author185.getUUID();
        java.util.UUID uuid2030 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author179 = new fifthelement.theelement.objects.Author(uuid2030, "Coldplay", 10);
        album70.setAuthor(author179);
        song35.setAlbum(album70);
    }
}

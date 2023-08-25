package fifthelement.theelement.objects;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

public class Test4091 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.MusicLibraryTests#deleteSongTest/Trace-1650046535485.txt
     * Method invocation under test: <fifthelement.theelement.objects.Song: java.util.UUID getUUID()>_1203_2403
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_objects_Song_getUUID_010() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        fifthelement.theelement.objects.Album album1883 = null;
        fifthelement.theelement.objects.Album album1884 = null;
        fifthelement.theelement.objects.Author author4119 = null;
        fifthelement.theelement.objects.Author author4120 = null;
        java.util.List list82 = null;
        fifthelement.theelement.objects.Author author4121 = null;
        fifthelement.theelement.objects.Author author4122 = null;
        fifthelement.theelement.objects.Song song3810 = null;
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
        java.util.UUID uuid26777 = java.util.UUID.fromString("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid26778 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author4121 = new fifthelement.theelement.objects.Author(uuid26778, "");
        java.util.UUID uuid26779 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        album1884 = new fifthelement.theelement.objects.Album(uuid26779, "");
        song3810 = new fifthelement.theelement.objects.Song(uuid26777, "Adventure of a Lifetime", "android.resource://fifthelement.theelement/raw/coldplay_adventure_of_a_lifetime", author4121, album1884, "Pop", 10, 3.5);
        java.util.UUID.fromString("e9bcb1b6-7b3b-11e8-adc0-fa7ae01bbebc");
        song3810.getAuthor();
        fifthelement.theelement.objects.Author author4124 = song3810.getAuthor();
        author4124.getUUID();
        java.util.UUID uuid26782 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author4120 = new fifthelement.theelement.objects.Author(uuid26782, "Coldplay", 10);
        song3810.setAuthor(author4120);
        song3810.getAlbum();
        fifthelement.theelement.objects.Album album1886 = song3810.getAlbum();
        album1886.getUUID();
        java.util.UUID uuid26784 = java.util.UUID.fromString("c8fe0546-7a88-11e8-adc0-fa7ae01bbebc");
        java.util.UUID uuid26785 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author4119 = new fifthelement.theelement.objects.Author(uuid26785, "");
        album1883 = new fifthelement.theelement.objects.Album(uuid26784, "A Head Full of Dreams", author4119, list82, 10);
        album1883.getAuthor();
        fifthelement.theelement.objects.Author author4126 = album1883.getAuthor();
        author4126.getUUID();
        java.util.UUID uuid26787 = java.util.UUID.fromString("359f9962-7a89-11e8-adc0-fa7ae01bbebc");
        author4122 = new fifthelement.theelement.objects.Author(uuid26787, "Coldplay", 10);
        album1883.setAuthor(author4122);
        song3810.setAlbum(album1883);
        song3810.getUUID();
    }
}

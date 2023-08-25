package fifthelement.theelement.objects;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

public class Test10166 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.MusicLibraryTests#searchSongsTest/Trace-1650046542039.txt
     * Method invocation under test: <fifthelement.theelement.objects.Author: void <init>(java.lang.String)>_562_1119
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_objects_Author_constructor_024() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        fifthelement.theelement.objects.Author author322 = null;
        author322 = new fifthelement.theelement.objects.Author("");
    }
}

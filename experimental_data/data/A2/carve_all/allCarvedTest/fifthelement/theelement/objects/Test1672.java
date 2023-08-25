package fifthelement.theelement.objects;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

public class Test1672 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.SettingsTest#selectTheme3Test/Trace-1650046433616.txt
     * Method invocation under test: <fifthelement.theelement.objects.Author: void <init>(java.lang.String)>_219_433
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_objects_Author_constructor_010() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        fifthelement.theelement.objects.Author author87 = null;
        author87 = new fifthelement.theelement.objects.Author("");
    }
}

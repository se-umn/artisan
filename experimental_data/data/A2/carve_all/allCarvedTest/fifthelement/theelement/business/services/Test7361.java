package fifthelement.theelement.business.services;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

public class Test7361 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.PlayMusicTests#seekTest/Trace-1650046478932.txt
     * Method invocation under test: <fifthelement.theelement.business.services.AuthorService: void <init>()>_1451_2899
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_business_services_AuthorService_constructor_001() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        fifthelement.theelement.business.services.AuthorService authorservice0 = null;
        authorservice0 = new fifthelement.theelement.business.services.AuthorService();
    }
}

package fifthelement.theelement.business.services;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

public class Test1310 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.CustomizeTests#checkStatsPage/Trace-1650046501797.txt
     * Method invocation under test: <fifthelement.theelement.business.services.AuthorService: java.util.List getAuthors()>_616_1230
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_business_services_AuthorService_getAuthors_001() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        fifthelement.theelement.business.services.AuthorService authorservice1 = null;
        authorservice1 = new fifthelement.theelement.business.services.AuthorService();
        authorservice1.getAuthors();
    }
}

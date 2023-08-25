package fifthelement.theelement.business.services;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

public class Test8981 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.CustomizeTests#deleteSong/Trace-1650046523752.txt
     * Method invocation under test: <fifthelement.theelement.business.services.AuthorService: java.util.List getAuthors()>_2181_4360
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_business_services_AuthorService_getAuthors_002() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        fifthelement.theelement.business.services.AuthorService authorservice5 = null;
        authorservice5 = new fifthelement.theelement.business.services.AuthorService();
        authorservice5.getAuthors();
        authorservice5.getMostPlayedAuthor();
        authorservice5.getTotalAuthorPlays();
        authorservice5.getTotalAuthorPlays();
        authorservice5.getAuthors();
    }
}

package fifthelement.theelement.business.services;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

public class Test8657 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.CustomizeTests#deleteSong/Trace-1650046523752.txt
     * Method invocation under test: <fifthelement.theelement.business.services.AuthorService: int getTotalAuthorPlays()>_1065_2128
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_business_services_AuthorService_getTotalAuthorPlays_002() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        fifthelement.theelement.business.services.AuthorService authorservice4 = null;
        authorservice4 = new fifthelement.theelement.business.services.AuthorService();
        authorservice4.getAuthors();
        authorservice4.getMostPlayedAuthor();
        authorservice4.getTotalAuthorPlays();
        authorservice4.getTotalAuthorPlays();
    }
}

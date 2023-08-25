package fifthelement.theelement.business.services;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

public class Test9044 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.CustomizeTests#deleteSong/Trace-1650046523752.txt
     * Method invocation under test: <fifthelement.theelement.business.services.AuthorService: int getTotalAuthorPlays()>_2395_4788
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_business_services_AuthorService_getTotalAuthorPlays_003() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        fifthelement.theelement.business.services.AuthorService authorservice7 = null;
        authorservice7 = new fifthelement.theelement.business.services.AuthorService();
        authorservice7.getAuthors();
        authorservice7.getMostPlayedAuthor();
        authorservice7.getTotalAuthorPlays();
        authorservice7.getTotalAuthorPlays();
        authorservice7.getAuthors();
        authorservice7.getMostPlayedAuthor();
        authorservice7.getTotalAuthorPlays();
    }
}

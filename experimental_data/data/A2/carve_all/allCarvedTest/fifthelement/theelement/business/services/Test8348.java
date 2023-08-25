package fifthelement.theelement.business.services;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

public class Test8348 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.SettingsTest#deleteSongsTest/Trace-1650046437980.txt
     * Method invocation under test: <fifthelement.theelement.business.services.AuthorService: int getTotalAuthorPlays()>_3294_6586
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_business_services_AuthorService_getTotalAuthorPlays_005() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        fifthelement.theelement.business.services.AuthorService authorservice11 = null;
        authorservice11 = new fifthelement.theelement.business.services.AuthorService();
        authorservice11.getAuthors();
        authorservice11.getMostPlayedAuthor();
        authorservice11.getTotalAuthorPlays();
        authorservice11.getTotalAuthorPlays();
        authorservice11.getAuthors();
        authorservice11.getMostPlayedAuthor();
        authorservice11.getTotalAuthorPlays();
        authorservice11.getTotalAuthorPlays();
        authorservice11.getAuthors();
        authorservice11.getMostPlayedAuthor();
        authorservice11.getTotalAuthorPlays();
    }
}

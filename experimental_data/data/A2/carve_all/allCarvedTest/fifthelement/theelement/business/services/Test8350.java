package fifthelement.theelement.business.services;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

public class Test8350 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.SettingsTest#deleteSongsTest/Trace-1650046437980.txt
     * Method invocation under test: <fifthelement.theelement.business.services.AuthorService: int getTotalAuthorPlays()>_3304_6606
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_business_services_AuthorService_getTotalAuthorPlays_006() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        fifthelement.theelement.business.services.AuthorService authorservice12 = null;
        authorservice12 = new fifthelement.theelement.business.services.AuthorService();
        authorservice12.getAuthors();
        authorservice12.getMostPlayedAuthor();
        authorservice12.getTotalAuthorPlays();
        authorservice12.getTotalAuthorPlays();
        authorservice12.getAuthors();
        authorservice12.getMostPlayedAuthor();
        authorservice12.getTotalAuthorPlays();
        authorservice12.getTotalAuthorPlays();
        authorservice12.getAuthors();
        authorservice12.getMostPlayedAuthor();
        authorservice12.getTotalAuthorPlays();
        authorservice12.getTotalAuthorPlays();
    }
}

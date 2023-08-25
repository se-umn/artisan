package fifthelement.theelement.business.services;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

public class Test8131 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.SettingsTest#deleteSongsTest/Trace-1650046437980.txt
     * Method invocation under test: <fifthelement.theelement.business.services.AuthorService: int getTotalAuthorPlays()>_2414_4826
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_business_services_AuthorService_getTotalAuthorPlays_004() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        fifthelement.theelement.business.services.AuthorService authorservice8 = null;
        authorservice8 = new fifthelement.theelement.business.services.AuthorService();
        authorservice8.getAuthors();
        authorservice8.getMostPlayedAuthor();
        authorservice8.getTotalAuthorPlays();
        authorservice8.getTotalAuthorPlays();
        authorservice8.getAuthors();
        authorservice8.getMostPlayedAuthor();
        authorservice8.getTotalAuthorPlays();
        authorservice8.getTotalAuthorPlays();
    }
}

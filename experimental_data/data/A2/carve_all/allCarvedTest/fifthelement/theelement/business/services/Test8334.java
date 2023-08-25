package fifthelement.theelement.business.services;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

public class Test8334 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.SettingsTest#deleteSongsTest/Trace-1650046437980.txt
     * Method invocation under test: <fifthelement.theelement.business.services.AuthorService: java.util.List getAuthors()>_3219_6436
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_business_services_AuthorService_getAuthors_003() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        fifthelement.theelement.business.services.AuthorService authorservice9 = null;
        authorservice9 = new fifthelement.theelement.business.services.AuthorService();
        authorservice9.getAuthors();
        authorservice9.getMostPlayedAuthor();
        authorservice9.getTotalAuthorPlays();
        authorservice9.getTotalAuthorPlays();
        authorservice9.getAuthors();
        authorservice9.getMostPlayedAuthor();
        authorservice9.getTotalAuthorPlays();
        authorservice9.getTotalAuthorPlays();
        authorservice9.getAuthors();
    }
}

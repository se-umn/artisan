package fifthelement.theelement.business.services;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

public class Test8341 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.SettingsTest#deleteSongsTest/Trace-1650046437980.txt
     * Method invocation under test: <fifthelement.theelement.business.services.AuthorService: fifthelement.theelement.objects.Author getMostPlayedAuthor()>_3245_6488
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_business_services_AuthorService_getMostPlayedAuthor_003() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        fifthelement.theelement.business.services.AuthorService authorservice10 = null;
        authorservice10 = new fifthelement.theelement.business.services.AuthorService();
        authorservice10.getAuthors();
        authorservice10.getMostPlayedAuthor();
        authorservice10.getTotalAuthorPlays();
        authorservice10.getTotalAuthorPlays();
        authorservice10.getAuthors();
        authorservice10.getMostPlayedAuthor();
        authorservice10.getTotalAuthorPlays();
        authorservice10.getTotalAuthorPlays();
        authorservice10.getAuthors();
        authorservice10.getMostPlayedAuthor();
    }
}

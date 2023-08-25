package fifthelement.theelement.business.services;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

public class Test9012 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.CustomizeTests#deleteSong/Trace-1650046523752.txt
     * Method invocation under test: <fifthelement.theelement.business.services.AuthorService: fifthelement.theelement.objects.Author getMostPlayedAuthor()>_2273_4544
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_business_services_AuthorService_getMostPlayedAuthor_002() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        fifthelement.theelement.business.services.AuthorService authorservice6 = null;
        authorservice6 = new fifthelement.theelement.business.services.AuthorService();
        authorservice6.getAuthors();
        authorservice6.getMostPlayedAuthor();
        authorservice6.getTotalAuthorPlays();
        authorservice6.getTotalAuthorPlays();
        authorservice6.getAuthors();
        authorservice6.getMostPlayedAuthor();
    }
}

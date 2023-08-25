package de.lebenshilfe_muenster.uk_gebaerden_muensterland.sign_browser.search;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test208 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /Users/mattia/Faculty/Research/2020_android_test_carving/repositories/action-based-test-carving/apps-src/UK-Gebaerden_Muensterland/traces/de.lebenshilfe_muenster.uk_gebaerden_muensterland.sign_browser.search.SignSearchTest#checkSearchingForTagsWorks/Trace-1651015749366.txt
     * Method invocation under test: <de.lebenshilfe_muenster.uk_gebaerden_muensterland.sign_browser.search.SignSearchActivity: boolean onCreateOptionsMenu(android.view.Menu)>_563_1126
     */
    @Test(timeout = 4000)
    public void test_de_lebenshilfe_muenster_uk_gebaerden_muensterland_sign_browser_search_SignSearchActivity_onCreateOptionsMenu_001() throws Exception {
        android.content.Intent intent6 = null;
        intent6 = new android.content.Intent();
        android.content.Intent intent7 = intent6.putExtra("query", "Person");
        org.robolectric.android.controller.ActivityController activitycontroller25 = org.robolectric.Robolectric.buildActivity(de.lebenshilfe_muenster.uk_gebaerden_muensterland.sign_browser.search.SignSearchActivity.class, intent7);
        de.lebenshilfe_muenster.uk_gebaerden_muensterland.sign_browser.search.SignSearchActivity signsearchactivity7 = (de.lebenshilfe_muenster.uk_gebaerden_muensterland.sign_browser.search.SignSearchActivity) activitycontroller25.get();
        org.robolectric.android.controller.ActivityController activitycontroller26 = activitycontroller25.create();
        org.robolectric.android.controller.ActivityController activitycontroller27 = activitycontroller26.start();
        signsearchactivity7.findViewById(2131230966);
        org.robolectric.android.controller.ActivityController activitycontroller28 = activitycontroller27.pause();
        activitycontroller28.visible();
    }
}

package de.lebenshilfe_muenster.uk_gebaerden_muensterland.sign_browser.search;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test134 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /Users/mattia/Faculty/Research/2020_android_test_carving/repositories/action-based-test-carving/apps-src/UK-Gebaerden_Muensterland/traces/de.lebenshilfe_muenster.uk_gebaerden_muensterland.sign_browser.search.SignSearchTest#checkNavigatingToVideosAndReturningWorks/Trace-1651015738924.txt
     * Method invocation under test: <de.lebenshilfe_muenster.uk_gebaerden_muensterland.sign_browser.search.SignSearchActivity: void onPause()>_541_1082
     */
    @Test(timeout = 4000)
    public void test_de_lebenshilfe_muenster_uk_gebaerden_muensterland_sign_browser_search_SignSearchActivity_onPause_001() throws Exception {
        android.content.Intent intent4 = null;
        intent4 = new android.content.Intent();
        android.content.Intent intent5 = intent4.putExtra("query", "mam");
        org.robolectric.android.controller.ActivityController activitycontroller20 = org.robolectric.Robolectric.buildActivity(de.lebenshilfe_muenster.uk_gebaerden_muensterland.sign_browser.search.SignSearchActivity.class, intent5);
        activitycontroller20.get();
        org.robolectric.android.controller.ActivityController activitycontroller21 = activitycontroller20.create();
        org.robolectric.android.controller.ActivityController activitycontroller22 = activitycontroller21.start();
        activitycontroller22.pause();
    }
}

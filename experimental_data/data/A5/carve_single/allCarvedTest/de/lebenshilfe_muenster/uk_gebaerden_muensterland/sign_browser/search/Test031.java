package de.lebenshilfe_muenster.uk_gebaerden_muensterland.sign_browser.search;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test031 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /Users/mattia/Faculty/Research/2020_android_test_carving/repositories/action-based-test-carving/apps-src/UK-Gebaerden_Muensterland/traces/de.lebenshilfe_muenster.uk_gebaerden_muensterland.sign_browser.search.SignSearchTest#checkStarredButtonIsPresent/Trace-1651015756628.txt
     * Method invocation under test: <de.lebenshilfe_muenster.uk_gebaerden_muensterland.sign_browser.search.SignSearchActivity: void onStart()>_533_1066
     */
    @Test(timeout = 4000)
    public void test_de_lebenshilfe_muenster_uk_gebaerden_muensterland_sign_browser_search_SignSearchActivity_onStart_001() throws Exception {
        android.content.Intent intent2 = null;
        intent2 = new android.content.Intent();
        android.content.Intent intent3 = intent2.putExtra("query", "Mama");
        org.robolectric.android.controller.ActivityController activitycontroller16 = org.robolectric.Robolectric.buildActivity(de.lebenshilfe_muenster.uk_gebaerden_muensterland.sign_browser.search.SignSearchActivity.class, intent3);
        activitycontroller16.get();
        org.robolectric.android.controller.ActivityController activitycontroller17 = activitycontroller16.create();
        activitycontroller17.start();
    }
}

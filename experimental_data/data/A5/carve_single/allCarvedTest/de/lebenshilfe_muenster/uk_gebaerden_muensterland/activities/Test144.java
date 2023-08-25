package de.lebenshilfe_muenster.uk_gebaerden_muensterland.activities;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test144 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /Users/mattia/Faculty/Research/2020_android_test_carving/repositories/action-based-test-carving/apps-src/UK-Gebaerden_Muensterland/traces/de.lebenshilfe_muenster.uk_gebaerden_muensterland.sign_browser.SignBrowserTest#checkSignRecyclerViewIsDisplayed/Trace-1651015865137.txt
     * Method invocation under test: <de.lebenshilfe_muenster.uk_gebaerden_muensterland.activities.MainActivity: void onCreate(android.os.Bundle)>_6_12
     */
    @Test(timeout = 4000)
    public void test_de_lebenshilfe_muenster_uk_gebaerden_muensterland_activities_MainActivity_onCreate_001() throws Exception {
        org.robolectric.android.controller.ActivityController activitycontroller1 = org.robolectric.Robolectric.buildActivity(de.lebenshilfe_muenster.uk_gebaerden_muensterland.activities.MainActivity.class);
        activitycontroller1.get();
        activitycontroller1.create();
    }
}

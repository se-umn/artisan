package de.lebenshilfe_muenster.uk_gebaerden_muensterland.sign_browser.search;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test138 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /Users/mattia/Faculty/Research/2020_android_test_carving/repositories/action-based-test-carving/apps-src/UK-Gebaerden_Muensterland/traces/de.lebenshilfe_muenster.uk_gebaerden_muensterland.sign_browser.search.SignSearchTest#checkNavigatingToVideosAndReturningWorks/Trace-1651015738924.txt
     * Method invocation under test: <de.lebenshilfe_muenster.uk_gebaerden_muensterland.sign_browser.search.SignSearchActivity: void onSignBrowserAdapterSignClicked(de.lebenshilfe_muenster.uk_gebaerden_muensterland.database.Sign)>_700_1398
     */
    @Test(timeout = 4000)
    public void test_de_lebenshilfe_muenster_uk_gebaerden_muensterland_sign_browser_search_SignSearchActivity_onSignBrowserAdapterSignClicked_001() throws Exception {
        android.content.Intent intent9 = null;
        de.lebenshilfe_muenster.uk_gebaerden_muensterland.database.Sign sign4 = org.mockito.Mockito.mock(de.lebenshilfe_muenster.uk_gebaerden_muensterland.database.Sign.class);
        org.mockito.stubbing.Stubber stubber183 = org.mockito.Mockito.doReturn("Mama");
        de.lebenshilfe_muenster.uk_gebaerden_muensterland.database.Sign sign5 = stubber183.when(sign4);
        sign5.getNameLocaleDe();
        org.mockito.stubbing.Stubber stubber184 = org.mockito.Mockito.doReturn(0);
        de.lebenshilfe_muenster.uk_gebaerden_muensterland.database.Sign sign6 = stubber184.when(sign4);
        sign6.getLearningProgress();
        org.mockito.stubbing.Stubber stubber185 = org.mockito.Mockito.doReturn(false);
        de.lebenshilfe_muenster.uk_gebaerden_muensterland.database.Sign sign7 = stubber185.when(sign4);
        sign7.isStarred();
        org.robolectric.android.controller.ActivityController activitycontroller40 = org.robolectric.Robolectric.buildActivity(de.lebenshilfe_muenster.uk_gebaerden_muensterland.sign_browser.search.SignSearchActivity.class, intent9);
        de.lebenshilfe_muenster.uk_gebaerden_muensterland.sign_browser.search.SignSearchActivity signsearchactivity11 = (de.lebenshilfe_muenster.uk_gebaerden_muensterland.sign_browser.search.SignSearchActivity) activitycontroller40.get();
        org.robolectric.android.controller.ActivityController activitycontroller41 = activitycontroller40.create();
        org.robolectric.android.controller.ActivityController activitycontroller42 = activitycontroller41.start();
        signsearchactivity11.findViewById(2131230966);
        activitycontroller42.visible();
        sign7.getNameLocaleDe();
        sign7.getLearningProgress();
        sign7.isStarred();
        signsearchactivity11.onSignBrowserAdapterSignClicked(sign7);
    }
}

package de.lebenshilfe_muenster.uk_gebaerden_muensterland.activities;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test159 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /Users/mattia/Faculty/Research/2020_android_test_carving/repositories/action-based-test-carving/apps-src/UK-Gebaerden_Muensterland/traces/de.lebenshilfe_muenster.uk_gebaerden_muensterland.sign_browser.video.SignVideoTest#checkUpButtonNavigatesToSignBrowser/Trace-1651015812777.txt
     * Method invocation under test: <de.lebenshilfe_muenster.uk_gebaerden_muensterland.activities.LevelOneActivity: void onCreate(android.os.Bundle)>_7145_14290
     */
    @Test(timeout = 4000)
    public void test_de_lebenshilfe_muenster_uk_gebaerden_muensterland_activities_LevelOneActivity_onCreate_001() throws Exception {
        android.content.Intent intent0 = null;
        android.os.Bundle bundle3 = null;
        de.lebenshilfe_muenster.uk_gebaerden_muensterland.database.Sign sign24 = org.mockito.Mockito.mock(de.lebenshilfe_muenster.uk_gebaerden_muensterland.database.Sign.class);
        org.mockito.stubbing.Stubber stubber137 = org.mockito.Mockito.doReturn("Mama");
        de.lebenshilfe_muenster.uk_gebaerden_muensterland.database.Sign sign25 = stubber137.when(sign24);
        sign25.getNameLocaleDe();
        org.mockito.stubbing.Stubber stubber138 = org.mockito.Mockito.doReturn(0);
        de.lebenshilfe_muenster.uk_gebaerden_muensterland.database.Sign sign26 = stubber138.when(sign24);
        sign26.getLearningProgress();
        org.mockito.stubbing.Stubber stubber139 = org.mockito.Mockito.doReturn(false);
        de.lebenshilfe_muenster.uk_gebaerden_muensterland.database.Sign sign27 = stubber139.when(sign24);
        sign27.isStarred();
        org.mockito.stubbing.Stubber stubber140 = org.mockito.Mockito.doReturn("Mama");
        de.lebenshilfe_muenster.uk_gebaerden_muensterland.database.Sign sign28 = stubber140.when(sign24);
        sign28.getNameLocaleDe();
        org.mockito.stubbing.Stubber stubber141 = org.mockito.Mockito.doReturn(0);
        de.lebenshilfe_muenster.uk_gebaerden_muensterland.database.Sign sign29 = stubber141.when(sign24);
        sign29.getLearningProgress();
        org.mockito.stubbing.Stubber stubber142 = org.mockito.Mockito.doReturn(false);
        de.lebenshilfe_muenster.uk_gebaerden_muensterland.database.Sign sign30 = stubber142.when(sign24);
        sign30.isStarred();
        org.mockito.stubbing.Stubber stubber143 = org.mockito.Mockito.doReturn("mama");
        de.lebenshilfe_muenster.uk_gebaerden_muensterland.database.Sign sign31 = stubber143.when(sign24);
        sign31.getName();
        android.content.Context context3 = ApplicationProvider.getApplicationContext();
        sign31.getNameLocaleDe();
        sign31.getLearningProgress();
        sign31.isStarred();
        sign31.getNameLocaleDe();
        sign31.getLearningProgress();
        sign31.isStarred();
        sign31.getName();
        intent0 = new android.content.Intent(context3, de.lebenshilfe_muenster.uk_gebaerden_muensterland.activities.LevelOneActivity.class);
        bundle3 = new android.os.Bundle();
        bundle3.putString("fragment_to_show", "SignVideoFragment");
        bundle3.putParcelable("sign_to_show", sign31);
        android.content.Intent intent1 = intent0.putExtra("extra", bundle3);
        org.robolectric.android.controller.ActivityController activitycontroller13 = org.robolectric.Robolectric.buildActivity(de.lebenshilfe_muenster.uk_gebaerden_muensterland.activities.LevelOneActivity.class, intent1);
        activitycontroller13.get();
        activitycontroller13.create();
    }
}

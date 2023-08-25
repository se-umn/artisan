package com.dcfportfolio.owlflashcardsen_cnlevel1;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test165 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /Users/mattia/Faculty/Research/2020_android_test_carving/repositories/action-based-test-carving/apps-src/OWLFlashCardsencnlevel1/traces/com.dcfportfolio.owlflashcardsen_cnlevel1.CardMenuTest#openCardMenu9/Trace-1650559478827.txt
     * Method invocation under test: <com.dcfportfolio.owlflashcardsen_cnlevel1.CardPagerActivity: boolean onCreateOptionsMenu(android.view.Menu)>_230_460
     */
    @Test(timeout = 4000)
    public void test_com_dcfportfolio_owlflashcardsen_cnlevel1_CardPagerActivity_onCreateOptionsMenu_001() throws Exception {
        android.content.Intent intent3 = null;
        android.content.Context context3 = ApplicationProvider.getApplicationContext();
        intent3 = new android.content.Intent(context3, com.dcfportfolio.owlflashcardsen_cnlevel1.CardPagerActivity.class);
        android.content.Intent intent4 = intent3.putExtra("com.dcfportfolio.owlflashcardsen_cnlevel1.CARD_TITLE", "Transportation");
        android.content.Intent intent5 = intent4.putExtra("com.dcfportfolio.owlflashcardsen_cnlevel1.CARD_CATEGORY", 9);
        org.robolectric.android.controller.ActivityController activitycontroller35 = org.robolectric.Robolectric.buildActivity(com.dcfportfolio.owlflashcardsen_cnlevel1.CardPagerActivity.class, intent5);
        activitycontroller35.get();
        org.robolectric.android.controller.ActivityController activitycontroller36 = activitycontroller35.create();
        activitycontroller36.visible();
    }
}

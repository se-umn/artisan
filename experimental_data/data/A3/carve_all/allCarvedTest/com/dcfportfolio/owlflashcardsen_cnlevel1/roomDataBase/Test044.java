package com.dcfportfolio.owlflashcardsen_cnlevel1.roomDataBase;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test044 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /Users/mattia/Faculty/Research/2020_android_test_carving/repositories/action-based-test-carving/apps-src/OWLFlashCardsencnlevel1/traces/com.dcfportfolio.owlflashcardsen_cnlevel1.CardMenuTest#openCardMenu4/Trace-1650559474138.txt
     * Method invocation under test: <com.dcfportfolio.owlflashcardsen_cnlevel1.roomDataBase.CardRepository: android.arch.lifecycle.LiveData getInitCard()>_186_369
     */
    @Test(timeout = 4000)
    public void test_com_dcfportfolio_owlflashcardsen_cnlevel1_roomDataBase_CardRepository_getInitCard_001() throws Exception {
        com.dcfportfolio.owlflashcardsen_cnlevel1.roomDataBase.CardRepository cardrepository2 = null;
        org.robolectric.android.controller.ActivityController activitycontroller23 = org.robolectric.Robolectric.buildActivity(com.dcfportfolio.owlflashcardsen_cnlevel1.CardMenu.class);
        com.dcfportfolio.owlflashcardsen_cnlevel1.CardMenu cardmenu11 = (com.dcfportfolio.owlflashcardsen_cnlevel1.CardMenu) activitycontroller23.get();
        org.robolectric.android.controller.ActivityController activitycontroller24 = activitycontroller23.create();
        activitycontroller24.visible();
        android.app.Application application7 = (android.app.Application) cardmenu11.getApplicationContext();
        cardrepository2 = new com.dcfportfolio.owlflashcardsen_cnlevel1.roomDataBase.CardRepository(application7);
        cardrepository2.getAllCards();
        cardrepository2.getInitCard();
    }
}

package com.dcfportfolio.owlflashcardsen_cnlevel1.roomDataBase;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test128 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /Users/mattia/Faculty/Research/2020_android_test_carving/repositories/action-based-test-carving/apps-src/OWLFlashCardsencnlevel1/traces/com.dcfportfolio.owlflashcardsen_cnlevel1.CardMenuTest#openCardMenu6/Trace-1650559450062.txt
     * Method invocation under test: <com.dcfportfolio.owlflashcardsen_cnlevel1.roomDataBase.CardRepository: android.arch.lifecycle.LiveData getAllCards()>_185_367
     */
    @Test(timeout = 4000)
    public void test_com_dcfportfolio_owlflashcardsen_cnlevel1_roomDataBase_CardRepository_getAllCards_001() throws Exception {
        com.dcfportfolio.owlflashcardsen_cnlevel1.roomDataBase.CardRepository cardrepository1 = null;
        org.robolectric.android.controller.ActivityController activitycontroller19 = org.robolectric.Robolectric.buildActivity(com.dcfportfolio.owlflashcardsen_cnlevel1.CardMenu.class);
        com.dcfportfolio.owlflashcardsen_cnlevel1.CardMenu cardmenu9 = (com.dcfportfolio.owlflashcardsen_cnlevel1.CardMenu) activitycontroller19.get();
        org.robolectric.android.controller.ActivityController activitycontroller20 = activitycontroller19.create();
        activitycontroller20.visible();
        android.app.Application application5 = (android.app.Application) cardmenu9.getApplicationContext();
        cardrepository1 = new com.dcfportfolio.owlflashcardsen_cnlevel1.roomDataBase.CardRepository(application5);
        cardrepository1.getAllCards();
    }
}

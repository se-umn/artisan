package com.dcfportfolio.owlflashcardsen_cnlevel1.roomDataBase;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test163 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /Users/mattia/Faculty/Research/2020_android_test_carving/repositories/action-based-test-carving/apps-src/OWLFlashCardsencnlevel1/traces/com.dcfportfolio.owlflashcardsen_cnlevel1.CardMenuTest#openCardMenu9/Trace-1650559478827.txt
     * Method invocation under test: <com.dcfportfolio.owlflashcardsen_cnlevel1.roomDataBase.CardRepository: android.arch.lifecycle.LiveData getSomeCards(int)>_209_416
     */
    @Test(timeout = 4000)
    public void test_com_dcfportfolio_owlflashcardsen_cnlevel1_roomDataBase_CardRepository_getSomeCards_001() throws Exception {
        com.dcfportfolio.owlflashcardsen_cnlevel1.roomDataBase.CardRepository cardrepository3 = null;
        org.robolectric.android.controller.ActivityController activitycontroller31 = org.robolectric.Robolectric.buildActivity(com.dcfportfolio.owlflashcardsen_cnlevel1.CardMenu.class);
        com.dcfportfolio.owlflashcardsen_cnlevel1.CardMenu cardmenu15 = (com.dcfportfolio.owlflashcardsen_cnlevel1.CardMenu) activitycontroller31.get();
        org.robolectric.android.controller.ActivityController activitycontroller32 = activitycontroller31.create();
        activitycontroller32.visible();
        android.app.Application application11 = (android.app.Application) cardmenu15.getApplicationContext();
        cardrepository3 = new com.dcfportfolio.owlflashcardsen_cnlevel1.roomDataBase.CardRepository(application11);
        cardrepository3.getAllCards();
        cardrepository3.getInitCard();
        cardrepository3.getSomeCards(9);
    }
}

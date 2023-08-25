package com.dcfportfolio.owlflashcardsen_cnlevel1.roomDataBase;

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
     * Generated from /Users/mattia/Faculty/Research/2020_android_test_carving/repositories/action-based-test-carving/apps-src/OWLFlashCardsencnlevel1/traces/com.dcfportfolio.owlflashcardsen_cnlevel1.CardMenuTest#openCardMenu7/Trace-1650559455040.txt
     * Method invocation under test: <com.dcfportfolio.owlflashcardsen_cnlevel1.roomDataBase.CardRepository: void <init>(android.app.Application)>_134_265
     */
    @Test(timeout = 4000)
    public void test_com_dcfportfolio_owlflashcardsen_cnlevel1_roomDataBase_CardRepository_constructor_001() throws Exception {
        com.dcfportfolio.owlflashcardsen_cnlevel1.roomDataBase.CardRepository cardrepository0 = null;
        org.robolectric.android.controller.ActivityController activitycontroller15 = org.robolectric.Robolectric.buildActivity(com.dcfportfolio.owlflashcardsen_cnlevel1.CardMenu.class);
        com.dcfportfolio.owlflashcardsen_cnlevel1.CardMenu cardmenu7 = (com.dcfportfolio.owlflashcardsen_cnlevel1.CardMenu) activitycontroller15.get();
        org.robolectric.android.controller.ActivityController activitycontroller16 = activitycontroller15.create();
        activitycontroller16.visible();
        android.app.Application application3 = (android.app.Application) cardmenu7.getApplicationContext();
        cardrepository0 = new com.dcfportfolio.owlflashcardsen_cnlevel1.roomDataBase.CardRepository(application3);
    }
}

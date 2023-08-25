package com.dcfportfolio.owlflashcardsen_cnlevel1.roomDataBase;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test130 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /Users/mattia/Faculty/Research/2020_android_test_carving/repositories/action-based-test-carving/apps-src/OWLFlashCardsencnlevel1/traces/com.dcfportfolio.owlflashcardsen_cnlevel1.CardMenuTest#openCardMenu6/Trace-1650559450062.txt
     * Method invocation under test: <com.dcfportfolio.owlflashcardsen_cnlevel1.roomDataBase.CardViewModel: void setCategory(int)>_198_395
     */
    @Test(timeout = 4000)
    public void test_com_dcfportfolio_owlflashcardsen_cnlevel1_roomDataBase_CardViewModel_setCategory_001() throws Exception {
        com.dcfportfolio.owlflashcardsen_cnlevel1.roomDataBase.CardViewModel cardviewmodel1 = null;
        org.robolectric.android.controller.ActivityController activitycontroller27 = org.robolectric.Robolectric.buildActivity(com.dcfportfolio.owlflashcardsen_cnlevel1.CardMenu.class);
        com.dcfportfolio.owlflashcardsen_cnlevel1.CardMenu cardmenu13 = (com.dcfportfolio.owlflashcardsen_cnlevel1.CardMenu) activitycontroller27.get();
        org.robolectric.android.controller.ActivityController activitycontroller28 = activitycontroller27.create();
        activitycontroller28.visible();
        android.app.Application application9 = (android.app.Application) cardmenu13.getApplicationContext();
        cardviewmodel1 = new com.dcfportfolio.owlflashcardsen_cnlevel1.roomDataBase.CardViewModel(application9);
        cardviewmodel1.setCategory(6);
    }
}

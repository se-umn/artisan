package com.dcfportfolio.owlflashcardsen_cnlevel1.roomDataBase;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test004 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /Users/mattia/Faculty/Research/2020_android_test_carving/repositories/action-based-test-carving/apps-src/OWLFlashCardsencnlevel1/traces/com.dcfportfolio.owlflashcardsen_cnlevel1.CardMenuTest#openCardMenu2/Trace-1650559465392.txt
     * Method invocation under test: <com.dcfportfolio.owlflashcardsen_cnlevel1.roomDataBase.CardViewModel: void <init>(android.app.Application)>_124_246
     */
    @Test(timeout = 4000)
    public void test_com_dcfportfolio_owlflashcardsen_cnlevel1_roomDataBase_CardViewModel_constructor_001() throws Exception {
        com.dcfportfolio.owlflashcardsen_cnlevel1.roomDataBase.CardViewModel cardviewmodel0 = null;
        org.robolectric.android.controller.ActivityController activitycontroller11 = org.robolectric.Robolectric.buildActivity(com.dcfportfolio.owlflashcardsen_cnlevel1.CardMenu.class);
        com.dcfportfolio.owlflashcardsen_cnlevel1.CardMenu cardmenu5 = (com.dcfportfolio.owlflashcardsen_cnlevel1.CardMenu) activitycontroller11.get();
        org.robolectric.android.controller.ActivityController activitycontroller12 = activitycontroller11.create();
        activitycontroller12.visible();
        android.app.Application application1 = (android.app.Application) cardmenu5.getApplicationContext();
        cardviewmodel0 = new com.dcfportfolio.owlflashcardsen_cnlevel1.roomDataBase.CardViewModel(application1);
    }
}

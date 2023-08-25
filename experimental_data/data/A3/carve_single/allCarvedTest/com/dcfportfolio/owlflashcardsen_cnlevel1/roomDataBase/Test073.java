package com.dcfportfolio.owlflashcardsen_cnlevel1.roomDataBase;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test073 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /Users/mattia/Faculty/Research/2020_android_test_carving/repositories/action-based-test-carving/apps-src/OWLFlashCardsencnlevel1/traces/com.dcfportfolio.owlflashcardsen_cnlevel1.SearchActivityTest#searchBarTest/Trace-1650559430531.txt
     * Method invocation under test: <com.dcfportfolio.owlflashcardsen_cnlevel1.roomDataBase.CardRepository: android.arch.lifecycle.LiveData getAllCards()>_75_147
     */
    @Test(timeout = 4000)
    public void test_com_dcfportfolio_owlflashcardsen_cnlevel1_roomDataBase_CardRepository_getAllCards_001() throws Exception {
        com.dcfportfolio.owlflashcardsen_cnlevel1.roomDataBase.CardRepository cardrepository1 = null;
        android.app.Application application22 = null;
        android.app.Application application24 = org.mockito.Mockito.mock(android.app.Application.class);
        org.mockito.stubbing.Stubber stubber15 = org.mockito.Mockito.doReturn(application22);
        android.app.Application application25 = stubber15.when(application24);
        application25.getApplicationContext();
        org.mockito.stubbing.Stubber stubber16 = org.mockito.Mockito.doReturn("Flash Card Database");
        android.app.Application application27 = stubber16.when(application24);
        application27.getString(2131624020);
        org.mockito.stubbing.Stubber stubber17 = org.mockito.Mockito.doReturn(application22);
        android.app.Application application28 = stubber17.when(application24);
        application28.getApplicationContext();
        cardrepository1 = new com.dcfportfolio.owlflashcardsen_cnlevel1.roomDataBase.CardRepository(application28);
        cardrepository1.getAllCards();
    }
}

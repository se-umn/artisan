package com.dcfportfolio.owlflashcardsen_cnlevel1.roomDataBase;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test099 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /Users/mattia/Faculty/Research/2020_android_test_carving/repositories/action-based-test-carving/apps-src/OWLFlashCardsencnlevel1/traces/com.dcfportfolio.owlflashcardsen_cnlevel1.SearchActivityTest#searchBarTestChinese/Trace-1650559440115.txt
     * Method invocation under test: <com.dcfportfolio.owlflashcardsen_cnlevel1.roomDataBase.CardRepository: android.arch.lifecycle.LiveData findCard(java.lang.String)>_122_239
     */
    @Test(timeout = 4000)
    public void test_com_dcfportfolio_owlflashcardsen_cnlevel1_roomDataBase_CardRepository_findCard_001() throws Exception {
        android.app.Application application53 = null;
        com.dcfportfolio.owlflashcardsen_cnlevel1.roomDataBase.CardRepository cardrepository3 = null;
        android.app.Application application54 = org.mockito.Mockito.mock(android.app.Application.class);
        org.mockito.stubbing.Stubber stubber33 = org.mockito.Mockito.doReturn(application53);
        android.app.Application application55 = stubber33.when(application54);
        application55.getApplicationContext();
        org.mockito.stubbing.Stubber stubber34 = org.mockito.Mockito.doReturn("Flash Card Database");
        android.app.Application application57 = stubber34.when(application54);
        application57.getString(2131624020);
        org.mockito.stubbing.Stubber stubber35 = org.mockito.Mockito.doReturn(application53);
        android.app.Application application58 = stubber35.when(application54);
        application58.getApplicationContext();
        cardrepository3 = new com.dcfportfolio.owlflashcardsen_cnlevel1.roomDataBase.CardRepository(application58);
        cardrepository3.getAllCards();
        cardrepository3.getInitCard();
        cardrepository3.findCard("");
    }
}

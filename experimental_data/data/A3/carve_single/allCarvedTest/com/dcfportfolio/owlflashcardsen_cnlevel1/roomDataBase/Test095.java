package com.dcfportfolio.owlflashcardsen_cnlevel1.roomDataBase;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test095 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /Users/mattia/Faculty/Research/2020_android_test_carving/repositories/action-based-test-carving/apps-src/OWLFlashCardsencnlevel1/traces/com.dcfportfolio.owlflashcardsen_cnlevel1.SearchActivityTest#searchBarTestChinese/Trace-1650559440115.txt
     * Method invocation under test: <com.dcfportfolio.owlflashcardsen_cnlevel1.roomDataBase.CardRepository: android.arch.lifecycle.LiveData getInitCard()>_76_149
     */
    @Test(timeout = 4000)
    public void test_com_dcfportfolio_owlflashcardsen_cnlevel1_roomDataBase_CardRepository_getInitCard_001() throws Exception {
        android.app.Application application33 = null;
        com.dcfportfolio.owlflashcardsen_cnlevel1.roomDataBase.CardRepository cardrepository2 = null;
        android.app.Application application34 = org.mockito.Mockito.mock(android.app.Application.class);
        org.mockito.stubbing.Stubber stubber21 = org.mockito.Mockito.doReturn(application33);
        android.app.Application application35 = stubber21.when(application34);
        application35.getApplicationContext();
        org.mockito.stubbing.Stubber stubber22 = org.mockito.Mockito.doReturn("Flash Card Database");
        android.app.Application application37 = stubber22.when(application34);
        application37.getString(2131624020);
        org.mockito.stubbing.Stubber stubber23 = org.mockito.Mockito.doReturn(application33);
        android.app.Application application38 = stubber23.when(application34);
        application38.getApplicationContext();
        cardrepository2 = new com.dcfportfolio.owlflashcardsen_cnlevel1.roomDataBase.CardRepository(application38);
        cardrepository2.getAllCards();
        cardrepository2.getInitCard();
    }
}

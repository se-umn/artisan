package com.dcfportfolio.owlflashcardsen_cnlevel1.roomDataBase;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test067 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /Users/mattia/Faculty/Research/2020_android_test_carving/repositories/action-based-test-carving/apps-src/OWLFlashCardsencnlevel1/traces/com.dcfportfolio.owlflashcardsen_cnlevel1.SearchActivityTest#searchBarTest/Trace-1650559430531.txt
     * Method invocation under test: <com.dcfportfolio.owlflashcardsen_cnlevel1.roomDataBase.CardRepository: void <init>(android.app.Application)>_24_45
     */
    @Test(timeout = 4000)
    public void test_com_dcfportfolio_owlflashcardsen_cnlevel1_roomDataBase_CardRepository_constructor_001() throws Exception {
        com.dcfportfolio.owlflashcardsen_cnlevel1.roomDataBase.CardRepository cardrepository0 = null;
        android.app.Application application12 = null;
        android.app.Application application14 = org.mockito.Mockito.mock(android.app.Application.class);
        org.mockito.stubbing.Stubber stubber9 = org.mockito.Mockito.doReturn(application12);
        android.app.Application application15 = stubber9.when(application14);
        application15.getApplicationContext();
        org.mockito.stubbing.Stubber stubber10 = org.mockito.Mockito.doReturn("Flash Card Database");
        android.app.Application application17 = stubber10.when(application14);
        application17.getString(2131624020);
        org.mockito.stubbing.Stubber stubber11 = org.mockito.Mockito.doReturn(application12);
        android.app.Application application18 = stubber11.when(application14);
        application18.getApplicationContext();
        cardrepository0 = new com.dcfportfolio.owlflashcardsen_cnlevel1.roomDataBase.CardRepository(application18);
    }
}

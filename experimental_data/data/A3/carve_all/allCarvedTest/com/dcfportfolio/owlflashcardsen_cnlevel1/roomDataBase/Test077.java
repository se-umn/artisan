package com.dcfportfolio.owlflashcardsen_cnlevel1.roomDataBase;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test077 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /Users/mattia/Faculty/Research/2020_android_test_carving/repositories/action-based-test-carving/apps-src/OWLFlashCardsencnlevel1/traces/com.dcfportfolio.owlflashcardsen_cnlevel1.SearchActivityTest#searchBarTest/Trace-1650559430531.txt
     * Method invocation under test: <com.dcfportfolio.owlflashcardsen_cnlevel1.roomDataBase.CardViewModel: void setSearchTerm(java.lang.String)>_118_235
     */
    @Test(timeout = 4000)
    public void test_com_dcfportfolio_owlflashcardsen_cnlevel1_roomDataBase_CardViewModel_setSearchTerm_001() throws Exception {
        com.dcfportfolio.owlflashcardsen_cnlevel1.roomDataBase.CardViewModel cardviewmodel1 = null;
        android.app.Application application42 = null;
        android.app.Application application44 = org.mockito.Mockito.mock(android.app.Application.class);
        org.mockito.stubbing.Stubber stubber27 = org.mockito.Mockito.doReturn(application42);
        android.app.Application application45 = stubber27.when(application44);
        application45.getApplicationContext();
        org.mockito.stubbing.Stubber stubber28 = org.mockito.Mockito.doReturn("Flash Card Database");
        android.app.Application application47 = stubber28.when(application44);
        application47.getString(2131624020);
        org.mockito.stubbing.Stubber stubber29 = org.mockito.Mockito.doReturn(application42);
        android.app.Application application48 = stubber29.when(application44);
        application48.getApplicationContext();
        cardviewmodel1 = new com.dcfportfolio.owlflashcardsen_cnlevel1.roomDataBase.CardViewModel(application48);
        cardviewmodel1.setSearchTerm("sky");
    }
}

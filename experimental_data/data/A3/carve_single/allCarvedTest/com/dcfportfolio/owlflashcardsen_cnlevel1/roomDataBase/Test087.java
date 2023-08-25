package com.dcfportfolio.owlflashcardsen_cnlevel1.roomDataBase;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test087 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /Users/mattia/Faculty/Research/2020_android_test_carving/repositories/action-based-test-carving/apps-src/OWLFlashCardsencnlevel1/traces/com.dcfportfolio.owlflashcardsen_cnlevel1.SearchActivityTest#searchBarTestChinese/Trace-1650559440115.txt
     * Method invocation under test: <com.dcfportfolio.owlflashcardsen_cnlevel1.roomDataBase.CardViewModel: void <init>(android.app.Application)>_14_26
     */
    @Test(timeout = 4000)
    public void test_com_dcfportfolio_owlflashcardsen_cnlevel1_roomDataBase_CardViewModel_constructor_001() throws Exception {
        com.dcfportfolio.owlflashcardsen_cnlevel1.roomDataBase.CardViewModel cardviewmodel0 = null;
        android.app.Application application3 = null;
        android.app.Application application4 = org.mockito.Mockito.mock(android.app.Application.class);
        org.mockito.stubbing.Stubber stubber3 = org.mockito.Mockito.doReturn(application3);
        android.app.Application application5 = stubber3.when(application4);
        application5.getApplicationContext();
        org.mockito.stubbing.Stubber stubber4 = org.mockito.Mockito.doReturn("Flash Card Database");
        android.app.Application application7 = stubber4.when(application4);
        application7.getString(2131624020);
        org.mockito.stubbing.Stubber stubber5 = org.mockito.Mockito.doReturn(application3);
        android.app.Application application8 = stubber5.when(application4);
        application8.getApplicationContext();
        cardviewmodel0 = new com.dcfportfolio.owlflashcardsen_cnlevel1.roomDataBase.CardViewModel(application8);
    }
}

package com.dcfportfolio.owlflashcardsen_cnlevel1.roomDataBase;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test089 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /Users/mattia/Faculty/Research/2020_android_test_carving/repositories/action-based-test-carving/apps-src/OWLFlashCardsencnlevel1/traces/com.dcfportfolio.owlflashcardsen_cnlevel1.SearchActivityTest#searchBarTestChinese/Trace-1650559440115.txt
     * Method invocation under test: <com.dcfportfolio.owlflashcardsen_cnlevel1.roomDataBase.CardDatabase_Impl: void <init>()>_38_70
     */
    @Test(timeout = 4000)
    public void test_com_dcfportfolio_owlflashcardsen_cnlevel1_roomDataBase_CardDatabase_Impl_constructor_001() throws Exception {
        com.dcfportfolio.owlflashcardsen_cnlevel1.roomDataBase.CardDatabase_Impl carddatabase_impl0 = null;
        carddatabase_impl0 = new com.dcfportfolio.owlflashcardsen_cnlevel1.roomDataBase.CardDatabase_Impl();
    }
}

package com.dcfportfolio.owlflashcardsen_cnlevel1;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test076 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /Users/mattia/Faculty/Research/2020_android_test_carving/repositories/action-based-test-carving/apps-src/OWLFlashCardsencnlevel1/traces/com.dcfportfolio.owlflashcardsen_cnlevel1.SearchActivityTest#searchBarTest/Trace-1650559430531.txt
     * Method invocation under test: <com.dcfportfolio.owlflashcardsen_cnlevel1.SearchActivity: void commitSearch(android.view.View)>_112_224
     */
    @Test(timeout = 4000)
    public void test_com_dcfportfolio_owlflashcardsen_cnlevel1_SearchActivity_commitSearch_001() throws Exception {
        android.support.v7.widget.AppCompatButton appcompatbutton1 = org.mockito.Mockito.mock(android.support.v7.widget.AppCompatButton.class);
        org.robolectric.android.controller.ActivityController activitycontroller8 = org.robolectric.Robolectric.buildActivity(com.dcfportfolio.owlflashcardsen_cnlevel1.SearchActivity.class);
        com.dcfportfolio.owlflashcardsen_cnlevel1.SearchActivity searchactivity5 = (com.dcfportfolio.owlflashcardsen_cnlevel1.SearchActivity) activitycontroller8.get();
        org.robolectric.android.controller.ActivityController activitycontroller9 = activitycontroller8.create();
        activitycontroller9.visible();
        searchactivity5.commitSearch(appcompatbutton1);
    }
}

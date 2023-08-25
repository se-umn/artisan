package com.dcfportfolio.owlflashcardsen_cnlevel1;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test084 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /Users/mattia/Faculty/Research/2020_android_test_carving/repositories/action-based-test-carving/apps-src/OWLFlashCardsencnlevel1/traces/com.dcfportfolio.owlflashcardsen_cnlevel1.CardMenuTest#openPronounceActivity/Trace-1650559471990.txt
     * Method invocation under test: <com.dcfportfolio.owlflashcardsen_cnlevel1.PronunciationGuideActivity: boolean onCreateOptionsMenu(android.view.Menu)>_111_222
     */
    @Test(timeout = 4000)
    public void test_com_dcfportfolio_owlflashcardsen_cnlevel1_PronunciationGuideActivity_onCreateOptionsMenu_001() throws Exception {
        org.robolectric.android.controller.ActivityController activitycontroller11 = org.robolectric.Robolectric.buildActivity(com.dcfportfolio.owlflashcardsen_cnlevel1.PronunciationGuideActivity.class);
        activitycontroller11.get();
        org.robolectric.android.controller.ActivityController activitycontroller12 = activitycontroller11.create();
        activitycontroller12.visible();
    }
}

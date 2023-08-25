package com.dcfportfolio.owlflashcardsen_cnlevel1;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test104 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /Users/mattia/Faculty/Research/2020_android_test_carving/repositories/action-based-test-carving/apps-src/OWLFlashCardsencnlevel1/traces/com.dcfportfolio.owlflashcardsen_cnlevel1.CardMenuTest#openCardMenu8/Trace-1650559461098.txt
     * Method invocation under test: <com.dcfportfolio.owlflashcardsen_cnlevel1.CardPagerActivity: void onCreate(android.os.Bundle)>_80_160
     */
    @Test(timeout = 4000)
    public void test_com_dcfportfolio_owlflashcardsen_cnlevel1_CardPagerActivity_onCreate_001() throws Exception {
        android.content.Intent intent0 = null;
        android.content.Context context1 = ApplicationProvider.getApplicationContext();
        intent0 = new android.content.Intent(context1, com.dcfportfolio.owlflashcardsen_cnlevel1.CardPagerActivity.class);
        android.content.Intent intent1 = intent0.putExtra("com.dcfportfolio.owlflashcardsen_cnlevel1.CARD_TITLE", "Shapes and Colors");
        android.content.Intent intent2 = intent1.putExtra("com.dcfportfolio.owlflashcardsen_cnlevel1.CARD_CATEGORY", 8);
        org.robolectric.android.controller.ActivityController activitycontroller8 = org.robolectric.Robolectric.buildActivity(com.dcfportfolio.owlflashcardsen_cnlevel1.CardPagerActivity.class, intent2);
        activitycontroller8.get();
        activitycontroller8.create();
    }
}

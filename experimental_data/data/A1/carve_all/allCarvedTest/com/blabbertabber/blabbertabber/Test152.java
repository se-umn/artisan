package com.blabbertabber.blabbertabber;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import abc.shadows.ABCTextViewShadow152;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
@org.robolectric.annotation.Config(shadows = { ABCTextViewShadow152.class })
public class Test152 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /Users/mattia/Faculty/Research/2020_android_test_carving/repositories/action-based-test-carving/apps-src/blabbertabber/traces/com.blabbertabber.blabbertabber.AboutActivityTest#buttonAckFinishTest/Trace-1650060773293.txt
     * Method invocation under test: <com.blabbertabber.blabbertabber.AboutActivity: void onResume()>_7_14
     */
    @Test(timeout = 4000)
    public void test_com_blabbertabber_blabbertabber_AboutActivity_onResume_001() throws Exception {
        org.robolectric.android.controller.ActivityController activitycontroller4 = org.robolectric.Robolectric.buildActivity(com.blabbertabber.blabbertabber.AboutActivity.class);
        com.blabbertabber.blabbertabber.AboutActivity aboutactivity3 = (com.blabbertabber.blabbertabber.AboutActivity) activitycontroller4.get();
        org.robolectric.android.controller.ActivityController activitycontroller5 = activitycontroller4.create();
        android.widget.TextView textview2 = (android.widget.TextView) aboutactivity3.findViewById(com.blabbertabber.blabbertabber.R.id.aboutVersion);
        ABCTextViewShadow152 abctextviewshadow1521 = org.robolectric.shadow.api.Shadow.extract(textview2);
        android.widget.TextView textview3 = org.mockito.Mockito.mock(android.widget.TextView.class);
        org.mockito.stubbing.Stubber stubber1 = org.mockito.Mockito.doNothing();
        android.widget.TextView textview4 = stubber1.when(textview3);
        textview4.setText("BlabberTabber version: 1.0.10\\n");
        abctextviewshadow1521.setMockFor("<android.widget.TextView: void setText(java.lang.CharSequence)>", textview3);
        abctextviewshadow1521.setStrictShadow();
        activitycontroller5.resume();
    }
}

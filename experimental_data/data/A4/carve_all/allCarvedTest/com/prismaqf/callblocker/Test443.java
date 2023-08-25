package com.prismaqf.callblocker;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test443 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.EditFiltersTest#NavigateUpIsContextSensitive/Trace-1651091805625.txt
     * Method invocation under test: <com.prismaqf.callblocker.EditCalendarRules: boolean onOptionsItemSelected(android.view.MenuItem)>_415_830
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_EditCalendarRules_onOptionsItemSelected_001() throws Exception {
        android.content.Intent intent24 = null;
        androidx.appcompat.view.menu.ActionMenuItem actionmenuitem2 = org.mockito.Mockito.mock(androidx.appcompat.view.menu.ActionMenuItem.class);
        org.mockito.stubbing.Stubber stubber103 = org.mockito.Mockito.doReturn(16908332);
        androidx.appcompat.view.menu.ActionMenuItem actionmenuitem3 = stubber103.when(actionmenuitem2);
        actionmenuitem3.getItemId();
        android.content.Context context31 = ApplicationProvider.getApplicationContext();
        intent24 = new android.content.Intent(context31, com.prismaqf.callblocker.EditCalendarRules.class);
        android.content.Intent intent25 = intent24.putExtra("com.prismaqf.callblocker:context", "com.prismaqf.callblocker:pick");
        org.robolectric.android.controller.ActivityController activitycontroller56 = org.robolectric.Robolectric.buildActivity(com.prismaqf.callblocker.EditCalendarRules.class, intent25);
        com.prismaqf.callblocker.EditCalendarRules editcalendarrules5 = (com.prismaqf.callblocker.EditCalendarRules) activitycontroller56.get();
        org.robolectric.android.controller.ActivityController activitycontroller57 = activitycontroller56.create();
        activitycontroller57.visible();
        editcalendarrules5.onOptionsItemSelected(actionmenuitem3);
    }
}

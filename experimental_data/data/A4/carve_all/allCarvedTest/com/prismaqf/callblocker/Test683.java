package com.prismaqf.callblocker;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test683 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.NewCalendarRuleTest#TestSingleDays/Trace-1651091736894.txt
     * Method invocation under test: <com.prismaqf.callblocker.NewEditCalendarRule: void validateActions()>_378_755
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_NewEditCalendarRule_validateActions_004() throws Exception {
        android.content.Intent intent15 = null;
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox112 = org.mockito.Mockito.mock(androidx.appcompat.widget.AppCompatCheckBox.class);
        org.mockito.stubbing.Stubber stubber122 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox113 = stubber122.when(appcompatcheckbox112);
        appcompatcheckbox113.setEnabled(true);
        org.mockito.stubbing.Stubber stubber123 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox114 = stubber123.when(appcompatcheckbox112);
        appcompatcheckbox114.setEnabled(true);
        org.mockito.stubbing.Stubber stubber124 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox115 = stubber124.when(appcompatcheckbox112);
        appcompatcheckbox115.setChecked(true);
        org.mockito.stubbing.Stubber stubber125 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox116 = stubber125.when(appcompatcheckbox112);
        appcompatcheckbox116.setChecked(false);
        org.mockito.stubbing.Stubber stubber126 = org.mockito.Mockito.doReturn(com.prismaqf.callblocker.R.id.cb_Monday);
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox117 = stubber126.when(appcompatcheckbox112);
        appcompatcheckbox117.getId();
        org.mockito.stubbing.Stubber stubber127 = org.mockito.Mockito.doReturn(true);
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox118 = stubber127.when(appcompatcheckbox112);
        appcompatcheckbox118.isChecked();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox119 = org.mockito.Mockito.mock(androidx.appcompat.widget.AppCompatCheckBox.class);
        org.mockito.stubbing.Stubber stubber128 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox120 = stubber128.when(appcompatcheckbox119);
        appcompatcheckbox120.setEnabled(true);
        org.mockito.stubbing.Stubber stubber129 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox121 = stubber129.when(appcompatcheckbox119);
        appcompatcheckbox121.setEnabled(true);
        org.mockito.stubbing.Stubber stubber130 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox122 = stubber130.when(appcompatcheckbox119);
        appcompatcheckbox122.setChecked(true);
        org.mockito.stubbing.Stubber stubber131 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox123 = stubber131.when(appcompatcheckbox119);
        appcompatcheckbox123.setChecked(false);
        org.mockito.stubbing.Stubber stubber132 = org.mockito.Mockito.doReturn(com.prismaqf.callblocker.R.id.cb_Tuesday);
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox124 = stubber132.when(appcompatcheckbox119);
        appcompatcheckbox124.getId();
        org.mockito.stubbing.Stubber stubber133 = org.mockito.Mockito.doReturn(true);
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox125 = stubber133.when(appcompatcheckbox119);
        appcompatcheckbox125.isChecked();
        androidx.appcompat.widget.AppCompatButton appcompatbutton39 = org.mockito.Mockito.mock(androidx.appcompat.widget.AppCompatButton.class);
        org.mockito.stubbing.Stubber stubber134 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatButton appcompatbutton40 = stubber134.when(appcompatbutton39);
        appcompatbutton40.setEnabled(true);
        org.mockito.stubbing.Stubber stubber135 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatButton appcompatbutton41 = stubber135.when(appcompatbutton39);
        appcompatbutton41.setEnabled(true);
        org.robolectric.android.controller.ActivityController activitycontroller43 = org.robolectric.Robolectric.buildActivity(com.prismaqf.callblocker.NewEditCalendarRule.class, intent15);
        com.prismaqf.callblocker.NewEditCalendarRule neweditcalendarrule19 = (com.prismaqf.callblocker.NewEditCalendarRule) activitycontroller43.get();
        org.robolectric.android.controller.ActivityController activitycontroller44 = activitycontroller43.create();
        activitycontroller44.visible();
        neweditcalendarrule19.onNoDays(appcompatbutton41);
        neweditcalendarrule19.onCheckDay(appcompatcheckbox118);
        neweditcalendarrule19.onCheckDay(appcompatcheckbox125);
        neweditcalendarrule19.validateActions();
    }
}

package com.prismaqf.callblocker;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test684 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.NewCalendarRuleTest#TestSingleDays/Trace-1651091736894.txt
     * Method invocation under test: <com.prismaqf.callblocker.NewEditCalendarRule: void onCheckDay(android.view.View)>_384_768
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_NewEditCalendarRule_onCheckDay_004() throws Exception {
        android.content.Intent intent16 = null;
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox154 = org.mockito.Mockito.mock(androidx.appcompat.widget.AppCompatCheckBox.class);
        org.mockito.stubbing.Stubber stubber162 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox155 = stubber162.when(appcompatcheckbox154);
        appcompatcheckbox155.setEnabled(true);
        org.mockito.stubbing.Stubber stubber163 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox156 = stubber163.when(appcompatcheckbox154);
        appcompatcheckbox156.setEnabled(true);
        org.mockito.stubbing.Stubber stubber164 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox157 = stubber164.when(appcompatcheckbox154);
        appcompatcheckbox157.setChecked(true);
        org.mockito.stubbing.Stubber stubber165 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox158 = stubber165.when(appcompatcheckbox154);
        appcompatcheckbox158.setChecked(false);
        org.mockito.stubbing.Stubber stubber166 = org.mockito.Mockito.doReturn(com.prismaqf.callblocker.R.id.cb_Monday);
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox159 = stubber166.when(appcompatcheckbox154);
        appcompatcheckbox159.getId();
        org.mockito.stubbing.Stubber stubber167 = org.mockito.Mockito.doReturn(true);
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox160 = stubber167.when(appcompatcheckbox154);
        appcompatcheckbox160.isChecked();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox161 = org.mockito.Mockito.mock(androidx.appcompat.widget.AppCompatCheckBox.class);
        org.mockito.stubbing.Stubber stubber168 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox162 = stubber168.when(appcompatcheckbox161);
        appcompatcheckbox162.setEnabled(true);
        org.mockito.stubbing.Stubber stubber169 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox163 = stubber169.when(appcompatcheckbox161);
        appcompatcheckbox163.setEnabled(true);
        org.mockito.stubbing.Stubber stubber170 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox164 = stubber170.when(appcompatcheckbox161);
        appcompatcheckbox164.setChecked(true);
        org.mockito.stubbing.Stubber stubber171 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox165 = stubber171.when(appcompatcheckbox161);
        appcompatcheckbox165.setChecked(false);
        org.mockito.stubbing.Stubber stubber172 = org.mockito.Mockito.doReturn(com.prismaqf.callblocker.R.id.cb_Tuesday);
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox166 = stubber172.when(appcompatcheckbox161);
        appcompatcheckbox166.getId();
        org.mockito.stubbing.Stubber stubber173 = org.mockito.Mockito.doReturn(true);
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox167 = stubber173.when(appcompatcheckbox161);
        appcompatcheckbox167.isChecked();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox168 = org.mockito.Mockito.mock(androidx.appcompat.widget.AppCompatCheckBox.class);
        org.mockito.stubbing.Stubber stubber174 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox169 = stubber174.when(appcompatcheckbox168);
        appcompatcheckbox169.setEnabled(true);
        org.mockito.stubbing.Stubber stubber175 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox170 = stubber175.when(appcompatcheckbox168);
        appcompatcheckbox170.setEnabled(true);
        org.mockito.stubbing.Stubber stubber176 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox171 = stubber176.when(appcompatcheckbox168);
        appcompatcheckbox171.setChecked(true);
        org.mockito.stubbing.Stubber stubber177 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox172 = stubber177.when(appcompatcheckbox168);
        appcompatcheckbox172.setChecked(false);
        org.mockito.stubbing.Stubber stubber178 = org.mockito.Mockito.doReturn(com.prismaqf.callblocker.R.id.cb_Wednesday);
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox173 = stubber178.when(appcompatcheckbox168);
        appcompatcheckbox173.getId();
        org.mockito.stubbing.Stubber stubber179 = org.mockito.Mockito.doReturn(true);
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox174 = stubber179.when(appcompatcheckbox168);
        appcompatcheckbox174.isChecked();
        androidx.appcompat.widget.AppCompatButton appcompatbutton45 = org.mockito.Mockito.mock(androidx.appcompat.widget.AppCompatButton.class);
        org.mockito.stubbing.Stubber stubber180 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatButton appcompatbutton46 = stubber180.when(appcompatbutton45);
        appcompatbutton46.setEnabled(true);
        org.mockito.stubbing.Stubber stubber181 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatButton appcompatbutton47 = stubber181.when(appcompatbutton45);
        appcompatbutton47.setEnabled(true);
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox175 = org.mockito.Mockito.mock(androidx.appcompat.widget.AppCompatCheckBox.class);
        org.mockito.stubbing.Stubber stubber182 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox176 = stubber182.when(appcompatcheckbox175);
        appcompatcheckbox176.setEnabled(true);
        org.mockito.stubbing.Stubber stubber183 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox177 = stubber183.when(appcompatcheckbox175);
        appcompatcheckbox177.setEnabled(true);
        org.mockito.stubbing.Stubber stubber184 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox178 = stubber184.when(appcompatcheckbox175);
        appcompatcheckbox178.setChecked(true);
        org.mockito.stubbing.Stubber stubber185 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox179 = stubber185.when(appcompatcheckbox175);
        appcompatcheckbox179.setChecked(false);
        org.mockito.stubbing.Stubber stubber186 = org.mockito.Mockito.doReturn(com.prismaqf.callblocker.R.id.cb_Thursday);
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox180 = stubber186.when(appcompatcheckbox175);
        appcompatcheckbox180.getId();
        org.mockito.stubbing.Stubber stubber187 = org.mockito.Mockito.doReturn(true);
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox181 = stubber187.when(appcompatcheckbox175);
        appcompatcheckbox181.isChecked();
        org.robolectric.android.controller.ActivityController activitycontroller47 = org.robolectric.Robolectric.buildActivity(com.prismaqf.callblocker.NewEditCalendarRule.class, intent16);
        com.prismaqf.callblocker.NewEditCalendarRule neweditcalendarrule21 = (com.prismaqf.callblocker.NewEditCalendarRule) activitycontroller47.get();
        org.robolectric.android.controller.ActivityController activitycontroller48 = activitycontroller47.create();
        activitycontroller48.visible();
        neweditcalendarrule21.onNoDays(appcompatbutton47);
        neweditcalendarrule21.onCheckDay(appcompatcheckbox160);
        neweditcalendarrule21.onCheckDay(appcompatcheckbox167);
        neweditcalendarrule21.onCheckDay(appcompatcheckbox174);
        neweditcalendarrule21.onCheckDay(appcompatcheckbox181);
    }
}

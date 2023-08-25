package com.prismaqf.callblocker;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test686 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.NewCalendarRuleTest#TestSingleDays/Trace-1651091736894.txt
     * Method invocation under test: <com.prismaqf.callblocker.NewEditCalendarRule: void onCheckDay(android.view.View)>_395_790
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_NewEditCalendarRule_onCheckDay_005() throws Exception {
        android.content.Intent intent18 = null;
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox259 = org.mockito.Mockito.mock(androidx.appcompat.widget.AppCompatCheckBox.class);
        org.mockito.stubbing.Stubber stubber260 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox260 = stubber260.when(appcompatcheckbox259);
        appcompatcheckbox260.setEnabled(true);
        org.mockito.stubbing.Stubber stubber261 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox261 = stubber261.when(appcompatcheckbox259);
        appcompatcheckbox261.setEnabled(true);
        org.mockito.stubbing.Stubber stubber262 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox262 = stubber262.when(appcompatcheckbox259);
        appcompatcheckbox262.setChecked(true);
        org.mockito.stubbing.Stubber stubber263 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox263 = stubber263.when(appcompatcheckbox259);
        appcompatcheckbox263.setChecked(false);
        org.mockito.stubbing.Stubber stubber264 = org.mockito.Mockito.doReturn(com.prismaqf.callblocker.R.id.cb_Monday);
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox264 = stubber264.when(appcompatcheckbox259);
        appcompatcheckbox264.getId();
        org.mockito.stubbing.Stubber stubber265 = org.mockito.Mockito.doReturn(true);
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox265 = stubber265.when(appcompatcheckbox259);
        appcompatcheckbox265.isChecked();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox266 = org.mockito.Mockito.mock(androidx.appcompat.widget.AppCompatCheckBox.class);
        org.mockito.stubbing.Stubber stubber266 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox267 = stubber266.when(appcompatcheckbox266);
        appcompatcheckbox267.setEnabled(true);
        org.mockito.stubbing.Stubber stubber267 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox268 = stubber267.when(appcompatcheckbox266);
        appcompatcheckbox268.setEnabled(true);
        org.mockito.stubbing.Stubber stubber268 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox269 = stubber268.when(appcompatcheckbox266);
        appcompatcheckbox269.setChecked(true);
        org.mockito.stubbing.Stubber stubber269 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox270 = stubber269.when(appcompatcheckbox266);
        appcompatcheckbox270.setChecked(false);
        org.mockito.stubbing.Stubber stubber270 = org.mockito.Mockito.doReturn(com.prismaqf.callblocker.R.id.cb_Tuesday);
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox271 = stubber270.when(appcompatcheckbox266);
        appcompatcheckbox271.getId();
        org.mockito.stubbing.Stubber stubber271 = org.mockito.Mockito.doReturn(true);
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox272 = stubber271.when(appcompatcheckbox266);
        appcompatcheckbox272.isChecked();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox273 = org.mockito.Mockito.mock(androidx.appcompat.widget.AppCompatCheckBox.class);
        org.mockito.stubbing.Stubber stubber272 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox274 = stubber272.when(appcompatcheckbox273);
        appcompatcheckbox274.setEnabled(true);
        org.mockito.stubbing.Stubber stubber273 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox275 = stubber273.when(appcompatcheckbox273);
        appcompatcheckbox275.setEnabled(true);
        org.mockito.stubbing.Stubber stubber274 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox276 = stubber274.when(appcompatcheckbox273);
        appcompatcheckbox276.setChecked(true);
        org.mockito.stubbing.Stubber stubber275 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox277 = stubber275.when(appcompatcheckbox273);
        appcompatcheckbox277.setChecked(false);
        org.mockito.stubbing.Stubber stubber276 = org.mockito.Mockito.doReturn(com.prismaqf.callblocker.R.id.cb_Wednesday);
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox278 = stubber276.when(appcompatcheckbox273);
        appcompatcheckbox278.getId();
        org.mockito.stubbing.Stubber stubber277 = org.mockito.Mockito.doReturn(true);
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox279 = stubber277.when(appcompatcheckbox273);
        appcompatcheckbox279.isChecked();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox280 = org.mockito.Mockito.mock(androidx.appcompat.widget.AppCompatCheckBox.class);
        org.mockito.stubbing.Stubber stubber278 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox281 = stubber278.when(appcompatcheckbox280);
        appcompatcheckbox281.setEnabled(true);
        org.mockito.stubbing.Stubber stubber279 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox282 = stubber279.when(appcompatcheckbox280);
        appcompatcheckbox282.setEnabled(true);
        org.mockito.stubbing.Stubber stubber280 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox283 = stubber280.when(appcompatcheckbox280);
        appcompatcheckbox283.setChecked(true);
        org.mockito.stubbing.Stubber stubber281 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox284 = stubber281.when(appcompatcheckbox280);
        appcompatcheckbox284.setChecked(false);
        org.mockito.stubbing.Stubber stubber282 = org.mockito.Mockito.doReturn(com.prismaqf.callblocker.R.id.cb_Friday);
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox285 = stubber282.when(appcompatcheckbox280);
        appcompatcheckbox285.getId();
        org.mockito.stubbing.Stubber stubber283 = org.mockito.Mockito.doReturn(true);
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox286 = stubber283.when(appcompatcheckbox280);
        appcompatcheckbox286.isChecked();
        androidx.appcompat.widget.AppCompatButton appcompatbutton57 = org.mockito.Mockito.mock(androidx.appcompat.widget.AppCompatButton.class);
        org.mockito.stubbing.Stubber stubber284 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatButton appcompatbutton58 = stubber284.when(appcompatbutton57);
        appcompatbutton58.setEnabled(true);
        org.mockito.stubbing.Stubber stubber285 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatButton appcompatbutton59 = stubber285.when(appcompatbutton57);
        appcompatbutton59.setEnabled(true);
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox287 = org.mockito.Mockito.mock(androidx.appcompat.widget.AppCompatCheckBox.class);
        org.mockito.stubbing.Stubber stubber286 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox288 = stubber286.when(appcompatcheckbox287);
        appcompatcheckbox288.setEnabled(true);
        org.mockito.stubbing.Stubber stubber287 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox289 = stubber287.when(appcompatcheckbox287);
        appcompatcheckbox289.setEnabled(true);
        org.mockito.stubbing.Stubber stubber288 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox290 = stubber288.when(appcompatcheckbox287);
        appcompatcheckbox290.setChecked(true);
        org.mockito.stubbing.Stubber stubber289 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox291 = stubber289.when(appcompatcheckbox287);
        appcompatcheckbox291.setChecked(false);
        org.mockito.stubbing.Stubber stubber290 = org.mockito.Mockito.doReturn(com.prismaqf.callblocker.R.id.cb_Thursday);
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox292 = stubber290.when(appcompatcheckbox287);
        appcompatcheckbox292.getId();
        org.mockito.stubbing.Stubber stubber291 = org.mockito.Mockito.doReturn(true);
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox293 = stubber291.when(appcompatcheckbox287);
        appcompatcheckbox293.isChecked();
        org.robolectric.android.controller.ActivityController activitycontroller55 = org.robolectric.Robolectric.buildActivity(com.prismaqf.callblocker.NewEditCalendarRule.class, intent18);
        com.prismaqf.callblocker.NewEditCalendarRule neweditcalendarrule25 = (com.prismaqf.callblocker.NewEditCalendarRule) activitycontroller55.get();
        org.robolectric.android.controller.ActivityController activitycontroller56 = activitycontroller55.create();
        activitycontroller56.visible();
        neweditcalendarrule25.onNoDays(appcompatbutton59);
        neweditcalendarrule25.onCheckDay(appcompatcheckbox265);
        neweditcalendarrule25.onCheckDay(appcompatcheckbox272);
        neweditcalendarrule25.onCheckDay(appcompatcheckbox279);
        neweditcalendarrule25.onCheckDay(appcompatcheckbox293);
        neweditcalendarrule25.onCheckDay(appcompatcheckbox286);
    }
}

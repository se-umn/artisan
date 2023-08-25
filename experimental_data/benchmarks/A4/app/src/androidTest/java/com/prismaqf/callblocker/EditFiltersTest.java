package com.prismaqf.callblocker;

import android.Manifest.permission;
import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.prismaqf.callblocker.filters.FilterHandle;
import com.prismaqf.callblocker.rules.CalendarRule;
import com.prismaqf.callblocker.rules.FilterRule;
import com.prismaqf.callblocker.sql.CalendarRuleProvider;
import com.prismaqf.callblocker.sql.DbHelper;
import com.prismaqf.callblocker.sql.FilterProvider;
import com.prismaqf.callblocker.sql.FilterRuleProvider;
import com.prismaqf.callblocker.utils.DebugDBFileName;
import com.prismaqf.callblocker.utils.InstrumentTestHelper;

import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isChecked;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.assertEquals;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.not;

@RunWith(AndroidJUnit4.class)
public class EditFiltersTest {

    private static final String FILTER_NAME = "dummy filter";
    private static final String FILTER_NAME_2 = "My filter with existing rules";
    private static final String CAL_RULE = "cal rule";
    private static final String PATTERNS_RULE = "patterns rule";
    private static final String ACTION_NAME = "action name";
    private static final String TEST_CAL_RULE = "My calendar rule for testing";
    private static final String TEST_PAT_RULE = "My filter rule for testing";
    private static final String NEW_RULE = "New rule";
    @ClassRule
    public static final DebugDBFileName myDebugDB = new DebugDBFileName();

    public final ActivityTestRule<CallBlockerManager> myActivityRule = new ActivityTestRule<>(CallBlockerManager.class);

    public final GrantPermissionRule permissionRule = GrantPermissionRule
          .grant(permission.READ_CONTACTS, permission.CALL_PHONE);

    @Rule
    public final RuleChain chian = RuleChain.outerRule(permissionRule).around(myActivityRule);

    @Before
    public void before() {
        FilterHandle filter = new FilterHandle(FILTER_NAME,CAL_RULE, PATTERNS_RULE, ACTION_NAME);
        SQLiteDatabase db = new DbHelper(myActivityRule.getActivity()).getWritableDatabase();
        FilterProvider.InsertRow(db, filter);
        filter = new FilterHandle(FILTER_NAME_2,TEST_CAL_RULE, TEST_PAT_RULE, ACTION_NAME);
        FilterProvider.InsertRow(db, filter);
        CalendarRule crule = new CalendarRule(TEST_CAL_RULE,CalendarRule.makeMask(9), 1,2,23,22);
        CalendarRuleProvider.InsertRow(db, crule);
        FilterRule frule = new FilterRule(TEST_PAT_RULE,"test rule");
        FilterRuleProvider.InsertRow(db,frule);
        db.close();
    }

    @After
    public void after() {
        SQLiteDatabase db = new DbHelper(myActivityRule.getActivity()).getWritableDatabase();
        FilterProvider.DeleteFilter(db, FILTER_NAME);
        FilterProvider.DeleteFilter(db, FILTER_NAME_2);
        CalendarRuleProvider.DeleteCalendarRule(db, TEST_CAL_RULE);
        CalendarRuleProvider.DeleteCalendarRule(db, NEW_RULE);
        FilterRuleProvider.DeleteFilterRule(db, NEW_RULE);
        FilterRuleProvider.DeleteFilterRule(db, TEST_PAT_RULE);
        db.close();
    }

    @Test
    public void SmokeTest() {
        Intent intent = new Intent(myActivityRule.getActivity(),EditFilters.class);
        myActivityRule.getActivity().startActivity(intent);
        //test that the filter is displayed
        onView(ViewMatchers.withText(FILTER_NAME)).check(matches(withText(FILTER_NAME)));
        //test that the add action is present
        onView(ViewMatchers.withId(R.id.action_new_item)).check(matches(isDisplayed()));
    }

    @Test
    public void SelectFilterTest() {
        Intent intent = new Intent(myActivityRule.getActivity(),EditFilters.class);
        myActivityRule.getActivity().startActivity(intent);
        //test that the filter is displayed
        onView(ViewMatchers.withText(FILTER_NAME)).check(matches(withText(FILTER_NAME)));
        //now select it
        onView(ViewMatchers.withText(FILTER_NAME)).perform(click());
        //check the current activity
        Activity activity = InstrumentTestHelper.getCurrentActivity();
        assertEquals("Check the current running activity",NewEditFilter.class.getCanonicalName(),activity.getClass().getCanonicalName());
        onView(ViewMatchers.withId(R.id.text_calendar_name)).check(matches(withText(containsString(CAL_RULE))));
        onView(ViewMatchers.withId(R.id.text_filter_rule_name)).check(matches(withText(containsString(PATTERNS_RULE))));
        onView(ViewMatchers.withId(R.id.text_action_name)).check(matches(withText(containsString(ACTION_NAME))));
    }

    @Test
    public void NewFilterTest() {
        Intent intent = new Intent(myActivityRule.getActivity(),EditFilters.class);
        myActivityRule.getActivity().startActivity(intent);
        onView(ViewMatchers.withId(R.id.action_new_item)).perform(click());
        //check that we land on a NewEditFilter activity and that we are in action create
        Activity activity = InstrumentTestHelper.getCurrentActivity();
        assertEquals("Check the current running activity", NewEditFilter.class.getCanonicalName(), activity.getClass().getCanonicalName());
        onView(ViewMatchers.withId(R.id.action_save)).check(matches(isDisplayed()));
        //now change the name to an existing one
        onView(ViewMatchers.withId(R.id.edit_filter_name)).perform(replaceText(FILTER_NAME));
        //the save action should disapper
        onView(ViewMatchers.withId(R.id.action_save)).check(doesNotExist());
        //and the validation text should tell
        onView(ViewMatchers.withId(R.id.tx_filter_rule_validation)).check(matches(withText(containsString("name already used"))));
        //now set the name to empty
        onView(ViewMatchers.withId(R.id.edit_filter_name)).perform(replaceText(""));
        //the save action should disapper
        onView(ViewMatchers.withId(R.id.action_save)).check(doesNotExist());
        //and the validation text should tell
        onView(ViewMatchers.withId(R.id.tx_filter_rule_validation)).check(matches(withText(containsString("can not be empty"))));
        //now reset to a valid name
        onView(ViewMatchers.withId(R.id.edit_filter_name)).perform(replaceText("a1b2c3"));
        onView(ViewMatchers.withId(R.id.action_save)).check(matches(isDisplayed()));
    }

    @Test
    public void ChangeActionTest() {
        Intent intent = new Intent(myActivityRule.getActivity(),EditFilters.class);
        myActivityRule.getActivity().startActivity(intent);
        //test that the filter is displayed
        onView(ViewMatchers.withText(FILTER_NAME)).check(matches(withText(FILTER_NAME)));
        //now select it
        onView(ViewMatchers.withText(FILTER_NAME)).perform(click());
        //check that the change button is active
        onView(ViewMatchers.withId(R.id.action_change)).check(matches(isDisplayed()));
        onView(ViewMatchers.withId(R.id.text_calendar_name)).check(matches(not(isEnabled())));
        //now click it
        onView(ViewMatchers.withId(R.id.action_change)).perform(click());
        onView(ViewMatchers.withId(R.id.text_calendar_name)).check(matches(isEnabled()));
    }

    @Test
    public void DeleteActionTest() {
        Intent intent = new Intent(myActivityRule.getActivity(),EditFilters.class);
        myActivityRule.getActivity().startActivity(intent);
        //test that the filter is displayed
        onView(ViewMatchers.withText(FILTER_NAME)).check(matches(withText(FILTER_NAME)));
        //now select it
        onView(ViewMatchers.withText(FILTER_NAME)).perform(click());
        //check that the change button is active
        onView(ViewMatchers.withId(R.id.action_delete)).check(matches(isDisplayed()));
        //click the delete rule
        onView(ViewMatchers.withId(R.id.action_delete)).perform(click());
        //a dialog confirmation should appear
        onView(ViewMatchers.withText(myActivityRule.getActivity().getString(R.string.tx_filter_delete_confirm)))
                .check(matches(isDisplayed()));
    }

    @Test
    public void UndoActionTest() {
        Intent intent = new Intent(myActivityRule.getActivity(),EditFilters.class);
        myActivityRule.getActivity().startActivity(intent);
        //test that the filter is displayed
        onView(ViewMatchers.withText(FILTER_NAME)).check(matches(withText(FILTER_NAME)));
        //now select it
        onView(ViewMatchers.withText(FILTER_NAME)).perform(click());
        //check that the change button is active
        onView(ViewMatchers.withId(R.id.action_undo)).check(doesNotExist());
        onView(ViewMatchers.withId(R.id.action_change)).perform(click());
        //now pick a new calendar rule
        Activity activity = InstrumentTestHelper.getCurrentActivity();
        openActionBarOverflowOrOptionsMenu(activity);
        onView(withText("Pick a calendar rule")).perform(click());
        //now select a new calendar rule
        onView(withText(TEST_CAL_RULE)).perform(click());
        activity = InstrumentTestHelper.getCurrentActivity();
        assertEquals("Back in EditFilters activity", NewEditFilter.class.getCanonicalName(), activity.getClass().getCanonicalName());
        try {
            onView(ViewMatchers.withId(R.id.action_undo)).perform(click());
        }
        catch (NoMatchingViewException e) {
            openActionBarOverflowOrOptionsMenu(activity);
            onView(withText("Undo a change")).perform(click());
        }
        onView(ViewMatchers.withId(R.id.action_undo)).check(doesNotExist());
        onView(ViewMatchers.withId(R.id.action_change)).check(matches(isDisplayed()));
    }

    @Test
    public void CreateAndSelectNewCalendarRule() {
        Intent intent = new Intent(myActivityRule.getActivity(),EditFilters.class);
        myActivityRule.getActivity().startActivity(intent);
        //test that the filter is displayed
        onView(ViewMatchers.withText(FILTER_NAME)).check(matches(withText(FILTER_NAME)));
        //now select it
        onView(ViewMatchers.withText(FILTER_NAME)).perform(click());
        //check that the change button is active
        onView(ViewMatchers.withId(R.id.action_change)).perform(click());
        //now pick a new calendar rule
        Activity activity = InstrumentTestHelper.getCurrentActivity();
        openActionBarOverflowOrOptionsMenu(activity);
        onView(withText("Pick a calendar rule")).perform(click());
        activity = InstrumentTestHelper.getCurrentActivity();
        assertEquals("EditCalendarRules activity", EditCalendarRules.class.getCanonicalName(), activity.getClass().getCanonicalName());
        onView(ViewMatchers.withId(R.id.action_new_item)).perform(click());
        activity = InstrumentTestHelper.getCurrentActivity();
        assertEquals("NewEditCalendarRule activity", NewEditCalendarRule.class.getCanonicalName(), activity.getClass().getCanonicalName());
        //now change the name and save
        onView(ViewMatchers.withId(R.id.edit_calendar_rule_name)).perform(replaceText(NEW_RULE));
        onView(ViewMatchers.withId(R.id.action_save)).perform(click());
        activity = InstrumentTestHelper.getCurrentActivity();
        assertEquals("Back on NewEditFilter activity", NewEditFilter.class.getCanonicalName(), activity.getClass().getCanonicalName());
        onView(ViewMatchers.withId(R.id.text_calendar_name)).check(matches(withText(containsString(NEW_RULE))));
    }

    @Test
    public void CreateAndSelectNewFilterRule() {
        Intent intent = new Intent(myActivityRule.getActivity(), EditFilters.class);
        myActivityRule.getActivity().startActivity(intent);
        //test that the filter is displayed
        onView(ViewMatchers.withText(FILTER_NAME)).check(matches(withText(FILTER_NAME)));
        //now select it
        onView(ViewMatchers.withText(FILTER_NAME)).perform(click());
        //check that the change button is active
        onView(ViewMatchers.withId(R.id.action_change)).perform(click());
        //now pick a new filter rule
        Activity activity = InstrumentTestHelper.getCurrentActivity();
        openActionBarOverflowOrOptionsMenu(activity);
        onView(withText("Pick a filter rule")).perform(click());
        activity = InstrumentTestHelper.getCurrentActivity();
        assertEquals("EditFilterRules activity", EditFilterRules.class.getCanonicalName(), activity.getClass().getCanonicalName());
        onView(ViewMatchers.withId(R.id.action_new_item)).perform(click());
        activity = InstrumentTestHelper.getCurrentActivity();
        assertEquals("NewEditFilterRule activity", NewEditFilterRule.class.getCanonicalName(), activity.getClass().getCanonicalName());
        //now change the name and save
        onView(ViewMatchers.withId(R.id.edit_filter_rule_name)).perform(replaceText(NEW_RULE));
        onView(ViewMatchers.withId(R.id.action_save)).perform(click());
        activity = InstrumentTestHelper.getCurrentActivity();
        assertEquals("Back on NewEditFilter activity", NewEditFilter.class.getCanonicalName(), activity.getClass().getCanonicalName());
        onView(ViewMatchers.withId(R.id.text_filter_rule_name)).check(matches(withText(containsString(NEW_RULE))));
    }

    @Test
    public void NavigateUpIsContextSensitive() {
        Intent intent = new Intent(myActivityRule.getActivity(),EditFilters.class);
        myActivityRule.getActivity().startActivity(intent);
        //test that the filter is displayed
        onView(ViewMatchers.withText(FILTER_NAME)).check(matches(withText(FILTER_NAME)));
        //now select it
        onView(ViewMatchers.withText(FILTER_NAME)).perform(click());
        //check that the change button is active
        onView(ViewMatchers.withId(R.id.action_change)).perform(click());
        //now pick a new calendar rule
        Activity activity = InstrumentTestHelper.getCurrentActivity();
        openActionBarOverflowOrOptionsMenu(activity);
        onView(withText("Pick a calendar rule")).perform(click());
        activity = InstrumentTestHelper.getCurrentActivity();
        assertEquals("EditCalendarRules activity", EditCalendarRules.class.getCanonicalName(), activity.getClass().getCanonicalName());
        onView(ViewMatchers.withContentDescription(containsString("Navigate up"))).perform(click());
        activity = InstrumentTestHelper.getCurrentActivity();
        assertEquals("Back on NewEditFilter activity", NewEditFilter.class.getCanonicalName(), activity.getClass().getCanonicalName());
    }

    @Test
    public void PickActionTest() {
        Intent intent = new Intent(myActivityRule.getActivity(),EditFilters.class);
        myActivityRule.getActivity().startActivity(intent);
        //test that the filter is displayed
        onView(ViewMatchers.withText(FILTER_NAME)).check(matches(withText(FILTER_NAME)));
        //now select it
        onView(ViewMatchers.withText(FILTER_NAME)).perform(click());
        //check that the change button is active
        onView(ViewMatchers.withId(R.id.action_change)).perform(click());
        //now pick a new filter rule
        Activity activity = InstrumentTestHelper.getCurrentActivity();
        openActionBarOverflowOrOptionsMenu(activity);
        onView(withText("Pick an action")).perform(click());
        activity = InstrumentTestHelper.getCurrentActivity();
        assertEquals("PickAction activity", PickAction.class.getCanonicalName(), activity.getClass().getCanonicalName());
        onView(ViewMatchers.withText("DropCallByEndCall")).perform(click());
        //back to the NewEditFilter action
        activity = InstrumentTestHelper.getCurrentActivity();
        assertEquals("NewEditFilter activity", NewEditFilter.class.getCanonicalName(), activity.getClass().getCanonicalName());
        onView(ViewMatchers.withText(containsString("DropCallByEndCall"))).check(matches(isDisplayed()));

    }

    @Test
    public void EditTheCalendarRule() {
        Intent intent = new Intent(myActivityRule.getActivity(),EditFilters.class);
        myActivityRule.getActivity().startActivity(intent);
        //now select it
        onView(ViewMatchers.withText(FILTER_NAME_2)).perform(click());
        //check that the change button is active
        onView(ViewMatchers.withId(R.id.action_change)).perform(click());
        Activity activity = InstrumentTestHelper.getCurrentActivity();
        openActionBarOverflowOrOptionsMenu(activity);
        onView(withText("Edit the calendar rule")).perform(click());
        activity = InstrumentTestHelper.getCurrentActivity();
        assertEquals("NewEditCalendarRule activity", NewEditCalendarRule.class.getCanonicalName(), activity.getClass().getCanonicalName());
        onView(ViewMatchers.withId(R.id.action_change)).perform(click());
        onView(ViewMatchers.withId(R.id.cb_Sunday)).check(matches(not(isChecked())));
        onView(ViewMatchers.withId(R.id.cb_Sunday)).perform(click());
        onView(ViewMatchers.withId(R.id.action_save)).perform(click());
        activity = InstrumentTestHelper.getCurrentActivity();
        assertEquals("NewEditFilter activity", NewEditFilter.class.getCanonicalName(), activity.getClass().getCanonicalName());
        openActionBarOverflowOrOptionsMenu(activity);
        onView(withText("Edit the calendar rule")).perform(click());
        onView(ViewMatchers.withId(R.id.action_change)).perform(click());
        onView(ViewMatchers.withId(R.id.cb_Sunday)).check(matches(isChecked()));
    }

    @Test
    public void EditTheFilterRule() {
        Intent intent = new Intent(myActivityRule.getActivity(),EditFilters.class);
        myActivityRule.getActivity().startActivity(intent);
        //now select it
        onView(ViewMatchers.withText(FILTER_NAME_2)).perform(click());
        //check that the change button is active
        onView(ViewMatchers.withId(R.id.action_change)).perform(click());
        Activity activity = InstrumentTestHelper.getCurrentActivity();
        openActionBarOverflowOrOptionsMenu(activity);
        onView(withText("Edit the filter rule")).perform(click());
        activity = InstrumentTestHelper.getCurrentActivity();
        assertEquals("NewEditFilterRule activity", NewEditFilterRule.class.getCanonicalName(), activity.getClass().getCanonicalName());
        onView(ViewMatchers.withId(R.id.action_change)).perform(click());
        onView(ViewMatchers.withId(R.id.edit_filter_rule_description)).perform(replaceText("bibbo"));
        onView(ViewMatchers.withId(R.id.action_save)).perform(click());
        activity = InstrumentTestHelper.getCurrentActivity();
        assertEquals("NewEditFilter activity", NewEditFilter.class.getCanonicalName(), activity.getClass().getCanonicalName());
        openActionBarOverflowOrOptionsMenu(activity);
        onView(withText("Edit the filter rule")).perform(click());
        onView(ViewMatchers.withId(R.id.action_change)).perform(click());
        onView(ViewMatchers.withId(R.id.edit_filter_rule_description)).check(matches(withText("bibbo")));
    }
}

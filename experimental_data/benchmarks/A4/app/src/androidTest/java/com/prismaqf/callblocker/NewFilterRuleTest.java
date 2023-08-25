package com.prismaqf.callblocker;

import android.Manifest.permission;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import androidx.test.espresso.action.ReplaceTextAction;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import android.widget.EditText;

import com.prismaqf.callblocker.sql.DbContract;
import com.prismaqf.callblocker.sql.DbHelper;
import com.prismaqf.callblocker.utils.DebugDBFileName;
import com.prismaqf.callblocker.utils.InstrumentTestHelper;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Collections;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.containsString;

@RunWith(AndroidJUnit4.class)
public class NewFilterRuleTest {

    private Intent intent;
    private Context ctx;

    @ClassRule
    public static final DebugDBFileName myDebugDB = new DebugDBFileName();

    public final ActivityTestRule<EditCalendarRules> myActivityRule = new ActivityTestRule<>(EditCalendarRules.class);

    public final GrantPermissionRule permissionRule = GrantPermissionRule
          .grant(permission.READ_CONTACTS, permission.CALL_PHONE);

    @Rule
    public final RuleChain chian = RuleChain.outerRule(permissionRule).around(myActivityRule);

    @Before
    public void before() {
        ctx = myActivityRule.getActivity();
        SQLiteDatabase db = new DbHelper(myActivityRule.getActivity()).getWritableDatabase();
        db.delete(DbContract.FilterRules.TABLE_NAME, null, null);
        db.delete(DbContract.FilterPatterns.TABLE_NAME, null, null);
        intent = new Intent(ctx,NewEditFilterRule.class);
        intent.putExtra(NewEditActivity.KEY_ACTION, NewEditActivity.ACTION_CREATE);
    }

    @Test
    public void TestActionOnCreating() {
        intent.putStringArrayListExtra(NewEditActivity.KEY_RULENAMES, new ArrayList<String>());
        ctx.startActivity(intent);
        onView(ViewMatchers.withId(R.id.edit_filter_rule_name)).check(matches(isEnabled()));
        onView(ViewMatchers.withId(R.id.edit_filter_rule_description)).check(matches(isEnabled()));
        onView(ViewMatchers.withId(R.id.bt_filter_rule_patterns)).check(matches(isEnabled()));
        onView(ViewMatchers.withId(R.id.tx_filter_rule_validation)).check(matches(isEnabled()));
        onView(ViewMatchers.withId(R.id.tx_rule_description)).check(matches(isEnabled()));
    }

    @Test
    public void TestEmptyNameShouldFlag() {
        intent.putStringArrayListExtra(NewEditActivity.KEY_RULENAMES, new ArrayList<String>());
        ctx.startActivity(intent);
        onView(ViewMatchers.withId(R.id.edit_filter_rule_name)).perform(new ReplaceTextAction(""));
        onView(ViewMatchers.withId(R.id.tx_filter_rule_validation)).check(matches(withText(containsString("can not be empty"))));
    }

    @Test
    public void TestUsedNameShouldFlag() {
        intent.putStringArrayListExtra(NewEditActivity.KEY_RULENAMES,
                new ArrayList<>(Collections.singletonList("first")));
        ctx.startActivity(intent);
        onView(ViewMatchers.withId(R.id.edit_filter_rule_name)).perform(new ReplaceTextAction("first"));
        onView(ViewMatchers.withId(R.id.tx_filter_rule_validation)).check(matches(withText(containsString("name already used"))));
    }

    @Test
    public void TestSaveAction() {
        intent.putStringArrayListExtra(NewEditActivity.KEY_RULENAMES, new ArrayList<String>());
        ctx.startActivity(intent);
        onView(ViewMatchers.withId(R.id.action_save)).check(matches(isDisplayed()));
        //now empty the rule name, the action disappears
        onView(ViewMatchers.withId(R.id.edit_filter_rule_name)).perform(new ReplaceTextAction(""));
        onView(ViewMatchers.withId(R.id.action_save)).check(doesNotExist());
        //reinsert a valid name, the action is back
        onView(ViewMatchers.withId(R.id.edit_filter_rule_name)).perform(new ReplaceTextAction("a"));
        onView(ViewMatchers.withId(R.id.action_save)).check(matches(isDisplayed()));
    }

    @Test
    public void TestDeleteActionMissingOnCreate(){
        intent.putStringArrayListExtra(NewEditActivity.KEY_RULENAMES, new ArrayList<String>());
        ctx.startActivity(intent);
        onView(ViewMatchers.withId(R.id.action_delete)).check(doesNotExist());
    }

    @Test
    public void TestChangeActionMissingOnCreate(){
        intent.putStringArrayListExtra(NewEditActivity.KEY_RULENAMES, new ArrayList<String>());
        ctx.startActivity(intent);
        onView(ViewMatchers.withId(R.id.action_change)).check(doesNotExist());
    }

    @Test
    public void TestButtonFilterPatterns() {
        intent.putStringArrayListExtra(NewEditActivity.KEY_RULENAMES, new ArrayList<String>());
        ctx.startActivity(intent);
        onView(ViewMatchers.withId(R.id.bt_filter_rule_patterns)).check(matches(isEnabled()));
        onView(ViewMatchers.withId(R.id.bt_filter_rule_patterns)).perform(click());
        onView(ViewMatchers.withId(R.id.action_help_patterns)).check(matches(isDisplayed()));
    }

    @Test
    @Ignore
    public void TestHelpAction() {
        intent.putStringArrayListExtra(NewEditActivity.KEY_RULENAMES, new ArrayList<String>());
        ctx.startActivity(intent);
        onView(ViewMatchers.withId(R.id.action_help)).check(matches(isDisplayed()));
        onView(ViewMatchers.withId(R.id.action_help)).perform(click());
        onView(ViewMatchers.withText("Help on editing filter rules")).check(matches(isDisplayed()));
    }

    @Test
    public void TestDynamicStateOnRotation() throws Throwable {
        intent.putStringArrayListExtra(NewEditActivity.KEY_RULENAMES, new ArrayList<String>());
        ctx.startActivity(intent);
        onView(ViewMatchers.withId(R.id.edit_filter_rule_name)).perform(new ReplaceTextAction("Name"));
        onView(ViewMatchers.withId(R.id.edit_filter_rule_description)).perform(new ReplaceTextAction("Description"));
        onView(ViewMatchers.withId(R.id.bt_filter_rule_patterns)).perform(click());
        Activity activity = InstrumentTestHelper.getCurrentActivity();
        openActionBarOverflowOrOptionsMenu(activity);
        onView(withText("Add a pattern")).perform(click());
        onView(withClassName(equalTo(EditText.class.getCanonicalName()))).perform(typeText("123*456"));
        onView(withText("OK")).perform(click());
        onView(withText("123*456")).check(matches(isDisplayed()));
        onView(withId(R.id.action_update_patterns)).perform(click());
        onView(withId(R.id.tx_rule_description)).check(matches(withText(containsString("123*456"))));
        activity = InstrumentTestHelper.getCurrentActivity();
        InstrumentTestHelper.rotateScreen(activity);
        onView(ViewMatchers.withId(R.id.edit_filter_rule_name)).check(matches(withText("Name")));
        onView(ViewMatchers.withId(R.id.edit_filter_rule_description)).check(matches(withText("Description")));
        onView(withId(R.id.tx_rule_description)).check(matches(withText(containsString("123*456"))));
        InstrumentTestHelper.rotateScreen(activity);
        onView(ViewMatchers.withId(R.id.edit_filter_rule_name)).check(matches(withText("Name")));
        onView(ViewMatchers.withId(R.id.edit_filter_rule_description)).check(matches(withText("Description")));
        onView(withId(R.id.tx_rule_description)).check(matches(withText(containsString("123*456"))));
    }

    @Test
    public void TestRotationWithEmptyText() throws Throwable {
        intent.putStringArrayListExtra(NewEditActivity.KEY_RULENAMES, new ArrayList<String>());
        ctx.startActivity(intent);
        Activity activity = InstrumentTestHelper.getCurrentActivity();
        onView(ViewMatchers.withId(R.id.edit_filter_rule_name)).perform(new ReplaceTextAction(""));
        InstrumentTestHelper.rotateScreen(activity);
        onView(ViewMatchers.withId(R.id.edit_filter_rule_name)).check(matches(withText("")));
    }

    @Test
    public void AfterUpdatingTheRulePatternsTheRuleShouldSaveWhenActivityIsLeft() {
        intent.putStringArrayListExtra(NewEditActivity.KEY_RULENAMES, new ArrayList<String>());
        ctx.startActivity(intent);
        onView(ViewMatchers.withId(R.id.edit_filter_rule_name)).perform(new ReplaceTextAction("my dummy rule"));
        onView(ViewMatchers.withId(R.id.bt_filter_rule_patterns)).perform(click());
        openActionBarOverflowOrOptionsMenu(ctx);
        //add a patterns
        onView(withText("Add a pattern")).perform(click());
        onView(withClassName(equalTo(EditText.class.getCanonicalName()))).perform(typeText("123"));
        onView(withText("OK")).perform(click());
        onView(withText("123")).check(matches(isDisplayed()));
        onView(withId(R.id.action_update_patterns)).perform(click());
        //back in the main activity
        onView(withId(R.id.tx_rule_description)).check(matches(withText(containsString("123"))));
        //now leave the activity without saving
        pressBack();
        //now check the rule is in the list
        onView(ViewMatchers.withId(R.id.text_rule_name)).check(matches(withText("my dummy rule")));
        //now go back and check the pattren was saved
        onView(ViewMatchers.withId(R.id.text_rule_name)).perform(click());
        onView(withId(R.id.tx_rule_description)).check(matches(withText(containsString("123"))));
    }
}

package abc.basiccalculator;

import android.content.Context;

import androidx.test.espresso.Espresso;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;


@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void testCalculate() {
        onView(withId(R.id.input)).perform(typeText("1+1"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.calculateButton)).perform(click());
        onView(withId(R.id.resultView)).check(matches(withText("2.0")));
    }

    @Test
    public void testCalculateAndIncrementByOne() {
        onView(withId(R.id.input)).perform(typeText("1+1"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.calculateButton)).perform(click());
        onView(withId(R.id.resultView)).check(matches(withText("2.0")));
        onView(withId(R.id.incrementButtonByOne)).perform(click());
        onView(withId(R.id.resultView)).check(matches(withText("3.0")));
    }

    @Test
    public void testCalculateAndIncrementByTwo() {
        onView(withId(R.id.input)).perform(typeText("1+1"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.calculateButton)).perform(click());
        onView(withId(R.id.resultView)).check(matches(withText("2.0")));
        onView(withId(R.id.incrementButtonByTwo)).perform(click());
        onView(withId(R.id.resultView)).check(matches(withText("4.0")));
    }

    @Test
    public void testCalculateAndIncrementByOneWithLogging() {
        onView(withId(R.id.input)).perform(typeText("3+1"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.calculateButton)).perform(click());
        onView(withId(R.id.resultView)).check(matches(withText("4.0")));
        onView(withId(R.id.incrementButtonByOne)).perform(click());
        onView(withId(R.id.resultView)).check(matches(withText("5.0")));
    }

    @Test
    public void testCalculateAnExceptionAcrossMethods() {
        onView(withId(R.id.input)).perform(typeText("22+1"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.calculateButton)).perform(click());
        onView(withId(R.id.resultView)).check(matches(withText("23.0")));
    }

    @Test
    public void testCalculateAnExceptionWithinMethod() {
        onView(withId(R.id.input)).perform(typeText("2+2"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.calculateButton)).perform(click());
        onView(withId(R.id.resultView)).check(matches(withText("4.0")));
        onView(withId(R.id.incrementButtonByTwo)).perform(click());
        onView(withId(R.id.resultView)).check(matches(withText("6.0")));
        onView(withId(R.id.incrementButtonByTwo)).perform(click());
        onView(withId(R.id.resultView)).check(matches(withText("8.0")));
    }
}
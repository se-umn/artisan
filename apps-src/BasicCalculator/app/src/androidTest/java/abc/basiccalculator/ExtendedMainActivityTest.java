package abc.basiccalculator;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.espresso.Espresso;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ExtendedMainActivityTest {

    @Rule
    public MonitorRule<ExtendedMainActivity> monitorRule = new MonitorRule<>(ExtendedMainActivity.class);

    @Test
    public void testCalculateAndReturnBackToMain() {
        onView(withId(R.id.input)).perform(typeText("4+4"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.calculateButton)).perform(click());
        onView(withId(R.id.resultView)).check(matches(withText("8")));
        onView(withText(R.string.back_text)).perform(click());
    }

    @Test
    public void testCalculateNullPointerThrownByResultActivity() {
        onView(withId(R.id.input)).perform(typeText("4+4"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.calculateButton)).perform(click());
        onView(withId(R.id.resultView)).check(matches(withText("8")));
        onView(withText(R.string.crash_text)).perform(click());
    }

    @Test
    public void testCalculateWithValidComment() {
        onView(withId(R.id.input)).perform(typeText("4+4"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.comment)).perform(typeText("comm"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.calculateButton)).perform(click());
        onView(withId(R.id.resultView)).check(matches(withText("8")));
        onView(withId(R.id.commentView)).check(matches(withText("Comment: comm")));
    }

    @Test
    public void testCalculateWithCommentThrowingIllegalArgumentException() {
        onView(withId(R.id.input)).perform(typeText("4+4"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.comment)).perform(typeText(ExtendedMainActivity.FAILING_COMMENT));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.calculateButton)).perform(click());
    }

}

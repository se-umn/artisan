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

    /**
     * The UI should be changed back to the {@link ExtendedMainActivity} by clicking the `Back`
     * button (that was created programmatically in the {@link ExtendedMainActivity}) within the
     * {@link ExtendedResultActivity}.
     */
    @Test
    public void testCalculateAndReturnBackToMain() {
        onView(withId(R.id.input)).perform(typeText("4+4"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.calculateButton)).perform(click());
        onView(withId(R.id.resultView)).check(matches(withText("8")));
        onView(withText(R.string.back_text)).perform(click());
    }

    /**
     * A NPE should be thrown from the {@link ExtendedResultActivity} by clicking the `Crash`
     * button that was created programmatically within the {@link ExtendedMainActivity}.
     */
    @Test
    public void testCalculateNullPointerThrownByResultActivity() {
        try {
            onView(withId(R.id.input)).perform(typeText("4+4"));
            Espresso.closeSoftKeyboard();
            onView(withId(R.id.calculateButton)).perform(click());
            onView(withId(R.id.resultView)).check(matches(withText("8")));
            onView(withText(R.string.crash_text)).perform(click());
        } catch (Exception e) {
        }
    }

    /**
     * A comment that has been entered in the {@link ExtendedMainActivity} should be visible
     * in the {@link ExtendedResultActivity}.
     */
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

    /**
     * An IllegalArgumentException should be thrown from the {@link ExtendedMainActivity} when
     * a specific comment is entered into the comment field.
     */
    @Test
    public void testCalculateWithCommentThrowingIllegalArgumentException() {
        try {
            onView(withId(R.id.input)).perform(typeText("4+4"));
            Espresso.closeSoftKeyboard();
            onView(withId(R.id.comment)).perform(typeText(ExtendedMainActivity.FAILING_COMMENT));
            Espresso.closeSoftKeyboard();
            onView(withId(R.id.calculateButton)).perform(click());
        } catch (Exception e) {
        }
    }

}

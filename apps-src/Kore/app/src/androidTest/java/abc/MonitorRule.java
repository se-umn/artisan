package abc;

import android.app.Activity;
import android.content.Intent;

import androidx.test.rule.ActivityTestRule;
import java.lang.reflect.Method;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public class MonitorRule<T extends Activity> implements TestRule {

  private ActivityTestRule<T> activityTestRule;

  public MonitorRule(Class<T> clazz) {
    this.activityTestRule = new ActivityTestRule<>(clazz, true, false);
  }

  @Override
  public Statement apply(final Statement base, final Description description) {
    return new Statement() {
      @Override
      public void evaluate() throws Throwable {
        String testName = description.getMethodName();
        invokeStartTestMethod(testName);
        activityTestRule.launchActivity(null);
        try {
          base.evaluate();
        } finally {
          activityTestRule.finishActivity();
        }
      }
    };
  }

  private void invokeStartTestMethod(String testName) {
    try {
      final String className = "de.unipassau.abc.instrumentation.Monitor";
      final Class<?> monitorClass = Class.forName(className);
      final Method startTestMethod = monitorClass.getMethod("testStart", String.class);
      startTestMethod.invoke(null, testName);
    } catch (Exception e) {
      // Do nothing here.
    }
  }

  public ActivityTestRule<T> getActivityTestRule() {
    return this.activityTestRule;
  }

  public T getActivity() {
    return this.activityTestRule.getActivity();
  }

  public void finishActivity() {
    this.activityTestRule.finishActivity();
  }

  public void launchActivity(Intent intent) {
    this.activityTestRule.launchActivity(intent);
  }
}

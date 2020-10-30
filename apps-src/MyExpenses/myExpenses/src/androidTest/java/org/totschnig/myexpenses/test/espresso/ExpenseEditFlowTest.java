package org.totschnig.myexpenses.test.espresso;

import android.content.OperationApplicationException;
import android.os.RemoteException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.totschnig.myexpenses.R;
import org.totschnig.myexpenses.activity.ExpenseEdit;
import org.totschnig.myexpenses.model.Account;
import org.totschnig.myexpenses.model.AccountType;
import org.totschnig.myexpenses.model.CurrencyUnit;
import org.totschnig.myexpenses.model.PaymentMethod;

import java.util.Currency;

import androidx.test.espresso.Espresso;
import androidx.test.filters.FlakyTest;
import androidx.test.rule.ActivityTestRule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isNotChecked;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.TestCase.assertTrue;
import static org.totschnig.myexpenses.testutils.Espresso.withIdAndParent;

public class ExpenseEditFlowTest {

  @Rule
  public ActivityTestRule<ExpenseEdit> mActivityRule = new ActivityTestRule<>(ExpenseEdit.class);
  private static String accountLabel1 = "Test label 1";
  private static Account account1;
  private static CurrencyUnit currency1 = CurrencyUnit.create(Currency.getInstance("USD"));
  private static PaymentMethod paymentMethod;

  @BeforeClass
  public static void fixture() {
    account1 = new Account(accountLabel1, currency1, 0, "", AccountType.CASH, Account.DEFAULT_COLOR);
    assertNotNull(account1.save());
    paymentMethod = new PaymentMethod("TEST");
    paymentMethod.setPaymentType(PaymentMethod.EXPENSE);
    paymentMethod.addAccountType(AccountType.CASH);
    assertNotNull(paymentMethod.save());
  }

  @AfterClass
  public static void tearDown() throws RemoteException, OperationApplicationException {
    Account.delete(account1.getId());
    PaymentMethod.delete(paymentMethod.getId());
  }

  /**
   * If user toggles from expense (where we have at least one payment method) to income (where there is none)
   * and then selects category, or opens calculator, and comes back, saving failed. We test here
   * the fix for this bug.
   */
  @Test
  @FlakyTest
  public void testScenarioForBug5b11072e6007d59fcd92c40b() {
    onView(withIdAndParent(R.id.AmountEditText, R.id.Amount)).perform(typeText(String.valueOf(10)));
    onView(withIdAndParent(R.id.TaType, R.id.Amount)).perform(click());
    onView(withId(R.id.Category)).perform(click());
    Espresso.pressBack();
    onView(withId(R.id.SAVE_COMMAND)).perform(click());
    assertTrue(mActivityRule.getActivity().isFinishing());
  }

  @Test
  public void calculatorMaintainsType() {
    onView(withIdAndParent(R.id.AmountEditText, R.id.Amount)).perform(typeText("123"));
    onView(withIdAndParent(R.id.Calculator, R.id.Amount)).perform(click());
    onView(withId(R.id.bOK)).perform(click());
    onView(withIdAndParent(R.id.TaType, R.id.Amount)).check(matches(isNotChecked()));
  }
}

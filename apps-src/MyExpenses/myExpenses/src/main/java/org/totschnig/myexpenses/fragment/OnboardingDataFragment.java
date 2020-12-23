package org.totschnig.myexpenses.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;

import org.totschnig.myexpenses.MyApplication;
import org.totschnig.myexpenses.R;
import org.totschnig.myexpenses.activity.BackupRestoreActivity;
import org.totschnig.myexpenses.activity.OnboardingActivity;
import org.totschnig.myexpenses.activity.SyncBackendSetupActivity;
import org.totschnig.myexpenses.adapter.CurrencyAdapter;
import org.totschnig.myexpenses.dialog.DialogUtils;
import org.totschnig.myexpenses.model.Account;
import org.totschnig.myexpenses.model.AccountType;
import org.totschnig.myexpenses.model.CurrencyContext;
import org.totschnig.myexpenses.model.CurrencyUnit;
import org.totschnig.myexpenses.model.Money;
import org.totschnig.myexpenses.preference.PrefHandler;
import org.totschnig.myexpenses.preference.PrefKey;
import org.totschnig.myexpenses.sync.GenericAccountService;
import org.totschnig.myexpenses.ui.AmountInput;
import org.totschnig.myexpenses.util.UiUtils;
import org.totschnig.myexpenses.viewmodel.CurrencyViewModel;
import org.totschnig.myexpenses.viewmodel.data.Currency;

import java.math.BigDecimal;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import eltos.simpledialogfragment.SimpleDialog;
import eltos.simpledialogfragment.color.SimpleColorDialog;
import icepick.Icepick;
import icepick.State;

import static org.totschnig.myexpenses.activity.ConstantsKt.RESTORE_REQUEST;
import static org.totschnig.myexpenses.activity.ProtectedFragmentActivity.EDIT_COLOR_DIALOG;
import static org.totschnig.myexpenses.provider.DatabaseConstants.KEY_CURRENCY;


public class OnboardingDataFragment extends OnboardingFragment implements AdapterView.OnItemSelectedListener,
    SimpleDialog.OnDialogResultListener {

  private static final String KEY_LABEL_UNCHANGED_OR_EMPTY = "label_unchanged_or_empty";
  @BindView(R.id.MoreOptionsContainer)
  View moreOptionsContainer;
  @BindView(R.id.MoreOptionsButton)
  View moreOptionsButton;
  @BindView(R.id.Label)
  EditText labelEditText;
  @BindView(R.id.Description)
  EditText descriptionEditText;
  @BindView(R.id.Amount)
  AmountInput amountInput;
  @BindView(R.id.ColorIndicator)
  AppCompatButton colorIndicator;
  @BindView(R.id.Currency)
  Spinner currencySpinner;
  @BindView(R.id.AccountType)
  Spinner accountTypeSpinner;

  @State
  boolean moreOptionsShown = false;
  @State
  int accountColor = Account.DEFAULT_COLOR;

  @Inject
  CurrencyContext currencyContext;
  @Inject
  PrefHandler prefHandler;

  private CurrencyViewModel currencyViewModel;

  public static OnboardingDataFragment newInstance() {
    return new OnboardingDataFragment();
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Icepick.restoreInstanceState(this, savedInstanceState);
    MyApplication.getInstance().getAppComponent().inject(this);
    currencyViewModel = ViewModelProviders.of(this).get(CurrencyViewModel.class);
  }

  @Override
  public void onSaveInstanceState(@NonNull Bundle outState) {
    super.onSaveInstanceState(outState);
    final Currency selectedItem = (Currency) currencySpinner.getSelectedItem();
    if (selectedItem != null) {
      outState.putString(KEY_CURRENCY, selectedItem.code());
    }
    String label = labelEditText.getText().toString();
    outState.putBoolean(KEY_LABEL_UNCHANGED_OR_EMPTY, TextUtils.isEmpty(label) ||
        label.equals(getString(R.string.default_account_name)));
    Icepick.saveInstanceState(this, outState);
  }

  @Override
  protected int getNavigationButtonId() {
    return R.id.suw_navbar_done;
  }

  @Override
  protected void onNextButtonClicked() {
    prefHandler.putString(PrefKey.HOME_CURRENCY, validateSelectedCurrency().code());
    ((OnboardingActivity) getActivity()).finishOnboarding();
  }

  @Override
  protected int getMenuResId() {
    return R.menu.onboarding_data;
  }

  @Override
  public void setupMenu() {
    Menu menu = toolbar.getMenu();
    SubMenu subMenu = menu.findItem(R.id.SetupFromRemote).getSubMenu();
    subMenu.clear();
    ((SyncBackendSetupActivity) getActivity()).addSyncProviderMenuEntries(subMenu);
    GenericAccountService.getAccountsAsStream(getActivity()).forEach(
        account -> subMenu.add(Menu.NONE, Menu.NONE, Menu.NONE, account.name));
    toolbar.setOnMenuItemClickListener(this::onRestoreMenuItemSelected);
  }

  private boolean onRestoreMenuItemSelected(MenuItem item) {
    if (item.getItemId() == R.id.SetupFromLocal) {
      final Intent intent = new Intent(getActivity(), BackupRestoreActivity.class);
      intent.setAction(BackupRestoreActivity.ACTION_RESTORE);
      getActivity().startActivityForResult(intent, RESTORE_REQUEST);
    } else {
      SyncBackendSetupActivity hostActivity = (SyncBackendSetupActivity) getActivity();
      if (item.getItemId() == Menu.NONE) {
        hostActivity.fetchAccountData(item.getTitle().toString());
      } else {
        hostActivity.startSetup(item.getItemId());
      }
    }
    return true;
  }

  @Override
  protected int getLayoutResId() {
    return R.layout.onboarding_wizzard_data;
  }

  @Override
  protected void configureView(Bundle savedInstanceState) {
    //label
    setDefaultLabel();

    //amount
    amountInput.setFractionDigits(2);
    amountInput.setAmount(BigDecimal.ZERO);
    amountInput.findViewById(R.id.Calculator).setVisibility(View.GONE);

    //currency
    DialogUtils.configureCurrencySpinner(currencySpinner, this);

    String code = savedInstanceState != null ? (String) savedInstanceState.get(KEY_CURRENCY) : null;
    final Currency currency = code != null ? Currency.create(code) : currencyViewModel.getDefault();

    currencyViewModel.getCurrencies().observe(this, currencies -> {
      final CurrencyAdapter adapter = (CurrencyAdapter) currencySpinner.getAdapter();
      adapter.clear();
      adapter.addAll(currencies);
      currencySpinner.setSelection(adapter.getPosition(currency));
      nextButton.setVisibility(View.VISIBLE);
    });
    currencyViewModel.loadCurrencies();

    //type
    DialogUtils.configureTypeSpinner(accountTypeSpinner);

    //color
    UiUtils.setBackgroundOnButton(colorIndicator, accountColor);

    if (moreOptionsShown) {
      showMoreOptions();
    }
  }

  @Override
  protected CharSequence getTitle() {
    return getString(R.string.onboarding_data_title);
  }

  public void setDefaultLabel() {
    labelEditText.setText(R.string.default_account_name);
  }

  @Override
  public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
    super.onViewStateRestored(savedInstanceState);
    if (savedInstanceState != null) {
      if(savedInstanceState.getBoolean(KEY_LABEL_UNCHANGED_OR_EMPTY)) {
        setDefaultLabel();
      }
    }
  }

  public void showMoreOptions(View view) {
    moreOptionsShown = true;
    showMoreOptions();
  }

  private void showMoreOptions() {
    moreOptionsButton.setVisibility(View.GONE);
    moreOptionsContainer.setVisibility(View.VISIBLE);
  }

  @Override
  public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    switch (parent.getId()) {
      case R.id.Currency:
        amountInput.setFractionDigits(validateSelectedCurrency().fractionDigits());
        break;
    }
  }

  private CurrencyUnit validateSelectedCurrency() {
    final String currency = ((Currency) currencySpinner.getSelectedItem()).code();
    return currencyContext.get(currency);
  }

  @Override
  public void onNothingSelected(AdapterView<?> parent) {

  }

  public Account buildAccount() {
    String label = labelEditText.getText().toString();
    if (android.text.TextUtils.isEmpty(label)) {
      label = getString(R.string.default_account_name);
    }
    BigDecimal openingBalance = amountInput.getTypedValue();
    CurrencyUnit currency = validateSelectedCurrency();
    return new Account(label, currency, new Money(currency, openingBalance),
        descriptionEditText.getText().toString(),
        (AccountType) accountTypeSpinner.getSelectedItem(), accountColor);
  }

  public void editAccountColor() {
    SimpleColorDialog.build()
        .allowCustom(true)
        .cancelable(false)
        .neut()
        .colorPreset(accountColor)
        .theme(R.style.SimpleDialog)
        .show(this, EDIT_COLOR_DIALOG);
  }

  @Override
  public boolean onResult(@NonNull String dialogTag, int which, @NonNull Bundle extras) {
    if (EDIT_COLOR_DIALOG.equals(dialogTag) && which == BUTTON_POSITIVE) {
      accountColor = extras.getInt(SimpleColorDialog.COLOR);
      UiUtils.setBackgroundOnButton(colorIndicator, accountColor);
      return true;
    }
    return false;
  }
}

package org.totschnig.myexpenses.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.annimon.stream.Stream;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.snackbar.Snackbar;

import org.totschnig.myexpenses.R;
import org.totschnig.myexpenses.activity.BudgetEdit;
import org.totschnig.myexpenses.activity.ProtectedFragmentActivity;
import org.totschnig.myexpenses.adapter.BudgetAdapter;
import org.totschnig.myexpenses.model.CurrencyUnit;
import org.totschnig.myexpenses.model.Grouping;
import org.totschnig.myexpenses.model.Money;
import org.totschnig.myexpenses.model.Sort;
import org.totschnig.myexpenses.preference.PrefKey;
import org.totschnig.myexpenses.provider.TransactionProvider;
import org.totschnig.myexpenses.provider.filter.FilterPersistence;
import org.totschnig.myexpenses.ui.BudgetSummary;
import org.totschnig.myexpenses.util.TextUtils;
import org.totschnig.myexpenses.viewmodel.BudgetViewModel;
import org.totschnig.myexpenses.viewmodel.data.Budget;
import org.totschnig.myexpenses.viewmodel.data.Category;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.lifecycle.ViewModelProviders;
import butterknife.ButterKnife;
import eltos.simpledialogfragment.SimpleDialog;
import eltos.simpledialogfragment.form.AmountEdit;
import eltos.simpledialogfragment.form.SimpleFormDialog;

import static org.totschnig.myexpenses.provider.DatabaseConstants.KEY_AMOUNT;
import static org.totschnig.myexpenses.provider.DatabaseConstants.KEY_BUDGET;
import static org.totschnig.myexpenses.provider.DatabaseConstants.KEY_BUDGETID;
import static org.totschnig.myexpenses.provider.DatabaseConstants.KEY_CATID;
import static org.totschnig.myexpenses.provider.DatabaseConstants.KEY_ROWID;
import static org.totschnig.myexpenses.util.MoreUiUtilsKt.addChipsBulk;
import static org.totschnig.myexpenses.util.TextUtils.appendCurrencySymbol;

public class BudgetFragment extends DistributionBaseFragment implements
    BudgetAdapter.OnBudgetClickListener, SimpleDialog.OnDialogResultListener {
  private Budget budget;
  BudgetSummary budgetSummary;
  ChipGroup filterGroup;
  public static final String EDIT_BUDGET_DIALOG = "EDIT_BUDGET";
  private static final String DELETE_BUDGET_DIALOG = "DELETE_BUDGET";

  private BudgetViewModel viewModel;

  public long getAllocated() {
    return allocated;
  }

  private long allocated, spent;

  private boolean allocatedOnly;

  FilterPersistence filterPersistence;

  @Override
  protected boolean showAllCategories() {
    return true;
  }

  @Override
  protected Object getSecondarySort() {
    return Sort.preferredOrderByForBudgets(getSortOrderPrefKey(), prefHandler, getDefaultSortOrder());
  }

  @Override
  protected PrefKey getSortOrderPrefKey() {
    return PrefKey.SORT_ORDER_BUDGET_CATEGORIES;
  }

  @Override
  protected Sort getDefaultSortOrder() {
    return Sort.ALLOCATED;
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.budget_list, container, false);
    ButterKnife.bind(this, view);
    budgetSummary = (BudgetSummary) inflater.inflate(R.layout.budget_fragment_summary, mListView, false);
    budgetSummary.setOnBudgetClickListener(view1 -> onBudgetClick(null, null));
    filterGroup = (ChipGroup) inflater.inflate(R.layout.budget_filter, mListView, false);
    registerForContextMenu(mListView);
    return view;
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    viewModel = ViewModelProviders.of(this).get(BudgetViewModel.class);
    viewModel.getBudget().observe(getViewLifecycleOwner(), this::setBudget);
    viewModel.getDatabaseResult().observe(getViewLifecycleOwner(), success -> {
      Activity activity = getActivity();
      if (activity != null) {
        if (success > -1) {
          activity.setResult(Activity.RESULT_FIRST_USER);
          activity.finish();
        } else {
          Toast.makeText(activity, "Error while deleting budget", Toast.LENGTH_LONG).show();
        }
      }
    });
    final long budgetId = getActivity().getIntent().getLongExtra(KEY_ROWID, 0);
    loadBudget(budgetId);
    filterPersistence = new FilterPersistence(prefHandler, BudgetViewModel.Companion.prefNameForCriteria(budgetId), null, false, true);
  }

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    if (budget != null) {
      switch (item.getItemId()) {
        case R.id.EDIT_COMMAND: {
          Intent intent = new Intent(getActivity(), BudgetEdit.class);
          intent.putExtra(KEY_ROWID, budget.getId());
          startActivity(intent);
          return true;
        }
        case R.id.DELETE_COMMAND: {
          SimpleDialog.build()
              .title(R.string.dialog_title_warning_delete_budget)
              .msg(getString(R.string.warning_delete_budget, budget.getTitle()) + " " + getString(R.string.continue_confirmation))
              .pos(R.string.menu_delete)
              .neg(android.R.string.cancel)
              .theme(R.style.SimpleDialog)
              .show(this, DELETE_BUDGET_DIALOG);
          return true;
        }
        case R.id.BUDGET_ALLOCATED_ONLY: {
          allocatedOnly = !allocatedOnly;
          prefHandler.putBoolean(getTemplateForAllocatedOnlyKey(budget), allocatedOnly);
          reset();
          return true;
        }
      }
    }
    return super.onOptionsItemSelected(item);
  }

  private void showEditBudgetDialog(Category category, Category parentItem) {
    final Money amount, max, min;
    final SimpleFormDialog simpleFormDialog = SimpleFormDialog.build()
        .title(category == null ? getString(R.string.dialog_title_edit_budget) : category.label)
        .theme(R.style.SimpleDialog)
        .neg();
    if (category != null) {
      long allocated = parentItem == null ? getAllocated() :
          Stream.of(parentItem.getChildren()).mapToLong(category1 -> category1.budget).sum();
      final Long budgetAmount = parentItem == null ? budget.getAmount().getAmountMinor() : parentItem.budget;
      long allocatable = budgetAmount - allocated;
      final long maxLong = allocatable + category.budget;
      if (maxLong <= 0) {
        ((ProtectedFragmentActivity) getActivity()).showSnackbar(TextUtils.concatResStrings(getActivity(), " ",
            parentItem == null ? R.string.budget_exceeded_error_1_2 : R.string.sub_budget_exceeded_error_1_2,
            parentItem == null ? R.string.budget_exceeded_error_2 : R.string.sub_budget_exceeded_error_2),
            Snackbar.LENGTH_LONG);
        return;
      }
      Bundle bundle = new Bundle(1);
      bundle.putLong(KEY_CATID, category.id);
      simpleFormDialog.extra(bundle);
      amount = new Money(budget.getCurrency(), category.budget);
      max = new Money(budget.getCurrency(), maxLong);
      min = parentItem != null ? null : new Money(budget.getCurrency(), Stream.of(category.getChildren()).mapToLong(category1 -> category1.budget).sum());
    } else {
      amount = budget.getAmount();
      max = null;
      min = new Money(budget.getCurrency(), getAllocated());
    }
    simpleFormDialog
        .fields(buildAmountField(amount, max == null ? null : max.getAmountMajor(),
            min == null ? null : min.getAmountMajor(), category != null, parentItem != null, getContext()))
        .show(this, EDIT_BUDGET_DIALOG);
  }

  public static AmountEdit buildAmountField(Money amount, Context context) {
    return buildAmountField(amount, null, null, false, false, context);
  }

  public static AmountEdit buildAmountField(Money amount, BigDecimal max, BigDecimal min,
                                            boolean isMainCategory, boolean isSubCategory, Context context) {
    final AmountEdit amountEdit = AmountEdit.plain(KEY_AMOUNT)
        .label(appendCurrencySymbol(context, R.string.budget_allocated_amount, amount.getCurrencyUnit()))
        .fractionDigits(amount.getCurrencyUnit().fractionDigits()).required();
    if (!(amount.getAmountMajor().compareTo(BigDecimal.ZERO) == 0)) {
      amountEdit.amount(amount.getAmountMajor());
    }
    if (max != null) {
      amountEdit.max(max, String.format(Locale.ROOT, "%s %s",
          context.getString(isSubCategory ? R.string.sub_budget_exceeded_error_1_1 : R.string.budget_exceeded_error_1_1, max),
          context.getString(isSubCategory ? R.string.sub_budget_exceeded_error_2 : R.string.budget_exceeded_error_2)));
    }
    if (min != null) {
      amountEdit.min(min, context.getString(isMainCategory ? R.string.sub_budget_under_allocated_error : R.string.budget_under_allocated_error, min));
    }
    return amountEdit;
  }

  @Override
  public boolean onResult(@NonNull String dialogTag, int which, @NonNull Bundle extras) {
    if (which == BUTTON_POSITIVE) {
      if (dialogTag.equals(EDIT_BUDGET_DIALOG)) {
        final Money amount = new Money(budget.getCurrency(), (BigDecimal) extras.getSerializable(KEY_AMOUNT));
        viewModel.updateBudget(this.budget.getId(), extras.getLong(KEY_CATID), amount);
        return true;
      }
      if (dialogTag.equals(DELETE_BUDGET_DIALOG)) {
        viewModel.deleteBudget(budget.getId());
        return true;
      }
    }

    return false;
  }

  public void loadBudget(long budgetId) {
    viewModel.loadBudget(budgetId, false);
  }

  private void setBudget(@NonNull Budget budget) {
    this.budget = budget;
    filterPersistence.reloadFromPreferences();
    allocatedOnly = prefHandler.getBoolean(getTemplateForAllocatedOnlyKey(budget),false);
    setAccountInfo(new AccountInfo() {
      @Override
      public long getId() {
        return budget.getAccountId();
      }

      @Override
      public CurrencyUnit getCurrencyUnit() {
        return budget.getCurrency();
      }
    });
    final ActionBar actionBar = ((ProtectedFragmentActivity) getActivity()).getSupportActionBar();
    actionBar.setTitle(budget.getTitle());
    if (mAdapter == null) {
      mAdapter = new BudgetAdapter((ProtectedFragmentActivity) getActivity(), currencyFormatter,
          budget.getCurrency(), this);
      mListView.addHeaderView(filterGroup, null, false);
      mListView.addHeaderView(budgetSummary, null, false);
      mListView.setAdapter(mAdapter);
    }

    mGrouping = budget.getGrouping();
    mGroupingYear = 0;
    mGroupingSecond = 0;
    if (mGrouping == Grouping.NONE) {
      updateSum();
      loadData();
      setSubTitle(budget.durationPrettyPrint());
    } else {
      updateDateInfo(false);
    }
    updateSummary();
    setFilterInfo();
  }

  private void setFilterInfo() {
    ArrayList<String> filterList = new ArrayList<>();
    filterList.add(budget.label(requireContext()));
    Stream.of(filterPersistence.getWhereFilter().getCriteria()).forEach(criterion -> filterList.add(criterion.prettyPrint(requireContext())));
    addChipsBulk(filterGroup, filterList, null);
  }

  private String getTemplateForAllocatedOnlyKey(@NonNull Budget budget) {
    return String.format(Locale.ROOT, "allocatedOnly_%d",
        budget.getId());
  }

  @Override
  public void onBudgetClick(Category category, Category parentItem) {
    showEditBudgetDialog(category, parentItem);
  }

  @Override
  protected void onDateInfoReceived() {
    //we fetch dateinfo from database two times, first to get info about current date,
    //then we use this info in second run
    if (mGroupingYear == 0) {
      mGroupingYear = dateInfo.getThisYear();
      switch (mGrouping) {
        case DAY:
          mGroupingSecond = dateInfo.getThisDay();
          break;
        case WEEK:
          mGroupingYear = dateInfo.getThisYearOfWeekStart();
          mGroupingSecond = dateInfo.getThisWeek();
          break;
        case MONTH:
          mGroupingYear = dateInfo.getThisYearOfMonthStart();
          mGroupingSecond = dateInfo.getThisMonth();
          break;
      }
      updateDateInfo(true);
      updateSum();
    } else {
      super.onDateInfoReceived();
      loadData();
    }
  }

  @Override
  protected String buildFilterClause(String tableName) {
    String dateFilter = (budget.getGrouping() == Grouping.NONE) ? budget.durationAsSqlFilter() :
        super.buildFilterClause(tableName);

    return filterPersistence.getWhereFilter().isEmpty() ? dateFilter :
        dateFilter + " AND " + filterPersistence.getWhereFilter().getSelectionForParts(tableName);
  }

  @Override
  protected String[] filterSelectionArgs() {
    return filterPersistence.getWhereFilter().getSelectionArgs(true);
  }

  @Override
  protected void onLoadFinished() {
    super.onLoadFinished();
    allocated = Stream.of(mAdapter.getMainCategories()).mapToLong(category -> category.budget).sum();
    budgetSummary.setAllocated(currencyFormatter.formatCurrency(new Money(budget.getCurrency(),
        allocated)));
  }

  @Override
  void updateIncomeAndExpense(long income, long expense) {
    this.spent = expense;
    if (aggregateTypes) {
      this.spent -= income;
    }
    updateSummary();
  }

  private void updateSummary() {
    final ProtectedFragmentActivity context = (ProtectedFragmentActivity) getActivity();
    if (context == null) {
      return;
    }
    budgetSummary.bind(budget, spent, currencyFormatter);
  }

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    inflater.inflate(R.menu.budget, menu);
  }

  @Override
  public void onPrepareOptionsMenu(@NonNull Menu menu) {
    super.onPrepareOptionsMenu(menu);
    MenuItem m = menu.findItem(R.id.BUDGET_ALLOCATED_ONLY);
    if (m != null) {
      m.setChecked(allocatedOnly);
    }
  }

  @Override
  protected String getExtraColumn() {
    return KEY_BUDGET;
  }

  @Override
  protected Uri getCategoriesUri() {
    final Uri.Builder builder = super.getCategoriesUri().buildUpon()
        .appendQueryParameter(KEY_BUDGETID, String.valueOf(budget.getId()));
    if (allocatedOnly) {
      builder.appendQueryParameter(TransactionProvider.QUERY_PARAMETER_ALLOCATED_ONLY, "1");
    }
    return builder.build();
  }

  @NonNull
  protected PrefKey getPrefKey() {
    return PrefKey.BUDGET_AGGREGATE_TYPES;
  }
}

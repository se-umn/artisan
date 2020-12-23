/*   This file is part of My Expenses.
 *   My Expenses is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   My Expenses is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with My Expenses.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.totschnig.myexpenses.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.SparseBooleanArray;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.ExpandableListContextMenuInfo;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.squareup.sqlbrite3.BriteContentResolver;
import com.squareup.sqlbrite3.QueryObservable;
import com.squareup.sqlbrite3.SqlBrite;

import org.apache.commons.lang3.ArrayUtils;
import org.totschnig.myexpenses.MyApplication;
import org.totschnig.myexpenses.R;
import org.totschnig.myexpenses.activity.CategoryActivity;
import org.totschnig.myexpenses.activity.ProtectedFragmentActivity;
import org.totschnig.myexpenses.adapter.CategoryTreeAdapter;
import org.totschnig.myexpenses.adapter.CategoryTreeBaseAdapter;
import org.totschnig.myexpenses.dialog.MessageDialogFragment;
import org.totschnig.myexpenses.dialog.select.SelectMainCategoryDialogFragment;
import org.totschnig.myexpenses.model.CurrencyContext;
import org.totschnig.myexpenses.model.Sort;
import org.totschnig.myexpenses.preference.PrefHandler;
import org.totschnig.myexpenses.preference.PrefKey;
import org.totschnig.myexpenses.provider.DatabaseConstants;
import org.totschnig.myexpenses.provider.TransactionProvider;
import org.totschnig.myexpenses.task.TaskExecutionFragment;
import org.totschnig.myexpenses.util.CurrencyFormatter;
import org.totschnig.myexpenses.util.MenuUtilsKt;
import org.totschnig.myexpenses.util.Utils;
import org.totschnig.myexpenses.viewmodel.data.Category;

import java.util.ArrayList;
import java.util.Arrays;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import icepick.Icepick;
import icepick.State;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

import static android.app.Activity.RESULT_FIRST_USER;
import static android.app.Activity.RESULT_OK;
import static org.totschnig.myexpenses.ConstantsKt.ACTION_MANAGE;
import static org.totschnig.myexpenses.ConstantsKt.ACTION_SELECT_FILTER;
import static org.totschnig.myexpenses.ConstantsKt.ACTION_SELECT_MAPPING;
import static org.totschnig.myexpenses.adapter.CategoryTreeBaseAdapter.NULL_ITEM_ID;
import static org.totschnig.myexpenses.provider.DatabaseConstants.KEY_CATID;
import static org.totschnig.myexpenses.provider.DatabaseConstants.KEY_COLOR;
import static org.totschnig.myexpenses.provider.DatabaseConstants.KEY_ICON;
import static org.totschnig.myexpenses.provider.DatabaseConstants.KEY_LABEL;
import static org.totschnig.myexpenses.provider.DatabaseConstants.KEY_LABEL_NORMALIZED;
import static org.totschnig.myexpenses.provider.DatabaseConstants.KEY_PARENTID;
import static org.totschnig.myexpenses.provider.DatabaseConstants.KEY_ROWID;
import static org.totschnig.myexpenses.provider.DatabaseConstants.TABLE_BUDGET_CATEGORIES;
import static org.totschnig.myexpenses.provider.DatabaseConstants.TABLE_CATEGORIES;
import static org.totschnig.myexpenses.util.MenuUtilsKt.prepareSearch;

public class CategoryList extends SortableListFragment {

  public static final String KEY_FILTER = "filter";
  private Disposable categoryDisposable;
  public static final String CATTREE_WHERE_CLAUSE = KEY_CATID + " IN (SELECT " +
      TABLE_CATEGORIES + "." + KEY_ROWID +
      " UNION SELECT " + KEY_ROWID + " FROM "
      + TABLE_CATEGORIES + " subtree WHERE " + KEY_PARENTID + " = " + TABLE_CATEGORIES + "." + KEY_ROWID + ")";
  // The adapter needs to find main categories first
  protected static final String PRIMARY_SORT = "case when " + KEY_PARENTID + " is null then 0 else 1 end";


  protected int getMenuResource() {
    return R.menu.categorylist_context;
  }

  protected CategoryTreeBaseAdapter mAdapter;
  @BindView(R.id.list)
  ExpandableListView mListView;
  @Nullable
  @BindView(R.id.sum_income)
  TextView incomeSumTv;
  @Nullable
  @BindView(R.id.sum_expense)
  TextView expenseSumTv;
  @Nullable
  @BindView(R.id.SETUP_CATEGORIES_DEFAULT_COMMAND)
  View mImportButton;

  protected int lastExpandedPosition = -1;

  @State
  String mFilter;

  @Inject
  CurrencyFormatter currencyFormatter;
  @Inject
  PrefHandler prefHandler;
  @Inject
  BriteContentResolver briteContentResolver;
  @Inject
  CurrencyContext currencyContext;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setHasOptionsMenu(true);
    Icepick.restoreInstanceState(this, savedInstanceState);
    MyApplication.getInstance().getAppComponent().inject(this);
    briteContentResolver = new SqlBrite.Builder().build().wrapContentProvider(getContext().getContentResolver(), Schedulers.io());
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    final ProtectedFragmentActivity ctx = (ProtectedFragmentActivity) getActivity();
    View v = inflater.inflate(R.layout.categories_list, container, false);
    ButterKnife.bind(this, v);
    configureImportButton(true);
    final View emptyView = v.findViewById(R.id.empty);
    mListView.setEmptyView(emptyView);
    mAdapter = new CategoryTreeAdapter(ctx, currencyFormatter, null, isWithMainColors(),
        false, getAction().equals(ACTION_SELECT_FILTER));
    mListView.setAdapter(mAdapter);
    loadData();
    registerForContextualActionBar(mListView);
    return v;
  }

  private void configureImportButton(boolean visible) {
    if (mImportButton != null) {
      mImportButton.setVisibility(visible/* && getResources().getBoolean(R.bool.has_localized_categories)*/
          ? View.VISIBLE : View.GONE);
    }
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    disposeCategory();
  }

  protected void onLoadFinished() {
    lastExpandedPosition = -1;
  }

  protected void loadData() {
    disposeCategory();
    categoryDisposable = createQuery()
        .map(SqlBrite.Query::run)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(cursor -> {
          mAdapter.ingest(cursor);
          onLoadFinished();
        });
  }

  protected QueryObservable createQuery() {
    String selection = null;
    String[] selectionArgs;
    String catFilter = CATTREE_WHERE_CLAUSE;
    String[] projection = new String[]{
        KEY_ROWID,
        KEY_PARENTID,
        KEY_LABEL,
        KEY_COLOR,
        KEY_ICON,
        //here we do not filter out void transactions since they need to be considered as mapped
        "(select 1 FROM " + TABLE_BUDGET_CATEGORIES + " WHERE " + catFilter + ") AS " + DatabaseConstants.KEY_MAPPED_BUDGETS
    };
    boolean isFiltered = !TextUtils.isEmpty(mFilter);
    if (isFiltered) {
      String normalized = Utils.esacapeSqlLikeExpression(Utils.normalize(mFilter));
      String filterSelection = KEY_LABEL_NORMALIZED + " LIKE ?";
      selectionArgs = new String[]{"%" + normalized + "%", "%" + normalized + "%"};
      selection = filterSelection + " OR EXISTS (SELECT 1 FROM " + TABLE_CATEGORIES +
          " subtree WHERE " + KEY_PARENTID + " = " + TABLE_CATEGORIES + "." + KEY_ROWID + " AND ("
          + filterSelection + " ))";
    } else {
      selectionArgs = null;
    }
    return briteContentResolver.createQuery(TransactionProvider.CATEGORIES_URI,
        projection, selection, selectionArgs, getSortExpression(), true);
  }

  protected final String getSortExpression() {
    return String.format("%s,%s", PRIMARY_SORT, getSecondarySort());
  }

  protected Object getSecondarySort() {
    return Sort.preferredOrderByForCategories(getSortOrderPrefKey(), prefHandler, getDefaultSortOrder());
  }

  private void disposeCategory() {
    if (categoryDisposable != null && !categoryDisposable.isDisposed()) {
      categoryDisposable.dispose();
    }
  }

  @Override
  public boolean dispatchCommandMultiple(int command,
                                         SparseBooleanArray positions, Long[] itemIds) {
    ProtectedFragmentActivity ctx = (ProtectedFragmentActivity) getActivity();
    ArrayList<Long> idList;
    switch (command) {
      case R.id.DELETE_COMMAND: {
        int hasChildrenCount = 0, mappedBudgetsCount = 0;
        idList = new ArrayList<>();
        for (int i = 0; i < positions.size(); i++) {
          Category c;
          if (positions.valueAt(i)) {
            int position = positions.keyAt(i);
            long pos = mListView.getExpandableListPosition(position);
            int type = ExpandableListView.getPackedPositionType(pos);
            int group = ExpandableListView.getPackedPositionGroup(pos),
                child = ExpandableListView.getPackedPositionChild(pos);
            if (type == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
              c = mAdapter.getChild(group, child);
            } else {
              c = mAdapter.getGroup(group);
            }
            Bundle extras = ctx.getIntent().getExtras();
            if ((extras == null || extras.getLong(KEY_ROWID) != c.id)) {
              if (type == ExpandableListView.PACKED_POSITION_TYPE_GROUP && c.hasChildren()) {
                hasChildrenCount++;
              }
              if (c.hasMappedBudgets) {
                mappedBudgetsCount++;
              }
              idList.add(c.id);
            } else {
              ctx.showSnackbar(getResources().getQuantityString(R.plurals.not_deletable_mapped_transactions,
                  1, 1), Snackbar.LENGTH_LONG);
            }
          }
        }
        if (!idList.isEmpty()) {
          Long[] objectIds = idList.toArray(new Long[idList.size()]);
          if (hasChildrenCount > 0 || mappedBudgetsCount > 0) {
            String message = hasChildrenCount > 0 ?
                getResources().getQuantityString(R.plurals.warning_delete_main_category, hasChildrenCount, hasChildrenCount) : "";
            if (mappedBudgetsCount > 0) {
              if (!message.equals("")) {
                message += " ";
              }
              message += getString(R.string.warning_delete_category_with_budget);
            }
            MessageDialogFragment.newInstance(
                getString(R.string.dialog_title_warning_delete_category),
                message,
                new MessageDialogFragment.Button(android.R.string.yes, R.id.DELETE_COMMAND_DO, objectIds),
                null,
                new MessageDialogFragment.Button(android.R.string.no, R.id.CANCEL_CALLBACK_COMMAND, null))
                .show(ctx.getSupportFragmentManager(), "DELETE_CATEGORY");
          } else {
            ctx.dispatchCommand(R.id.DELETE_COMMAND_DO, objectIds);
          }
        }
        return true;
      }
      case R.id.SELECT_COMMAND_MULTIPLE: {
        if (itemIds.length == 1 || Arrays.asList(itemIds).indexOf(NULL_ITEM_ID) == -1) {
          ArrayList<String> labelList = new ArrayList<>();
          for (int i = 0; i < positions.size(); i++) {
            Category c;
            if (positions.valueAt(i)) {
              int position = positions.keyAt(i);
              long pos = mListView.getExpandableListPosition(position);
              int type = ExpandableListView.getPackedPositionType(pos);
              int group = ExpandableListView.getPackedPositionGroup(pos),
                  child = ExpandableListView.getPackedPositionChild(pos);
              if (type == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
                c = mAdapter.getChild(group, child);
              } else {
                c = mAdapter.getGroup(group);
              }
              labelList.add(c.label);
            }
          }
          Intent intent = new Intent();
          intent.putExtra(KEY_CATID, ArrayUtils.toPrimitive(itemIds));
          intent.putExtra(KEY_LABEL, TextUtils.join(",", labelList));
          ctx.setResult(RESULT_FIRST_USER, intent);
          ctx.finish();
        } else {
          ctx.showSnackbar(R.string.unmapped_filter_only_single, Snackbar.LENGTH_LONG);
        }
        return true;
      }
      case R.id.MOVE_COMMAND:
        final Long[] excludedIds;
        final boolean inGroup = expandableListSelectionType == ExpandableListView.PACKED_POSITION_TYPE_GROUP;
        if (inGroup) {
          excludedIds = itemIds;
        } else {
          idList = new ArrayList<>();
          for (int i = 0; i < positions.size(); i++) {
            if (positions.valueAt(i)) {
              int position = positions.keyAt(i);
              long pos = mListView.getExpandableListPosition(position);
              int group = ExpandableListView.getPackedPositionGroup(pos);
              idList.add(mAdapter.getGroup(group).id);
            }
          }
          excludedIds = idList.toArray(new Long[idList.size()]);
        }
        Bundle args = new Bundle(3);
        args.putBoolean(SelectMainCategoryDialogFragment.KEY_WITH_ROOT, !inGroup);
        args.putLongArray(SelectMainCategoryDialogFragment.KEY_EXCLUDED_ID, ArrayUtils.toPrimitive(excludedIds));
        args.putLongArray(TaskExecutionFragment.KEY_OBJECT_IDS, ArrayUtils.toPrimitive(itemIds));
        SelectMainCategoryDialogFragment.newInstance(args)
            .show(getFragmentManager(), "SELECT_TARGET");
        return true;
    }
    return false;
  }

  @Override
  public boolean dispatchCommandSingle(int command, ContextMenu.ContextMenuInfo info) {
    CategoryActivity ctx = (CategoryActivity) getActivity();
    String action = getAction();
    ExpandableListContextMenuInfo elcmi = (ExpandableListContextMenuInfo) info;
    int type = ExpandableListView.getPackedPositionType(elcmi.packedPosition);
    Category category;
    boolean isMain;
    int group = ExpandableListView.getPackedPositionGroup(elcmi.packedPosition),
        child = ExpandableListView.getPackedPositionChild(elcmi.packedPosition);
    if (type == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
      category = mAdapter.getChild(group, child);
      isMain = false;
    } else {
      category = mAdapter.getGroup(group);
      isMain = true;
    }
    String label = category.label;
    switch (command) {
      case R.id.COLOR_COMMAND:
        ctx.editCategoryColor(category);
        return true;
      case R.id.EDIT_COMMAND:
        ctx.editCat(category);
        return true;
      case R.id.SELECT_COMMAND:
        if (!isMain && action.equals(ACTION_SELECT_MAPPING)) {
          label = mAdapter.getGroup(group).label + TransactionList.CATEGORY_SEPARATOR + label;
        }
        doSingleSelection(elcmi.id, label, category.icon, isMain);
        finishActionMode();
        return true;
      case R.id.CREATE_COMMAND:
        ctx.createCat(elcmi.id);
        return true;
    }
    return super.dispatchCommandSingle(command, info);
  }

  protected PrefKey getSortOrderPrefKey() {
    return PrefKey.SORT_ORDER_CATEGORIES;
  }

  @Override
  public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
    if (getActivity() == null) return;
    inflater.inflate(R.menu.search, menu);
    MenuUtilsKt.configureSearch(getActivity(), menu, this::onQueryTextChange);
  }

  private Boolean onQueryTextChange(String newText) {
    if (TextUtils.isEmpty(newText)) {
      mFilter = "";
      configureImportButton(true);
    } else {
      mFilter = newText;
      // if a filter results in an empty list,
      // we do not want to show the setup default categories button
      configureImportButton(false);
    }
    collapseAll();
    loadData();
    return true;
  }

  @Override
  public void onPrepareOptionsMenu(@NonNull Menu menu) {
    super.onPrepareOptionsMenu(menu);
    prepareSearch(menu, mFilter);
  }

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    return handleSortOption(item);
  }

  public void collapseAll() {
    int count = mAdapter.getGroupCount();
    for (int i = 0; i < count; i++)
      mListView.collapseGroup(i);
  }

  /*     (non-Javadoc)
   * return the sub cat to the calling activity
   * @see android.app.ExpandableListActivity#onChildClick(android.widget.ExpandableListView, android.view.View, int, int, long)
   */
  @Override
  public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
    if (super.onChildClick(parent, v, groupPosition, childPosition, id))
      return true;
    if (getActivity() == null) {
      return false;
    }
    String action = getAction();
    if (action.equals(ACTION_MANAGE)) {
      return false;
    }
    String label = ((TextView) v.findViewById(R.id.label)).getText().toString();
    if (action.equals(ACTION_SELECT_MAPPING)) {
      label = mAdapter.getGroup(groupPosition).label + TransactionList.CATEGORY_SEPARATOR + label;
    }
    doSingleSelection(id, label, mAdapter.getChild(groupPosition, childPosition).icon, false);
    return true;
  }

  @Override
  public boolean onGroupClick(ExpandableListView parent, View v,
                              int groupPosition, long id) {
    if (super.onGroupClick(parent, v, groupPosition, id))
      return true;
    if (getActivity() == null) {
      return false;
    }
    String action = getAction();
    if (action.equals(ACTION_MANAGE) || mAdapter.getGroup(groupPosition).hasChildren()) {
      return false;
    }
    String label = ((TextView) v.findViewById(R.id.label)).getText().toString();
    doSingleSelection(id, label, mAdapter.getGroup(groupPosition).icon, true);
    return true;
  }

  protected void doSingleSelection(long cat_id, String label, String icon, boolean isMain) {
    Activity ctx = getActivity();
    Intent intent = new Intent();
    intent.putExtra(KEY_CATID, cat_id);
    intent.putExtra(KEY_LABEL, label);
    intent.putExtra(KEY_ICON, icon);
    ctx.setResult(RESULT_OK, intent);
    ctx.finish();
  }

  public void reset() {
    //TODO: would be nice to retrieve the same open groups on the next or previous group
    Timber.w("reset");
    mListView.clearChoices();
    lastExpandedPosition = -1;
    loadData();
  }

  @Override
  public void onSaveInstanceState(@NonNull Bundle outState) {
    super.onSaveInstanceState(outState);
    Icepick.saveInstanceState(this, outState);
  }

  @Override
  protected boolean withCommonContext() {
    return !(getAction().equals(ACTION_SELECT_FILTER));
  }

  @Override
  protected void inflateHelper(Menu menu, int listId) {
    super.inflateHelper(menu, listId);
    MenuInflater inflater = getActivity().getMenuInflater();
    if (hasSelectSingle()) {
      inflater.inflate(R.menu.select, menu);
    }
    if (hasSelectMultiple()) {
      inflater.inflate(R.menu.select_multiple, menu);
    }
  }

  protected boolean hasSelectSingle() {
    return getAction().equals(ACTION_SELECT_MAPPING);
  }

  protected boolean hasSelectMultiple() {
    return getAction().equals(ACTION_SELECT_FILTER);
  }

  @Override
  protected void configureMenuLegacy(Menu menu, ContextMenu.ContextMenuInfo menuInfo, int listId) {
    super.configureMenuLegacy(menu, menuInfo, listId);
    boolean hasChildren = false;
    if (expandableListSelectionType == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
      ExpandableListContextMenuInfo info = (ExpandableListContextMenuInfo) menuInfo;
      int groupPos = ExpandableListView.getPackedPositionGroup(info.packedPosition);
      hasChildren = hasChildren(groupPos);
    }
    configureMenuInternal(menu, hasChildren);
  }

  @Override
  protected void configureMenu11(Menu menu, int count, AbsListView lv) {
    boolean hasChildren = false;
    super.configureMenu11(menu, count, lv);
    if (expandableListSelectionType == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
      SparseBooleanArray checkedItemPositions = mListView.getCheckedItemPositions();
      for (int i = 0; i < checkedItemPositions.size(); i++) {
        if (checkedItemPositions.valueAt(i)) {
          int position = checkedItemPositions.keyAt(i);
          long pos = mListView.getExpandableListPosition(position);
          int groupPos = ExpandableListView.getPackedPositionGroup(pos);
          if (hasChildren(groupPos)) {
            hasChildren = true;
            break;
          }
        }
      }
    }
    configureMenuInternal(menu, hasChildren);
  }

  private boolean hasChildren(int position) {
    return position != -1 && mAdapter.getGroup(position).hasChildren();
  }

  protected void configureMenuInternal(Menu menu, boolean hasChildren) {
    String action = getAction();
    final boolean isFilter = action.equals(ACTION_SELECT_FILTER);
    maybeHide(menu, R.id.CREATE_COMMAND, isFilter);
    maybeHide(menu, R.id.MOVE_COMMAND, (isFilter || hasChildren));
    maybeHide(menu, R.id.COLOR_COMMAND, !isWithMainColors());
    maybeHide(menu, R.id.SELECT_ALL_COMMAND, true);
  }

  private void maybeHide(Menu menu, int id, boolean condition) {
    if (condition) {
      @Nullable final MenuItem item = menu.findItem(id);
      if (item != null) {
        item.setVisible(false);
      }
    }
  }

  private boolean isWithMainColors() {
    return getAction().equals(ACTION_MANAGE);
  }

  private String getAction() {
    return ((CategoryActivity) getActivity()).getAction();
  }
}

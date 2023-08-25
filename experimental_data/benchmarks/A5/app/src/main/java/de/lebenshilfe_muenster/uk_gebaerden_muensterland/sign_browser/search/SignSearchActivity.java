package de.lebenshilfe_muenster.uk_gebaerden_muensterland.sign_browser.search;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.lebenshilfe_muenster.uk_gebaerden_muensterland.R;
import de.lebenshilfe_muenster.uk_gebaerden_muensterland.database.Sign;
import de.lebenshilfe_muenster.uk_gebaerden_muensterland.database.SignDAO;
import de.lebenshilfe_muenster.uk_gebaerden_muensterland.sign_browser.SignBrowserAdapter;
import de.lebenshilfe_muenster.uk_gebaerden_muensterland.sign_browser.search.video.SignSearchVideoActivity;
import de.lebenshilfe_muenster.uk_gebaerden_muensterland.sign_browser.video.SignVideoFragment;
/**
 * Copyright (c) 2016 Matthias Tonhäuser
 * <p/>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p/>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
public class SignSearchActivity extends AppCompatActivity implements SignBrowserAdapter.OnSignBrowserAdapterSignClickedListener {

    public static final String QUERY = "sign_browser_search_query";
    private static final String TAG = SignSearchActivity.class.getSimpleName();
    private static final boolean INTERRUPT_IF_RUNNING = true;
    private String query = StringUtils.EMPTY;
    private SearchSignsTask signSearchTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate() " + this.hashCode());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);
        if (null != savedInstanceState) {
            this.query = savedInstanceState.getString(QUERY);
        } else {
            final Intent intent = getIntent();
            this.query = StringUtils.trimToEmpty(StringUtils.stripToEmpty(intent.getStringExtra(SearchManager.QUERY)));
            Validate.notNull(this.query, "The query supplied to this activity is null!");
        }
        setupRecyclerView();
        setupSupportActionBar();
        this.signSearchTask = new SearchSignsTask(this);
        this.signSearchTask.execute(this.query);
    }

    private void setupRecyclerView() {
        final RecyclerView recyclerView = (RecyclerView) this.findViewById(R.id.signRecyclerView);
        Validate.notNull(recyclerView);
        recyclerView.setHasFixedSize(true); // performance fix
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new SignBrowserAdapter(this, this, new ArrayList<Sign>()));
    }

    private void setupSupportActionBar() {
        final ActionBar supportActionBar = getSupportActionBar();
        Validate.notNull(supportActionBar, "SupportActionBar is null. Should have been set in onCreate().");
        supportActionBar.setTitle(getResources().getString(R.string.search_results) + StringUtils.SPACE + this.query);
        supportActionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onStart() {
        Log.d(TAG, "onStart() " + this.hashCode());
        super.onStart();
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause()" + this.hashCode());
        super.onPause();
        if (null != this.signSearchTask) {
            final AsyncTask.Status status = this.signSearchTask.getStatus();
            if (status.equals(AsyncTask.Status.PENDING)|| status.equals(AsyncTask.Status.RUNNING)) {
                this.signSearchTask.cancel(INTERRUPT_IF_RUNNING);
            }
        }
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu()" + this.hashCode());
        super.onCreateOptionsMenu(menu);
        final MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_sign_browser_search, menu);
        final SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "onSaveInstanceState() " + this.hashCode());
        super.onSaveInstanceState(outState);
        outState.putString(QUERY, this.query);
    }

    @Override
    public void onSignBrowserAdapterSignClicked(Sign sign) {
        Log.d(TAG, "onSignBrowserAdapterSignClicked() " + this.hashCode());
        final Intent intent = new Intent(this, SignSearchVideoActivity.class);
        final Bundle bundle = new Bundle();
        bundle.putString(SignSearchActivity.QUERY, this.query);
        bundle.putParcelable(SignVideoFragment.SIGN_TO_SHOW, sign);
        intent.putExtra(SignSearchVideoActivity.EXTRA, bundle);
        startActivity(intent);
    }

    /**
     * The first string parameter is the query to search in the name_locale_de for.
     */
    private class SearchSignsTask extends AsyncTask<String, Void, List<Sign>> {

        private final Context context;

        public SearchSignsTask(Context context) {
            this.context = context;
        }


        @Override
        protected List<Sign> doInBackground(String... params) {
            Log.d(SignSearchActivity.class.getSimpleName(), "doInBackground " + this.hashCode());
            Validate.exclusiveBetween(0, 2, params.length, "Exactly one string as a parameter allowed.");
            final List<Sign> signs = new ArrayList<>();
            if (isCancelled()) {
                return signs;
            }
            final SignDAO signDAO = SignDAO.getInstance(this.context);
            signDAO.open();
            signs.addAll(signDAO.read(params[0]));
            signDAO.close();
            return signs;
        }

        @Override
        protected void onPostExecute(List<Sign> result) {
            Log.d(TAG, "onPostExecute " + this.hashCode());
            final RecyclerView mRecyclerView = (RecyclerView) SignSearchActivity.this.findViewById(R.id.signRecyclerView);
            Validate.notNull(mRecyclerView, "RecyclerView is null.");
            mRecyclerView.swapAdapter(new SignBrowserAdapter(SignSearchActivity.this, SignSearchActivity.this, result), false);
        }

    }

}

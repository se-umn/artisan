package de.lebenshilfe_muenster.uk_gebaerden_muensterland.sign_browser;

import android.app.Activity;
import android.app.Fragment;
import android.app.SearchManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import org.apache.commons.lang3.Validate;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.lebenshilfe_muenster.uk_gebaerden_muensterland.R;
import de.lebenshilfe_muenster.uk_gebaerden_muensterland.database.Sign;
import de.lebenshilfe_muenster.uk_gebaerden_muensterland.database.SignDAO;

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
public class SignBrowserFragment extends Fragment implements SignBrowserAdapter.OnSignBrowserAdapterSignClickedListener{

    private static final boolean INTERRUPT_IF_RUNNING = true;
    private static final String TAG = SignBrowserFragment.class.getSimpleName();
    private static final String KEY_SHOW_STARRED_ONLY = "sign_browser_show_starred_only";
    private LoadSignsTask loadSignsTask;
    private boolean showStarredOnly = false;
    private OnSignBrowserSignClickedListener onSignBrowserSignClickedListener = null;

    @SuppressWarnings("deprecation") // necessary for API 15!
    @Override
    public void onAttach(Activity activity) {
        Log.d(TAG, "onAttach " + hashCode());
        super.onAttach(activity);
        try {
            this.onSignBrowserSignClickedListener = (OnSignBrowserSignClickedListener) activity;
        } catch (ClassCastException ex) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnSignBrowserSignClickedListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView " + hashCode());
        final View view = inflater.inflate(R.layout.browser_fragment, container, false);
        setHasOptionsMenu(true);
        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.signRecyclerView);
        recyclerView.setHasFixedSize(true); // performance fix
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new SignBrowserAdapter(this, getActivity(), new ArrayList<Sign>()));
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.d(TAG, "onActivityCreated " + hashCode());
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            this.showStarredOnly = savedInstanceState.getBoolean(KEY_SHOW_STARRED_ONLY);
        }
        this.loadSignsTask = new LoadSignsTask(getActivity());
        this.loadSignsTask.execute(this.showStarredOnly);
    }

    @Override
    public void onStart() {
        Log.d(TAG, "onStart " + hashCode());
        super.onStart();
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause " + hashCode());
        if (null != this.loadSignsTask) {
            final AsyncTask.Status status = this.loadSignsTask.getStatus();
            if (status.equals(AsyncTask.Status.PENDING)|| status.equals(AsyncTask.Status.RUNNING)) {
                this.loadSignsTask.cancel(INTERRUPT_IF_RUNNING);
            }
        }
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.d(TAG, "onStop " + hashCode());
        super.onStop();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy " + hashCode());
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        Log.d(TAG, "onDetach " + hashCode());
        super.onDetach();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.d(TAG, "onCreateOptionsMenu " + hashCode());
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.options_sign_browser, menu);
        final MenuItem item = menu.findItem(R.id.action_toggle_starred);
        if (this.showStarredOnly) {
            item.setIcon(R.drawable.ic_sign_browser_grade_checked);
        } else {
            item.setIcon(R.drawable.ic_sign_browser_grade_no_stroke);
        }
        final SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected " + hashCode());
        if (item.getItemId() == R.id.action_toggle_starred) {
            if (!this.showStarredOnly) {
                this.showStarredOnly = true;
                item.setIcon(R.drawable.ic_sign_browser_grade_checked);
            } else {
                this.showStarredOnly = false;
                item.setIcon(R.drawable.ic_sign_browser_grade_no_stroke);
            }
            this.loadSignsTask = new LoadSignsTask(getActivity());
            this.loadSignsTask.execute(this.showStarredOnly);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "onSaveInstance " + hashCode());
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_SHOW_STARRED_ONLY, this.showStarredOnly);
    }

    @Override
    public void onSignBrowserAdapterSignClicked(Sign sign) {
        Log.d(TAG, "onSignBrowserAdapterSignClicked " + hashCode());
        Validate.notNull(this.onSignBrowserSignClickedListener, "Parent activity has to implement the OnSignBrowserSignClickedListener");
        this.onSignBrowserSignClickedListener.onSignBrowserSignSelected(sign);

    }

    /**
     * Has to be implemented by parent activity.
     */
    public interface OnSignBrowserSignClickedListener {
        void onSignBrowserSignSelected(Sign sign);
    }

    /**
     * If the first parameter Boolean is true, only starred signs will be loaded.
     */
    private class LoadSignsTask extends AsyncTask<Boolean, Void, List<Sign>> {

        private final Context context;

        public LoadSignsTask(Context context) {
            this.context = context;
        }

        @Override
        protected List<Sign> doInBackground(Boolean... params) {
            Log.d(LoadSignsTask.class.getSimpleName(), "doInBackground " + hashCode());
            Validate.inclusiveBetween(0, 1, params.length, "Only null or one Boolean as a parameter allowed.");
            List<Sign> signs = new ArrayList<>();
            if (isCancelled()) {
                return signs;
            }
            final SignDAO signDAO = SignDAO.getInstance(this.context);
            signDAO.open();
            if (1 == params.length && params[0]) { // read starred signs only
                signs = signDAO.readStarredSignsOnly();
            } else {
                signs = signDAO.read();
            }
            signDAO.close();
            return signs;
        }
        @Override
        protected void onPostExecute(List<Sign> result) {
            Log.d(LoadSignsTask.class.getSimpleName(), "onPostExecute " + hashCode());
            final RecyclerView recyclerView = (RecyclerView) getActivity().findViewById(R.id.signRecyclerView);
            Validate.notNull(recyclerView, "RecyclerView is null");
            recyclerView.swapAdapter(new SignBrowserAdapter(SignBrowserFragment.this, getActivity(), result), true);
        }

    }

}


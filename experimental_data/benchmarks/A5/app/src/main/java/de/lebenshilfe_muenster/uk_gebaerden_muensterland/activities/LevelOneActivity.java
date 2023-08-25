package de.lebenshilfe_muenster.uk_gebaerden_muensterland.activities;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import de.lebenshilfe_muenster.uk_gebaerden_muensterland.R;
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
public class LevelOneActivity extends AppCompatActivity {

    public static final String FRAGMENT_TO_SHOW = "fragment_to_show";
    public static final String EXTRA = "extra";
    private static final String TAG = LevelOneActivity.class.getSimpleName();
    private static final String SIGN_VIDEO_TAG = "sign_video_tag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate()");
        super.onCreate(savedInstanceState);
        setupView();
        setupVideoFragment();
    }

    private void setupView() {
        setContentView(R.layout.level_one_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Validate.notNull(getSupportActionBar(), "SupportActionBar is null.");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.sign_viewer);
    }

    private void setupVideoFragment() {
        final Intent intent = getIntent();
        final Bundle bundle = intent.getBundleExtra(EXTRA);
        Validate.notNull(bundle, "The bundle supplied to the activity is null.");
        final String fragmentToShow = bundle.getString(FRAGMENT_TO_SHOW, StringUtils.EMPTY);
        if (SignVideoFragment.class.getSimpleName().equals(fragmentToShow)) {
            final Parcelable sign = bundle.getParcelable(SignVideoFragment.SIGN_TO_SHOW);
            SignVideoFragment signVideoFragment = new SignVideoFragment();
            final Bundle args = new Bundle();
            args.putParcelable(SignVideoFragment.SIGN_TO_SHOW, sign);
            signVideoFragment.setArguments(args);
            setFragment(signVideoFragment, SIGN_VIDEO_TAG);
        } else {
            throw new IllegalArgumentException("Cannot show the fragment with name: " + fragmentToShow);
        }
    }

    private void setFragment(Fragment fragment, String fragmentTag) {
        Log.d(TAG, "setFragment: " + fragmentTag);
        final FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frame_level_one, fragment, fragmentTag);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed() " + this.hashCode());
        finish();
    }

}

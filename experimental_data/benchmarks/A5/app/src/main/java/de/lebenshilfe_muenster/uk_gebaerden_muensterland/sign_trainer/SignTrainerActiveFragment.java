package de.lebenshilfe_muenster.uk_gebaerden_muensterland.sign_trainer;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.apache.commons.lang3.Validate;

import androidx.annotation.Nullable;
import de.lebenshilfe_muenster.uk_gebaerden_muensterland.R;
import de.lebenshilfe_muenster.uk_gebaerden_muensterland.database.Sign;
import de.lebenshilfe_muenster.uk_gebaerden_muensterland.sign_video_view.VideoSetupException;

import static de.lebenshilfe_muenster.uk_gebaerden_muensterland.sign_trainer.AbstractSignTrainerFragment.OnToggleLearningModeListener.LearningMode;

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
public class SignTrainerActiveFragment extends AbstractSignTrainerFragment {

    private static final String TAG = SignTrainerActiveFragment.class.getSimpleName();

    private TextView signQuestionTextDetail;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView " + hashCode());
        final View view = inflater.inflate(R.layout.trainer_active_fragment, container, false);
        setHasOptionsMenu(true);
        initializeQuestionViews(view);
        this.signQuestionTextDetail = (TextView) view.findViewById(R.id.signTrainerQuestionTextDetail);
        initializeExceptionViews(view);
        initializeAnswerViews(view);
        initializeVideoViews(view);
        this.questionViews = new View[]{this.signQuestionText, this.signQuestionTextDetail, this.solveQuestionButton};
        this.answerViews = new View[]{this.signAnswerTextView, this.videoView, this.signMnemonicTextView,
                this.signLearningProgressTextView, this.signHowHardWasQuestionTextView, this.signTrainerExplanationTextView,
                this.questionWasEasyButton, this.questionWasFairButton, this.questionWasHardButton};
        this.exceptionViews = new View [] {this.signTrainerExceptionMessageTextView};
        setVisibility(this.questionViews, View.VISIBLE);
        setVisibility(this.answerViews, View.GONE);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.d(TAG, "onActivityCreated " + hashCode());
        super.onActivityCreated(savedInstanceState);
        if (null != savedInstanceState) {
            final Sign parcelledSign = savedInstanceState.getParcelable(KEY_CURRENT_SIGN);
            if (null != parcelledSign) this.currentSign = parcelledSign;
            final Boolean answerVisible = savedInstanceState.getBoolean(KEY_ANSWER_VISIBLE);
            Validate.notNull(answerVisible, "AnswerVisible should always be non-null in savedInstance bundle.");
            if (answerVisible && (null != this.currentSign)) {
                setVisibility(this.questionViews, View.GONE);
                setVisibility(this.answerViews, View.VISIBLE);
                setAnswerTextViews();
                // just to make the SignTrainerActiveTest happy - was hard-coded in xml before.
                setQuestionTextViews(getString(R.string.howDoesThisSignLookLike));
                try {
                    setupVideoView(this.currentSign, SOUND.ON, CONTROLS.SHOW);
                } catch (VideoSetupException ex) {
                    handleVideoCouldNotBeLoaded(ex);
                }
            } else {
                setVisibility(this.questionViews, View.VISIBLE);
                setQuestionTextViews(getString(R.string.howDoesThisSignLookLike));
                if (null != this.currentSign) this.signQuestionTextDetail.setText(this.currentSign.getNameLocaleDe());
                setVisibility(this.answerViews, View.GONE);
            }
        } else {
            this.loadRandomSignTask = new LoadRandomSignTask(getActivity());
            this.loadRandomSignTask.execute(this.currentSign);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected " + hashCode());
        if (R.id.action_toggle_learning_mode == item.getItemId()) {
            this.onToggleLearningModeListener.toggleLearningMode(LearningMode.PASSIVE);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void handleClickOnSolveQuestionButton() {
        setVisibility(this.questionViews, View.GONE);
        setVisibility(this.answerViews, View.VISIBLE);
        setAnswerTextViews();
        try {
            setupVideoView(this.currentSign, SOUND.ON, CONTROLS.SHOW);
        } catch (VideoSetupException ex) {
            handleVideoCouldNotBeLoaded(ex);
        }
    }

    @Override
    protected void handleLoadRandomSignTaskOnPostExecute() {
        setVisibility(SignTrainerActiveFragment.this.questionViews, View.VISIBLE);
        setVisibility(SignTrainerActiveFragment.this.answerViews, View.GONE);
        setVisibility(SignTrainerActiveFragment.this.exceptionViews, View.GONE);
        setQuestionTextViews(getString(R.string.howDoesThisSignLookLike));
        this.signQuestionTextDetail.setText(this.currentSign.getNameLocaleDe());
    }

    @Override
    protected void handleVideoCouldNotBeLoaded(VideoSetupException videoSetupException) {
        this.signAnswerTextView.setText(R.string.videoError);
        this.signTrainerExceptionMessageTextView.setText(videoSetupException.getMessage());
        setVisibility(this.answerViews, View.VISIBLE);
        setVisibility(this.exceptionViews, View.VISIBLE);
        setVisibility(this.questionViews, View.GONE);
    }
}

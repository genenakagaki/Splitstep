package com.genenakagaki.splitstep.exercise.ui.coach;

import android.view.View;

import com.genenakagaki.splitstep.R;
import com.genenakagaki.splitstep.exercise.data.entity.Exercise;

import butterknife.OnClick;

/**
 * Created by Gene on 9/13/2017.
 */

public class RegularRepsCoachFragment extends CoachFragment {

    @Override
    public void setupExerciseSet(Exercise exercise) {
        mMainProgressTopText.setText(getString(R.string.reps_count, exercise.reps));
    }

    @Override
    public void startExerciseSet() {
        mDoneButton.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.done_button)
    public void onClickDone() {
        mDoneButton.setVisibility(View.INVISIBLE);
        onFinishExerciseSet();
    }
}

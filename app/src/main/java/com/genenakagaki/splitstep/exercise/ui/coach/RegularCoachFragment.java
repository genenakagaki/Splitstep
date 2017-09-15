package com.genenakagaki.splitstep.exercise.ui.coach;

import android.view.View;

import com.genenakagaki.splitstep.R;
import com.genenakagaki.splitstep.exercise.data.entity.Exercise;
import com.genenakagaki.splitstep.exercise.data.entity.ExerciseSubType;
import com.genenakagaki.splitstep.exercise.ui.model.DurationDisplayable;

import butterknife.OnClick;
import io.reactivex.functions.Consumer;

/**
 * Created by Gene on 9/13/2017.
 */

public class RegularCoachFragment extends CoachFragment {

    @Override
    public void startExerciseSet() {
        Exercise exercise = getViewModel().getExercise();

        switch (ExerciseSubType.fromValue(exercise.subType)) {
            case REPS:
                mMainProgressTopText.setText(getString(R.string.reps_count, exercise.reps));
                mDoneButton.setVisibility(View.VISIBLE);
                break;
            case TIMED_SETS:
                mTimedSetProgressBar.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void startRest() {
        getDisposable().add(getViewModel().getRestTimer().subscribe(new Consumer<DurationDisplayable>() {
            @Override
            public void accept(DurationDisplayable durationDisplayable) throws Exception {

            }
        }));
    }

    @Override
    public void cancelTimers() {

    }

    @OnClick(R.id.done_button)
    public void onClickDone() {
        startRest();
    }
}

package com.genenakagaki.splitstep.exercise.ui.coach;

import android.view.View;

import com.genenakagaki.splitstep.exercise.data.entity.Exercise;
import com.genenakagaki.splitstep.exercise.ui.model.DurationDisplayable;

import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * Created by Gene on 9/15/2017.
 */

public class RegularTimedSetsCoachFragment extends CoachFragment {

    private TimerViewModel mTimedSetsTimerViewModel;

    @Override
    public void setupExerciseSet(Exercise exercise) {
        mTimedSetProgressBar.setVisibility(View.VISIBLE);
        mTimedSetsTimerViewModel = new TimerViewModel(
                new DurationDisplayable(DurationDisplayable.TYPE_SET_DURATION, exercise.setDuration));
    }

    @Override
    public void startExerciseSet() {
        mTimedSetProgressBar.setProgress(mTimedSetsTimerViewModel.getMax());
        animateProgress(mTimedSetProgressBar, 0, mTimedSetsTimerViewModel.getAnimateDuration());
        mMainProgressText.setText(mTimedSetsTimerViewModel.getTimerDisplay());

        getDisposable().add(mTimedSetsTimerViewModel.startTimer().subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                mMainProgressText.setText(s);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
            }
        }, new Action() {
            @Override
            public void run() throws Exception {
                startExerciseSet();
            }
        }));
    }
}

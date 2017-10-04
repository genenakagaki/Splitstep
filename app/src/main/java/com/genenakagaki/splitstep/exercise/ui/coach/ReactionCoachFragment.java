package com.genenakagaki.splitstep.exercise.ui.coach;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.genenakagaki.splitstep.exercise.data.ExerciseSharedPref;
import com.genenakagaki.splitstep.exercise.data.entity.ReactionExercise;
import com.genenakagaki.splitstep.exercise.ui.model.DurationDisplayable;

import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * Created by Gene on 9/18/2017.
 */

public class ReactionCoachFragment extends CoachFragment {

    private ReactionCoachViewModel mReactionCoachViewModel;
    private TimerViewModel mTimedSetsTimerViewModel;
    private ConeViewModel mConeViewModel;
    private TimerViewModel mRepsTimerViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mReactionCoachViewModel = new ReactionCoachViewModel(
                getActivity(), ExerciseSharedPref.getExerciseId(getActivity()));
    }

    @Override
    public void onResume() {
        super.onResume();

        getDisposable().add(mReactionCoachViewModel.loadExercise().subscribe(new Consumer<ReactionExercise>() {
            @Override
            public void accept(ReactionExercise exercise) throws Exception {
            }
        }));
    }

    @Override
    public void setupExerciseSet() {
        switch (getViewModel().getExerciseSubType()) {
            case REPS:
//                mMainProgressTopText.setText(getString(R.string.reps_count, exercise.reps));
                break;
            case TIMED_SETS:
                int setDuration = getViewModel().getExercise().setDuration;

                mTimedSetsTimerViewModel = new TimerViewModel(
                        new DurationDisplayable(DurationDisplayable.TYPE_SET_DURATION, setDuration));
                mTimedSetProgressBar.setMax(mTimedSetsTimerViewModel.getMax());
                break;
        }

        mConeViewModel = new ConeViewModel(mReactionCoachViewModel.getExercise().cones);

        int repDuration = mReactionCoachViewModel.getExercise().repDuration;
        mRepsTimerViewModel = new TimerViewModel(
                new DurationDisplayable(DurationDisplayable.TYPE_REP_DURATION, repDuration));
    }

    @Override
    public void startExerciseSet() {
        switch (getViewModel().getExerciseSubType()) {
            case REPS:
                mDoneButton.setVisibility(View.VISIBLE);
                break;
            case TIMED_SETS:
                mTimedSetProgressBar.setVisibility(View.VISIBLE);
                mTimedSetProgressBar.setProgress(mTimedSetsTimerViewModel.getMax());
                animateProgress(mTimedSetProgressBar, 0, mTimedSetsTimerViewModel.getAnimateDuration());
                getDisposable().add(mConeViewModel.getNextCone().subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        mMainProgressText.setText(Integer.toString(integer));
                    }
                }));

                // Timer for set
                getDisposable().add(mTimedSetsTimerViewModel.startTimer().subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        mTimedSetProgressBar.setVisibility(View.INVISIBLE);
                        onFinishExerciseSet();
                    }
                }));

                // Timer for rep
                getDisposable().add(mRepsTimerViewModel.startTimer().subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        getDisposable().add(mConeViewModel.getNextCone().subscribe(new Consumer<Integer>() {
                            @Override
                            public void accept(Integer integer) throws Exception {
                                mMainProgressText.setText(Integer.toString(integer));
                            }
                        }));

                        mRepsTimerViewModel.startTimer();
                    }
                }));
                break;
        }
    }
}

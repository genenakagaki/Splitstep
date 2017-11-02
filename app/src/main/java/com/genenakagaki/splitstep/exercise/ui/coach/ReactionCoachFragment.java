package com.genenakagaki.splitstep.exercise.ui.coach;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.view.View;

import com.genenakagaki.splitstep.exercise.data.entity.ReactionExercise;
import com.genenakagaki.splitstep.exercise.ui.model.DurationDisplayable;

import java.util.Locale;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import timber.log.Timber;

/**
 * Created by Gene on 9/18/2017.
 */

public class ReactionCoachFragment extends CoachFragment {

    public static CoachFragment newInstance(long exerciseId) {
        return CoachFragment.newInstance(exerciseId, new ReactionCoachFragment());
    }

    private ReactionCoachViewModel mReactionCoachViewModel;
    private TimerViewModel mTimedSetsTimerViewModel;
    private ConeViewModel mConeViewModel;
    private RepTimerViewModel mRepTimerViewModel;

    private Disposable mRepTimerDisposable;

    private TextToSpeech mTextToSpeech;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            long exerciseId = getViewModel().getExerciseId();
            mReactionCoachViewModel = new ReactionCoachViewModel(getActivity(), exerciseId);
        }

        mTextToSpeech = new TextToSpeech(getActivity(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (i != TextToSpeech.ERROR) {
                    mTextToSpeech.setLanguage(Locale.JAPAN);
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        addDisposable(mReactionCoachViewModel.loadExercise().subscribe(new Consumer<ReactionExercise>() {
            @Override
            public void accept(ReactionExercise exercise) throws Exception {
            }
        }));
    }

    @Override
    public void setupExerciseSet() {
        mConeViewModel = new ConeViewModel(mReactionCoachViewModel.getExercise().cones);

        int repDuration = mReactionCoachViewModel.getExercise().repDuration;
        Timber.d("repDuration" + repDuration);
        mRepTimerViewModel = new RepTimerViewModel(
                new DurationDisplayable(DurationDisplayable.TYPE_REP_DURATION, repDuration),
                getViewModel().getExercise());

        switch (getViewModel().getExerciseSubType()) {
            case REPS:
                mSetProgressBar.setMax(mRepTimerViewModel.getMax());
//                mMainProgressTopText.setText(getString(R.string.reps_count, exercise.reps));
                break;
            case TIMED_SETS:
                int setDuration = getViewModel().getExercise().setDuration;

                mTimedSetsTimerViewModel = new TimerViewModel(
                        new DurationDisplayable(DurationDisplayable.TYPE_SET_DURATION, setDuration));
                mSetProgressBar.setMax(mTimedSetsTimerViewModel.getMax());
                break;
        }
    }

    @Override
    public void startExerciseSet() {
        mSetProgressBar.setVisibility(View.VISIBLE);

        switch (getViewModel().getExerciseSubType()) {
            case REPS:
                mSetProgressBar.setProgress(mRepTimerViewModel.getMax());
                showNextCone();

                // Timer for rep
                addDisposable(mRepTimerViewModel.startInterval().subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        showNextCone();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        onFinishExerciseSet();
                    }
                }));
                break;
            case TIMED_SETS:
                mSetProgressBar.setProgress(mTimedSetsTimerViewModel.getMax());
                animateProgress(mSetProgressBar, 0, mTimedSetsTimerViewModel.getAnimateDuration());
                addDisposable(mConeViewModel.getNextCone().subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        mTextToSpeech.speak(Integer.toString(integer), TextToSpeech.QUEUE_FLUSH, null);
                        mMainProgressText.setText(Integer.toString(integer));
                    }
                }));

                // Timer for set
                addDisposable(mTimedSetsTimerViewModel.startTimer().subscribe(new Consumer<String>() {
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
                        mRepTimerDisposable.dispose();
                        onFinishExerciseSet();
                    }
                }));

                // Timer for rep
                mRepTimerDisposable = mRepTimerViewModel.startInterval().subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        mConeViewModel.getNextCone().subscribe(new Consumer<Integer>() {
                            @Override
                            public void accept(Integer integer) throws Exception {
                                mTextToSpeech.speak(Integer.toString(integer), TextToSpeech.QUEUE_FLUSH, null);
                                mMainProgressText.setText(Integer.toString(integer));
                            }
                        });
                    }
                });

                addDisposable(mRepTimerDisposable);
                break;
        }
    }

    @Override
    public void onFinishExerciseSet() {
        mSetProgressBar.setVisibility(View.INVISIBLE);

        super.onFinishExerciseSet();
    }

    private void showNextCone() {
        addDisposable(mConeViewModel.getNextCone().subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                mTextToSpeech.speak(Integer.toString(integer), TextToSpeech.QUEUE_FLUSH, null);
                mMainProgressText.setText(Integer.toString(integer));
                animateProgress(mSetProgressBar, mRepTimerViewModel.getCurrentProgress(), mRepTimerViewModel.getAnimateDuration());
            }
        }));
    }
}

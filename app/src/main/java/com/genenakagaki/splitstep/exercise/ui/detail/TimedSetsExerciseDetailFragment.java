package com.genenakagaki.splitstep.exercise.ui.detail;

import com.genenakagaki.splitstep.exercise.ui.model.DurationDisplayable;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by gene on 8/21/17.
 */

public class TimedSetsExerciseDetailFragment extends ExerciseDetailFragment {

    private CompositeDisposable mDisposable;

    public TimedSetsExerciseDetailFragment() {}

    @Override
    public void setDuration(DurationDisplayable durationDisplayable) {

    }

    @Override
    public void onResume() {
        super.onResume();
        mDisposable = new CompositeDisposable();

//        mDisposable.add(getViewModel().getTimedSetsExercise()
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.io())
//                .subscribe(new Consumer<TimedSetsExercise>() {
//                    @Override
//                    public void accept(TimedSetsExercise timedSetsExercise) throws Exception {
//                        mSetsNumberInput.setNumber(timedSetsExercise.sets);
//                        mSetDurationTextView.setText(getViewModel().getDisplayableDuration(timedSetsExercise.setRestDuration));
//                        mRestDurationTextView.setText(getViewModel().getDisplayableDuration(timedSetsExercise.restDuration));
//                    }
//                }));
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
    }

    @Override
    public void createViewModel(long exerciseId) {

    }

    @Override
    public void startCoachFragment() {
//        TimedSetsExercise timedSetsExercise = new TimedSetsExercise(getViewModel().getExercise().id);
//        timedSetsExercise.sets = mSetsNumberInput.getInputText();
//        timedSetsExercise.setRestDuration = getViewModel().getDuration(mSetDurationTextView.getText().toString());
//        timedSetsExercise.restDuration = getViewModel().getDuration(mRestDurationTextView.getText().toString());
//
//        mDisposable.add(getViewModel().updateTimedSetsExercise(timedSetsExercise)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.computation())
//                .subscribe(new Action() {
//                    @Override
//                    public void run() throws Exception {
//                        ((MainActivity)getActivity()).showFragment(
//                                new TimedSetsCoachFragment(), TimedSetsCoachFragment.class.getSimpleName(), true);
//                    }
//                }));
    }

    @Override
    public void onBackPressed() {

    }
}

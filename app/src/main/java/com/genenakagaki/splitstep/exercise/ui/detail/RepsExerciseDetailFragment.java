package com.genenakagaki.splitstep.exercise.ui.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.genenakagaki.splitstep.exercise.data.ExerciseSharedPref;
import com.genenakagaki.splitstep.exercise.data.entity.Exercise;
import com.genenakagaki.splitstep.exercise.data.entity.RepsExercise;
import com.genenakagaki.splitstep.exercise.ui.model.DurationDisplayable;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by gene on 8/21/17.
 */

public class RepsExerciseDetailFragment extends ExerciseDetailFragment {

    private CompositeDisposable mDisposable;
    private RepsExerciseDetailViewModel mViewModel;

    public RepsExerciseDetailFragment() {}


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewModel = new RepsExerciseDetailViewModel(getActivity(), ExerciseSharedPref.getExerciseId(getActivity()));
    }

    @Override
    public void onResume() {
        super.onResume();
        mSetDurationLayout.setVisibility(View.GONE);

        mDisposable = new CompositeDisposable();

        mDisposable.add(mViewModel.getExerciseSubject()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.computation())
                .subscribe(new Consumer<Exercise>() {
                    @Override
                    public void accept(Exercise exercise) throws Exception {
                        getActivity().setTitle(exercise.name);
                    }
                }));
        mViewModel.setExercise();

        mDisposable.add(mViewModel.getRepsExerciseSubject()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.computation())
                .subscribe(new Consumer<RepsExercise>() {
                    @Override
                    public void accept(RepsExercise repsExercise) throws Exception {
                        mSetsNumberInput.setNumber(repsExercise.sets);
                        mRepsNumberInput.setNumber(repsExercise.reps);
//                        mRestDurationTextView.setText(
//                                getViewModel().getDisplayableDuration(repsExercise.restDuration));
                    }
                }));
        mViewModel.setRepsExercise();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
    }

    @Override
    public void setDuration(DurationDisplayable durationDisplayable) {

    }

    @Override
    public void startCoachFragment() {
        RepsExercise repsExercise = new RepsExercise(mViewModel.getExerciseId());
        repsExercise.reps = mRepsNumberInput.getNumber();
        repsExercise.sets = mSetsNumberInput.getNumber();
//        repsExercise.restDuration = mViewModel.getDuration(mRestDurationTextView.getText().toString());

//        mDisposable.add(getViewModel().updateRepsExercise(repsExercise)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.computation())
//                .subscribe(new Action() {
//                    @Override
//                    public void run() throws Exception {
//                        ((MainActivity)getActivity()).showFragment(
//                                new RepsCoachFragment(), RepsCoachFragment.class.getSimpleName(), true);
//                    }
//                }));
    }
}

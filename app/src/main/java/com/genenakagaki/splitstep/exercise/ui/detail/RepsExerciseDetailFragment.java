package com.genenakagaki.splitstep.exercise.ui.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.genenakagaki.splitstep.R;
import com.genenakagaki.splitstep.exercise.data.entity.RegularExercise;
import com.genenakagaki.splitstep.exercise.ui.model.DurationDisplayable;

import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by gene on 8/21/17.
 */

public class RepsExerciseDetailFragment extends ExerciseDetailFragment {

    private RepsExerciseDetailViewModel mViewModel;

    public RepsExerciseDetailFragment() {
    }

//    @Override
//    public void createViewModel(long exerciseId) {
//        mViewModel = new RepsExerciseDetailViewModel(getActivity(), exerciseId);
//    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Timber.d("onCreateView");
        View view = super.onCreateView(inflater, container, savedInstanceState);

        mSetDurationLayout.setVisibility(View.GONE);

        mRepsNumberInput.setOnInputChangedListener(this);
        mSetsNumberInput.setOnInputChangedListener(this);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getDisposable().add(mViewModel.getRepsExerciseSubject()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<RegularExercise>() {
                    @Override
                    public void accept(RegularExercise regularExercise) throws Exception {
                        mSetsNumberInput.setNumber(regularExercise.sets);
                        mRepsNumberInput.setNumber(regularExercise.reps);
                    }
                }));

        getDisposable().add(mViewModel.getRestDurationSubject()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<DurationDisplayable>() {
                    @Override
                    public void accept(DurationDisplayable durationDisplayable) throws Exception {
                        mRestDurationTextView.setText(durationDisplayable.getDisplay());
                    }
                }));

        getDisposable().add(mViewModel.setRepsExercise()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.computation())
                .subscribe());
    }

    @OnClick(R.id.rest_duration_layout)
    public void onClickRestDuration() {
        DurationPickerDialog fragment =
                DurationPickerDialog.newInstance(mViewModel.getRestDurationDisplayable());
        fragment.show(getFragmentManager(), DurationPickerDialog.class.getSimpleName());
    }

    @Override
    public void setDuration(DurationDisplayable durationDisplayable) {
        mViewModel.setRestDuration(durationDisplayable)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.computation())
                .subscribe();
    }

    @Override
    public void startCoachFragment() {
//        RegularExercise repsExercise = new RegularExercise(mViewModel.getExerciseId());
//        repsExercise.reps = mRepsNumberInput.getNumber();
//        repsExercise.sets = mSetsNumberInput.getNumber();
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

    @Override
    public void onInputChanged(View view, int number) {
        Timber.d("onInputChanged " + number);
        if (view.getId() == mRepsNumberInput.getId()) {
            Timber.d("reps");
            mViewModel.setReps(number)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.computation())
                    .subscribe();
        } else if (view.getId() == mSetsNumberInput.getId()) {
            Timber.d("sets");
            mViewModel.setSets(number)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.computation())
                    .subscribe();
        }
    }
}

package com.genenakagaki.splitstep.exercise.ui.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.genenakagaki.splitstep.R;
import com.genenakagaki.splitstep.exercise.data.ExerciseSharedPref;
import com.genenakagaki.splitstep.exercise.data.entity.Exercise;
import com.genenakagaki.splitstep.exercise.ui.ExerciseActivity;
import com.genenakagaki.splitstep.exercise.ui.model.DurationDisplayable;
import com.genenakagaki.splitstep.exercise.ui.view.NumberInput;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by Gene on 9/8/2017.
 */

public abstract class ExerciseDetailFragment extends Fragment implements ExerciseActivity.OnBackPressedListener {

    @BindView(R.id.exercise_name_textview)
    TextView mExerciseNameTextView;
    @BindView(R.id.sets_numberinput)
    NumberInput mSetsNumberInput;
    @BindView(R.id.reps_numberinput) NumberInput mRepsNumberInput;
    @BindView(R.id.set_duration_layout)
    LinearLayout mSetDurationLayout;
    @BindView(R.id.set_duration_textview) TextView mSetDurationTextView;
    @BindView(R.id.rest_duration_layout) LinearLayout mRestDurationLayout;
    @BindView(R.id.rest_duration_textview) TextView mRestDurationTextView;

    private Unbinder mUnbinder;
    private CompositeDisposable mDisposable;
    private ExerciseDetailViewModel mViewModel;

    public ExerciseDetailFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ExerciseActivity activity = (ExerciseActivity) getActivity();
        activity.setOnBackPressedListener(this);

        long exerciseId = ExerciseSharedPref.getExerciseId(getActivity());
        mViewModel = new ExerciseDetailViewModel(getActivity(), exerciseId);
        createViewModel(exerciseId);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Timber.d("onCreateView");
        View view = inflater.inflate(R.layout.fragment_exercise_detail, container, false);
        mUnbinder = ButterKnife.bind(this, view);

        if (mRepsNumberInput == null) {
            Timber.d("number input is null");
        } else {
            Timber.d("number input is not null");
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mDisposable = new CompositeDisposable();

        mDisposable.add(mViewModel.getExerciseSubject()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.computation())
                .subscribe(new Consumer<Exercise>() {
                    @Override
                    public void accept(Exercise exercise) throws Exception {
                        mExerciseNameTextView.setText(exercise.name);
                    }
                }));
        mDisposable.add(mViewModel.setExercise()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.computation())
                .subscribe());
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
    }

    @Override
    public void onDestroyView() {
        Timber.d("onDestroyView");
        super.onDestroyView();
        mUnbinder.unbind();
    }

    public CompositeDisposable getDisposable() {
        return mDisposable;
    }

    public abstract void createViewModel(long exerciseId);
    public abstract void setDuration(DurationDisplayable durationDisplayable);
    public abstract void startCoachFragment();
}

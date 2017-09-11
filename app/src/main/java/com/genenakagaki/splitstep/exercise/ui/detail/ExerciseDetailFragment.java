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
import com.genenakagaki.splitstep.exercise.ui.model.DurationDisplayable;
import com.genenakagaki.splitstep.exercise.ui.view.NumberInput;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Gene on 9/8/2017.
 */

public abstract class ExerciseDetailFragment extends Fragment {

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

    public ExerciseDetailFragment() {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exercise_detail, container, false);
        mUnbinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    @OnClick({R.id.rest_duration_layout, R.id.set_duration_layout})
    public void onClickSetDurationLayout(View view) {
        int duration;
        int durationType;

//        switch (view.getId()) {
//            case R.id.rest_duration_layout:
//                duration = mViewModel.getDuration(mSetDurationTextView.getText().toString());
//                durationType = DurationPickerDialog.REST_DURATION;
//                break;
//            default: // TimedExerciseDao.id.set_duration_layout:
//                duration = mViewModel.getDuration(mRestDurationTextView.getText().toString());
//                durationType = DurationPickerDialog.SET_DURATION;
//        }

//        DurationPickerDialog fragment =
//                DurationPickerDialog.newInstance(
//                        getString(R.string.rest_duration), durationType, duration);
//        fragment.show(getFragmentManager(), DurationPickerDialog.class.getSimpleName());
    }

    public abstract void setDuration(DurationDisplayable durationDisplayable);

    public abstract void startCoachFragment();
}

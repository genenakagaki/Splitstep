package com.genenakagaki.splitstep.exercise.ui.detail;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.genenakagaki.splitstep.R;
import com.genenakagaki.splitstep.exercise.ui.view.NumberInput;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Gene on 9/8/2017.
 */

public class ExerciseDetailFragment extends Fragment {

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
    private ExerciseDetailViewModel mViewModel;

    public ExerciseDetailFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewModel = new ExerciseDetailViewModel(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exercise_detail, container, false);
        mUnbinder = ButterKnife.bind(this, view);

        mExerciseNameTextView = ButterKnife.findById(view, R.id.exercise_name_textview);
        mSetDurationLayout    = ButterKnife.findById(view, R.id.set_duration_layout);
        mSetDurationTextView  = ButterKnife.findById(view, R.id.set_duration_textview);
        mRestDurationLayout   = ButterKnife.findById(view, R.id.rest_duration_layout);
        mRestDurationTextView = ButterKnife.findById(view, R.id.rest_duration_textview);

        MainActivity activity = (MainActivity) getActivity();

        activity.getFab().setVisibility(View.GONE);
        FrameLayout startExerciseLayout = activity.getStartExerciseLayout();
        startExerciseLayout.setVisibility(View.VISIBLE);
        startExerciseLayout.getChildAt(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startCoachFragment();
            }
        });

        mExerciseNameTextView.setText(mViewModel.getExercise().name);

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

        switch (view.getId()) {
            case R.id.rest_duration_layout:
                duration = mViewModel.getDuration(mSetDurationTextView.getText().toString());
                durationType = DurationPickerDialog.REST_DURATION;
                break;
            default: // R.id.set_duration_layout:
                duration = mViewModel.getDuration(mRestDurationTextView.getText().toString());
                durationType = DurationPickerDialog.SET_DURATION;
        }

        DurationPickerDialog fragment =
                DurationPickerDialog.newInstance(
                        getString(R.string.rest_duration), durationType, duration);
        fragment.show(getFragmentManager(), DurationPickerDialog.class.getSimpleName());
    }

    public void setSetDuration(int minutes, int seconds) {
        if (minutes == 0) {
            mSetDurationTextView.setText(getString(R.string.duration_value_seconds, seconds));
        } else {
            mSetDurationTextView.setText(getString(R.string.duration_value, minutes, seconds));
        }
    }

    public void setRestDuration(int minutes, int seconds) {
        if (minutes == 0) {
            mRestDurationTextView.setText(getString(R.string.duration_value_seconds, seconds));
        } else {
            mRestDurationTextView.setText(getString(R.string.duration_value, minutes, seconds));
        }
    }

    public ExerciseDetailViewModel getViewModel() {
        return mViewModel;
    }

    public abstract void startCoachFragment();
}

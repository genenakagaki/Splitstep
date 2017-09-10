package com.genenakagaki.splitstep.exercise.ui.detail;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.genenakagaki.splitstep.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import timber.log.Timber;

/**
 * Created by gene on 8/24/17.
 */

public class DurationPickerDialog extends DialogFragment {

    public static final int SET_DURATION = 0;
    public static final int REST_DURATION = 1;

    private static final String ARG_DURATION_TYPE = "ARG_DURATION_TYPE";
    private static final String ARG_DURATION = "ARG_DURATION";
    private static final String ARG_TITLE = "ARG_TITLE";

    @BindView(R.id.colon_picker) NumberPicker mColonPicker;
    @BindView(R.id.minutes_picker) NumberPicker mMinutesPicker;
    @BindView(R.id.seconds_picker) NumberPicker mSecondsPicker;
    @BindView(R.id.error_textview) TextView mErrorTextView;

    private Unbinder mUnbinder;
    private DurationPickerViewModel mViewModel;

    public static DurationPickerDialog newInstance(String title, int durationType, int duration) {
        DurationPickerDialog dialog = new DurationPickerDialog();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putInt(ARG_DURATION, duration);
        args.putInt(ARG_DURATION_TYPE, durationType);
        dialog.setArguments(args);
        return dialog;
    }

    public DurationPickerDialog() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String title = getArguments().getString(ARG_TITLE);
        int durationType = getArguments().getInt(ARG_DURATION_TYPE);
        int duration = getArguments().getInt(ARG_DURATION);

        mViewModel = new DurationPickerViewModel(getActivity(), title, duration, durationType);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_duration_picker, null);
        mUnbinder = ButterKnife.bind(this, view);

        mColonPicker.setDisplayedValues(mViewModel.COLON_PICKER_DISPLAY_VALUES);
        mMinutesPicker.setMaxValue(59);
        mSecondsPicker.setMaxValue(59);
        mMinutesPicker.setDisplayedValues(mViewModel.PICKER_DISPLAY_VALUE);
        mSecondsPicker.setDisplayedValues(mViewModel.PICKER_DISPLAY_VALUE);
        mMinutesPicker.setValue(mViewModel.getMinutes());
        mSecondsPicker.setValue(mViewModel.getSeconds());

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(mViewModel.getTitle())
                .setView(view)
                .setPositiveButton(R.string.save, null)
                .setNegativeButton(R.string.cancel, null);
        return builder.create();
    }

    @Override
    public void onStart() {
        // to validate input, prevent dialog to be dismissed right after button is pressed
        super.onStart();
        final AlertDialog dialog = (AlertDialog) getDialog();
        if (dialog != null) {
            Button positiveButton = dialog.getButton(Dialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Timber.d("OnClick BUTTON_POSITIVE");

//                    if (mSecondsPicker.getValue() == 0 && mMinutesPicker.getValue() == 0) {
//                        mErrorTextView.setVisibility(View.VISIBLE);
//                    } else {
//                        ExerciseType exerciseType = ((MainActivity) getActivity()).getExerciseType();
//                        switch (exerciseType) {
//                            case REPS:
//                                setDuration(RepsExerciseDetailFragment.class.getSimpleName());
//                                break;
//                            case TIMED_SETS:
//                                setDuration(TimedSetsExerciseDetailFragment.class.getSimpleName());
//                                break;
//                        }
//
//                        dialog.dismiss();
//                    }
                }
            });
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    private void setDuration(String fragmentTag) {
//        switch (mDurationType) {
//            case SET_DURATION:
//                ExerciseDetailFragment fragment = (ExerciseDetailFragment) getFragmentManager()
//                        .findFragmentByTag(fragmentTag);
//                fragment.setSetDuration(mMinutesPicker.getValue(), mSecondsPicker.getValue());
//                break;
//            case REST_DURATION:
//                fragment = (ExerciseDetailFragment) getFragmentManager()
//                        .findFragmentByTag(fragmentTag);
//                fragment.setRestDuration(mMinutesPicker.getValue(), mSecondsPicker.getValue());
//                break;
//        }
    }

}

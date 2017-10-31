package com.genenakagaki.splitstep.exercise.ui.detail;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.genenakagaki.splitstep.R;
import com.genenakagaki.splitstep.base.BaseDialogFragment;
import com.genenakagaki.splitstep.exercise.ui.model.DurationDisplayable;
import com.genenakagaki.splitstep.exercise.ui.model.ErrorMessage;

import org.parceler.Parcels;

import butterknife.BindView;
import io.reactivex.functions.Consumer;
import timber.log.Timber;

/**
 * Created by gene on 8/24/17.
 */

public class DurationPickerDialog extends BaseDialogFragment {

    private static final String ARG_DURATION_DISPLAYABLE = "ARG_DURATION_DISPLAYABLE";

    @BindView(R.id.colon_picker)
    NumberPicker mColonPicker;
    @BindView(R.id.minutes_picker)
    NumberPicker mMinutesPicker;
    @BindView(R.id.seconds_picker)
    NumberPicker mSecondsPicker;
    @BindView(R.id.error_textview)
    TextView mErrorTextView;

    private DurationPickerViewModel mViewModel;

    public static DurationPickerDialog newInstance(DurationDisplayable durationDisplayable) {
        DurationPickerDialog dialog = new DurationPickerDialog();
        Bundle args = new Bundle();
        args.putParcelable(ARG_DURATION_DISPLAYABLE, Parcels.wrap(durationDisplayable));
        dialog.setArguments(args);
        return dialog;
    }

    public DurationPickerDialog() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Parcelable parcelable = getArguments().getParcelable(ARG_DURATION_DISPLAYABLE);
        DurationDisplayable durationDisplayable = Parcels.unwrap(parcelable);

        mViewModel = new DurationPickerViewModel(getActivity(), durationDisplayable);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_duration_picker, null);
        bindView(this, view);

        mColonPicker.setDisplayedValues(mViewModel.COLON_PICKER_DISPLAY_VALUES);
        mMinutesPicker.setMaxValue(mViewModel.PICKER_MAX_VALUE);
        mSecondsPicker.setMaxValue(mViewModel.PICKER_MAX_VALUE);
        mMinutesPicker.setDisplayedValues(mViewModel.PICKER_DISPLAY_VALUE);
        mSecondsPicker.setDisplayedValues(mViewModel.PICKER_DISPLAY_VALUE);
        DurationDisplayable durationDisplayable = mViewModel.getDurationDisplayable();
        mMinutesPicker.setValue(durationDisplayable.getMinutes());
        mSecondsPicker.setValue(durationDisplayable.getSeconds());

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(durationDisplayable.getTitle())
                .setView(view)
                .setPositiveButton(R.string.save, null)
                .setNegativeButton(R.string.cancel, null);
        return builder.create();
    }

    @Override
    public void onStart() {
        // to validateDuration input, prevent dialog to be dismissed right after button is pressed
        super.onStart();
        final AlertDialog dialog = (AlertDialog) getDialog();
        if (dialog != null) {
            Button positiveButton = dialog.getButton(Dialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Timber.d("OnClick BUTTON_POSITIVE");

                    mViewModel.validateDuration(mMinutesPicker.getValue(), mSecondsPicker.getValue());
                }
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        addDisposable(mViewModel.getErrorMessageSubject()
                .subscribe(new Consumer<ErrorMessage>() {
                    @Override
                    public void accept(ErrorMessage errorMessage) throws Exception {
                        if (errorMessage.isValid()) {
                            ExerciseDetailFragment fragment = (ExerciseDetailFragment) getFragmentManager()
                                    .findFragmentByTag(ExerciseDetailFragment.class.getSimpleName());
                            fragment.setDuration(mViewModel.getDurationDisplayable());
                            dismiss();
                        } else {
                            mErrorTextView.setVisibility(View.VISIBLE);
                        }
                    }
                }));
    }

}

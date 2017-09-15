package com.genenakagaki.splitstep.exercise.ui.list;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import com.genenakagaki.splitstep.R;
import com.genenakagaki.splitstep.exercise.data.ExerciseSharedPref;
import com.genenakagaki.splitstep.exercise.data.entity.ExerciseSubType;
import com.genenakagaki.splitstep.exercise.data.exception.ExerciseAlreadyExistsException;
import com.genenakagaki.splitstep.exercise.data.exception.InvalidExerciseNameException;
import com.genenakagaki.splitstep.exercise.ui.model.ErrorMessage;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import timber.log.Timber;

/**
 * Created by gene on 8/12/17.
 */

public class AddExerciseDialog extends DialogFragment {

    @BindView(R.id.name_input) TextInputEditText mExerciseNameInput;
    @BindView(R.id.name_inputlayout) TextInputLayout mExerciseNameInputLayout;
    @BindView(R.id.reps_radiobutton) RadioButton mRepsRadioButton;
    @BindView(R.id.timed_sets_radiobutton) RadioButton mTimedSetsRadioButton;

    private Unbinder mUnbinder;
    private CompositeDisposable mDisposable;
    private AddExerciseViewModel mViewModel;

    public AddExerciseDialog() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewModel = new AddExerciseViewModel(
                getActivity(), ExerciseSharedPref.getExerciseType(getActivity()));
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_add_exercise, null);
        mUnbinder = ButterKnife.bind(this, view);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.add_exercise_title))
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

                    enableSaveButton(false);
                    onSaveButtonClick();
                }
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mDisposable = new CompositeDisposable();

        mDisposable.add(mViewModel.getErrorMessageSubject()
                .subscribe(new Consumer<ErrorMessage>() {
                    @Override
                    public void accept(ErrorMessage errorMessage) throws Exception {
                        if (!errorMessage.isValid()) {
                            mExerciseNameInputLayout.setError(errorMessage.getErrorMessage());
                        }
                    }
                }));
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
        super.onDestroyView();
        mUnbinder.unbind();
    }

    @OnClick(R.id.reps_radiobutton)
    public void onClickRepsRadioButton() {
        mRepsRadioButton.setChecked(true);
        mViewModel.setExerciseSubType(ExerciseSubType.REPS);
    }

    @OnClick(R.id.timed_sets_radiobutton)
    public void onClickTimedSetsButton() {
        mTimedSetsRadioButton.setChecked(true);
        mViewModel.setExerciseSubType(ExerciseSubType.TIMED_SETS);
    }

    public void onSaveButtonClick() {
        final String exerciseName = mExerciseNameInput.getText().toString();

        mDisposable.add(mViewModel.insertExercise(exerciseName)
                .subscribe(new Action() {
                    @Override
                    public void run() throws Exception {
                        onExerciseInserted();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if (throwable instanceof InvalidExerciseNameException) {
                            mViewModel.setInvalidExerciseNameError();
                        } else if (throwable instanceof ExerciseAlreadyExistsException) {
                            mViewModel.setExerciseAlreadyExistsError();
                        }

                        enableSaveButton(true);
                    }
                }));
    }

    public void onExerciseInserted() {
        ExerciseListFragment fragment = (ExerciseListFragment) getFragmentManager()
                .findFragmentByTag(ExerciseListFragment.class.getSimpleName());
        fragment.getExerciseList();
        getDialog().dismiss();
    }

    public void enableSaveButton(boolean enable) {
        AlertDialog dialog = (AlertDialog) getDialog();

        if (dialog != null) {
            Button positiveButton = dialog.getButton(Dialog.BUTTON_POSITIVE);
            positiveButton.setEnabled(enable);
        }
    }
}

package com.genenakagaki.splitstep.exercise.ui.add;


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

import com.genenakagaki.splitstep.R;
import com.genenakagaki.splitstep.exercise.data.ExerciseSharedPref;
import com.genenakagaki.splitstep.exercise.ui.list.ExerciseListFragment;
import com.genenakagaki.splitstep.exercise.ui.model.ValidationModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by gene on 8/12/17.
 */

public class AddExerciseDialog extends DialogFragment {

    @BindView(R.id.name_input) TextInputEditText mExerciseNameInput;
    @BindView(R.id.name_inputlayout) TextInputLayout mExerciseNameInputLayout;

    private Unbinder mUnbinder;
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
        // to validate input, prevent dialog to be dismissed right after button is pressed
        super.onStart();
        final AlertDialog dialog = (AlertDialog) getDialog();
        if (dialog != null) {
            Button positiveButton = dialog.getButton(Dialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Timber.d("OnClick BUTTON_POSITIVE");

                    enableSaveButton(false);
                    validateExerciseName();
                }
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mViewModel.initDisposable();
    }

    @Override
    public void onPause() {
        super.onPause();

        CompositeDisposable disposable = mViewModel.getDisposable();
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    public void validateExerciseName() {
        final String exerciseName = mExerciseNameInput.getText().toString();

        mViewModel.getDisposable().add(mViewModel.validateExerciseName(exerciseName)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.computation())
                .subscribe(new Consumer<ValidationModel>() {
                    @Override
                    public void accept(ValidationModel validationModel) throws Exception {
                        if (validationModel.isValid()) {
                            insertExercise(exerciseName);
                        } else {
                            mExerciseNameInputLayout.setError(validationModel.getErrorMessage());
                            enableSaveButton(true);
                        }
                    }
                }));
    }

    public void insertExercise(String exerciseName) {
        mViewModel.getDisposable().add(mViewModel.insertExercise(exerciseName)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Action() {
                    @Override
                    public void run() throws Exception {
                        ExerciseListFragment fragment = (ExerciseListFragment) getFragmentManager()
                                .findFragmentByTag(ExerciseListFragment.class.getSimpleName());
                        fragment.getViewModel().getExerciseList();

                        getDialog().dismiss();
                    }
                }));
    }

    public void enableSaveButton(boolean enable) {
        AlertDialog dialog = (AlertDialog) getDialog();

        if (dialog != null) {
            Button positiveButton = dialog.getButton(Dialog.BUTTON_POSITIVE);
            positiveButton.setEnabled(enable);
        }
    }
}

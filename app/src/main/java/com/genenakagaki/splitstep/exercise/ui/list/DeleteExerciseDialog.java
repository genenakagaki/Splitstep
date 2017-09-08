package com.genenakagaki.splitstep.exercise.ui.list;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.genenakagaki.splitstep.R;
import com.genenakagaki.splitstep.exercise.data.ExerciseSharedPref;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by gene on 9/7/17.
 */

public class DeleteExerciseDialog extends DialogFragment {

    private static final String ARG_EXERCISE_ID = "ARG_EXERCISE_ID";
    private static final String ARG_DELETE_MESSAGE = "ARG_DELETE_MESSAGE";

    public static DeleteExerciseDialog newInstance(long exerciseId, String deleteMessage) {
        DeleteExerciseDialog dialog = new DeleteExerciseDialog();
        Bundle args = new Bundle();
        args.putLong(ARG_EXERCISE_ID, exerciseId);
        args.putString(ARG_DELETE_MESSAGE, deleteMessage);
        dialog.setArguments(args);
        return dialog;
    }

    private CompositeDisposable mDisposable;
    private DeleteExerciseViewModel mViewModel;

    public DeleteExerciseDialog() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        long exerciseId = getArguments().getLong(ARG_EXERCISE_ID);
        String deleteMessage = getArguments().getString(ARG_DELETE_MESSAGE);
        mViewModel = new DeleteExerciseViewModel(exerciseId, ExerciseSharedPref.getExerciseType(getActivity()), deleteMessage);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.delete_exercise_title))
                .setMessage(mViewModel.getDeleteMessage())
                .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        onDeleteButtonClick();
                    }
                })
                .setNegativeButton(R.string.cancel, null);
        return builder.create();
    }

    private void onDeleteButtonClick() {
        mDisposable.add(mViewModel.deleteExerciseCompletable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.computation())
                .subscribe(new Action() {
                    @Override
                    public void run() throws Exception {
                        ExerciseListFragment fragment =
                                (ExerciseListFragment) getFragmentManager().findFragmentByTag(ExerciseListFragment.class.getSimpleName());
                        fragment.getViewModel().getExerciseList()
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribeOn(Schedulers.computation())
                                .subscribe();
                    }
                }));
    }

    @Override
    public void onResume() {
        super.onResume();
        mDisposable = new CompositeDisposable();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
    }
}

package com.genenakagaki.splitstep.exercise.ui.list;

import android.content.Context;
import android.support.annotation.NonNull;

import com.genenakagaki.splitstep.R;
import com.genenakagaki.splitstep.exercise.data.ExerciseDatabase;
import com.genenakagaki.splitstep.exercise.data.entity.Exercise;
import com.genenakagaki.splitstep.exercise.data.entity.ExerciseType;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.raizlabs.android.dbflow.structure.database.transaction.ITransaction;
import com.raizlabs.android.dbflow.structure.database.transaction.Transaction;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.subjects.BehaviorSubject;

/**
 * Created by gene on 9/7/17.
 */

public class ExerciseListItemViewModel {

    private Context mContext;
    private CompositeDisposable mDisposable;
    private Exercise mExercise;

    private BehaviorSubject<Exercise> mExerciseSubject = BehaviorSubject.create();

    public ExerciseListItemViewModel(Context context, Exercise exercise) {
        mContext = context;
        mExercise = exercise;
        mExerciseSubject.onNext(exercise);
    }

    public CompositeDisposable getDisposable() {
        return mDisposable;
    }

    public void initDisposable() {
        mDisposable = new CompositeDisposable();
    }

    public Exercise getExercise() {
        return mExercise;
    }

    public BehaviorSubject<Exercise> getExerciseSubject() {
        return mExerciseSubject;
    }

    public void toggleExerciseFavorite() {
        FlowManager.getDatabase(ExerciseDatabase.class).beginTransactionAsync(new ITransaction() {
            @Override
            public void execute(DatabaseWrapper databaseWrapper) {
                mExercise.favorite = !mExercise.favorite;
                mExercise.update();
            }
        }).success(new Transaction.Success() {
            @Override
            public void onSuccess(@NonNull Transaction transaction) {
                mExerciseSubject.onNext(mExercise);
            }
        }).build().execute();
    }

    public String getDeleteMessage() {
        String exerciseTypeString;

        switch (mExercise.type) {
            case ExerciseType.REPS_VALUE:
                exerciseTypeString = mContext.getString(R.string.reps_exercise_short);
                break;
            case ExerciseType.TIMED_SETS_VALUE:
                exerciseTypeString = mContext.getString(R.string.timed_sets_exercise_short);
                break;
            default: // REACTION:
                exerciseTypeString = mContext.getString(R.string.reaction_exercise_short);
        }

        return mExercise.name + " (" + exerciseTypeString + ")";
    }

}

package com.genenakagaki.splitstep.exercise.ui.list;

import android.content.Context;

import com.genenakagaki.splitstep.R;
import com.genenakagaki.splitstep.exercise.data.entity.Exercise;
import com.genenakagaki.splitstep.exercise.data.entity.ExerciseType;

import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.subjects.BehaviorSubject;

/**
 * Created by gene on 9/7/17.
 */

public class ExerciseListItemViewModel {

    private Context mContext;
    private Exercise mExercise;

    private BehaviorSubject<Exercise> mExerciseSubject = BehaviorSubject.create();

    public ExerciseListItemViewModel(Context context, Exercise exercise) {
        mContext = context;
        mExercise = exercise;
        mExerciseSubject.onNext(exercise);
    }

    public Exercise getExercise() {
        return mExercise;
    }

    public BehaviorSubject<Exercise> getExerciseSubject() {
        return mExerciseSubject;
    }

    public Completable toggleExerciseFavorite() {
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(@io.reactivex.annotations.NonNull CompletableEmitter e) throws Exception {
                mExercise.favorite = !mExercise.favorite;
                mExercise.update();
                mExerciseSubject.onNext(mExercise);
                e.onComplete();
            }
        });
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

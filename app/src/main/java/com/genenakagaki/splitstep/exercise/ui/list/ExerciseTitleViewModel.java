package com.genenakagaki.splitstep.exercise.ui.list;

import android.content.Context;

import com.genenakagaki.splitstep.R;
import com.genenakagaki.splitstep.base.BaseViewModel;
import com.genenakagaki.splitstep.exercise.data.entity.Exercise;
import com.genenakagaki.splitstep.exercise.data.entity.ExerciseSubType;

import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;

/**
 * Created by gene on 9/7/17.
 */

public class ExerciseTitleViewModel extends BaseViewModel {

    private Exercise exercise;

    private BehaviorSubject<Exercise> exerciseSubject;

    public ExerciseTitleViewModel(Context context, Exercise exercise) {
        this(context, exercise, null);
    }

    public ExerciseTitleViewModel(Context context, Exercise exercise, BehaviorSubject<Exercise> exerciseSubject) {
        super(context);
        this.exercise = exercise;
        if (this.exerciseSubject == null) {
            this.exerciseSubject = BehaviorSubject.create();
            this.exerciseSubject.onNext(exercise);
        } else {
            this.exerciseSubject = exerciseSubject;
        }
    }

    public Exercise getExercise() {
        return exercise;
    }

    public void setExercise(Exercise exercise) {
        this.exercise = exercise;
    }

    public Observable<Exercise> getExerciseSubject() {
        return exerciseSubject
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    public Completable toggleExerciseFavorite() {
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(@io.reactivex.annotations.NonNull CompletableEmitter e) throws Exception {
                exercise.favorite = !exercise.favorite;
                exercise.update();
                exerciseSubject.onNext(exercise);
                e.onComplete();
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.computation());
    }

    public String getExerciseDisplay() {
        String exerciseTypeString;

        switch (ExerciseSubType.fromValue(exercise.subType)) {
            case REPS:
                exerciseTypeString = getContext().getString(R.string.reps_subtype);
                break;
            default: // TIMED_SETS:
                exerciseTypeString = getContext().getString(R.string.timed_sets_subtype);
        }

        return exercise.name + "  (" + exerciseTypeString + ")";

    }

}

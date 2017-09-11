package com.genenakagaki.splitstep.exercise.ui.detail;

import android.content.Context;

import com.genenakagaki.splitstep.exercise.data.ExerciseDao;
import com.genenakagaki.splitstep.exercise.data.entity.Exercise;

import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.CompletableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.subjects.BehaviorSubject;

/**
 * Created by gene on 8/21/17.
 */

public class ExerciseDetailViewModel {

    private Context context;
    private long exerciseId;

    private Exercise exercise;
    private BehaviorSubject<Exercise> exerciseSubject = BehaviorSubject.create();

    public ExerciseDetailViewModel(Context context, long exerciseId) {
        this.context = context;
        this.exerciseId = exerciseId;
    }

    protected long getExerciseId() {
        return exerciseId;
    }

    public BehaviorSubject<Exercise> getExerciseSubject() {
        return exerciseSubject;
    }

    public Completable setExercise() {
        return ExerciseDao.getInstance().findById(exerciseId).flatMapCompletable(new Function<Exercise, CompletableSource>() {
            @Override
            public CompletableSource apply(@NonNull final Exercise exercise) throws Exception {
                return Completable.create(new CompletableOnSubscribe() {
                    @Override
                    public void subscribe(@NonNull CompletableEmitter e) throws Exception {
                        ExerciseDetailViewModel.this.exercise = exercise;
                        exerciseSubject.onNext(exercise);
                        e.onComplete();
                    }
                });
            }
        });
    }


}

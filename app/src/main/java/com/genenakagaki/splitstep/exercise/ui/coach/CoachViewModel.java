package com.genenakagaki.splitstep.exercise.ui.coach;

import android.content.Context;

import com.genenakagaki.splitstep.exercise.data.ExerciseDao;
import com.genenakagaki.splitstep.exercise.data.entity.Exercise;

import java.util.concurrent.TimeUnit;

import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.CompletableSource;
import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.subjects.BehaviorSubject;

/**
 * Created by Gene on 9/13/2017.
 */

public class CoachViewModel {

    private Context context;
    private long exerciseId;

    private Exercise exercise;
    private BehaviorSubject<Exercise> exerciseSubject = BehaviorSubject.create();

    public CoachViewModel(Context context, long exerciseId) {
        this.context = context;
        this.exerciseId = exerciseId;
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
                        CoachViewModel.this.exercise = exercise;
                        exerciseSubject.onNext(exercise);
                    }
                });
            }
        });
    }

    public Observable<Long> getStartCountDown() {
        return Observable.interval(1, TimeUnit.SECONDS).takeWhile(new Predicate<Long>() {
            @Override
            public boolean test(@NonNull Long aLong) throws Exception {
                return aLong <= 3;
            }
        }).;
    }

    public int getStartCountDownToDisplay() {

    }
}

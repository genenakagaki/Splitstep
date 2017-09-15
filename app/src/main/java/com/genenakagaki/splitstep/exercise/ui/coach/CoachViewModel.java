package com.genenakagaki.splitstep.exercise.ui.coach;

import android.content.Context;

import com.genenakagaki.splitstep.exercise.data.ExerciseDao;
import com.genenakagaki.splitstep.exercise.data.entity.Exercise;
import com.genenakagaki.splitstep.exercise.ui.model.DurationDisplayable;

import java.util.concurrent.TimeUnit;

import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.CompletableSource;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;

/**
 * Created by Gene on 9/13/2017.
 */

public class CoachViewModel {

    private static final int START_COUNT_DOWN_TIME = 3;

    private Context context;
    private long exerciseId;
    private int remainingSets;

    private Exercise exercise;
    private BehaviorSubject<Exercise> exerciseSubject = BehaviorSubject.create();

    private DurationDisplayable restDuration;

    public CoachViewModel(Context context, long exerciseId) {
        this.context = context;
        this.exerciseId = exerciseId;
    }

    public Observable<Exercise> getExerciseSubject() {
        return exerciseSubject
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    public Completable setExercise() {
        return ExerciseDao.getInstance().findById(exerciseId).flatMapCompletable(new Function<Exercise, CompletableSource>() {
            @Override
            public CompletableSource apply(@NonNull final Exercise exercise) throws Exception {
                return Completable.create(new CompletableOnSubscribe() {
                    @Override
                    public void subscribe(@NonNull CompletableEmitter e) throws Exception {
                        CoachViewModel.this.exercise = exercise;
                        remainingSets = exercise.sets;
                        exerciseSubject.onNext(exercise);

                        restDuration = new DurationDisplayable(
                                DurationDisplayable.TYPE_REST_DURATION, exercise.restDuration);
                        e.onComplete();
                    }
                });
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.computation());
    }

    public Exercise getExercise() {
        return exercise;
    }

    public Observable<Long> getStartCountDown() {
        return Observable.interval(1, TimeUnit.SECONDS).takeWhile(new Predicate<Long>() {
            @Override
            public boolean test(@NonNull Long aLong) throws Exception {
                return aLong <= START_COUNT_DOWN_TIME;
            }
        }).map(new Function<Long, Long>() {
            @Override
            public Long apply(@NonNull Long aLong) throws Exception {
                return START_COUNT_DOWN_TIME - aLong;
            }
        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.computation());
    }

    public Observable<DurationDisplayable> getRestTimer() {
        return Observable.interval(1, TimeUnit.SECONDS).takeWhile(new Predicate<Long>() {
            @Override
            public boolean test(@NonNull Long aLong) throws Exception {
                return aLong <= exercise.restDuration;
            }
        }).map(new Function<Long, DurationDisplayable>() {
            @Override
            public DurationDisplayable apply(@NonNull Long aLong) throws Exception {
                restDuration.setDuration(exercise.restDuration - aLong.intValue());
                return restDuration;
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.computation());
    }
}

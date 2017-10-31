package com.genenakagaki.splitstep.exercise.ui.coach;

import android.content.Context;

import com.genenakagaki.splitstep.exercise.data.ExerciseDao;
import com.genenakagaki.splitstep.exercise.data.entity.Exercise;
import com.genenakagaki.splitstep.exercise.data.entity.ExerciseSubType;
import com.genenakagaki.splitstep.exercise.ui.model.DurationDisplayable;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Gene on 9/13/2017.
 */

public class CoachViewModel {

    private static final int START_COUNT_DOWN_TIME = 3;

    private Context context;
    private long exerciseId;
    private Exercise exercise;
    private ExerciseSubType exerciseSubType;

    private DurationDisplayable restDuration;
    private DurationDisplayable setDuration;

    public CoachViewModel(Context context, long exerciseId) {
        this.context = context;
        this.exerciseId = exerciseId;
    }

    public Single<Exercise> loadExercise() {
        return ExerciseDao.getInstance().findById(exerciseId).map(new Function<Exercise, Exercise>() {
            @Override
            public Exercise apply(@NonNull Exercise exercise) throws Exception {
                CoachViewModel.this.exercise = exercise;
                exerciseSubType = ExerciseSubType.fromValue(exercise.subType);

                restDuration = new DurationDisplayable(
                        DurationDisplayable.TYPE_REST_DURATION, exercise.restDuration);
                return exercise;
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.computation());
    }

    public long getExerciseId() {
        return exerciseId;
    }

    public Exercise getExercise() {
        return exercise;
    }

    public ExerciseSubType getExerciseSubType() {
        return exerciseSubType;
    }

    public Observable<Long> getStartCountDown() {
        return Observable.interval(1, TimeUnit.SECONDS).takeWhile(new Predicate<Long>() {
            @Override
            public boolean test(@NonNull Long aLong) throws Exception {
                return aLong < START_COUNT_DOWN_TIME-1;
            }
        }).map(new Function<Long, Long>() {
            @Override
            public Long apply(@NonNull Long aLong) throws Exception {
                return START_COUNT_DOWN_TIME - aLong -1;
            }
        }).observeOn(AndroidSchedulers.mainThread())
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

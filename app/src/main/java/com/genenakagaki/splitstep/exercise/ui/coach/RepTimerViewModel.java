package com.genenakagaki.splitstep.exercise.ui.coach;

import com.genenakagaki.splitstep.exercise.data.entity.Exercise;
import com.genenakagaki.splitstep.exercise.data.entity.ExerciseSubType;
import com.genenakagaki.splitstep.exercise.ui.model.DurationDisplayable;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Gene on 10/5/2017.
 */

public class RepTimerViewModel {

    private DurationDisplayable duration;
    private Exercise exercise;
    private int currentRep;

    public RepTimerViewModel(DurationDisplayable duration, Exercise exercise) {
        this.duration = duration;
        this.exercise = exercise;
    }

    public int getCurrentRep() {
        return currentRep;
    }

    public Observable<Long> startInterval() {
        switch (ExerciseSubType.fromValue(exercise.subType)) {
            case REPS:
                currentRep = exercise.reps;

                return Observable.interval(duration.getDuration(), TimeUnit.SECONDS).takeWhile(new Predicate<Long>() {
                    @Override
                    public boolean test(@NonNull Long aLong) throws Exception {
                        if (aLong < exercise.reps - 1) {
                            currentRep++;
                            return true;
                        } else {
                            return false;
                        }
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io());
            case TIMED_SETS:
                return Observable.interval(duration.getDuration(), TimeUnit.SECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.computation());
        }

        return null;
    }
}

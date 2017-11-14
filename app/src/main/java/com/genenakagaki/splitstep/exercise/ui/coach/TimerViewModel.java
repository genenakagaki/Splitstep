package com.genenakagaki.splitstep.exercise.ui.coach;

import com.genenakagaki.splitstep.exercise.ui.model.DurationDisplayable;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Gene on 9/15/2017.
 */

public class TimerViewModel {

    private int max;
    private int animateDuration;
    private DurationDisplayable duration;

    public TimerViewModel(DurationDisplayable duration) {
        this.duration = duration;
        max = duration.getDuration();
        animateDuration = duration.getDuration() * 1000;
    }

    public int getMax() {
        return max * 100;
    }

    public int getAnimateDuration() {
        return animateDuration;
    }

    public DurationDisplayable getDuration() {
        return duration;
    }

    public Observable<String> startTimer() {
        duration.setDuration(max - 1);

        return Observable.interval(1, TimeUnit.SECONDS).takeWhile(aLong -> {
            if (aLong < max - 1) {
                duration.setDuration(max - aLong.intValue() - 1);
                return true;
            } else {
                duration.setDuration(max);
                return false;
            }
        }).map(aLong -> getTimerDisplay()).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    public Observable<Long> startInterval() {
        return Observable.interval(duration.getDuration(), TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.computation());
    }

    public Observable<String> startInterval(final int reps) {
        return Observable.interval(duration.getDuration(), TimeUnit.SECONDS).takeWhile(aLong -> {
            if (aLong < reps - 1) {
                duration.setDuration(max - aLong.intValue() - 1);
                return true;
            } else {
                duration.setDuration(max);
                return false;
            }
        }).map(aLong -> getTimerDisplay())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    public String getTimerDisplay() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%02d", duration.getMinutes()));
        sb.append(" : ");
        sb.append(String.format("%02d", duration.getSeconds()));
        return sb.toString();
    }

}

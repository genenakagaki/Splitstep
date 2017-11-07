package com.genenakagaki.splitstep.exercise.ui.coach;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;

/**
 * Created by Gene on 11/6/2017.
 */

public class CircularProgressBarViewModel {

    private static final int MAX_DEFAULT = 100;
    private static final int PROGRESS_DEFAULT = 0;
    private static final int ANIMATE_DURATION_DEFAULT = 500;

    private int max;
    private int progress;
    private int animateDuration;
    private BehaviorSubject<Integer> progressSubject = BehaviorSubject.create();

    public CircularProgressBarViewModel() {
        this.max = MAX_DEFAULT;
        this.progress = PROGRESS_DEFAULT;
        animateDuration = ANIMATE_DURATION_DEFAULT;
        progressSubject.onNext(progress * 100);
    }

    public Observable<Integer> getProgress() {
        return progressSubject
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    public void incrementProgressBy(int diff) {
        progress += diff;
        if (progress > max) {
            progress = max;
        } else if (progress < 0) {
            progress = 0;
        }

        progressSubject.onNext(progress * 100);
    }

    public String getDisplayRemaining() {
        return Integer.toString(max - progress);
    }

    public String getDisplayProgress() {
        return Integer.toString(progress);
    }

    public boolean isFinished() {
        return max == progress;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }


}

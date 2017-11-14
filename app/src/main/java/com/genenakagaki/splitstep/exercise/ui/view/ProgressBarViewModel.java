package com.genenakagaki.splitstep.exercise.ui.view;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;

/**
 * Created by Gene on 11/6/2017.
 */

public class ProgressBarViewModel {

    private static final int MAX_DEFAULT = 100;
    private static final int PROGRESS_DEFAULT = 0;
    private static final int ANIMATE_DURATION_DEFAULT = 500;

    private int max;
    private int progress;
    private int animateDuration;
    private BehaviorSubject<Integer> progressSubject = BehaviorSubject.create();

    public ProgressBarViewModel() {
        this.max = MAX_DEFAULT;
        this.progress = PROGRESS_DEFAULT;
        animateDuration = ANIMATE_DURATION_DEFAULT;
    }

    public Observable<Integer> getProgress() {
        return progressSubject
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    public void incrementProgressBy(int diff) {
        setProgress(progress + diff);
    }

    public void setProgress(int progress) {
        this.progress = progress;
        if (progress > max) {
            this.progress = max;
        } else if (progress < 0) {
            this.progress = 0;
        }

        progressSubject.onNext(progress * 100);
    }

    public void setStartProgress(int progress) {
        this.progress = progress;
        if (progress > max) {
            this.progress = max;
        } else if (progress < 0) {
            this.progress = 0;
        }
    }

    public boolean isFinished() {
        return max == progress;
    }

    public int getMax() {
        return max * 100;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getAnimateDuration() {
        return animateDuration * 1000;
    }

    public void setAnimateDuration(int animateDuration) {
        this.animateDuration = animateDuration;
    }
}

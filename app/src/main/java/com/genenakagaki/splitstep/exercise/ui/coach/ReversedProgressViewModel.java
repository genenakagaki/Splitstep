package com.genenakagaki.splitstep.exercise.ui.coach;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;

/**
 * Created by Gene on 9/15/2017.
 */

public class ReversedProgressViewModel {

    private static final int ANIMATE_DURATION_DEFAULT = 500;

    private int max;
    private int animateDuration;
    private int progress;
    private BehaviorSubject<Integer> progressSubject = BehaviorSubject.create();

    public ReversedProgressViewModel(int max, int progress) {
        this.max = max;
        this.progress = progress;
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

    public int getMax() {
        return max * 100;
    }

    public String getDisplayProgress() {
        return Integer.toString(max - progress);
    }

    public int getAnimateDuration() {
        return animateDuration;
    }

    public boolean isFinished() {
        return max == progress;
    }
}

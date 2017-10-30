package com.genenakagaki.splitstep.exercise.ui.view;

import io.reactivex.subjects.BehaviorSubject;

/**
 * Created by Gene on 9/8/2017.
 */

public class NumberInputViewModel {

    public static final int MAX_DEFAULT = 999;
    public static final int MIN_DEFAULT = 1;

    private int max = MAX_DEFAULT;
    private int min = MIN_DEFAULT;
    private int number;
    private BehaviorSubject<Integer> numberSubject = BehaviorSubject.create();

    public NumberInputViewModel() {}

    public void setMax(int max) {
        this.max = max;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        if (this.number == number) return;

        if (number > max) {
            this.number = max;
        } else if (number < min) {
            this.number = min;
        } else {
            this.number = number;
        }
        numberSubject.onNext(number);
    }

    public BehaviorSubject<Integer> getNumberSubject() {
        return numberSubject;
    }

    public void incrementNumber() {
        if (number < max) number++;
        numberSubject.onNext(number);
    }

    public void decrementNumber() {
        if (number > min) number--;
        numberSubject.onNext(number);
    }

}

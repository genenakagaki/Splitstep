package com.genenakagaki.splitstep.exercise.ui.view;

import io.reactivex.subjects.BehaviorSubject;

/**
 * Created by Gene on 9/8/2017.
 */

public class NumberInputViewModel {

    public static final int MAX_DEFAULT = 999;
    public static final int MIN_DEFAULT = 0;

    private BehaviorSubject<Integer> subject = BehaviorSubject.create();

    private int max = MAX_DEFAULT;
    private int min = MIN_DEFAULT;
    private int number;

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
        this.number = number;
    }

    public int incrementNumber() {
        if (number < max) number++;
        return number;
    }

    public int decrementNumber() {
        if (number > min) number--;
        return number;
    }

}

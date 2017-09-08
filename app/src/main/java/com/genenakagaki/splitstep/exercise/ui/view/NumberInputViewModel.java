package com.genenakagaki.splitstep.exercise.ui.view;

/**
 * Created by Gene on 9/8/2017.
 */

public class NumberInputViewModel {

    public static final int MAX_DEFAULT = 999;
    public static final int MIN_DEFAULT = 0;

    private int max = MAX_DEFAULT;
    private int min = MIN_DEFAULT;

    public NumberInputViewModel() {}

    public void setMax(int max) {
        this.max = max;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int incrementNumber(int number) {
        if (number < max) number++;
        return number;
    }

    public int decrementNumber(int number) {
        if (number > min) number--;
        return number;
    }

    public int getInputValue(String inputString) {
        if (inputString.length() > 0) {
            return Integer.parseInt(inputString);
        } else {
            return 0;
        }
    }
}

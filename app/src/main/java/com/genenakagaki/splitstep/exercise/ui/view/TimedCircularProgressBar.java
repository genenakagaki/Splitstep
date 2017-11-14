package com.genenakagaki.splitstep.exercise.ui.view;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by Gene on 11/13/2017.
 */

public class TimedCircularProgressBar extends CircularProgressBar {

    private TimedProgressBarViewModel mViewModel;

    public TimedCircularProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);

        mViewModel = new TimedProgressBarViewModel();
    }
}

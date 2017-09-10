package com.genenakagaki.splitstep.exercise.ui.detail;

import android.content.Context;

import com.genenakagaki.splitstep.R;

/**
 * Created by gene on 9/8/17.
 */

public class DurationPickerViewModel {

    protected static final String[] COLON_PICKER_DISPLAY_VALUES = new String[] {":"};
    protected static final String[] PICKER_DISPLAY_VALUE = buildPickerDisplayValues();

    private static String[] buildPickerDisplayValues() {
        String[] pickerDisplayValues = new String[60];
        for (int i = 0; i < 60; i++) {
            pickerDisplayValues[i] = String.format("%02d", i); //display in 2 digits
        }

        return pickerDisplayValues;
    }

    private Context mContext;
    private String title;
    private int duration;
    private int durationType;

    public DurationPickerViewModel(Context context, String title, int duration, int durationType) {
        this.mContext = context;
        this.title = title;
        this.duration = duration;
        this.durationType = durationType;
    }

    public String getTitle() {
        return title;
    }

    public int getDuration() {
        return duration;
    }

    public int getDurationType() {
        return durationType;
    }

    public String getDisplayableDuration() {
        int minutes = duration / 60;
        int seconds = duration - minutes * 60;

        if (minutes == 0) {
            return mContext.getString(R.string.duration_value_seconds, seconds);
        } else {
            return mContext.getString(R.string.duration_value, minutes, seconds);
        }
    }

    public int getDuration(String durationString) {
        String[] split = durationString.split(" ");

        int minutes = 0;
        int seconds;
        if (split.length == 2) {
            seconds = Integer.parseInt(split[0]);
        } else {
            minutes = Integer.parseInt(split[0]);
            seconds = Integer.parseInt(split[2]);
        }

        return seconds + minutes * 60;
    }

    public int getMinutes() {
        return duration / 60;
    }

    public int getSeconds() {
        return duration % 60;
    }
}

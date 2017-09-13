package com.genenakagaki.splitstep.exercise.ui.model;


import org.parceler.Parcel;

/**
 * Created by gene on 9/10/17.
 */

@Parcel
public class DurationDisplayable {

    public static final int TYPE_REST_DURATION = 1;
    public static final int TYPE_SET_DURATION = 2;
    public static final int TYPE_REP_DURATION = 3;

    int type;
    String title;
    String display;
    int minutes;
    int seconds;

    public DurationDisplayable() {}

    public DurationDisplayable(int type, int duration) {
        this.type = type;
        setDuration(duration);
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public int getDuration() {
        return minutes * 60 + seconds;
    }

    public void setDuration(int duration) {
        this.minutes = duration / 60;
        this.seconds = duration - minutes * 60;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }
}

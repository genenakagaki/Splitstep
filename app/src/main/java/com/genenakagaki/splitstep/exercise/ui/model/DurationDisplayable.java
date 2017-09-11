package com.genenakagaki.splitstep.exercise.ui.model;

/**
 * Created by gene on 9/10/17.
 */

public class DurationDisplayable {

    private String title;
    private int minutes;
    private int seconds;

    public DurationDisplayable(String title, int duration) {
        this.title = title;
        setDuration(duration);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

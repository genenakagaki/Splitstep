package com.genenakagaki.splitstep.exercise.data.entity;

/**
 * Created by gene on 9/12/17.
 */

public enum ExerciseSubType {
    REPS, TIMED_SETS;

    public static final int REPS_VALUE = 1;
    public static final int TIMED_SETS_VALUE = 2;

    ExerciseSubType() {}

    public static ExerciseSubType fromValue(int value) {
        switch (value) {
            case REPS_VALUE:
                return REPS;
            case TIMED_SETS_VALUE:
                return TIMED_SETS;
            default:
                return null;
        }
    }

    public int getValue() {
        switch (this) {
            case REPS:
                return REPS_VALUE;
            case TIMED_SETS:
                return TIMED_SETS_VALUE;
            default:
                return -1;
        }
    }
}

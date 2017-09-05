package com.genenakagaki.splitstep.exercise.data.entity;

/**
 * Created by gene on 8/14/17.
 */

public enum ExerciseType {
    REPS, TIMED_SETS, REACTION;

    public static final int REPS_VALUE = 1;
    public static final int TIMED_SETS_VALUE = 2;
    public static final int REACTION_VALUE = 3;

    ExerciseType() {}

    public static ExerciseType fromValue(int value) {
        switch (value) {
            case REPS_VALUE:
                return ExerciseType.REPS;
            case TIMED_SETS_VALUE:
                return ExerciseType.TIMED_SETS;
            case REACTION_VALUE:
                return ExerciseType.REACTION;
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
            default: // REACTION:
                return REACTION_VALUE;
        }
    }
}

package com.genenakagaki.splitstep.exercise.data.entity;

/**
 * Created by gene on 8/14/17.
 */

public enum ExerciseType {
    REPS, TIMED_SETS, REACTION;

    public static final int REPS_VALUE = 1;
    public static final int TIMED_SETS_VALUE = 2;
    public static final int REACTION_VALUE = 3;

    public int getValue() {
        switch (this) {
            case REPS:
                return REPS_VALUE;
            case TIMED_SETS:
                return TIMED_SETS_VALUE;
            case REACTION:
                return REACTION_VALUE;
        }

        return REPS_VALUE;
    }
}

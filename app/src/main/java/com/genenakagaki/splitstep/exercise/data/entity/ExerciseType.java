package com.genenakagaki.splitstep.exercise.data.entity;

import org.parceler.Parcel;

/**
 * Created by gene on 8/14/17.
 */

@Parcel
public enum ExerciseType {
    REGULAR, REACTION;

    public static final int REGULAR_VALUE = 1;
    public static final int REACTION_VALUE = 2;

    ExerciseType() {}

    public static ExerciseType fromValue(int value) {
        switch (value) {
            case REGULAR_VALUE:
                return ExerciseType.REGULAR;
            case REACTION_VALUE:
                return ExerciseType.REACTION;
            default:
                return null;
        }
    }

    public int getValue() {
        switch (this) {
            case REGULAR:
                return REGULAR_VALUE;
            case REACTION:
                return REACTION_VALUE;
            default:
                return -1;
        }
    }
}

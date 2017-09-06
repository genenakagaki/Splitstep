package com.genenakagaki.splitstep.exercise.utils;

import com.genenakagaki.splitstep.exercise.data.entity.Exercise;

/**
 * Created by Gene on 9/6/2017.
 */

public class DatabaseUtils {

    public static void insertExercises(String[] names, int type, boolean favorite) {
        for (String name: names) {
            new Exercise(type, name, favorite).insert();
        }
    }
}

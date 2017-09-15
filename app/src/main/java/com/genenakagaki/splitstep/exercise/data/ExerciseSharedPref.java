package com.genenakagaki.splitstep.exercise.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.genenakagaki.splitstep.exercise.data.entity.ExerciseSubType;
import com.genenakagaki.splitstep.exercise.data.entity.ExerciseType;

/**
 * Created by gene on 4/7/17.
 */

public class ExerciseSharedPref {

    public static final String PREF_EXERCISE_ID = "PREF_EXERCISE_ID";
    public static final String PREF_EXERCISE_TYPE = "PREF_EXERCISE_TYPE";
    public static final String PREF_EXERCISE_SUB_TYPE = "PREF_EXERCISE_SUB_TYPE";

    public static void setExerciseType(Context context, ExerciseType exerciseType) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();

        editor.putInt(PREF_EXERCISE_TYPE, exerciseType.getValue());
        editor.apply();
    }

    public static ExerciseType getExerciseType(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        int exerciseTypeValue = pref.getInt(PREF_EXERCISE_TYPE, -1);

        return ExerciseType.fromValue(exerciseTypeValue);
    }

    public static void setExerciseId(Context context, long id) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();

        editor.putLong(PREF_EXERCISE_ID, id);
        editor.apply();
    }

    public static long getExerciseId(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return pref.getLong(PREF_EXERCISE_ID, -1);
    }

    public static void setExerciseSubType(Context context, ExerciseSubType exerciseSubType) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();

        editor.putInt(PREF_EXERCISE_SUB_TYPE, exerciseSubType.getValue());
        editor.apply();
    }

    public static ExerciseSubType getExerciseSubType(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        int exerciseSubTypeValue = pref.getInt(PREF_EXERCISE_SUB_TYPE, -1);

        return ExerciseSubType.fromValue(exerciseSubTypeValue);
    }

}

package com.genenakagaki.splitstep.exercise.data;

import android.content.Context;
import android.preference.PreferenceManager;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.genenakagaki.splitstep.exercise.data.entity.ExerciseType;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

/**
 * Created by gene on 9/5/17.
 */

@RunWith(AndroidJUnit4.class)
public class ExerciseSharedPrefTest {

    private Context mContext = InstrumentationRegistry.getTargetContext();

    @Before
    public void setup() {
        PreferenceManager.getDefaultSharedPreferences(mContext).edit().clear().commit();
    }

    @Test
    public void testGetExerciseType_WithNoValueSet_ShouldReturnNull() {
        ExerciseType exerciseType = ExerciseSharedPref.getExerciseType(mContext);
        assertEquals(null, exerciseType);
    }

    @Test
    public void testSetAndGetExerciseType_ShouldReturnSetValue() {
        ExerciseSharedPref.setExerciseType(mContext, ExerciseType.REPS);

        ExerciseType exerciseType = ExerciseSharedPref.getExerciseType(mContext);
        assertEquals(ExerciseType.REPS, exerciseType);
    }

    @Test
    public void testSetExerciseType_WithSetValueReplaced_ShouldReturnNewValue() {
        ExerciseSharedPref.setExerciseType(mContext, ExerciseType.REPS);
        ExerciseSharedPref.setExerciseType(mContext, ExerciseType.TIMED_SETS);

        ExerciseType exerciseType = ExerciseSharedPref.getExerciseType(mContext);
        assertEquals(ExerciseType.TIMED_SETS, exerciseType);
    }

    @Test
    public void testGetExerciseId_WithNoValueSet_ShouldReturnInvalidValue() {
        long exerciseId = ExerciseSharedPref.getExerciseId(mContext);
        assertEquals(-1, exerciseId);
    }

    @Test
    public void testSetAndGetExerciseId_ShouldReturnSetValue() {
        ExerciseSharedPref.setExerciseId(mContext, 1);
        long exerciseId = ExerciseSharedPref.getExerciseId(mContext);
        assertEquals(1, exerciseId);
    }

    @Test
    public void testSetExerciseId_WithSetValueReplaced_ShouldReturnNewValue() {
        ExerciseSharedPref.setExerciseId(mContext, 1);
        ExerciseSharedPref.setExerciseId(mContext, 2);

        long exerciseId = ExerciseSharedPref.getExerciseId(mContext);
        assertEquals(2, exerciseId);
    }
}
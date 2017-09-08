package com.genenakagaki.splitstep.exercise.data.entity;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.genenakagaki.splitstep.exercise.data.ExerciseDatabase;
import com.raizlabs.android.dbflow.config.DatabaseConfig;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by gene on 9/4/17.
 */

@RunWith(AndroidJUnit4.class)
public class RepsExerciseTest {

    private static final RepsExercise REPS_EXERCISE = new RepsExercise(1, 1, 1, 1);

    @Before
    public void setUp() throws Exception {
        Context mContext = InstrumentationRegistry.getTargetContext();

        FlowManager.init(FlowConfig.builder(mContext)
                .addDatabaseConfig(DatabaseConfig.inMemoryBuilder(ExerciseDatabase.class)
                        .databaseName("ExerciseDatabase")
                        .build())
                .build());
    }

    @After
    public void tearDown() throws Exception {
        FlowManager.destroy();
    }

    @Test
    public void testGetRepsExercise_WithNoExerciseInserted_ShouldReturnEmptyList() {
        List<RepsExercise> exercises = SQLite.select()
                .from(RepsExercise.class)
                .queryList();

        assertEquals(0, exercises.size());
    }

    @Test
    public void testInsertAndGetRepsExercise_ShouldReturnInsertedExercise() {
        REPS_EXERCISE.insert();

        List<RepsExercise> exercises = SQLite.select()
                .from(RepsExercise.class)
                .queryList();

        assertTrue(isExerciseEqual(exercises.get(0), REPS_EXERCISE));
    }

    @Test
    public void testUpdateRepsExercise_ShouldBeUpdated() {
        final RepsExercise exerciseToUpdate = new RepsExercise(1, 1, 1, 1);

        exerciseToUpdate.insert();
        exerciseToUpdate.reps = 2;
        exerciseToUpdate.sets = 2;
        exerciseToUpdate.restDuration = 2;
        exerciseToUpdate.update();

        List<RepsExercise> exercises = SQLite.select()
                .from(RepsExercise.class)
                .queryList();

        assertTrue(isExerciseEqual(exerciseToUpdate, exercises.get(0)));
    }

    @Test
    public void testDeleteRepsExercise_ShouldBeDeleted() {
        REPS_EXERCISE.insert();
        REPS_EXERCISE.delete();

        List<RepsExercise> exercises = SQLite.select()
                .from(RepsExercise.class)
                .queryList();

        assertEquals(0, exercises.size());
    }

    private boolean isExerciseEqual(RepsExercise r1, RepsExercise r2) {
        return r1.reps == r2.reps
                && r1.sets == r2.sets
                && r1.restDuration == r2.restDuration;
    }


}
package com.genenakagaki.splitstep.exercise.data.entity;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import com.genenakagaki.splitstep.exercise.data.ExerciseDatabase;
import com.raizlabs.android.dbflow.config.DatabaseConfig;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by gene on 9/4/17.
 */
public class TimedSetsExerciseTest {


    private static final TimedSetsExercise TIMED_SETS_EXERCISE = new TimedSetsExercise(1, 1, 1, 1);

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
    public void testGetTimedSetsExercise_WithNoExerciseInserted_ShouldReturnEmptyList() {
        List<TimedSetsExercise> exercises = SQLite.select()
                .from(TimedSetsExercise.class)
                .queryList();

        assertEquals(0, exercises.size());
    }

    @Test
    public void testInsertAndGetTimedSetsExercise_ShouldReturnInsertedExercise() {
        TIMED_SETS_EXERCISE.insert();

        List<TimedSetsExercise> exercises = SQLite.select()
                .from(TimedSetsExercise.class)
                .queryList();

        assertTrue(isExerciseEqual(exercises.get(0), TIMED_SETS_EXERCISE));
    }

    @Test
    public void testUpdateTimedSetsExercise_ShouldBeUpdated() {
        final TimedSetsExercise exerciseToUpdate = new TimedSetsExercise(1, 1, 1, 1);

        exerciseToUpdate.insert();
        exerciseToUpdate.sets = 2;
        exerciseToUpdate.setDuration = 2;
        exerciseToUpdate.restDuration = 2;
        exerciseToUpdate.update();

        List<TimedSetsExercise> exercises = SQLite.select()
                .from(TimedSetsExercise.class)
                .queryList();

        assertTrue(isExerciseEqual(exerciseToUpdate, exercises.get(0)));
    }

    @Test
    public void testDeleteTimedSetsExercise_ShouldBeDeleted() {
        TIMED_SETS_EXERCISE.insert();
        TIMED_SETS_EXERCISE.delete();

        List<TimedSetsExercise> exercises = SQLite.select()
                .from(TimedSetsExercise.class)
                .queryList();

        assertEquals(0, exercises.size());
    }

    private boolean isExerciseEqual(TimedSetsExercise r1, TimedSetsExercise r2) {
        return r1.sets == r2.sets
                && r1.setDuration == r2.setDuration
                && r1.restDuration == r2.restDuration;
    }
}
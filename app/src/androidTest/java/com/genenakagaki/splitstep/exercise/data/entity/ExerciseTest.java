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
public class ExerciseTest {

    private static final Exercise EXERCISE = new Exercise(1, "test", false);

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
    public void testGetExercise_WithNoExerciseInserted_ShouldReturnEmptyList() {
        List<Exercise> exercises = SQLite.select()
                .from(Exercise.class)
                .queryList();

        assertEquals(0, exercises.size());
    }

    @Test
    public void testInsertAndGetExercise_ShouldReturnInsertedExercise() {
        EXERCISE.insert();

        List<Exercise> exercises = SQLite.select()
                .from(Exercise.class)
                .queryList();

        assertTrue(isExerciseEqual(exercises.get(0), EXERCISE));
    }

    @Test
    public void testUpdateExercise_ShouldBeUpdated() {
        final Exercise exerciseToUpdate = new Exercise(1, "test", false);
        String newName = "new";

        exerciseToUpdate.insert();
        exerciseToUpdate.type = 2;
        exerciseToUpdate.favorite = true;
        exerciseToUpdate.name = newName;
        exerciseToUpdate.update();

        List<Exercise> exercises = SQLite.select()
                .from(Exercise.class)
                .queryList();

        assertTrue(isExerciseEqual(exerciseToUpdate, exercises.get(0)));
    }

    @Test
    public void testDeleteExercise_ShouldBeDeleted() {
        EXERCISE.insert();
        EXERCISE.delete();

        List<Exercise> exercises = SQLite.select()
                .from(Exercise.class)
                .queryList();

        assertEquals(0, exercises.size());
    }

    private boolean isExerciseEqual(Exercise e1, Exercise e2) {
        return e1.type == e2.type
                && e1.favorite == e2.favorite
                && e1.name.equals(e2.name);
    }
}
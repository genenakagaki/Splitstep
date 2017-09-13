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
public class ReactionExerciseTest {

    private static final ReactionExercise REACTION_EXERCISE = new ReactionExercise(1, 1, 1);

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
    public void testGetReactionExercise_WithNoExerciseInserted_ShouldReturnEmptyList() {
        List<ReactionExercise> exercises = SQLite.select()
                .from(ReactionExercise.class)
                .queryList();

        assertEquals(0, exercises.size());
    }

    @Test
    public void testInsertAndGetReactionExercise_ShouldReturnInsertedExercise() {
        REACTION_EXERCISE.insert();

        List<ReactionExercise> exercises = SQLite.select()
                .from(ReactionExercise.class)
                .queryList();

        assertTrue(isExerciseEqual(exercises.get(0), REACTION_EXERCISE));
    }

    @Test
    public void testUpdateReactionExercise_ShouldBeUpdated() {
        final ReactionExercise exerciseToUpdate = new ReactionExercise(1, 1, 1);

        exerciseToUpdate.insert();
        exerciseToUpdate.cones = 2;
        exerciseToUpdate.repDuration = 2;
        exerciseToUpdate.update();

        List<ReactionExercise> exercises = SQLite.select()
                .from(ReactionExercise.class)
                .queryList();

        assertTrue(isExerciseEqual(exerciseToUpdate, exercises.get(0)));
    }

    @Test
    public void testDeleteReactionExercise_ShouldBeDeleted() {
        REACTION_EXERCISE.insert();
        REACTION_EXERCISE.delete();

        List<ReactionExercise> exercises = SQLite.select()
                .from(ReactionExercise.class)
                .queryList();

        assertEquals(0, exercises.size());
    }

    private boolean isExerciseEqual(ReactionExercise r1, ReactionExercise r2) {
        return r1.cones == r2.cones
                && r1.repDuration == r2.repDuration;
    }

}
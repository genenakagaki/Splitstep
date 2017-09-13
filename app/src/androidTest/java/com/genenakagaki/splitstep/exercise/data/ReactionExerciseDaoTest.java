package com.genenakagaki.splitstep.exercise.data;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.genenakagaki.splitstep.exercise.data.entity.ReactionExercise;
import com.genenakagaki.splitstep.exercise.data.exception.ExerciseNotFoundException;
import com.raizlabs.android.dbflow.config.DatabaseConfig;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Predicate;

import static org.junit.Assert.assertEquals;

/**
 * Created by gene on 9/13/17.
 */

@RunWith(AndroidJUnit4.class)
public class ReactionExerciseDaoTest {

    private static final ReactionExercise REACTION_EXERCISE = new ReactionExercise(1);

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
    public void testFindById_WithExistingExerciseId_ShouldEmitCorrectExercise() {
        REACTION_EXERCISE.insert();

        ReactionExerciseDao.getInstance().findById(REACTION_EXERCISE.id)
                .test()
                .assertValue(new Predicate<ReactionExercise>() {
                    @Override
                    public boolean test(@NonNull ReactionExercise exercise) throws Exception {
                        return REACTION_EXERCISE.id == exercise.id
                                && REACTION_EXERCISE.cones == exercise.cones
                                && REACTION_EXERCISE.repDuration == exercise.repDuration;
                    }
                });
    }

    @Test
    public void testFindById_WithNonExistingExerciseId_ShouldEmitError() {
        ExerciseDao exerciseDao = ExerciseDao.getInstance();

        exerciseDao.findById(0)
                .test()
                .assertError(ExerciseNotFoundException.class);
    }

    @Test
    public void testIsColumnsValid_WithInvalidColumns_ShouldReturnFalse() {
        ReactionExercise exercise = new ReactionExercise(1);
        exercise.cones = 1;

        assertEquals(false, ReactionExerciseDao.getInstance().isColumnsValid(exercise));
    }

    @Test
    public void testIsColumnsValid_WithValidColumns_ShouldReturnTrue() {
        assertEquals(true, ReactionExerciseDao.getInstance().isColumnsValid(REACTION_EXERCISE));
    }

    @Test
    public void testUpdate_WithInvalidColumns_ShouldEmitError() {
        ReactionExerciseDao.getInstance().update(REACTION_EXERCISE)
                .test()
                .assertComplete();
    }
}
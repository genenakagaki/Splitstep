package com.genenakagaki.splitstep.exercise.data;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.genenakagaki.splitstep.exercise.data.entity.Exercise;
import com.genenakagaki.splitstep.exercise.data.entity.ExerciseType;
import com.genenakagaki.splitstep.exercise.data.entity.ReactionExercise;
import com.genenakagaki.splitstep.exercise.data.entity.RepsExercise;
import com.genenakagaki.splitstep.exercise.data.entity.TimedSetsExercise;
import com.genenakagaki.splitstep.exercise.data.exception.ExerciseAlreadyExistsException;
import com.genenakagaki.splitstep.exercise.data.exception.ExerciseNotFoundException;
import com.genenakagaki.splitstep.exercise.data.exception.InvalidExerciseColumnException;
import com.genenakagaki.splitstep.exercise.utils.DatabaseUtils;
import com.raizlabs.android.dbflow.config.DatabaseConfig;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Predicate;

import static org.junit.Assert.assertEquals;

/**
 * Created by gene on 9/9/17.
 */
@RunWith(AndroidJUnit4.class)
public class ExerciseDaoTest {

    private static final Exercise EXERCISE = new Exercise(ExerciseType.REPS_VALUE, "test", false);

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
    public void testFindById_WithExistingExerciseId_ShouldReturnCorrectExercise() {
        ExerciseDao exerciseDao = ExerciseDao.getInstance();

        EXERCISE.insert();

        exerciseDao.findById(EXERCISE.id)
                .test()
                .assertValue(new Predicate<Exercise>() {
                    @Override
                    public boolean test(@NonNull Exercise exercise) throws Exception {
                        return EXERCISE.type == exercise.type
                                && EXERCISE.favorite == exercise.favorite
                                && EXERCISE.name.equals(exercise.name);
                    }
                });
    }

    @Test
    public void testFindById_WithNonExistingExerciseId_ShouldReturnCorrectExercise() {
        ExerciseDao exerciseDao = ExerciseDao.getInstance();

        exerciseDao.findById(0)
                .test()
                .assertError(ExerciseNotFoundException.class);
    }

    @Test
    public void testFindByType_WithNoInsertedExercises_ShouldReturnEmptyList() {
        ExerciseDao exerciseDao = ExerciseDao.getInstance();

        exerciseDao.findByType(ExerciseType.REPS)
                .test()
                .assertValue(new Predicate<List<Exercise>>() {
                    @Override
                    public boolean test(@NonNull List<Exercise> exercises) throws Exception {
                        return exercises.size() == 0;
                    }
                });
    }

    @Test
    public void testFindByType_WithRepsExercises_ShouldReturnRepsExercises() {
        ExerciseDao exerciseDao = ExerciseDao.getInstance();

        String[] exerciseNames = new String[] {
                "exerciseName1",
                "exerciseName1",
                "exerciseName1"
        };
        DatabaseUtils.insertExercises(exerciseNames, ExerciseType.REPS_VALUE, false);

        exerciseDao.findByType(ExerciseType.REPS)
                .test()
                .assertValue(new Predicate<List<Exercise>>() {
                    @Override
                    public boolean test(@NonNull List<Exercise> exercises) throws Exception {
                        return exercises.size() == 3;
                    }
                });
    }

    @Test
    public void testFindByType_WithTimedSetsExercises_ShouldReturnTimedSetsExercises() {
        ExerciseDao exerciseDao = ExerciseDao.getInstance();

        String[] exerciseNames = new String[] {
                "exerciseName1",
                "exerciseName1",
                "exerciseName1"
        };
        DatabaseUtils.insertExercises(exerciseNames, ExerciseType.TIMED_SETS_VALUE, false);

        exerciseDao.findByType(ExerciseType.TIMED_SETS)
                .test()
                .assertValue(new Predicate<List<Exercise>>() {
                    @Override
                    public boolean test(@NonNull List<Exercise> exercises) throws Exception {
                        return exercises.size() == 3;
                    }
                });
    }

    @Test
    public void testFindByType_WithReactionExercises_ShouldReturnReactionExercises() {
        ExerciseDao exerciseDao = ExerciseDao.getInstance();

        String[] exerciseNames = new String[] {
                "exerciseName1",
                "exerciseName1",
                "exerciseName1"
        };
        DatabaseUtils.insertExercises(exerciseNames, ExerciseType.REACTION_VALUE, false);

        exerciseDao.findByType(ExerciseType.REACTION)
                .test()
                .assertValue(new Predicate<List<Exercise>>() {
                    @Override
                    public boolean test(@NonNull List<Exercise> exercises) throws Exception {
                        return exercises.size() == 3;
                    }
                });
    }

    @Test
    public void testInsert_WithNonExistingExercise_ShouldComplete() {
        ExerciseDao exerciseDao = ExerciseDao.getInstance();

        exerciseDao.insert(EXERCISE.name, ExerciseType.REPS)
                .test()
                .assertComplete();
    }

    @Test
    public void testInsert_WithEmptyNameString_ShouldEmitError() {
        ExerciseDao exerciseDao = ExerciseDao.getInstance();

        exerciseDao.insert("", ExerciseType.REPS)
                .test()
                .assertError(InvalidExerciseColumnException.class);
    }

    @Test
    public void testInsert_WithExistingExercise_ShouldEmitError() {
        ExerciseDao exerciseDao = ExerciseDao.getInstance();

        EXERCISE.insert();

        exerciseDao.insert(EXERCISE.name, ExerciseType.REPS)
                .test()
                .assertError(ExerciseAlreadyExistsException.class);
    }

    @Test
    public void testIsConflict_WithNonExistingExercise_ShouldReturnFalse() {
        ExerciseDao exerciseDao = ExerciseDao.getInstance();

        assertEquals(false, exerciseDao.isConflict(EXERCISE.name, ExerciseType.REPS));
    }

    @Test
    public void testIsConflict_WithExistingExercise_ShouldReturnTrue() {
        ExerciseDao exerciseDao = ExerciseDao.getInstance();

        EXERCISE.insert();

        assertEquals(true, exerciseDao.isConflict(EXERCISE.name, ExerciseType.REPS));
    }

    @Test
    public void testDelete_WithRepsExercise_ShouldBeDeleted() {
        ExerciseDao exerciseDao = ExerciseDao.getInstance();

        EXERCISE.insert();
        RepsExercise repsExercise = new RepsExercise(EXERCISE.id, 1, 1, 1);
        repsExercise.insert();

        exerciseDao.delete(EXERCISE.id, ExerciseType.REPS)
                .test()
                .assertComplete();

        List<Exercise> exercises = SQLite.select()
                .from(Exercise.class)
                .queryList();

        List<RepsExercise> repsExercises = SQLite.select()
                .from(RepsExercise.class)
                .queryList();

        assertEquals(0, exercises.size());
        assertEquals(0, repsExercises.size());
    }

    @Test
    public void testDelete_WithTimedSetsExercise_ShouldBeDeleted() {
        ExerciseDao exerciseDao = ExerciseDao.getInstance();

        EXERCISE.insert();
        TimedSetsExercise timedSetsExercise = new TimedSetsExercise(EXERCISE.id, 1, 1, 1);
        timedSetsExercise.insert();

        exerciseDao.delete(EXERCISE.id, ExerciseType.TIMED_SETS)
                .test()
                .assertComplete();

        List<Exercise> exercises = SQLite.select()
                .from(Exercise.class)
                .queryList();

        List<TimedSetsExercise> timedSetsExercises = SQLite.select()
                .from(TimedSetsExercise.class)
                .queryList();

        assertEquals(0, exercises.size());
        assertEquals(0, timedSetsExercises.size());
    }

    @Test
    public void testDelete_WithReactionExercise_ShouldBeDeleted() {
        ExerciseDao exerciseDao = ExerciseDao.getInstance();

        EXERCISE.insert();
        ReactionExercise reactionExercise = new ReactionExercise(EXERCISE.id, 1, 1, 1, 1, 1);
        reactionExercise.insert();

        exerciseDao.delete(EXERCISE.id, ExerciseType.REACTION)
                .test()
                .assertComplete();

        List<Exercise> exercises = SQLite.select()
                .from(Exercise.class)
                .queryList();

        List<ReactionExercise> reactionExercises = SQLite.select()
                .from(ReactionExercise.class)
                .queryList();

        assertEquals(0, exercises.size());
        assertEquals(0, reactionExercises.size());
    }

}
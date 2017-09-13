package com.genenakagaki.splitstep.exercise.data;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.genenakagaki.splitstep.exercise.data.entity.Exercise;
import com.genenakagaki.splitstep.exercise.data.entity.ExerciseSubType;
import com.genenakagaki.splitstep.exercise.data.entity.ExerciseType;
import com.genenakagaki.splitstep.exercise.data.entity.ReactionExercise;
import com.genenakagaki.splitstep.exercise.data.exception.ExerciseAlreadyExistsException;
import com.genenakagaki.splitstep.exercise.data.exception.ExerciseNotFoundException;
import com.genenakagaki.splitstep.exercise.data.exception.InvalidExerciseColumnsException;
import com.genenakagaki.splitstep.exercise.data.exception.InvalidExerciseNameException;
import com.genenakagaki.splitstep.exercise.utils.DatabaseUtils;
import com.raizlabs.android.dbflow.config.DatabaseConfig;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Predicate;

import static com.raizlabs.android.dbflow.sql.language.SQLite.select;
import static org.junit.Assert.assertEquals;

/**
 * Created by gene on 9/9/17.
 */
@RunWith(AndroidJUnit4.class)
public class ExerciseDaoTest {

    private static final Exercise EXERCISE = new Exercise(ExerciseType.REGULAR_VALUE, "test");

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


    /* Insert */
    @Test
    public void testIsNameAndTypeValid_WithNonExistingExercise_ShouldReturnTrue() {
        ExerciseDao exerciseDao = ExerciseDao.getInstance();

        assertEquals(true, exerciseDao.isNameAndTypeValid(EXERCISE.name, ExerciseType.REGULAR_VALUE));
    }

    @Test
    public void testIsNameAndTypeValid_WithExistingExercise_ShouldReturnTrue() {
        ExerciseDao exerciseDao = ExerciseDao.getInstance();

        EXERCISE.insert();

        assertEquals(false, exerciseDao.isNameAndTypeValid(EXERCISE.name, ExerciseType.REGULAR_VALUE));
    }

    @Test
    public void testInsert_WithNonExistingExercise_ShouldComplete() {
        ExerciseDao exerciseDao = ExerciseDao.getInstance();

        exerciseDao.insert(EXERCISE.name, ExerciseType.REGULAR)
                .test()
                .assertComplete();
    }

    @Test
    public void testInsert_WithEmptyNameString_ShouldEmitError() {
        ExerciseDao exerciseDao = ExerciseDao.getInstance();

        exerciseDao.insert("", ExerciseType.REGULAR)
                .test()
                .assertError(InvalidExerciseNameException.class);
    }

    @Test
    public void testInsert_WithExistingExercise_ShouldEmitError() {
        ExerciseDao exerciseDao = ExerciseDao.getInstance();

        EXERCISE.insert();

        exerciseDao.insert(EXERCISE.name, ExerciseType.REGULAR)
                .test()
                .assertError(ExerciseAlreadyExistsException.class);
    }


    /* Get */
    @Test
    public void testFindById_WithExistingExerciseId_ShouldEmitCorrectExercise() {
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
    public void testFindById_WithNonExistingExerciseId_ShouldEmitError() {
        ExerciseDao exerciseDao = ExerciseDao.getInstance();

        exerciseDao.findById(0)
                .test()
                .assertError(ExerciseNotFoundException.class);
    }

    @Test
    public void testFindByType_WithNoInsertedExercises_ShouldEmitEmptyList() {
        ExerciseDao exerciseDao = ExerciseDao.getInstance();

        exerciseDao.findByType(ExerciseType.REGULAR)
                .test()
                .assertValue(new Predicate<List<Exercise>>() {
                    @Override
                    public boolean test(@NonNull List<Exercise> exercises) throws Exception {
                        return exercises.size() == 0;
                    }
                });
    }

    @Test
    public void testFindByType_WithRegularExercises_ShouldEmitExercises() {
        ExerciseDao exerciseDao = ExerciseDao.getInstance();

        String[] exerciseNames = new String[] {
                "exerciseName1",
                "exerciseName1",
                "exerciseName1"
        };
        DatabaseUtils.insertExercises(exerciseNames, ExerciseType.REGULAR_VALUE);

        exerciseDao.findByType(ExerciseType.REGULAR)
                .test()
                .assertValue(new Predicate<List<Exercise>>() {
                    @Override
                    public boolean test(@NonNull List<Exercise> exercises) throws Exception {
                        return exercises.size() == 3;
                    }
                });
    }

    @Test
    public void testFindByType_WithReactionExercises_ShouldEmitReactionExercises() {
        ExerciseDao exerciseDao = ExerciseDao.getInstance();

        String[] exerciseNames = new String[] {
                "exerciseName1",
                "exerciseName1",
                "exerciseName1"
        };
        DatabaseUtils.insertExercises(exerciseNames, ExerciseType.REACTION_VALUE);

        exerciseDao.findByType(ExerciseType.REACTION)
                .test()
                .assertValue(new Predicate<List<Exercise>>() {
                    @Override
                    public boolean test(@NonNull List<Exercise> exercises) throws Exception {
                        return exercises.size() == 3;
                    }
                });
    }


    /* Update */
    @Test
    public void testIsColumnsValid_WithZeroSets_ShouldReturnFalse() {
        Exercise exercise = new Exercise(ExerciseType.REGULAR_VALUE, "exercise");
        exercise.sets = 0;

        assertEquals(false, ExerciseDao.getInstance().isColumnsValid(exercise));
    }

    @Test
    public void testIsColumnsValid_WithZeroRestDuration_ShouldReturnFalse() {
        Exercise exercise = new Exercise(ExerciseType.REGULAR_VALUE, "exercise");
        exercise.restDuration = 0;

        assertEquals(false, ExerciseDao.getInstance().isColumnsValid(exercise));
    }

    @Test
    public void testIsColumnsValid_WithRepsSubTypeWithZeroReps_ShouldReturnFalse() {
        Exercise exercise = new Exercise(ExerciseType.REGULAR_VALUE, "exercise");
        exercise.subType = ExerciseSubType.REPS_VALUE;
        exercise.reps = 0;

        assertEquals(false, ExerciseDao.getInstance().isColumnsValid(exercise));
    }

    @Test
    public void testIsColumnsValid_WithTimedSetSubTypeWithZeroSetDuration_ShouldReturnFalse() {
        Exercise exercise = new Exercise(ExerciseType.REGULAR_VALUE, "exercise");
        exercise.subType = ExerciseSubType.TIMED_SETS_VALUE;
        exercise.setDuration = 0;

        assertEquals(false, ExerciseDao.getInstance().isColumnsValid(exercise));
    }

    @Test
    public void testIsColumnsValid_WithValidColumns_ShouldReturnTrue() {
        Exercise exercise = new Exercise(ExerciseType.REGULAR_VALUE, "exercise");

        assertEquals(true, ExerciseDao.getInstance().isColumnsValid(exercise));
    }

    @Test
    public void testUpdate_WithInvalidColumns_ShouldEmitError() {
        Exercise exercise = new Exercise(ExerciseType.REGULAR_VALUE, "exercise");
        exercise.insert();
        exercise.sets = 0;

        ExerciseDao.getInstance().update(exercise)
                .test()
                .assertError(InvalidExerciseColumnsException.class);
    }

    @Test
    public void testUpdate_WithValidColumns_ShouldComplete() {
        Exercise exercise = new Exercise(ExerciseType.REGULAR_VALUE, "exercise");
        exercise.insert();

        ExerciseDao.getInstance().update(exercise)
                .test()
                .assertComplete();
    }


    /* Delete */
    @Test
    public void testDelete_WithRegularExercise_ShouldBeDeleted() {
        ExerciseDao exerciseDao = ExerciseDao.getInstance();

        EXERCISE.insert();

        exerciseDao.delete(EXERCISE.id, ExerciseType.REGULAR)
                .test()
                .assertComplete();

        List<Exercise> exercises = select()
                .from(Exercise.class)
                .queryList();

        assertEquals(0, exercises.size());
    }

    @Test
    public void testDelete_WithReactionExercise_ShouldBeDeleted() {
        ExerciseDao exerciseDao = ExerciseDao.getInstance();

        EXERCISE.insert();
        ReactionExercise reactionExercise = new ReactionExercise(EXERCISE.id, 1, 1);
        reactionExercise.insert();

        exerciseDao.delete(EXERCISE.id, ExerciseType.REACTION)
                .test()
                .assertComplete();

        List<Exercise> exercises = select()
                .from(Exercise.class)
                .queryList();

        List<ReactionExercise> reactionExercises = select()
                .from(ReactionExercise.class)
                .queryList();

        assertEquals(0, exercises.size());
        assertEquals(0, reactionExercises.size());
    }

}
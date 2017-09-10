package com.genenakagaki.splitstep.exercise.ui.list;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.genenakagaki.splitstep.exercise.data.ExerciseDatabase;
import com.genenakagaki.splitstep.exercise.data.entity.Exercise;
import com.genenakagaki.splitstep.exercise.data.entity.ExerciseType;
import com.genenakagaki.splitstep.exercise.data.entity.ReactionExercise;
import com.genenakagaki.splitstep.exercise.data.entity.RepsExercise;
import com.genenakagaki.splitstep.exercise.data.entity.TimedSetsExercise;
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

/**
 * Created by gene on 9/8/17.
 */

@RunWith(AndroidJUnit4.class)
public class DeleteExerciseViewModelTest {

    private Context mContext = InstrumentationRegistry.getTargetContext();

    @Before
    public void setUp() throws Exception {
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
    public void testDeleteExercise_WithRepsExercise_ShouldBeDeleted() {
        Exercise exercise = new Exercise(ExerciseType.REPS_VALUE, "Reps exercise", false);
        exercise.insert();

        DeleteExerciseViewModel viewModel = new DeleteExerciseViewModel(exercise.id, ExerciseType.REPS, "deleting");

        RepsExercise repsExercise = new RepsExercise(exercise.id, 1, 1, 1);
        repsExercise.insert();

        viewModel.deleteExerciseCompletable()
                .test()
                .awaitTerminalEvent();

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
    public void testDeleteExercise_WithTimedSetsExercise_ShouldBeDeleted() {
        Exercise exercise = new Exercise(ExerciseType.TIMED_SETS_VALUE, "TimedSets exercise", false);
        exercise.insert();

        DeleteExerciseViewModel viewModel = new DeleteExerciseViewModel(exercise.id, ExerciseType.TIMED_SETS, "deleting");

        TimedSetsExercise timedSetsExercise = new TimedSetsExercise(exercise.id, 1, 1, 1);
        timedSetsExercise.insert();

        viewModel.deleteExerciseCompletable()
                .test()
                .awaitTerminalEvent();

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
    public void testDeleteExercise_WithReactionExercise_ShouldBeDeleted() {
        Exercise exercise = new Exercise(ExerciseType.REACTION_VALUE, "Reaction exercise", false);
        exercise.insert();

        DeleteExerciseViewModel viewModel = new DeleteExerciseViewModel(exercise.id, ExerciseType.REACTION, "deleting");

        ReactionExercise reactionExercise = new ReactionExercise(exercise.id, 1, 1, 1, 1, 1);
        reactionExercise.insert();

        viewModel.deleteExerciseCompletable()
                .test()
                .awaitTerminalEvent();

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
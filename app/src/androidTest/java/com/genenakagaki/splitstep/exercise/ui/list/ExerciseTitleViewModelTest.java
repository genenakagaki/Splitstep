package com.genenakagaki.splitstep.exercise.ui.list;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.genenakagaki.splitstep.exercise.data.ExerciseDatabase;
import com.genenakagaki.splitstep.exercise.data.entity.Exercise;
import com.genenakagaki.splitstep.exercise.data.entity.ExerciseType;
import com.genenakagaki.splitstep.exercise.data.entity.Exercise_Table;
import com.raizlabs.android.dbflow.config.DatabaseConfig;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Predicate;

import static org.junit.Assert.assertEquals;

/**
 * Created by Gene on 9/8/2017.
 */

@RunWith(AndroidJUnit4.class)
public class ExerciseTitleViewModelTest {

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
    public void testToggleExerciseFavorite_WithNonFavoriteExercise_ShouldBecomeFavorite() {
        Exercise exercise = new Exercise(ExerciseType.REGULAR_VALUE, "exercise");
        exercise.insert();

        ExerciseTitleViewModel viewModel = new ExerciseTitleViewModel(mContext, exercise);

        viewModel.toggleExerciseFavorite()
                .test()
                .assertComplete();

        Exercise updatedExercise = SQLite.select()
                .from(Exercise.class)
                .where(Exercise_Table.id.eq(exercise.id))
                .querySingle();

        assertEquals(true, updatedExercise.favorite);
    }

    @Test
    public void testToggleExerciseFavorite_WithFavoriteExercise_ShouldBecomeNonFavorite() {
        Exercise exercise = new Exercise(ExerciseType.REGULAR_VALUE, "exercise");
        exercise.favorite = true;
        exercise.insert();

        ExerciseTitleViewModel viewModel = new ExerciseTitleViewModel(mContext, exercise);

        viewModel.toggleExerciseFavorite()
                .test()
                .assertComplete();

        Exercise updatedExercise = SQLite.select()
                .from(Exercise.class)
                .where(Exercise_Table.id.eq(exercise.id))
                .querySingle();

        assertEquals(false, updatedExercise.favorite);
    }

    @Test
    public void testExerciseSubject_ShouldEmitOnToggleExerciseFavorite() {
        final Exercise exercise = new Exercise(ExerciseType.REGULAR_VALUE, "exercise");
        exercise.favorite = true;
        exercise.insert();

        ExerciseTitleViewModel viewModel = new ExerciseTitleViewModel(mContext, exercise);

        viewModel.toggleExerciseFavorite()
                .test()
                .assertComplete();

        viewModel.getExerciseSubject()
                .test()
                .assertValue(new Predicate<Exercise>() {
                    @Override
                    public boolean test(@NonNull Exercise e) throws Exception {
                        return exercise.type == e.type
                                && exercise.favorite == e.favorite
                                && exercise.name.equals(e.name);
                    }
                });
    }

    @Test
    public void testGetDeleteMessage_WithRepsExercise_ShouldReturnCorrectMessage() {
        String exerciseName = "exercise name";
        Exercise exercise = new Exercise(ExerciseType.REGULAR_VALUE, exerciseName);

        ExerciseTitleViewModel viewModel = new ExerciseTitleViewModel(mContext, exercise);

        String deleteMessage = viewModel.getExerciseDisplayable();
        String expectedMessage = "exercise name (Regular)";

        assertEquals(expectedMessage, deleteMessage);
    }

    @Test
    public void testGetDeleteMessage_WithReactionExercise_ShouldReturnCorrectMessage() {
        String exerciseName = "exercise name";
        Exercise exercise = new Exercise(ExerciseType.REACTION_VALUE, exerciseName);

        ExerciseTitleViewModel viewModel = new ExerciseTitleViewModel(mContext, exercise);

        String deleteMessage = viewModel.getExerciseDisplayable();
        String expectedMessage = "exercise name (Reaction)";

        assertEquals(expectedMessage, deleteMessage);
    }
}
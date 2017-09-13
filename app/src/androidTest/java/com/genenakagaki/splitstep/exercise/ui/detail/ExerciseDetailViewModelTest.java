package com.genenakagaki.splitstep.exercise.ui.detail;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.genenakagaki.splitstep.R;
import com.genenakagaki.splitstep.exercise.data.ExerciseDatabase;
import com.genenakagaki.splitstep.exercise.data.entity.Exercise;
import com.genenakagaki.splitstep.exercise.data.entity.ExerciseType;
import com.genenakagaki.splitstep.exercise.ui.model.DurationDisplayable;
import com.raizlabs.android.dbflow.config.DatabaseConfig;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

/**
 * Created by Gene on 9/13/2017.
 */
@RunWith(AndroidJUnit4.class)
public class ExerciseDetailViewModelTest {

    private static final Exercise EXERCISE = new Exercise(ExerciseType.REGULAR_VALUE, "test");

    private Context context = InstrumentationRegistry.getTargetContext();

    @Before
    public void setUp() throws Exception {
        FlowManager.init(FlowConfig.builder(context)
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
    public void testSetDurationDisplay_WithDurationLessThanAMinute_ShouldSetCorrectDisplay() {
        EXERCISE.insert();
        ExerciseDetailViewModel viewModel = new ExerciseDetailViewModel(context, EXERCISE.id);

        DurationDisplayable durationDisplayable = new DurationDisplayable(DurationDisplayable.TYPE_REST_DURATION, 30);
        viewModel.setDurationDisplay(durationDisplayable);
        assertEquals(context.getString(R.string.duration_value_seconds, durationDisplayable.getSeconds()),
                durationDisplayable.getDisplay());
    }

    @Test
    public void testSetDurationDisplay_WithDurationMoreThanAMinute_ShouldSetCorrectDisplay() {
        EXERCISE.insert();
        ExerciseDetailViewModel viewModel = new ExerciseDetailViewModel(context, EXERCISE.id);

        DurationDisplayable durationDisplayable = new DurationDisplayable(DurationDisplayable.TYPE_REST_DURATION, 100);
        viewModel.setDurationDisplay(durationDisplayable);
        assertEquals(context.getString(R.string.duration_value, durationDisplayable.getMinutes(), durationDisplayable.getSeconds()),
                durationDisplayable.getDisplay());
    }

    @Test
    public void testSetRestDuration_ShouldUpdateRestDuration() {
        Exercise exercise = new Exercise(ExerciseType.REGULAR_VALUE, "test");
        exercise.restDuration = 10;
        exercise.insert();
        ExerciseDetailViewModel viewModel = new ExerciseDetailViewModel(context, exercise.id);
        viewModel.setExercise().test()
                .assertComplete();

        DurationDisplayable durationDisplayable = new DurationDisplayable(DurationDisplayable.TYPE_REST_DURATION, 20);
        viewModel.setRestDuration(durationDisplayable).test()
                .assertComplete();

        Exercise e = SQLite.select()
                .from(Exercise.class)
                .querySingle();

        assertEquals(20, e.restDuration);
    }

    @Test
    public void testSetSetDuration_ShouldUpdateSetDuration() {
        Exercise exercise = new Exercise(ExerciseType.REGULAR_VALUE, "test");
        exercise.setDuration = 10;
        exercise.insert();
        ExerciseDetailViewModel viewModel = new ExerciseDetailViewModel(context, exercise.id);
        viewModel.setExercise().test()
                .assertComplete();

        DurationDisplayable durationDisplayable = new DurationDisplayable(DurationDisplayable.TYPE_SET_DURATION, 20);
        viewModel.setSetDuration(durationDisplayable).test()
                .assertComplete();

        Exercise e = SQLite.select()
                .from(Exercise.class)
                .querySingle();

        assertEquals(20, e.setDuration);
    }
}
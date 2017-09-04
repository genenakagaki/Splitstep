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

    private Context mContext;

    @Before
    public void setUp() throws Exception {
        mContext = InstrumentationRegistry.getTargetContext();

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
    public void NoRowsIfNoExerciseInserted() {
        List<Exercise> exercises = SQLite.select()
                .from(Exercise.class)
                .queryList();

        assertEquals(0, exercises.size());
    }

    @Test
    public void ExerciseExistsInExerciseTableWhenExerciseInserted() {
        new Exercise(1, "test", false).insert();

        List<Exercise> exercises = SQLite.select()
                .from(Exercise.class)
                .queryList();

        assertTrue(exercises.size() > 0);
    }


}
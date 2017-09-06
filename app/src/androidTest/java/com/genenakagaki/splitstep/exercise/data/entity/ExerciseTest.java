package com.genenakagaki.splitstep.exercise.data.entity;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.genenakagaki.splitstep.exercise.data.ExerciseDatabase;
import com.raizlabs.android.dbflow.config.DatabaseConfig;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.raizlabs.android.dbflow.structure.database.transaction.ITransaction;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;
import com.raizlabs.android.dbflow.structure.database.transaction.Transaction;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

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
    public void testGetExerciseAsync_WithNoExerciseInserted_ShouldReturnEmptyList() throws InterruptedException {
        final CountDownLatch countDownLatch = new CountDownLatch(1);

        SQLite.select()
                .from(Exercise.class)
                .async()
                .queryListResultCallback(new QueryTransaction.QueryResultListCallback<Exercise>() {
                    @Override
                    public void onListQueryResult(QueryTransaction transaction, @NonNull List<Exercise> tResult) {
                        assertEquals(0, tResult.size());
                        countDownLatch.countDown();
                    }
                }).execute();

        countDownLatch.await(10, TimeUnit.SECONDS);
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
    public void testInsertAndGetExerciseAsync_ShouldReturnInsertedExercise() throws InterruptedException {
        final CountDownLatch countDownLatch = new CountDownLatch(1);

        FlowManager.getDatabase(ExerciseDatabase.class).beginTransactionAsync(new ITransaction() {
            @Override
            public void execute(DatabaseWrapper databaseWrapper) {
                EXERCISE.insert();
            }
        }).success(new Transaction.Success() {
            @Override
            public void onSuccess(@NonNull Transaction transaction) {
                SQLite.select()
                        .from(Exercise.class)
                        .async()
                        .queryListResultCallback(new QueryTransaction.QueryResultListCallback<Exercise>() {
                            @Override
                            public void onListQueryResult(QueryTransaction transaction, @NonNull List<Exercise> tResult) {
                                assertTrue(isExerciseEqual(EXERCISE, tResult.get(0)));
                                assertTrue(true);
                                countDownLatch.countDown();

                            }
                        }).execute();
            }
        }).build().execute();

        countDownLatch.await(10, TimeUnit.SECONDS);
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
    public void testUpdateExerciseAsync_ShouldBeUpdated() throws InterruptedException {
        final CountDownLatch countDownLatch = new CountDownLatch(1);

        final Exercise exerciseToUpdate = new Exercise(1, "test", false);
        final String newName = "new";

        FlowManager.getDatabase(ExerciseDatabase.class).beginTransactionAsync(new ITransaction() {
            @Override
            public void execute(DatabaseWrapper databaseWrapper) {
                exerciseToUpdate.insert();
                exerciseToUpdate.type = 2;
                exerciseToUpdate.favorite = true;
                exerciseToUpdate.name = newName;
                exerciseToUpdate.update();
            }
        }).success(new Transaction.Success() {
            @Override
            public void onSuccess(@NonNull Transaction transaction) {
                SQLite.select()
                        .from(Exercise.class)
                        .async()
                        .queryListResultCallback(new QueryTransaction.QueryResultListCallback<Exercise>() {
                            @Override
                            public void onListQueryResult(QueryTransaction transaction, @NonNull List<Exercise> tResult) {
                                assertTrue(isExerciseEqual(exerciseToUpdate, tResult.get(0)));
                                countDownLatch.countDown();
                            }
                        }).execute();
            }
        }).build().execute();

        countDownLatch.await(10, TimeUnit.SECONDS);
    }

    private boolean isExerciseEqual(Exercise e1, Exercise e2) {
        return e1.type == e2.type
                && e1.favorite == e2.favorite
                && e1.name.equals(e2.name);
    }
}
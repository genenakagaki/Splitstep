package com.genenakagaki.splitstep.exercise.data;

import com.genenakagaki.splitstep.exercise.data.entity.Exercise;
import com.genenakagaki.splitstep.exercise.data.entity.ExerciseType;
import com.genenakagaki.splitstep.exercise.data.entity.Exercise_Table;
import com.genenakagaki.splitstep.exercise.data.entity.ReactionExercise;
import com.genenakagaki.splitstep.exercise.data.entity.ReactionExercise_Table;
import com.genenakagaki.splitstep.exercise.data.entity.RepsExercise;
import com.genenakagaki.splitstep.exercise.data.entity.RepsExercise_Table;
import com.genenakagaki.splitstep.exercise.data.entity.TimedSetsExercise;
import com.genenakagaki.splitstep.exercise.data.entity.TimedSetsExercise_Table;
import com.genenakagaki.splitstep.exercise.data.exception.ExerciseAlreadyExistsException;
import com.genenakagaki.splitstep.exercise.data.exception.ExerciseNotFoundException;
import com.genenakagaki.splitstep.exercise.data.exception.InvalidExerciseColumnException;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.annotations.NonNull;
import timber.log.Timber;

/**
 * Created by gene on 9/9/17.
 */

public class ExerciseDao {

    private static ExerciseDao instance;

    public static ExerciseDao getInstance() {
        if (instance == null) {
            instance = new ExerciseDao();
        }
        return instance;
    }

    private ExerciseDao() {}

    public Single<Exercise> findById(final long exerciseId) {
        return Single.create(new SingleOnSubscribe<Exercise>() {
            @Override
            public void subscribe(@NonNull SingleEmitter<Exercise> e) throws Exception {
                Exercise exercise = SQLite.select()
                        .from(Exercise.class)
                        .where(Exercise_Table.id.eq(exerciseId))
                        .querySingle();

                if (exercise == null) {
                    e.onError(new ExerciseNotFoundException());
                } else {
                    e.onSuccess(exercise);
                }
            }
        });
    }

    public Single<List<Exercise>> findByType(final ExerciseType exerciseType) {
        return Single.create(new SingleOnSubscribe<List<Exercise>>() {
            @Override
            public void subscribe(@NonNull SingleEmitter<List<Exercise>> e) throws Exception {
                List<Exercise> exercises = SQLite.select()
                        .from(Exercise.class)
                        .where(Exercise_Table.type.eq(exerciseType.getValue()))
                        .queryList();

                e.onSuccess(exercises);
            }
        });
    }

    public Completable insert(final String name, final ExerciseType exerciseType) {
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(@NonNull CompletableEmitter e) throws Exception {
                if (name.length() < 1) {
                    Timber.d("InvalidExerciseColumnException");
                    e.onError(new InvalidExerciseColumnException());
                } else if (isConflict(name, exerciseType)) {
                    Timber.d("ExerciseAlreadyExistsException");
                    e.onError(new ExerciseAlreadyExistsException());
                } else {
                    Exercise exercise = new Exercise(exerciseType.getValue(), name, false);
                    long exerciseId = exercise.insert();

                    switch (exerciseType) {
                        case REPS:
                            new RepsExercise(exerciseId).insert();
                            break;
                        case TIMED_SETS:
                            new TimedSetsExercise(exerciseId).insert();
                            break;
                        case REACTION:
                            new ReactionExercise(exerciseId).insert();
                            break;
                    }

                    e.onComplete();
                }
            }
        });
    }

    public boolean isConflict(final String name, final ExerciseType exerciseType) {
        Exercise exercise = SQLite.select()
                .from(Exercise.class)
                .where(Exercise_Table.name.eq(name))
                .and(Exercise_Table.type.eq(exerciseType.getValue()))
                .querySingle();

        return exercise != null;
    }

    public Completable delete(final long exerciseId, final ExerciseType exerciseType) {
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(@NonNull CompletableEmitter e) throws Exception {
                SQLite.delete(Exercise.class)
                        .where(Exercise_Table.id.eq(exerciseId))
                        .execute();

                switch (exerciseType) {
                    case REPS:
                        SQLite.delete(RepsExercise.class)
                                .where(RepsExercise_Table.id.eq(exerciseId))
                                .execute();
                        break;
                    case TIMED_SETS:
                        SQLite.delete(TimedSetsExercise.class)
                                .where(TimedSetsExercise_Table.id.eq(exerciseId))
                                .execute();
                        break;
                    default: // REACTION
                        SQLite.delete(ReactionExercise.class)
                                .where(ReactionExercise_Table.id.eq(exerciseId))
                                .execute();
                }

                e.onComplete();
            }
        });
    }

}

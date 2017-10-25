package com.genenakagaki.splitstep.exercise.data;

import com.genenakagaki.splitstep.exercise.data.entity.Exercise;
import com.genenakagaki.splitstep.exercise.data.entity.ExerciseSubType;
import com.genenakagaki.splitstep.exercise.data.entity.ExerciseType;
import com.genenakagaki.splitstep.exercise.data.entity.Exercise_Table;
import com.genenakagaki.splitstep.exercise.data.entity.ReactionExercise;
import com.genenakagaki.splitstep.exercise.data.entity.ReactionExercise_Table;
import com.genenakagaki.splitstep.exercise.data.exception.ExerciseAlreadyExistsException;
import com.genenakagaki.splitstep.exercise.data.exception.ExerciseNotFoundException;
import com.genenakagaki.splitstep.exercise.data.exception.InvalidExerciseColumnsException;
import com.genenakagaki.splitstep.exercise.data.exception.InvalidExerciseNameException;
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

    private ExerciseDao() {
    }

    public Completable insert(final String name, final int exerciseType) {
        return insert(new Exercise(exerciseType, name));
    }

    public Completable insert(final Exercise exercise) {
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(@NonNull CompletableEmitter e) throws Exception {
                if (exercise.name.length() < 1) {
                    Timber.d("InvalidExerciseNameException");
                    e.onError(new InvalidExerciseNameException());
                } else if (!isNameAndTypeValid(exercise.name, exercise.type)) {
                    Timber.d("ExerciseAlreadyExistsException");
                    e.onError(new ExerciseAlreadyExistsException());
                } else {
                    long exerciseId = exercise.insert();

                    if (exercise.type == ExerciseType.REACTION.getValue()) {
                        new ReactionExercise(exerciseId).insert();
                    }

                    e.onComplete();
                }
            }
        });
    }

    public boolean isNameAndTypeValid(final String name, final int exerciseType) {
        Exercise exercise = SQLite.select()
                .from(Exercise.class)
                .where(Exercise_Table.name.eq(name))
                .and(Exercise_Table.type.eq(exerciseType))
                .querySingle();

        return exercise == null;
    }

    public Single<Exercise> findById(final long exerciseId) {
        return Single.create(new SingleOnSubscribe<Exercise>() {
            @Override
            public void subscribe(@NonNull SingleEmitter<Exercise> e) throws Exception {
                Exercise exercise = SQLite.select()
                        .from(Exercise.class)
                        .where(Exercise_Table.id.eq(exerciseId))
                        .querySingle();

                if (exercise == null) {
                    Timber.d("ExerciseNotFoundException");
                    e.onError(new ExerciseNotFoundException());
                } else {
                    Timber.d("Exercise found");
                    e.onSuccess(exercise);
                }
            }
        });
    }

    public Single<List<Exercise>> findByType(final int exerciseType) {
        return Single.create(new SingleOnSubscribe<List<Exercise>>() {
            @Override
            public void subscribe(@NonNull SingleEmitter<List<Exercise>> e) throws Exception {
                List<Exercise> exercises = SQLite.select()
                        .from(Exercise.class)
                        .where(Exercise_Table.type.eq(exerciseType))
                        .queryList();

                e.onSuccess(exercises);
            }
        });
    }

    public Completable update(final Exercise exercise) {
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(@NonNull CompletableEmitter e) throws Exception {
                if (isColumnsValid(exercise)) {
                    exercise.update();
                    e.onComplete();
                } else {
                    e.onError(new InvalidExerciseColumnsException());
                }
            }
        });
    }

    public boolean isColumnsValid(Exercise exercise) {
        if (exercise.sets < 1 || exercise.restDuration < 1) {
            return false;
        }
        switch (ExerciseSubType.fromValue(exercise.subType)) {
            case REPS:
                if (exercise.reps < 1) {
                    return false;
                }
                break;
            case TIMED_SETS:
                if (exercise.setDuration < 1) {
                    return false;
                }
                break;
        }
        return true;
    }

    public Completable delete(final long exerciseId, final int exerciseType) {
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(@NonNull CompletableEmitter e) throws Exception {
                SQLite.delete(Exercise.class)
                        .where(Exercise_Table.id.eq(exerciseId))
                        .execute();

                if (ExerciseType.fromValue(exerciseType) == ExerciseType.REACTION) {
                    SQLite.delete(ReactionExercise.class)
                            .where(ReactionExercise_Table.id.eq(exerciseId))
                            .execute();
                }

                e.onComplete();
            }
        });
    }

}

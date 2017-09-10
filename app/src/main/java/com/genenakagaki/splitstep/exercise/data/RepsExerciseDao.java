package com.genenakagaki.splitstep.exercise.data;

import com.genenakagaki.splitstep.exercise.data.entity.RepsExercise;
import com.genenakagaki.splitstep.exercise.data.entity.RepsExercise_Table;
import com.genenakagaki.splitstep.exercise.data.exception.ExerciseNotFoundException;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.annotations.NonNull;

/**
 * Created by gene on 9/9/17.
 */

public class RepsExerciseDao {

    private static RepsExerciseDao instance;

    public static RepsExerciseDao getInstance() {
        if (instance == null) {
            instance = new RepsExerciseDao();
        }
        return instance;
    }

    private RepsExerciseDao() {}

    public Single<RepsExercise> findById(final long exerciseId) {
        return Single.create(new SingleOnSubscribe<RepsExercise>() {
            @Override
            public void subscribe(@NonNull SingleEmitter<RepsExercise> e) throws Exception {
                RepsExercise exercise = SQLite.select()
                        .from(RepsExercise.class)
                        .where(RepsExercise_Table.id.eq(exerciseId))
                        .querySingle();

                if (exercise == null) {
                    e.onError(new ExerciseNotFoundException());
                } else {
                    e.onSuccess(exercise);
                }
            }
        });
    }

    public Completable insert(final RepsExercise repsExercise) {
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(@NonNull CompletableEmitter e) throws Exception {
                repsExercise.insert();

                e.onComplete();
            }
        });
    }
}

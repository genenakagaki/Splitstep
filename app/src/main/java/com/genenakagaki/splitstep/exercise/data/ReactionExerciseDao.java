package com.genenakagaki.splitstep.exercise.data;

import com.genenakagaki.splitstep.exercise.data.entity.ReactionExercise;
import com.genenakagaki.splitstep.exercise.data.entity.ReactionExercise_Table;
import com.genenakagaki.splitstep.exercise.data.exception.ExerciseNotFoundException;
import com.genenakagaki.splitstep.exercise.data.exception.InvalidExerciseColumnsException;
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

public class ReactionExerciseDao {

    private static ReactionExerciseDao instance;

    public static ReactionExerciseDao getInstance() {
        if (instance == null) {
            instance = new ReactionExerciseDao();
        }
        return instance;
    }

    private ReactionExerciseDao() {}

    public Single<ReactionExercise> findById(final long exerciseId) {
        return Single.create(new SingleOnSubscribe<ReactionExercise>() {
            @Override
            public void subscribe(@NonNull SingleEmitter<ReactionExercise> e) throws Exception {
                ReactionExercise reactionExercise = SQLite.select()
                        .from(ReactionExercise.class)
                        .where(ReactionExercise_Table.id.eq(exerciseId))
                        .querySingle();

                if (reactionExercise == null) {
                    e.onError(new ExerciseNotFoundException());
                } else {
                    e.onSuccess(reactionExercise);
                }
            }
        });
    }

    public Completable update(final ReactionExercise reactionExercise) {
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(@NonNull CompletableEmitter e) throws Exception {
                if (isColumnsValid(reactionExercise)) {
                    reactionExercise.update();
                    e.onComplete();
                } else {
                    e.onError(new InvalidExerciseColumnsException());
                }
            }
        });
    }

    public boolean isColumnsValid(ReactionExercise reactionExercise) {
        return reactionExercise.cones >= 2;
    }
}

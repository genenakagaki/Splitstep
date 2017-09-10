package com.genenakagaki.splitstep.exercise.ui.list;

import com.genenakagaki.splitstep.exercise.data.entity.Exercise;
import com.genenakagaki.splitstep.exercise.data.entity.ExerciseType;
import com.genenakagaki.splitstep.exercise.data.entity.Exercise_Table;
import com.genenakagaki.splitstep.exercise.data.entity.ReactionExercise;
import com.genenakagaki.splitstep.exercise.data.entity.ReactionExercise_Table;
import com.genenakagaki.splitstep.exercise.data.entity.RepsExercise;
import com.genenakagaki.splitstep.exercise.data.entity.RepsExercise_Table;
import com.genenakagaki.splitstep.exercise.data.entity.TimedSetsExercise;
import com.genenakagaki.splitstep.exercise.data.entity.TimedSetsExercise_Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.annotations.NonNull;

/**
 * Created by gene on 9/7/17.
 */

public class DeleteExerciseViewModel {

    private long exerciseId;
    private ExerciseType exerciseType;
    private String deleteMessage;

    public DeleteExerciseViewModel(long exerciseId, ExerciseType exerciseType, String deleteMessage) {
        this.exerciseId = exerciseId;
        this.exerciseType = exerciseType;
        this.deleteMessage = deleteMessage;
    }

    public String getDeleteMessage() {
        return deleteMessage;
    }

    public Completable deleteExerciseCompletable() {
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

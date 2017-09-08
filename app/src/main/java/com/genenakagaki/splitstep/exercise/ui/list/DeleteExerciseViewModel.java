package com.genenakagaki.splitstep.exercise.ui.list;

import com.genenakagaki.splitstep.exercise.data.entity.Exercise;
import com.genenakagaki.splitstep.exercise.data.entity.Exercise_Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by gene on 9/7/17.
 */

public class DeleteExerciseViewModel {

    private CompositeDisposable mDisposable;

    private long mExerciseId;
    private String mDeleteMessage;

    public DeleteExerciseViewModel(long exerciseId, String deleteMessage) {
        this.mExerciseId = exerciseId;
        this.mDeleteMessage = deleteMessage;
    }

    public CompositeDisposable getDisposable() {
        return mDisposable;
    }

    public void initDisposable() {
        mDisposable = new CompositeDisposable();
    }

    public String getDeleteMessage() {
        return mDeleteMessage;
    }

    public Completable deleteExercise() {
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(@NonNull CompletableEmitter e) throws Exception {
                SQLite.delete(Exercise.class)
                        .where(Exercise_Table.id.eq(mExerciseId))
                        .execute();
                e.onComplete();
            }
        });
    }
}

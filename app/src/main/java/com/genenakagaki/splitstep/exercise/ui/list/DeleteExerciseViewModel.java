package com.genenakagaki.splitstep.exercise.ui.list;

import com.genenakagaki.splitstep.exercise.data.ExerciseDao;
import com.genenakagaki.splitstep.exercise.data.entity.ExerciseType;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

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
        return ExerciseDao.getInstance().delete(exerciseId, exerciseType)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.computation());
    }
}

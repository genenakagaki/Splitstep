package com.genenakagaki.splitstep.exercise.ui.coach;

import android.content.Context;

import com.genenakagaki.splitstep.exercise.data.ReactionExerciseDao;
import com.genenakagaki.splitstep.exercise.data.entity.ReactionExercise;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Gene on 10/3/2017.
 */

public class ReactionCoachViewModel {

    private Context context;
    private long exerciseId;
    private ReactionExercise exercise;

    public ReactionCoachViewModel(Context context, long exerciseId) {
        this.context = context;
        this.exerciseId = exerciseId;
    }

    public Single<ReactionExercise> loadExercise() {
        return ReactionExerciseDao.getInstance().findById(exerciseId).map(new Function<ReactionExercise, ReactionExercise>() {
            @Override
            public ReactionExercise apply(@NonNull ReactionExercise exercise) throws Exception {
                ReactionCoachViewModel.this.exercise = exercise;
                return exercise;
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.computation());
    }

    public ReactionExercise getExercise() {
        return exercise;
    }
}

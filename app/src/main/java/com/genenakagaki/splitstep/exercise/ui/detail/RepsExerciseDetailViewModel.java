package com.genenakagaki.splitstep.exercise.ui.detail;

import android.content.Context;

import com.genenakagaki.splitstep.exercise.data.RepsExerciseDao;
import com.genenakagaki.splitstep.exercise.data.entity.RepsExercise;

import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.CompletableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.subjects.BehaviorSubject;

/**
 * Created by gene on 9/10/17.
 */

public class RepsExerciseDetailViewModel extends ExerciseDetailViewModel {

    private RepsExercise repsExercise;
    private BehaviorSubject<RepsExercise> repsExerciseSubject = BehaviorSubject.create();

    public RepsExerciseDetailViewModel(Context context, long exerciseId) {
        super(context, exerciseId);
    }

    public BehaviorSubject<RepsExercise> getRepsExerciseSubject() {
        return repsExerciseSubject;
    }

    public Completable setRepsExercise() {
        return RepsExerciseDao.getInstance().findById(getExerciseId()).flatMapCompletable(new Function<RepsExercise, CompletableSource>() {
            @Override
            public CompletableSource apply(@NonNull final RepsExercise repsExercise) throws Exception {
                return Completable.create(new CompletableOnSubscribe() {
                    @Override
                    public void subscribe(@NonNull CompletableEmitter e) throws Exception {
                        RepsExerciseDetailViewModel.this.repsExercise = repsExercise;
                        repsExerciseSubject.onNext(repsExercise);
                        e.onComplete();
                    }
                });
            }
        });
    }
}

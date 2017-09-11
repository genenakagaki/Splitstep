package com.genenakagaki.splitstep.exercise.ui.detail;

import android.content.Context;

import com.genenakagaki.splitstep.R;
import com.genenakagaki.splitstep.exercise.data.ExerciseDao;
import com.genenakagaki.splitstep.exercise.data.entity.Exercise;
import com.genenakagaki.splitstep.exercise.ui.model.DurationDisplayable;

import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.CompletableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.subjects.BehaviorSubject;

import static android.R.attr.duration;

/**
 * Created by gene on 8/21/17.
 */

public abstract class ExerciseDetailViewModel {

    private Context context;
    private long exerciseId;

    private Exercise exercise;
    private BehaviorSubject<Exercise> exerciseSubject = BehaviorSubject.create();

    private DurationDisplayable restDurationDisplayable;
    private BehaviorSubject<DurationDisplayable> restDurationSubject = BehaviorSubject.create();

    public ExerciseDetailViewModel(Context context, long exerciseId) {
        this.context = context;
        this.exerciseId = exerciseId;
    }

    protected long getExerciseId() {
        return exerciseId;
    }

    public BehaviorSubject<Exercise> getExerciseSubject() {
        return exerciseSubject;
    }

    public BehaviorSubject<DurationDisplayable> getRestDurationSubject() {
        return restDurationSubject;
    }

    public Completable setExercise() {
        return ExerciseDao.getInstance().findById(exerciseId).flatMapCompletable(new Function<Exercise, CompletableSource>() {
            @Override
            public CompletableSource apply(@NonNull final Exercise exercise) throws Exception {
                return Completable.create(new CompletableOnSubscribe() {
                    @Override
                    public void subscribe(@NonNull CompletableEmitter e) throws Exception {
                        ExerciseDetailViewModel.this.exercise = exercise;
                        exerciseSubject.onNext(exercise);
                        e.onComplete();
                    }
                });
            }
        });
    }

    public Completable setDisplayableDuration() {
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(@NonNull CompletableEmitter e) throws Exception {

            }
        })
        int minutes = duration / 60;
        int seconds = duration - minutes * 60;

        if (minutes == 0) {
            return context.getString(R.string.duration_value_seconds, seconds);
        } else {
            return context.getString(R.string.duration_value, minutes, seconds);
        }
    }

}

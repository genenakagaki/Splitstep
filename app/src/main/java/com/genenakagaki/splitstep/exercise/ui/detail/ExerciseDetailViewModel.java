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

/**
 * Created by gene on 8/21/17.
 */

public class ExerciseDetailViewModel {

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

    public Completable setExercise() {
        return ExerciseDao.getInstance().findById(exerciseId).flatMapCompletable(new Function<Exercise, CompletableSource>() {
            @Override
            public CompletableSource apply(@NonNull final Exercise exercise) throws Exception {
                return Completable.create(new CompletableOnSubscribe() {
                    @Override
                    public void subscribe(@NonNull CompletableEmitter e) throws Exception {
                        ExerciseDetailViewModel.this.exercise = exercise;
                        exerciseSubject.onNext(exercise);

                        restDurationDisplayable = new DurationDisplayable(
                                DurationDisplayable.TYPE_REST_DURATION, exercise.restDuration);
                        restDurationDisplayable.setTitle(context.getString(R.string.rest_duration));
                        e.onComplete();
                    }
                }).andThen(setRestDuration(restDurationDisplayable));
            }
        });
    }

    public Completable setReps(final int reps) {
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(@NonNull CompletableEmitter e) throws Exception {
                exercise.reps = reps;
                exercise.update();
            }
        });
    }

    public Completable setSets(final int sets) {
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(@NonNull CompletableEmitter e) throws Exception {
                exercise.sets = sets;
                exercise.update();
            }
        });
    }

    public DurationDisplayable getRestDurationDisplayable() {
        return restDurationDisplayable;
    }

    public BehaviorSubject<DurationDisplayable> getRestDurationSubject() {
        return restDurationSubject;
    }

    public Completable setRestDuration(final DurationDisplayable durationDisplayable) {
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(@NonNull CompletableEmitter e) throws Exception {
                if (durationDisplayable != null) {
                    restDurationDisplayable = durationDisplayable;
                }

                String display;
                int minutes = restDurationDisplayable.getMinutes();
                int seconds = restDurationDisplayable.getSeconds();

                if (minutes == 0) {
                    display = context.getString(R.string.duration_value_seconds, seconds);
                } else {
                    display = context.getString(R.string.duration_value, minutes, seconds);
                }
                restDurationDisplayable.setDisplay(display);

                exercise.restDuration = restDurationDisplayable.getDuration();
                exercise.update();

                getRestDurationSubject().onNext(restDurationDisplayable);
                e.onComplete();
            }
        });
    }


}

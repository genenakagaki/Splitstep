package com.genenakagaki.splitstep.exercise.ui.detail;

import android.content.Context;

import com.genenakagaki.splitstep.R;
import com.genenakagaki.splitstep.exercise.data.entity.RegularExercise;
import com.genenakagaki.splitstep.exercise.ui.model.DurationDisplayable;

import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.subjects.BehaviorSubject;

/**
 * Created by gene on 9/10/17.
 */

public class RepsExerciseDetailViewModel {

    private Context context;
    private long exerciseId;

    private RegularExercise regularExercise;
    private BehaviorSubject<RegularExercise> repsExerciseSubject = BehaviorSubject.create();

    private DurationDisplayable restDurationDisplayable;
    private BehaviorSubject<DurationDisplayable> restDurationSubject = BehaviorSubject.create();

    public RepsExerciseDetailViewModel(Context context, long exerciseId) {
        this.context = context;
        this.exerciseId = exerciseId;
    }

    public BehaviorSubject<RegularExercise> getRepsExerciseSubject() {
        return repsExerciseSubject;
    }

    public Completable setRepsExercise() {
//        return RepsExerciseDao.getInstance().findById(exerciseId).flatMapCompletable(new Function<RegularExercise, CompletableSource>() {
//            @Override
//            public CompletableSource apply(@NonNull final RegularExercise regularExercise) throws Exception {
//                return Completable.create(new CompletableOnSubscribe() {
//                    @Override
//                    public void subscribe(@NonNull CompletableEmitter e) throws Exception {
//                        RepsExerciseDetailViewModel.this.regularExercise = regularExercise;
//                        repsExerciseSubject.onNext(regularExercise);
//
//                        restDurationDisplayable = new DurationDisplayable(
//                                DurationDisplayable.TYPE_REST_DURATION, regularExercise.restDuration);
//                        restDurationDisplayable.setTitle(context.getString(R.string.rest_duration));
//                        e.onComplete();
//                    }
//                }).andThen(setRestDuration(restDurationDisplayable));
//            }
//        });
        return null;
    }

    public Completable setReps(final int reps) {
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(@NonNull CompletableEmitter e) throws Exception {
                regularExercise.reps = reps;
                regularExercise.update();
            }
        });
    }

    public Completable setSets(final int sets) {
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(@NonNull CompletableEmitter e) throws Exception {
                regularExercise.sets = sets;
                regularExercise.update();
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

                regularExercise.restDuration = restDurationDisplayable.getDuration();
                regularExercise.update();

                getRestDurationSubject().onNext(restDurationDisplayable);
                e.onComplete();
            }
        });
    }

}

package com.genenakagaki.splitstep.exercise.ui.detail;

import android.content.Context;

import com.genenakagaki.splitstep.R;
import com.genenakagaki.splitstep.exercise.data.RepsExerciseDao;
import com.genenakagaki.splitstep.exercise.data.entity.RepsExercise;
import com.genenakagaki.splitstep.exercise.ui.model.DurationDisplayable;

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

public class RepsExerciseDetailViewModel {

    private Context context;
    private long exerciseId;

    private BehaviorSubject<RepsExercise> repsExerciseSubject = BehaviorSubject.create();

    private DurationDisplayable restDurationDisplayable;
    private BehaviorSubject<DurationDisplayable> restDurationSubject = BehaviorSubject.create();

    public RepsExerciseDetailViewModel(Context context, long exerciseId) {
        this.context = context;
        this.exerciseId = exerciseId;
    }

    public BehaviorSubject<RepsExercise> getRepsExerciseSubject() {
        return repsExerciseSubject;
    }

    public Completable setRepsExercise() {
        return RepsExerciseDao.getInstance().findById(exerciseId).flatMapCompletable(new Function<RepsExercise, CompletableSource>() {
            @Override
            public CompletableSource apply(@NonNull final RepsExercise repsExercise) throws Exception {
                return Completable.create(new CompletableOnSubscribe() {
                    @Override
                    public void subscribe(@NonNull CompletableEmitter e) throws Exception {
                        repsExerciseSubject.onNext(repsExercise);

                        restDurationDisplayable = new DurationDisplayable(
                                DurationDisplayable.TYPE_REST_DURATION, repsExercise.restDuration);
                        restDurationDisplayable.setTitle(context.getString(R.string.rest_duration));
                        setRestDuration(restDurationDisplayable);
                        e.onComplete();
                    }
                });
            }
        });
    }

    public Completable setRepsExercise(final RepsExercise repsExercise) {
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(@NonNull CompletableEmitter e) throws Exception {
                repsExercise.id = exerciseId;
                repsExercise.update();
            }
        });
    }

    public DurationDisplayable getRestDurationDisplayable() {
        return restDurationDisplayable;
    }

    public BehaviorSubject<DurationDisplayable> getRestDurationSubject() {
        return restDurationSubject;
    }

    public void setRestDuration(final DurationDisplayable durationDisplayable) {
        String display;
        if (durationDisplayable.getMinutes() == 0) {
            display = context.getString(R.string.duration_value_seconds, durationDisplayable.getSeconds());
        } else {
            display = context.getString(
                    R.string.duration_value, durationDisplayable.getMinutes(), durationDisplayable.getSeconds());
        }
        durationDisplayable.setDisplay(display);
        restDurationDisplayable = durationDisplayable;
        restDurationSubject.onNext(restDurationDisplayable);
    }

}

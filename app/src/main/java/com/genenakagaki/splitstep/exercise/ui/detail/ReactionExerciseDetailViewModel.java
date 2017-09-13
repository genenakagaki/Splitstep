package com.genenakagaki.splitstep.exercise.ui.detail;

import android.content.Context;

import com.genenakagaki.splitstep.R;
import com.genenakagaki.splitstep.exercise.data.ReactionExerciseDao;
import com.genenakagaki.splitstep.exercise.data.entity.ReactionExercise;
import com.genenakagaki.splitstep.exercise.ui.model.DurationDisplayable;

import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.CompletableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.subjects.BehaviorSubject;

/**
 * Created by Gene on 9/13/2017.
 */

public class ReactionExerciseDetailViewModel {

    private Context context;
    private long exerciseId;

    private ReactionExercise reactionExercise;
    private BehaviorSubject<ReactionExercise> reactionExerciseSubject = BehaviorSubject.create();

    private DurationDisplayable repDuration;
    private BehaviorSubject<DurationDisplayable> repDurationSubject = BehaviorSubject.create();

    public ReactionExerciseDetailViewModel(Context context, long exerciseId) {
        this.context = context;
        this.exerciseId = exerciseId;
    }

    public BehaviorSubject<ReactionExercise> getReactionExerciseSubject() {
        return reactionExerciseSubject;
    }

    public Completable setReactionExercise() {
        return ReactionExerciseDao.getInstance().findById(exerciseId).flatMapCompletable(new Function<ReactionExercise, CompletableSource>() {
            @Override
            public CompletableSource apply(@NonNull final ReactionExercise exercise) throws Exception {
                return Completable.create(new CompletableOnSubscribe() {
                    @Override
                    public void subscribe(@NonNull CompletableEmitter e) throws Exception {
                        ReactionExerciseDetailViewModel.this.reactionExercise = exercise;
                        reactionExerciseSubject.onNext(exercise);

                        repDuration = new DurationDisplayable(
                                DurationDisplayable.TYPE_REST_DURATION, exercise.repDuration);
                        repDuration.setTitle(context.getString(R.string.rep_duration));

                        e.onComplete();
                    }
                }).andThen(setRepDuration(repDuration));
            }
        });
    }

    public Completable setCones(int cones) {
        reactionExercise.cones = cones;
        return ReactionExerciseDao.getInstance().update(reactionExercise);
    }

    public DurationDisplayable getRepDuration() {
        return repDuration;
    }

    public BehaviorSubject<DurationDisplayable> getRestDurationSubject() {
        return repDurationSubject;
    }

    public Completable setRepDuration(final DurationDisplayable durationDisplayable) {
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(@NonNull CompletableEmitter e) throws Exception {
                if (durationDisplayable != null) {
                    repDuration = durationDisplayable;
                }

                setDurationDisplay(repDuration);

                reactionExercise.repDuration = repDuration.getDuration();
                reactionExercise.update();

                repDurationSubject.onNext(repDuration);
                e.onComplete();
            }
        });
    }

    public void setDurationDisplay(DurationDisplayable durationDisplay) {
        String display;
        int minutes = durationDisplay.getMinutes();
        int seconds = durationDisplay.getSeconds();

        if (minutes == 0) {
            display = context.getString(R.string.duration_value_seconds, seconds);
        } else {
            display = context.getString(R.string.duration_value, minutes, seconds);
        }
        durationDisplay.setDisplay(display);
    }
}

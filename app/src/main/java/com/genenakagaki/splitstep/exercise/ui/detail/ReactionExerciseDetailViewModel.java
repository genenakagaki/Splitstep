package com.genenakagaki.splitstep.exercise.ui.detail;

import android.content.Context;

import com.genenakagaki.splitstep.R;
import com.genenakagaki.splitstep.base.BaseViewModel;
import com.genenakagaki.splitstep.exercise.data.ReactionExerciseDao;
import com.genenakagaki.splitstep.exercise.data.entity.ReactionExercise;
import com.genenakagaki.splitstep.exercise.ui.model.DurationDisplayable;

import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.CompletableSource;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;

/**
 * Created by Gene on 9/13/2017.
 */

public class ReactionExerciseDetailViewModel extends BaseViewModel {

    private long exerciseId;

    private ReactionExercise reactionExercise;
    private BehaviorSubject<ReactionExercise> reactionExerciseSubject = BehaviorSubject.create();

    private DurationDisplayable repDuration;
    private BehaviorSubject<DurationDisplayable> repDurationSubject = BehaviorSubject.create();

    public ReactionExerciseDetailViewModel(Context context, long exerciseId) {
        super(context);
        this.exerciseId = exerciseId;
    }

    public Observable<ReactionExercise> getReactionExerciseSubject() {
        return reactionExerciseSubject
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    public long getExerciseId() {
        return exerciseId;
    }

    public Completable loadReactionExercise() {
        return ReactionExerciseDao.getInstance().findById(exerciseId).flatMapCompletable(new Function<ReactionExercise, CompletableSource>() {
            @Override
            public CompletableSource apply(@NonNull final ReactionExercise exercise) throws Exception {
                return Completable.create(new CompletableOnSubscribe() {
                    @Override
                    public void subscribe(@NonNull CompletableEmitter e) throws Exception {
                        ReactionExerciseDetailViewModel.this.reactionExercise = exercise;
                        reactionExerciseSubject.onNext(exercise);

                        repDuration = new DurationDisplayable(
                                DurationDisplayable.TYPE_REP_DURATION, exercise.repDuration);
                        repDuration.setTitle(getContext().getString(R.string.rep_duration));

                        e.onComplete();
                    }
                }).andThen(setRepDuration(repDuration));
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.computation());
    }

    public Completable setCones(int cones) {
        if (reactionExercise == null) {
            return Completable.complete();
        }

        reactionExercise.cones = cones;
        return ReactionExerciseDao.getInstance().update(reactionExercise)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.computation());
    }

    public DurationDisplayable getRepDuration() {
        return repDuration;
    }

    public Observable<DurationDisplayable> getRestDurationSubject() {
        return repDurationSubject
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    public Completable setRepDuration(final DurationDisplayable durationDisplayable) {
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(@NonNull CompletableEmitter e) throws Exception {
                if (durationDisplayable != null && durationDisplayable.getDuration() > 0) {
                    repDuration = durationDisplayable;
                }

                setDurationDisplay(repDuration);

                reactionExercise.repDuration = repDuration.getDuration();
                reactionExercise.update();

                repDurationSubject.onNext(repDuration);
                e.onComplete();
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.computation());
    }

    public void setDurationDisplay(DurationDisplayable durationDisplay) {
        String display;
        int minutes = durationDisplay.getMinutes();
        int seconds = durationDisplay.getSeconds();

        if (minutes == 0) {
            display = getContext().getString(R.string.duration_value_seconds, seconds);
        } else {
            display = getContext().getString(R.string.duration_value, minutes, seconds);
        }
        durationDisplay.setDisplay(display);
    }
}

package com.genenakagaki.splitstep.exercise.ui.detail;

import android.content.Context;

import com.genenakagaki.splitstep.R;
import com.genenakagaki.splitstep.base.BaseViewModel;
import com.genenakagaki.splitstep.exercise.data.ExerciseDao;
import com.genenakagaki.splitstep.exercise.data.entity.Exercise;
import com.genenakagaki.splitstep.exercise.ui.list.ExerciseTitleViewModel;
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
 * Created by gene on 8/21/17.
 */

public class ExerciseDetailViewModel extends BaseViewModel {

    private ExerciseTitleViewModel exerciseTitleViewModel;

    private long exerciseId;
    private Exercise exercise;
    private BehaviorSubject<Exercise> exerciseSubject = BehaviorSubject.create();

    private DurationDisplayable restDuration;
    private BehaviorSubject<DurationDisplayable> restDurationSubject = BehaviorSubject.create();

    private DurationDisplayable setDuration;
    private BehaviorSubject<DurationDisplayable> setDurationSubject = BehaviorSubject.create();

    public ExerciseDetailViewModel(Context context, long exerciseId) {
        super(context);
        this.exerciseId = exerciseId;
        exerciseTitleViewModel = new ExerciseTitleViewModel(context, exercise, exerciseSubject);
    }

    public Observable<Exercise> getExerciseSubject() {
        return exerciseSubject
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    public Completable loadExercise() {
        return ExerciseDao.getInstance().findById(exerciseId).flatMapCompletable(new Function<Exercise, CompletableSource>() {
            @Override
            public CompletableSource apply(@NonNull final Exercise exercise) throws Exception {
                return Completable.create(new CompletableOnSubscribe() {
                    @Override
                    public void subscribe(@NonNull CompletableEmitter e) throws Exception {
                        ExerciseDetailViewModel.this.exercise = exercise;
                        exerciseSubject.onNext(exercise);

                        restDuration = new DurationDisplayable(
                                DurationDisplayable.TYPE_REST_DURATION, exercise.restDuration);
                        restDuration.setTitle(getContext().getString(R.string.rest_duration));

                        setDuration = new DurationDisplayable(
                                DurationDisplayable.TYPE_SET_DURATION, exercise.setDuration);
                        setDuration.setTitle(getContext().getString(R.string.set_duration));
                        e.onComplete();
                    }
                }).andThen(setRestDuration(restDuration)).andThen(setSetDuration(setDuration));
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.computation());
    }

    public Completable toggleExerciseFavorite() {
        return exerciseTitleViewModel.toggleExerciseFavorite();
    }

    public String getExerciseDisplayable() {
        return exerciseTitleViewModel.getExerciseDisplayable();
    }

    public Completable setReps(final int reps) {
        exercise.reps = reps;
        return ExerciseDao.getInstance().update(exercise)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.computation());
    }

    public Completable setSets(final int sets) {
        exercise.sets = sets;
        return ExerciseDao.getInstance().update(exercise)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.computation());
    }

    public Completable setNotes(final String notes) {
        exercise.notes = notes;
        return ExerciseDao.getInstance().update(exercise)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.computation());
    }

    public DurationDisplayable getRestDuration() {
        return restDuration;
    }

    public Observable<DurationDisplayable> getRestDurationSubject() {
        return restDurationSubject
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    public Completable setRestDuration(final DurationDisplayable durationDisplayable) {
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(@NonNull CompletableEmitter e) throws Exception {
                if (durationDisplayable != null) {
                    restDuration = durationDisplayable;
                }

                setDurationDisplay(restDuration);

                exercise.restDuration = restDuration.getDuration();
                exercise.update();

                restDurationSubject.onNext(restDuration);
                e.onComplete();
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.computation());
    }

    public DurationDisplayable getSetDuration() {
        return setDuration;
    }

    public Observable<DurationDisplayable> getSetDurationSubject() {
        return setDurationSubject
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    public Completable setSetDuration(final DurationDisplayable durationDisplayable) {
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(@NonNull CompletableEmitter e) throws Exception {
                if (durationDisplayable != null) {
                    setDuration = durationDisplayable;
                }

                setDurationDisplay(setDuration);

                exercise.setDuration = setDuration.getDuration();
                exercise.update();

                setDurationSubject.onNext(setDuration);
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

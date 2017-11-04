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
import timber.log.Timber;

/**
 * Created by gene on 8/21/17.
 */

public class ExerciseDetailViewModel extends BaseViewModel {

    private ExerciseTitleViewModel exerciseTitleViewModel;

    private long exerciseId;
    private Exercise exercise;
    private BehaviorSubject<Exercise> exerciseSubject;

    private DurationDisplayable restDuration;
    private BehaviorSubject<DurationDisplayable> restDurationSubject;

    private DurationDisplayable setDuration;
    private BehaviorSubject<DurationDisplayable> setDurationSubject;

    public ExerciseDetailViewModel(Context context, long exerciseId) {
        super(context);
        this.exerciseId = exerciseId;
        exerciseTitleViewModel = new ExerciseTitleViewModel(context, exercise, exerciseSubject);
        exerciseSubject = BehaviorSubject.create();
        restDurationSubject = BehaviorSubject.create();
        setDurationSubject = BehaviorSubject.create();
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
                        Timber.d("Rest duration is: " + exercise.restDuration);
                        Timber.d("Rep  is: " + exercise.reps);
                        Timber.d("Set is: " + exercise.sets);
                        Timber.d("Type is: " + exercise.type);
                        Timber.d("SubType is: " + exercise.subType);
                        Timber.d("Name is: " + exercise.name);
                        Timber.d("Notes is: " + exercise.notes);
                        Timber.d("Favorite is: " + exercise.favorite);
                        Timber.d("Setduration is: " + exercise.setDuration);
                        setExercise(exercise);
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

    public long getExerciseId() {
        return exerciseId;
    }

    public Completable toggleExerciseFavorite() {
        return exerciseTitleViewModel.toggleExerciseFavorite();
    }

    public String getExerciseDisplay() {
        return exerciseTitleViewModel.getExerciseDisplay();
    }

    public Completable setReps(final int reps) {
        if (exercise == null) {
            return Completable.complete();
        }
        exercise.reps = reps;
        return ExerciseDao.getInstance().update(exercise)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.computation());
    }

    public Completable setSets(final int sets) {
        if (exercise == null) {
            return Completable.complete();
        }

        exercise.sets = sets;
        return ExerciseDao.getInstance().update(exercise)
                .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.computation());
    }

    public Completable setNotes(final String notes) {
        if (exercise == null) {
            return Completable.complete();
        }

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
                if (durationDisplayable != null && durationDisplayable.getDuration() > 0) {
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
                if (durationDisplayable != null && durationDisplayable.getDuration() > 0) {
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

    private void setExercise(Exercise exercise) {
        this.exercise = exercise;
        this.exerciseTitleViewModel.setExercise(exercise);
    }

}

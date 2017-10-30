package com.genenakagaki.splitstep.exercise.ui.list;

import android.content.Context;

import com.genenakagaki.splitstep.base.BaseViewModel;
import com.genenakagaki.splitstep.exercise.data.ExerciseDao;
import com.genenakagaki.splitstep.exercise.data.entity.Exercise;
import com.genenakagaki.splitstep.exercise.data.entity.ExerciseType;

import java.util.List;

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

public class ExerciseListViewModel extends BaseViewModel{

    private ExerciseType exerciseType;
    private boolean isEditMode;

    private BehaviorSubject<List<Exercise>> exercisesSubject = BehaviorSubject.create();

    public ExerciseListViewModel(Context context, ExerciseType exerciseType) {
        super(context);
        this.exerciseType = exerciseType;
    }

    public Observable<List<Exercise>> getExercisesSubject() {
        return exercisesSubject
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    public void setEditMode(boolean isEditMode) {
        this.isEditMode = isEditMode;
    }

    public boolean isEditMode() {
        return isEditMode;
    }

//    public String getTitle() {
//        switch (exerciseType) {
//            case REGULAR:
//                return context.getString(R.string.exercise);
//            default: // REACTION:
//                return context.getString(R.string.reaction_exercise);
//        }
//    }

    public Completable loadExerciseList() {
        Timber.d("loadExerciseList");

        return ExerciseDao.getInstance().findByType(exerciseType.getValue()).flatMapCompletable(new Function<List<Exercise>, CompletableSource>() {
            @Override
            public CompletableSource apply(@NonNull final List<Exercise> exercises) throws Exception {
                return Completable.create(new CompletableOnSubscribe() {
                    @Override
                    public void subscribe(@NonNull CompletableEmitter e) throws Exception {
                        exercisesSubject.onNext(exercises);
                        e.onComplete();
                    }
                });
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.computation());
    }

    public void setExerciseType(int exerciseType) {
        this.exerciseType = ExerciseType.fromValue(exerciseType);
    }

    public ExerciseType getExerciseType() {
        return exerciseType;
    }
}

package com.genenakagaki.splitstep.exercise.ui.list;

import android.content.Context;

import com.genenakagaki.splitstep.R;
import com.genenakagaki.splitstep.exercise.data.ExerciseDao;
import com.genenakagaki.splitstep.exercise.data.entity.Exercise;
import com.genenakagaki.splitstep.exercise.data.entity.ExerciseSubType;
import com.genenakagaki.splitstep.exercise.data.entity.ExerciseType;
import com.genenakagaki.splitstep.exercise.ui.model.ErrorMessage;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;

/**
 * Created by Gene on 9/6/2017.
 */

public class AddExerciseViewModel {

    private Context context;
    private ExerciseType exerciseType;
    private ExerciseSubType exerciseSubType;

    private ErrorMessage errorMessage;
    private BehaviorSubject<ErrorMessage> errorMessageSubject = BehaviorSubject.create();

    public AddExerciseViewModel(Context context, ExerciseType exerciseType) {
        this.context = context;
        this.exerciseType = exerciseType;
        this.exerciseSubType = ExerciseSubType.REPS;
        errorMessage = new ErrorMessage();
    }

    public Observable<ErrorMessage> getErrorMessageSubject() {
        return errorMessageSubject
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    public void setExerciseSubType(ExerciseSubType subType) {
        exerciseSubType = subType;
    }

    public Completable insertExercise(final String name) {
        return ExerciseDao.getInstance().insert(
                new Exercise(exerciseType.getValue(), exerciseSubType.getValue(), name))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.computation());
    }

    public void setExerciseAlreadyExistsError() {
        errorMessage.setValid(false);
        errorMessage.setErrorMessage(context.getString(R.string.error_exercise_already_exists));
        errorMessageSubject.onNext(errorMessage);
    }

    public void setInvalidExerciseNameError() {
        errorMessage.setValid(false);
        errorMessage.setErrorMessage(context.getString(R.string.error_empty_exercise_name));
        errorMessageSubject.onNext(errorMessage);
    }
}

package com.genenakagaki.splitstep.exercise.ui.add;

import android.content.Context;

import com.genenakagaki.splitstep.R;
import com.genenakagaki.splitstep.exercise.data.ExerciseDao;
import com.genenakagaki.splitstep.exercise.data.entity.ExerciseType;
import com.genenakagaki.splitstep.exercise.ui.model.ErrorMessage;

import io.reactivex.Completable;
import io.reactivex.subjects.BehaviorSubject;

/**
 * Created by Gene on 9/6/2017.
 */

public class AddExerciseViewModel {

    private Context context;
    private ExerciseType exerciseType;

    private ErrorMessage errorMessage;
    private BehaviorSubject<ErrorMessage> errorMessageSubject = BehaviorSubject.create();

    public AddExerciseViewModel(Context context, ExerciseType exerciseType) {
        this.context = context;
        this.exerciseType = exerciseType;
        errorMessage = new ErrorMessage();
    }

    public BehaviorSubject<ErrorMessage> getErrorMessageSubject() {
        return errorMessageSubject;
    }

    public Completable insertExercise(final String name) {
        return ExerciseDao.getInstance().insert(name, exerciseType);
    }

    public void setExerciseAlreadyExistsError() {
        errorMessage.setValid(false);
        errorMessage.setErrorMessage(context.getString(R.string.error_exercise_already_exists));
        errorMessageSubject.onNext(errorMessage);
    }

    public void setEmptyExerciseNameError() {
        errorMessage.setValid(false);
        errorMessage.setErrorMessage(context.getString(R.string.error_empty_exercise_name));
        errorMessageSubject.onNext(errorMessage);
    }
}

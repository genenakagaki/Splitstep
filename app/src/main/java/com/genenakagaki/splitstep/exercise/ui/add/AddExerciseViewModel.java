package com.genenakagaki.splitstep.exercise.ui.add;

import android.content.Context;

import com.genenakagaki.splitstep.R;
import com.genenakagaki.splitstep.exercise.data.entity.Exercise;
import com.genenakagaki.splitstep.exercise.data.entity.ExerciseType;
import com.genenakagaki.splitstep.exercise.data.entity.Exercise_Table;
import com.genenakagaki.splitstep.exercise.data.entity.ReactionExercise;
import com.genenakagaki.splitstep.exercise.data.entity.RepsExercise;
import com.genenakagaki.splitstep.exercise.data.entity.TimedSetsExercise;
import com.genenakagaki.splitstep.exercise.ui.model.ValidationModel;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.annotations.NonNull;

/**
 * Created by Gene on 9/6/2017.
 */

public class AddExerciseViewModel {

    private Context mContext;
    private ExerciseType mExerciseType;

    public AddExerciseViewModel(Context context, ExerciseType exerciseType) {
        mContext = context;
        mExerciseType = exerciseType;
    }

    public Single<ValidationModel> validateExerciseNameSingle(final String exerciseName) {
        return Single.create(new SingleOnSubscribe<ValidationModel>() {
            @Override
            public void subscribe(@NonNull SingleEmitter<ValidationModel> e) throws Exception {
                ValidationModel validationModel = new ValidationModel();

                if (exerciseName.length() < 1) {
                    validationModel.setValid(false);
                    validationModel.setErrorMessage(mContext.getString(R.string.error_empty_exercise_name));
                    e.onSuccess(validationModel);
                    return;
                }

                List<Exercise> exercises = SQLite.select()
                        .from(Exercise.class)
                        .where(Exercise_Table.type.eq(mExerciseType.getValue()))
                        .and(Exercise_Table.name.eq(exerciseName))
                        .queryList();

                if (exercises.size() == 0) {
                    validationModel.setValid(true);
                    e.onSuccess(validationModel);
                } else {
                    validationModel.setValid(false);
                    validationModel.setErrorMessage(mContext.getString(R.string.error_exercise_name_duplicate));
                    e.onSuccess(validationModel);
                }
            }
        });
    }

    public Completable insertExerciseCompletable(final String name) {
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(@NonNull CompletableEmitter e) throws Exception {
                Exercise exercise = new Exercise(mExerciseType.getValue(), name, false);
                long exerciseId = exercise.insert();

                switch (mExerciseType) {
                    case REPS:
                        new RepsExercise(exerciseId).insert();
                        break;
                    case TIMED_SETS:
                        new TimedSetsExercise(exerciseId).insert();
                        break;
                    case REACTION:
                        new ReactionExercise(exerciseId).insert();
                        break;
                }

                e.onComplete();
            }
        });
    }

}

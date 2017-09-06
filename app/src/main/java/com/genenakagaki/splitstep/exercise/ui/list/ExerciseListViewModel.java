package com.genenakagaki.splitstep.exercise.ui.list;

import android.content.Context;

import com.genenakagaki.splitstep.R;
import com.genenakagaki.splitstep.exercise.data.ExerciseSharedPref;
import com.genenakagaki.splitstep.exercise.data.entity.Exercise;
import com.genenakagaki.splitstep.exercise.data.entity.ExerciseType;
import com.genenakagaki.splitstep.exercise.data.entity.Exercise_Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;

import java.util.List;

import io.reactivex.functions.Consumer;
import io.reactivex.subjects.BehaviorSubject;
import timber.log.Timber;

/**
 * Created by gene on 8/21/17.
 */

public class ExerciseListViewModel {

    private Context mContext;
    private ExerciseType mExerciseType;

    private BehaviorSubject<List<Exercise>> mExercisesSubject = BehaviorSubject.create();

    public ExerciseListViewModel(Context context) {
        mContext = context;
        mExerciseType = ExerciseSharedPref.getExerciseType(context);
    }

    public ExerciseType getExerciseType() {
        return mExerciseType;
    }

    public BehaviorSubject<List<Exercise>> getExercisesSubject() {
        return mExercisesSubject;
    }

    public String getTitle() {
        switch (mExerciseType) {
            case REPS:
                return mContext.getString(R.string.reps_exercise);
            case TIMED_SETS:
                return mContext.getString(R.string.timed_sets_exercise);
            default: // REACTION:
                return mContext.getString(R.string.reaction_exercise);
        }
    }

    public void test() {
        BehaviorSubject<String> subject = BehaviorSubject.create();
        subject.subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Timber.d(s);
            }
        });
    }

    public List<Exercise> getExercises() {
        return SQLite.select()
                .from(Exercise.class)
                .queryList();
    }

    public void getExerciseList() {
        Timber.d("getExerciseList");

        SQLite.select()
                .from(Exercise.class)
                .where(Exercise_Table.type.eq(mExerciseType.getValue()))
                .async()
                .queryListResultCallback(new QueryTransaction.QueryResultListCallback<Exercise>() {
                    @Override
                    public void onListQueryResult(QueryTransaction transaction, @android.support.annotation.NonNull List<Exercise> tResult) {
                        Timber.d("triggerOnNext");
                        mExercisesSubject.onNext(tResult);
                    }
                }).execute();
    }
}

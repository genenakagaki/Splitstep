package com.genenakagaki.splitstep.exercise.ui.list;

import android.content.Context;

import com.genenakagaki.splitstep.R;
import com.genenakagaki.splitstep.exercise.data.ExerciseSharedPref;
import com.genenakagaki.splitstep.exercise.data.entity.Exercise;
import com.genenakagaki.splitstep.exercise.data.entity.ExerciseType;
import com.genenakagaki.splitstep.exercise.data.entity.Exercise_Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;
import timber.log.Timber;


/**
 * Created by gene on 8/21/17.
 */

public class ExerciseListViewModel {

    private Context mContext;
    private CompositeDisposable mDisposable;

    private ExerciseType mExerciseType;
    private boolean mIsEditMode;

    private BehaviorSubject<List<Exercise>> mExercisesSubject = BehaviorSubject.create();

    public ExerciseListViewModel(Context context) {
        mContext = context;
        mExerciseType = ExerciseSharedPref.getExerciseType(context);
    }

    public CompositeDisposable getDisposable() {
        return mDisposable;
    }

    public void initDisposable() {
        mDisposable = new CompositeDisposable();
    }

    public BehaviorSubject<List<Exercise>> getExercisesSubject() {
        return mExercisesSubject;
    }

    public void setEditMode(boolean isEditMode) {
        mIsEditMode = isEditMode;
    }

    public boolean isEditMode() {
        return mIsEditMode;
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

    public void getExerciseList() {
        Timber.d("getExerciseList");

        Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(@NonNull CompletableEmitter e) throws Exception {
                List<Exercise> exercises = SQLite.select()
                        .from(Exercise.class)
                        .where(Exercise_Table.type.eq(mExerciseType.getValue()))
                        .queryList();

                mExercisesSubject.onNext(exercises);
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.computation())
                .subscribe();
    }
}

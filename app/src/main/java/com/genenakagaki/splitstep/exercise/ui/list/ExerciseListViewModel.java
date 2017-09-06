package com.genenakagaki.splitstep.exercise.ui.list;

import android.content.Context;
import android.os.SystemClock;

import com.genenakagaki.splitstep.exercise.data.ExerciseSharedPref;
import com.genenakagaki.splitstep.exercise.data.entity.Exercise;
import com.genenakagaki.splitstep.exercise.data.entity.ExerciseType;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import timber.log.Timber;

/**
 * Created by gene on 8/21/17.
 */

public class ExerciseListViewModel {

    private Context mContext;
    private ExerciseType mExerciseType;

    public ExerciseListViewModel(Context context) {
        mContext = context;
        mExerciseType = ExerciseSharedPref.getExerciseType(context);
    }

    public ExerciseType getExerciseType() {
        return mExerciseType;
    }

    public Flowable<List<Exercise>> getExerciseList() {
        Timber.d("getExerciseList");

        Observable<Integer> serverDownloadObservable = Observable.create(new Exercise() {
            
            SystemClock.sleep(10000); // simulate delay
            emitter.onNext(5);
            emitter.onComplete();
        });

//        ExerciseDatabase db = ExerciseDatabase.getInstance(mContext);
//        switch (mExerciseType) {
//            case REPS:
//                return db.exerciseDao().getRepsExercises();
//            case TIMED_SETS:
//                return db.exerciseDao().getTimedSetsExercises();
//            default: // REACTION
//                return db.exerciseDao().getReactionExercises();
//        }
    }
}

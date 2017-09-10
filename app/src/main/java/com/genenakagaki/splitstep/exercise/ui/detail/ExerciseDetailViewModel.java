package com.genenakagaki.splitstep.exercise.ui.detail;

import android.content.Context;

import com.genenakagaki.splitstep.exercise.data.entity.Exercise;
import com.genenakagaki.splitstep.exercise.data.entity.ReactionExercise;
import com.genenakagaki.splitstep.exercise.data.entity.RepsExercise;
import com.genenakagaki.splitstep.exercise.data.entity.TimedSetsExercise;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

/**
 * Created by gene on 8/21/17.
 */

public class ExerciseDetailViewModel {

    private Context mContext;
    private Exercise mExercise;
    private long mExerciseId;

    public ExerciseDetailViewModel(Context context, long exerciseId) {
        mContext = context;
        mExerciseId = exerciseId;
    }

    public Exercise getExercise() {
        return mExercise;
    }

    public Single<RepsExercise> getRepsExercise() {
//        return Single.create(new SingleOnSubscribe<RepsExercise>() {
//            @Override
//            public void subscribe(@NonNull SingleEmitter<RepsExercise> e) throws Exception {
//                mExercise = SQLite.select()
//                        .from(Exercise.class)
//                        .where(Exercise_Table.id.eq(mExerciseId))
//                        .querySingle();
//
//                RepsExercise
//
//                e.onSuccess();
//            }
//        })
//        return ExerciseDatabase.getInstance(mContext).reps ExerciseDao().findById(mExercise.id);
        return null;
    }

    public Flowable<TimedSetsExercise> getTimedSetsExercise() {
//        return ExerciseDatabase.getInstance(mContext).timedSetsExerciseDao().findById(mExercise.id);
        return null;
    }

    public Flowable<ReactionExercise> getReactionExercise() {
//        return ExerciseDatabase.getInstance(mContext).reactionExerciseDao().findById(mExercise.id);
        return null;
    }

    public Completable updateRepsExercise(final RepsExercise repsExercise) {
//        return Completable.create(new CompletableOnSubscribe() {
//            @Override
//            public void subscribe(@NonNull CompletableEmitter e) throws Exception {
//                ExerciseDatabase db = ExerciseDatabase.getInstance(mContext);
//                db.repsExerciseDao().updateAll(repsExercise);
//                e.onComplete();
//            }
//        });
        return null;
    }

    public Completable updateTimedSetsExercise(final TimedSetsExercise timedSetsExercise) {
//        return Completable.create(new CompletableOnSubscribe() {
//            @Override
//            public void subscribe(@NonNull CompletableEmitter e) throws Exception {
//                ExerciseDatabase db = ExerciseDatabase.getInstance(mContext);
//                db.timedSetsExerciseDao().updateAll(timedSetsExercise);
//                e.onComplete();
//            }
//        });
        return null;
    }

    public Completable updateReactionExercise(final ReactionExercise reactionExercise) {
//        return Completable.create(new CompletableOnSubscribe() {
//            @Override
//            public void subscribe(@NonNull CompletableEmitter e) throws Exception {
//                ExerciseDatabase db = ExerciseDatabase.getInstance(mContext);
//                db.reactionExerciseDao().updateAll(reactionExercise);
//                e.onComplete();
//            }
//        });
        return null;
    }

}

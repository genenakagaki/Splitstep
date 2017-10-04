package com.genenakagaki.splitstep.exercise.ui.coach;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Gene on 10/3/2017.
 */

public class ConeViewModel {

    private int coneCount;

    public ConeViewModel(int coneCount) {
        this.coneCount = coneCount;
    }

    public Single<Integer> getNextCone() {
        return Single.create(new SingleOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull SingleEmitter<Integer> e) throws Exception {
                int nextCone = (int) (Math.random() * coneCount + 1);
                e.onSuccess(nextCone);
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.computation());
    }
}

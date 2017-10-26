package com.genenakagaki.splitstep.base;

import android.view.View;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by Gene on 10/25/2017.
 */

public class BaseView {

    private Unbinder mUnbinder;
    private CompositeDisposable mDisposable;

    public void bindView(Object target, View view) {
        mUnbinder = ButterKnife.bind(target, view);
    }

    public void onResume() {
        mDisposable = new CompositeDisposable();
    }

    public void onPause() {
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
    }

    public void onDestroyView() {
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
    }

    public void addDisposable(Disposable disposable) {
        if (mDisposable != null) {
            mDisposable.add(disposable);
        }
    }
}

package com.genenakagaki.splitstep.base;

import android.support.v4.app.DialogFragment;
import android.view.View;

import io.reactivex.disposables.Disposable;

/**
 * Created by Gene on 10/25/2017.
 */

public abstract class BaseDialogFragment extends DialogFragment {

    private BaseView mBaseView;

    private BaseView getBaseView() {
        if (mBaseView == null) {
            mBaseView = new BaseView();
        }
        return mBaseView;
    }

    public void bindView(Object target, View view) {
        getBaseView().bindView(target, view);
    }

    @Override
    public void onResume() {
        super.onResume();
        getBaseView().onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        getBaseView().onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getBaseView().onDestroyView();
    }

    public void addDisposable(Disposable disposable) {
        getBaseView().addDisposable(disposable);
    }
}

package com.genenakagaki.splitstep.base;

import android.content.Context;

/**
 * Created by Gene on 10/25/2017.
 */

public class BaseViewModel {

    private Context context;

    public BaseViewModel(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }
}

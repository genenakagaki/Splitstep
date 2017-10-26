package com.genenakagaki.splitstep.exercise.ui.detail;

import android.content.Context;

import com.genenakagaki.splitstep.R;
import com.genenakagaki.splitstep.base.BaseViewModel;
import com.genenakagaki.splitstep.exercise.ui.model.DurationDisplayable;
import com.genenakagaki.splitstep.exercise.ui.model.ErrorMessage;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;

/**
 * Created by gene on 9/8/17.
 */

public class DurationPickerViewModel extends BaseViewModel {

    protected static final String[] COLON_PICKER_DISPLAY_VALUES = new String[] {":"};
    protected static final String[] PICKER_DISPLAY_VALUE = buildPickerDisplayValues();
    protected static final int PICKER_MAX_VALUE = 59;

    private static String[] buildPickerDisplayValues() {
        String[] pickerDisplayValues = new String[60];
        for (int i = 0; i < 60; i++) {
            pickerDisplayValues[i] = String.format("%02d", i); //display in 2 digits
        }

        return pickerDisplayValues;
    }

    private DurationDisplayable durationDisplayable;

    private BehaviorSubject<ErrorMessage> errorMessageSubject = BehaviorSubject.create();

    public DurationPickerViewModel(Context context, DurationDisplayable durationDisplayable) {
        super(context);
        this.durationDisplayable = durationDisplayable;
    }

    public Observable<ErrorMessage> getErrorMessageSubject() {
        return errorMessageSubject
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    public DurationDisplayable getDurationDisplayable() {
        return durationDisplayable;
    }

    public void validateDuration(int minutes, int seconds) {
        durationDisplayable.setMinutes(minutes);
        durationDisplayable.setSeconds(seconds);

        ErrorMessage errorMessage = new ErrorMessage();
        if (minutes == 0 && seconds == 0) {
            errorMessage.setValid(false);
            errorMessage.setErrorMessage(getContext().getString(R.string.error_zero_duration));
        } else {
            errorMessage.setValid(true);
        }
        errorMessageSubject.onNext(errorMessage);
    }
}

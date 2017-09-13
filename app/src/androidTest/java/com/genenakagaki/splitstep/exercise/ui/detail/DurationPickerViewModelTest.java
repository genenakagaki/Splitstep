package com.genenakagaki.splitstep.exercise.ui.detail;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.genenakagaki.splitstep.R;
import com.genenakagaki.splitstep.exercise.ui.model.DurationDisplayable;
import com.genenakagaki.splitstep.exercise.ui.model.ErrorMessage;

import org.junit.Test;
import org.junit.runner.RunWith;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Predicate;

/**
 * Created by Gene on 9/13/2017.
 */
@RunWith(AndroidJUnit4.class)
public class DurationPickerViewModelTest {

    private Context context = InstrumentationRegistry.getTargetContext();

    @Test
    public void testValidateDuration_WithInvalidDuration_ShouldBeInvalid() {
        DurationDisplayable durationDisplayable = new DurationDisplayable(
                DurationDisplayable.TYPE_REST_DURATION, 0);

        DurationPickerViewModel viewModel = new DurationPickerViewModel(context, durationDisplayable);
        viewModel.validateDuration(0, 0);

        viewModel.getErrorMessageSubject()
                .test()
                .assertValue(new Predicate<ErrorMessage>() {
                    @Override
                    public boolean test(@NonNull ErrorMessage errorMessage) throws Exception {
                        return errorMessage.isValid() == false
                                && errorMessage.getErrorMessage().equals(context.getString(R.string.error_zero_duration));
                    }
                });
    }

    @Test
    public void testValidateDuration_WithValidDuration_ShouldBeValid() {
        DurationDisplayable durationDisplayable = new DurationDisplayable(
                DurationDisplayable.TYPE_REST_DURATION, 0);

        DurationPickerViewModel viewModel = new DurationPickerViewModel(context, durationDisplayable);
        viewModel.validateDuration(0, 30);

        viewModel.getErrorMessageSubject()
                .test()
                .assertValue(new Predicate<ErrorMessage>() {
                    @Override
                    public boolean test(@NonNull ErrorMessage errorMessage) throws Exception {
                        return errorMessage.isValid() == true;
                    }
                });
    }
}
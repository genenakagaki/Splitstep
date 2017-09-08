package com.genenakagaki.splitstep.exercise.ui.view;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Gene on 9/8/2017.
 */

public class NumberInputViewModelTest {

    @Test
    public void testIncrementNumber_WithNumberLessThanMax_ShouldIncrement() {
        NumberInputViewModel viewModel = new NumberInputViewModel();
        viewModel.setMax(10);

        int result = viewModel.incrementNumber(9);

        assertEquals(10, result);
    }

    @Test
    public void testIncrementNumber_WithNumberEqualToMax_ShouldNotIncrement() {
        NumberInputViewModel viewModel = new NumberInputViewModel();
        viewModel.setMax(10);

        int result = viewModel.incrementNumber(10);

        assertEquals(10, result);
    }

    @Test
    public void testIncrementNumber_WithNumberMoreThanMax_ShouldNotIncrement() {
        NumberInputViewModel viewModel = new NumberInputViewModel();
        viewModel.setMax(10);

        int result = viewModel.incrementNumber(20);

        assertEquals(20, result);
    }

}
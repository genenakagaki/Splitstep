package com.genenakagaki.splitstep.exercise.ui.view;

import org.junit.Test;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Predicate;

import static org.junit.Assert.assertEquals;

/**
 * Created by Gene on 9/8/2017.
 */

public class NumberInputViewModelTest {

    @Test
    public void testSetNumber_WithNumberLessThanMaxAndMoreThanMin_ShouldBeSet() {
        NumberInputViewModel viewModel = new NumberInputViewModel();
        viewModel.setMax(10);
        viewModel.setMin(1);
        viewModel.setNumber(5);
        assertEquals(5, viewModel.getNumber());
    }

    @Test
    public void testSetNumber_With_NumberMoreThanMax_ShouldBeSetToMax() {
        NumberInputViewModel viewModel = new NumberInputViewModel();
        viewModel.setMax(10);
        viewModel.setNumber(20);
        assertEquals(10, viewModel.getNumber());
    }

    @Test
    public void testSetNumber_With_NumberLessThanMin_ShouldBeSetToMin() {
        NumberInputViewModel viewModel = new NumberInputViewModel();
        viewModel.setMin(10);
        viewModel.setNumber(5);
        assertEquals(10, viewModel.getNumber());
    }

    @Test
    public void testIncrementNumber_WithNumberLessThanMax_ShouldIncrement() {
        NumberInputViewModel viewModel = new NumberInputViewModel();
        viewModel.setMax(10);
        viewModel.setNumber(9);
        viewModel.incrementNumber();

        viewModel.getNumberSubject()
                .test()
                .assertValue(new Predicate<Integer>() {
                    @Override
                    public boolean test(@NonNull Integer integer) throws Exception {
                        return integer.intValue() == 10;
                    }
                });
    }

    @Test
    public void testIncrementNumber_WithNumberEqualToMax_ShouldNotIncrement() {
        NumberInputViewModel viewModel = new NumberInputViewModel();
        viewModel.setMax(10);
        viewModel.setNumber(10);
        viewModel.incrementNumber();

        viewModel.getNumberSubject()
                .test()
                .assertValue(new Predicate<Integer>() {
                    @Override
                    public boolean test(@NonNull Integer integer) throws Exception {
                        return integer.intValue() == 10;
                    }
                });
    }

    @Test
    public void testDecrementNumber_WithNumberMoreThanMin_ShouldDecrement() {
        NumberInputViewModel viewModel = new NumberInputViewModel();
        viewModel.setMin(1);
        viewModel.setNumber(10);
        viewModel.decrementNumber();

        viewModel.getNumberSubject()
                .test()
                .assertValue(new Predicate<Integer>() {
                    @Override
                    public boolean test(@NonNull Integer integer) throws Exception {
                        return integer.intValue() == 9;
                    }
                });
    }

    @Test
    public void testDecrementNumber_WithNumberEqualToMin_ShouldNotDecrement() {
        NumberInputViewModel viewModel = new NumberInputViewModel();
        viewModel.setMin(1);
        viewModel.setNumber(1);
        viewModel.decrementNumber();

        viewModel.getNumberSubject()
                .test()
                .assertValue(new Predicate<Integer>() {
                    @Override
                    public boolean test(@NonNull Integer integer) throws Exception {
                        return integer.intValue() == 1;
                    }
                });
    }
}
package com.genenakagaki.splitstep.exercise.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.genenakagaki.splitstep.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by Gene on 9/8/2017.
 */

public class NumberInput extends FrameLayout {

    @BindView(R.id.input)
    EditText mInput;
    @BindView(R.id.label_textview)
    TextView mLabelTextView;

    private Unbinder mUnbinder;
    private CompositeDisposable mDisposable;
    private NumberInputViewModel mViewModel;

    public NumberInput(Context context) {
        super(context);

        mViewModel = new NumberInputViewModel();

        LayoutInflater.from(context).inflate(R.layout.number_input, this);
        mUnbinder = ButterKnife.bind(this);
    }

    public NumberInput(Context context, AttributeSet attrs) {
        super(context, attrs);
        mViewModel = new NumberInputViewModel();

        LayoutInflater.from(context).inflate(R.layout.number_input, this);
        mUnbinder = ButterKnife.bind(this);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.NumberInput);
        Timber.d(typedArray.getString(R.styleable.NumberInput_label));

        mLabelTextView.setText(typedArray.getString(R.styleable.NumberInput_label));

        int max = typedArray.getInteger(R.styleable.NumberInput_max, mViewModel.MAX_DEFAULT);
        int min = typedArray.getInteger(R.styleable.NumberInput_min, mViewModel.MIN_DEFAULT);
        mViewModel.setMax(max);
        mViewModel.setMin(min);

        typedArray.recycle();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mDisposable = new CompositeDisposable();

        mDisposable.add(mViewModel.getNumberSubject()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        mInput.setText(Integer.toString(integer));
                    }
                }));
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }

        mUnbinder.unbind();
    }

    @OnClick(R.id.plus_button)
    public void onClickPlusButton() {
        mViewModel.incrementNumber();
    }

    @OnClick(R.id.minus_button)
    public void onClickMinusButton() {
        mViewModel.decrementNumber();
    }

    public int getNumber() {
        return mViewModel.getNumber();
    }

    public void setNumber(int number) {
        mViewModel.setNumber(number);
    }
}

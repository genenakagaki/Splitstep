package com.genenakagaki.splitstep.exercise.ui.coach;

import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.genenakagaki.splitstep.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.CompositeDisposable;

import static android.R.attr.duration;

/**
 * Created by Gene on 11/6/2017.
 */

public class CircularProgressBar extends FrameLayout {

    public static final int INTERPOLATOR_LINEAR = 1;

    public interface OnProgressChangeListener {
        void onProgressChanged(int progress);
    }

    @BindView(R.id.progressbar)
    ProgressBar mProgressBar;
    @BindView(R.id.text)
    TextView mTextView;

    private Unbinder mUnbinder;
    private CompositeDisposable mDisposable;
    private CircularProgressBarViewModel mViewModel;
    private OnProgressChangeListener mOnProgressChangeListener;

    private TimeInterpolator mInterpolator;

    public CircularProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        mViewModel = new CircularProgressBarViewModel();

        LayoutInflater.from(context).inflate(R.layout.circular_progressbar, this);
        mUnbinder = ButterKnife.bind(this);

        setInterpolator(new LinearInterpolator());
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mDisposable = new CompositeDisposable();

        mDisposable.add(mViewModel.getProgress().subscribe(progress -> {
            animateProgress(progress);
            mOnProgressChangeListener.onProgressChanged(progress);
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

    public void animateProgress(int progress) {
        if (android.os.Build.VERSION.SDK_INT < 11) {
            mProgressBar.setProgress(progress);
        } else {
            ObjectAnimator animation = ObjectAnimator.ofInt(mProgressBar, "progress", progress);
            animation.setDuration(duration);
            animation.setInterpolator(mInterpolator);
            animation.start();
        }
    }

    public void setText(String text) {
        mTextView.setText(text);
    }

    // ----------------------------------------
    //   Setup
    // ----------------------------------------
    public void setMax(int max) {
        mViewModel.setMax(max);
    }

    public void setProgress(int progress) {
        mViewModel.setProgress(progress);
    }

    public void setInterpolator(TimeInterpolator interpolator) {
        mInterpolator = interpolator;
    }
}

package com.genenakagaki.splitstep.exercise.ui.view;

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
import timber.log.Timber;

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
    private ProgressBarViewModel mViewModel;
    private OnProgressChangeListener mOnProgressChangeListener;

    private TimeInterpolator mInterpolator;

    public CircularProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        mViewModel = new ProgressBarViewModel();

        LayoutInflater.from(context).inflate(R.layout.circular_progressbar, this);
        mUnbinder = ButterKnife.bind(this);

        setInterpolator(new LinearInterpolator());
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        Timber.d("onAttachedToWindow");
        mDisposable = new CompositeDisposable();

        mDisposable.add(mViewModel.getProgress().subscribe(progress -> {
            animateProgress(progress, mViewModel.getAnimateDuration());

            if (mOnProgressChangeListener != null) {
                mOnProgressChangeListener.onProgressChanged(progress);
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

    public void setProgress(int progress) {
        mViewModel.setProgress(progress);
    }

    private void animateProgress(int progress, int duration) {
        if (android.os.Build.VERSION.SDK_INT < 11) {
            mProgressBar.setProgress(progress);
        } else {
            Timber.d("setProgress: " + progress);
            ObjectAnimator animation = ObjectAnimator.ofInt(mProgressBar, "progress", progress);
            animation.setDuration(duration);
            animation.setInterpolator(mInterpolator);
            animation.start();
        }
    }


    public void incrementProgressBy(int diff) {
        mViewModel.incrementProgressBy(diff);
    }

    public void setText(String text) {
        mTextView.setText(text);
    }

    // ----------------------------------------
    //   Setup
    // ----------------------------------------
    public void setToMax() {
        mViewModel.setStartProgress(mViewModel.getMax());
        mProgressBar.setProgress(mViewModel.getMax());
    }

    public void setMax(int max) {
        Timber.d("setMax: " + max);
        mViewModel.setMax(max);
        mProgressBar.setMax(mViewModel.getMax());
    }

    public void setStartProgress(int progress) {
        mViewModel.setStartProgress(progress);
        mProgressBar.setProgress(0);
    }

    public void setAnimateDuration(int animateDuration) {
        mViewModel.setAnimateDuration(animateDuration);
    }

    public void setInterpolator(TimeInterpolator interpolator) {
        mInterpolator = interpolator;
    }
}

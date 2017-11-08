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
    private CircularProgressBarViewModel mViewModel;
    private OnProgressChangeListener mOnProgressChangeListener;

    private TimeInterpolator mInterpolator;

    public CircularProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        mViewModel = new CircularProgressBarViewModel();

        LayoutInflater.from(context).inflate(R.layout.circular_progressbar, this);
        mUnbinder = ButterKnife.bind(this);

        mProgressBar.setMax(mViewModel.getMax());

        setInterpolator(new LinearInterpolator());

//        int size = getWidth();
//        if (getHeight() > size) {
//            size = getHeight();
//        }
//        setLayoutParams(new FrameLayout.LayoutParams(size, size));

//        mContentLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                // Adjust views
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                    mContentLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//                } else {
//                    mContentLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
//                }
//
//                int size = mMainProgressBar.getWidth();
//                mMainProgressBar.setLayoutParams(new RelativeLayout.LayoutParams(size, size));
//                mRestProgressBar.setLayoutParams(new RelativeLayout.LayoutParams(size, size));
//
//                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
//                        (int) (size / 1.1f), (int) (size / 1.1f));
//                layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
//                mSetImageViewContainer.setLayoutParams(layoutParams);
//                mRestImageViewContainer.setLayoutParams(layoutParams);
//                FrameLayout.LayoutParams imageLayoutParams = new FrameLayout.LayoutParams(
//                        (int) (size / 1.2f), (int) (size / 1.2f));
//                imageLayoutParams.gravity = Gravity.CENTER;
//                mSetImageView.setLayoutParams(imageLayoutParams);
//                mRestImageView.setLayoutParams(imageLayoutParams);
//
//                mDoneButton.setLayoutParams(layoutParams);
//                mCompleteLayout.setLayoutParams(layoutParams);
//
//                size = mSubProgressBar.getWidth();
//                FrameLayout.LayoutParams frameLayoutParams = new FrameLayout.LayoutParams(size, size);
//                mSubProgressBar.setLayoutParams(frameLayoutParams);
//            }
//        });
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mDisposable = new CompositeDisposable();

        mDisposable.add(mViewModel.getProgress().subscribe(progress -> {
            animateProgress(progress);

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

    private void animateProgress(int progress) {
        if (android.os.Build.VERSION.SDK_INT < 11) {
            mProgressBar.setProgress(progress);
        } else {
            Timber.d("animateProgress: " + progress);
            ObjectAnimator animation = ObjectAnimator.ofInt(mProgressBar, "progress", progress);
            Timber.d("animateProgress duration: " + mViewModel.getAnimateDuration() * 1000);
            animation.setDuration(mViewModel.getAnimateDuration() * 1000);
            animation.setInterpolator(mInterpolator);
            animation.start();
        }
    }

    public void setToMax() {
        mViewModel.setStartProgress(mViewModel.getMax());
        mProgressBar.setProgress(mViewModel.getMax());
        Timber.d("setToMax: " + mProgressBar.getProgress());
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
    public void setMax(int max) {
        mViewModel.setMax(max);
        mProgressBar.setMax(mViewModel.getMax());
        Timber.d("setMax: " + mProgressBar.getMax());
    }

    public void setStartProgress(int progress) {
        mViewModel.setStartProgress(progress);
        mProgressBar.setProgress(progress);
    }

    public void setAnimateDuration(int animateDuration) {
        mViewModel.setAnimateDuration(animateDuration);
    }

    public void setInterpolator(TimeInterpolator interpolator) {
        mInterpolator = interpolator;
    }
}

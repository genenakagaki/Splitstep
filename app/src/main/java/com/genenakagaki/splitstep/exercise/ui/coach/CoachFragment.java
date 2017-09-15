package com.genenakagaki.splitstep.exercise.ui.coach;

import android.animation.ObjectAnimator;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.genenakagaki.splitstep.R;
import com.genenakagaki.splitstep.exercise.data.ExerciseSharedPref;
import com.genenakagaki.splitstep.exercise.data.entity.Exercise;
import com.genenakagaki.splitstep.exercise.data.entity.ExerciseSubType;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

/**
 * Created by Gene on 9/13/2017.
 */

public abstract class CoachFragment extends Fragment {

    @BindView(R.id.content)
    RelativeLayout mContentLayout;
    @BindView(R.id.exercise_name_textview)
    TextView mExerciseNameTextView;
    @BindView(R.id.sets_progressbar)
    ProgressBar mSetsProgressBar;
    @BindView(R.id.rest_progressbar)
    ProgressBar mRestProgressBar;
    @BindView(R.id.timed_set_progressbar)
    ProgressBar mTimedSetProgressBar;
    @BindView(R.id.main_progressbar_container)
    LinearLayout mMainProgressBarContainer;
    @BindView(R.id.sets_progress_text)
    TextView mSetsProgressText;
    @BindView(R.id.main_progress_text)
    TextView mMainProgressText;
    @BindView(R.id.main_progress_top_text)
    TextView mMainProgressTopText;
    @BindView(R.id.done_button)
    Button mDoneButton;
    @BindView(R.id.complete_layout)
    LinearLayout mCompleteLayout;
    @BindView(R.id.overlay)
    FrameLayout mOverlay;
    @BindView(R.id.overlay_textview)
    TextView mOverlayTextView;

    private Unbinder mUnbinder;
    private CompositeDisposable mDisposable;
    private CoachViewModel mViewModel;

    private Ringtone mAlarm;

    public CoachFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewModel = new CoachViewModel(getActivity(), ExerciseSharedPref.getExerciseId(getActivity()));

        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        mAlarm = RingtoneManager.getRingtone(getActivity(), notification);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_coach, container, false);
        mUnbinder = ButterKnife.bind(this, view);

        mCompleteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        mContentLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // Adjust views
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    mContentLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    mContentLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }

                int size = mTimedSetProgressBar.getWidth();
                mTimedSetProgressBar.setLayoutParams(new RelativeLayout.LayoutParams(size, size));
                mRestProgressBar.setLayoutParams(new RelativeLayout.LayoutParams(size, size));

                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                        (int) (size / 1.1f), (int) (size / 1.1f));
                layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
                mDoneButton.setLayoutParams(layoutParams);
                mCompleteLayout.setLayoutParams(layoutParams);

                size = mSetsProgressBar.getWidth();
                FrameLayout.LayoutParams frameLayoutParams = new FrameLayout.LayoutParams(size, size);
                mSetsProgressBar.setLayoutParams(frameLayoutParams);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mDisposable = new CompositeDisposable();

        mDisposable.add(mViewModel.getStartCountDown()
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        if (aLong == 0) {
                            mOverlay.setVisibility(View.GONE);
                            startExerciseSet();
                        } else {
                            mOverlayTextView.setText(Long.toString(aLong));
                        }
                    }
                }));

        mDisposable.add(mViewModel.getExerciseSubject().subscribe(new Consumer<Exercise>() {
            @Override
            public void accept(Exercise exercise) throws Exception {
                mExerciseNameTextView.setText(exercise.name);

                mSetsProgressText.setText(Integer.toString(exercise.sets));
                setProgressMax(mSetsProgressBar, exercise.sets);
                setProgressMax(mRestProgressBar, exercise.restDuration);
                setProgress(mSetsProgressBar, 0, 0);
                setProgress(mRestProgressBar, exercise.restDuration, 0);

                switch (ExerciseSubType.fromValue(exercise.subType)) {
                    case REPS:
                        mMainProgressTopText.setText(getString(R.string.reps_count, exercise.reps));
                        mDoneButton.setVisibility(View.VISIBLE);
                        break;
                    case TIMED_SETS:
                        mTimedSetProgressBar.setVisibility(View.VISIBLE);
                        setProgressMax(mTimedSetProgressBar, exercise.setDuration);
                        setProgress(mTimedSetProgressBar, mTimedSetProgressBar.getMax(), 100);
                        break;
                }
            }
        }));

        mDisposable.add(mViewModel.setExercise().subscribe());
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();

        cancelTimers();
    }

    public void setProgressMax(ProgressBar progressBar, int max) {
        progressBar.setMax(max * 100);
    }

    public void setProgress(ProgressBar progressBar, int progress, long duration) {
        if (duration == 0 || android.os.Build.VERSION.SDK_INT < 11) {
            progressBar.setProgress(progress * 100);
        } else {
            ObjectAnimator animation = ObjectAnimator.ofInt(progressBar, "progress", progress * 100);
            animation.setDuration(duration);
            animation.setInterpolator(new LinearInterpolator());
            animation.start();
        }
    }

    public void playAlarm() {
        mAlarm.play();
    }

    public CoachViewModel getViewModel() {
        return mViewModel;
    }

    public CompositeDisposable getDisposable() {
        return mDisposable;
    }

    public abstract void startExerciseSet();

    public abstract void startRest();

    public abstract void cancelTimers();
}

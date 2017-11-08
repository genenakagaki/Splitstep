package com.genenakagaki.splitstep.exercise.ui.coach;

import android.animation.ObjectAnimator;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.genenakagaki.splitstep.R;
import com.genenakagaki.splitstep.base.BaseFragment;
import com.genenakagaki.splitstep.exercise.data.entity.Exercise;
import com.genenakagaki.splitstep.exercise.receiver.CoachAlarmBroadcastReceiver;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;
import timber.log.Timber;

/**
 * Created by Gene on 9/13/2017.
 */

public abstract class CoachFragment extends BaseFragment {

    private static final String EXERCISE_ID_KEY = "EXERCISE_ID_KEY";

    public static CoachFragment newInstance(long exerciseId, CoachFragment fragment) {
        Bundle args = new Bundle();
        args.putLong(EXERCISE_ID_KEY, exerciseId);
        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R.id.content)
    RelativeLayout mContentLayout;
    @BindView(R.id.exercise_name_textview)
    TextView mExerciseNameTextView;
    @BindView(R.id.sub_progressbar)
    ProgressBar mSubProgressBar;
    @BindView(R.id.rest_imageview_container)
    FrameLayout mRestImageViewContainer;
    @BindView(R.id.rest_imageview)
    ImageView mRestImageView;
    @BindView(R.id.set_imageview_container)
    FrameLayout mSetImageViewContainer;
    @BindView(R.id.set_imageview)
    ImageView mSetImageView;
//    @BindView(R.id.rest_progressbar)
//    ProgressBar mRestProgressBar;
    @BindView(R.id.rest_progressbar)
    CircularProgressBar mRestProgressBar;
    @BindView(R.id.main_progressbar)
    ProgressBar mMainProgressBar;
    @BindView(R.id.main_progressbar_container)
    LinearLayout mMainProgressBarContainer;
    @BindView(R.id.sub_progress_text)
    TextView mSubProgressText;
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

    private CoachViewModel mViewModel;
    private ProgressViewModel mSetsProgressViewModel;
//    private TimerViewModel mRestTimerViewModel;

    private Ringtone mAlarm;

    public CoachFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            long exerciseId = getArguments().getLong(EXERCISE_ID_KEY);
            mViewModel = new CoachViewModel(getActivity(), exerciseId);
        }

        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        mAlarm = RingtoneManager.getRingtone(getActivity(), notification);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_coach, container, false);
        bindView(this, view);

        mOverlayTextView.setText(Integer.toString(3));

        mContentLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // Adjust views
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    mContentLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    mContentLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }

                int size = mMainProgressBar.getWidth();
                mMainProgressBar.setLayoutParams(new RelativeLayout.LayoutParams(size, size));
                mRestProgressBar.setLayoutParams(new RelativeLayout.LayoutParams(size, size));

                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                        (int) (size / 1.1f), (int) (size / 1.1f));
                layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
                mSetImageViewContainer.setLayoutParams(layoutParams);
                mRestImageViewContainer.setLayoutParams(layoutParams);
                FrameLayout.LayoutParams imageLayoutParams = new FrameLayout.LayoutParams(
                        (int) (size / 1.2f), (int) (size / 1.2f));
                imageLayoutParams.gravity = Gravity.CENTER;
                mSetImageView.setLayoutParams(imageLayoutParams);
                mRestImageView.setLayoutParams(imageLayoutParams);

                mDoneButton.setLayoutParams(layoutParams);
                mCompleteLayout.setLayoutParams(layoutParams);

                size = mSubProgressBar.getWidth();
                FrameLayout.LayoutParams frameLayoutParams = new FrameLayout.LayoutParams(size, size);
                mSubProgressBar.setLayoutParams(frameLayoutParams);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        addDisposable(mViewModel.getStartCountDown().subscribe(
                count -> mOverlayTextView.setText(Long.toString(count)),
                throwable -> {},
                () -> {
                    mOverlay.setVisibility(View.GONE);
                    startExerciseSet();
                }));

        addDisposable(mViewModel.loadExercise().subscribe(new Consumer<Exercise>() {
            @Override
            public void accept(Exercise exercise) throws Exception {
                mExerciseNameTextView.setText(exercise.name);

                mSetsProgressViewModel = new ProgressViewModel(exercise.sets, 0);
                mSubProgressBar.setMax(mSetsProgressViewModel.getMax());
                addDisposable(mSetsProgressViewModel.getProgress().subscribe(integer -> {
                    mSubProgressText.setText(mSetsProgressViewModel.getDisplayProgress());
                    animateProgress(mSubProgressBar, integer, mSetsProgressViewModel.getAnimateDuration());
                }));

//                mRestTimerViewModel = new TimerViewModel(
//                        new DurationDisplayable(DurationDisplayable.TYPE_REST_DURATION, exercise.restDuration));
//                mRestProgressBar.setMax(mRestTimerViewModel.getMax());
                mRestProgressBar.setMax(exercise.restDuration);
                mRestProgressBar.setAnimateDuration(exercise.restDuration);

                setupExerciseSet();
            }
        }));
    }

    @Override
    public void onPause() {
        Timber.d("onPause");
        super.onPause();

        AlarmManager am = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getActivity(), CoachAlarmBroadcastReceiver.class);
        intent.putExtra("a", Boolean.FALSE);
        PendingIntent pi = PendingIntent.getBroadcast(getActivity(), 0, intent, 0);
        //After after 5 seconds
        am.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + 5000,
                pi);
    }

    @OnClick(R.id.overlay)
    public void onClickOverlay() {
        // prevent clicks
        return;
    }

    @OnClick(R.id.complete_layout)
    public void onClickComplete() {
        FragmentManager fm = getFragmentManager();
        FragmentManager.BackStackEntry stack = fm.getBackStackEntryAt(0);
        fm.popBackStack(stack.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    public void onFinishExerciseSet() {
        mAlarm.play();
        mSetsProgressViewModel.incrementProgressBy(1);

        if (mSetsProgressViewModel.isFinished()) {
            mCompleteLayout.setVisibility(View.VISIBLE);
        } else {
//            mRestProgressBar.setStartProgress(mRestTimerViewModel.getMax());
            mRestProgressBar.setToMax();
//            mRestProgressBar.setProgress(0);
//            animateProgress(mR\estProgressBar, 0, mRestTimerViewModel.getAnimateDuration());
//            mMainProgressText.setText(mRestTimerViewModel.getTimerDisplay());

//            addDisposable(mRestTimerViewModel.startTimer().subscribe(
//                    s -> mMainProgressText.setText(s),
//                    throwable -> {},
//                    () -> startExerciseSet()));
        }
    }

    public void animateProgress(ProgressBar progressBar, int progress, long duration) {
        if (android.os.Build.VERSION.SDK_INT < 11) {
            progressBar.setProgress(progress);
        } else {
            ObjectAnimator animation = ObjectAnimator.ofInt(progressBar, "progress", progress);
            animation.setDuration(duration);
            animation.setInterpolator(new LinearInterpolator());
            animation.start();
        }
    }

    public CoachViewModel getViewModel() {
        return mViewModel;
    }

    public abstract void setupExerciseSet();
    public abstract void startExerciseSet();
}

package com.genenakagaki.splitstep.exercise.ui.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.genenakagaki.splitstep.R;
import com.genenakagaki.splitstep.exercise.data.entity.ReactionExercise;
import com.genenakagaki.splitstep.exercise.ui.ExerciseActivity;
import com.genenakagaki.splitstep.exercise.ui.coach.CoachFragment;
import com.genenakagaki.splitstep.exercise.ui.coach.ReactionCoachFragment;
import com.genenakagaki.splitstep.exercise.ui.model.DurationDisplayable;
import com.genenakagaki.splitstep.exercise.ui.view.NumberInput;

import butterknife.OnClick;
import io.reactivex.functions.Consumer;
import timber.log.Timber;

/**
 * Created by gene on 9/1/17.
 */

public class ReactionExerciseDetailFragment extends ExerciseDetailFragment {

    protected static final String EXERCISE_ID_KEY = "EXERCISE_ID_KEY";

    public static ReactionExerciseDetailFragment newInstance(long exerciseId) {
        ReactionExerciseDetailFragment fragment = new ReactionExerciseDetailFragment();
        Bundle args = new Bundle();
        args.putLong(EXERCISE_ID_KEY, exerciseId);
        fragment.setArguments(args);
        return fragment;
    }

    private ReactionExerciseDetailViewModel mViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.d("oncreate reaction");

        if (getArguments() != null) {
            long exerciseId = getArguments().getLong(EXERCISE_ID_KEY);
            mViewModel = new ReactionExerciseDetailViewModel(getActivity(), exerciseId);
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        mConesNumberInput.setVisibility(View.VISIBLE);
        mRepDurationLayout.setVisibility(View.VISIBLE);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        mConesNumberInput.setOnInputChangedListener(new NumberInput.OnInputChangedListener() {
            @Override
            public void onInputChanged(View view, int number) {
                Timber.d("onInputChanged cones");
                mViewModel.setCones(number).subscribe();
            }
        });

        addDisposable(mViewModel.getReactionExerciseSubject()
                .subscribe(new Consumer<ReactionExercise>() {
                    @Override
                    public void accept(ReactionExercise reactionExercise) throws Exception {
                        mConesNumberInput.setNumber(reactionExercise.cones);
                    }
                }));

        addDisposable(mViewModel.getRestDurationSubject()
                .subscribe(new Consumer<DurationDisplayable>() {
                    @Override
                    public void accept(DurationDisplayable durationDisplayable) throws Exception {
                        mRepDurationTextView.setText(durationDisplayable.getDisplay());
                    }
                }));

        addDisposable(mViewModel.loadReactionExercise().subscribe());
    }

    @OnClick(R.id.rep_duration_layout)
    public void onClickRepDuration() {
        DurationPickerDialog fragment =
                DurationPickerDialog.newInstance(mViewModel.getRepDuration());
        fragment.show(getFragmentManager(), DurationPickerDialog.class.getSimpleName());
    }

    @Override
    public void setDuration(DurationDisplayable durationDisplayable) {
        super.setDuration(durationDisplayable);

        if (durationDisplayable.getType() == DurationDisplayable.TYPE_REP_DURATION) {
            addDisposable(mViewModel.setRepDuration(durationDisplayable).subscribe());
        }
    }

    @OnClick(R.id.start_exercise_button)
    public void onClickStartExercise() {
        Timber.d("onClickStartExercise");
        ExerciseActivity activity = (ExerciseActivity) getActivity();
        Fragment fragment = ReactionCoachFragment.newInstance(mViewModel.getExerciseId());
        activity.showFragment(fragment, CoachFragment.class.getSimpleName(), true);
    }
}

package com.genenakagaki.splitstep.exercise.ui.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.genenakagaki.splitstep.R;
import com.genenakagaki.splitstep.exercise.data.ExerciseSharedPref;
import com.genenakagaki.splitstep.exercise.data.entity.ReactionExercise;
import com.genenakagaki.splitstep.exercise.ui.model.DurationDisplayable;

import butterknife.OnClick;
import io.reactivex.functions.Consumer;
import timber.log.Timber;

/**
 * Created by gene on 9/1/17.
 */

public class ReactionExerciseDetailFragment extends ExerciseDetailFragment {

    private ReactionExerciseDetailViewModel mViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewModel = new ReactionExerciseDetailViewModel(getActivity(), ExerciseSharedPref.getExerciseId(getActivity()));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        mConesNumberInput.setVisibility(View.VISIBLE);
        mRepDurationLayout.setVisibility(View.VISIBLE);

        mConesNumberInput.setOnInputChangedListener(this);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        getDisposable().add(mViewModel.getReactionExerciseSubject()
                .subscribe(new Consumer<ReactionExercise>() {
                    @Override
                    public void accept(ReactionExercise reactionExercise) throws Exception {
                        mConesNumberInput.setNumber(reactionExercise.cones);
                    }
                }));

        getDisposable().add(mViewModel.getRestDurationSubject()
                .subscribe(new Consumer<DurationDisplayable>() {
                    @Override
                    public void accept(DurationDisplayable durationDisplayable) throws Exception {
                        mRepDurationTextView.setText(durationDisplayable.getDisplay());
                    }
                }));

        getDisposable().add(mViewModel.setReactionExercise().subscribe());
    }

    @Override
    public void onInputChanged(View view, int number) {
        super.onInputChanged(view, number);

        Timber.d("onInputChanged " + number);
        if (view.getId() == mConesNumberInput.getId()) {
            Timber.d("cones");
            mViewModel.setCones(number).subscribe();
        }
    }

    @OnClick(R.id.rep_duration_layout)
    public void onClickSetDuration() {
        DurationPickerDialog fragment =
                DurationPickerDialog.newInstance(mViewModel.getRepDuration());
        fragment.show(getFragmentManager(), DurationPickerDialog.class.getSimpleName());
    }

    @Override
    public void setDuration(DurationDisplayable durationDisplayable) {
        super.setDuration(durationDisplayable);

        if (durationDisplayable.getType() == DurationDisplayable.TYPE_REP_DURATION) {
            getDisposable().add(mViewModel.setRepDuration(durationDisplayable).subscribe());
        }
    }
}

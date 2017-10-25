package com.genenakagaki.splitstep.exercise.ui.type;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.genenakagaki.splitstep.R;
import com.genenakagaki.splitstep.exercise.data.ExerciseSharedPref;
import com.genenakagaki.splitstep.exercise.data.entity.ExerciseType;
import com.genenakagaki.splitstep.exercise.ui.ExerciseActivity;
import com.genenakagaki.splitstep.exercise.ui.list.ExerciseListFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import timber.log.Timber;

/**
 * Created by gene on 8/2/17.
 */

public class ExerciseTypeFragment extends Fragment {

    @BindView(R.id.reps_exercise_button)  Button mRepsButton;
    @BindView(R.id.reaction_exercise_button) Button mReactionButton;

    private Unbinder mUnbinder;

    public ExerciseTypeFragment() {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Timber.d("onCreateView");

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_exercise_type, container, false);
        mUnbinder = ButterKnife.bind(this, rootView);

        return rootView;
    }

    @Override
    public void onResume() {
        Timber.d("onResume");
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        Timber.d("onDestroyView");
        super.onDestroyView();
        mUnbinder.unbind();
    }

    @OnClick({ R.id.reps_exercise_button, R.id.reaction_exercise_button })
    public void onButtonClick(View view) {
        if (view.getId() == R.id.reps_exercise_button) {
            Timber.d("onClick on reps exercise");
            ExerciseSharedPref.setExerciseType(getActivity(), ExerciseType.REGULAR);
        } else { // R.id.reaction_exercise_button
            Timber.d("onClick on reaction exercise");
            ExerciseSharedPref.setExerciseType(getActivity(), ExerciseType.REACTION);
        }

        ExerciseActivity activity = (ExerciseActivity) getActivity();
        activity.showFragment(new ExerciseListFragment(), ExerciseListFragment.class.getSimpleName(), true);
    }
}

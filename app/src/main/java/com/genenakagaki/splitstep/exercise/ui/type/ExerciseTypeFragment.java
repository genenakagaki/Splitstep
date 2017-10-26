package com.genenakagaki.splitstep.exercise.ui.type;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.genenakagaki.splitstep.R;
import com.genenakagaki.splitstep.base.BaseFragment;
import com.genenakagaki.splitstep.exercise.data.entity.ExerciseType;
import com.genenakagaki.splitstep.exercise.ui.ExerciseActivity;
import com.genenakagaki.splitstep.exercise.ui.list.ExerciseListFragment;

import butterknife.BindView;
import butterknife.OnClick;
import timber.log.Timber;

/**
 * Created by gene on 8/2/17.
 */

public class ExerciseTypeFragment extends BaseFragment {

    @BindView(R.id.regular_exercise_button)
    Button mRepsButton;
    @BindView(R.id.reaction_exercise_button)
    Button mReactionButton;

    public ExerciseTypeFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Timber.d("onCreateView");

        getActivity().setTitle(R.string.app_name);

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_exercise_type, container, false);
        bindView(this, rootView);

        return rootView;
    }

    @OnClick({R.id.regular_exercise_button, R.id.reaction_exercise_button})
    public void onButtonClick(View view) {
        ExerciseType exerciseType;
        if (view.getId() == R.id.regular_exercise_button) {
            Timber.d("onClick on regular exercise");
            exerciseType = ExerciseType.REGULAR;
        } else { // R.id.reaction_exercise_button
            Timber.d("onClick on reaction exercise");
            exerciseType = ExerciseType.REACTION;
        }

        ExerciseActivity activity = (ExerciseActivity) getActivity();
        activity.showFragment(
                ExerciseListFragment.newInstance(exerciseType.getValue()),
                ExerciseListFragment.class.getSimpleName(),
                true);
    }
}

package com.genenakagaki.splitstep.exercise.ui.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.genenakagaki.splitstep.R;
import com.genenakagaki.splitstep.base.BaseFragment;
import com.genenakagaki.splitstep.exercise.data.entity.Exercise;
import com.genenakagaki.splitstep.exercise.data.entity.ExerciseSubType;
import com.genenakagaki.splitstep.exercise.data.entity.ExerciseType;
import com.genenakagaki.splitstep.exercise.ui.ExerciseActivity;
import com.genenakagaki.splitstep.exercise.ui.coach.CoachFragment;
import com.genenakagaki.splitstep.exercise.ui.coach.RegularCoachFragment;
import com.genenakagaki.splitstep.exercise.ui.model.DurationDisplayable;
import com.genenakagaki.splitstep.exercise.ui.view.NumberInput;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;
import timber.log.Timber;

/**
 * Created by Gene on 9/8/2017.
 */

public class ExerciseDetailFragment extends BaseFragment
        implements NumberInput.OnInputChangedListener {

    private static final String EXERCISE_ID_KEY = "EXERCISE_ID_KEY";

    public static ExerciseDetailFragment newInstance(long exerciseId) {
        ExerciseDetailFragment fragment = new ExerciseDetailFragment();
        Bundle args = new Bundle();
        args.putLong(EXERCISE_ID_KEY, exerciseId);
        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R.id.exercise_name_textview)
    TextView mExerciseNameTextView;
    @BindView(R.id.favorite_imageswitcher)
    ImageSwitcher mFavoriteImageSwitcher;
    @BindView(R.id.notes_input)
    TextInputEditText mNotesInput;
    @BindView(R.id.sets_numberinput)
    NumberInput mSetsNumberInput;
    @BindView(R.id.reps_numberinput)
    NumberInput mRepsNumberInput;
    @BindView(R.id.cones_numberinput)
    NumberInput mConesNumberInput;
    @BindView(R.id.set_duration_layout)
    LinearLayout mSetDurationLayout;
    @BindView(R.id.set_duration_textview)
    TextView mSetDurationTextView;
    @BindView(R.id.rep_duration_layout)
    LinearLayout mRepDurationLayout;
    @BindView(R.id.rep_duration_textview)
    TextView mRepDurationTextView;
    @BindView(R.id.rest_duration_layout)
    LinearLayout mRestDurationLayout;
    @BindView(R.id.rest_duration_textview)
    TextView mRestDurationTextView;

    private ExerciseDetailViewModel mViewModel;

    public ExerciseDetailFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            long exerciseId = getArguments().getLong(EXERCISE_ID_KEY);
            mViewModel = new ExerciseDetailViewModel(getActivity(), exerciseId);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Timber.d("onCreateView");
        View view = inflater.inflate(R.layout.fragment_exercise_detail, container, false);
        bindView(this, view);

        mConesNumberInput.setVisibility(View.GONE);
        mRepDurationLayout.setVisibility(View.GONE);

        mSetsNumberInput.setOnInputChangedListener(this);
        mRepsNumberInput.setOnInputChangedListener(this);
        mNotesInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                addDisposable(mViewModel.setNotes(charSequence.toString())
                        .subscribe());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        mFavoriteImageSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView switcherImageView = new ImageView(getActivity());
                switcherImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                switcherImageView.setLayoutParams(new ImageSwitcher.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                return switcherImageView;
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ExerciseActivity activity = (ExerciseActivity) getActivity();
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        addDisposable(mViewModel.getExerciseSubject().subscribe(new Consumer<Exercise>() {
            @Override
            public void accept(Exercise exercise) throws Exception {
                ((ExerciseActivity) getActivity())
                        .setTitle(ExerciseType.fromValue(exercise.type));

                setExerciseNameText(exercise);

                setInputValues(exercise);
            }
        }));

        addDisposable(mViewModel.getSetDurationSubject()
                .subscribe(new Consumer<DurationDisplayable>() {
                    @Override
                    public void accept(DurationDisplayable durationDisplayable) throws Exception {
                        mSetDurationTextView.setText(durationDisplayable.getDisplay());
                    }
                }));

        addDisposable(mViewModel.getRestDurationSubject()
                .subscribe(new Consumer<DurationDisplayable>() {
                    @Override
                    public void accept(DurationDisplayable durationDisplayable) throws Exception {
                        mRestDurationTextView.setText(durationDisplayable.getDisplay());
                    }
                }));

        addDisposable(mViewModel.loadExercise().subscribe());
    }

    @Override
    public void onInputChanged(View view, int number) {
        Timber.d("onInputChanged " + number);
        if (view.getId() == mRepsNumberInput.getId()) {
            Timber.d("reps");
            mViewModel.setReps(number).subscribe();
        } else if (view.getId() == mSetsNumberInput.getId()) {
            Timber.d("sets");
            mViewModel.setSets(number).subscribe();
        }
    }

    @OnClick(R.id.favorite_imageswitcher)
    public void onClickFavorite() {
        addDisposable(mViewModel.toggleExerciseFavorite().subscribe());
    }

    @OnClick(R.id.rest_duration_layout)
    public void onClickRestDuration() {
        DurationPickerDialog fragment =
                DurationPickerDialog.newInstance(mViewModel.getRestDuration());
        fragment.show(getFragmentManager(), DurationPickerDialog.class.getSimpleName());
    }

    @OnClick(R.id.set_duration_layout)
    public void onClickSetDuration() {
        DurationPickerDialog fragment =
                DurationPickerDialog.newInstance(mViewModel.getSetDuration());
        fragment.show(getFragmentManager(), DurationPickerDialog.class.getSimpleName());
    }

    @OnClick(R.id.start_exercise_button)
    public void onClickStartExercise() {
        Timber.d("onClickStartExercise");
        ExerciseActivity activity = (ExerciseActivity) getActivity();
        Fragment fragment = RegularCoachFragment.newInstance(mViewModel.getExerciseId());
        activity.showFragment(fragment, CoachFragment.class.getSimpleName(), true);
    }

    public void setDuration(DurationDisplayable durationDisplayable) {
        switch (durationDisplayable.getType()) {
            case DurationDisplayable.TYPE_REST_DURATION:
                addDisposable(mViewModel.setRestDuration(durationDisplayable).subscribe());
                break;
            case DurationDisplayable.TYPE_SET_DURATION:
                addDisposable(mViewModel.setSetDuration(durationDisplayable).subscribe());
                break;
        }
    }

    public void setExerciseNameText(Exercise exercise) {
        String exerciseDisplay = mViewModel.getExerciseDisplay();
        SpannableString exerciseTypeString = new SpannableString(exerciseDisplay);
        int iStart = exercise.name.length();
        int iEnd = exerciseDisplay.length();
        exerciseTypeString.setSpan(new RelativeSizeSpan(0.8f), iStart, iEnd, 0);
        int color = ContextCompat.getColor(getActivity(), R.color.textGray);
        exerciseTypeString.setSpan(new ForegroundColorSpan(color), iStart, iEnd, 0);
        mExerciseNameTextView.setText(exerciseTypeString);
    }

    public void setInputValues(Exercise exercise) {
        if (exercise.favorite) {
            mFavoriteImageSwitcher.setImageResource(R.drawable.ic_star);
        } else {
            mFavoriteImageSwitcher.setImageResource(R.drawable.ic_star_border);
        }

        mNotesInput.setText(exercise.notes);

        mSetsNumberInput.setNumber(exercise.sets);

        switch (ExerciseSubType.fromValue(exercise.subType)) {
            case REPS:
                mSetDurationLayout.setVisibility(View.GONE);
                mRepsNumberInput.setNumber(exercise.reps);
                break;
            case TIMED_SETS:
                mRepsNumberInput.setVisibility(View.GONE);
                break;
        }
    }


}

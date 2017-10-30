package com.genenakagaki.splitstep.exercise.ui.list;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.genenakagaki.splitstep.R;
import com.genenakagaki.splitstep.exercise.data.entity.Exercise;
import com.genenakagaki.splitstep.exercise.data.entity.ExerciseType;
import com.genenakagaki.splitstep.exercise.ui.ExerciseActivity;
import com.genenakagaki.splitstep.exercise.ui.detail.ExerciseDetailFragment;
import com.genenakagaki.splitstep.exercise.ui.detail.ReactionExerciseDetailFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;
import timber.log.Timber;

/**
 * Created by Gene on 9/8/2017.
 */

public class ExerciseViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.delete_button) ImageButton mDeleteButton;
    @BindView(R.id.favorite_imageswitcher) ImageSwitcher mFavoriteImageSwitcher;
    @BindView(R.id.name_textview) TextView mNameTextButton;

    private ExerciseActivity mContext;
    private ExerciseTitleViewModel mViewModel;

    public ExerciseViewHolder(View view, Context context) {
        super(view);
        mContext = (ExerciseActivity) context;
        ButterKnife.bind(this, view);
        mFavoriteImageSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView switcherImageView = new ImageView(mContext);
                switcherImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                switcherImageView.setLayoutParams(new ImageSwitcher.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                return switcherImageView;
            }
        });
    }

    public void setExercise(Exercise exercise) {
        mViewModel = new ExerciseTitleViewModel(mContext, exercise);

        setExerciseNameText(exercise);

        ExerciseListFragment fragment = (ExerciseListFragment)
                mContext.findFragment(ExerciseListFragment.class.getSimpleName());

        fragment.addDisposable(mViewModel.getExerciseSubject()
                .subscribe(new Consumer<Exercise>() {
                    @Override
                    public void accept(Exercise exercise) throws Exception {
                        if (exercise.favorite) {
                            mFavoriteImageSwitcher.setImageResource(R.drawable.ic_star);
                        } else {
                            mFavoriteImageSwitcher.setImageResource(R.drawable.ic_star_border);
                        }
                    }
                }));
    }

    @OnClick(R.id.delete_button)
    public void onDeleteButtonClick() {
        showDeleteExerciseDialog();
    }

    @OnClick(R.id.favorite_imageswitcher)
    public void onFavoriteClick() {
        Timber.d("onFavoriteClick");
        mViewModel.toggleExerciseFavorite().subscribe();
    }

    @OnClick(R.id.content)
    public void onExerciseClick() {
        Timber.d("onExerciseClick");

        ExerciseListFragment fragment = (ExerciseListFragment) mContext.findFragment(ExerciseListFragment.class.getSimpleName());
        ExerciseListViewModel exerciseListViewModel = fragment.getViewModel();
        if (exerciseListViewModel.isEditMode()) {
            showDeleteExerciseDialog();
        } else {
            long exerciseId = mViewModel.getExercise().id;
            switch (ExerciseType.fromValue(mViewModel.getExercise().type)) {
                case REGULAR:
                    mContext.showFragment(ExerciseDetailFragment.newInstance(exerciseId),
                            ExerciseDetailFragment.class.getSimpleName(),
                            true);
                    break;
                default:// REACTION:
                    mContext.showFragment(ReactionExerciseDetailFragment.newInstance(exerciseId),
                            ExerciseDetailFragment.class.getSimpleName(),
                            true);
            }
        }
    }

    private void showDeleteExerciseDialog() {
        DeleteExerciseDialog dialog = DeleteExerciseDialog.newInstance(
                mViewModel.getExercise().id, mViewModel.getExerciseDisplay());
        dialog.show(mContext.getSupportFragmentManager(), DeleteExerciseDialog.class.getSimpleName());
    }

    private void setExerciseNameText(Exercise exercise) {
        String exerciseDisplay = mViewModel.getExerciseDisplay();
        SpannableString exerciseTypeString = new SpannableString(exerciseDisplay);
        int iStart = exercise.name.length();
        int iEnd = exerciseDisplay.length();
        int color = ContextCompat.getColor(mContext, R.color.textGray);
        exerciseTypeString.setSpan(new ForegroundColorSpan(color), iStart, iEnd, 0);
        mNameTextButton.setText(exerciseTypeString);
    }
}

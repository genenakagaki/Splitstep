package com.genenakagaki.splitstep.exercise.ui.list;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.genenakagaki.splitstep.R;
import com.genenakagaki.splitstep.exercise.data.entity.Exercise;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by gene on 4/16/17.
 */

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.delete_button)
        ImageButton mDeleteButton;
        @BindView(R.id.favorite_imageswitcher) ImageSwitcher mFavoriteImageSwitcher;
        @BindView(R.id.exercise_textview) TextView mExerciseTextButton;
        private ExerciseListItemViewModel mViewModel;

        public ViewHolder(View view) {
            super(view);
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
            mViewModel = new ExerciseListItemViewModel(mContext, exercise);

            mViewModel.getExerciseSubject()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.computation())
                    .subscribe(new Consumer<Exercise>() {
                        @Override
                        public void accept(Exercise exercise) throws Exception {
                            if (exercise.favorite) {
                                mFavoriteImageSwitcher.setImageResource(R.drawable.ic_star);
                            } else {
                                mFavoriteImageSwitcher.setImageResource(R.drawable.ic_star_border);
                            }
                        }
                    });
        }

        @OnClick(R.id.delete_button)
        public void onDeleteButtonClick() {
            DeleteExerciseDialog dialog = DeleteExerciseDialog.newInstance(
                    mViewModel.getExercise().id, mViewModel.getDeleteMessage());
            AppCompatActivity activity = (AppCompatActivity) mContext;
            dialog.show(activity.getSupportFragmentManager(), DeleteExerciseDialog.class.getSimpleName());
        }

        @OnClick(R.id.favorite_imageswitcher)
        public void onFavoriteClick() {
            Timber.d("onFavoriteClick");
            mViewModel.toggleExerciseFavorite();
        }

        @OnClick(R.id.exercise_textview)
        public void onExerciseClick() {
            Timber.d("onExerciseClick");
//            ExerciseActivity activity = (ExerciseActivity) mContext;
//                ExerciseSharedPref.setExerciseId(mContext, mExercise.id);
//
//                switch (ExerciseSharedPref.getExerciseType(mContext)) {
//                case REPS:
//                    activity.showFragment(new RepsExerciseDetailFragment(),
//                            RepsExerciseDetailFragment.class.getSimpleName(),
//                            true);
//                    break;
//                case TIMED_SETS:
//                    activity.showFragment(new TimedSetsExerciseDetailFragment(),
//                            TimedSetsExerciseDetailFragment.class.getSimpleName(),
//                            true);
//                    break;
//                case REACTION:
//                    activity.showFragment(new ReactionExerciseDetailFragment(),
//                            ReactionExerciseDetailFragment.class.getSimpleName(),
//                            true);
        }
    }

    private Context mContext;
    private ExerciseListViewModel mViewModel;
    private List<Exercise> mExercises;

    public ExerciseAdapter(Context context, ExerciseListViewModel viewModel) {
        mContext = context;
        mViewModel = viewModel;
        mExercises = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_exercise_list, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (mViewModel.isEditMode()) {
            holder.mDeleteButton.setVisibility(View.VISIBLE);
            holder.mFavoriteImageSwitcher.setVisibility(View.GONE);
        } else {
            holder.mDeleteButton.setVisibility(View.GONE);
            holder.mFavoriteImageSwitcher.setVisibility(View.VISIBLE);
        }

        Exercise exercise = mExercises.get(position);
        holder.setExercise(exercise);
        holder.mExerciseTextButton.setText(exercise.name);

//        if (exercise.favorite) {
//            holder.mFavoriteImageSwitcher.setImageResource(R.drawable.ic_star);
//        } else {
//            holder.mFavoriteImageSwitcher.setImageResource(R.drawable.ic_star_border);
//        }
//        Animation animationOut = AnimationUtils.loadAnimation(this, android.R.anim.slide_out_right);
//        Animation animationIn = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left);
//
//        myImageSwitcher.setOutAnimation(animationOut);
//        myImageSwitcher.setInAnimation(animationIn);
    }

    @Override
    public int getItemCount() {
        return mExercises.size();
    }

    public void clear() {
        int size = mExercises.size();
        mExercises.clear();
        notifyItemRangeRemoved(0, size);
    }

    public void addAll(List<Exercise> exercises) {
        int prevSize = mExercises.size();
        mExercises.addAll(exercises);
        notifyItemRangeInserted(prevSize, mExercises.size());
    }

    public List<Exercise> getExercises() {
        return mExercises;
    }

}

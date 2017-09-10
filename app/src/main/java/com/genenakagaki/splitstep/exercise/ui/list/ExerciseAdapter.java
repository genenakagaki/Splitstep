package com.genenakagaki.splitstep.exercise.ui.list;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.genenakagaki.splitstep.R;
import com.genenakagaki.splitstep.exercise.data.entity.Exercise;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gene on 4/16/17.
 */

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseViewHolder> {

    private Context mContext;
    private ExerciseListViewModel mViewModel;
    private List<Exercise> mExercises;

    public ExerciseAdapter(Context context, ExerciseListViewModel viewModel) {
        mContext = context;
        mViewModel = viewModel;
        mExercises = new ArrayList<>();
    }

    @Override
    public ExerciseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_exercise_list, parent, false);

        return new ExerciseViewHolder(view, mContext);
    }

    @Override
    public void onBindViewHolder(ExerciseViewHolder holder, int position) {
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

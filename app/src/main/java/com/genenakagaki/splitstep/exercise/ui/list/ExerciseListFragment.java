package com.genenakagaki.splitstep.exercise.ui.list;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.genenakagaki.splitstep.R;
import com.genenakagaki.splitstep.base.BaseFragment;
import com.genenakagaki.splitstep.exercise.data.entity.Exercise;
import com.genenakagaki.splitstep.exercise.data.entity.ExerciseType;
import com.genenakagaki.splitstep.exercise.ui.ExerciseActivity;

import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;
import timber.log.Timber;

/**
 * Created by gene on 8/2/17.
 */

public class ExerciseListFragment extends BaseFragment {

    private static final String EXERCISE_TYPE_KEY = "EXERCISE_TYPE_KEY";
    private static final String EDIT_MODE_KEY = "EDIT_MODE_KEY";

    public static ExerciseListFragment newInstance(ExerciseType exerciseType) {
        ExerciseListFragment fragment = new ExerciseListFragment();
        Bundle args = new Bundle();
        args.putParcelable(EXERCISE_TYPE_KEY, Parcels.wrap(exerciseType));
        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R.id.recyclerview) RecyclerView mRecyclerView;
    @BindView(R.id.empty_textview) TextView mEmptyTextView;
    @BindView(R.id.fab) FloatingActionButton mFab;

    private ExerciseListViewModel mViewModel;

    private ExerciseAdapter mExerciseAdapter;

    public ExerciseListFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Timber.d("onCreate");
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        if (getArguments() != null) {
            ExerciseType exerciseType = Parcels.unwrap(getArguments().getParcelable(EXERCISE_TYPE_KEY));
            mViewModel = new ExerciseListViewModel(getActivity(), exerciseType);
        } else {
            Timber.wtf("OHNNOOOO");
        }

        if (savedInstanceState != null) {
            mViewModel.setEditMode(savedInstanceState.getBoolean(EDIT_MODE_KEY));
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Timber.d("onCreateView");

        View view = inflater.inflate(R.layout.fragment_exercise_list, container, false);
        bindView(this, view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mExerciseAdapter = new ExerciseAdapter(getContext(), mViewModel);

        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mExerciseAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                layoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        return view;
    }

    @Override
    public void onResume() {
        Timber.d("onResume");
        super.onResume();

        addDisposable(mViewModel.getExercisesSubject()
                .subscribe(new Consumer<List<Exercise>>() {
                    @Override
                    public void accept(List<Exercise> exercises) throws Exception {
                        mExerciseAdapter.clear();
                        mExerciseAdapter.addAll(exercises);

                        if (mExerciseAdapter.getItemCount() > 0) {
                            mEmptyTextView.setVisibility(View.GONE);
                            mRecyclerView.setVisibility(View.VISIBLE);
                        } else {
                            mRecyclerView.setVisibility(View.GONE);
                            mEmptyTextView.setVisibility(View.VISIBLE);
                        }
                    }
                }));

        addDisposable(mViewModel.loadExerciseList().subscribe());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Timber.d("onCreateOptionsMenu");
        super.onCreateOptionsMenu(menu, inflater);

        ExerciseActivity activity = (ExerciseActivity) getActivity();
        activity.setTitle(mViewModel.getExerciseType());
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        inflater.inflate(R.menu.menu_exercise_list, menu);

        Drawable drawable = menu.findItem(R.id.action_edit).getIcon();
        if (drawable != null) {
            // If we don't mutate the drawable, then all drawable's with this id will have a color
            // filter applied to it.
            drawable.mutate();
            int color = ContextCompat.getColor(getActivity(), android.R.color.white);
            drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        }

        if (mViewModel.isEditMode()) {
            menu.findItem(R.id.action_edit).setVisible(false);
            menu.findItem(R.id.action_cancel).setVisible(true);
        } else {
            menu.findItem(R.id.action_edit).setVisible(true);
            menu.findItem(R.id.action_cancel).setVisible(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                Timber.d("action_edit");
                mViewModel.setEditMode(true);
                getActivity().invalidateOptionsMenu();
                mExerciseAdapter.notifyDataSetChanged();
                break;
            case R.id.action_cancel:
                Timber.d("action_cancel");
                mViewModel.setEditMode(false);
                getActivity().invalidateOptionsMenu();
                mExerciseAdapter.notifyDataSetChanged();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Timber.d("onSaveInstanceState");

        outState.putBoolean(EDIT_MODE_KEY, mViewModel.isEditMode());
    }

    @OnClick(R.id.fab)
    public void onFabClick() {
        AddExerciseDialog fragment = AddExerciseDialog.newInstance(mViewModel.getExerciseType());
        fragment.show(getFragmentManager(), AddExerciseDialog.class.getSimpleName());
    }

    public ExerciseListViewModel getViewModel() {
        return mViewModel;
    }
}

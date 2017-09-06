package com.genenakagaki.splitstep.exercise.ui.list;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.genenakagaki.splitstep.R;
import com.genenakagaki.splitstep.exercise.data.entity.Exercise;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by gene on 8/2/17.
 */

public class ExerciseListFragment extends Fragment implements View.OnClickListener {

    @BindView(R.id.recyclerview) RecyclerView mRecyclerView;
    @BindView(R.id.empty_textview) TextView mEmptyTextView;
    @BindView(R.id.fab) FloatingActionButton mFab;

    private Unbinder mUnbinder;
    private CompositeDisposable mDisposable;
    private ExerciseListViewModel mViewModel;

    private ExerciseAdapter mExerciseAdapter;

    public ExerciseListFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewModel = new ExerciseListViewModel(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Timber.d("onCreateView");

        View view = inflater.inflate(R.layout.fragment_exercise_list, container, false);
        mUnbinder = ButterKnife.bind(this, view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mExerciseAdapter = new ExerciseAdapter(getContext(), mViewModel.getExerciseType());

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
        mDisposable = new CompositeDisposable();

        mDisposable.add(mViewModel.getExerciseList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.computation())
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
    }

    @Override
    public void onPause() {
        Timber.d("onPause");
        super.onPause();
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
//                AddExerciseDialog fragment = new AddExerciseDialog();
//                fragment.show(getFragmentManager(), AddExerciseDialog.class.getSimpleName());
                break;
        }
    }
}

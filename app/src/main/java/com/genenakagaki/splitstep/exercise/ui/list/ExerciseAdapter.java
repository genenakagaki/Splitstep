package com.genenakagaki.splitstep.exercise.ui.list;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.genenakagaki.splitstep.R;
import com.genenakagaki.splitstep.exercise.data.ExerciseSharedPref;
import com.genenakagaki.splitstep.exercise.data.entity.Exercise;
import com.genenakagaki.splitstep.exercise.data.entity.ExerciseType;
import com.genenakagaki.splitstep.exercise.ui.ExerciseActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * Created by gene on 4/16/17.
 */

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.exercise_radiobutton)
        RadioButton mExerciseRadioButton;
        @BindView(R.id.exercise_textview)
        TextView mExerciseTextButton;
        Exercise mExercise;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            ExerciseActivity activity = (ExerciseActivity) mContext;
            ExerciseSharedPref.setExerciseId(mContext, mExercise.id);

            switch (ExerciseSharedPref.getExerciseType(mContext)) {
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
    }

    private Map<Integer, Boolean> mIsSelectedByExerciseId = new HashMap<>();
    private Context mContext;
    private List<Exercise> mExercises;

    private int mMode;

    public ExerciseAdapter(Context context, ExerciseType exerciseType) {
//        mMode = ExerciseChooserFragment.MODE_CHOOSE;
        mContext = context;
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
//        if (mMode == ExerciseChooserFragment.MODE_CHOOSE) {
//            viewHolder.mExerciseRadioButton.setVisibility(View.GONE);
//        } else {
//            viewHolder.mExerciseRadioButton.setVisibility(View.VISIBLE);
//        }

        Exercise exercise = mExercises.get(position);
        holder.mExercise = exercise;
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

    private void showEditDialog(Context context, ViewHolder viewHolder) {
        Timber.d("showEditDialog");

//        try {
//            switch (mExerciseType) {
////                case Exercise.TYPE_REPS:
//                    TimedSetsExercise timedSetsExercise = RegularExerciseDb.getExerciseByName(
//                            context, viewHolder.mExerciseRadioButton.getText().toString());
//                    EditExerciseDialog.newInstance(mExerciseType, timedSetsExercise.id).show(
//                            ((ExerciseChooserActivity) context).getSupportFragmentManager(), null);
//                    break;
//                case Exercise.TYPE_TIMED_SETS:
//
//                    break;
//                case Exercise.TYPE_REACTION:
//                    ReactionExercise reactionExercise = ReactionExerciseDb.getExerciseByName(
//                            context, viewHolder.mExerciseRadioButton.getText().toString());
//                    EditExerciseDialog.newInstance(mExerciseType, reactionExercise.id).show(
//                            ((ExerciseChooserActivity) context).getSupportFragmentManager(), null);
//                    break;
//            }
//        } catch (ExerciseNotFoundException e) {
//            Timber.d(e.getMessage());
//            e.printStackTrace();
//        }
    }

    public void setMode(int mode) {
        mMode = mode;
        mIsSelectedByExerciseId.clear();
    }

    public List<Integer> getSelectedIds() {
        List<Integer> selectedIds = new ArrayList<>();

        for (Map.Entry<Integer, Boolean> entry: mIsSelectedByExerciseId.entrySet()) {
            if (entry.getValue()) {
                selectedIds.add(entry.getKey());
            }
        }

        return selectedIds;
    }

    public void onClick(Context context, View view) {

        ViewHolder viewHolder = (ViewHolder) view.getTag();

//        if (mMode == ExerciseChooserFragment.MODE_CHOOSE) {
//            Timber.d("onClick Mode: Choose");
//            switch (mExerciseType) {
//                case REGULAR:
//                    RegularExerciseDb.setCurrentExercise(context, viewHolder.mExerciseId);
//                    break;
//                case REACTION:
//                    ReactionExerciseDb.setCurrentExercise(context, viewHolder.mExerciseId);
//                    break;
//            }
//            Intent intent = new Intent(view.getContext(), ExerciseSettingActivity.class);
//            intent.putExtra(ExerciseSettingActivity.EXTRA_EXERCISE_TYPE, mExerciseType);
//            intent.putExtra(ExerciseSettingActivity.EXTRA_EXERCISE_ID, viewHolder.mExerciseId);
//            view.getContext().startActivity(intent);
//        } else {
//            Timber.d("onClick Mode: Edit");
//            Boolean selected = mIsSelectedByExerciseId.get(viewHolder.mExerciseId);
//            if (selected == null) {
//                selected = true;
//            } else {
//                selected = !selected;
//            }
//
//            mIsSelectedByExerciseId.put(viewHolder.mExerciseId, selected);
//            viewHolder.mExerciseRadioButton.setChecked(selected);
//        }

    }


//        implements View.OnTouchListener, View.OnDragListener {

//    private static final long DRAG_START_TIME = 100;


//    private View mDragView;
//    private float mDragOffset;
//    private boolean mDragging = false;
//    private long mTouchStartTime;

    //    @Override
//    public boolean onTouch(View v, MotionEvent event) {
//        Timber.d("onTouch");
//
//        final int action = MotionEventCompat.getActionMasked(event);
//
//        switch (action) {
//            case MotionEvent.ACTION_DOWN:
//                Timber.d("ACTION_DOWN");
//                mTouchStartTime = Calendar.getInstance().getTimeInMillis();
//
//                break;
//            case MotionEvent.ACTION_MOVE:
//                Timber.d("ACTION_MOVE");
//                long touchDuration = Calendar.getInstance().getTimeInMillis() - mTouchStartTime;
//                if(touchDuration > DRAG_START_TIME && !mDragging) {
//                    mDragView = v;
//                    ClipData data = ClipData.newPlainText("", "");
//                    View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(new View(v.getContext()));
//
//                    if (Build.VERSION.SDK_INT >= 24) {
//                        v.startDragAndDrop(data, shadowBuilder, v.findViewById(R.id.exercise_container), 0);
//                    } else {
//                        v.startDrag(data, shadowBuilder, v.findViewById(R.id.exercise_container), 0);
//                    }
//                }
//                break;
//            case MotionEvent.ACTION_UP:
//                touchDuration = Calendar.getInstance().getTimeInMillis() - mTouchStartTime;
//                if (touchDuration < DRAG_START_TIME) {
//                    onClick(v.getContext(), v);
//                }
//                break;
//        }
//        return true;
//    }

    //    @Override
//    public boolean onDrag(View v, DragEvent event) {
//        ViewHolder viewHolder = (ViewHolder) mDragView.getTag();
//
//        Context context = v.getContext();
//
//        switch (event.getAction()) {
//            case DragEvent.ACTION_DRAG_STARTED:
//                Timber.d("ACTION_DRAG_STARTED");
//
//                mDragging = true;
//                mDragOffset = event.getX();
//                break;
//            case DragEvent.ACTION_DRAG_LOCATION:
//                Timber.d("ACTION_DRAG_LOCATION");
//
//                float x = event.getX();
//
//                int margin = (int) (x - mDragOffset);
//                int marginLeft = margin;
//                int marginRight = 0 - margin;
//
//                Drawable background = viewHolder.mExerciseContainer.getBackground();
//                background.setAlpha(200);
//
//                if (marginLeft > 0) { // Dragging to right
//                    ButterKnife.findById(mDragView, R.id.content).setBackgroundColor(
//                            ContextCompat.getColor(context, R.color.colorPrimary));
//                    viewHolder.mEditLayout.setVisibility(View.VISIBLE);
//                    viewHolder.deleteLayout.setVisibility(View.INVISIBLE);
//                } else {
//                    ButterKnife.findById(mDragView, R.id.content).setBackgroundColor(
//                            ContextCompat.getColor(context, R.color.error));
//                    viewHolder.mEditLayout.setVisibility(View.INVISIBLE);
//                    viewHolder.deleteLayout.setVisibility(View.VISIBLE);
//                }
//
//                RelativeLayout.LayoutParams layoutParams =
//                        (RelativeLayout.LayoutParams) viewHolder.mExerciseContainer.getLayoutParams();
//                layoutParams.setMargins(marginLeft, 0, marginRight, 0);
//                viewHolder.mExerciseContainer.setLayoutParams(layoutParams);
//                break;
//            case DragEvent.ACTION_DROP:
//                Timber.d("ACTION_DROP");
//
//                background = viewHolder.mExerciseContainer.getBackground();
//                background.setAlpha(255);
//
//                layoutParams = (RelativeLayout.LayoutParams) viewHolder.mExerciseContainer.getLayoutParams();
//
//                DisplayMetrics displayMetrics = new DisplayMetrics();
//                ((Activity) context).getWindowManager()
//                        .getDefaultDisplay()
//                        .getMetrics(displayMetrics);
//                int width = displayMetrics.widthPixels;
//
//                if (layoutParams.leftMargin > width / 3) {
//                    showEditDialog(context, viewHolder);
//
//                } else if (layoutParams.rightMargin > width / 3) {
//                    showDeleteDialog(context, viewHolder);
//
//                }
//                layoutParams.setMargins(0, 0, 0, 0);
//
//                viewHolder.mExerciseContainer.setLayoutParams(layoutParams);
//
//                mDragging = false;
//                break;
//            default:
//                break;
//        }
//        return true;
//    }
}

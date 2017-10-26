package com.genenakagaki.splitstep.exercise.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.genenakagaki.splitstep.R;
import com.genenakagaki.splitstep.exercise.data.entity.ExerciseType;
import com.genenakagaki.splitstep.exercise.ui.type.ExerciseTypeFragment;

import butterknife.ButterKnife;
import timber.log.Timber;

public class ExerciseActivity extends AppCompatActivity {

    public interface OnBackPressedListener {
        void onBackPressed();
    }

    private Toolbar mToolbar;
    private String mCurrentFragmentTag;
    private OnBackPressedListener mOnBackPressedListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.d("onCreate");

        setContentView(R.layout.activity_exercise);
        mToolbar = findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);

        if (savedInstanceState == null) {
            showFragment(new ExerciseTypeFragment(), ExerciseTypeFragment.class.getSimpleName(), false);
        }

        // TODO: remove
        ButterKnife.setDebug(true);
    }

    @Override
    public void setTitle(int titleId) {
        getSupportActionBar().setTitle(titleId);
    }

    @Override
    public void setTitle(CharSequence title) {
        getSupportActionBar().setTitle(title);
    }

    public void setTitle(ExerciseType exerciseType) {
        switch (exerciseType) {
            case REGULAR:
                setTitle(R.string.exercise);
                break;
            default: // REACTION:
                setTitle(R.string.reaction_exercise);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_exercise, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (mOnBackPressedListener != null) {
            mOnBackPressedListener.onBackPressed();
        }

        // get current fragment tag
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
//            fragmentManager.popBackStack();
//            mCurrentFragmentTag = fragmentManager
//                    .getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1)
//                    .getName();

//            if (ExerciseListFragment.class.getSimpleName().equals(fragmentTag)) {
//                Timber.d("onBackPressed ExerciseListFragment");
//                mFab.setVisibility(View.VISIBLE);
//                mStartExerciseLayout.setVisibility(View.GONE);
//            } else if (RepsCoachFragment.class.getSimpleName().equals(fragmentTag)) {
//                Timber.d("onBackPressed RepsCoachFragment");
//                mFab.setVisibility(View.GONE);
//                mStartExerciseLayout.setVisibility(View.VISIBLE);
//            }
        } else {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
    }

    public void setOnBackPressedListener(OnBackPressedListener onBackPressedListener) {
        this.mOnBackPressedListener = onBackPressedListener;
    }

    public void showFragment(Fragment fragment, String tag, boolean addToBackStack) {
        Timber.d("showFragment with tag " + tag);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment, tag);

        if (addToBackStack) {
            transaction.addToBackStack(tag);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        transaction.commit();

        mCurrentFragmentTag = tag;
    }

    public Fragment findFragment(String fragmentTag) {
        return getSupportFragmentManager().findFragmentByTag(fragmentTag);
    }

    public Fragment getCurrentFragment() {
        return getSupportFragmentManager().findFragmentByTag(mCurrentFragmentTag);
    }

}

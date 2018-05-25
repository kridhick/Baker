package com.example.udacity.karthikeyan.baker.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import com.example.udacity.karthikeyan.baker.Model.Step;
import com.example.udacity.karthikeyan.baker.R;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.udacity.karthikeyan.baker.UI.RecipeDetailStepsFragment.RECIPE_NAME;
import static com.example.udacity.karthikeyan.baker.UI.RecipeDetailStepsFragment.SELECTED_STEP_POSITION;
import static com.example.udacity.karthikeyan.baker.UI.RecipeDetailStepsFragment.STEP_LIST;
import static com.example.udacity.karthikeyan.baker.UI.RecipeDetailStepsFragment.newInstance;

/**
 * An activity representing a single RecipeDetail detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link RecipeDetailListActivity}.
 */
public class RecipeDetailStepsActivity extends AppCompatActivity implements RecipeDetailStepsFragment.ListItemClickListener{

    private static final int DEFAULT_SELECTED_ITEM_INDEX = -1;
    private static final String SELECTED_RECIPE_LIST = "DetailList";

    private ArrayList<Step> mStepList = new ArrayList<>();
    private int mSelectedPosition;
    private String mRecipeName;

    @BindView(R.id.detail_toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipedetail_detail);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
           mStepList = getIntent().getParcelableArrayListExtra(STEP_LIST);
           mSelectedPosition = getIntent().getIntExtra(SELECTED_STEP_POSITION, DEFAULT_SELECTED_ITEM_INDEX);
           mRecipeName = getIntent().getStringExtra(RECIPE_NAME);
        }
        else {
            mStepList = savedInstanceState.getParcelableArrayList(STEP_LIST);
            mSelectedPosition = savedInstanceState.getInt(SELECTED_STEP_POSITION, DEFAULT_SELECTED_ITEM_INDEX);
            mRecipeName = savedInstanceState.getString(RECIPE_NAME);
        }

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setTitle(mRecipeName);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fragmentManager = getSupportFragmentManager();
                if (fragmentManager.getBackStackEntryCount() >1) {
                    getSupportFragmentManager().popBackStack(SELECTED_RECIPE_LIST, 0);
                }
                else if (fragmentManager.getBackStackEntryCount() > 0)
                {
                    finish();
                }
            }
        });


        RecipeDetailStepsFragment fragment = newInstance((java.util.List<Step>) mStepList, mSelectedPosition, mRecipeName);
        getSupportFragmentManager().beginTransaction()
                .addToBackStack(SELECTED_RECIPE_LIST)
                .add(R.id.recipedetail_detail_container, fragment)
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            navigateUpTo(new Intent(this, RecipeDetailListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(STEP_LIST, mStepList);
        outState.putInt(SELECTED_STEP_POSITION, mSelectedPosition);
        outState.putString(RECIPE_NAME, mRecipeName);
    }

    @Override
    public void onListItemClick(List<Step> stepsOut, int clickedItemIndex, String recipeName) {
        Objects.requireNonNull(getSupportActionBar()).setTitle(recipeName);

            Intent stepDetailIntent = new Intent(this, RecipeDetailStepsActivity.class);

            stepDetailIntent.putParcelableArrayListExtra(STEP_LIST, (ArrayList<Step>)stepsOut);
            stepDetailIntent.putExtra(SELECTED_STEP_POSITION, clickedItemIndex);
            stepDetailIntent.putExtra(RECIPE_NAME, recipeName);

            this.startActivity(stepDetailIntent);

    }
}

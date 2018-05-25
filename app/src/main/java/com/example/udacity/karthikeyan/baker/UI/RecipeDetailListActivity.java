package com.example.udacity.karthikeyan.baker.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.udacity.karthikeyan.baker.Adapter.RecipeDetailAdapter;
import com.example.udacity.karthikeyan.baker.Model.Recipe;
import com.example.udacity.karthikeyan.baker.Model.Step;
import com.example.udacity.karthikeyan.baker.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.udacity.karthikeyan.baker.UI.RecipeDetailStepsFragment.RECIPE_NAME;
import static com.example.udacity.karthikeyan.baker.UI.RecipeDetailStepsFragment.SELECTED_STEP_POSITION;
import static com.example.udacity.karthikeyan.baker.UI.RecipeDetailStepsFragment.STEP_LIST;

/**
 * An activity representing a list of Details. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link RecipeDetailStepsActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class RecipeDetailListActivity extends AppCompatActivity implements RecipeDetailAdapter.ListItemClickListener, RecipeDetailStepsFragment.ListItemClickListener{

    //Constants
    public static final String SELECTED_RECIPE_LIST ="RecipeList";
    public static final String STACK_RECIPE_STEP_DETAIL="STACK_RECIPE_STEP_DETAIL";
    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private ArrayList<Recipe> mRecipeList;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipedetail_list);

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);


        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }

        if (findViewById(R.id.recipedetail_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        RecyclerView recyclerView = findViewById(R.id.recipedetail_list);

        if (savedInstanceState == null) {

            Bundle bundle = getIntent().getExtras();
            mRecipeList = new ArrayList<>();
            assert bundle != null;
            mRecipeList = bundle.getParcelableArrayList(SELECTED_RECIPE_LIST);

        }
        else
        {
            mRecipeList = savedInstanceState.getParcelableArrayList(SELECTED_RECIPE_LIST);
        }


       if (mRecipeList.size() >= 1) {


           assert actionBar != null;
           actionBar.setTitle(mRecipeList.get(0).getName());

           assert recyclerView != null;
           recyclerView.setAdapter(new RecipeDetailAdapter(this, mRecipeList, this));
       }
       else
       {
           Toast.makeText(this, "An error had occurred.", Toast.LENGTH_LONG).show();
       }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onListItemClick(List<Step> stepsOut, int clickedItemIndex, String recipeName) {

        getSupportActionBar().setTitle(recipeName);

        if (mTwoPane) {

            RecipeDetailStepsFragment fragment = RecipeDetailStepsFragment.newInstance(stepsOut, clickedItemIndex, recipeName);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().addToBackStack(STACK_RECIPE_STEP_DETAIL).replace(R.id.recipedetail_detail_container, fragment).commit();

        }
        else{
            Intent stepDetailIntent = new Intent(this, RecipeDetailStepsActivity.class);

           stepDetailIntent.putParcelableArrayListExtra(STEP_LIST, (ArrayList<Step>)stepsOut);
           stepDetailIntent.putExtra(SELECTED_STEP_POSITION, clickedItemIndex);
           stepDetailIntent.putExtra(RECIPE_NAME, recipeName);

            this.startActivity(stepDetailIntent);
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(SELECTED_RECIPE_LIST, mRecipeList);
    }
}

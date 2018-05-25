package com.example.udacity.karthikeyan.baker.UI;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.udacity.karthikeyan.baker.Adapter.RecipeMainAdapter;
import com.example.udacity.karthikeyan.baker.Model.Recipe;
import com.example.udacity.karthikeyan.baker.Network.RESTApiClient;
import com.example.udacity.karthikeyan.baker.Network.RESTApiInterface;
import com.example.udacity.karthikeyan.baker.R;
import com.example.udacity.karthikeyan.baker.TestingUtils.BakingIdlingResource;
import com.example.udacity.karthikeyan.baker.Utils.Utilities;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeMainActivity extends AppCompatActivity {

    private RecipeMainAdapter mAdapter;
    private List<Recipe> mRecipeList;


    @BindView(R.id.pb_recipe_main)
    ProgressBar progressBar;

    @BindView(R.id.rv_recipe_main)
    RecyclerView recyclerView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private BakingIdlingResource mIdlingResource;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_main);

        initialize();


        hideLoadingInfo();

        getIdlingResource();


    }

    /**
     * Only called from test, creates and returns a new {@link com.example.udacity.karthikeyan.baker.TestingUtils.BakingIdlingResource}.
     */
    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new BakingIdlingResource();
        }
        return mIdlingResource;
    }


    private void initialize()
    {

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        showLoadingInfo();

    }

    private void showLoadingInfo()
    {

        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);

        if (!Utilities.isNetworkAvailable(this)){
            Toast.makeText(this, R.string.NO_NETWORK, Toast.LENGTH_LONG).show();
        }
        else
        {
            if(Utilities.isTablet(this))
            {
                recyclerView.setLayoutManager(new GridLayoutManager(this, Utilities.calcNoOfColumns(this)));
            }
            else
            {
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
            }

            mAdapter = new RecipeMainAdapter(this);
            recyclerView.setAdapter(mAdapter);
            mRecipeList = new ArrayList<>();
            fetchDataFromNetwork();


        }
    }

    private void fetchDataFromNetwork()
    {
        if(mIdlingResource != null) mIdlingResource.setIdleState(false);

        RESTApiInterface restApiInterface =  RESTApiClient.getRetrofit().create(RESTApiInterface.class);
        final Call<List<Recipe>> recipeCall = restApiInterface.getAllRecipes();
        recipeCall.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(@NonNull Call<List<Recipe>> call, Response<List<Recipe>> response) {
                if(response.isSuccessful()) {

                    mRecipeList = response.body();

                    // Assign to local String
                    Utilities.NW_DATA = new GsonBuilder().create().toJson(mRecipeList, Utilities.RECIPEJSONTYPE);

                    recyclerView.setAdapter(mAdapter);

                    mAdapter.swapData(mRecipeList);

                    if(mRecipeList.size() == 0)
                    {
                        Toast.makeText(getApplicationContext(), R.string.NO_DATA_FOUND, Toast.LENGTH_LONG).show();
                    }

                    if(mIdlingResource != null) mIdlingResource.setIdleState(true);


                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Recipe>> call, Throwable t) {
                t.printStackTrace();

                Toast.makeText(getApplicationContext(), R.string.NO_NETWORK, Toast.LENGTH_LONG).show();

            }
        });
    }

    private void hideLoadingInfo()
    {
        progressBar.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.VISIBLE);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_recipe_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

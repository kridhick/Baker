package com.example.udacity.karthikeyan.baker.Adapter;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.udacity.karthikeyan.baker.Model.Ingredient;
import com.example.udacity.karthikeyan.baker.Model.Recipe;
import com.example.udacity.karthikeyan.baker.Model.Step;
import com.example.udacity.karthikeyan.baker.R;
import com.example.udacity.karthikeyan.baker.Widget.UpdateBakingService;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeDetailAdapter extends RecyclerView.Adapter {

    //Constants
    private static final String LOG_TAG = RecipeDetailAdapter.class.getSimpleName();
    private static final int INGREDIENT = 0;
    private static final int STEP = 1;

    //Member Variables
    private List<Ingredient> mIngredientList = new ArrayList<>();
    private List<Step> mStepList = new ArrayList<>();
    private List<Recipe> mRecipeList = new ArrayList<>();
    private final Context mContext;
    private final ListItemClickListener lOnClickListener;
    private final String mRecipeName;

    public interface ListItemClickListener {
        void onListItemClick(List<Step> stepsOut,int clickedItemIndex,  String recipeName);
    }


    public RecipeDetailAdapter(Context context, List<Recipe> recipeList, ListItemClickListener listener){
        this.mContext = context;
        this.mRecipeList = recipeList;
        this.mIngredientList = recipeList.get(0).getIngredients();
        this.mStepList = recipeList.get(0).getSteps();
        this.lOnClickListener = listener;
        this.mRecipeName = recipeList.get(0).getName();
        notifyDataSetChanged();
    }




    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view;
        switch (viewType) {
            case INGREDIENT:
                view = LayoutInflater.from(mContext).inflate(R.layout.recipedetail_ingredient_content, parent, false);
                view.setFocusable(true);
                return new IngredientViewHolder(view);
            case STEP:
                view = LayoutInflater.from(mContext).inflate(R.layout.recipedetail_steps_content, parent, false);
                view.setFocusable(true);
                return new StepViewHolder(view);

        }
        return null;

    }

    @Override
    public void onBindViewHolder(final @NonNull RecyclerView.ViewHolder holder, final int position) {
        switch(getItemViewType(position)){
            case INGREDIENT:
                ArrayList<String> widgetIngredientList = new ArrayList<>();
                String consolidatedIngredientText ="";
                for (int i=1;i<mIngredientList.size()-1; i++){
                    consolidatedIngredientText = consolidatedIngredientText +
                            i +")"+ mIngredientList.get(i).getIngredient().toUpperCase() + "=>" +
                            mIngredientList.get(i).getQuantity().toString() + " " +
                            mIngredientList.get(i).getMeasure() +"\n\n";

                    widgetIngredientList.add(
                            mIngredientList.get(i).getIngredient().toUpperCase() + "\n" +
                                    "Quantity: "+ mIngredientList.get(i).getQuantity().toString() + "\n" +
                                    "Measure:  "+ mIngredientList.get(i).getMeasure() +"\n\n");
                }

                ((IngredientViewHolder) holder).ingredientTextView.setText(consolidatedIngredientText);

                //Call to update the widget --TODO:Check the performance.
                UpdateBakingService.startBakingService(mContext, widgetIngredientList);

                break;
            case  STEP:
                //Negating one position value to offset the ingredient text view type.
                final Step step = mStepList.get(position-1);
                ((StepViewHolder) holder).stepShortDescTextView.setText(step.getId()+ "." + step.getShortDescription());


                ((StepViewHolder) holder).stepView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        lOnClickListener.onListItemClick(mStepList, position-1, mRecipeName);
                    }
                });


                break;

        }

    }

    @Override
    public int getItemCount() {
        return mStepList.size()+1;
    }

    @Override
    public int getItemViewType(int position) {
        switch(position){
            case 0: return INGREDIENT;
            default : return STEP;
        }
    }

    class IngredientViewHolder extends RecyclerView.ViewHolder {

        View ingredientView;

        @BindView(R.id.tv_ingredient)
        TextView ingredientTextView;

        IngredientViewHolder(View itemView){
            super(itemView);
            this.ingredientView = itemView;
            ButterKnife.bind(this, itemView);

        }
    }

    class StepViewHolder extends RecyclerView.ViewHolder {

        View stepView;

        @BindView(R.id.tv_step_short_description)
        TextView stepShortDescTextView;

        StepViewHolder(View itemView) {
            super(itemView);
            this.stepView = itemView;
            ButterKnife.bind(this, itemView);
        }
    }


}

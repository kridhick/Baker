package com.example.udacity.karthikeyan.baker.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.udacity.karthikeyan.baker.Model.Recipe;
import com.example.udacity.karthikeyan.baker.R;
import com.example.udacity.karthikeyan.baker.UI.RecipeDetailListActivity;
import com.example.udacity.karthikeyan.baker.Utils.Utilities;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.net.Uri.parse;

public class RecipeMainAdapter extends RecyclerView.Adapter<RecipeMainAdapter.RecipeMainViewHolder> {

    //Constants
    private static final String LOG_TAG = RecipeMainAdapter.class.getSimpleName();

    // Member Variable
    private List<Recipe> mValues;
    private final Context mContext;

    public RecipeMainAdapter(Context context)
    {
        this.mContext = context;
        this.mValues = new GsonBuilder().create().fromJson(Utilities.NW_DATA, Utilities.RECIPEJSONTYPE);
    }

    @NonNull
    @Override
    public RecipeMainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.content_list_recipe, parent, false);
        view.setFocusable(true);
        return new RecipeMainViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeMainViewHolder holder, int position) {
        final Recipe recipe = mValues.get(position);

        holder.RecipeNameTextView.setText(recipe.getName());
        String imageUrl = recipe.getImage();

        // ImageUrl is not empty
        if(!imageUrl.isEmpty())
        {
            Uri builtUri = parse(imageUrl).buildUpon().build();
            Picasso.get().load(builtUri).error(R.drawable.ic_launcher_background).noPlaceholder().into(holder.RecipeImageView);
        }

        //Pass the selected Recipe on Click
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Bundle bundle = new Bundle();
               ArrayList<Recipe> selectedRecipe = new ArrayList<>();
               selectedRecipe.add(recipe);
               bundle.putParcelableArrayList(RecipeDetailListActivity.SELECTED_RECIPE_LIST, selectedRecipe);
               final Intent detailIndent = new Intent(mContext, RecipeDetailListActivity.class);
               detailIndent.putExtras(bundle);
               mContext.startActivity(detailIndent);
            }
        });

    }

    @Override
    public int getItemCount() {
        if(null == mValues) {
            return 0;
        }
        return mValues.size();
    }

    public void swapData(final List<Recipe> newValues){
        if (null == mValues) {
            this.mValues = newValues;
            notifyDataSetChanged();
        }
    }

    class RecipeMainViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_recipe_name)
        TextView RecipeNameTextView;

        @BindView(R.id.iv_recipe)
        ImageView RecipeImageView;

        View view;


        RecipeMainViewHolder(View itemView){
            super(itemView);
            this.view = itemView;
            ButterKnife.bind(this, itemView);

        }
    }
}

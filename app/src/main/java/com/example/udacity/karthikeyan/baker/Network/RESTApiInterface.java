package com.example.udacity.karthikeyan.baker.Network;

import com.example.udacity.karthikeyan.baker.Model.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;


public interface RESTApiInterface {

    @GET("topher/2017/May/59121517_baking/baking.json")
    Call<List<Recipe>> getAllRecipes();
}

package com.example.udacity.karthikeyan.baker.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;

import com.example.udacity.karthikeyan.baker.Model.Ingredient;
import com.example.udacity.karthikeyan.baker.Model.Recipe;
import com.example.udacity.karthikeyan.baker.Model.Step;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class Utilities {

    public static String NW_DATA;

    public static Type RECIPEJSONTYPE = new TypeToken<List<Recipe>>(){}.getType();

    public static Type STEPJSONTYPE = new TypeToken<List<Step>>(){}.getType();

    public static Type INGREDIENTJSONTYPE = new TypeToken<List<Ingredient>>(){}.getType();

    // Helper to calculate the number of columns for grid layout
    public static int calcNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        return (int) (dpWidth / 180);
    }

    //Helper to check the current device in Tablet or Phone
    public static boolean isTablet(Activity activity){
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

        float yInches= metrics.heightPixels/metrics.ydpi;
        float xInches= metrics.widthPixels/metrics.xdpi;
        double diagonalInches = Math.sqrt(xInches*xInches + yInches*yInches);
        if (diagonalInches>= 7){
            return true;
        }else{
            return false;
        }
    }


    //Helper to check the network connection
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager;
        connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    public static boolean isInLandscapeMode( Context context ) {
        return (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE);
    }

}

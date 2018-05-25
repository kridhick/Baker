package com.example.udacity.karthikeyan.baker.Widget;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import java.util.ArrayList;

public class UpdateBakingService extends IntentService {

   public static String ACTIVITY_INGREDIENTS_LIST = "IngredientsListForWidget";

    public UpdateBakingService() {
        super("UpdateBakingService");
    }

    public static void startBakingService(Context context, ArrayList<String> fromActivityIngredientsList) {
        Intent intent = new Intent(context, UpdateBakingService.class);
        intent.putExtra(ACTIVITY_INGREDIENTS_LIST,fromActivityIngredientsList);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent callingIntent) {

        if (callingIntent != null) {
            ArrayList<String> fromActivityIngredientsList = callingIntent.getExtras().getStringArrayList(ACTIVITY_INGREDIENTS_LIST);
            if(!fromActivityIngredientsList.isEmpty()) {
                Intent intent = new Intent("android.appwidget.action.APPWIDGET_UPDATE");
                intent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
                intent.putExtra(ACTIVITY_INGREDIENTS_LIST, fromActivityIngredientsList);
                sendBroadcast(intent);
            }
        }

    }
}

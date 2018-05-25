package com.example.udacity.karthikeyan.baker.Widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.udacity.karthikeyan.baker.R;

import java.util.List;

public class GridWidgetService extends RemoteViewsService {

    List<String> remoteViewIngredientsList;
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new GridRemoteViewFactory(this.getApplicationContext(), intent);
    }

    class GridRemoteViewFactory implements RemoteViewsService.RemoteViewsFactory {

        Context mContext = null;

        public GridRemoteViewFactory(Context context, Intent intent)
        {
            mContext = context;
        }

        @Override
        public void onCreate() {
         //Not Implemented
        }

        @Override
        public void onDataSetChanged() {

            remoteViewIngredientsList = BakerAppWidget.ingredientsList;

        }

        @Override
        public void onDestroy() {
         //Not Implemented
        }

        @Override
        public int getCount() {
            return remoteViewIngredientsList.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.baker_app_widget_grid_item);
            remoteViews.setTextViewText(R.id.widget_grid_view_item, remoteViewIngredientsList.get(position));
            Intent fillIntent = new Intent();
            remoteViews.setOnClickFillInIntent(R.id.widget_grid_view_item, fillIntent);
            return remoteViews;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}

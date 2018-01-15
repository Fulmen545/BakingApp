package com.riso.android.bakingapp.util;

import android.app.LauncherActivity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.riso.android.bakingapp.R;
import com.riso.android.bakingapp.data.IngredientItems;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by richard.janitor on 13-Jan-18.
 */

public class ListProvider implements RemoteViewsService.RemoteViewsFactory {
    private List<IngredientItems> listItemList = new ArrayList();
    private Context context = null;
    private int appWidgetId;
    private Parcelable[] ingArray;

    public ListProvider(Context context, Intent intent, Parcelable[] ingredArray) {
        this.context = context;
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
        this.ingArray=ingredArray;
        populateListItem();
    }

    private void populateListItem() {
        IngredientItems item;
        if (ingArray != null) {
            for (int i = 0; i < ingArray.length; i++) {
                item = (IngredientItems) ingArray[0];
                listItemList.add(item);
            }
        }
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return listItemList.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        final RemoteViews remoteView = new RemoteViews(
                context.getPackageName(), R.layout.ingredient_card);
        IngredientItems listItem = listItemList.get(position);
        remoteView.setTextViewText(R.id.ingredient_name_tv, listItem.ingredient);
        remoteView.setTextViewText(R.id.quantity_tv, listItem.quantity);
        remoteView.setTextViewText(R.id.measure_tv, listItem.measure);

        return remoteView;
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
        return false;
    }

}

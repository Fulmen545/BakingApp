package com.riso.android.bakingapp.util;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.riso.android.bakingapp.data.IngredientItems;

/**
 * Created by richard.janitor on 13-Jan-18.
 */

public class WidgetService extends RemoteViewsService {
    private static final String INGRED_ARRAY = "ingred_array";


    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        int appWidgetId = intent.getIntExtra(
                AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
        Bundle b = intent.getBundleExtra("bundle");
        Parcelable[] ingArray = b.getParcelableArray(INGRED_ARRAY);
        if (ingArray == null) {
            ingArray = new Parcelable[1];
            ingArray[0] = new IngredientItems("Ingredients23 ", "", "");
        }

        return (new ListProvider(this.getApplicationContext(), intent, ingArray));

    }
}

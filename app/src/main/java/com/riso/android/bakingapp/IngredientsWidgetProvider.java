package com.riso.android.bakingapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.widget.RemoteViews;

import com.riso.android.bakingapp.data.IngredientItems;

/**
 * Implementation of App Widget functionality.
 */
public class IngredientsWidgetProvider extends AppWidgetProvider {
    private static final String INGRED_ARRAY = "ingred_array";
    private static final String WIDGET_INGR = "wg_ingred";
    private static final String WIDGET_QUAN = "wg_quan";
    private static final String WIDGET_MEAS = "wg_meas";
    private  Parcelable[] mIngredientList;
    private String ingMsq = "Go to Ingredients step to fill the widget";
    private String ingName = "Ingredients";
    private String quan = "";
    private String meas = "";


    private void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredients_widget_provider);
        if (ingName.equals("Ingredients")){
            views.setTextViewText(R.id.wg_ingredient_name, ingMsq);
        } else {
            views.setTextViewText(R.id.wg_ingredient_name, ingName);
        }
        views.setTextViewText(R.id.wg_quantity, quan);
        views.setTextViewText(R.id.wg_measure, meas);
        Intent mainIntent;
//        if (ingName.equals("Ingredients")) {
            mainIntent = new Intent(context, MainActivity.class);
//        } else {
//            mainIntent = new Intent(context, DetailActivity.class);
////        }
        PendingIntent mainPendingIntent = PendingIntent.getActivity(context, 0, mainIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.wg_ingredient_name, mainPendingIntent);
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredients_widget_provider);
            updateAppWidget(context, appWidgetManager, appWidgetId);
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getExtras() != null){
            mIngredientList =  intent.getExtras().getParcelableArray(INGRED_ARRAY);
            if (mIngredientList != null) {
                ingName = intent.getStringExtra(WIDGET_INGR);
                quan = intent.getStringExtra(WIDGET_QUAN);
                meas = intent.getStringExtra(WIDGET_MEAS);
            }
        }
        super.onReceive(context, intent);
    }
}


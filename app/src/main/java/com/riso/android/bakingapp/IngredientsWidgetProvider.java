package com.riso.android.bakingapp;

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
    private String ingName = "Ingredients";
    private String quan = "";
    private String meas = "";
//    @BindView(R.id.wg_listview)
//    ListView wg_listView;


//    private RemoteViews updateAppWidget(Context context, AppWidgetManager appWidgetManager,
    private void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

//        CharSequence widgetText = context.getString(R.string.appwidget_text);
        CharSequence widgetText = "Skuska";
        IngredientItems[] ingItems;
//        ButterKnife.bind(this, view);
//        if (mIngredientList == null) {
//            widgetText = "Ingredients";
////            wg_listView.setVisibility(View.GONE);
//        } else {
//            IngredientItems item = (IngredientItems) mIngredientList[0];
//            widgetText = item.ingredient;
//            ingItems = new IngredientItems[mIngredientList.length];
//            for (int i =0; i<mIngredientList.length; i++){
//                ingItems[i]= (IngredientItems) mIngredientList[i];
//            }
////        IngredientItems[] ingItems = (IngredientItems[]) mIngredientList;
//        }


        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredients_widget_provider);
        views.setTextViewText(R.id.wg_ingredient_name, ingName);
        views.setTextViewText(R.id.wg_quantity, quan);
        views.setTextViewText(R.id.wg_measure, meas);
//        Bundle b = new Bundle();
//        b.putParcelableArray(INGRED_ARRAY,mIngredientList);
//        Intent intent = new Intent(context, WidgetService.class);
//        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
//        intent.setData(Uri.parse(
//                intent.toUri(Intent.URI_INTENT_SCHEME)));
//        intent.putExtra("bundle",b);

//        views.setRemoteAdapter(appWidgetId, R.id.wg_listview, intent);

//        return views;
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
//            Bundle b = new Bundle();
//            b.putParcelableArray(INGRED_ARRAY,mIngredientList);
//            Intent intent = new Intent(context, WidgetService.class);
//            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
//            intent.setData(Uri.parse(
//                    intent.toUri(Intent.URI_INTENT_SCHEME)));
//            intent.putExtra("bundle",b);
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredients_widget_provider);
//            views.setRemoteAdapter(appWidgetId, R.id.wg_listview, intent);
//            RemoteViews remoteViews = updateAppWidget(context, appWidgetManager, appWidgetId);
            updateAppWidget(context, appWidgetManager, appWidgetId);
//            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
//        super.onUpdate(context, appWidgetManager, appWidgetIds);
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
//            int arraySize = intent.getIntExtra("size", 0);
//            mIngredientList = new IngredientItems[arraySize];
            mIngredientList =  intent.getExtras().getParcelableArray(INGRED_ARRAY);
//            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
//            ComponentName thisAppWidget = new ComponentName(context.getPackageName(), IngredientsWidgetProvider.class.getName());
//            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidget);
//            onUpdate(context, appWidgetManager,appWidgetIds);
            if (mIngredientList != null) {
//                IngredientItems item = (IngredientItems) mIngredientList[0];
                ingName = intent.getStringExtra(WIDGET_INGR);
                quan = intent.getStringExtra(WIDGET_QUAN);
                meas = intent.getStringExtra(WIDGET_MEAS);
            }
        }
        super.onReceive(context, intent);
    }

//    private class MyArrayAdapter extends ArrayAdapter<IngredientItems[]> {
//        private final Context context;
//        private final IngredientItems[] ingItems;
//
//        public MyArrayAdapter(@NonNull Context context, IngredientItems[] ingItems) {
//            super(context, -1);
//            this.context = context;
//            this.ingItems = ingItems;
//        }
//
//
////        public MyArrayAdapter(@NonNull Context context, IngredientItems[] ingItems) {
////            super(context, -1,
////                    );
////            this.context = context;
////            this.ingItems = ingItems;
////        }
//
//        @NonNull
//        @Override
//        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//            LayoutInflater inflater = (LayoutInflater) context
//                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            View rowView = inflater.inflate(R.layout.ingredient_card, parent, false);
//            TextView ingedient = rowView.findViewById(R.id.ingredient_name_tv);
//            ingedient.setText(ingItems[position].ingredient);
//            TextView quantity = rowView.findViewById(R.id.quantity_tv);
//            quantity.setText(ingItems[position].quantity);
//            TextView measure = rowView.findViewById(R.id.measure_tv);
//            measure.setText(ingItems[position].measure);
//
//            return rowView;
//        }
//    }
}


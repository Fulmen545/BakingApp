package com.riso.android.bakingapp;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.riso.android.bakingapp.data.IngredientItems;
import com.riso.android.bakingapp.data.RecipeColumns;
import com.riso.android.bakingapp.util.IngredientsAdapter;
import com.riso.android.bakingapp.util.RecipeAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment implements IngredientsAdapter.ListItemClickListener {
    private static final String POSITION = "position";
    private static final String RECIPE_NAME = "rec_name";
    private static final String RECEPT_POSITION = "recept_position";
    private static final String STEP_COUNT = "step_count";
    private static final String INGRED_ARRAY = "ingred_array";
    private static final String WIDGET_INGR = "wg_ingred";
    private static final String WIDGET_QUAN = "wg_quan";
    private static final String WIDGET_MEAS = "wg_meas";


    @BindView(R.id.back_button)
    TextView back_btn;
    @BindView(R.id.forward_button)
    TextView forward_btn;
    @BindView(R.id.detail_rv)
    RecyclerView ingredientRv;
    private int position;
    private String recipeName;
    private String recept_position;
    private int stepCount;
    private IngredientItems[] mIngredientList;
    private IngredientsAdapter mIngredientAdapter;
    private String wg_ingredient = "";
    private String wg_quantity = "";
    private String wg_measure = "";
    private boolean tabletSize;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        return view;
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        Bundle bundle = this.getArguments();
        stepCount = bundle.getInt(STEP_COUNT);
        recipeName = bundle.getString(RECIPE_NAME);
        position = bundle.getInt(POSITION, 0);
        recept_position = bundle.getString(RECEPT_POSITION);
        tabletSize = getResources().getBoolean(R.bool.isTablet);
        if (tabletSize) {
            back_btn.setVisibility(View.GONE);
            forward_btn.setVisibility(View.GONE);
        } else {
            back_btn.setText(getString(R.string.step_list));
            back_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                getActivity().dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK));
//                getActivity().dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BACK));
//                getActivity().getSupportFragmentManager().beginTransaction().remove(DetailFragment.this).commit();
//                Bundle bundle = new Bundle();
//                bundle.putInt(POSITION, Integer.parseInt(recept_position));
//                bundle.putString(RECIPE_NAME, recipeName);
////                StepListFragment slf = new StepListFragment();
////                slf.setArguments(bundle);
////                changeTo(slf, android.R.id.content);
//                Intent intent = new Intent(getActivity(), StepListActivity.class);
//                intent.putExtras(bundle);
//                startActivity(intent);
                    getActivity().finish();

                }
            });
            forward_btn.setText(getString(R.string.rec_introduction));
            forward_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putInt(POSITION, position + 1);
                    bundle.putString(RECEPT_POSITION, recept_position);
                    bundle.putString(RECIPE_NAME, recipeName);
                    bundle.putInt(STEP_COUNT, stepCount);
                    StepsFragment sf = new StepsFragment();
                    sf.setArguments(bundle);
                    changeTo(sf, android.R.id.content, "tag1");
//                Intent intent = new Intent(view.getContext(), ExoTestActivity.class);
//                startActivity(intent);
//                Intent intent = new Intent(getActivity(), StepActivity.class);
//                intent.putExtras(bundle);
//                startActivity(intent);
                }
            });
        }
        getActivity().setTitle(recipeName);
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        ingredientRv.setLayoutManager(layoutManager);
        getIngredientList();
        updateIngredientsWidget(getContext());
        mIngredientAdapter = new IngredientsAdapter(mIngredientList, DetailFragment.this);
        ingredientRv.setAdapter(mIngredientAdapter);
    }

    @Override
    public void onListItemClick(int listItem) {
    }

//    private void changeTo(Fragment fragment, int containerViewId) {
//        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
////        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
//        fragmentManager.beginTransaction().replace(containerViewId, fragment).commit();
//        getActivity().getSupportFragmentManager().beginTransaction().remove(DetailFragment.this).commit();
//    }

    public void changeTo(Fragment fragment, int containerViewId, String tag) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        fragmentManager.beginTransaction().replace(containerViewId, fragment, tag == null ? fragment.getClass().getName() : tag).commit();

    }

    private void getIngredientList() {
        Cursor c = getActivity().getContentResolver().query(RecipeColumns.RecipeEntry.CONTENT_URI_INGREDIENTS,
                new String[]{RecipeColumns.RecipeEntry.INGREDIENT, RecipeColumns.RecipeEntry.QUANTITY, RecipeColumns.RecipeEntry.MEASURE},
                RecipeColumns.RecipeEntry.RECIPE_ID + "=?",
                new String[]{recept_position},
                RecipeColumns.RecipeEntry._ID);
        if (c.getCount() != 0) {
            IngredientItems item;
            mIngredientList = new IngredientItems[c.getCount()];
            int i = 0;
            if (c.moveToFirst()) {
                do {
                    String cIngredient = c.getString(c.getColumnIndex(RecipeColumns.RecipeEntry.INGREDIENT));
                    String cQuantity = c.getString(c.getColumnIndex(RecipeColumns.RecipeEntry.QUANTITY));
                    String cMeasure = c.getString(c.getColumnIndex(RecipeColumns.RecipeEntry.MEASURE));
                    item = new IngredientItems(cIngredient, cQuantity, cMeasure);
                    mIngredientList[i] = item;
                    i++;
                } while (c.moveToNext());
            }
        }
    }

    public void updateIngredientsWidget(Context context) {
        Intent intent = new Intent(context, IngredientsWidgetProvider.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
//        Bundle bundle = new Bundle();
//        bundle.putSerializable(INGRED_ARRAY, mIngredientList);
//        intent.putExtras(bundle);
        buildIngredientsForWidget();
        intent.putExtra(INGRED_ARRAY, mIngredientList);
        intent.putExtra(WIDGET_INGR, wg_ingredient);
        intent.putExtra(WIDGET_QUAN, wg_quantity);
        intent.putExtra(WIDGET_MEAS, wg_measure);
//        intent.putExtra("size", mIngredientList.length);
        int[] ids = AppWidgetManager.getInstance(context).getAppWidgetIds(new ComponentName(context, IngredientsWidgetProvider.class));
        if (ids != null && ids.length > 0) {
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
            context.sendBroadcast(intent);
        }
    }

    private void buildIngredientsForWidget() {
        String delimeter = "\n";
        int ingLength;
        for (IngredientItems item : mIngredientList) {
            wg_ingredient += item.ingredient + delimeter;
            ingLength = item.ingredient.length();
            wg_quantity += item.quantity + delimeter;
            wg_measure += item.measure + delimeter;
            if (ingLength > 29) {
                wg_quantity += delimeter;
                wg_measure += delimeter;
            }
        }
    }

}

package com.riso.android.bakingapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.riso.android.bakingapp.data.RecipeColumns;
import com.riso.android.bakingapp.util.RecipeAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by richard.janitor on 28-Dec-17.
 */

public class StepListFragment extends Fragment implements RecipeAdapter.ListItemClickListener {
    private static final String POSITION = "position";
    private static final String RECEPT_POSITION = "recept_position";
    private static final String RECIPE_NAME = "rec_name";
    private static final String STEP_LIST = "step_list";
    private static final String STEP_COUNT = "step_count";
    private static final String STEP_PRESSED = "step_pressed";
    private static final String EXO_POSITION = "exo_position";
    private static final String RV_STATE = "rv_state";
    private static final String TAG = "StepListFragment";
    private String recipePosition;
    @BindView(R.id.rv_recipes)
    RecyclerView mRecipeNamesList;
    public RecipeAdapter mRecipeAdapter;
    public String[] recipeSteps;
    private String recipeTitle;
    private boolean tabletSize;
    private int stepPressed = 0;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        tabletSize = getResources().getBoolean(R.bool.isTablet);
///////////////////////////////////////
        Bundle bundle = this.getArguments();
        recipePosition = Integer.toString(bundle.getInt(POSITION, 0));
        recipeTitle = bundle.getString(RECIPE_NAME);
        getActivity().setTitle(recipeTitle);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecipeNamesList.setLayoutManager(layoutManager);
        getStepTitles();
        if (mRecipeAdapter == null) {
            mRecipeAdapter = new RecipeAdapter(recipeSteps, StepListFragment.this, true, stepPressed, tabletSize, null);
            mRecipeNamesList.setAdapter(mRecipeAdapter);
        }
/////////////////////////////////////////

//        mRecipeAdapter = new RecipeAdapter(recipeSteps, StepListFragment.this, true, stepPressed, tabletSize, null);
//        mRecipeNamesList.setAdapter(mRecipeAdapter);
        Log.i(TAG, "RISO - Here is value: " + recipePosition);
    }


    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null){
            recipePosition = savedInstanceState.getString(POSITION);
            recipeTitle = savedInstanceState.getString(RECIPE_NAME);
            recipeSteps = savedInstanceState.getStringArray(STEP_LIST);
            stepPressed = savedInstanceState.getInt(STEP_PRESSED);
            mRecipeAdapter.setItemPosition(stepPressed);
//            Parcelable layoutManagerSavedState = null;
//            if (savedInstanceState instanceof Bundle) {
//                layoutManagerSavedState = savedInstanceState.getParcelable(RV_STATE);
//            }
////            ExoPlayerSingleton.getInstance().setExoCurrentposition(savedInstanceState.getInt(EXO_POSITION));
//            mRecipeAdapter = new RecipeAdapter(recipeSteps, StepListFragment.this, true, stepPressed, tabletSize, null);
//            mRecipeNamesList.setAdapter(mRecipeAdapter);
//            mRecipeNamesList.getLayoutManager().onRestoreInstanceState(layoutManagerSavedState);
        } else {
//            Bundle bundle = this.getArguments();
//            recipePosition = Integer.toString(bundle.getInt(POSITION, 0));
//            recipeTitle = bundle.getString(RECIPE_NAME);
//            getActivity().setTitle(recipeTitle);
//            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
//            mRecipeNamesList.setLayoutManager(layoutManager);
//            getStepTitles();
//            if (mRecipeAdapter == null) {
//                mRecipeAdapter = new RecipeAdapter(recipeSteps, StepListFragment.this, true, stepPressed, tabletSize, null);
//                mRecipeNamesList.setAdapter(mRecipeAdapter);
//            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(POSITION, recipePosition);
        outState.putString(RECIPE_NAME, recipeTitle);
        outState.putStringArray(STEP_LIST, recipeSteps);
        outState.putInt(STEP_PRESSED, stepPressed);
    }

    private void getStepTitles() {
        Cursor c = getActivity().getContentResolver().query(RecipeColumns.RecipeEntry.CONTENT_URI_STEPS,
                new String[]{RecipeColumns.RecipeEntry.STEP_TITLE},
                RecipeColumns.RecipeEntry.RECIPE_ID + "=?",
                new String[]{recipePosition},
                RecipeColumns.RecipeEntry._ID);
        if (c.getCount() != 0) {
            recipeSteps = new String[c.getCount() + 1];
            recipeSteps[0] = "Ingredients";
            int i = 1;
            int stepId;
            if (c.moveToFirst()) {
                do {
                    stepId=i-1;
                    if (i == 1) {
                        recipeSteps[i] = c.getString(c.getColumnIndex(RecipeColumns.RecipeEntry.STEP_TITLE));
                    } else {
                        recipeSteps[i] = "Step " + stepId + ": " + c.getString(c.getColumnIndex(RecipeColumns.RecipeEntry.STEP_TITLE));
                    }
                    i++;
                } while (c.moveToNext());
            }
        }
    }

    @Override
    public void onListItemClick(int listItem) {
        stepPressed=listItem;
        Bundle bundle = new Bundle();
        bundle.putInt(POSITION, listItem);
        bundle.putString(RECEPT_POSITION, recipePosition);
        bundle.putString(RECIPE_NAME, recipeTitle);
        bundle.putInt(STEP_COUNT, recipeSteps.length);
        if (tabletSize){
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            if (listItem == 0){
                DetailFragment detailFragment = new DetailFragment();
                detailFragment.setArguments(bundle);
                fragmentManager.beginTransaction()
                        .replace(R.id.detail_container, detailFragment, "tag1")
                        .commit();
            } else {
                StepsFragment stepsFragment = new StepsFragment();
                stepsFragment.setArguments(bundle);
                fragmentManager.beginTransaction()
                        .replace(R.id.detail_container, stepsFragment, "tag1")
                        .commit();
            }
        } else {
            Intent intent = new Intent(getActivity(), StepActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }
}

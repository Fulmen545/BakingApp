package com.riso.android.bakingapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
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
    private static final String STEP_COUNT = "step_count";
    private static final String TAG = "StepListFragment";
    private String recipePosition;
    @BindView(R.id.rv_recipes)
    RecyclerView mRecipeNamesList;
    public RecipeAdapter mRecipeAdapter;
    public String[] recipeSteps;
    private String recipeTitle;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);

        Bundle bundle = this.getArguments();
        recipePosition = Integer.toString(bundle.getInt(POSITION, 0));
        recipeTitle = bundle.getString(RECIPE_NAME);
        getActivity().setTitle(recipeTitle);
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        mRecipeNamesList.setLayoutManager(layoutManager);
        getStepTitles();
        mRecipeAdapter = new RecipeAdapter(recipeSteps, StepListFragment.this);
        mRecipeNamesList.setAdapter(mRecipeAdapter);
        Log.i(TAG, "RISO - Here is value: " + recipePosition);
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

    private void changeTo(Fragment fragment, int containerViewId) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        fragmentManager.beginTransaction().replace(containerViewId, fragment).addToBackStack("tag1").commit();
    }

    @Override
    public void onListItemClick(int listItem) {
        Bundle bundle = new Bundle();
        bundle.putInt(POSITION, listItem);
        bundle.putString(RECEPT_POSITION, recipePosition);
        bundle.putString(RECIPE_NAME, recipeTitle);
        bundle.putInt(STEP_COUNT, recipeSteps.length);
        if (listItem == 0) {
            DetailFragment df = new DetailFragment();
            df.setArguments(bundle);
            changeTo(df, android.R.id.content);
        } else {
            StepsFragment sf = new StepsFragment();
            sf.setArguments(bundle);
            changeTo(sf, android.R.id.content);
        }
    }
}

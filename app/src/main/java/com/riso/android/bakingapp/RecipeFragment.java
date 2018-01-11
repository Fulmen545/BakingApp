package com.riso.android.bakingapp;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.riso.android.bakingapp.data.HttpHandler;
import com.riso.android.bakingapp.data.RecipeColumns;
import com.riso.android.bakingapp.util.RecipeAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * create an instance of this fragment.
 */
public class RecipeFragment extends Fragment implements RecipeAdapter.ListItemClickListener {
    private static final String POSITION = "position";
    private static final String RECIPE_NAME = "rec_name";

    public String[] recipeNames;
    @BindView(R.id.rv_recipes)
    RecyclerView mRecipeNamesList;
    public RecipeAdapter mRecipeAdapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_recipe, container, false);


        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(R.string.app_name);
        ButterKnife.bind(this, view);
        new GetRecipes().execute();
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        mRecipeNamesList.setLayoutManager(layoutManager);

    }

    public void insertIngredients(String recipeID, String recName, String ingredID, String quantity,
                                  String measure, String ingredient) {
        ContentValues cv = new ContentValues();
        cv.put(RecipeColumns.RecipeEntry.RECIPE_ID, recipeID);
        cv.put(RecipeColumns.RecipeEntry.RECIPE_NAME, recName);
        cv.put(RecipeColumns.RecipeEntry.INGRED_ID, ingredID);
        cv.put(RecipeColumns.RecipeEntry.QUANTITY, quantity);
        cv.put(RecipeColumns.RecipeEntry.MEASURE, measure);
        cv.put(RecipeColumns.RecipeEntry.INGREDIENT, ingredient);
        getContext().getContentResolver().insert(RecipeColumns.RecipeEntry.CONTENT_URI_INGREDIENTS, cv);
    }

    public void insertSteps(String recipeID, String stepID, String stepTitle, String description,
                            String url) {
        ContentValues cv = new ContentValues();
        cv.put(RecipeColumns.RecipeEntry.RECIPE_ID, recipeID);
        cv.put(RecipeColumns.RecipeEntry.STEP_ID, stepID);
        cv.put(RecipeColumns.RecipeEntry.STEP_TITLE, stepTitle);
        cv.put(RecipeColumns.RecipeEntry.DESCRIPTION, description);
        cv.put(RecipeColumns.RecipeEntry.URL, url);
        getContext().getContentResolver().insert(RecipeColumns.RecipeEntry.CONTENT_URI_STEPS, cv);
    }

    private void changeTo(Fragment fragment, int containerViewId) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        fragmentManager.beginTransaction().replace(containerViewId, fragment).addToBackStack("tag").commit();
    }

    @Override
    public void onListItemClick(int listItem) {
        Bundle bundle = new Bundle();
        bundle.putInt(POSITION, listItem);
        bundle.putString(RECIPE_NAME, recipeNames[listItem]);
//        StepListFragment slf = new StepListFragment();
//        slf.setArguments(bundle);
//        changeTo(slf, android.R.id.content);
        Intent intent = new Intent(getActivity(), StepListActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public boolean isInsideDb(String recipeName) {
        Cursor c = getActivity().getContentResolver().query(RecipeColumns.RecipeEntry.CONTENT_URI_INGREDIENTS,
                new String[]{RecipeColumns.RecipeEntry.RECIPE_NAME},
                RecipeColumns.RecipeEntry.RECIPE_NAME + "=?",
                new String[]{recipeName},
                null);
        if (!c.moveToNext()) {
            return false;
        } else {
            return true;
        }
    }

    private class GetRecipes extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            HttpHandler hh = new HttpHandler();
            String movieUrl = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
            URL url;
            String jsonStr = null;
            try {
                url = new URL(movieUrl);
                jsonStr = hh.makeServiceCall(url);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (jsonStr != null) {
                try {
//                    JSONObject jsonObject = new JSONObject(jsonStr);
                    JSONArray recipes = new JSONArray(jsonStr);
                    recipeNames = new String[recipes.length()];
                    for (int i = 0; i < recipes.length(); i++) {
                        String mRecipeName = recipes.getJSONObject(i).getString("name");
                        recipeNames[i] = mRecipeName;
                        if (!isInsideDb(mRecipeName)) {
                            String mRecipeID = Integer.toString(i);
                            JSONObject o = recipes.getJSONObject(i);
                            JSONArray ingredients = o.getJSONArray("ingredients");
                            for (int j = 0; j < ingredients.length(); j++) {
                                String mIngredID = Integer.toString(j);
                                JSONObject ing = ingredients.getJSONObject(j);
                                insertIngredients(mRecipeID, mRecipeName, mIngredID, ing.getString("quantity"), ing.getString("measure"), ing.getString("ingredient"));
                            }
                            JSONArray steps = o.getJSONArray("steps");
                            for (int k = 0; k < steps.length(); k++) {
                                String mStepID = Integer.toString(k);
                                JSONObject stp = steps.getJSONObject(k);
                                insertSteps(mRecipeID, mStepID, stp.getString("shortDescription"),
                                        stp.getString("description"), stp.getString("videoURL"));
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mRecipeAdapter = new RecipeAdapter(recipeNames, RecipeFragment.this);
            mRecipeNamesList.setAdapter(mRecipeAdapter);
        }
    }
}

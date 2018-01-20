package com.riso.android.bakingapp;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
    public String[] recipeImages;
    @BindView(R.id.rv_recipes)
    RecyclerView mRecipeNamesList;
    public RecipeAdapter mRecipeAdapter;
    private boolean tabletSize;


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
        tabletSize = getResources().getBoolean(R.bool.isTablet);

        if (!isOnline() && isDbEmpty()) {
            internetDialog();
        } else {
            if (!isOnline())
                internetDialogWithDB();
            new GetRecipes().execute();
            if (!tabletSize) {
                LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
                mRecipeNamesList.setLayoutManager(layoutManager);
            } else {
                GridLayoutManager layoutManager = new GridLayoutManager(view.getContext(), 3);
                mRecipeNamesList.setLayoutManager(layoutManager);
            }
        }


    }

    public void insertIngredients(String recipeID, String recName, String ingredID, String quantity,
                                  String measure, String ingredient, String image) {
        ContentValues cv = new ContentValues();
        cv.put(RecipeColumns.RecipeEntry.RECIPE_ID, recipeID);
        cv.put(RecipeColumns.RecipeEntry.RECIPE_NAME, recName);
        cv.put(RecipeColumns.RecipeEntry.INGRED_ID, ingredID);
        cv.put(RecipeColumns.RecipeEntry.QUANTITY, quantity);
        cv.put(RecipeColumns.RecipeEntry.MEASURE, measure);
        cv.put(RecipeColumns.RecipeEntry.INGREDIENT, ingredient);
        cv.put(RecipeColumns.RecipeEntry.IMAGE, image);
        getContext().getContentResolver().insert(RecipeColumns.RecipeEntry.CONTENT_URI_INGREDIENTS, cv);
    }

    public void insertSteps(String recipeID, String stepID, String stepTitle, String description,
                            String url, String thumb) {
        ContentValues cv = new ContentValues();
        cv.put(RecipeColumns.RecipeEntry.RECIPE_ID, recipeID);
        cv.put(RecipeColumns.RecipeEntry.STEP_ID, stepID);
        cv.put(RecipeColumns.RecipeEntry.STEP_TITLE, stepTitle);
        cv.put(RecipeColumns.RecipeEntry.DESCRIPTION, description);
        cv.put(RecipeColumns.RecipeEntry.URL, url);
        cv.put(RecipeColumns.RecipeEntry.THUMBNAIL, thumb);
        getContext().getContentResolver().insert(RecipeColumns.RecipeEntry.CONTENT_URI_STEPS, cv);
    }

    @Override
    public void onListItemClick(int listItem) {
        Bundle bundle = new Bundle();
        bundle.putInt(POSITION, listItem);
        bundle.putString(RECIPE_NAME, recipeNames[listItem]);
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

    public void getRecipeNames() {
        Cursor c = getActivity().getContentResolver().query(RecipeColumns.RecipeEntry.CONTENT_URI_INGREDIENTS,
                new String[]{"DISTINCT " + RecipeColumns.RecipeEntry.RECIPE_NAME, RecipeColumns.RecipeEntry.IMAGE},
                null,
                null,
                RecipeColumns.RecipeEntry._ID);
        if (c.getCount() != 0) {
            recipeNames = new String[c.getCount()];
            recipeImages = new String[c.getCount()];
            int i = 0;
            if (c.moveToFirst()) {
                do {
                    recipeNames[i] = c.getString(c.getColumnIndex(RecipeColumns.RecipeEntry.RECIPE_NAME));
                    recipeNames[i] = c.getString(c.getColumnIndex(RecipeColumns.RecipeEntry.IMAGE));
                    i++;
                } while (c.moveToNext());
            }
        }
    }

    private boolean isDbEmpty() {
        Cursor c = getActivity().getContentResolver().query(RecipeColumns.RecipeEntry.CONTENT_URI_INGREDIENTS,
                new String[]{RecipeColumns.RecipeEntry.RECIPE_NAME},
                null,
                null,
                RecipeColumns.RecipeEntry._ID);
        if (c.getCount() != 0) {
            return false;
        }
        return true;
    }

    private class GetRecipes extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            if (isOnline()) {
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
                        JSONArray recipes = new JSONArray(jsonStr);
                        recipeNames = new String[recipes.length()];
                        recipeImages = new String[recipes.length()];
                        for (int i = 0; i < recipes.length(); i++) {
                            String mRecipeName = recipes.getJSONObject(i).getString("name");
                            String mRecipeImage = recipes.getJSONObject(i).getString("image");
                            recipeNames[i] = mRecipeName;
                            recipeImages[i] = mRecipeImage;
                            if (!isInsideDb(mRecipeName)) {
                                String mRecipeID = Integer.toString(i);
                                JSONObject o = recipes.getJSONObject(i);
                                JSONArray ingredients = o.getJSONArray("ingredients");
                                for (int j = 0; j < ingredients.length(); j++) {
                                    String mIngredID = Integer.toString(j);
                                    JSONObject ing = ingredients.getJSONObject(j);
                                    insertIngredients(mRecipeID, mRecipeName, mIngredID, ing.getString("quantity"), ing.getString("measure"), ing.getString("ingredient"), mRecipeImage);
                                }
                                JSONArray steps = o.getJSONArray("steps");
                                for (int k = 0; k < steps.length(); k++) {
                                    String mStepID = Integer.toString(k);
                                    JSONObject stp = steps.getJSONObject(k);
                                    insertSteps(mRecipeID, mStepID, stp.getString("shortDescription"),
                                            stp.getString("description"), stp.getString("videoURL"), stp.getString("thumbnailURL"));
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                getRecipeNames();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mRecipeAdapter = new RecipeAdapter(recipeNames, RecipeFragment.this, false, 0, tabletSize, recipeImages);
            mRecipeNamesList.setAdapter(mRecipeAdapter);
        }


    }

    private void internetDialog() {
        Log.w("Recipes", "Something went wrong");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        String msg1 = getString(R.string.dialog_msg1);
        String msg2 = getString(R.string.dialog_msg2);
        builder.setMessage(msg1 + "\n" + msg2)
                .setTitle("No internet connection")
                .setNeutralButton(R.string.refresh, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = getActivity().getIntent();
                        getActivity().finish();
                        startActivity(intent);
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        getActivity().finish();
                    }
                })
                .show();
    }

    private void internetDialogWithDB() {
        Log.w("Recipes", "Something went wrong");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Recipes will be loaded from database")
                .setTitle("No internet connection")
                .setPositiveButton(android.R.string.yes, null)
                .show();
    }

    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}

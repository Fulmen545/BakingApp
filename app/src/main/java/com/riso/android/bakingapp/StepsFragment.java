package com.riso.android.bakingapp;


import android.database.Cursor;
import android.graphics.Color;
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
import com.riso.android.bakingapp.data.StepItems;
import com.riso.android.bakingapp.util.IngredientsAdapter;
import com.riso.android.bakingapp.util.StepsAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class StepsFragment extends Fragment implements StepsAdapter.ListItemClickListener{
    private static final String POSITION = "position";
    private static final String RECIPE_NAME = "rec_name";
    private static final String RECEPT_POSITION = "recept_position";
    private static final String STEP_COUNT = "step_count";

    private int position;
    private String stepPosition;
    private String recept_position;
    private String recipeName;
    private int stepForward;
    private int stepBack;
    private int stepCount;
    @BindView(R.id.back_button)
    TextView back_btn;
    @BindView(R.id.forward_button)
    TextView forward_btn;
    @BindView(R.id.steps_rv)
    RecyclerView stepsRv;
    private StepItems[] mStepArray;
    private StepsAdapter mStepAdapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_steps, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(R.string.app_name);
        ButterKnife.bind(this, view);
        Bundle bundle = this.getArguments();
        stepCount = bundle.getInt(STEP_COUNT);
        recipeName = bundle.getString(RECIPE_NAME);
        position = bundle.getInt(POSITION, 0);
        stepPosition = Integer.toString(position-1);
        recept_position = bundle.getString(RECEPT_POSITION);
        if (position==1) {
            back_btn.setText(getString(R.string.ingredients));
        } else {
            stepBack = position-1;
            back_btn.setText(getString(R.string.step_number) + " " + stepBack);
        }
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position==1){
                    Bundle bundle = new Bundle();
                    bundle.putInt(POSITION, position);
                    bundle.putString(RECEPT_POSITION, recept_position);
                    bundle.putString(RECIPE_NAME, recipeName);
                    bundle.putInt(STEP_COUNT, stepCount);
                    DetailFragment df = new DetailFragment();
                    df.setArguments(bundle);
                    changeTo(df, android.R.id.content);
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putInt(POSITION, stepBack);
                    bundle.putString(RECEPT_POSITION, recept_position);
                    bundle.putString(RECIPE_NAME, recipeName);
                    bundle.putInt(STEP_COUNT, stepCount);
                    StepsFragment sf = new StepsFragment();
                    sf.setArguments(bundle);
                    changeTo(sf, android.R.id.content);
                }
            }
        });
        stepForward=position+1;
        if (stepCount==stepForward){
            forward_btn.setBackgroundColor(Color.parseColor("#bdbdbd"));
        } else {
            forward_btn.setText(getString(R.string.step_number) + " " + stepForward );
            forward_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putInt(POSITION, stepForward);
                    bundle.putString(RECEPT_POSITION, recept_position);
                    bundle.putString(RECIPE_NAME, recipeName);
                    bundle.putInt(STEP_COUNT, stepCount);
                    StepsFragment sf = new StepsFragment();
                    sf.setArguments(bundle);
                    changeTo(sf, android.R.id.content);
                }
            });
        }
        getActivity().setTitle(recipeName);

        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        stepsRv.setLayoutManager(layoutManager);
        getStepArray();
        mStepAdapter = new StepsAdapter(mStepArray, StepsFragment.this);
        stepsRv.setAdapter(mStepAdapter);
    }

    private void changeTo(Fragment fragment, int containerViewId) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        fragmentManager.beginTransaction().replace(containerViewId, fragment).commit();
    }

    private void getStepArray() {
        Cursor c = getActivity().getContentResolver().query(RecipeColumns.RecipeEntry.CONTENT_URI_STEPS,
                new String[]{RecipeColumns.RecipeEntry.STEP_TITLE, RecipeColumns.RecipeEntry.DESCRIPTION, RecipeColumns.RecipeEntry.URL},
                RecipeColumns.RecipeEntry.RECIPE_ID + "=? AND " + RecipeColumns.RecipeEntry.STEP_ID + "=?",
                new String[]{recept_position, stepPosition},
                RecipeColumns.RecipeEntry._ID);
        if (c.getCount() != 0) {
            StepItems item;
            mStepArray = new StepItems[c.getCount()];
            int i=0;
            if (c.moveToFirst()){
                do {
                    String cTitle = c.getString(c.getColumnIndex(RecipeColumns.RecipeEntry.STEP_TITLE));
                    String cDescription = c.getString(c.getColumnIndex(RecipeColumns.RecipeEntry.DESCRIPTION));
                    String cUrl = c.getString(c.getColumnIndex(RecipeColumns.RecipeEntry.URL));
                    item = new StepItems(cTitle, cDescription, cUrl);
                    mStepArray[i] = item;
                    i++;
                } while (c.moveToNext());
            }
        }
    }

    @Override
    public void onListItemClick(int listItem) {

    }


}

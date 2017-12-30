package com.riso.android.bakingapp;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
public class DetailFragment extends Fragment implements IngredientsAdapter.ListItemClickListener{
    private static final String POSITION = "position";
    private static final String RECIPE_NAME = "rec_name";
    private static final String RECEPT_POSITION = "recept_position";


    @BindView(R.id.back_button)
    TextView back_btn;
    @BindView(R.id.forward_button)
    TextView forward_btn;
    @BindView(R.id.detail_rv)
    RecyclerView ingredientRv;
    private int position;
    private String recept_position;
    private IngredientItems[] mIngredientList;
    private IngredientsAdapter mIngredientAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        Bundle bundle = this.getArguments();
        position = bundle.getInt(POSITION, 0);
        recept_position = bundle.getString(RECEPT_POSITION);
        back_btn.setText(getString(R.string.step_list));
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK));
                getActivity().dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BACK));
            }
        });
        forward_btn.setText(getString(R.string.step_number) + " " + position + 1);
        getActivity().setTitle(bundle.getString(RECIPE_NAME));
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        ingredientRv.setLayoutManager(layoutManager);
        getIngredientList();
        mIngredientAdapter = new IngredientsAdapter(mIngredientList, DetailFragment.this);
        ingredientRv.setAdapter(mIngredientAdapter);
    }

    @Override
    public void onListItemClick(int listItem) {
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
            int i=0;
            if (c.moveToFirst()){
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
}

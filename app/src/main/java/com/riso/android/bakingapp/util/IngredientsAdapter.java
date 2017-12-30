package com.riso.android.bakingapp.util;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.riso.android.bakingapp.R;
import com.riso.android.bakingapp.data.IngredientItems;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by richard.janitor on 30-Dec-17.
 */

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.IngredientsViewHolder> {
    private static final String TAG = RecipeAdapter.class.getSimpleName();
    final private ListItemClickListener mOnClickListener;
    
    private IngredientItems[] mIngredientItems;

    public IngredientsAdapter(IngredientItems[] ingredientItems, ListItemClickListener mOnClickListener) {
        this.mOnClickListener = mOnClickListener;
        this.mIngredientItems = ingredientItems;
    }


    @Override
    public IngredientsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.ingredient_card;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, parent, false);
        IngredientsViewHolder viewHolder = new IngredientsViewHolder(view);
        Log.d(TAG, "onCreateViewHolder: number of ViewHolders created: "
                + mIngredientItems.length);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(IngredientsViewHolder holder, int position) {
        Log.d(TAG, "#" + position);
        holder.bind(position);
    }


    @Override
    public int getItemCount() {
        return mIngredientItems.length;
    }

    public interface ListItemClickListener {
        void onListItemClick(int listItem);

    }
    
    class IngredientsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.ingredient_name_tv)
        TextView ingredientName;
        @BindView(R.id.quantity_tv)
        TextView quantity;
        @BindView(R.id.measure_tv)
        TextView measure;
        
        public IngredientsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }

        public void bind(int position) {
            ingredientName.setText(mIngredientItems[position].ingredient);
            quantity.setText(mIngredientItems[position].quantity);
            measure.setText(mIngredientItems[position].measure);
        }
    }
}

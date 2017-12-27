package com.riso.android.bakingapp.util;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.riso.android.bakingapp.R;

/**
 * Created by richard.janitor on 27-Dec-17.
 */

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {
    private static final String TAG = RecipeAdapter.class.getSimpleName();
    final private ListItemClickListener mOnClickListener;

    private String[] mRecipeNames;

    public RecipeAdapter(String[] recipeNames, ListItemClickListener mOnClickListener) {
        mRecipeNames = recipeNames;
        this.mOnClickListener = mOnClickListener;
    }

    public interface ListItemClickListener {
        void onListItemClick(int listItem);

    }

    @Override
    public RecipeAdapter.RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.recipe_card;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, parent, false);
        RecipeViewHolder viewHolder = new RecipeViewHolder(view);
        Log.d(TAG, "onCreateViewHolder: number of ViewHolders created: "
                + mRecipeNames.length);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecipeViewHolder holder, int position) {
        Log.d(TAG, "#" + position);
        holder.bind(position);
    }


    @Override
    public int getItemCount() {
        return mRecipeNames.length;
    }

    class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //        @BindView(R.id.recipeTitle) TextView recipeNameTv;
        TextView recipeNameTv;
        public RecipeViewHolder(View itemView) {
            super(itemView);
//            ButterKnife.bind((Activity) itemView.getContext());
            recipeNameTv = itemView.findViewById(R.id.recipeTitle);
            itemView.setOnClickListener(this);
        }

        void bind(int listIndex) {
            recipeNameTv.setText(mRecipeNames[listIndex]);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }
    }
}

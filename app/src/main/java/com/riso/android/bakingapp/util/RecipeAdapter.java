package com.riso.android.bakingapp.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.riso.android.bakingapp.R;
import com.squareup.picasso.Picasso;

/**
 * Created by richard.janitor on 27-Dec-17.
 */

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {
    private static final String TAG = RecipeAdapter.class.getSimpleName();
    final private ListItemClickListener mOnClickListener;

    private String[] mRecipeNames;
    private String[] mRecipeIamges;
    public boolean steps;
    private int stepPressed;
    boolean tablet;

    public RecipeAdapter(String[] recipeNames, ListItemClickListener mOnClickListener, boolean steps, int stepPressed, boolean tablet, String[] recipeImages) {
        mRecipeNames = recipeNames;
        this.mOnClickListener = mOnClickListener;
        this.steps = steps;
        this.stepPressed = stepPressed;
        this.tablet = tablet;
        mRecipeIamges=recipeImages;
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

    public void setItemPosition(int position){
        this.stepPressed=position;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return mRecipeNames.length;
    }

    class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.recipeTitle)
        TextView recipeNameTv;
        @BindView(R.id.recipeCardLayout)
        LinearLayout linearLayout;
        @BindView(R.id.imageView)
        ImageView recipeImage;

        public RecipeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        void bind(int listIndex) {
            if (steps && tablet) {
                if (listIndex == stepPressed) {
                    linearLayout.setBackgroundResource(R.drawable.pressed_round_edge);
                    recipeNameTv.setTextColor(Color.WHITE);
                } else {
                    linearLayout.setBackgroundResource(R.drawable.round_edge);
                    recipeNameTv.setTextColor(Color.DKGRAY);
                }
            }
            if (mRecipeNames[listIndex].startsWith("Step") || mRecipeNames[listIndex].startsWith("Recipe Introduction")) {
                recipeNameTv.setText(mRecipeNames[listIndex]);
                recipeNameTv.setTextAppearance(itemView.getContext(), R.style.TitleSteps);
            } else if (mRecipeNames[listIndex] == "Ingredients") {
                recipeNameTv.setText(mRecipeNames[listIndex]);
                recipeNameTv.setTextAppearance(itemView.getContext(), R.style.Ingredients);
            } else {

                if (!mRecipeIamges[listIndex].isEmpty() && mRecipeIamges != null){
                    recipeImage.setVisibility(View.VISIBLE);
                    Picasso.with(itemView.getContext())
                            .load(mRecipeIamges[listIndex])
                            .placeholder(R.drawable.cupcake)
                            .into(recipeImage);

                }
                recipeNameTv.setText(mRecipeNames[listIndex]);
            }
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
            stepPressed=clickedPosition;
//            if (steps) {
//                v.setSelected(true);
//            }
            notifyDataSetChanged();
        }
    }

}

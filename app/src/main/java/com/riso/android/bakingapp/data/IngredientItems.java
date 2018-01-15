package com.riso.android.bakingapp.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by richard.janitor on 30-Dec-17.
 */

public class IngredientItems implements Parcelable{

    public final String ingredient;
    public final String quantity;
    public final String measure;

    public IngredientItems(String ingredient, String quantity, String measure) {
        this.ingredient = ingredient;
        this.quantity = quantity;
        this.measure = measure;
    }

    public IngredientItems(Parcel in) {
        ingredient = in.readString();
        quantity = in.readString();
        measure = in.readString();
    }

    public static final Creator<IngredientItems> CREATOR = new Creator<IngredientItems>() {
        @Override
        public IngredientItems createFromParcel(Parcel in) {
            return new IngredientItems(in);
        }

        @Override
        public IngredientItems[] newArray(int size) {
            return new IngredientItems[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ingredient);
        dest.writeString(quantity);
        dest.writeString(measure);
    }

    public String getIngredient(){
        return ingredient;
    }

}

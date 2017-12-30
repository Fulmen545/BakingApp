package com.riso.android.bakingapp.data;

/**
 * Created by richard.janitor on 30-Dec-17.
 */

public class IngredientItems {

    public final String ingredient;
    public final String quantity;
    public final String measure;

    public IngredientItems(String ingredient, String quantity, String measure) {
        this.ingredient = ingredient;
        this.quantity = quantity;
        this.measure = measure;
    }
}

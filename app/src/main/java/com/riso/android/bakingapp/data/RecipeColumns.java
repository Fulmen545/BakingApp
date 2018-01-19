package com.riso.android.bakingapp.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by richard.janitor on 27-Dec-17.
 */

public class RecipeColumns {
    public static final String CONTENT_AUTHORITY = "com.riso.android.bakingapp";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final class RecipeEntry implements BaseColumns {
        public static final String TABLE_NAME_INGREDIENTS = "ingredients";
        public static final String TABLE_NAME_STEPS = "steps";
        public static final String RECIPE_ID = "recipeid";
        public static final String RECIPE_NAME = "recname";
        public static final String INGRED_ID = "ingredid";
        public static final String QUANTITY = "quantity";
        public static final String MEASURE = "measure";
        public static final String INGREDIENT = "ingredient";
        public static final String STEP_ID = "stepid";
        public static final String STEP_TITLE = "steptitle";
        public static final String DESCRIPTION = "description";
        public static final String URL = "url";
        public static final String THUMBNAIL = "thumbnail";

        public static final Uri CONTENT_URI_INGREDIENTS = BASE_CONTENT_URI.buildUpon()
                .appendPath(TABLE_NAME_INGREDIENTS).build();

        public static final Uri CONTENT_URI_STEPS = BASE_CONTENT_URI.buildUpon()
                .appendPath(TABLE_NAME_STEPS).build();

        public static final String CONTENT_DIR_TYPE_INGREDIENTS =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_NAME_INGREDIENTS;

        public static final String CONTENT_ITEM_TYPE_INGREDIENTS =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_NAME_INGREDIENTS;

        public static final String CONTENT_DIR_TYPE_STEPS =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_NAME_STEPS;

        public static final String CONTENT_ITEM_TYPE_STEPS =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_NAME_STEPS;

        public static Uri buildIngredientsUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI_INGREDIENTS, id);
        }

        public static Uri buildStepsUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI_STEPS, id);
        }
    }
}

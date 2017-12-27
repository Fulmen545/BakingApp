package com.riso.android.bakingapp.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by richard.janitor on 27-Dec-17.
 */

public class RecipeProvider extends ContentProvider {
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private RecipeDbHelper mOpenHelper;

    private static final int RECIPE_ING = 100;
    private static final int RECIPE_ING_WITH_ID = 200;
    private static final int RECIPE_STP = 101;
    private static final int RECIPE_STP_WITH_ID = 201;

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = RecipeColumns.CONTENT_AUTHORITY;

        matcher.addURI(authority, RecipeColumns.RecipeEntry.TABLE_NAME_INGREDIENTS, RECIPE_ING);
        matcher.addURI(authority, RecipeColumns.RecipeEntry.TABLE_NAME_INGREDIENTS + "/#", RECIPE_ING_WITH_ID);
        matcher.addURI(authority, RecipeColumns.RecipeEntry.TABLE_NAME_STEPS, RECIPE_STP);
        matcher.addURI(authority, RecipeColumns.RecipeEntry.TABLE_NAME_STEPS + "/#", RECIPE_STP_WITH_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new RecipeDbHelper(getContext());
        return true;
    }


    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            case RECIPE_STP: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        RecipeColumns.RecipeEntry.TABLE_NAME_STEPS,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                return retCursor;
            }
            case RECIPE_STP_WITH_ID:{
                retCursor = mOpenHelper.getReadableDatabase().query(
                        RecipeColumns.RecipeEntry.TABLE_NAME_STEPS,
                        projection,
                        RecipeColumns.RecipeEntry._ID + " = ?",
                        new String[] {String.valueOf(ContentUris.parseId(uri))},
                        null,
                        null,
                        sortOrder);
                return retCursor;
            }
            case RECIPE_ING: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        RecipeColumns.RecipeEntry.TABLE_NAME_INGREDIENTS,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                return retCursor;
            }
            case RECIPE_ING_WITH_ID:{
                retCursor = mOpenHelper.getReadableDatabase().query(
                        RecipeColumns.RecipeEntry.TABLE_NAME_INGREDIENTS,
                        projection,
                        RecipeColumns.RecipeEntry._ID + " = ?",
                        new String[] {String.valueOf(ContentUris.parseId(uri))},
                        null,
                        null,
                        sortOrder);
                return retCursor;
            }
            default:{
                // By default, we assume a bad URI
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match){
            case RECIPE_STP:{
                return RecipeColumns.RecipeEntry.CONTENT_DIR_TYPE_STEPS;
            }
            case RECIPE_STP_WITH_ID:{
                return RecipeColumns.RecipeEntry.CONTENT_DIR_TYPE_STEPS;
            }
            case RECIPE_ING:{
                return RecipeColumns.RecipeEntry.CONTENT_DIR_TYPE_INGREDIENTS;
            }
            case RECIPE_ING_WITH_ID:{
                return RecipeColumns.RecipeEntry.CONTENT_DIR_TYPE_INGREDIENTS;
            }
            default:{
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        Uri returnUri;
        switch (sUriMatcher.match(uri)) {
            case RECIPE_STP:{
                long _id = db.insert(RecipeColumns.RecipeEntry.TABLE_NAME_STEPS, null, values);
                if (_id > 0) {
                    returnUri = RecipeColumns.RecipeEntry.buildStepsUri(_id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into: " + uri);
                }
                break;
            }
            case RECIPE_ING:{
                long _id = db.insert(RecipeColumns.RecipeEntry.TABLE_NAME_INGREDIENTS, null, values);
                if (_id > 0) {
                    returnUri = RecipeColumns.RecipeEntry.buildIngredientsUri(_id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into: " + uri);
                }
                break;
            }
            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);

            }
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}

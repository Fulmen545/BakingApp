package com.riso.android.bakingapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by richard.janitor on 27-Dec-17.
 */

public class RecipeDbHelper extends SQLiteOpenHelper{
    private static final String DATABASE_NAME = "recipes.db";

    private static final int DATABASE_VERSION = 1;

    public RecipeDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_INGRDIENTS_TABLE = "CREATE TABLE " + RecipeColumns.RecipeEntry.TABLE_NAME_INGREDIENTS + " (" +
                RecipeColumns.RecipeEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                RecipeColumns.RecipeEntry.RECIPE_ID + " TEXT NOT NULL, " +
                RecipeColumns.RecipeEntry.RECIPE_NAME + " TEXT NOT NULL, " +
                RecipeColumns.RecipeEntry.INGRED_ID + " TEXT NOT NULL, " +
                RecipeColumns.RecipeEntry.QUANTITY + " TEXT NOT NULL, " +
                RecipeColumns.RecipeEntry.MEASURE + " TEXT NOT NULL, " +
                RecipeColumns.RecipeEntry.INGREDIENT + " TEXT NOT NULL " +
                "); ";

        final String SQL_CREATE_STEPS_TABLE = "CREATE TABLE " + RecipeColumns.RecipeEntry.TABLE_NAME_STEPS + " (" +
                RecipeColumns.RecipeEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                RecipeColumns.RecipeEntry.RECIPE_ID + " TEXT NOT NULL, " +
                RecipeColumns.RecipeEntry.STEP_ID + " TEXT NOT NULL, " +
                RecipeColumns.RecipeEntry.STEP_TITLE + " TEXT NOT NULL, " +
                RecipeColumns.RecipeEntry.DESCRIPTION + " TEXT NOT NULL, " +
                RecipeColumns.RecipeEntry.URL + " TEXT  " +
                "); ";

        db.execSQL(SQL_CREATE_STEPS_TABLE);
        db.execSQL(SQL_CREATE_INGRDIENTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + RecipeColumns.RecipeEntry.TABLE_NAME_INGREDIENTS);
        db.execSQL("DROP TABLE IF EXISTS " + RecipeColumns.RecipeEntry.TABLE_NAME_STEPS);
        onCreate(db);
    }
}

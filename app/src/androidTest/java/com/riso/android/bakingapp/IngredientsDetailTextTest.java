package com.riso.android.bakingapp;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.riso.android.bakingapp.TestUtils.withRecyclerView;

/**
 * Created by richard.janitor on 19-Jan-18.
 */

@RunWith(AndroidJUnit4.class)
public class IngredientsDetailTextTest {
    public static final String RECIPE_NAME = "Cheesecake";
    public static final String STEP_NAME = "Ingredients";
    public static final String INGRED_DESC = "cream cheese, softened";
    public static final String INGRED_QUAN = "680";
    public static final String INGRED_MEAS = "G";

    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    private IdlingResource mIdlingResource;

    @Before
    public void registerIdlingResource(){
        mIdlingResource = mainActivityActivityTestRule.getActivity().getIdlingResource();
        Espresso.registerIdlingResources(mIdlingResource);
    }

    @Test
    public void IngredientDetailTextTest(){
        onView(withRecyclerView(R.id.rv_recipes)
                .atPositionOnView(3, R.id.recipeTitle))
                .check(matches(withText(RECIPE_NAME)));

        onView(withId(R.id.rv_recipes)).perform(
                RecyclerViewActions.actionOnItemAtPosition(3, click()));

        onView(withRecyclerView(R.id.rv_recipes)
                .atPositionOnView(0, R.id.recipeTitle))
                .check(matches(withText(STEP_NAME)));

        onView(withId(R.id.rv_recipes)).perform(
                RecyclerViewActions.actionOnItemAtPosition(0, click()));

        onView(withRecyclerView(R.id.detail_rv)
                .atPositionOnView(5, R.id.ingredient_name_tv))
                .check(matches(withText(INGRED_DESC)));

        onView(withRecyclerView(R.id.detail_rv)
                .atPositionOnView(5, R.id.quantity_tv))
                .check(matches(withText(INGRED_QUAN)));

        onView(withRecyclerView(R.id.detail_rv)
                .atPositionOnView(5, R.id.measure_tv))
                .check(matches(withText(INGRED_MEAS)));

    }

    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            Espresso.unregisterIdlingResources(mIdlingResource);
        }
    }

}

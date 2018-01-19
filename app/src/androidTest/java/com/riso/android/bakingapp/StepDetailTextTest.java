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
public class StepDetailTextTest {
    public static final String RECIPE_NAME = "Brownies";
    public static final String STEP_NAME = "Step 3: Add sugars to wet mixture.";
    public static final String STEP_DESC = "3. Mix both sugars into the melted chocolate in a large mixing bowl until mixture is smooth and uniform.";

    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    private IdlingResource mIdlingResource;

    @Before
    public void registerIdlingResource(){
        mIdlingResource = mainActivityActivityTestRule.getActivity().getIdlingResource();
        Espresso.registerIdlingResources(mIdlingResource);
    }

    @Test
    public void StepDetailTextTest(){
        onView(withRecyclerView(R.id.rv_recipes)
                .atPositionOnView(1, R.id.recipeTitle))
                .check(matches(withText(RECIPE_NAME)));

        onView(withId(R.id.rv_recipes)).perform(
                RecyclerViewActions.actionOnItemAtPosition(1, click()));

        onView(withRecyclerView(R.id.rv_recipes)
                .atPositionOnView(4, R.id.recipeTitle))
                .check(matches(withText(STEP_NAME)));

        onView(withId(R.id.rv_recipes)).perform(
                RecyclerViewActions.actionOnItemAtPosition(4, click()));

        onView((withId(R.id.stepDesc))).check(matches(withText(STEP_DESC)));

    }

    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            Espresso.unregisterIdlingResources(mIdlingResource);
        }
    }

}

package com.riso.android.bakingapp;

/**
 * Created by richard.janitor on 19-Jan-18.
 */

public class TestUtils {
    public static RecyclerViewMatcher withRecyclerView(final int recyclerViewId) {

        return new RecyclerViewMatcher(recyclerViewId);
    }
}

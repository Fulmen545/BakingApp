package com.riso.android.bakingapp.data;

/**
 * Created by richard.janitor on 30-Dec-17.
 */

public class StepItems {
    public final String stepTitle;
    public final String description;
    public final String url;

    public StepItems(String stepTitle, String description, String url) {
        this.stepTitle = stepTitle;
        this.description = description;
        this.url = url;
    }
}

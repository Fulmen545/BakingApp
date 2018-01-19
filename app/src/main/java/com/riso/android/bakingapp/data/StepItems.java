package com.riso.android.bakingapp.data;

/**
 * Created by richard.janitor on 30-Dec-17.
 */

public class StepItems {
    public final String stepTitle;
    public final String description;
    public final String url;
    public final String thumbnail;

    public StepItems(String stepTitle, String description, String url, String thumbnail) {
        this.stepTitle = stepTitle;
        this.description = description;
        this.url = url;
        this.thumbnail = thumbnail;
    }
}

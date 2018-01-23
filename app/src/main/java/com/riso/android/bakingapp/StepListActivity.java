package com.riso.android.bakingapp;

import android.os.PersistableBundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

public class StepListActivity extends AppCompatActivity {
    private static final String RECEPT_POSITION = "recept_position";
    private static final String POSITION = "position";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean tabletSize = getResources().getBoolean(R.bool.isTablet);
        if (tabletSize) {
            Bundle bundle = this.getIntent().getExtras();
            bundle.putString(RECEPT_POSITION, Integer.toString(bundle.getInt(POSITION, 0)));
            FragmentManager fragmentManager = getSupportFragmentManager();
            setContentView(R.layout.step_list_tablet);
            StepListFragment stepFragment = new StepListFragment();
            stepFragment.setArguments(bundle);
            DetailFragment detailFragment = new DetailFragment();
            detailFragment.setArguments(bundle);
            // Add the fragment to its container using a transaction
            if (fragmentManager.findFragmentByTag("tagstep")==null) {
                fragmentManager.beginTransaction()
                        .add(R.id.step_list_container, stepFragment, "tagstep")
                        .commit();
            }

            if (savedInstanceState == null) {
                if (fragmentManager.findFragmentByTag("tag1")==null) {
                    fragmentManager.beginTransaction()
                            .add(R.id.detail_container, detailFragment, "tag1")
                            .commit();
                }
            } else {
                fragmentManager.findFragmentByTag("tag1");
            }
        } else {
            Bundle bundle = this.getIntent().getExtras();
            StepListFragment fragment = new StepListFragment();
            fragment.setArguments(bundle);
            FragmentManager fragmentManager = getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction ft = fragmentManager.beginTransaction();
            if (fragmentManager.findFragmentByTag("tag3")==null) {
                ft.add(android.R.id.content, fragment, "tag3").commit();
            } else {
                fragmentManager.findFragmentByTag("tag3");
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }
}

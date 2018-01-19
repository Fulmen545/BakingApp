package com.riso.android.bakingapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getIntent().getExtras();
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (savedInstanceState == null) {
            android.support.v4.app.FragmentTransaction ft = fragmentManager.beginTransaction();
            DetailFragment df = new DetailFragment();
            df.setArguments(bundle);
            ft.add(android.R.id.content, df).commit();
        } else {
            fragmentManager.findFragmentByTag("tag1");
        }

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
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

}
